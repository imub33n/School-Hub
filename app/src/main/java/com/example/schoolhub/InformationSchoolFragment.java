package com.example.schoolhub;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.viewpager2.widget.ViewPager2;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.schoolhub.data.FeeStructure;
import com.example.schoolhub.data.Image;
import com.example.schoolhub.data.SchoolCoordinates;
import com.example.schoolhub.data.SchoolData;
import com.example.schoolhub.data.Video;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;

public class InformationSchoolFragment extends Fragment implements OnMapReadyCallback  {
    //retrofit
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    List<SchoolData> schoolData;
    static String adminId="602231442d72da3314e30809";
    String adminIdGet;

    public static SchoolData thisSchoolData=new SchoolData();

    ViewPager2 viewPager;
    SlideAdapter adapter;
    Button ModellButton;
    private MapView mMapView;
    GoogleMap googleMap;

    TextView schoolName,tContact,tEmail,tAddress,tZip,tAbout,tSchoolType,tEducationLevel,tCourseType;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_information_school, container, false);

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
        tSchoolType=root.findViewById(R.id.tSchoolType);
        tEducationLevel=root.findViewById(R.id.tEducationLevel);
        tCourseType=root.findViewById(R.id.tCourseType);

        //retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(SignIn.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        Call<List<SchoolData>> call = retrofitInterface.getSchoolData();
        call.enqueue(new Callback<List<SchoolData>>() {
            @Override
            public void onResponse(Call<List<SchoolData>> call, Response<List<SchoolData>> response) {
                if (response.code() == 200) {
                    Log.d("TAG",response.code()+"");
                    schoolData =  response.body();
                    //Toast.makeText(getContext(),schoolData.toString(),Toast.LENGTH_SHORT).show();

                    for(int i=0;i<schoolData.size();i++){
                        adminIdGet=schoolData.get(i).getAdminID();
                        if(Objects.equals(adminIdGet, adminId)){
                            thisSchoolData=schoolData.get(i);
//                            for(int j=0;j<thisSchoolData.getImages().size();j++){
//                                Log.d(TAG, "onResponse: ______"+j+"_as_____________"+thisSchoolData.getImages().get(j).getPath());
//                            }
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
                            tSchoolType.setText(thisSchoolData.getSchoolType());
                            tAbout.setText(thisSchoolData.getAboutSchool());
                            tAddress.setText(thisSchoolData.getSchoolAddress());
                            tEmail.setText(thisSchoolData.getSchoolEmail());
                            tContact.setText(thisSchoolData.getContactNumber());
                            tZip.setText(thisSchoolData.getZipCode().toString());
                            tEducationLevel.setText(thisSchoolData.getEducationLevel());
                            tCourseType.setText(thisSchoolData.getEducationType());
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

//        float lat= Float.parseFloat(schoolCoordinates.getLatitude());
//        float lng= Float.parseFloat(thisSchoolData.getSchoolCoordinates().getLongitude());
//        googleMap.addMarker(new MarkerOptions().position(new LatLng(lat,lng)).title(thisSchoolData.getSchoolName()).draggable(true));

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

}