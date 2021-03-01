package com.example.schoolhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.schoolhub.Adapters.AdapterLanding;
import com.example.schoolhub.Adapters.PostViewAdapter;
import com.example.schoolhub.Adapters.SearchResultAdapter;
import com.example.schoolhub.Adapters.SlideAdapter;
import com.example.schoolhub.data.Fee;
import com.example.schoolhub.data.Image;
import com.example.schoolhub.data.PostResult;
import com.example.schoolhub.data.SchoolData;
import com.example.schoolhub.data.SchoolsLandingModel;
import com.example.schoolhub.data.SearchFilters;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;

public class LandingScreen extends AppCompatActivity {
    EditText searchEditText;
    Button search,reset;
    RadioButton radioButton1,radioButton2,radioButton3;
    RadioGroup group1,group2,group3;
    LinearLayout filterHeading,filtersLayout;
    ViewPager2 viewPager;
    RecyclerView recyclerViewLanding,recyclerViewLanding2;

    List<Image> imagesLanding= new ArrayList<>();
    Image imageInLanding= new Image();
    public Boolean theBoolean =true;

    SearchFilters searchFilters= new SearchFilters();
    Fee fee=new Fee();
    List<SchoolData> schoolData;
    SearchResultAdapter searchResultAdapter;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    ArrayList<SchoolsLandingModel> models = new ArrayList<>();
    ArrayList<SchoolsLandingModel> models2 = new ArrayList<>();
    SlideAdapter adapter;
    AdapterLanding adapterLanding;
    Handler sliderHandler= new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_screen);

        retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);

        //sliderAdapter
        viewPager = (ViewPager2) findViewById(R.id.viewPagerSliderLandingPage);
        imageInLanding.setPath("gs://okay-945dc.appspot.com/images/landingScreen.jpg");
        imagesLanding.add(imageInLanding);
        imagesLanding.add(imageInLanding);
        imagesLanding.add(imageInLanding);

        adapter= new SlideAdapter(imagesLanding,this);
        viewPager.setAdapter(adapter);

        //landing view pager
        recyclerViewLanding = (RecyclerView) findViewById(R.id.schoolsSliderLandingPage);
        recyclerViewLanding.setHasFixedSize(true);
        recyclerViewLanding.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        this.models.add( new SchoolsLandingModel(R.drawable.a, "Brochure", "Brochure is an informative ") );
        this.models.add(new SchoolsLandingModel(R.drawable.b, "Sticker", "Sticker is a type of label"));
        this.models.add(new SchoolsLandingModel(R.drawable.c, "Poster", "Poster is any piece of printed"));
        this.models.add(new SchoolsLandingModel(R.drawable.a, "Namecard", "Business cards are cards"));
        adapterLanding = new AdapterLanding(models,this);
        recyclerViewLanding.setAdapter(adapterLanding);
        //2
        recyclerViewLanding2 = (RecyclerView) findViewById(R.id.schoolsSliderLandingPage2);
        recyclerViewLanding2.setHasFixedSize(true);
        recyclerViewLanding2.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        this.models2.add( new SchoolsLandingModel(R.drawable.b, "Brochure2", "Brochure is an informative ") );
        this.models2.add(new SchoolsLandingModel(R.drawable.a, "Sticker2", "Sticker is a type of label"));
        this.models2.add(new SchoolsLandingModel(R.drawable.c, "Poster2", "Poster is any piece of printed"));
        this.models2.add(new SchoolsLandingModel(R.drawable.a, "Namecard2", "Business cards are cards"));
        adapterLanding = new AdapterLanding(models2,this);
        recyclerViewLanding2.setAdapter(adapterLanding);
        //sliderAdapter
        viewPager.setClipToPadding(false);
        viewPager.setClipChildren(false);
        viewPager.setOffscreenPageLimit(4);
        viewPager.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(20));//margin next picture is 20
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float a = 1-Math.abs(position);
                page.setScaleY(0.85f+ a * 0.15f);
            }
        });
        viewPager.setPageTransformer(compositePageTransformer);
        //auto scroll
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable,4000);//oh yas 4 sec delay
                if(viewPager.getCurrentItem()==5){
                    viewPager.setCurrentItem(0);
                }
            }
        });
    }

    public void openSearchFragment(View view) {
        View v = getLayoutInflater().inflate(R.layout.fragment_filters, null);
        //full screen bottom sheet
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        LinearLayout linearLayout = v.findViewById(R.id.rootfrag);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
        params.height = getScreenHeight();
        linearLayout.setLayoutParams(params);
        dialog.setContentView(v);
        dialog.show();
        BottomSheetBehavior mBehavior;
        mBehavior = BottomSheetBehavior.from((View) v.getParent());
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        //yes
        searchEditText=v.findViewById(R.id.searchEditText);
        reset=v.findViewById(R.id.button);
        search=v.findViewById(R.id.button2);

        filterHeading=v.findViewById(R.id.filterHeading);
        filtersLayout=v.findViewById(R.id.filtersLayout);

        group1=v.findViewById(R.id.group1);
        group2=v.findViewById(R.id.group2);
        group3=v.findViewById(R.id.group3);

        //recycler
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.searchResultRecycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                filtersLayout.setLayoutParams(layoutParams);
                theBoolean=true;
                LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(0, 0);
                recyclerView.setLayoutParams(layoutParams2);
            }
        });
        filterHeading.setOnClickListener(new View.OnClickListener() {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, 0);
            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            @Override
            public void onClick(View v) {
                if(theBoolean){
                    filtersLayout.setLayoutParams(layoutParams);
                    theBoolean = !theBoolean;
                }else if(!theBoolean){
                    filtersLayout.setLayoutParams(layoutParams2);
                    recyclerView.setLayoutParams(layoutParams);
                    theBoolean = !theBoolean;
                }
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                group1.clearCheck();
                group2.clearCheck();
                group3.clearCheck();
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButton1=v.findViewById(group1.getCheckedRadioButtonId());
                radioButton2=v.findViewById(group2.getCheckedRadioButtonId());
                radioButton3=v.findViewById(group3.getCheckedRadioButtonId());
                //set values to send
                fee.setMin(10);
                fee.setMax(10000);
                searchFilters.setFee(fee);
                searchFilters.setSchoolType("Co-Education");
                searchFilters.setEducationLevel("Primary");
                searchFilters.setEducationType("Science");
//                if(radioButton1.getText().toString()!=null){
//                    searchFilters.setSchoolType(radioButton1.getText().toString());
//                }if(radioButton2.getText().toString()!=null){
//                    searchFilters.setEducationLevel(radioButton2.getText().toString());
//                }if(radioButton3.getText().toString()!=null){
//                    searchFilters.setEducationType(radioButton3.getText().toString());
//                }

                Call<List<SchoolData>> call = retrofitInterface.searchSchools(searchEditText.getText().toString(),searchFilters);
                call.enqueue(new Callback<List<SchoolData>>() {
                    @Override
                    public void onResponse(Call<List<SchoolData>> call, Response<List<SchoolData>> response) {
                        Log.d(TAG, "Size: _____________----____"+response.body().size());

                        if(response.code()==200){
                            //filter
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, 0);
                            filtersLayout.setLayoutParams(layoutParams);
                            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            recyclerView.setLayoutParams(layoutParams2);
                            theBoolean=false;
                            //
                            schoolData =  response.body();
                            searchResultAdapter = new SearchResultAdapter(schoolData,getApplicationContext());
                            recyclerView.setAdapter(searchResultAdapter);
//                            for(int i=0;i<schoolData.size();i++){
//                                Log.d(TAG, "onResponse:___________________ "+schoolData.get(i).getSchoolName());
//
//                            }
                        }
                        if(!response.isSuccessful()){
                            Log.d(TAG, "onResponse search retrofit: "+response.code());
                            return;
                        }
                    }
                    @Override
                    public void onFailure(Call<List<SchoolData>> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), " some connection error in search : "+t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
    }

    private int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
//            if(viewPager.getCurrentItem()==3){
//
//            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable,4000);
    }

    public void openSignIn(View view) {
        Intent it = new Intent( getApplicationContext() , SignIn.class);
        startActivity(it);
    }
}