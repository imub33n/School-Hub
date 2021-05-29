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
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.schoolhub.InformationSchoolFragment;
import com.example.schoolhub.MainActivity;
import com.example.schoolhub.R;
import com.example.schoolhub.RetrofitInterface;
import com.example.schoolhub.SendNotification;
import com.example.schoolhub.data.LiveStreamRequests;
import com.example.schoolhub.data.LoginResult;
import com.example.schoolhub.data.PostResult;
import com.example.schoolhub.data.PreferenceData;
import com.example.schoolhub.data.ReplyReview;
import com.example.schoolhub.data.SchoolHubReview;
import com.example.schoolhub.data.SchoolReviews;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
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

public class SchoolReviewsAdapter extends RecyclerView.Adapter<SchoolReviewsAdapter.ViewHolder>{
    List<SchoolReviews> schoolReviews;
    Context context;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    RecyclerView recyclerForReviewReply;
    ReplyReviewAdapter replyReviewAdapter;
    List<ReplyReview> replyReviews;
    FirebaseStorage storage= FirebaseStorage.getInstance();

    ReplyReview rep=new ReplyReview();

    public SchoolReviewsAdapter(List<SchoolReviews> body, Context context) {
        this.schoolReviews=body;
        this.context=context;
    }


    @NonNull
    @Override
    public SchoolReviewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.review_body,parent,false);
        SchoolReviewsAdapter.ViewHolder viewHolder = new SchoolReviewsAdapter.ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SchoolReviewsAdapter.ViewHolder holder, int position) {

        holder.userNameInReview.setText(schoolReviews.get(position).getUsername());
        holder.dateInReview.setText(schoolReviews.get(position).getDate());
        holder.textInReview.setText(schoolReviews.get(position).getReviewText());
        holder.ratingBarInReview.setRating(schoolReviews.get(position).getRating());
        //set pic of Reviewer
        //set dp of person who commented
        Call<List<LoginResult>> call2 = retrofitInterface.userData(schoolReviews.get(position).getUserID());
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
                                    Glide.with(context)
                                            .load(uri)
                                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)         //ALL or NONE as your requirement
                                            .thumbnail(Glide.with(context).load(R.drawable.ic_img_loading))
                                            .error(R.drawable.ic_image_error)
                                            .into(holder.picReviewer);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                    Glide.with(context)
                                            .load(R.drawable.ic_image_error)
                                            .fitCenter()
                                            .into(holder.picReviewer);
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

        //set reply
        if(schoolReviews.get(position).getReply().size()!=0){
            replyReviewAdapter = new ReplyReviewAdapter(schoolReviews.get(position).getReply(),context);
            recyclerForReviewReply.setAdapter(replyReviewAdapter);
        }

        holder.replyReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.giveReviewReply.getText().toString().isEmpty()){
                    Toast.makeText(context, "Reply field empty!", Toast.LENGTH_LONG).show();
                }else{
                    rep.setUsername(PreferenceData.getLoggedInUserData(context).get("username"));
                    rep.setText(holder.giveReviewReply.getText().toString());
                    rep.setUserID(PreferenceData.getLoggedInUserData(context).get("userID"));

                    Call<Void> call =retrofitInterface.replyReview(schoolReviews.get(position).getId(),rep);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if(response.isSuccessful()){
                                //notiStart
                                String title="Reply on School Review";
                                String subTitle = InformationSchoolFragment.thisSchoolData.getSchoolName()+ " has new reply on your review";
                                new SendNotification(title,subTitle, PreferenceData.getLoggedInUserData(context).get("userID"), schoolReviews.get(position).getUserID());
                                //notiEnd
                                Toast.makeText(context, "Reply Sent", Toast.LENGTH_LONG).show();
                                holder.giveReviewReply.setText("");
                            }else{
                                Toast.makeText(context, "Err Code: "+response.code(), Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(context, " Some error in reply patch request : "+t.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return schoolReviews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RatingBar ratingBarInReview;
        TextView userNameInReview,dateInReview,textInReview;
        EditText giveReviewReply;
        ImageView replyReviewButton;
        LinearLayout reviewReply;
        CircleImageView picReviewer;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.ratingBarInReview=itemView.findViewById(R.id.ratingBarInReview);
            this.userNameInReview=itemView.findViewById(R.id.userNameInReview);
            this.dateInReview=itemView.findViewById(R.id.dateInReview);
            this.textInReview=itemView.findViewById(R.id.textInReview);
            this.reviewReply=itemView.findViewById(R.id.reviewReply);
            giveReviewReply=itemView.findViewById(R.id.giveReviewReply);
            replyReviewButton=itemView.findViewById(R.id.replyReview);
            picReviewer=itemView.findViewById(R.id.picReviewer);
            if(PreferenceData.getLoggedInUserData(context).get("userType").isEmpty()){
                reviewReply.setLayoutParams(new LinearLayout.LayoutParams(0,0));
            }

            recyclerForReviewReply=itemView.findViewById(R.id.recyclerForReviewReply);
            recyclerForReviewReply.setLayoutManager(new LinearLayoutManager(context));

            retrofit = new Retrofit.Builder()
                    .baseUrl(MainActivity.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            retrofitInterface = retrofit.create(RetrofitInterface.class);
        }
    }
}
