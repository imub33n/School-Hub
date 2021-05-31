package com.example.schoolhub.Adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.schoolhub.AdminDashMainPage;
import com.example.schoolhub.EditSchoolPhotos;
import com.example.schoolhub.MainActivity;
import com.example.schoolhub.R;
import com.example.schoolhub.RetrofitInterface;
import com.example.schoolhub.data.Image;
import com.example.schoolhub.data.SchoolReviews;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;

public class EditPhotosViewAdapter extends RecyclerView.Adapter<EditPhotosViewAdapter.ViewHolder>{
    List<Image> images= new ArrayList<>();
    Context context;
    FirebaseStorage storage= FirebaseStorage.getInstance();
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    public EditPhotosViewAdapter(List<Image> images, EditSchoolPhotos editSchoolPhotos) {
        this.images=images;
        this.context=editSchoolPhotos;
    }

    @NonNull
    @Override
    public EditPhotosViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.frag_edit_photo,parent,false);
        EditPhotosViewAdapter.ViewHolder viewHolder = new EditPhotosViewAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EditPhotosViewAdapter.ViewHolder holder, int position) {
        if(images.get(position).getPath()!=null){
            try{
                StorageReference storageRef = storage.getReferenceFromUrl(images.get(position).getPath());
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
                                    .into(holder.image);
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
                                .into(holder.image);
                    }
                });
            }catch(Exception e){
                Log.d(TAG, "Photo loading failed : "+ e);
            }
        }
        holder.removeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //deleteImage
                HashMap<String, String> mapImage = new HashMap<>();
                mapImage.put("imageID", images.get(position).get_id());
                Call<Void> call2er = retrofitInterface.deleteImage(AdminDashMainPage.yesSchoolData.get_id(),mapImage);
                call2er.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(context, "Removed", Toast.LENGTH_LONG).show();
                            images.remove(position);
                            notifyDataSetChanged();
                        }else {
                            Toast.makeText(context, "Err Code: "+response.code(), Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(context, "Connection Err: "+t, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        LinearLayout removeImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            removeImage = itemView.findViewById(R.id.removeImage);
            image = itemView.findViewById(R.id.image);
            //retrofit
            retrofit = new Retrofit.Builder()
                    .baseUrl(MainActivity.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            retrofitInterface = retrofit.create(RetrofitInterface.class);
        }
    }
}

//https://stackoverflow.com/questions/43387925/use-recyclerview-to-hold-multiple-images-per-row