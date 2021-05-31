package com.example.schoolhub.Adapters;

import android.content.Context;
import android.content.Intent;
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
import com.example.schoolhub.UserProfile;
import com.example.schoolhub.data.LoginResult;
import com.example.schoolhub.data.ReplyReview;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.cometchat.pro.uikit.ui_components.shared.cometchatReaction.fragment.FragmentReactionObject.TAG;

public class ReplyReviewAdapter extends RecyclerView.Adapter<ReplyReviewAdapter.ViewHolder> {
    List<ReplyReview> replyReviews;
    Context context;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    FirebaseStorage storage= FirebaseStorage.getInstance();

    public ReplyReviewAdapter(List<ReplyReview> reply, Context context) {
        this.replyReviews=reply;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.review_reply_body,parent,false);
        return new ReplyReviewAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textReplyReview.setText(replyReviews.get(position).getText());
        holder.userNameReplyReview.setText(replyReviews.get(position).getUsername());
        //
        holder.userDpReplyReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callProfile(replyReviews.get(position).getUserID());
            }
        });
        //set dp of person who replied
        Call<List<LoginResult>> call2 = retrofitInterface.userData(replyReviews.get(position).getUserID());
        call2.enqueue(new Callback<List<LoginResult>>() {
            @Override
            public void onResponse(Call<List<LoginResult>> call, Response<List<LoginResult>> response) {
                if (response.code() == 200) {
                    if(response.body().get(0).getProfilePic()==null){

                    }else{
                        try{
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
                                                    .into(holder.userDpReplyReview);
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
                                                .into(holder.userDpReplyReview);
                                    }
                                });
                            }catch(Exception e){
                                Log.d(TAG, "Photo loading failed : "+ e);
                            }
                        }catch (Exception e){
                            Log.d(TAG, "Err loading review replier pic: "+e);
                        }
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
        return replyReviews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView userNameReplyReview,textReplyReview;
        CircleImageView userDpReplyReview;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textReplyReview=itemView.findViewById(R.id.textReplyReview);
            userNameReplyReview=itemView.findViewById(R.id.userNameReplyReview);
            userDpReplyReview=itemView.findViewById(R.id.userDpReplyReview);
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
