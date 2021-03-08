package com.example.schoolhub.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolhub.R;
import com.example.schoolhub.data.Comment;

import java.util.List;

import static android.content.ContentValues.TAG;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    List<Comment> resourceComment;
    Context context;
    public CommentAdapter(List<Comment> res, Context context) {
        Log.d(TAG, "CommentAdapter: "+res.size());
        this.resourceComment=res;
        this.context = context;
    }

    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_body,parent,false);
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

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
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
