package com.example.schoolhub;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.schoolhub.data.EducationLevel;
import com.example.schoolhub.data.SchoolCoordinates;
import com.example.schoolhub.data.SchoolData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import static android.content.ContentValues.TAG;

public class EditSchool extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener{

    public Toolbar toolbarEdit;
    private MapView mMapView;
    private GoogleMap googleMap;
    //float lat,lng;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    public static String lat, lng;
    EditText schoolName;
    EditText schoolEmail;
    EditText schoolPhoneNo;
    EditText schoolZip;
    EditText schoolAbout;
    EditText schoolAddress;
    RadioButton radioButtonSkolType,radioButtonSkolType1,radioButtonSkolType2,
            radioButtonSkolType3,radioButtonEducationType,radioButtonEducationType1,
            radioButtonEducationType2,radioButtonEducationType3;
    RadioGroup radioGroupSkolType,radioGroupEducationType;
    CheckBox checkBoxPrimary,checkBoxMiddle,checkBoxHigher;
    public static EducationLevel EducationLevel= new EducationLevel();

    List<SchoolData> UpdatedSchoolData = new ArrayList<>();
    SchoolData updateSchoolData= new SchoolData();
    SchoolCoordinates schoolCoordinates= new SchoolCoordinates();

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
        //retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);
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
        if(AdminDashMainPage.yesEducationLevel.getPrimary()!=null){
            if(AdminDashMainPage.yesEducationLevel.getPrimary()){
                checkBoxPrimary.setChecked(true);
            }
        }
        if(AdminDashMainPage.yesEducationLevel.getMiddle()!=null){
            if(AdminDashMainPage.yesEducationLevel.getMiddle()){
                checkBoxMiddle.setChecked(true);
            }
        }
        if(AdminDashMainPage.yesEducationLevel.getHigher()!=null){
            if(AdminDashMainPage.yesEducationLevel.getHigher()){
                checkBoxHigher.setChecked(true);
            }
        }
        lat= AdminDashMainPage.yesSchoolData.getSchoolCoordinates().getLatitude();
        lng= AdminDashMainPage.yesSchoolData.getSchoolCoordinates().getLongitude();
//        List<String> splitStr =  Arrays.stream(AdminDashMainPage.yesSchoolData.getEducationLevel().split(" "))
//                .map(String::trim)
//                .collect(Collectors.toList());
//        for(int i=0;i<splitStr.size();i++){
//            if(Objects.equals(splitStr.get(i),checkBoxPrimary.getText())){
//                checkBoxPrimary.setChecked(true);
//            }
//            if(Objects.equals(splitStr.get(i),checkBoxHigher.getText())){
//                checkBoxHigher.setChecked(true);
//            }
//            if(Objects.equals(splitStr.get(i),checkBoxMiddle.getText())){
//                checkBoxMiddle.setChecked(true);
//            }
//        }
        //put map coordinates
//        lat= AdminDashMainPage.yesSchoolData.getSchoolCoordinates().getLatitude();
//        lng= AdminDashMainPage.yesSchoolData.getSchoolCoordinates().getLongitude();

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
        lat= String.valueOf(latLng.latitude);
        lng= String.valueOf(latLng.longitude);
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
        googleMap.addMarker(new MarkerOptions().
                position(new LatLng(
                        Float.parseFloat(AdminDashMainPage.yesSchoolData.getSchoolCoordinates().getLatitude()),
                        Float.parseFloat(AdminDashMainPage.yesSchoolData.getSchoolCoordinates().getLongitude()))).
                title(AdminDashMainPage.yesSchoolData.getSchoolName()).draggable(true));
        LatLng location = new LatLng(Float.parseFloat(AdminDashMainPage.yesSchoolData.getSchoolCoordinates().getLatitude()),
                Float.parseFloat(AdminDashMainPage.yesSchoolData.getSchoolCoordinates().getLongitude()));
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

