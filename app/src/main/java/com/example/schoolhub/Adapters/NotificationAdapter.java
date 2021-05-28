package com.example.schoolhub.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolhub.Notifications;
import com.example.schoolhub.R;
import com.example.schoolhub.data.Notification;

import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    List<Notification> notificationList= new ArrayList<>();
    Context context;

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
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleNoti,subTitleNoti;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.titleNoti=itemView.findViewById(R.id.titleNoti);
            this.subTitleNoti=itemView.findViewById(R.id.subTitleNoti);
        }
    }
}
