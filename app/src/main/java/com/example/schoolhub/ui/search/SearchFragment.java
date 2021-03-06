package com.example.schoolhub.ui.search;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.schoolhub.Adapters.SearchResultAdapter;
import com.example.schoolhub.AddingSchool.AddingSchool;
import com.example.schoolhub.AdminDashboard;
import com.example.schoolhub.LandingScreen;
import com.example.schoolhub.MainActivity;
import com.example.schoolhub.RetrofitInterface;
import com.example.schoolhub.data.Fee;
import com.example.schoolhub.data.OnItemClick;
import com.example.schoolhub.data.SchoolCoordinates;
import com.example.schoolhub.data.SchoolData;
import com.example.schoolhub.data.SearchFilters;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.internal.Constants;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolhub.R;

import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;


public class SearchFragment extends Fragment implements OnMapReadyCallback, LocationListener {
    String jaga="";
    OnItemClick m;
    EditText searchEditText,editTextMin,editTextMax,distanceMax;
    Button search,reset;
    TextView statusSearch;
    LocationManager locationManager;
    Double lat,lng;
    private RadioButton radioButton1,radioButton2,radioButton3;
    private RadioGroup group1,group2,group3;
    LinearLayout filterHeading,filtersLayout;
    public Boolean theBoolean =true;
    SearchFilters searchFilters= new SearchFilters();
    Fee fee=new Fee();
    Object A;
    SearchFilters searchFiltersReset= new SearchFilters();
    Fee feeReset=new Fee();
    SchoolCoordinates schoolCoordinates=new SchoolCoordinates();
    GoogleMap googleMap;
    List<SchoolData> schoolData;
    List<SchoolData> allSchoolsData;
    //SchoolCoordinates allSchoolCoordinates=new SchoolCoordinates();
    SearchResultAdapter searchResultAdapter;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    private BottomSheetBehavior mBehavior;
    TextView searchButton;
    private MapView mMapView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search, container, false);
        searchButton= root.findViewById(R.id.searchText);
        getLocation();
        retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View v = getLayoutInflater().inflate(R.layout.fragment_filters, null);

                //Bottom navBar
                BottomSheetDialog dialog = new BottomSheetDialog(getContext());
                LinearLayout linearLayout = v.findViewById(R.id.rootfrag);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
                params.height = getScreenHeight();
                linearLayout.setLayoutParams(params);
                dialog.setContentView(v);
                dialog.show();
                mBehavior = BottomSheetBehavior.from((View) v.getParent());
                mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

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

                //recycler
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
                                Toast.makeText(getContext(), " some connection error in search : "+t.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                });
            }
        });

        mMapView = root.findViewById(R.id.mapView2);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);

        return root;
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
    void getSchoolMarkers(){

        Call<List<SchoolData>> call2 = retrofitInterface.getSchoolData();
        call2.enqueue(new Callback<List<SchoolData>>() {
            @Override
            public void onResponse(Call<List<SchoolData>> call, Response<List<SchoolData>> response) {
                if (response.code() == 200) {
                    Log.d("TAG",response.code()+"");
                    allSchoolsData =  response.body();

                    for(int i=0;i<allSchoolsData.size();i++){
                        lat= Double.valueOf(allSchoolsData.get(i).getSchoolCoordinates().getLatitude());
                        lng= Double.valueOf(allSchoolsData.get(i).getSchoolCoordinates().getLongitude());
                        Log.d(TAG, "onResponse: _________this skol____"+allSchoolsData.get(i).getSchoolName());
                        Log.d(TAG, "onResponse: ______________"+lat+"_________"+lng);
                        while(googleMap==null){
                            try {
                                A.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Log.d(TAG, "__________________:waiting for googleMap:__________________ ");
                        }
                        googleMap.addMarker(new MarkerOptions().position(new LatLng(lat,lng)).title(allSchoolsData.get(i).getSchoolName()));
                    }
                }else {
                    Toast.makeText(getContext(), "ERROR CODE: "+response.code(), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<List<SchoolData>> call2, Throwable t) {
                Toast.makeText(getContext(), ""+t, Toast.LENGTH_LONG).show();
            }
        });

    }
    private int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
//        googleMap.addMarker(new MarkerOptions().position(new LatLng(33.6376464, 73.1467503)).title("Current Location"));
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        this.googleMap=googleMap;
        getSchoolMarkers();
        googleMap.setMyLocationEnabled(true);

    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();

        mMapView.onResume();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


    @Override
    public void onLocationChanged(Location location) {
        schoolCoordinates.setLongitude(String.valueOf(location.getLongitude()));
        schoolCoordinates.setLatitude(String.valueOf(location.getLatitude()));
        searchFilters.setSchoolCoordinates(schoolCoordinates);
        LatLng l = new LatLng(Double.valueOf(schoolCoordinates.getLatitude()),Double.valueOf(schoolCoordinates.getLongitude()));
        Log.d(TAG, "onResponse: ________________))_______________)()(_______"+l);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(l,15));
    }
}