package com.example.schoolhub.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolhub.MainActivity;
import com.example.schoolhub.Notifications;
import com.example.schoolhub.R;
import com.example.schoolhub.RetrofitInterface;
import com.example.schoolhub.data.Notification;
import com.example.schoolhub.data.PreferenceData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.cometchat.pro.uikit.ui_components.shared.cometchatReaction.fragment.FragmentReactionObject.TAG;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    List<Notification> notificationList= new ArrayList<>();
    Context context;
    Retrofit retrofit;
    RetrofitInterface retrofitInterface;
    public NotificationAdapter(List<Notification> notification, Notifications notifications) {
        this.notificationList=notification;
        this.context=notifications;
    }

    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.frag_notification,parent,false);
        NotificationAdapter.ViewHolder viewHolder = new NotificationAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, int position) {
        holder.titleNoti.setText(notificationList.get(position).getNotificationType());
        holder.subTitleNoti.setText(notificationList.get(position).getText());
        holder.hideNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> map = new HashMap<>();

                map.put("notificationID",notificationList.get(position).getNotificationID());

                Call<Void> callin = retrofitInterface.deleteNoti(PreferenceData.getLoggedInUserData(context).get("userID"),map);
                callin.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            notificationList.remove(position);
                            notifyDataSetChanged();
                            Log.d(TAG, "Noti Removed: ");
                        }else {
                            Log.d(TAG, "Notification Remove Err: "+response.code());
                        }
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.d(TAG, "Notification Connection Err: "+t);
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleNoti,subTitleNoti;
        ImageView hideNotification;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.titleNoti=itemView.findViewById(R.id.titleNoti);
            this.subTitleNoti=itemView.findViewById(R.id.subTitleNoti);
            this.hideNotification=itemView.findViewById(R.id.hideNotification);
            //retrofit
            retrofit = new Retrofit.Builder()
                    .baseUrl(MainActivity.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            retrofitInterface = retrofit.create(RetrofitInterface.class);
        }
    }
}
