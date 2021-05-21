package com.example.schoolhub;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.schoolhub.Adapters.SchoolReviewsAdapter;
import com.example.schoolhub.data.PreferenceData;
import com.example.schoolhub.data.SchoolHubReview;
import com.example.schoolhub.data.SchoolReviews;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReviewAndFeedback extends AppCompatActivity {
    RatingBar giveRatingBar;
    EditText textReview;
    ImageView postReview;

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    SchoolHubReview schoolHubReview= new SchoolHubReview();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_and_feedback);
        postReview=findViewById(R.id.postReview);
        textReview=findViewById(R.id.textReview);
        giveRatingBar=findViewById(R.id.giveRatingBar);

        //retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        //POST REQUEST
        postReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(giveRatingBar.getRating()==0 ||textReview.getText().length()==0){
                    if(giveRatingBar.getRating()==0){
                        Toast.makeText(ReviewAndFeedback.this, "Please give rating to post your review", Toast.LENGTH_LONG).show();
                    }
                    if(textReview.getText().length()==0){
                        Toast.makeText(ReviewAndFeedback.this, "Please write review to post it", Toast.LENGTH_LONG).show();
                    }
                }else{
                    schoolHubReview.setUserID(PreferenceData.getLoggedInUserData(ReviewAndFeedback.this).get("userID"));
                    schoolHubReview.setUsername(PreferenceData.getLoggedInUserData(ReviewAndFeedback.this).get("username"));
                    schoolHubReview.setRating((int) giveRatingBar.getRating());
                    schoolHubReview.setReviewText(textReview.getText().toString());
                    Timestamp timestamp= new Timestamp(System.currentTimeMillis());
                    SimpleDateFormat df= new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    schoolHubReview.setDate(df.format(timestamp));
                    schoolHubReview.setUserProfilePic(PreferenceData.getLoggedInUserData(ReviewAndFeedback.this).get("userPic"));

                    Call<Void> call = retrofitInterface.postSchoolhubReview(schoolHubReview);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.code() == 200) {
                                Toast.makeText(ReviewAndFeedback.this, "Review Posted!", Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(ReviewAndFeedback.this, "Error Code: "+response.code(), Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {

                            Toast.makeText(ReviewAndFeedback.this, "Connection Error: "+t.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

    }
}