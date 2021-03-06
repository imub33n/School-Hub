package com.example.schoolhub.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolhub.AdminDashMainPage;
import com.example.schoolhub.EditAccountSettings;
import com.example.schoolhub.MainActivity;
import com.example.schoolhub.R;
import com.example.schoolhub.RetrofitInterface;
import com.example.schoolhub.SendNotification;
import com.example.schoolhub.UserProfile;
import com.example.schoolhub.data.Comment;
import com.example.schoolhub.data.Likes;
import com.example.schoolhub.data.LoginResult;
import com.example.schoolhub.data.OnCommentClick;
import com.example.schoolhub.data.PostResult;
import com.example.schoolhub.data.PreferenceData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.cometchat.pro.uikit.ui_components.shared.cometchatReaction.fragment.FragmentReactionObject.TAG;

public class PostViewAdapter extends RecyclerView.Adapter<PostViewAdapter.ViewHolder> {
    List<PostResult> resourcePost;
    List<Comment> resourceComment;
    List<Likes> resourceLike;
    Likes like;
    Context context;
    CommentAdapter commentAdapter;
    RecyclerView recyclerViewCmnt;
    FirebaseStorage storage= FirebaseStorage.getInstance();
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private OnCommentClick cCallback;
    List<PostResult> resourcer= new ArrayList<>();

