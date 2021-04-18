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
import com.example.schoolhub.data.PreferenceData;
import com.example.schoolhub.data.SchoolReviews;
import com.example.schoolhub.ui.liveStream.LiveStreamRequest;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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
    RecyclerView recyclerForReview;
    SchoolReviewsAdapter schoolReviewsAdapter;
    List<SchoolReviews> thisSchoolReviews = new ArrayList<SchoolReviews>();
    SchoolReviews schoolReviews= new SchoolReviews();
    DecimalFormat adf;
    float avg=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.fragment_reviews, container, false);
        adf = new DecimalFormat("0.0");
        progressBar = (ProgressBar) root.findViewById(R.id.progressBar);
        reviewsStatus= root.findViewById(R.id.reviewsStatus);
        postReview = root.findViewById(R.id.postReview);
        giveReview= root.findViewById(R.id.giveReview);
        giveRatingBar= root.findViewById(R.id.giveRatingBar);
        //retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

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
                    if(PreferenceData.getLoggedInUserData(getContext()).get("userID")==null){
                        Toast.makeText(getContext(), "Please sign in to give review/rating!", Toast.LENGTH_LONG).show();
                    }
                    else{
                        progressBar.setVisibility(View.VISIBLE);
                        schoolReviews.setUserID(PreferenceData.getLoggedInUserData(getContext()).get("userID"));
                        schoolReviews.setUsername(PreferenceData.getLoggedInUserData(getContext()).get("username"));
                        schoolReviews.setRating((int) giveRatingBar.getRating());
                        schoolReviews.setReviewText(giveReview.getText().toString());
                        schoolReviews.setSchoolID(InformationSchoolFragment.thisSchoolData.get_id());
                        Timestamp timestamp= new Timestamp(System.currentTimeMillis());
                        SimpleDateFormat df= new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        schoolReviews.setDate(df.format(timestamp));
                        //POST REQUEST
                        Call<Void> call = retrofitInterface.postReviews(schoolReviews);
                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.code() == 200) {
                                    Toast.makeText(getContext(), "Review Posted!", Toast.LENGTH_LONG).show();
                                    giveRatingBar.setRating(0);
                                    giveReview.setText("");
                                    List<SchoolReviews> updatedSchoolReviews = new ArrayList<SchoolReviews>();
                                    //update review list after posting review
                                    Call<List<SchoolReviews>> call2 = retrofitInterface.getReviews();
                                    call2.enqueue(new Callback<List<SchoolReviews>>() {
                                        @Override
                                        public void onResponse(Call<List<SchoolReviews>> call, Response<List<SchoolReviews>> response) {
                                            if (response.code() == 200) {
                                                progressBar.setVisibility(View.INVISIBLE);
                                                for(int i=0;i<response.body().size();i++){
                                                    if(Objects.equals(InformationSchoolFragment.thisSchoolData.get_id(),response.body().get(i).getSchoolID())){
                                                        updatedSchoolReviews.add(response.body().get(i));
                                                    }
                                                    if(i==response.body().size()-1){
                                                        if(updatedSchoolReviews.size()==0){
                                                            reviewsStatus.setText("No Reviews Yet!");
                                                        } else{
                                                            for(int j=0;j<updatedSchoolReviews.size();j++){
                                                                avg+=updatedSchoolReviews.get(j).getRating();
                                                            }
                                                            avg=avg/updatedSchoolReviews.size();

                                                            reviewsStatus.setText("Rating  "+adf.format(avg));
                                                            avg=0;
                                                            schoolReviewsAdapter = new SchoolReviewsAdapter(updatedSchoolReviews,getContext());
                                                            recyclerForReview.setAdapter(schoolReviewsAdapter);
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
                                }else{
                                    Toast.makeText(getContext(), "Error Code: "+response.code(), Toast.LENGTH_LONG).show();
                                }
                            }
                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(getContext(), "Connection Error: "+t.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                }
            }
        });

        //listViewReviews
        recyclerForReview = (RecyclerView) root.findViewById(R.id.recuclerForReview);
        recyclerForReview.setLayoutManager(new LinearLayoutManager(getContext()));

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
                                    avg+=thisSchoolReviews.get(j).getRating();
                                }
                                avg=avg/thisSchoolReviews.size();

                                reviewsStatus.setText("Rating  "+adf.format(avg));
                                avg=0;
                                schoolReviewsAdapter = new SchoolReviewsAdapter(thisSchoolReviews,getContext());
                                recyclerForReview.setAdapter(schoolReviewsAdapter);
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