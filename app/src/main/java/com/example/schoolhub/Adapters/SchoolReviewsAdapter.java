package com.example.schoolhub.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolhub.R;
import com.example.schoolhub.data.LiveStreamRequests;
import com.example.schoolhub.data.SchoolReviews;

import java.util.List;

public class SchoolReviewsAdapter extends RecyclerView.Adapter<SchoolReviewsAdapter.ViewHolder>{
    List<SchoolReviews> schoolReviews;
    Context context;

    public SchoolReviewsAdapter(List<SchoolReviews> body, Context context) {
        this.schoolReviews=body;
        this.context=context;
    }

    @NonNull
    @Override
    public SchoolReviewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.review_body,parent,false);
        SchoolReviewsAdapter.ViewHolder viewHolder = new SchoolReviewsAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SchoolReviewsAdapter.ViewHolder holder, int position) {
        holder.userNameInReview.setText(schoolReviews.get(position).getUsername());
        holder.dateInReview.setText(schoolReviews.get(position).getDate());
        holder.textInReview.setText(schoolReviews.get(position).getReviewText());
        holder.ratingBarInReview.setRating(schoolReviews.get(position).getRating());
    }

    @Override
    public int getItemCount() {
        return schoolReviews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RatingBar ratingBarInReview;
        TextView userNameInReview,dateInReview,textInReview;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.ratingBarInReview=itemView.findViewById(R.id.ratingBarInReview);
            this.userNameInReview=itemView.findViewById(R.id.userNameInReview);
            this.dateInReview=itemView.findViewById(R.id.dateInReview);
            this.textInReview=itemView.findViewById(R.id.textInReview);
        }
    }
}