    public PostViewAdapter(List<PostResult> postLists, Context context,OnCommentClick listener) {
        Collections.reverse(postLists);
        this.resourcePost = postLists;
        this.context = context;
        this.cCallback = listener;
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

        if(Objects.equals(context.getClass().getSimpleName(),"UserProfile")){
            if(postResult.getUserID().equals(PreferenceData.getLoggedInUserData(context).get("userID"))){
                holder.delete_icon.setVisibility(View.VISIBLE);
            }
        }
        holder.delete_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
                builder2.setMessage("Delete this post permanently?");
                builder2.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // delete ...
                        Call<Void> call2er = retrofitInterface.deletePost(postResult.getId());
                        call2er.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(context,"Post Deleted",Toast.LENGTH_LONG).show();
                                    resourcePost.remove(position);
                                    notifyDataSetChanged();
                                }else {
                                    Toast.makeText(context, "CODE: "+response.code(), Toast.LENGTH_LONG).show();
                                }
                            }
                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(context, "Connection Err: "+t, Toast.LENGTH_LONG).show();
                            }
                        });
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
        });
        holder.userNamePost.setText(postResult.getUsername());
        holder.postTextData.setText(postResult.getText());
        holder.timePost.setText(postResult.getTime());
        holder.totalLikesTextPost.setText("("+postResult.getTotalLikes()+" Likes) ");
        this.resourceLike=postResult.getLikes();
        if(resourceLike!=null){
            for(int i=0;i<postResult.getLikes().size();i++){
                if(Objects.equals(resourceLike.get(i).getUserID(), PreferenceData.getLoggedInUserData(context).get("userID")) && resourceLike.get(i).getLike()){
                    holder.likeTextPost.setText("Unlike");
                    holder.likeButtonPost.setImageDrawable(context.getResources().getDrawable(R.drawable.liketrue));
                }
            }
        }

        //set dp of person who posted
        Call<List<LoginResult>> call2 = retrofitInterface.userData(postResult.getUserID());
        call2.enqueue(new Callback<List<LoginResult>>() {
            @Override
            public void onResponse(Call<List<LoginResult>> call, Response<List<LoginResult>> response) {
                if (response.code() == 200) {
                    try{
                        StorageReference storageRef2 = storage.getReferenceFromUrl(response.body().get(0).getProfilePic());
                        storageRef2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                try{
                                    Glide.with(context)
                                            .load(uri)
                                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)         //ALL or NONE as your requirement
                                            .thumbnail(Glide.with(context).load(R.drawable.ic_img_loading))
                                            .error(R.drawable.ic_image_error)
                                            .into(holder.userDpPost);
                                }catch(Exception e){
                                    Log.d(TAG, "Photo loading failed : "+ e);
                                }
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
                    }catch(Exception e){
                        Log.d(TAG, "Photo loading failed : "+ e);
                    }

                }else {
                    Toast.makeText(context, "Some response code: "+ response.code(), Toast.LENGTH_LONG).show();
                }

            }
            @Override
            public void onFailure(Call<List<LoginResult>> call, Throwable t) {
                Toast.makeText(context, ""+t, Toast.LENGTH_LONG).show();
            }
        });
        //
        holder.userDpPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callProfile(postResult.getUserID());
            }
        });
        //set image in post
        if(postResult.getImage()==null){
            //holder.imagePost.setLayoutParams(new LinearLayout.LayoutParams(0,0));
        }else{
            if(postResult.getImage().isEmpty()){

            }else{
                try{
                    StorageReference storageRef = storage.getReferenceFromUrl(postResult.getImage());
                    storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            try{
                                Glide.with(context)
                                        .load(uri)
                                        //.fitCenter()
                                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)         //ALL or NONE as your requirement
                                        .thumbnail(Glide.with(context).load(R.drawable.ic_img_loading))
                                        .error(R.drawable.a)
                                        .into(holder.imagePost);
                            }catch(Exception e){
                                Log.d(TAG, "Photo loading failed : "+ e);
                            }
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
                }catch(Exception e){
                    Log.d(TAG, "Photo loading failed : "+ e);
                }
            }
        }
        if(postResult.getComments().isEmpty()){
        }else{
            this.resourceComment= postResult.getComments();
            commentAdapter = new CommentAdapter(resourceComment,context);
            commentAdapter.setHasStableIds(true);
            recyclerViewCmnt.setAdapter(commentAdapter);
        }

        holder.likeButtonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                like=new Likes(PreferenceData.getLoggedInUserData(context).get("userID"),true);
                like.setUserID(PreferenceData.getLoggedInUserData(context).get("userID"));
                like.setUsername(PreferenceData.getLoggedInUserData(context).get("username"));
                if(Objects.equals(holder.likeTextPost.getText().toString(),"Like")){
                    like.setLike(true);
                } else if(Objects.equals(holder.likeTextPost.getText().toString(),"Unlike")){
                    like.setLike(false);
                }
                HashMap<String,Likes> maping = new HashMap<>();
                maping.put("likes",like);

                Call<PostResult> call =retrofitInterface.putLike(postResult.getId(),maping);
                call.enqueue(new Callback<PostResult>() {
                    @Override
                    public void onResponse(Call<PostResult> call, Response<PostResult> response) {
                        if(response.isSuccessful()){
                            if(Objects.equals(holder.likeTextPost.getText().toString(),"Like")){
                                //notiStart
                                Log.d(TAG, "noti sending here ");
                                String title="Like";
                                String subTitle=PreferenceData.getLoggedInUserData(context).get("username")+" liked your post";
                                new SendNotification(title,subTitle, PreferenceData.getLoggedInUserData(context).get("userID"),postResult.getUserID());
                                //notiEnd
                                holder.totalLikesTextPost.setText("("+(postResult.getTotalLikes()+1)+" Likes) ");
                                holder.likeTextPost.setText("Unlike");
                                holder.likeButtonPost.setImageDrawable(context.getResources().getDrawable(R.drawable.liketrue));
                            } else if(Objects.equals(holder.likeTextPost.getText().toString(),"Unlike")){
                                holder.totalLikesTextPost.setText("("+(postResult.getTotalLikes()-1)+" Likes) ");
                                holder.likeTextPost.setText("Like");
                                holder.likeButtonPost.setImageDrawable(context.getResources().getDrawable(R.drawable.likefalse));
                            }
                        }else{
                            Toast.makeText(context, "Err Code: "+response.code(), Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<PostResult> call, Throwable t) {
                        Toast.makeText(context, " Some error in like patch request : "+t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        holder.commentSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.commentSendText.getText().length()>0){
                    HashMap<String, String> maped = new HashMap<>();
                    maped.put("userID", PreferenceData.getLoggedInUserData(context).get("userID"));
                    maped.put("username", PreferenceData.getLoggedInUserData(context).get("username"));
                    maped.put("text", holder.commentSendText.getText().toString());
                    
                    HashMap<String,HashMap<String, String>> map = new HashMap<>();
                    map.put("comments",maped);
                    Call<PostResult> call =retrofitInterface.putComment(postResult.getId(),map);

                    call.enqueue(new Callback<PostResult>() {
                        @Override
                        public void onResponse(Call<PostResult> call, Response<PostResult> response) {
                            if(response.isSuccessful()){
                                //notiStart
                                String title="Comment";
                                String subTitle=PreferenceData.getLoggedInUserData(context).get("username")+" commented on your post";
                                new SendNotification(title,subTitle, PreferenceData.getLoggedInUserData(context).get("userID"),postResult.getUserID());
                                //notiEnd
                                Toast.makeText(context, "Comment Posted.", Toast.LENGTH_LONG).show();
                                holder.commentSendText.setText("");
                                //get shitPostings cmnt
                                Call<List<PostResult>> caller = retrofitInterface.doGetListResources();
                                caller.enqueue(new Callback<List<PostResult>>() {
                                    @Override
                                    public void onResponse(Call<List<PostResult>> call, Response<List<PostResult>> response) {
                                        if (response.code() == 200) {
                                            Collections.reverse(response.body());
                                            if(Objects.equals(context.getClass().getSimpleName(),"HomePanel")){
                                                resourcePost=response.body();
                                                cCallback.onClick(response.body(),position);
                                            }else if(Objects.equals(context.getClass().getSimpleName(),"UserProfile")){
                                                for(int i=0;i<response.body().size();i++){
                                                    if(Objects.equals(response.body().get(i).getUserID(),PreferenceData.getLoggedInUserData(context).get("userID"))){
                                                        resourcer.add(response.body().get(i));
                                                    }
                                                    if(i==response.body().size()-1){
                                                        resourcePost=resourcer;
                                                        cCallback.onClick(resourcer,position);
                                                    }
                                                }
                                            }
                                        }else {
                                            Toast.makeText(context, "Some response code: "+ response.code(), Toast.LENGTH_LONG).show();
                                        }

                                    }
                                    @Override
                                    public void onFailure(Call<List<PostResult>> call, Throwable t) {

                                        Toast.makeText(context, ""+t, Toast.LENGTH_LONG).show();
                                    }
                                });
                                //
                                return;
                            }else{
                                Toast.makeText(context, "Err: "+response.code(), Toast.LENGTH_LONG).show();
                            }


                        }
                        @Override
                        public void onFailure(Call<PostResult> call, Throwable t) {
                            Toast.makeText(context, " Some error in comment patch request : "+t.getMessage(),
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

        public TextView userNamePost,timePost,postTextData,likeTextPost,totalLikesTextPost;
        public ImageView imagePost,commentSendButton,likeButtonPost,delete_icon;
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
            this.likeButtonPost= itemView.findViewById(R.id.likeButtonPost);
            this.likeTextPost= itemView.findViewById(R.id.likeTextPost);
            this.totalLikesTextPost= itemView.findViewById(R.id.totalLikesTextPost);
            this.delete_icon= itemView.findViewById(R.id.delete_icon);

            recyclerViewCmnt = (RecyclerView) itemView.findViewById(R.id.commentView);
            recyclerViewCmnt.setLayoutManager(new LinearLayoutManager(context));

            retrofit = new Retrofit.Builder()
                    .baseUrl(MainActivity.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            retrofitInterface = retrofit.create(RetrofitInterface.class);

        }
    }
    private void callProfile(String id){
        Intent it = new Intent( context , UserProfile.class);
        it.putExtra("EXTRA_USER_ID", id);
        context.startActivity(it);
    }

}
