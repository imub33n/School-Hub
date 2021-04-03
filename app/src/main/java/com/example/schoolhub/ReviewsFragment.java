package com.example.schoolhub;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.schoolhub.Adapters.LivestreamRequestsAdapter;
import com.example.schoolhub.Adapters.SchoolReviewsAdapter;
import com.example.schoolhub.data.LiveStreamRequests;
import com.example.schoolhub.data.SchoolReviews;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;

public class ReviewsFragment extends Fragment {
    TextView reviewsStatus;
    ProgressBar progressBar;
    ImageView postReview;
    EditText giveReview;
    RatingBar giveRatingBar;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    RecyclerView recuclerForReview;
    SchoolReviewsAdapter schoolReviewsAdapter;
    List<SchoolReviews> thisSchoolReviews = new ArrayList<SchoolReviews>();
    SchoolReviews schoolReviews;
    float avg=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.fragment_reviews, container, false);

        progressBar = (ProgressBar) root.findViewById(R.id.progressBar);
        reviewsStatus= root.findViewById(R.id.reviewsStatus);
        postReview = root.findViewById(R.id.postReview);
        giveReview= root.findViewById(R.id.giveReview);
        giveRatingBar= root.findViewById(R.id.giveRatingBar);
        postReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(giveRatingBar.getRating()==0 ||giveReview.getText().length()==0){
                    if(giveRatingBar.getRating()==0){
                        Toast.makeText(getContext(), "Please give rating to post your review", Toast.LENGTH_LONG).show();
                    }
                    if(giveReview.getText().length()==0){
                        Toast.makeText(getContext(), "Please write review to post it", Toast.LENGTH_LONG).show();
                    }
                }else{
                    //posting here
                    schoolReviews.setRating((int) giveRatingBar.getRating());
                    schoolReviews.setReviewText(giveReview.getText().toString());
                    schoolReviews.setSchoolID(InformationSchoolFragment.thisSchoolData.get_id());
                    schoolReviews.setUserID("");
                    schoolReviews.setUsername("");
                }
            }
        });

        //retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);
        //listView
        recuclerForReview = (RecyclerView) root.findViewById(R.id.recuclerForReview);
        recuclerForReview.setLayoutManager(new LinearLayoutManager(getContext()));

        Call<List<SchoolReviews>> call2 = retrofitInterface.getReviews();
        call2.enqueue(new Callback<List<SchoolReviews>>() {
            @Override
            public void onResponse(Call<List<SchoolReviews>> call, Response<List<SchoolReviews>> response) {
                if (response.code() == 200) {
                    progressBar.setVisibility(View.INVISIBLE);
                    for(int i=0;i<response.body().size();i++){
                        if(Objects.equals(InformationSchoolFragment.thisSchoolData.get_id(),response.body().get(i).getSchoolID())){
                            thisSchoolReviews.add(response.body().get(i));
                        }
                        if(i==response.body().size()-1){
                            if(thisSchoolReviews.size()==0){
                                reviewsStatus.setText("No Reviews Yet!");
                            } else{
                                for(int j=0;j<thisSchoolReviews.size();j++){
                                    avg+=thisSchoolReviews.get(0).getRating();
                                }
                                avg=avg/thisSchoolReviews.size();
                                reviewsStatus.setText("Rating  "+avg);
                                schoolReviewsAdapter = new SchoolReviewsAdapter(thisSchoolReviews,getContext());
                                recuclerForReview.setAdapter(schoolReviewsAdapter);
                            }

                        }
                    }

                }else {
                    Toast.makeText(getContext(), "CODE: "+response.code(), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<List<SchoolReviews>> call, Throwable t) {
                Toast.makeText(getContext(), ""+t, Toast.LENGTH_LONG).show();
            }
        });

        return root;
    }
}