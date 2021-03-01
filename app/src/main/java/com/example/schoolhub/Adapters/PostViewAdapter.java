package com.example.schoolhub.Adapters;

import android.content.Context;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolhub.MainActivity;
import com.example.schoolhub.R;
import com.example.schoolhub.RetrofitInterface;
import com.example.schoolhub.SignIn;
import com.example.schoolhub.data.Comment;
import com.example.schoolhub.data.PostResult;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.List;

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
        //ImageList imageList=imageLists.get(position);
        //holder.tvname.setText(imageList.getName());
        PostResult postResult=resourcePost.get(position);
        holder.userNamePost.setText(postResult.getUsername());
        holder.postTextData.setText(postResult.getText());
        holder.timePost.setText(postResult.getTime());
        if(postResult.getImage()!=null){
            if(!(postResult.getImage().isEmpty())){
                StorageReference storageRef = storage.getReferenceFromUrl(postResult.getImage());
                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(context)
                                .load(uri)
                                //.fitCenter()
                                //.dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)         //ALL or NONE as your requirement
                                .thumbnail(Glide.with(context).load(R.drawable.ic_image_loading))
                                .error(R.drawable.a)
                                //.apply(new RequestOptions().override(1000, 500))
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
        }else{
        }
        if(!(postResult.getComments().isEmpty())){
            this.resourceComment= postResult.getComments();
            commentAdapter = new CommentAdapter(resourceComment,context);
            recyclerViewCmnt.setAdapter(commentAdapter);
        }else{
        }
        holder.commentSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, postResult.getId(), Toast.LENGTH_LONG).show();
                //Toast.makeText(context, "postResult.getId()", Toast.LENGTH_LONG).show();
                HashMap<String, String> maped = new HashMap<>();
                maped.put("username", SignIn.userName);
                maped.put("text", holder.commentSendText.getText().toString());
                HashMap<String,HashMap<String, String>> map = new HashMap<>();

//                comment.add(new Comment(SignIn.userName,holder.commentSendText.getText().toString()));

                map.put("comments",maped);
//                map.put("username", SignIn.userName);
//                map.put("text", holder.commentSendText.getText().toString());

                //Comment comment= new Comment(SignIn.userName,"supumf");
                Call<PostResult> call =retrofitInterface.putComment(postResult.getId(),map);

                call.enqueue(new Callback<PostResult>() {
                    @Override
                    public void onResponse(Call<PostResult> call, Response<PostResult> response) {
                        if(!response.isSuccessful()){
                            Log.d(TAG, "onResponse comment retrofit: "+response.code());
                            return;
                        }
                    }
                    @Override
                    public void onFailure(Call<PostResult> call, Throwable t) {
                        Toast.makeText(context, " some error in comment patch request : "+t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

//        GlideApp.with(context)
//                .load("http://via.placeholder.com/300.png")
//                .override(300, 200)
//                .into(ivImg);
//        GlideApp.with(holder.itemView.getContext())
//                .load(pi.getImage_url())
//                .into(holder.picture);
//        final PostResult myListData = listdata[position];
//        holder.userNamePost.setText(listdata[position].getUsername());
//        holder.postTextData.setText(listdata[position].getText());
//        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(view.getContext(),"click on item: ",Toast.LENGTH_LONG).show();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return resourcePost.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView userNamePost,timePost,postTextData;
        public ImageView imagePost,commentSendButton;
        public EditText commentSendText;
        //public LinearLayout linearLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.userNamePost = itemView.findViewById(R.id.userNamePost);
            this.timePost = itemView.findViewById(R.id.timePost);
            this.postTextData = itemView.findViewById(R.id.postTextData);
            this.imagePost = itemView.findViewById(R.id.imagePost);
            this.commentSendText = itemView.findViewById(R.id.commentSendText);
            this.commentSendButton = itemView.findViewById(R.id.commentSendButton);

            recyclerViewCmnt = (RecyclerView) itemView.findViewById(R.id.commentView);
            recyclerViewCmnt.setHasFixedSize(true);
            recyclerViewCmnt.setLayoutManager(new LinearLayoutManager(context));

            retrofit = new Retrofit.Builder()
                    .baseUrl(MainActivity.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            retrofitInterface = retrofit.create(RetrofitInterface.class);
            //linearLayout = itemView.findViewById(R.id.linearLayout);
        }
    }

}