    public void updateSchoolGeneral(View view) {
        radioGroupSkolType =  findViewById(R.id. radioGroupSkolType);
        radioGroupEducationType = findViewById(R.id.radioGroupEducationType);
        radioButtonSkolType = (RadioButton) findViewById(radioGroupSkolType.getCheckedRadioButtonId());
        radioButtonEducationType = (RadioButton) findViewById(radioGroupEducationType.getCheckedRadioButtonId());
        new AlertDialog.Builder(this)
                //.setTitle("Updating school information!")
                .setMessage("Are you sure you want to update ?")
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //
                        if (!Objects.equals(schoolName.getText(), AdminDashMainPage.yesSchoolData.getSchoolName())) {
                           updateSchoolData.setSchoolName(String.valueOf(schoolName.getText()));
                        }
                        if (!Objects.equals(schoolZip.getText(), AdminDashMainPage.yesSchoolData.getZipCode())) {
                            updateSchoolData.setZipCode(Integer.parseInt(String.valueOf(schoolZip.getText())));
                        }
                        if (!Objects.equals(schoolAbout.getText(), AdminDashMainPage.yesSchoolData.getAboutSchool())) {
                            updateSchoolData.setAboutSchool(String.valueOf(schoolAbout.getText()));
                        }
                        if (!Objects.equals(schoolAddress.getText(), AdminDashMainPage.yesSchoolData.getSchoolAddress())) {
                            updateSchoolData.setSchoolAddress(String.valueOf(schoolAddress.getText()));
                        }
                        if (!Objects.equals(schoolEmail.getText(), AdminDashMainPage.yesSchoolData.getSchoolEmail())) {
                            updateSchoolData.setSchoolEmail(String.valueOf(schoolEmail.getText()));
                        }
                        if (!Objects.equals(schoolPhoneNo.getText(), AdminDashMainPage.yesSchoolData.getContactNumber())) {
                            updateSchoolData.setContactNumber(String.valueOf(schoolPhoneNo.getText()));
                        }
                        if(!Objects.equals(radioGroupSkolType.getCheckedRadioButtonId(),AdminDashMainPage.yesSchoolData.getSchoolType())){
                            updateSchoolData.setSchoolType(String.valueOf(radioButtonSkolType.getText()));
                        }
                        if(!Objects.equals(radioGroupEducationType.getCheckedRadioButtonId(),AdminDashMainPage.yesSchoolData.getEducationType())){
                            updateSchoolData.setEducationType(String.valueOf(radioButtonEducationType.getText()));
                        }
                        if(checkBoxPrimary.isChecked()){
                            EducationLevel.setPrimary(true);
//                            EducationLevel=EducationLevel.concat(" "+checkBoxPrimary.getText().toString());
                        }else{
                            EducationLevel.setPrimary(false);
                        }
                        if(checkBoxMiddle.isChecked()){
                            EducationLevel.setMiddle(true);
//                            EducationLevel=EducationLevel.concat(" "+checkBoxMiddle.getText().toString());
                        }else{
                            EducationLevel.setMiddle(false);
                        }
                        if(checkBoxHigher.isChecked()){
                            EducationLevel.setHigher(true);
//                            EducationLevel=EducationLevel.concat(" "+checkBoxHigher.getText().toString());
                        }else{
                            EducationLevel.setHigher(false);
                        }
                        if(!Objects.equals(EducationLevel,AdminDashMainPage.yesSchoolData.getEducationLevel())){
                            updateSchoolData.setEducationLevel(EducationLevel);
                        }

                        schoolCoordinates.setLatitude(lat);
                        schoolCoordinates.setLongitude(lng);
                        updateSchoolData.setSchoolCoordinates(schoolCoordinates);


                        UpdatedSchoolData.add(updateSchoolData);
                        // backend connection under this line
                        Call<SchoolData> call = retrofitInterface.putSchoolData(AdminDashMainPage.yesSchoolData.get_id(),updateSchoolData);
                        call.enqueue(new Callback<SchoolData>() {
                            @Override
                            public void onResponse(Call<SchoolData> call, Response<SchoolData> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(EditSchool.this, "Updated", Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(EditSchool.this, "Err Code: "+response.code(), Toast.LENGTH_LONG).show();
                                }
                            }
                            @Override
                            public void onFailure(Call<SchoolData> call, Throwable t) {
                                Toast.makeText(EditSchool.this, "Err Connection: "+t, Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                })
                .setNegativeButton("Cancel", null)
                //.setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void cancelEditSchool(View view) {
        onBackPressed();
    }
}