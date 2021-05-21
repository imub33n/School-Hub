package com.example.schoolhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.AppSettings;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.core.UsersRequest;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.User;
import com.cometchat.pro.uikit.ui_components.messages.message_list.CometChatMessageListActivity;
import com.cometchat.pro.uikit.ui_resources.constants.UIKitConstants;
import com.example.schoolhub.Adapters.PostViewAdapter;
import com.example.schoolhub.data.LoginResult;
import com.example.schoolhub.data.OnCommentClick;
import com.example.schoolhub.data.PostResult;
import com.example.schoolhub.data.PreferenceData;
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
import static android.content.ContentValues.TAG;

public class UserProfile extends AppCompatActivity implements OnCommentClick {

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    ProgressBar progressBar;
    List<PostResult> resource= new ArrayList<>();
    PostViewAdapter adapter;
    TextView postStatus,phoneNoProfile,userNameProfile,emailProfile,editDetails,send_msg;
    ImageView editDP;
    FirebaseStorage storage= FirebaseStorage.getInstance();
    CircleImageView profilePhoto;
    private final int PICK_IMAGE_REQUEST = 76;
    private Uri filePath;
    StorageReference storageReference ;
    OnCommentClick c=this;
    EditText userNameEdit,phoneNoEdit,oldPasswordEdit,newPasswordEdit,confirmPasswordEdit;

    Boolean themFriends=false;

    private int limit = 30;
    List <User> listOfFriends= new ArrayList<>();

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
        //chat
        AppSettings appSettings=new AppSettings.AppSettingsBuilder().subscribePresenceForAllUsers().setRegion(MainActivity.region).build();
        CometChat.init(this, MainActivity.appID,appSettings, new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String successMessage) {
                //UIKitSettings.setAuthKey(authKey);
                CometChat.setSource("ui-kit","android","java");
                Log.d(TAG, "Initialization completed successfully");
            }

