package com.example.schoolhub;


import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.schoolhub.data.Image;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import static android.content.ContentValues.TAG;
public class SlideAdapter extends RecyclerView.Adapter<SlideAdapter.ViewHolder> {
    //int []images;
    List<Image> images= new ArrayList<>();
    Context context;
    FirebaseStorage storage= FirebaseStorage.getInstance();
    public SlideAdapter(List<Image> images,Context context){
        this.images=images;
        this.context=context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.slide,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //holder.imageView.setBackgroundResource(Integer.parseInt(images.get(position).getPath()));
            //Log.d(TAG, "onBindViewHolder:__________________"+position+"______________ "+images.get(position).getPath());
            if(images.get(position).getPath()!=null){
                StorageReference storageRef = storage.getReferenceFromUrl(images.get(position).getPath());
                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(context)
                                .load(uri)
                                //.fitCenter()
                                //.dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)         //ALL or NONE as your requirement
                                .thumbnail(Glide.with(context).load(R.drawable.ic_image_loading))
                                .error(R.drawable.ic_image_error)
                                //.apply(new RequestOptions().override(1000, 500))
                                .into(holder.imageView);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                        Toast.makeText(context, "ye nae load ho rhi", Toast.LENGTH_LONG).show();
                        Glide.with(context)
                                .load(R.drawable.ic_image_error)
                                .fitCenter()
                                .into(holder.imageView);
                    }
                });
            }

    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.slideImage);

        }
    }

}