package com.example.schoolhub.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
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
import com.example.schoolhub.data.SchoolHubReview;
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

import static android.content.ContentValues.TAG;

public class SchoolHubReviewsAdapter extends RecyclerView.Adapter<SchoolHubReviewsAdapter.ViewHolder>{
    List<SchoolHubReview> schoolHubReviews;
    Context context;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    FirebaseStorage storage= FirebaseStorage.getInstance();

    public SchoolHubReviewsAdapter(List<SchoolHubReview> body, Context applicationContext) {
        this.schoolHubReviews=body;
        this.context=applicationContext;
    }

    @NonNull
    @Override
    public SchoolHubReviewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.schoolhub_review_layout,parent,false);
        SchoolHubReviewsAdapter.ViewHolder viewHolder = new SchoolHubReviewsAdapter.ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SchoolHubReviewsAdapter.ViewHolder holder, int position) {
        holder.userNameInReview.setText(schoolHubReviews.get(position).getUsername());
        holder.dateInReview.setText(schoolHubReviews.get(position).getDate());
        holder.textInReview.setText(schoolHubReviews.get(position).getReviewText());
        holder.ratingBarInReview.setRating(schoolHubReviews.get(position).getRating());
        if(schoolHubReviews.get(position).getReply().size()>0){
            holder.textReplyReview.setText(schoolHubReviews.get(position).getReply().get(0).getText());
        }else{
            holder.replyReviewLayout.setLayoutParams(new LinearLayout.LayoutParams(0,0));
        }
        //
        holder.dpReviewerApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callProfile(schoolHubReviews.get(position).getUserID());
            }
        });
        //dp load
        Call<List<LoginResult>> call2 = retrofitInterface.userData(schoolHubReviews.get(position).getUserID());
        call2.enqueue(new Callback<List<LoginResult>>() {
            @Override
            public void onResponse(Call<List<LoginResult>> call, Response<List<LoginResult>> response) {
                if (response.code() == 200) {
                    if(response.body().get(0).getProfilePic()==null){

                    }else{
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
                                                .into(holder.dpReviewerApp);
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
                                            .into(holder.dpReviewerApp);
                                }
                            });
                        }catch (Exception e){
                            Log.d(TAG, "Err loading reviewer pic: "+e);
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
        return schoolHubReviews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RatingBar ratingBarInReview;
        TextView userNameInReview,dateInReview,textInReview,textReplyReview;
        LinearLayout replyReviewLayout;
        CircleImageView dpReviewerApp;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.ratingBarInReview=itemView.findViewById(R.id.ratingBarInReview);
            this.userNameInReview=itemView.findViewById(R.id.userNameInReview);
            this.dateInReview=itemView.findViewById(R.id.dateInReview);
            this.textInReview=itemView.findViewById(R.id.textInReview);
            this.textReplyReview=itemView.findViewById(R.id.textReplyReview);
            this.replyReviewLayout=itemView.findViewById(R.id.replyReviewLayout);
            this.dpReviewerApp= itemView.findViewById(R.id.dpReviewerApp);

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
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(it);
    }
}
