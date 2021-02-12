package com.example.schoolhub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class EditSchool extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener{

    public Toolbar toolbarEdit;
    private MapView mMapView;
    private GoogleMap googleMap;
    float lat,lng;
    EditText schoolName,schoolEmail,schoolPhoneNo,schoolZip,schoolAbout,schoolAddress;
    RadioButton radioButtonSkolType1,radioButtonSkolType2,radioButtonSkolType3,radioButtonEducationType1,radioButtonEducationType2,radioButtonEducationType3;
    CheckBox checkBoxPrimary,checkBoxMiddle,checkBoxHigher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_school);
        toolbarEdit = (Toolbar) findViewById(R.id.toolbarEditSchool);
        mMapView = findViewById(R.id.mapViewSchoolLocation);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);
        schoolName= findViewById(R.id.schoolName);
        schoolEmail= findViewById(R.id.schoolEmail);
        schoolPhoneNo= findViewById(R.id.schoolPhoneNo);
        schoolZip= findViewById(R.id.schoolZip);
        schoolAbout= findViewById(R.id.schoolAbout);
        schoolAddress= findViewById(R.id.schoolAddress);
        radioButtonSkolType1 = (RadioButton) findViewById(R.id.radioButton1);
        radioButtonSkolType2 = (RadioButton) findViewById(R.id.radioButton2);
        radioButtonSkolType3 = (RadioButton) findViewById(R.id.radioButton3);
        radioButtonEducationType1 = (RadioButton) findViewById(R.id.radioButton7);
        radioButtonEducationType2 = (RadioButton) findViewById(R.id.radioButton8);
        radioButtonEducationType3 = (RadioButton) findViewById(R.id.radioButton9);
        checkBoxPrimary = findViewById(R.id.checkBoxPrimary);
        checkBoxMiddle = findViewById(R.id.checkBoxMiddle);
        checkBoxHigher = findViewById(R.id.checkBoxHigher);
        //setting Data
        schoolName.setText(AdminDashMainPage.yesSchoolData.getSchoolName());
        schoolEmail.setText(AdminDashMainPage.yesSchoolData.getSchoolEmail());
        schoolPhoneNo.setText(AdminDashMainPage.yesSchoolData.getContactNumber());
        schoolZip.setText(AdminDashMainPage.yesSchoolData.getZipCode().toString());
        schoolAbout.setText(AdminDashMainPage.yesSchoolData.getAboutSchool());
        schoolAddress.setText(AdminDashMainPage.yesSchoolData.getSchoolAddress());
        if(Objects.equals(radioButtonSkolType1.getText(),AdminDashMainPage.yesSchoolData.getSchoolType())){
            radioButtonSkolType1.setChecked(true);
        }else if(Objects.equals(radioButtonSkolType2.getText(),AdminDashMainPage.yesSchoolData.getSchoolType())){
            radioButtonSkolType2.setChecked(true);
        }else if(Objects.equals(radioButtonSkolType3.getText(),AdminDashMainPage.yesSchoolData.getSchoolType())){
            radioButtonSkolType3.setChecked(true);
        }
        if(Objects.equals(radioButtonEducationType1.getText(),AdminDashMainPage.yesSchoolData.getEducationType())){
            radioButtonEducationType1.setChecked(true);
        }else if(Objects.equals(radioButtonEducationType2.getText(),AdminDashMainPage.yesSchoolData.getEducationType())){
            radioButtonEducationType2.setChecked(true);
        }else if(Objects.equals(radioButtonEducationType3.getText(),AdminDashMainPage.yesSchoolData.getEducationType())){
            radioButtonEducationType3.setChecked(true);
        }
        List<String> splitStr =  Arrays.stream(AdminDashMainPage.yesSchoolData.getEducationLevel().split(" "))
                .map(String::trim)
                .collect(Collectors.toList());
        for(int i=0;i<splitStr.size();i++){
            if(Objects.equals(splitStr.get(i),checkBoxPrimary.getText())){
                checkBoxPrimary.setChecked(true);
            }
            if(Objects.equals(splitStr.get(i),checkBoxHigher.getText())){
                checkBoxHigher.setChecked(true);
            }
            if(Objects.equals(splitStr.get(i),checkBoxMiddle.getText())){
                checkBoxMiddle.setChecked(true);
            }
        }
        //put map coordinates
        lat= Float.parseFloat(AdminDashMainPage.yesSchoolData.getSchoolCoordinates().getLatitude());
        lng= Float.parseFloat(AdminDashMainPage.yesSchoolData.getSchoolCoordinates().getLongitude());

        //
        toolbarEdit.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                onBackPressed();
            }
        });
    }

    @Override
    public void onMapClick(LatLng latLng) {
        googleMap.clear();
        lat= (float) latLng.latitude;
        lng= (float) latLng.longitude;
        googleMap.addMarker(new MarkerOptions().position(latLng).title("lat="+lat+", lng="+lng).draggable(true));
        //Toast.makeText(this, "Lat " + lat + " " + "Long " +lng, Toast.LENGTH_LONG).show();

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
        googleMap.addMarker(new MarkerOptions().position(new LatLng(lat,lng)).title(AdminDashMainPage.yesSchoolData.getSchoolName()).draggable(true));
        LatLng location = new LatLng(lat, lng);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location,13));
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
}