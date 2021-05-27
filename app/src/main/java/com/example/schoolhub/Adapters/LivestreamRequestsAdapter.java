package com.example.schoolhub.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolhub.R;
import com.example.schoolhub.data.LiveStreamRequests;
import com.example.schoolhub.data.PreferenceData;
import com.example.schoolhub.ui.liveStream.LiveStreamAdminFragment;
import com.example.schoolhub.ui.liveStream.LiveStreamCam;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static com.cometchat.pro.uikit.ui_components.shared.cometchatReaction.fragment.FragmentReactionObject.TAG;

public class LivestreamRequestsAdapter extends RecyclerView.Adapter<LivestreamRequestsAdapter.ViewHolder>{
    List<LiveStreamRequests> liveStreamRequests;
    Context context;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("d/M/yyyy");

    LocalDateTime now = LocalDateTime.now();


    public LivestreamRequestsAdapter(List<LiveStreamRequests> body, Context context) {
        Collections.reverse(body);
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
//        LocalDate dateStream = LocalDate.parse(liveStreamRequests.get(position).getDate(), dtf);
//        LocalDate dateCurrent = LocalDate.parse(dtf.format(now), dtf);
//        Log.d(TAG, "_________________"+dateCurrent+"__"+dateStream);

        LocalTime sT=LocalTime.parse(liveStreamRequests.get(position).getStartTime());
        LocalTime eT=LocalTime.parse(liveStreamRequests.get(position).getEndTime());

        if(liveStreamRequests.get(position).getSchoolID().equals(PreferenceData.getLoggedInUserData(context).get("userID"))){
            if(liveStreamRequests.get(position).getStatus().equals("Accepted")){
                if(dtf.format(now).equals(liveStreamRequests.get(position).getDate())){
                    if( LocalTime.now().getHour() >= sT.getHour() && LocalTime.now().getHour() <= eT.getHour()){
                        if(LocalTime.now().getHour() > sT.getHour() && LocalTime.now().getHour() < eT.getHour()){
                            //do stuff
                            holder.statusStreamRequest.setBackgroundColor(context.getResources().getColor(R.color.online_green));
                            holder.statusStreamRequest.setTextColor(context.getResources().getColor(R.color.textColorWhite));
                            holder.statusStreamRequest.setText("Start Stream");
                        }else if(LocalTime.now().getHour() == sT.getHour()) {
                            if (LocalTime.now().getMinute() >= sT.getMinute()) {
                                //do stuff
                                holder.statusStreamRequest.setBackgroundColor(context.getResources().getColor(R.color.online_green));
                                holder.statusStreamRequest.setTextColor(context.getResources().getColor(R.color.textColorWhite));
                                holder.statusStreamRequest.setText("Start Stream");
                            }
                        }else if(LocalTime.now().getHour() == eT.getHour()){
                            if (LocalTime.now().getMinute() <= eT.getMinute()) {
                                //do stuff
                                holder.statusStreamRequest.setBackgroundColor(context.getResources().getColor(R.color.online_green));
                                holder.statusStreamRequest.setTextColor(context.getResources().getColor(R.color.textColorWhite));
                                holder.statusStreamRequest.setText("Start Stream");
                            }
                        }
                    }
                }
            }
        }
        holder.statusStreamRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.statusStreamRequest.getText().toString().equals("Start Stream")){
//                    if(dtf.format(now).equals(liveStreamRequests.get(position).getDate())){
//                        if( LocalTime.now().getHour() >= sT.getHour() && LocalTime.now().getHour() <= eT.getHour()){
//                            if(LocalTime.now().getHour() > sT.getHour() && LocalTime.now().getHour() < eT.getHour()){
//                                //do stuff
//                            }else if(LocalTime.now().getHour() == sT.getHour()) {
//                                if (LocalTime.now().getMinute() >= sT.getMinute()) {
//                                    //do stuff
//                                }
//                            }else if(LocalTime.now().getHour() == eT.getHour()){
//                                if (LocalTime.now().getMinute() <= eT.getMinute()) {
//                                    //do stuff
//                                }
//                            }
//                        }
//                    }
                    //do stuff
                    Intent intent = new Intent(context, LiveStreamCam.class );
                    intent.putExtra("StreamID", liveStreamRequests.get(position).getStreamID());
                    context.startActivity(intent);
                }
            }
        });
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
