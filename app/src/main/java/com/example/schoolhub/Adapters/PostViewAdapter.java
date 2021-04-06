package com.example.schoolhub.Adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolhub.MainActivity;
import com.example.schoolhub.R;
import com.example.schoolhub.RetrofitInterface;
import com.example.schoolhub.SignIn;
import com.example.schoolhub.data.Comment;
import com.example.schoolhub.data.LoginResult;
import com.example.schoolhub.data.PostResult;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;

public class PostViewAdapter extends RecyclerView.Adapter<PostViewAdapter.ViewHolder> {
    List<PostResult> resourcePost;
    List<Comment> resourceComment;
    Context context;
    CommentAdapter commentAdapter;
    RecyclerView recyclerViewCmnt;
    FirebaseStorage storage= FirebaseStorage.getInstance();
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    public PostViewAdapter(List<PostResult> postLists, Context context) {
        this.resourcePost = postLists;
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.post_body,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewAdapter.ViewHolder holder, int position) {

        PostResult postResult=resourcePost.get(position);
        holder.userNamePost.setText(postResult.getUsername());
        holder.postTextData.setText(postResult.getText());
        holder.timePost.setText(postResult.getTime());
        //dp set
        Call<List<LoginResult>> call2 = retrofitInterface.userData(postResult.getUserID());
        call2.enqueue(new Callback<List<LoginResult>>() {
            @Override
            public void onResponse(Call<List<LoginResult>> call, Response<List<LoginResult>> response) {
                if (response.code() == 200) {

                    StorageReference storageRef2 = storage.getReferenceFromUrl(response.body().get(0).getProfilePic());
                    storageRef2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(context)
                                    .load(uri)
                                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)         //ALL or NONE as your requirement
                                    .thumbnail(Glide.with(context).load(R.drawable.ic_image_loading))
                                    .error(R.drawable.ic_image_error)
                                    .into(holder.userDpPost);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                            Glide.with(context)
                                    .load(R.drawable.ic_image_error)
                                    .fitCenter()
                                    .into(holder.userDpPost);
                        }
                    });

                }else {
                    Toast.makeText(context, "Some response code: "+ response.code(), Toast.LENGTH_LONG).show();
                }

            }
            @Override
            public void onFailure(Call<List<LoginResult>> call, Throwable t) {
                Toast.makeText(context, ""+t, Toast.LENGTH_LONG).show();
            }
        });

        //image in post set
        if(postResult.getImage()==null){
            //holder.imagePost.setLayoutParams(new LinearLayout.LayoutParams(0,0));
        }else{
            if(postResult.getImage().isEmpty()){

            }else{
                StorageReference storageRef = storage.getReferenceFromUrl(postResult.getImage());
                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(context)
                                .load(uri)
                                //.fitCenter()
                                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)         //ALL or NONE as your requirement
                                .thumbnail(Glide.with(context).load(R.drawable.ic_image_loading))
                                .error(R.drawable.a)
                                .into(holder.imagePost);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                        Toast.makeText(context, "ye nae load ho rhi", Toast.LENGTH_LONG).show();
                        Glide.with(context)
                                .load(R.drawable.ic_image_error)
                                .fitCenter()
                                .into(holder.imagePost);
                    }
                });
            }
        }
        if(postResult.getComments().isEmpty()){

        }else{
            this.resourceComment= postResult.getComments();

            commentAdapter = new CommentAdapter(resourceComment,context);
            commentAdapter.setHasStableIds(true);
            recyclerViewCmnt.setAdapter(commentAdapter);
        }
        holder.commentSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.commentSendText.getText().length()>0){
                    Log.d(TAG, "___________________a____________");
                    HashMap<String, String> maped = new HashMap<>();
                    maped.put("username", SignIn.userName);
                    maped.put("text", holder.commentSendText.getText().toString());
                    HashMap<String,HashMap<String, String>> map = new HashMap<>();
                    Log.d(TAG, "___________________b____________"+holder.commentSendText.getText().toString());
                    map.put("comments",maped);
                    Call<PostResult> call =retrofitInterface.putComment(postResult.getId(),map);

                    call.enqueue(new Callback<PostResult>() {
                        @Override
                        public void onResponse(Call<PostResult> call, Response<PostResult> response) {
                            if(!response.isSuccessful()){
                                Log.d(TAG, "onResponse comment retrofit: "+response.code());
                                Log.d(TAG, "___________________c____________");
                                return;
                            }

                            Toast.makeText(context, "Comment Posted.", Toast.LENGTH_LONG).show();
                            //commentAdapter.notifyDataSetChanged();
                        }
                        @Override
                        public void onFailure(Call<PostResult> call, Throwable t) {
                            Toast.makeText(context, " some error in comment patch request : "+t.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }else{
                    Toast.makeText(context, "Please write something in comment section.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return resourcePost.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView userNamePost,timePost,postTextData;
        public ImageView imagePost,commentSendButton;
        public EditText commentSendText;
        public CircleImageView userDpPost;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.userNamePost = itemView.findViewById(R.id.userNamePost);
            this.timePost = itemView.findViewById(R.id.timePost);
            this.postTextData = itemView.findViewById(R.id.postTextData);
            this.imagePost = itemView.findViewById(R.id.imagePost);
            this.commentSendText = itemView.findViewById(R.id.commentSendText);
            this.commentSendButton = itemView.findViewById(R.id.commentSendButton);
            this.userDpPost= itemView.findViewById(R.id.userDpPost);

            recyclerViewCmnt = (RecyclerView) itemView.findViewById(R.id.commentView);
            recyclerViewCmnt.setLayoutManager(new LinearLayoutManager(context));

            retrofit = new Retrofit.Builder()
                    .baseUrl(MainActivity.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            retrofitInterface = retrofit.create(RetrofitInterface.class);

        }
    }

}
