package com.example.schoolhub.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolhub.R;
import com.example.schoolhub.data.ReplyReview;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReplyReviewAdapter extends RecyclerView.Adapter<ReplyReviewAdapter.ViewHolder> {
    List<ReplyReview> replyReviews;
    Context context;

    public ReplyReviewAdapter(List<ReplyReview> reply, Context context) {
        this.replyReviews=reply;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.review_reply_body,parent,false);
        return new ReplyReviewAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textReplyReview.setText(replyReviews.get(position).getText());
        holder.userNameReplyReview.setText(replyReviews.get(position).getUsername());
    }

    @Override
    public int getItemCount() {
        return replyReviews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView userNameReplyReview,textReplyReview;
        CircleImageView userDpReplyReview;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textReplyReview=itemView.findViewById(R.id.textReplyReview);
            userNameReplyReview=itemView.findViewById(R.id.userNameReplyReview);
            userDpReplyReview=itemView.findViewById(R.id.userDpReplyReview);
        }
    }
}
