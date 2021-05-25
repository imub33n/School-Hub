package com.example.schoolhub.AddingSchool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.User;
import com.example.schoolhub.MainActivity;
import com.example.schoolhub.R;
import com.example.schoolhub.RetrofitInterface;
import com.example.schoolhub.SignIn;
import com.example.schoolhub.data.FeeStructure;
import com.example.schoolhub.data.Image;
import com.example.schoolhub.data.PreferenceData;
import com.example.schoolhub.data.SchoolCoordinates;
import com.example.schoolhub.data.SchoolData;
import com.example.schoolhub.data.Video;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kofigyan.stateprogressbar.StateProgressBar;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import static android.content.ContentValues.TAG;
public class AddingSchoolStep4 extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {
    private MapView mMapView;
    private GoogleMap googleMap;
    public static String lat, lng;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    SchoolData schoolData = new SchoolData();
    SchoolCoordinates schoolCoordinates= new SchoolCoordinates();
    List<FeeStructure> feeStructures = new ArrayList<>();
    FeeStructure feeStructure= new FeeStructure();
    FeeStructure feeStructure2= new FeeStructure();
    FeeStructure feeStructure3= new FeeStructure();
    List<Image> images= new ArrayList<>();

    FirebaseStorage storage;
    StorageReference storageReference;

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
        //firebase
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        //retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);
//        for(int i=0;i<AddingSchoolStep2.newAttachmentList.size();i++){
//            Image image = new Image();
//            image.setPath(AddingSchoolStep2.newAttachmentList.get(i).getImageID());
//            images.add(i,image);
//            //images.add(0,image.setPath(AddingSchoolStep2.newAttachmentList.get(0).getImageID()));
//        }
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
        //Toast.makeText(this, "Lat " + lat + " " + "Long " +lng, Toast.LENGTH_LONG).show();

