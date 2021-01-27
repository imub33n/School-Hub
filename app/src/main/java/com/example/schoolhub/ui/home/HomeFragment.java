package com.example.schoolhub.ui.home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.schoolhub.HomePanel;
import com.example.schoolhub.PostViewAdapter;
import com.example.schoolhub.R;
import com.example.schoolhub.RetrofitInterface;
import com.example.schoolhub.SignIn;
import com.example.schoolhub.SignUp;
import com.example.schoolhub.data.LoginResult;
import com.example.schoolhub.data.PostResult;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
import static java.lang.Thread.sleep;

public class HomeFragment extends Fragment {
//recyclerView.getAdapter().notifyDataSetChanged();
    public String uploadedImageURL;
    String userIDPost,userNamePost, textPost, timePost, imagePost;
    private HomeViewModel homeViewModel;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    EditText postText;
    TextView postButton,uploadImageButton;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    //Firebase
    FirebaseStorage storage;
    StorageReference storageReference;
    //DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("images");
    //recyclerview
    List<PostResult> resource;
    PostViewAdapter adapter;
    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState ) {

        homeViewModel =ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
//refrences
        postText= root.findViewById(R.id.postEditText);
        postButton= root.findViewById(R.id.postButton);
        uploadImageButton= root.findViewById(R.id.uploadImageButton);
//backend
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
//recycler
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.postView);
        //PostViewAdapter adapter = new PostViewAdapter(resource);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //recyclerView.setAdapter(adapter);

//get shitpostings
        //retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(SignIn.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        Call<List<PostResult>> call = retrofitInterface.doGetListResources();
        call.enqueue(new Callback<List<PostResult>>() {
            @Override
            public void onResponse(Call<List<PostResult>> call, Response<List<PostResult>> response) {
                if (response.code() == 200) {
                    Log.d("TAG",response.code()+"");
                    String displayResponse = "";
                    resource =  response.body();
                    //Toast.makeText(getContext(),resource.toString(),Toast.LENGTH_SHORT).show();
                    adapter = new PostViewAdapter(resource,getContext());
                    recyclerView.setAdapter(adapter);

//                    for(int i=0;i<resource.size();i++){
//                        System.out.println(resource.get(i).getTime());
//                    }
//                    Toast.makeText(getContext(), displayResponse, Toast.LENGTH_LONG).show();
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
        //title home
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        return root;
    }

    private void gettingPosts() {


    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }
    private void postUpload(){
        LocalDateTime now = LocalDateTime.now();
        String currentTime=dtf.format(now);
        HashMap<String, String> map = new HashMap<>();
        map.put("userID", SignIn.userID);
        map.put("username", SignIn.userName);
        map.put("text", postText.getText().toString());
        map.put("time", currentTime);
        map.put("image",uploadedImageURL);

        Call<Void> call = retrofitInterface.executePost(map);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if (response.code() == 200) {
                    Toast.makeText(getContext(), "Post Successful", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "Post else", Toast.LENGTH_LONG).show();
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
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), filePath);
                Toast.makeText(getContext(), "Photo Selected Click Post to Upload", Toast.LENGTH_SHORT).show();
            }
            catch (IOException e)
            {
                Toast.makeText(getContext(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
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
                                //Toast.makeText(getContext(), yoru.toString(), Toast.LENGTH_SHORT).show();
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
}