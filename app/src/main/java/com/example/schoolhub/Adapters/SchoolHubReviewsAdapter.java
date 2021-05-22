package com.example.schoolhub.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolhub.R;
import com.example.schoolhub.data.SchoolHubReview;

import java.util.List;

public class SchoolHubReviewsAdapter extends RecyclerView.Adapter<SchoolHubReviewsAdapter.ViewHolder>{
    List<SchoolHubReview> schoolHubReviews;
    Context context;

    public SchoolHubReviewsAdapter(List<SchoolHubReview> body, Context applicationContext) {
        this.schoolHubReviews=body;
        this.context=applicationContext;
    }

    @NonNull
    @Override
    public SchoolHubReviewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.schoolhub_review_layout,parent,false);
        SchoolHubReviewsAdapter.ViewHolder viewHolder = new SchoolHubReviewsAdapter.ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SchoolHubReviewsAdapter.ViewHolder holder, int position) {
        holder.userNameInReview.setText(schoolHubReviews.get(position).getUsername());
        holder.dateInReview.setText(schoolHubReviews.get(position).getDate());
        holder.textInReview.setText(schoolHubReviews.get(position).getReviewText());
        holder.ratingBarInReview.setRating(schoolHubReviews.get(position).getRating());
        if(schoolHubReviews.get(position).getReply().size()>0){
            holder.textReplyReview.setText(schoolHubReviews.get(position).getReply().get(0).getText());
        }else{
            holder.replyReviewLayout.setLayoutParams(new LinearLayout.LayoutParams(0,0));
        }
    }

    @Override
    public int getItemCount() {
        return schoolHubReviews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RatingBar ratingBarInReview;
        TextView userNameInReview,dateInReview,textInReview,textReplyReview;
        LinearLayout replyReviewLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.ratingBarInReview=itemView.findViewById(R.id.ratingBarInReview);
            this.userNameInReview=itemView.findViewById(R.id.userNameInReview);
            this.dateInReview=itemView.findViewById(R.id.dateInReview);
            this.textInReview=itemView.findViewById(R.id.textInReview);
            this.textReplyReview=itemView.findViewById(R.id.textReplyReview);
            this.replyReviewLayout=itemView.findViewById(R.id.replyReviewLayout);
        }
    }
}
