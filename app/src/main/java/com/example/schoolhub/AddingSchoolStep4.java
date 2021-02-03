package com.example.schoolhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.schoolhub.data.PostResult;
import com.example.schoolhub.data.SchoolData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kofigyan.stateprogressbar.StateProgressBar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddingSchoolStep4 extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {
    private MapView mMapView;
    private GoogleMap googleMap;
    public static String lat, lng;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    SchoolData schoolData = new SchoolData();
    //List<SchoolData> skol;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_school_step4);
        StateProgressBar stateProgressBar = (StateProgressBar) findViewById(R.id.your_state_progress_bar_id);
        stateProgressBar.setStateDescriptionData(AddingSchool.descriptionData);
        stateProgressBar.setStateDescriptionTypeface("fonts/RobotoSlab-Light.ttf");
        stateProgressBar.setStateNumberTypeface("fonts/Questrial-Regular.ttf");
        mMapView = findViewById(R.id.mapViewSchoolLocation);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);
        //retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(SignIn.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap=googleMap;
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.setOnMapClickListener(this);

//        googleMap.animateCamera(CameraUpdateFactory.zoomTo(3));
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
    public void backStep4(View view) {
        Intent it = new Intent( getApplicationContext() , AddingSchoolStep3.class);
        it.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(it);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        googleMap.clear();
        lat= String.valueOf(latLng.latitude);
        lng= String.valueOf(latLng.longitude);
        googleMap.addMarker(new MarkerOptions().position(latLng).title("lat="+lat+", lng="+lng).draggable(true));
        Toast.makeText(this, "Lat " + lat + " " + "Long " +lng, Toast.LENGTH_LONG).show();
    }

    public void nextStep4(View view) {
        if(lat==null||lat.isEmpty()){
            Toast.makeText(this, "Please select a location on map to continue", Toast.LENGTH_LONG).show();
        }else{
           //skol = new ArrayList<>();
            schoolData.setAdminID("11223344");
            schoolData.setSchoolName("Comsats Public School");
            schoolData.setSchoolAddress("Tarlai, Islamabad");
            schoolData.setAboutSchool("Lumber 1 skol in town");
            schoolData.setZipCode(4214);
            schoolData.setContactNumber("090078601");
            schoolData.setEducationLevel("elevel");
            schoolData.setEducationType("etype");
            schoolData.setSchoolType("sType");
            schoolData.setFeeStructure();
            Call<Void> call = retrofitInterface.createSchool(schoolData);
            call.enqueue(new Callback<Void>() {
                             @Override
                             public void onResponse(Call<Void> call, Response<Void> response) {

                             }
                             @Override
                             public void onFailure(Call<Void> call, Throwable t) {

                             }

                         });
            Intent it = new Intent( getApplicationContext() , AddingSchoolCompleted.class);
            it.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(it);

        }

    }
}