        schoolCoordinates.setLatitude(lat);
        schoolCoordinates.setLongitude(lng);
    }

    public void nextStep4(View view) {
        if(lat==null||lat.isEmpty()){
            Toast.makeText(this,"Please select a location on map to continue", Toast.LENGTH_LONG).show();
        }else {
            //skol = new ArrayList<>();
            uploadImage();
        }
    }
    private void uploadImage() {
        for(int i=0;i<AddingSchoolStep2.newAttachmentList.size();i++) {
//            Image image = new Image();
            //convert string to uri
            Uri filePath = Uri.parse(AddingSchoolStep2.newAttachmentList.get(i).getImageID());
            //Toast.makeText(this,AddingSchoolStep2.newAttachmentList.get(i).getImageID(), Toast.LENGTH_LONG).show();
            if (filePath != null) {
                ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Uploading...");
                //Toast.makeText(getApplicationContext(), "Uploading Photos...", Toast.LENGTH_SHORT).show();
                progressDialog.show();
                StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
                int finalI1 = i;
                ref.putFile(filePath)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                progressDialog.dismiss();
                                //Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri yoru) {
                                        Image image = new Image();
                                        image.setPath(yoru.toString());
                                        images.add(image);
                                        schoolData.setImages(images);
                                        for(int ima=0;ima<schoolData.getImages().size();ima++){
                                            Log.d(TAG, "__________check if same images_____________: "+schoolData.getImages().get(ima).getPath());

                                        }
                                        if((AddingSchoolStep2.newAttachmentList.size()-1)== finalI1){
                                            schoolData.setAdminID(PreferenceData.getLoggedInUserData(getApplicationContext()).get("userID"));
                                            schoolData.setSchoolName(AddingSchool.schoolNames);
                                            schoolData.setSchoolAddress(AddingSchool.schoolAddresss);
                                            schoolData.setAboutSchool(AddingSchool.schoolAbouts);
                                            schoolData.setZipCode(AddingSchool.schoolZipi);
                                            schoolData.setSchoolEmail(AddingSchool.schoolEmails);
                                            schoolData.setContactNumber(AddingSchool.schoolPhoneNos);
                                            schoolData.setEducationLevel(AddingSchool.EducationLevel);
                                            schoolData.setEducationType(AddingSchool.EducationType);
                                            schoolData.setSchoolType(AddingSchool.SkolType);
                                            schoolData.setSchoolCoordinates(schoolCoordinates);
                                            if(AddingSchoolStep3.step1){
                                                feeStructure.setGroup(AddingSchoolStep3.To_From);
                                                feeStructure.setAdmissionFee(AddingSchoolStep3.iAddFee);
                                                feeStructure.setExamFee(AddingSchoolStep3.iExamFee);
                                                feeStructure.setLabFee(AddingSchoolStep3.ilab);
                                                feeStructure.setMonthlyFee(AddingSchoolStep3.imonthlyFee);
                                                feeStructure.setLibraryFee(AddingSchoolStep3.ilibrary);
                                                feeStructure.setOthersFee(AddingSchoolStep3.iothers);
                                                feeStructure.setSportsFee(AddingSchoolStep3.isports);
                                                feeStructure.setTotalAdmissionFee(AddingSchoolStep3.itotalAddFee);
                                                feeStructure.setTutionFee(AddingSchoolStep3.iTutionFee);
                                                feeStructures.add(feeStructure);
                                                schoolData.setFeeStructure(feeStructures);
                                            }if(AddingSchoolStep3.step2){
                                                feeStructure2.setGroup(AddingSchoolStep3.To_From2);
                                                feeStructure2.setAdmissionFee(AddingSchoolStep3.iAddFee2);
                                                feeStructure2.setExamFee(AddingSchoolStep3.iExamFee2);
                                                feeStructure2.setLabFee(AddingSchoolStep3.ilab2);
                                                feeStructure2.setMonthlyFee(AddingSchoolStep3.imonthlyFee2);
                                                feeStructure2.setLibraryFee(AddingSchoolStep3.ilibrary2);
                                                feeStructure2.setOthersFee(AddingSchoolStep3.iothers2);
                                                feeStructure2.setSportsFee(AddingSchoolStep3.isports2);
                                                feeStructure2.setTotalAdmissionFee(AddingSchoolStep3.itotalAddFee2);
                                                feeStructure2.setTutionFee(AddingSchoolStep3.iTutionFee2);
                                                feeStructures.add(feeStructure2);
                                                schoolData.setFeeStructure(feeStructures);
                                            }if(AddingSchoolStep3.step3){
                                                feeStructure3.setGroup(AddingSchoolStep3.To_From3);
                                                feeStructure3.setAdmissionFee(AddingSchoolStep3.iAddFee3);
                                                feeStructure3.setExamFee(AddingSchoolStep3.iExamFee3);
                                                feeStructure3.setLabFee(AddingSchoolStep3.ilab3);
                                                feeStructure3.setMonthlyFee(AddingSchoolStep3.imonthlyFee3);
                                                feeStructure3.setLibraryFee(AddingSchoolStep3.ilibrary3);
                                                feeStructure3.setOthersFee(AddingSchoolStep3.iothers3);
                                                feeStructure3.setSportsFee(AddingSchoolStep3.isports3);
                                                feeStructure3.setTotalAdmissionFee(AddingSchoolStep3.itotalAddFee3);
                                                feeStructure3.setTutionFee(AddingSchoolStep3.iTutionFee3);
                                                feeStructures.add(feeStructure3);
                                                schoolData.setFeeStructure(feeStructures);
                                            }

                                            schoolData.setVideos("video link here");
                                            schoolData.setSchoolIcon("https://firebasestorage.googleapis.com/v0/b/okay-945dc.appspot.com/o/images%2F7cb7677b-bf68-4131-b681-308f4175d8b3?alt=media&token=d9f5b8d9-4dc3-4d7f-a478-5753cb56f18b");
                                            Call<Void> call = retrofitInterface.createSchool(schoolData);
                                            call.enqueue(new Callback<Void>() {
                                                @Override
                                                public void onResponse(Call<Void> call, Response<Void> response) {
                                                    if(response.code()==201){
                                                        Toast.makeText(getApplicationContext(),"Added", Toast.LENGTH_LONG).show();
//                                                        //create chat user
//                                                        String authKey = MainActivity.authKey; // Replace with your App Auth Key
//                                                        User user = new User();
//                                                        user.setUid(response.body().get("_id")); // Replace with the UID for the user to be created
//                                                        user.setName(response.body().get("username")); // Replace with the name of the user
//
//                                                        CometChat.createUser(user, authKey, new CometChat.CallbackListener<User>() {
//                                                            @Override
//                                                            public void onSuccess(User user) {
//                                                                Log.d("createUser", user.toString());
//                                                            }
//
//                                                            @Override
//                                                            public void onError(CometChatException e) {
//                                                                Log.e("createUser", e.getMessage());
//                                                            }
//                                                        });
                                                        Intent it = new Intent( getApplicationContext() , AddingSchoolCompleted.class);
                                                        it.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                        startActivity(it);
                                                    }else{
                                                        Toast.makeText(getApplicationContext(),"Skol not added! "+response.code(), Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                                @Override
                                                public void onFailure(Call<Void> call, Throwable t) {
                                                    Toast.makeText(getApplicationContext(),"Error: "+t, Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                        .getTotalByteCount());
                                //Toast.makeText(getApplicationContext(),"Uploaded " + (int) progress + "%" , Toast.LENGTH_SHORT).show();
                                progressDialog.setMessage("Uploaded " + (int) progress + "%");
                            }
                        });
            }
        }
    }
}