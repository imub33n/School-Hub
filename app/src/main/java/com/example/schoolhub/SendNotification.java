package com.example.schoolhub;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.schoolhub.data.Notification;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.cometchat.pro.uikit.ui_components.shared.cometchatReaction.fragment.FragmentReactionObject.TAG;

public class SendNotification {
    Notification notification= new Notification();
    String receiverID;
    public SendNotification(String title, String subTitle, String senderID, String receiverId){

        this.notification.setText(subTitle);
        this.notification.setNotificationType(title);
        this.notification.setUserID(senderID);
        this.receiverID =receiverId;
        //retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);
        Call<Void> callin = retrofitInterface.sendNotification(receiverID,notification);
        callin.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    Log.d(TAG, "Notification sent to : "+receiverID);
                }else {
                    Log.d(TAG, "Notification Err: "+response.code());
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d(TAG, "Notification Connection Err: "+t);
            }
        });
    }



}
