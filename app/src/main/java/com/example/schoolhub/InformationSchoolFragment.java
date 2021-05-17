package com.example.schoolhub;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import com.example.schoolhub.Adapters.SearchResultAdapter;
import com.example.schoolhub.Adapters.SlideAdapter;
import com.example.schoolhub.data.PreferenceData;
import com.example.schoolhub.data.SchoolCoordinates;
import com.example.schoolhub.data.SchoolData;
import com.example.schoolhub.ui.statistics.StatisticsFragment;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;

public class InformationSchoolFragment extends Fragment implements LocationListener, OnMapReadyCallback  {
    //retrofit
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    List<SchoolData> schoolData;
    //logged in user id
    static String adminId;
    //mattching admin
    String adminIdGet;
    ProgressBar progressBar;
    public static SchoolData thisSchoolData=new SchoolData();

    ViewPager2 viewPager;
    SlideAdapter adapter;
    Button ModellButton;
    private MapView mMapView;
    GoogleMap googleMap;

    SchoolCoordinates schoolCoordinates=new SchoolCoordinates();
    LocationManager locationManager;
    DecimalFormat adf = new DecimalFormat("0.0");

    TextView schoolName,tContact,tEmail,tAddress,tZip,tAbout,tDistance,tSchoolType,tEducationLevel,tCourseType;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_information_school, container, false);

        getLocation();

        progressBar = (ProgressBar) root.findViewById(R.id.progressBar);
        viewPager = (ViewPager2) root.findViewById(R.id.viewPagerSlider);
        ModellButton = (Button) root.findViewById(R.id.view3dModellButton);

        ModellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent( getActivity().getApplicationContext() , ARModel.class);
                startActivity(it);
            }
        });

        mMapView=root.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);
        //data
        schoolName=root.findViewById(R.id.schoolName);
        tContact=root.findViewById(R.id.tContact);
        tEmail=root.findViewById(R.id.tEmail);
        tAddress=root.findViewById(R.id.tAddress);
        tZip=root.findViewById(R.id.tZip);
        tAbout=root.findViewById(R.id.tAbout);
        tDistance=root.findViewById(R.id.tDistance);
//        tSchoolType=root.findViewById(R.id.tSchoolType);
//        tEducationLevel=root.findViewById(R.id.tEducationLevel);
//        tCourseType=root.findViewById(R.id.tCourseType);

        //retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        if(Objects.equals(PreferenceData.getLoggedInUserData(getContext()).get("userType"),"School")){
            adminId=PreferenceData.getLoggedInUserData(getContext()).get("userID");
        }
        else if(SearchResultAdapter.userIDsearch!=""){
            adminId=SearchResultAdapter.userIDsearch;
            Log.d(TAG, "onCreateView: _____________adminId=SearchResultAdapter.userIDsearch;_____________"+adminId);
        }


        Call<List<SchoolData>> call = retrofitInterface.getSchoolData();
        call.enqueue(new Callback<List<SchoolData>>() {
            @Override
            public void onResponse(Call<List<SchoolData>> call, Response<List<SchoolData>> response) {
                if (response.code() == 200) {
                    schoolData =  response.body();
                    //Toast.makeText(getContext(),schoolData.toString(),Toast.LENGTH_SHORT).show();

                    for(int i=0;i<schoolData.size();i++){
                        adminIdGet=schoolData.get(i).getAdminID();
                        if(Objects.equals(adminIdGet, adminId)){
                            progressBar.setVisibility(View.INVISIBLE);
                            thisSchoolData=schoolData.get(i);

                            //put photos
                            adapter= new SlideAdapter(thisSchoolData.getImages(),getContext());
                            viewPager.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);
                            viewPager.setAdapter(adapter);

                            //put map coordinates
                            float lat= Float.parseFloat(thisSchoolData.getSchoolCoordinates().getLatitude());
                            float lng= Float.parseFloat(thisSchoolData.getSchoolCoordinates().getLongitude());
                            googleMap.addMarker(new MarkerOptions().position(new LatLng(lat,lng)).title(thisSchoolData.getSchoolName()).draggable(true));
                            LatLng location = new LatLng(lat, lng);
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location,13));
//                            Log.d(TAG, "onResponse:____________________lng__'"+schoolData.get(i).getSchoolCoordinates().getLongitude()+"'");
                            //putting data
                            schoolName.setText(thisSchoolData.getSchoolName());
                            //tSchoolType.setText(thisSchoolData.getSchoolType());
                            tAbout.setText(thisSchoolData.getAboutSchool());
                            tEmail.setText(thisSchoolData.getSchoolEmail());
                            tContact.setText(thisSchoolData.getContactNumber());
                            tAddress.setText(thisSchoolData.getSchoolAddress());
                            tZip.setText(thisSchoolData.getZipCode().toString());


//                            tEducationLevel.setText(thisSchoolData.getEducationLevel());
//                            tCourseType.setText(thisSchoolData.getEducationType());
                        }
                    }
                }else {
                    Toast.makeText(getContext(), "CODE: "+response.code(), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<List<SchoolData>> call, Throwable t) {

                Toast.makeText(getContext(), ""+t, Toast.LENGTH_LONG).show();
            }
        });


        return root;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap=googleMap;
        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(3));
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
    public void onLocationChanged(@NonNull Location location) {
        schoolCoordinates.setLongitude(String.valueOf(location.getLongitude()));
        schoolCoordinates.setLatitude(String.valueOf(location.getLatitude()));
//distance
        float[] results = new float[1];
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Location.distanceBetween(
                        Double.parseDouble(schoolCoordinates.getLatitude()),
                        Double.parseDouble(schoolCoordinates.getLongitude()),
                        Double.parseDouble(thisSchoolData.getSchoolCoordinates().getLatitude()),
                        Double.parseDouble(thisSchoolData.getSchoolCoordinates().getLongitude()),
                        results);
                String yes=adf.format(results[0]/1000)+" km Away";
                tDistance.setText(yes);
            }
        }, 1000);//1 second delay
    }
}