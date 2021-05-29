package com.example.schoolhub.Adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.schoolhub.MainActivity;
import com.example.schoolhub.R;
import com.example.schoolhub.RetrofitInterface;
import com.example.schoolhub.data.Comment;
import com.example.schoolhub.data.LoginResult;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    List<Comment> resourceComment;
    Context context;
    FirebaseStorage storage= FirebaseStorage.getInstance();
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    public CommentAdapter(List<Comment> res, Context context) {
        Log.d(TAG, "CommentAdapter: "+res.size());
        this.resourceComment=res;
        this.context = context;
    }

    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_body,parent,false);
        return new CommentAdapter.ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position) {
        Comment comments=resourceComment.get(position);
        holder.userNameComment.setText(comments.getUsername());
        holder.textComment.setText(comments.getText());
        //set dp of person who commented
        Call<List<LoginResult>> call2 = retrofitInterface.userData(comments.getUserID());
        call2.enqueue(new Callback<List<LoginResult>>() {
            @Override
            public void onResponse(Call<List<LoginResult>> call, Response<List<LoginResult>> response) {
                if (response.code() == 200) {
                    if(response.body().get(0).getProfilePic()==null){

                    }else{
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
                                            .into(holder.userDpComment);
                                }catch (Exception e){
                                    Log.d(TAG, "comment photo not loaded: "+e);
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                                try{
                                    Glide.with(context)
                                            .load(R.drawable.ic_image_error)
                                            .fitCenter()
                                            .into(holder.userDpComment);
                                }catch (Exception e){
                                    Log.d(TAG, "comment photo not loaded: "+e);
                                }
                            }
                        });
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
    }

    @Override
    public int getItemCount() {
        return resourceComment.size();
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
        public TextView userNameComment,textComment;
        public CircleImageView userDpComment;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.userNameComment = itemView.findViewById(R.id.userNameComment);
            this.textComment = itemView.findViewById(R.id.textComment);
            this.userDpComment= itemView.findViewById(R.id.userDpComment);
            retrofit = new Retrofit.Builder()
                    .baseUrl(MainActivity.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            retrofitInterface = retrofit.create(RetrofitInterface.class);
        }
    }
}
