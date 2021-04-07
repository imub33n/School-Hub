package com.example.schoolhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.schoolhub.Adapters.PostViewAdapter;
import com.example.schoolhub.data.LoginResult;
import com.example.schoolhub.data.OnCommentClick;
import com.example.schoolhub.data.PostResult;
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

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserProfile extends AppCompatActivity implements OnCommentClick {

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    ProgressBar progressBar;
    List<PostResult> resource= new ArrayList<>();
    PostViewAdapter adapter;
    TextView postStatus,phoneNoProfile,userNameProfile,emailProfile;
    FirebaseStorage storage= FirebaseStorage.getInstance();
    CircleImageView profilePhoto;
    private final int PICK_IMAGE_REQUEST = 76;
    private Uri filePath;
    StorageReference storageReference ;
    OnCommentClick c=this;
    EditText userNameEdit,phoneNoEdit,oldPasswordEdit,newPasswordEdit,confirmPasswordEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        //retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        storageReference = storage.getReference();

        profilePhoto = findViewById(R.id.profilePhoto);
        postStatus= findViewById(R.id.postStatus);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        phoneNoProfile= findViewById(R.id.phoneNoProfile);
        emailProfile= findViewById(R.id.emailProfile);
        userNameProfile= findViewById(R.id.userNameProfile);
        //setData
        userNameProfile.setText(SignIn.userName);
        //getting userData
        Call<List<LoginResult>> call2 = retrofitInterface.userData(SignIn.userID);
        call2.enqueue(new Callback<List<LoginResult>>() {
            @Override
            public void onResponse(Call<List<LoginResult>> call, Response<List<LoginResult>> response) {
                if (response.code() == 200) {
                    emailProfile.setText(response.body().get(0).getEmail());
                    phoneNoProfile.setText(response.body().get(0).getPhoneNumber());
                    StorageReference storageRef = storage.getReferenceFromUrl(response.body().get(0).getProfilePic());
                    storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(getApplicationContext())
                                    .load(uri)
                                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)         //ALL or NONE as your requirement
                                    .thumbnail(Glide.with(getApplicationContext()).load(R.drawable.ic_image_loading))
                                    .error(R.drawable.ic_image_error)
                                    .into(profilePhoto);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                            Glide.with(getApplicationContext())
                                    .load(R.drawable.ic_image_error)
                                    .fitCenter()
                                    .into(profilePhoto);
                        }
                    });

                }else {
                    Toast.makeText(getApplicationContext(), "Some response code: "+ response.code(), Toast.LENGTH_LONG).show();
                }

            }
            @Override
            public void onFailure(Call<List<LoginResult>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), ""+t, Toast.LENGTH_LONG).show();
            }
        });


        //recycler
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.postViewProfile);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        //get shitpostings
        Call<List<PostResult>> call = retrofitInterface.doGetListResources();
        call.enqueue(new Callback<List<PostResult>>() {
            @Override
            public void onResponse(Call<List<PostResult>> call, Response<List<PostResult>> response) {
                if (response.code() == 200) {
                    progressBar.setVisibility(View.INVISIBLE);
                    for(int i=0;i<response.body().size();i++){
                        if(Objects.equals(response.body().get(i).getUserID(),SignIn.userID)){
                            resource.add(response.body().get(i));
                        }
                        if(i==response.body().size()-1){
                            if(resource.size()==0){
                                postStatus.setText("No Posts Yet!");
                            }else{

                                adapter = new PostViewAdapter(resource,getApplicationContext(),c);
                                adapter.setHasStableIds(true);
                                recyclerView.setAdapter(adapter);
                            }
                        }
                    }

                }else {
                    Toast.makeText(getApplicationContext(), "some response code", Toast.LENGTH_LONG).show();
                }

            }
            @Override
            public void onFailure(Call<List<PostResult>> call, Throwable t) {

                Toast.makeText(getApplicationContext(), ""+t, Toast.LENGTH_LONG).show();
            }
        });

    }

    public void editProfilePic(View view) {
        chooseImage();
    }
    private void chooseImage() {
        //Uri uri = Uri.parse(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());
        Intent intentImages = new Intent();
        intentImages.setType("image/*");
        intentImages.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intentImages, "Select New Profile Pic"), PICK_IMAGE_REQUEST);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            if (data.getData() != null) {
                filePath = data.getData();
                Cursor returnCursor = getApplicationContext().getContentResolver().query(filePath, null, null, null, null);
                returnCursor.moveToFirst();
                String photoUri= String.valueOf(filePath);
                if (photoUri.isEmpty()||photoUri.equals(null)||photoUri.equals("")) {

                } else {
                    uploadImage();
                }
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
                                    HashMap<String, String> map = new HashMap<>();
                                    map.put("profilePic", yoru.toString());

                                    Call<Void> call3 = retrofitInterface.updateDp(SignIn.userID,map);
                                    call3.enqueue(new Callback<Void>() {
                                        @Override
                                        public void onResponse(Call<Void> call, Response<Void> response) {
                                            if (response.code() == 200) {
                                                Toast.makeText(getApplicationContext(), "Photo Updated", Toast.LENGTH_LONG).show();
                                                Glide.with(getApplicationContext())
                                                        .load(filePath)
                                                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)         //ALL or NONE as your requirement
                                                        .thumbnail(Glide.with(getApplicationContext()).load(R.drawable.ic_image_loading))
                                                        .error(R.drawable.ic_image_error)
                                                        .into(profilePhoto);
                                            } else {
                                                Toast.makeText(getApplicationContext(), "Server response code: "+response.code(), Toast.LENGTH_LONG).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Void> call, Throwable t) {
                                            Toast.makeText(getApplicationContext(), "Update error: "+t.getMessage(),
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

    public void editDetails(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog.Builder builder2 = new AlertDialog.Builder(UserProfile.this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();
        View convertview = inflater.inflate(R.layout.edit_profile_dialogue, null);
        LayoutInflater inflater2 = UserProfile.this.getLayoutInflater();
        View convertview2 = inflater2.inflate(R.layout.change_password_user, null);
        userNameEdit=convertview.findViewById(R.id.userNameEdit);
        phoneNoEdit=convertview.findViewById(R.id.phoneNoEdit);
        oldPasswordEdit=convertview2.findViewById(R.id.oldPasswordEdit);
        newPasswordEdit=convertview2.findViewById(R.id.newPasswordEdit);
        confirmPasswordEdit=convertview2.findViewById(R.id.confirmPasswordEdit);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(convertview)
                .setNeutralButton("Change Button", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        builder2.setView(convertview2)
                                // Add action buttons
                                .setPositiveButton("Change Password", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        // edit password ...
                                        HashMap<String, String> maped = new HashMap<>();

                                        if(!newPasswordEdit.getText().toString().isEmpty() && !confirmPasswordEdit.getText().toString().isEmpty()){
                                            if(Objects.equals(newPasswordEdit.getText().toString(),confirmPasswordEdit.getText().toString())){
                                                if(!oldPasswordEdit.getText().toString().isEmpty()){
                                                    maped.put("oldPassword", oldPasswordEdit.getText().toString());
                                                    maped.put("newPassword", newPasswordEdit.getText().toString());
                                                    Call<Void> called = retrofitInterface.updateUserData(SignIn.userID,maped);
                                                    called.enqueue(new Callback<Void>() {
                                                        @Override
                                                        public void onResponse(Call<Void> call, Response<Void> response) {
                                                            if (response.code() == 200) {
                                                                Toast.makeText(UserProfile.this, "Password Updated", Toast.LENGTH_LONG).show();
                                                                dialog.dismiss();
                                                            }else if(response.code()==401){
                                                                Toast.makeText(UserProfile.this, "Old Password is incorrect!", Toast.LENGTH_LONG).show();
                                                            }else {
                                                                Toast.makeText(UserProfile.this, "Server response code: "+response.code(), Toast.LENGTH_LONG).show();
                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(Call<Void> call, Throwable t) {
                                                            Toast.makeText(UserProfile.this, "Update error: "+t.getMessage(),
                                                                    Toast.LENGTH_LONG).show();
                                                        }
                                                    });
                                                }else{
                                                    Toast.makeText(UserProfile.this, "Please write your Old Password",
                                                            Toast.LENGTH_LONG).show();
                                                }
                                            }else{
                                                Toast.makeText(UserProfile.this, "New Password & Confirm New Password must be same",
                                                        Toast.LENGTH_LONG).show();
                                                }
                                        }else{
                                            Toast.makeText(UserProfile.this, "Write a New Password to Update",
                                                    Toast.LENGTH_LONG).show();
                                        }

                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        builder2.create();
                        builder2.show();
                    }
                })
                // Add action buttons
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // edit the user ...

                        HashMap<String, String> maper = new HashMap<>();
                        if(!userNameEdit.getText().toString().isEmpty()){
                            maper.put("username",userNameEdit.getText().toString());
                        }
                        if(!phoneNoEdit.getText().toString().isEmpty()){
                            maper.put("phoneNumber",phoneNoEdit.getText().toString());
                        }
                        if(!userNameEdit.getText().toString().isEmpty() || !phoneNoEdit.getText().toString().isEmpty()){
                            Call<Void> caller = retrofitInterface.updateUserData(SignIn.userID,maper);
                            caller.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if (response.code() == 200) {
                                        Toast.makeText(UserProfile.this, "Updated", Toast.LENGTH_LONG).show();
                                        dialog.dismiss();
                                    } else {
                                        Toast.makeText(UserProfile.this, "Server response code: "+response.code(), Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    Toast.makeText(UserProfile.this, "Update error: "+t.getMessage(),
                                            Toast.LENGTH_LONG).show();
                                }
                            });
                        }else{
                            Toast.makeText(UserProfile.this, "Nothing to update",
                                    Toast.LENGTH_LONG).show();
                            dialog.cancel();
                        }

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.create();
        builder.show();
    }

    @Override
    public void onClick(List<PostResult> postResult, int position) {
//        resource=postResult;
        adapter.notifyItemChanged(position);
    }
}