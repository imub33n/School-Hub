package com.example.schoolhub.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.schoolhub.R;
import com.example.schoolhub.UserProfile;
import com.example.schoolhub.data.LoginResult;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.cometchat.pro.uikit.ui_components.shared.cometchatReaction.fragment.FragmentReactionObject.TAG;

public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.ViewHolder>{
    List<LoginResult> loginResults;
    Context context;
    FirebaseStorage storage= FirebaseStorage.getInstance();

    public SearchUserAdapter(List<LoginResult> loginResults, Context applicationContext) {
        this.loginResults=loginResults;
        this.context=applicationContext;
    }

    @NonNull
    @Override
    public SearchUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.frag_user_search_result,parent,false);
        SearchUserAdapter.ViewHolder vH = new SearchUserAdapter.ViewHolder(v);
        return vH;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchUserAdapter.ViewHolder holder, int position) {
        holder.userName.setText(loginResults.get(position).getUsername());
        holder.userType.setText(loginResults.get(position).getType());
        if(loginResults.get(position).getProfilePic()!=null){
            if(!loginResults.get(position).getProfilePic().isEmpty()){
                StorageReference storageRef2 = storage.getReferenceFromUrl(loginResults.get(position).getProfilePic());
                storageRef2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        try {
                            Glide.with(context)
                                    .load(uri)
                                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)         //ALL or NONE as your requirement
                                    .thumbnail(Glide.with(context).load(R.drawable.ic_img_loading))
                                    .error(R.drawable.ic_image_error)
                                    .into(holder.pic_user);
                            Log.d(TAG, "onSuccess: ____________________ex");
                        }catch (Exception e){
                            Log.d(TAG, "onFail: ____________________ex"+e);
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                        Glide.with(context)
                                .load(R.drawable.ic_image_error)
                                .fitCenter()
                                .into(holder.pic_user);
                    }
                });
            }
        }

        holder.search_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vee) {
                Intent it = new Intent( context, UserProfile.class);
                it.putExtra("EXTRA_USER_ID",loginResults.get(position).getUserID() );
                it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(it);
            }
        });

    }

    @Override
    public int getItemCount() {
        return loginResults.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView userType,userName;
        public LinearLayout search_item;
        CircleImageView pic_user;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.userName=itemView.findViewById(R.id.userName);
            this.userType=itemView.findViewById(R.id.userType);
            this.search_item=itemView.findViewById(R.id.search_item);
        }
    }
}
