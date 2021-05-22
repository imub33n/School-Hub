package com.example.schoolhub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.schoolhub.data.SchoolReviews;
import com.example.schoolhub.ui.statistics.StatisticsFragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
    LinearLayout linearLayout6;
    ImageView shareButton;

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
        linearLayout6=findViewById(R.id.linearLayout6);
        shareButton=findViewById(R.id.shareButton);

        //retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
                View thisView = linearLayout6;
                store(getScreenShot(thisView),"Statistics");
            }
        });

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
        if(DistaceSkol>100){
            skolNameDistaceStats.setText(shortestDistaceSkol+"");
            DistaceStats.setText(bdf.format(DistaceSkol)+" KM Away");
        }else{
        skolNameDistaceStats.setText(shortestDistaceSkol+" is nearest to you!");
        DistaceStats.setText(bdf.format(DistaceSkol)+" KM Away");
        }

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
    public static Bitmap getScreenShot(View v) {
//        View screenView = view.getRootView();
//        screenView.setDrawingCacheEnabled(true);
//        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
//        screenView.setDrawingCacheEnabled(false);
        Bitmap b = Bitmap.createBitmap(v.getWidth() , v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);
        return b;
    }

    private void store(Bitmap finalBitmap,String filename) {

        String root = Environment.getExternalStorageDirectory().getAbsolutePath().toString();

        File myDir = new File(root + "/StatsScreenshots");
        if(!myDir.exists()){
            try {
                myDir.mkdirs();
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }

        File file = new File (myDir, filename);
        if (file.exists ()) file.delete ();
        try {
            file.createNewFile();
        } catch (SecurityException | IOException se) {
            se.printStackTrace();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            shareImage(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void shareImage(File file){
        //Uri uri = Uri.fromFile(file);
//        <meta-data
//        android:name="android.support.FILE_PROVIDER_PATHS"
//        android:resource="@xml/provider_paths" />
        Uri uri = FileProvider.getUriForFile(StatisticsResult.this, StatisticsResult.this.
                getApplicationContext().getPackageName() + ".provider", file);
        Log.d(TAG, "getScreenShot: ________uri_____"+uri);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, "");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        try {
            startActivity(Intent.createChooser(intent, "Share Screenshot"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), "No App Available", Toast.LENGTH_SHORT).show();
        }
    }
}
//
// https://stackoverflow.com/questions/30196965/how-to-take-a-screenshot-of-current-activity-and-then-share-it/30212385
// https://stackoverflow.com/questions/19214714/android-taking-the-screenshot-and-sharing-it

//full screen capture
//https://stackoverflow.com/questions/9791714/take-a-screenshot-of-a-whole-view