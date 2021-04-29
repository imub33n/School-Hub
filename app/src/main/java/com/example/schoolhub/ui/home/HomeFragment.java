 package com.example.schoolhub.ui.home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cometchat.pro.core.AppSettings;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.User;
import com.example.schoolhub.Adapters.PostViewAdapter;
import com.example.schoolhub.MainActivity;
import com.example.schoolhub.R;
import com.example.schoolhub.RetrofitInterface;
import com.example.schoolhub.SignIn;
import com.example.schoolhub.data.OnCommentClick;
import com.example.schoolhub.data.PostResult;
import com.example.schoolhub.data.PreferenceData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class HomeFragment extends Fragment implements OnCommentClick {
//recyclerView.getAdapter().notifyDataSetChanged();
    public String uploadedImageURL;
    public static String nameHomePhoto="";
    String userIDPost,userNamePost, textPost, timePost, imagePost;
    //private HomeViewModel homeViewModel;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    EditText postText;
    ImageView attachedImageId,cancelImage;
    TextView postButton,uploadImageButton,imageNameHome;
    RelativeLayout photoHomeLayout;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    //Firebase
    FirebaseStorage storage;
    StorageReference storageReference;
    List<PostResult> resource;
    PostViewAdapter adapter;
    ProgressBar progressBar;
    OnCommentClick c=this;
    RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState ) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

//refrences
        postText= root.findViewById(R.id.postEditText);
        postButton= root.findViewById(R.id.postButton);
        uploadImageButton= root.findViewById(R.id.uploadImageButton);
        imageNameHome=root.findViewById(R.id.imageNameHome);
        attachedImageId=root.findViewById(R.id.attachedImageId);
        photoHomeLayout=root.findViewById(R.id.photoHomeLayout);
        cancelImage= root.findViewById(R.id.cancelPhoto);
        progressBar = (ProgressBar) root.findViewById(R.id.progressBar);
        //create chat user
//        String authKey = MainActivity.authKey; // Replace with your App Auth Key
//        User user = new User();
//        user.setUid(PreferenceData.getLoggedInUserData(getContext()).get("userID")); // Replace with the UID for the user to be created
//        user.setName(PreferenceData.getLoggedInUserData(getContext()).get("username")); // Replace with the name of the user
//
//        CometChat.createUser(user, authKey, new CometChat.CallbackListener<User>() {
//            @Override
//            public void onSuccess(User user) {
//                Log.d("createUser", user.toString());
//            }
//
//            @Override
//            public void onError(CometChatException e) {
//                Log.e("createUser", e.getMessage());
//            }
//        });

        //chat
        if (CometChat.getLoggedInUser() == null) {
            CometChat.login(PreferenceData.getLoggedInUserData(getContext()).get("userID"), MainActivity.authKey, new CometChat.CallbackListener<User>() {
                @Override
                public void onSuccess(User user) {
                    Log.d(TAG, "Login Successful : " + user.toString());
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d(TAG, "Login failed with exception: " + e.getMessage());
                }
            });
        } else {
            // User already logged in
            Log.d(TAG, "Already loggedIn");
        }
        //endChat

        cancelImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageNameHome.setText("");
                nameHomePhoto="";
                filePath=null;
                photoHomeLayout.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
            }
        });
//backend
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
//recycler
        recyclerView = (RecyclerView) root.findViewById(R.id.postView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        //retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);
        //get shitpostings
        Call<List<PostResult>> call = retrofitInterface.doGetListResources();
        call.enqueue(new Callback<List<PostResult>>() {
            @Override
            public void onResponse(Call<List<PostResult>> call, Response<List<PostResult>> response) {
                if (response.code() == 200) {
                    Log.d("TAG",response.code()+"");
                    progressBar.setVisibility(View.INVISIBLE);
                    resource =  response.body();
                    adapter = new PostViewAdapter(resource,getContext(),c);
                    adapter.setHasStableIds(true);
                    recyclerView.setAdapter(adapter);
                }else {
                    Toast.makeText(getContext(), "some response code", Toast.LENGTH_LONG).show();
                }

            }
            @Override
            public void onFailure(Call<List<PostResult>> call, Throwable t) {

                Toast.makeText(getContext(), ""+t, Toast.LENGTH_LONG).show();
            }
        });
        //gettingPosts();

        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filePath != null) {
                    uploadImage();
                }else {
                    postUpload();
                }
            }
        });
        final TextView textView = root.findViewById(R.id.text_home);

        return root;
    }

    private void updatePosts(){
        Call<List<PostResult>> call = retrofitInterface.doGetListResources();
        call.enqueue(new Callback<List<PostResult>>() {
            @Override
            public void onResponse(Call<List<PostResult>> call, Response<List<PostResult>> response) {
                if (response.code() == 200) {
                    Log.d("TAG",response.code()+"");
                    resource =  response.body();
                    adapter = new PostViewAdapter(resource,getContext(),c);
                    adapter.setHasStableIds(true);
                    recyclerView.setAdapter(adapter);
//                    adapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(getContext(), "some response code", Toast.LENGTH_LONG).show();
                }

            }
            @Override
            public void onFailure(Call<List<PostResult>> call, Throwable t) {

                Toast.makeText(getContext(), ""+t, Toast.LENGTH_LONG).show();
            }
        });
    }
    private void chooseImage() {
        Uri uri = Uri.parse(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());
        Intent intentImages = new Intent();
        intentImages.setType("image/*");
        intentImages.putExtra(Intent.EXTRA_STREAM,uri);
        intentImages.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intentImages, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    private void postUpload(){
        LocalDateTime now = LocalDateTime.now();
        String currentTime=dtf.format(now);
        HashMap<String, String> map = new HashMap<>();
        map.put("userID", PreferenceData.getLoggedInUserData(getContext()).get("userID"));
        map.put("username", PreferenceData.getLoggedInUserData(getContext()).get("username"));
        map.put("text", postText.getText().toString());
        map.put("time", currentTime);
        map.put("image",uploadedImageURL);

        Call<Void> call = retrofitInterface.executePost(map);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if (response.code() == 200) {
                    postText.setText("");
                    Toast.makeText(getContext(), "Post Successful", Toast.LENGTH_LONG).show();
                    photoHomeLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
                    updatePosts();
                } else {
                    Toast.makeText(getContext(), "Server response code: "+response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Shit: "+t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            if (data.getData() != null) {
                filePath = data.getData();
                Cursor returnCursor = getContext().getContentResolver().query(filePath, null, null, null, null);
                int nameHomeIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                returnCursor.moveToFirst();
                nameHomePhoto=returnCursor.getString(nameHomeIndex);
                imageNameHome.setText(nameHomePhoto);
                photoHomeLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                String photoUri= String.valueOf(filePath);
                if (photoUri.isEmpty()||photoUri.equals(null)||photoUri.equals("")) {

                } else {
                    Glide.with(this)
                            .load(filePath)
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)         //ALL or NONE as your requirement
                            .thumbnail(Glide.with(this).load(R.drawable.ic_image_loading))
                            .error(R.drawable.ic_image_error)
                            .into(attachedImageId);
                }
            }
        }
    }

    private void uploadImage() {
        if(filePath != null)
        {
            ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                        {
                            @Override
                            public void onSuccess(Uri yoru)
                            {
                                uploadedImageURL=yoru.toString();
                                postUpload();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onClick(List<PostResult> postResult,int position) {
        //resource=postResult;
        adapter.notifyItemChanged(position);
    }
}
