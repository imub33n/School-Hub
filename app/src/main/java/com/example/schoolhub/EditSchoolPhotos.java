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
import android.os.Environment;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.schoolhub.Adapters.AdapterLanding;
import com.example.schoolhub.Adapters.AttachmentListAdapter;
import com.example.schoolhub.Adapters.EditPhotosViewAdapter;
import com.example.schoolhub.data.AttachmentListData;
import com.example.schoolhub.data.PreferenceData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
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

    public Toolbar toolbarEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_school_photos);
        toolbarEdit = (Toolbar) findViewById(R.id.toolbarEditSchool);
        schoolPhotosList = findViewById(R.id.schoolPhotosList);

        //retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);
        //
        editPhotosViewAdapter = new EditPhotosViewAdapter(AdminDashMainPage.yesSchoolData.getImages(),EditSchoolPhotos.this);
        schoolPhotosList.setAdapter(editPhotosViewAdapter);

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
                            Toast.makeText(getApplicationContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                            {
                                @Override
                                public void onSuccess(Uri yoru)
                                {
                                    HashMap<String, String> mapNewImage = new HashMap<>();
                                    mapNewImage.put("newImages", yoru.toString());

                                    Call<Void> call3 = retrofitInterface.addImage(AdminDashMainPage.yesSchoolData.get_id(),mapNewImage);
                                    call3.enqueue(new Callback<Void>() {
                                        @Override
                                        public void onResponse(Call<Void> call, Response<Void> response) {
                                            if (response.code() == 200) {
                                                Toast.makeText(getApplicationContext(), "Added", Toast.LENGTH_LONG).show();
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
