package com.example.schoolhub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.schoolhub.data.SchoolReviews;
import com.example.schoolhub.ui.statistics.StatisticsFragment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import static android.content.ContentValues.TAG;

public class StatisticsResult extends AppCompatActivity {
    public Toolbar schoolComparisonNav;
    TextView skolNameRatedStats,RatedStats,skolNameDistaceStats,DistaceStats,skolNameFeeStats,FeeStats;

    String highestRatedSkol;
    float RatingSkol=0;
    float avg=0;
    String shortestDistaceSkol;
    float DistaceSkol= 99999;
    String cheapestSkol;
    int fee= 999999;

    DecimalFormat adf= new DecimalFormat("0.0");
    DecimalFormat bdf= new DecimalFormat("00.0");

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

        for(int a=0;a<StatisticsFragment.ComparisonSchools.size();a++){
            for(int i=0;i<StatisticsFragment.allSchoolReviews.size();i++){
                if(Objects.equals(StatisticsFragment.ComparisonSchools.get(a).get_id(),StatisticsFragment.allSchoolReviews.get(i).getSchoolID())){
                    thisSchoolReviews.add(StatisticsFragment.allSchoolReviews.get(i));
                }
                if(i==StatisticsFragment.allSchoolReviews.size()-1){
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

        //distance
        for(int a=0;a<StatisticsFragment.ComparisonSchools.size();a++){
            float[] results = new float[1];
            Location.distanceBetween(
                    Double.parseDouble(StatisticsFragment.schoolCoordinates.getLatitude()),
                    Double.parseDouble(StatisticsFragment.schoolCoordinates.getLongitude()),
                    Double.parseDouble(StatisticsFragment.ComparisonSchools.get(a).getSchoolCoordinates().getLatitude()),
                    Double.parseDouble(StatisticsFragment.ComparisonSchools.get(a).getSchoolCoordinates().getLongitude()),
                    results);
            //valueOf
            if( (results[0] / 1000) < DistaceSkol){
                DistaceSkol = results[0]/1000;
                shortestDistaceSkol = StatisticsFragment.ComparisonSchools.get(a).getSchoolName();
            }
//            Log.d(TAG, "onLocationChanged() called with: location = [______]" + DistaceSkol[0]/1000+"____" );
        }
        skolNameDistaceStats.setText(shortestDistaceSkol+" is nearest to you!");
        DistaceStats.setText(bdf.format(DistaceSkol)+" KM Away");

        //fee

        for(int a=0;a<StatisticsFragment.ComparisonSchools.size();a++){
            if(StatisticsFragment.ComparisonSchools.get(a).getFeeStructure().get(StatisticsFragment.ComparisonSchools.get(a).getFeeStructure().size()-1).getMonthlyFee()<fee &&
                    StatisticsFragment.ComparisonSchools.get(a).getFeeStructure().get(StatisticsFragment.ComparisonSchools.get(a).getFeeStructure().size()-1).getMonthlyFee()!=0){
                fee=StatisticsFragment.ComparisonSchools.get(a).getFeeStructure().get(StatisticsFragment.ComparisonSchools.get(a).getFeeStructure().size()-1).getMonthlyFee();
                cheapestSkol=StatisticsFragment.ComparisonSchools.get(a).getSchoolName();
            }
        }
        skolNameFeeStats.setText(cheapestSkol);
        FeeStats.setText(fee+"PKR / Month");
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