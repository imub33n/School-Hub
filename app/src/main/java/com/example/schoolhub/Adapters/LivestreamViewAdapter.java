package com.example.schoolhub.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolhub.R;
import com.example.schoolhub.data.LiveStreamRequests;
import com.example.schoolhub.ui.liveStream.LiveStreamView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

public class LivestreamViewAdapter extends RecyclerView.Adapter<LivestreamViewAdapter.ViewHolder>{
    Context context;
    List<LiveStreamRequests> liveStreams;
    DateTimeFormatter df = DateTimeFormatter.ofPattern("d/M/yyyy");
    LocalDate dateCurrent = LocalDate.parse(df.format(LocalDateTime.now()), df);

    public LivestreamViewAdapter(List<LiveStreamRequests> myLiveStreams, Context context) {
        Collections.reverse(myLiveStreams);
        this.liveStreams=myLiveStreams;
        this.context=context;
    }

    @NonNull
    @Override
    public LivestreamViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_livestreams_view,parent,false);
        LivestreamViewAdapter.ViewHolder viewHolder = new LivestreamViewAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull LivestreamViewAdapter.ViewHolder holder, int position) {
        holder.titleStream.setText(liveStreams.get(position).getTitle());
        holder.nameSkolStream.setText(liveStreams.get(position).getSchoolName());

        LocalDate dateStream = LocalDate.parse(liveStreams.get(position).getDate(), df);
        if(dateStream.isEqual(dateCurrent)){
            if(liveStreams.get(position).getLive()){
                holder.timeDateStreams.setText("Started");
                holder.bgStreamList.setBackgroundColor(context.getResources().getColor(R.color.green_600));
            }else{
                holder.timeDateStreams.setText("Starts Today at "+ liveStreams.get(position).getStartTime());
            }
        }else{
            holder.timeDateStreams.setText("On "+liveStreams.get(position).getDate()+" At "+liveStreams.get(position).getStartTime());
        }
        holder.bgStreamList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dateStream.isEqual(dateCurrent)){
                    Intent it = new Intent( context , LiveStreamView.class);
                    it.putExtra("EXTRA_RESOURCE_URI", liveStreams.get(position).getResourceURI());
                    context.startActivity(it);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return liveStreams.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleStream,nameSkolStream,timeDateStreams;
        LinearLayout bgStreamList;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleStream=itemView.findViewById(R.id.titleStream);
            nameSkolStream=itemView.findViewById(R.id.nameSkolStream);
            timeDateStreams=itemView.findViewById(R.id.timeDateStreams);
            bgStreamList=itemView.findViewById(R.id.bgStreamList);

        }
    }

}