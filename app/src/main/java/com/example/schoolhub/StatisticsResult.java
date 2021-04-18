package com.example.schoolhub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.schoolhub.Adapters.GraphAdapter;
import com.example.schoolhub.Adapters.SchoolReviewsAdapter;
import com.example.schoolhub.data.SchoolReviews;
import com.example.schoolhub.ui.statistics.StatisticsFragment;
import com.google.android.material.tabs.TabLayout;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StatisticsResult extends AppCompatActivity {
    public Toolbar schoolComparisonNav;
    TextView skolNameRatedStats,RatedStats,skolNameDistaceStats,DistaceStats,skolNameFeeStats,FeeStats;

    String highestRatedSkol;
    float RatingSkol=0;
    float avg=0;
    DecimalFormat adf= new DecimalFormat("0.0");

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    List<SchoolReviews> thisSchoolReviews = new ArrayList<SchoolReviews>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics_result);
        skolNameRatedStats=findViewById(R.id.skolNameRatedStats);
        RatedStats=findViewById(R.id.RatedStats);
        skolNameDistaceStats=findViewById(R.id.skolNameDistaceStats);
        DistaceStats=findViewById(R.id.DistaceStats);
        skolNameFeeStats=findViewById(R.id.skolNameFeeStats);
        FeeStats=findViewById(R.id.FeeStats);
        //retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);
        Call<List<SchoolReviews>> call2 = retrofitInterface.getReviews();
        call2.enqueue(new Callback<List<SchoolReviews>>() {
            @Override
            public void onResponse(Call<List<SchoolReviews>> call, Response<List<SchoolReviews>> response) {
                if (response.code() == 200) {
                    for(int a=0;a<StatisticsFragment.ComparisonSchools.size();a++){
                        for(int i=0;i<response.body().size();i++){
                            if(Objects.equals(StatisticsFragment.ComparisonSchools.get(a).get_id(),response.body().get(i).getSchoolID())){
                                thisSchoolReviews.add(response.body().get(i));
                            }
                            if(i==response.body().size()-1){
                                if(thisSchoolReviews.size()==0){
                                } else{
                                    for(int j=0;j<thisSchoolReviews.size();j++){
                                        avg+=thisSchoolReviews.get(j).getRating();
                                }
                                avg=avg/thisSchoolReviews.size();
                                if(avg>RatingSkol){
                                    RatingSkol=avg;
                                    highestRatedSkol=StatisticsFragment.ComparisonSchools.get(a).getSchoolName();
                                }
                                avg=0;
                                thisSchoolReviews.clear();
                                }
                            }
                        }
                        if(a==StatisticsFragment.ComparisonSchools.size()-1){
                            skolNameRatedStats.setText(highestRatedSkol);

                            RatedStats.setText(adf.format(RatingSkol));
                        }
                    }

                }else {
                    Toast.makeText(getApplicationContext(), "CODE: "+response.code(), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<List<SchoolReviews>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), ""+t, Toast.LENGTH_LONG).show();
            }
        });
//        for(int a=0;a<StatisticsFragment.ComparisonSchools.size();a++){
//            if(StatisticsFragment.ComparisonSchools.)
//        }
        schoolComparisonNav=findViewById(R.id.schoolComparisonNav);
        schoolComparisonNav.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                onBackPressed();
            }
        });

    }
}