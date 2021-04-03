package com.example.schoolhub;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    RecyclerView recuclerForReview;
    SchoolReviewsAdapter schoolReviewsAdapter;
    List<SchoolReviews> thisSchoolReviews = new ArrayList<SchoolReviews>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.fragment_reviews, container, false);
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
                    for(int i=0;i<response.body().size();i++){
                        if(Objects.equals(InformationSchoolFragment.thisSchoolData.get_id(),response.body().get(i).getSchoolID())){
                            thisSchoolReviews.add(response.body().get(i));
                            Log.d(TAG, "onResponse:_____"+i+"________ ");
                        }
                        if(i==response.body().size()-1){
                            schoolReviewsAdapter = new SchoolReviewsAdapter(thisSchoolReviews,getContext());
                            recuclerForReview.setAdapter(schoolReviewsAdapter);
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