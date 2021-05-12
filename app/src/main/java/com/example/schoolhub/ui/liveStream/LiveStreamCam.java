 package com.example.schoolhub.ui.liveStream;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.SurfaceView;
import com.bambuser.broadcaster.BroadcastStatus;
import com.bambuser.broadcaster.Broadcaster;
import com.bambuser.broadcaster.CameraError;
import com.bambuser.broadcaster.ConnectionError;
import com.example.schoolhub.MainActivity;
import com.example.schoolhub.R;
import com.example.schoolhub.RetrofitInterface;
import com.example.schoolhub.UserProfile;
import com.example.schoolhub.data.LiveStreamRequests;
import com.example.schoolhub.data.PreferenceData;

import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;

public class LiveStreamCam extends AppCompatActivity {
    SurfaceView mPreviewSurface;
    private static final String APPLICATION_ID = "ddd56SuCBOa2vv6JoGwZ0g";
    private static final String API_KEY = "VyUHQSKdTjr4Merap5T4SE";
    Broadcaster mBroadcaster;
    Button mBroadcastButton;

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    LiveStreamRequests liveStreamURIStatus= new LiveStreamRequests();

    OkHttpClient mOkHttpClient = new OkHttpClient();
    String resourceUri = null;
    LinearLayout liveStreamView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_stream_cam);
        liveStreamView= findViewById(R.id.liveStreamView);
        mPreviewSurface = findViewById(R.id.PreviewSurfaceView);
        mBroadcaster = new Broadcaster(this, APPLICATION_ID, mBroadcasterObserver);
        mBroadcaster.setRotation(getWindowManager().getDefaultDisplay().getRotation());
        mBroadcastButton = findViewById(R.id.BroadcastButton);

        retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);

        mBroadcastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBroadcaster.canStartBroadcasting()){
                    liveStreamView.setVisibility(View.INVISIBLE);
                    mBroadcaster.startBroadcast();
                }
                else{
                    liveStreamView.setVisibility(View.VISIBLE);
                    mBroadcaster.stopBroadcast();
                }
            }
        });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mBroadcaster.onActivityDestroy();
    }
    @Override
    public void onPause() {
        super.onPause();
        mBroadcaster.onActivityPause();
    }
    private Broadcaster.Observer mBroadcasterObserver = new Broadcaster.Observer() {
        @Override
        public void onConnectionStatusChange(BroadcastStatus broadcastStatus) {
            Log.i("Mybroadcastingapp", "Received status change: " + broadcastStatus);
            mBroadcastButton.setText(broadcastStatus == BroadcastStatus.IDLE ? "START STREAMING" : "END STREAMING");
            if (broadcastStatus == BroadcastStatus.STARTING){
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
            if (broadcastStatus == BroadcastStatus.IDLE){
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
        }
        @Override
        public void onStreamHealthUpdate(int i) {
        }
        @Override
        public void onConnectionError(ConnectionError connectionError, String s) {
            Log.w("Mybroadcastingapp", "Received connection error: " + connectionError + ", " + s);
        }
        @Override
        public void onCameraError(CameraError cameraError) {
            Toast.makeText(getApplicationContext(),"Could not open camera",Toast.LENGTH_LONG).show();
        }
        @Override
        public void onChatMessage(String s) {
            Toast.makeText(getApplicationContext(),""+s,Toast.LENGTH_LONG).show();
        }
        @Override
        public void onResolutionsScanned() {
        }
        @Override
        public void onCameraPreviewStateChanged() {
        }
        @Override
        public void onBroadcastInfoAvailable(String s, String s1) {
        }
        @Override
        public void onBroadcastIdAvailable(String s) {
            Log.d(TAG, "___________________________onBroadcastIdAvailable: "+s);
            Request request = new Request.Builder()
                    .url("https://api.bambuser.com/broadcasts/"+s)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/vnd.bambuser.v1+json")
                    .addHeader("Authorization", "Bearer " + API_KEY)
                    .get()
                    .build();
            mOkHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(final Call call, final IOException e) {
                    Toast.makeText(getApplicationContext(),"onFailure:"+e,Toast.LENGTH_LONG).show();
                }
                @Override
                public void onResponse(final Call call, final Response response) throws IOException {
                    String body = response.body().string();

                    Log.d(TAG, "_____________body______________onBody: "+body);
                    try {
                        JSONObject json = new JSONObject(body);
                        resourceUri=json.optString("resourceUri");

                        liveStreamURIStatus.setResourceURI(resourceUri);
                        liveStreamURIStatus.setLive(true);
                        //path request here for URi
                        retrofit2.Call<Void> called = retrofitInterface.streamURIPatch(getIntent().getExtras().getString("StreamID"),liveStreamURIStatus);
                        called.enqueue(new retrofit2.Callback<Void>() {
                            @Override
                            public void onResponse(retrofit2.Call<Void> call, retrofit2.Response<Void> response) {
                                if(!response.isSuccessful()){
                                    Toast.makeText(LiveStreamCam.this, "URi Update error: "+response.code(), Toast.LENGTH_LONG).show();
                                }
                                Toast.makeText(LiveStreamCam.this, "URi Updated", Toast.LENGTH_LONG).show();
                            }
                            @Override
                            public void onFailure(retrofit2.Call<Void> call, Throwable t) {
                                Toast.makeText(LiveStreamCam.this, "URi Update error: "+t.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    } catch (Exception ignored) {}
                    Log.d(TAG, "_____________resourceUri______________onBody: "+resourceUri);
                }
            });
        }
    };
    @Override
    public void onResume() {
        super.onResume();
        if (!hasPermission(Manifest.permission.CAMERA)
                && !hasPermission(Manifest.permission.RECORD_AUDIO))
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO}, 1);
        else if (!hasPermission(Manifest.permission.RECORD_AUDIO))
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.RECORD_AUDIO}, 1);
        else if (!hasPermission(Manifest.permission.CAMERA))
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, 1);

        mBroadcaster.setCameraSurface(mPreviewSurface);
        mBroadcaster.onActivityResume();
        mBroadcaster.setRotation(getWindowManager().getDefaultDisplay().getRotation());
    }

    private boolean hasPermission(String permission) {
        return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public void goBackFromLivestream(View view) {
        onBackPressed();
    }
}
