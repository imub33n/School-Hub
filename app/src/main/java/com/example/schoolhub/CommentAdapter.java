package com.example.schoolhub;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolhub.data.Comment;
import com.example.schoolhub.data.PostResult;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    List<Comment> resourceComment;
    Context context;
    public CommentAdapter(List<Comment> resource, Context context) {
        this.resourceComment=resource;
        this.context = context;
    }

    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.post_body,parent,false);
        return new CommentAdapter.ViewHolder(v);
        //return null;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position) {
        Comment comments=resourceComment.get(position);
        holder.userNameComment.setText(comments.getUsername());
        holder.textComment.setText(comments.getText());
    }

    @Override
    public int getItemCount() {
        return resourceComment.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView userNameComment,textComment;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.userNameComment = itemView.findViewById(R.id.userNameComment);
            this.textComment = itemView.findViewById(R.id.textComment);
        }
    }
}
