package com.example.schoolhub.Adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.schoolhub.R;
import com.example.schoolhub.data.SchoolsLandingModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class AdapterLanding extends RecyclerView.Adapter<AdapterLanding.ViewHolder>{
    private ArrayList<SchoolsLandingModel> models= new ArrayList<>();
    DecimalFormat adf= new DecimalFormat("0.0");
    FirebaseStorage storage= FirebaseStorage.getInstance();
    //private LayoutInflater layoutInflater;
    private Context context;
    public AdapterLanding(ArrayList<SchoolsLandingModel> models, Context context) {
        this.models = models;
        this.context = context;

    }
    @NonNull
    @Override
    public AdapterLanding.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_layout,parent,false);
        AdapterLanding.ViewHolder viewHolder = new AdapterLanding.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterLanding.ViewHolder holder, int position) {
//        holder.imageView.setImageResource(models.get(position).getImage());
        if(models.get(position).getSchools().getImages().size()>0){
            if(models.get(position).getSchools().getImages().get(0).getPath()!=null){
                StorageReference storageRef = storage.getReferenceFromUrl(models.get(position).getSchools().getImages().get(0).getPath());
                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        try{
                            Glide.with(context)
                                    .load(uri)
                                    //.fitCenter()
                                    //.dontAnimate()
                                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)         //ALL or NONE as your requirement
                                    .thumbnail(Glide.with(context).load(R.drawable.ic_img_loading))
                                    .error(R.drawable.ic_image_error)
                                    //.apply(new RequestOptions().override(1000, 500))
                                    .into(holder.imageView);
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
                                .into(holder.imageView);
                    }
                });
            }
        }
        holder.title.setText(models.get(position).getSchools().getSchoolName());
        holder.desc.setText(adf.format(models.get(position).getRating()));
        holder.ratingBar.setRating(models.get(position).getRating());
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView title, desc;
        RatingBar ratingBar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.image);
            this.title = itemView.findViewById(R.id.title);
            this.desc = itemView.findViewById(R.id.rating);
            this.ratingBar =itemView.findViewById(R.id.RatingBar);
        }
    }
}
