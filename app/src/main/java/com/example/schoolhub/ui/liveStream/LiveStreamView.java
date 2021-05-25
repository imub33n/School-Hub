package com.example.schoolhub.ui.liveStream;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bambuser.broadcaster.BroadcastPlayer;
import com.bambuser.broadcaster.PlayerState;
import com.bambuser.broadcaster.SurfaceViewWithAutoAR;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.TextMessage;
import com.example.schoolhub.R;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import android.graphics.Point;
import android.view.Display;
import android.widget.MediaController;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

public class LiveStreamView extends AppCompatActivity {
    SurfaceViewWithAutoAR mVideoSurface;
    TextView mPlayerStatusTextView;
    EditText msg_box;
    ImageView send_msg_button;
    View mPlayerContentView;
    private static final String APPLICATION_ID = "Qj6nLAnk5oKeMua2ChtVlQ";
    private static final String API_KEY = "SnGkNLdLDAg1X6XHew1XbL";
    OkHttpClient mOkHttpClient = new OkHttpClient();
    BroadcastPlayer mBroadcastPlayer;
    Display mDefaultDisplay;
    MediaController mMediaController = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_stream_view);
        mVideoSurface = findViewById(R.id.VideoSurfaceView);
        mPlayerStatusTextView = findViewById(R.id.PlayerStatusTextView);
        mPlayerContentView = findViewById(R.id.PlayerContentView);
        //chat
        send_msg_button = findViewById(R.id.send_msg_button);
        msg_box = findViewById(R.id.msg_box);
        //Sizing the video surface
        mDefaultDisplay = getWindowManager().getDefaultDisplay();
        send_msg_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!msg_box.getText().toString().isEmpty()){
                    String receiverID = getIntent().getStringExtra("EXTRA_School_Id");
                    String messageText = msg_box.getText().toString();
                    String receiverType = CometChatConstants.RECEIVER_TYPE_USER;

                    TextMessage textMessage = new TextMessage(receiverID, messageText, receiverType);

                    CometChat.sendMessage(textMessage, new CometChat.CallbackListener <TextMessage> () {
                        @Override
                        public void onSuccess(TextMessage textMessage) {
                            Log.d(TAG, "Message sent successfully: " + textMessage.toString());
                            msg_box.setText("");
                            closeKeyboard();
                        }
                        @Override
                        public void onError(CometChatException e) {
                            Log.d(TAG, "Message sending failed with exception: " + e.getMessage());
                        }
                    });
                }else{
                    Toast.makeText(LiveStreamView.this, "Empty comment box!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    private Point getScreenSize() {
        if (mDefaultDisplay == null)
            mDefaultDisplay = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        try {
            // this is officially supported since SDK 17 and said to work down to SDK 14 through reflection,
            // so it might be everything we need.
            mDefaultDisplay.getClass().getMethod("getRealSize", Point.class).invoke(mDefaultDisplay, size);
        } catch (Exception e) {
            // fallback to approximate size.
            mDefaultDisplay.getSize(size);
        }
        return size;
    }
    BroadcastPlayer.Observer mBroadcastPlayerObserver = new BroadcastPlayer.Observer() {
        @Override
        public void onStateChange(PlayerState playerState) {
            if (mPlayerStatusTextView != null){
                if(playerState == PlayerState.PLAYING){
                    mPlayerStatusTextView.setVisibility(View.GONE);
                }
                if(playerState == PlayerState.COMPLETED){
                    onBackPressed();
                }
                mPlayerStatusTextView.setText("Status: " + playerState);
            }
            //set media controls
            if (playerState == PlayerState.PLAYING || playerState == PlayerState.PAUSED || playerState == PlayerState.COMPLETED) {
                if (mMediaController == null && mBroadcastPlayer != null && !mBroadcastPlayer.isTypeLive()) {
                    mMediaController = new MediaController(getApplicationContext());
                    mMediaController.setAnchorView(mPlayerContentView);
                    mMediaController.setMediaPlayer(mBroadcastPlayer);
                }
//                if (mMediaController != null) {
//                    mMediaController.setEnabled(true);
//                    mMediaController.show();
//                }
            } else if (playerState == PlayerState.ERROR || playerState == PlayerState.CLOSED) {
                if (mMediaController != null) {
                    mMediaController.setEnabled(false);
                    mMediaController.hide();
                }
                mMediaController = null;
            }
        }
        @Override
        public void onBroadcastLoaded(boolean live, int width, int height) {
            Point size = getScreenSize();
            float screenAR = size.x / (float) size.y;
            float videoAR = width / (float) height;
            float arDiff = screenAR - videoAR;
            mVideoSurface.setCropToParent(Math.abs(arDiff) < 0.2);
        }
    };
    @Override
    protected void onPause() {
        super.onPause();
        mOkHttpClient.dispatcher().cancelAll();
        mVideoSurface = null;
        if (mBroadcastPlayer != null)
            mBroadcastPlayer.close();
        mBroadcastPlayer = null;
        if (mMediaController != null)
            mMediaController.hide();
        mMediaController = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mVideoSurface = findViewById(R.id.VideoSurfaceView);
        mPlayerStatusTextView.setText("Loading broadcast");
        getLatestResourceUri();
    }

    void getLatestResourceUri() {
        runOnUiThread(new Runnable() { @Override public void run() {
            initPlayer(getIntent().getStringExtra("EXTRA_RESOURCE_URI"));
        }});
//        Request request = new Request.Builder()
//                .url("https://api.bambuser.com/broadcasts")
//                .addHeader("Accept", "application/vnd.bambuser.v1+json")
//                .addHeader("Content-Type", "application/json")
//                .addHeader("Authorization", "Bearer " + API_KEY)
//                .get()
//                .build();
//        mOkHttpClient.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(final Call call, final IOException e) {
//                runOnUiThread(new Runnable() { @Override public void run() {
//                    if (mPlayerStatusTextView != null)
//                        mPlayerStatusTextView.setText("Http exception: " + e);
//                }});
//            }
//            @Override
//            public void onResponse(final Call call, final Response response) throws IOException {
//                String body = response.body().string();
//                String resourceUri = null;
//                try {
//                    JSONObject json = new JSONObject(body);
//                    JSONArray results = json.getJSONArray("results");
//                    JSONObject latestBroadcast = results.optJSONObject(0);
//                    resourceUri = latestBroadcast.optString("resourceUri");
//                } catch (Exception ignored) {}
//                final String uri = resourceUri;
//                runOnUiThread(new Runnable() { @Override public void run() {
//                    initPlayer(uri);
//                }});
//            }
//        });
    }

    void initPlayer(String resourceUri) {
        if (resourceUri == null) {
            if (mPlayerStatusTextView != null)
                mPlayerStatusTextView.setText("Could not get the broadcast");
            return;
        }
        if (mVideoSurface == null) {
            // UI no longer active
            return;
        }
        if (mBroadcastPlayer != null)
            mBroadcastPlayer.close();
        mBroadcastPlayer = new BroadcastPlayer(this, resourceUri, APPLICATION_ID, mBroadcastPlayerObserver);
        mBroadcastPlayer.setSurfaceView(mVideoSurface);
        mBroadcastPlayer.load();
    }
}