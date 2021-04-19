package com.example.schoolhub.ui.statistics;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.example.schoolhub.Adapters.ComparisonAdapter;
import com.example.schoolhub.Adapters.GraphAdapter;
import com.example.schoolhub.Adapters.SearchResultAdapter;
import com.example.schoolhub.LandingScreen;
import com.example.schoolhub.MainActivity;
import com.example.schoolhub.R;
import com.example.schoolhub.RetrofitInterface;
import com.example.schoolhub.StatisticsResult;
import com.example.schoolhub.data.Fee;
import com.example.schoolhub.data.OnItemClick;
import com.example.schoolhub.data.SchoolCoordinates;
import com.example.schoolhub.data.SchoolData;
import com.example.schoolhub.data.SchoolReviews;
import com.example.schoolhub.data.SearchFilters;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;

public class StatisticsFragment extends Fragment implements LocationListener, OnItemClick {
    String jaga="Stats";
    OnItemClick m=this;
    View v;
    private BottomSheetBehavior mBehavior;
    BottomSheetDialog dialog;
    public Button compareButton,buttonAddSchool;
    LocationManager locationManager;
    EditText searchEditText,editTextMin,editTextMax,distanceMax;
    Button search,reset;
    TextView statusSearch;
    private RadioButton radioButton1,radioButton2,radioButton3;
    private RadioGroup group1,group2,group3;
    LinearLayout filterHeading,filtersLayout;
    public Boolean theBoolean =true;
    SearchFilters searchFilters= new SearchFilters();
    Fee fee=new Fee();
    SearchFilters searchFiltersReset= new SearchFilters();
    Fee feeReset=new Fee();
    public static SchoolCoordinates schoolCoordinates=new SchoolCoordinates();
    List<SchoolData> schoolData;
    SearchResultAdapter searchResultAdapter;
    static RecyclerView recyclerViewCompare;
    public static List<SchoolData> ComparisonSchools= new ArrayList<>();
    public static List<SchoolReviews> allSchoolReviews = new ArrayList<SchoolReviews>();
    ComparisonAdapter comparisonAdapter;

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_statistics, container, false);
        getLocation();
        v = getLayoutInflater().inflate(R.layout.fragment_filters, null, false);
        dialog = new BottomSheetDialog(getContext());
        dialog.setContentView(v);
        recyclerViewCompare = (RecyclerView) root.findViewById(R.id.yesCompare);
        recyclerViewCompare.setHasFixedSize(true);
        recyclerViewCompare.setLayoutManager(new LinearLayoutManager(getContext()));

        retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
        //request review data
        Call<List<SchoolReviews>> call2er = retrofitInterface.getReviews();
        call2er.enqueue(new Callback<List<SchoolReviews>>() {
            @Override
            public void onResponse(Call<List<SchoolReviews>> call, Response<List<SchoolReviews>> response) {
                if (response.code() == 200) {
                    allSchoolReviews = response.body();
                }else {
                    Toast.makeText(getContext(), "CODE: "+response.code(), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<List<SchoolReviews>> call, Throwable t) {
                Toast.makeText(getContext(), ""+t, Toast.LENGTH_LONG).show();
            }
        });
        //yes
        searchEditText=v.findViewById(R.id.searchEditText);
        reset=v.findViewById(R.id.button);
        search=v.findViewById(R.id.button2);
        editTextMin=v.findViewById(R.id.editTextMin);
        editTextMax=v.findViewById(R.id.editTextMax);
        statusSearch=v.findViewById(R.id.statusSearch);
        distanceMax=v.findViewById(R.id.distanceMax);

        filterHeading=v.findViewById(R.id.filterHeading);
        filtersLayout=v.findViewById(R.id.filtersLayout);

        group1=v.findViewById(R.id.group1);
        group2=v.findViewById(R.id.group2);
        group3=v.findViewById(R.id.group3);

        //recyclerSearch
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.searchResultRecycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        searchEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                filtersLayout.setLayoutParams(layoutParams);
                theBoolean=true;
                LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(0, 0);
                recyclerView.setLayoutParams(layoutParams2);
                statusSearch.setText("");
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
                    statusSearch.setText("");
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
                fee=feeReset;
                searchFilters=searchFiltersReset;
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioButton1 = (RadioButton) v.findViewById(group1.getCheckedRadioButtonId());
                radioButton2 = (RadioButton) v.findViewById(group2.getCheckedRadioButtonId());
                radioButton3 = (RadioButton) v.findViewById(group3.getCheckedRadioButtonId());
                //layouts
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, 0);
                filtersLayout.setLayoutParams(layoutParams);
                LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                recyclerView.setLayoutParams(layoutParams2);
                theBoolean=false;
                LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams3.gravity = Gravity.CENTER;
                statusSearch.setLayoutParams(layoutParams3);
                if(schoolData!=null){
                    schoolData.clear();
                }
                statusSearch.setText("Searching..");
                //____________setting values to send
                if(editTextMin.getText().toString().length()>0){
                    fee.setMin(Integer.parseInt(editTextMin.getText().toString()));
                }
                if(editTextMax.getText().toString().length()>0){
                    fee.setMax(Integer.parseInt(editTextMax.getText().toString()));
                }
                searchFilters.setFee(fee);//_________________________fee
                if (group1.getCheckedRadioButtonId() != -1){
                    searchFilters.setSchoolType(radioButton1.getText().toString());//_______________schoolType
                }if(group2.getCheckedRadioButtonId() != -1){
                    searchFilters.setEducationLevel(radioButton2.getText().toString());//_______________educationLevel
                }if(group3.getCheckedRadioButtonId() != -1){
                    searchFilters.setEducationType(radioButton3.getText().toString());//_______________educationType
                }if(distanceMax.getText().toString().length()>0){
                    searchFilters.setDistance(Integer.valueOf(distanceMax.getText().toString()));//_______________distance
                }

                Call<List<SchoolData>> call = retrofitInterface.searchSchools(searchEditText.getText().toString(),searchFilters);
                call.enqueue(new Callback<List<SchoolData>>() {
                    @Override
                    public void onResponse(Call<List<SchoolData>> call, Response<List<SchoolData>> response) {
                        Log.d(TAG, "Size: _____________SIZE:__"+ response.body().size());

                        if(response.code()==200){
                            //
                            schoolData =  response.body();
                            if(schoolData.size()>0){
                                statusSearch.setText("");
                                statusSearch.setLayoutParams(layoutParams);
                            }else if(schoolData.size()==0){
                                statusSearch.setText("No School Found");
                            }

                            searchResultAdapter = new SearchResultAdapter(jaga,schoolData,getContext(),m);
                            recyclerView.setAdapter(searchResultAdapter);

                        }
                        if(!response.isSuccessful()){
                            Log.d(TAG, "onResponse search retrofit: "+response.code());
                            return;
                        }
                    }
                    @Override
                    public void onFailure(Call<List<SchoolData>> call, Throwable t) {
                        Toast.makeText(getContext(), " some connection error in search : "+t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });

            }
        });


        compareButton=root.findViewById(R.id.compareButton);
        buttonAddSchool=root.findViewById(R.id.buttonAddSchool);
        buttonAddSchool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Bottom navBar
                if(ComparisonSchools.size()==5){
                    Toast.makeText(getContext(),"You can compare a max of 5 schools!", Toast.LENGTH_LONG).show();
                }else{
                    LinearLayout linearLayout = v.findViewById(R.id.rootfrag);
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
                    params.height = getScreenHeight();
                    linearLayout.setLayoutParams(params);

                    dialog.show();
                    mBehavior = BottomSheetBehavior.from((View) v.getParent());
                    mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });

        compareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ComparisonSchools.size()<2){
                    Toast.makeText(getContext(), "Select at least 2 schools to compare", Toast.LENGTH_LONG).show();
                }else{
                    Intent it = new Intent( getContext() , StatisticsResult.class);
                    startActivity(it);
                }
            }
        });

        return root;
    }
    private int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
    void getLocation() {
        try {
            locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000000, 0, this::onLocationChanged);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000000, 0, this::onLocationChanged);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        schoolCoordinates.setLongitude(String.valueOf(location.getLongitude()));
        schoolCoordinates.setLatitude(String.valueOf(location.getLatitude()));
        searchFilters.setSchoolCoordinates(schoolCoordinates);
    }

    @Override
    public void onClick(SchoolData schoolDataYes) {
        ComparisonSchools.add(schoolDataYes);
        comparisonAdapter = new ComparisonAdapter(ComparisonSchools);
        recyclerViewCompare.setAdapter(comparisonAdapter);
        dialog.dismiss();
//        v.getParent().removeView();

    }

}