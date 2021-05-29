package com.example.schoolhub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.schoolhub.Adapters.NotificationAdapter;
import com.example.schoolhub.data.LoginResult;
import com.example.schoolhub.data.PreferenceData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;

public class Notifications extends AppCompatActivity {

    NotificationAdapter notificationAdapter;
    RecyclerView recycler_notification;
    TextView status_notification;
    Toolbar navNoti;

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        status_notification=findViewById(R.id.status_notification);
        recycler_notification= findViewById(R.id.recycler_notification);
        navNoti= findViewById(R.id.navNoti);
        navNoti.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                onBackPressed();
            }
        });
        recycler_notification.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        //retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);

        //getting Notifications
        Call<List<LoginResult>> call2 = retrofitInterface.userData(PreferenceData.getLoggedInUserData(Notifications.this).get("userID"));
        call2.enqueue(new Callback<List<LoginResult>>() {
            @Override
            public void onResponse(Call<List<LoginResult>> call, Response<List<LoginResult>> response) {
                if (response.code() == 200) {
                    //setData
                    if(response.body().get(0).getNotification().size()>0){
                        notificationAdapter = new NotificationAdapter(response.body().get(0).getNotification(),Notifications.this);
                        recycler_notification.setAdapter(notificationAdapter);
                    }else{
                        status_notification.setText("No New Notifications");
                    }

                }else {
                    Log.d(TAG, "onResponse: "+response.code());
                }

            }
            @Override
            public void onFailure(Call<List<LoginResult>> call, Throwable t) {
                Log.d(TAG, "onResponse Err: "+t);
            }
        });

    }
}