package com.example.schoolhub.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolhub.R;
import com.example.schoolhub.data.CommentLiveStream;
import com.example.schoolhub.ui.liveStream.LiveStreamCam;

import java.util.ArrayList;
import java.util.List;

public class CommentLivestreamAdapter extends RecyclerView.Adapter<CommentLivestreamAdapter.ViewHolder> {
    List<CommentLiveStream> comments;
    Context context;

    public CommentLivestreamAdapter(List<CommentLiveStream> cmnts, LiveStreamCam liveStreamCam) {
        this.comments=cmnts;
        this.context=liveStreamCam;
    }

    @NonNull
    @Override
    public CommentLivestreamAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.frag_livestream_cmnt,parent,false);
        return new CommentLivestreamAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentLivestreamAdapter.ViewHolder holder, int position) {
        holder.cmnt_here.setText(comments.get(position).getText());
        holder.name_person.setText(comments.get(position).getUsername());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView cmnt_here,name_person;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cmnt_here= itemView.findViewById(R.id.cmnt_here);
            name_person= itemView.findViewById(R.id.name_person);
        }
    }
}