            @Override
            public void onError(CometChatException e) {
                Log.d(TAG, "Initialization failed with exception: " + e.getMessage());
            }
        });
        storageReference = storage.getReference();
        editDP = findViewById(R.id.editDP);
        editDetails = findViewById(R.id.editDetails);
        profilePhoto = findViewById(R.id.profilePhoto);
        postStatus= findViewById(R.id.postStatus);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        phoneNoProfile= findViewById(R.id.phoneNoProfile);
        emailProfile= findViewById(R.id.emailProfile);
        userNameProfile= findViewById(R.id.userNameProfile);
        send_msg= findViewById(R.id.send_msg);

        UsersRequest usersRequest = new UsersRequest.UsersRequestBuilder()
                .setLimit(limit)
                .friendsOnly(true)
                .build();
        usersRequest.fetchNext(new CometChat.CallbackListener<List<User>>() {
            @Override
            public void onSuccess(List <User> list) {
                listOfFriends=list;
                Log.d(TAG, "User list received: " + list.size());
            }
            @Override
            public void onError(CometChatException e) {
                Log.d(TAG, "User list fetching failed with exception: " + e.getMessage());
            }
        });

        if(!Objects.equals(getIntent().getStringExtra("EXTRA_USER_ID"),PreferenceData.getLoggedInUserData(this).get("userID"))){
            editDetails.setLayoutParams(new LinearLayout.LayoutParams(0,0));
            editDP.setLayoutParams(new FrameLayout.LayoutParams(0, 0));
        }else{
            send_msg.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
        }
        //send msg button
        send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                for(int i=0;i<listOfFriends.size();i++){

                    if(getIntent().getStringExtra("EXTRA_USER_ID").equals(listOfFriends.get(i).getUid())){
                        themFriends=true;
                    }
                    if(i==listOfFriends.size()-1){
                        if(!themFriends){
                            //add as friend
                            HashMap<String, String> mapUsers = new HashMap<>();

                            mapUsers.put( "userID", PreferenceData.getLoggedInUserData(UserProfile.this).get("userID") );
                            mapUsers.put( "otherID", getIntent().getStringExtra("EXTRA_USER_ID") );
                            //POST REQUEST
                            Call<Void> call = retrofitInterface.addChatFriend(mapUsers);
                            call.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    openChat(UserProfile.this,getIntent().getStringExtra("EXTRA_USER_ID"));
                                    if (response.isSuccessful()) {
                                        Toast.makeText(UserProfile.this, "Yes", Toast.LENGTH_LONG).show();
                                    }else{
                                        Toast.makeText(UserProfile.this, "Error Code: "+response.code(), Toast.LENGTH_LONG).show();
                                    }
                                }
                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    //Toast.makeText(UserProfile.this, "Connection Error: "+t.getMessage(), Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.INVISIBLE);
                                    openChat(UserProfile.this,getIntent().getStringExtra("EXTRA_USER_ID"));
                                }
                            });
                        }else{
                            progressBar.setVisibility(View.INVISIBLE);
                            openChat(UserProfile.this,getIntent().getStringExtra("EXTRA_USER_ID"));
                        }
                    }
                }//added as friend in chat if not already

            }
        });

        //getting userData
        Call<List<LoginResult>> call2 = retrofitInterface.userData(getIntent().getStringExtra("EXTRA_USER_ID"));
        call2.enqueue(new Callback<List<LoginResult>>() {
            @Override
            public void onResponse(Call<List<LoginResult>> call, Response<List<LoginResult>> response) {
                if (response.code() == 200) {
                    //setData
                    userNameProfile.setText(response.body().get(0).getUsername());
                    emailProfile.setText(response.body().get(0).getEmail());
                    phoneNoProfile.setText(response.body().get(0).getPhoneNumber());
                    StorageReference storageRef = storage.getReferenceFromUrl(response.body().get(0).getProfilePic());
                    storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(getApplicationContext())
                                    .load(uri)
                                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)         //ALL or NONE as your requirement
                                    .thumbnail(Glide.with(getApplicationContext()).load(R.drawable.ic_img_loading))
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
                        if(Objects.equals(response.body().get(i).getUserID(),getIntent().getStringExtra("EXTRA_USER_ID"))){
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

                                    Call<Void> call3 = retrofitInterface.updateDp(PreferenceData.getLoggedInUserData(getApplicationContext()).get("userID"),map);
                                    call3.enqueue(new Callback<Void>() {
                                        @Override
                                        public void onResponse(Call<Void> call, Response<Void> response) {
                                            if (response.code() == 200) {
                                                Toast.makeText(getApplicationContext(), "Photo Updated", Toast.LENGTH_LONG).show();
                                                Glide.with(getApplicationContext())
                                                        .load(filePath)
                                                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)         //ALL or NONE as your requirement
                                                        .thumbnail(Glide.with(getApplicationContext()).load(R.drawable.ic_img_loading))
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
                .setNeutralButton("Change Password", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        builder2.setView(convertview2)
                                // Add action buttons
                                .setPositiveButton("Update Password", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        // edit password ...
                                        HashMap<String, String> maped = new HashMap<>();

                                        if(!newPasswordEdit.getText().toString().isEmpty() && !confirmPasswordEdit.getText().toString().isEmpty()){
                                            if(Objects.equals(newPasswordEdit.getText().toString(),confirmPasswordEdit.getText().toString())){
                                                if(!oldPasswordEdit.getText().toString().isEmpty()){
                                                    maped.put("oldPassword", oldPasswordEdit.getText().toString());
                                                    maped.put("newPassword", newPasswordEdit.getText().toString());
                                                    Call<Void> called = retrofitInterface.updateUserData(PreferenceData.getLoggedInUserData(getApplicationContext()).get("userID"),maped);
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
                            Call<Void> caller = retrofitInterface.updateUserData(PreferenceData.getLoggedInUserData(getApplicationContext()).get("userID"),maper);
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
    public static void openChat(Context c,String uID){
        CometChat.getUser(uID, new CometChat.CallbackListener<User>() {
            @Override
            public void onSuccess(User user) {
                Log.d(TAG, "User details fetched for user: " + user.toString());
                Intent intent = new Intent(c, CometChatMessageListActivity.class);
                intent.putExtra(UIKitConstants.IntentStrings.TYPE, CometChatConstants.RECEIVER_TYPE_USER);
                intent.putExtra(UIKitConstants.IntentStrings.NAME,user.getName());
                intent.putExtra(UIKitConstants.IntentStrings.UID,user.getUid());
                intent.putExtra(UIKitConstants.IntentStrings.AVATAR,user.getAvatar());
                intent.putExtra(UIKitConstants.IntentStrings.STATUS,user.getStatus());
                c.startActivity(intent);
            }
            @Override
            public void onError(CometChatException e) {
                Log.d(TAG, "User details fetching failed with exception: " + e.getMessage());
            }
        });

    }
    @Override
    public void onClick(List<PostResult> postResult, int position) {
//        resource=postResult;
        adapter.notifyItemChanged(position);
    }

    public void goBackFromProfile(View view) {
        onBackPressed();
    }
}