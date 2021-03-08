package com.bambuser.broadcaster.example;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;

import com.bambuser.broadcaster.BackendApi;
import com.bambuser.broadcaster.ModernTlsSocketFactory;

import okhttp3.CacheControl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.util.Pair;

final class UploadHelper {
	private static final String LOGTAG = "UploadHelper";

	static {
		OkHttpClient.Builder builder = new OkHttpClient.Builder()
			.connectTimeout(10, TimeUnit.SECONDS)
			.readTimeout(60, TimeUnit.SECONDS)
			.writeTimeout(10, TimeUnit.SECONDS);
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
			builder.sslSocketFactory(ModernTlsSocketFactory.getInstance(), ModernTlsSocketFactory.getTrustManager());
		}
		HTTP_CLIENT = builder.build();
	}

	/** Implement the ProgressCallback interface to know when an upload is done
	 * or failed, and to get regular callbacks about progress while uploading.
	 * These methods may be invoked on a worker thread. */
	interface ProgressCallback {
		void onSuccess(String fileName);
		void onError(String error);
		/** Called regularly while uploading, to inform the observer about progress,
		 * and to check whether uploading is still desired or should be aborted.
		 * @param currentBytes The number of bytes sent so far
		 * @param totalBytes The number of bytes which should be sent
		 * @return Return true if the upload should continue, false to abort. */
		boolean onProgress(long currentBytes, long totalBytes);
	}

	/** Helper method for uploading data on a worker thread.
	 * Uploading files is only possible using an applicationId with special rights.
	 * @param context {@link Context} used for resolving the Uri and reading metadata.
	 * @param uri {@link Uri} to the media data (file) that should be uploaded.
	 * @param applicationId Secret Bambuser-provided application specific ID.
	 * @param author Author for this particular file, optional.
	 * @param title Title/description for this file, optional.
	 * @param location Location for this file, optional.
	 * @param cb An object implementing the {@link ProgressCallback} interface.
	 */
	static void upload(final Context context, final Uri uri, final String applicationId, final String author, final String title, final Location location, final ProgressCallback cb) {
		new Thread("UploadHelperThread") { @Override public void run() {
			final ContentResolver cr = context.getContentResolver();
			final String mimeType = cr.getType(uri);
			if (mimeType == null || mimeType.indexOf("/") < 0) {
				cb.onError("unsupported media type: " + mimeType);
				return;
			}
			final String type;
			if (mimeType.startsWith("video"))
				type = "video";
			else if (mimeType.startsWith("image"))
				type = "image";
			else {
				cb.onError("unsupported media type: " + mimeType);
				return;
			}
			final InputStream inputStream;
			try {
				inputStream = cr.openInputStream(uri);
			} catch (Exception e) {
				cb.onError("could not open " + uri);
				return;
			}
			if (inputStream == null) {
				cb.onError("could not open " + uri);
				return;
			}

			final long dateTaken = getDateTaken(cr, uri);
			String[] proj = {OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE};
			Cursor cursor = cr.query(uri, proj, null, null, null);
			cursor.moveToFirst();
			final String displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
			// openableSize is sometimes off by a few bytes at least on kitkat. not reliable
			// final long openableSize = cursor.getLong(cursor.getColumnIndex(OpenableColumns.SIZE));
			final String fileName;
			if (displayName != null && displayName.length() > 1) {
				fileName = displayName;
			} else {
				String suffix = mimeType.substring(mimeType.indexOf("/") + 1);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.US);
				fileName = "upload_" + sdf.format(new Date()) + "." + suffix;
			}
			cursor.close();

			final long fileSize;
			try {
				ParcelFileDescriptor pfd = cr.openFileDescriptor(uri, "r");
				fileSize = pfd.getStatSize();
				pfd.close();
			} catch (Exception e) {
				cb.onError("Unable to determine data size");
				return;
			}
			if (fileSize < 1024) {
				cb.onError("File has no content");
				return;
			}

			HashMap<String, String> params = new HashMap<>();
			params.put(BackendApi.TICKET_FILE_NAME, fileName);
			params.put(BackendApi.TICKET_FILE_TYPE, type);
			if (dateTaken > 0)
				params.put(BackendApi.TICKET_FILE_CREATED, String.valueOf(dateTaken / 1000));
			if (author != null && author.length() > 0)
				params.put(BackendApi.TICKET_FILE_AUTHOR, author);
			if (title != null && title.length() > 0)
				params.put(BackendApi.TICKET_FILE_TITLE, title);
			if (location != null) {
				params.put(BackendApi.TICKET_FILE_POS_LAT, String.valueOf(location.getLatitude()));
				params.put(BackendApi.TICKET_FILE_POS_LON, String.valueOf(location.getLongitude()));
				params.put(BackendApi.TICKET_FILE_POS_ACCURACY, String.valueOf(location.getAccuracy()));
			}

			Pair<Integer, String> pair = BackendApi.getUploadTicketForApplicationId(context, params, applicationId);
			String response = pair.second;
			JSONObject ticket = getJsonObjectFromString(response);
			if (ticket == null || !ticket.optString("upload_url").contains("://")) {
				String error = "Could not get ticket for uploading.";
				if (response == null || response.length() <= 0)
					error += " No response from server.";
				else
					error += " Unexpected server response. http code " + pair.first;
				cb.onError(error);
				return;
			}
			String uploadUrl = ticket.optString("upload_url");

			RequestBody requestBody = new RequestBody() {
				@Override public MediaType contentType() {
					return MediaType.parse(mimeType);
				}
				@Override public long contentLength() throws IOException {
					return fileSize;
				}
				@Override public void writeTo(BufferedSink sink) throws IOException {
					long sent = 0;
					byte [] buffer = new byte[65536];
					int bytesRead = 0;
					boolean running = true;
					while (running && (bytesRead = inputStream.read(buffer)) != -1) {
						sink.write(buffer, 0, bytesRead);
						sent += bytesRead;
						running = cb.onProgress(sent, fileSize);
					}
				}
			};

			int responseCode = 0;
			try {
				Request request = new Request.Builder().url(uploadUrl)
					.cacheControl(new CacheControl.Builder().noCache().noStore().build())
					.put(requestBody).build();
				Response res = HTTP_CLIENT.newCall(request).execute();
				responseCode = res.code();
				res.body().close();
			} catch (Exception e) {
				Log.w(LOGTAG, "Exception when doing PUT: " + e);
			}
			try {
				inputStream.close();
			} catch (Exception e) {}

			if (responseCode == 200)
				cb.onSuccess(fileName);
			else
				cb.onError("upload of " + fileName + " failed with code " + responseCode);
		}}.start();
	}

	private static long getDateTaken(ContentResolver cr, Uri uri) {
		// for modern document Uris, rely on Document.COLUMN_LAST_MODIFIED
		String columnName = DocumentsContract.Document.COLUMN_LAST_MODIFIED;
		if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme()) && MediaStore.AUTHORITY.equals(uri.getAuthority())) {
			// we have a MediaStore Uri and should query for the DATE_TAKEN column if possible
			String mimeType = cr.getType(uri);
			if (mimeType != null && mimeType.startsWith("image"))
				columnName = MediaStore.Images.ImageColumns.DATE_TAKEN;
			else if (mimeType != null && mimeType.startsWith("video"))
				columnName = MediaStore.Video.VideoColumns.DATE_TAKEN;
			else
				columnName = MediaStore.MediaColumns.DATE_MODIFIED;
		}
		String[] projection = {columnName};
		long dateTaken = 0;
		try {
			Cursor cursor = cr.query(uri, projection, null, null, null);
			if (cursor != null) {
				if (cursor.moveToFirst())
					dateTaken = cursor.getLong(cursor.getColumnIndex(columnName));
				cursor.close();
			}
		} catch (Exception e) {}
		if (MediaStore.MediaColumns.DATE_MODIFIED.equals(columnName))
			dateTaken *= 1000;
		return dateTaken;
	}

	/** Convenience method for creating a JSONObject from a String.
	 * @param string A String, preferably containing JSON.
	 * @return A {@link JSONObject} or null. */
	private static JSONObject getJsonObjectFromString(String string) {
		if (string != null && string.length() > 0) try {
			return new JSONObject(string);
		} catch (final JSONException e) {
			Log.w(LOGTAG, "exception in json parsing: " + e);
		}
		return null;
	}

	private final static OkHttpClient HTTP_CLIENT;
}
