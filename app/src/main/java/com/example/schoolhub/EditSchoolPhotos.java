package com.example.schoolhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.schoolhub.Adapters.EditPhotosViewAdapter;
import com.example.schoolhub.data.Image;
import com.example.schoolhub.data.PreferenceData;
import com.example.schoolhub.data.SchoolData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;

public class EditSchoolPhotos extends AppCompatActivity {
    private static final int PICK_FROM_GALLERY = 101;
    String photoUri;
    RecyclerView schoolPhotosList;
    EditPhotosViewAdapter editPhotosViewAdapter;
    //public static ArrayList<AttachmentListData> newAttachmentList = new ArrayList<>();
    Uri filePath;
    FirebaseStorage storage= FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    private RetrofitInterface retrofitInterface;
    List<Image> imaging=new ArrayList<>();
    public Toolbar toolbarEdit;
    TextView requestARModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_school_photos);
        toolbarEdit = (Toolbar) findViewById(R.id.toolbarEditSchool);
        schoolPhotosList = findViewById(R.id.schoolPhotosList);
        requestARModel = findViewById(R.id.requestARModel);

        //retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);
        //
        requestARModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //notiStart
                String title="AR Model Request";
                String subTitle = InformationSchoolFragment.thisSchoolData.getSchoolName()+" requested for an AR Model";
                new SendNotification(title,subTitle, PreferenceData.getLoggedInUserData(EditSchoolPhotos.this).get("userID"),MainActivity.SuperAdminID);
                //notiEnd
            }
        });
        Call<List<SchoolData>> call = retrofitInterface.getSchoolData();
        call.enqueue(new Callback<List<SchoolData>>() {
            @Override
            public void onResponse(Call<List<SchoolData>> call, Response<List<SchoolData>> response) {
                if (response.code() == 200) {
                    for(int i=0;i<response.body().size();i++){
                        String adminIdGet=response.body().get(i).getAdminID();
                        if(Objects.equals(adminIdGet, PreferenceData.getLoggedInUserData(EditSchoolPhotos.this).get("userID"))){
                            imaging=response.body().get(i).getImages();
                            editPhotosViewAdapter = new EditPhotosViewAdapter(imaging,EditSchoolPhotos.this);
                            schoolPhotosList.setAdapter(editPhotosViewAdapter);
                        }
                    }
                }else {
                    Toast.makeText(EditSchoolPhotos.this, "CODE: "+response.code(), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<List<SchoolData>> call, Throwable t) {

                Toast.makeText(EditSchoolPhotos.this, ""+t, Toast.LENGTH_LONG).show();
            }
        });


        toolbarEdit.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                onBackPressed();
            }
        });
    }

    public void selectPhotos(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_FROM_GALLERY);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //for photos
        if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK && data != null && data.getData() != null ) {
            if (data.getData() != null) {
                filePath = data.getData();
                Cursor returnCursor = getContentResolver().query(filePath, null, null, null, null);
                returnCursor.moveToFirst();
                photoUri= String.valueOf(filePath);
                if (photoUri.isEmpty()||photoUri.equals(null)||photoUri.equals("")) {
                } else { uploadImage(); }
            }
        }
    }
    private void uploadImage() {
        if(filePath != null)
        {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Log.d(TAG, "photo uploaded: ");
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                            {
                                @Override
                                public void onSuccess(Uri yoru)
                                {
                                    Image image= new Image();
                                    image.setPath(yoru.toString());
                                    HashMap<String, Image> map = new HashMap<>();
                                    map.put("images",image);
                                    //map.put("videos","video");
                                    Call<Void> call3 = retrofitInterface.addImage(AdminDashMainPage.yesSchoolData.get_id(),map);
                                    call3.enqueue(new Callback<Void>() {
                                        @Override
                                        public void onResponse(Call<Void> call, Response<Void> response) {
                                            if (response.code() == 200) {
                                                Toast.makeText(getApplicationContext(), "Added", Toast.LENGTH_LONG).show();
                                                Call<List<SchoolData>> callAgain = retrofitInterface.getSchoolData();
                                                callAgain.enqueue(new Callback<List<SchoolData>>() {
                                                    @Override
                                                    public void onResponse(Call<List<SchoolData>> call, Response<List<SchoolData>> response) {
                                                        if (response.code() == 200) {
                                                            for(int i=0;i<response.body().size();i++){
                                                                String adminIdGet=response.body().get(i).getAdminID();
                                                                if(Objects.equals(adminIdGet, PreferenceData.getLoggedInUserData(EditSchoolPhotos.this).get("userID"))){
                                                                    imaging=response.body().get(i).getImages();
                                                                    editPhotosViewAdapter = new EditPhotosViewAdapter(imaging,EditSchoolPhotos.this);
                                                                    schoolPhotosList.setAdapter(editPhotosViewAdapter);
                                                                }
                                                            }
                                                        }else {
                                                            Toast.makeText(EditSchoolPhotos.this, "CODE: "+response.code(), Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                    @Override
                                                    public void onFailure(Call<List<SchoolData>> call, Throwable t) {

                                                        Toast.makeText(EditSchoolPhotos.this, ""+t, Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                            } else {
                                                Toast.makeText(getApplicationContext(), "Err Code: "+response.code(), Toast.LENGTH_LONG).show();
                                            }
                                        }
                                        @Override
                                        public void onFailure(Call<Void> call, Throwable t) {
                                            Toast.makeText(getApplicationContext(), "Connection Err: "+t.getMessage(),
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }
}
