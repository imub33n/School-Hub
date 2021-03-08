package com.example.schoolhub.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolhub.R;
import com.example.schoolhub.data.LiveStreamRequests;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class LivestreamRequestsAdapter extends RecyclerView.Adapter<LivestreamRequestsAdapter.ViewHolder>{
    List<LiveStreamRequests> liveStreamRequests;
    Context context;
    public LivestreamRequestsAdapter(List<LiveStreamRequests> body, Context context) {
        this.liveStreamRequests=body;
        this.context=context;
    }

    @NonNull
    @Override
    public LivestreamRequestsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_livestream_requests,parent,false);
        LivestreamRequestsAdapter.ViewHolder viewHolder = new LivestreamRequestsAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull LivestreamRequestsAdapter.ViewHolder holder, int position) {

        holder.nameSkolStreamRequest.setText(liveStreamRequests.get(position).getSchoolName());
        holder.titleStreamRequest.setText(liveStreamRequests.get(position).getTitle());
        String dateNtime="On "+liveStreamRequests.get(position).getDate()+" FROM "+ liveStreamRequests.get(position).getStartTime()+" TO "+ liveStreamRequests.get(position).getEndTime();
        holder.timeDateStreamRequest.setText(dateNtime);
        holder.statusStreamRequest.setText(liveStreamRequests.get(position).getStatus());
    }

    @Override
    public int getItemCount() {
        return liveStreamRequests.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameSkolStreamRequest,titleStreamRequest,timeDateStreamRequest;
        Button statusStreamRequest;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.nameSkolStreamRequest=itemView.findViewById(R.id.nameSkolStreamRequest);
            this.titleStreamRequest=itemView.findViewById(R.id.titleStreamRequest);
            this.timeDateStreamRequest=itemView.findViewById(R.id.timeDateStreamRequest);
            this.statusStreamRequest=itemView.findViewById(R.id.statusStreamRequest);
        }
    }
}
