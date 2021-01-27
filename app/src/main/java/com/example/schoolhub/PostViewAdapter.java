package com.example.schoolhub;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.module.AppGlideModule;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.example.schoolhub.data.PostResult;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class PostViewAdapter extends RecyclerView.Adapter<PostViewAdapter.ViewHolder> {
    List<PostResult> resource;
    Context context;
    FirebaseStorage storage= FirebaseStorage.getInstance();
    StorageReference storageReference= storage.getReference();
    public PostViewAdapter(List<PostResult> postLists, Context context) {
        this.resource = postLists;
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
//        View listItem= layoutInflater.inflate(R.layout.post_body, parent, false);
//        ViewHolder viewHolder = new ViewHolder(listItem);
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.post_body,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewAdapter.ViewHolder holder, int position) {
        //ImageList imageList=imageLists.get(position);
        //holder.tvname.setText(imageList.getName());
        PostResult postResult=resource.get(position);
        holder.userNamePost.setText(postResult.getUsername());
        holder.postTextData.setText(postResult.getText());
        holder.timePost.setText(postResult.getTime());
        if(postResult.getImage()!=null){

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
        return resource.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView userNamePost,timePost,postTextData;
        public ImageView imagePost;
        public LinearLayout linearLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.userNamePost = itemView.findViewById(R.id.userNamePost);
            this.timePost = itemView.findViewById(R.id.timePost);
            this.postTextData = itemView.findViewById(R.id.postTextData);
            this.imagePost = itemView.findViewById(R.id.imagePost);
            //linearLayout = itemView.findViewById(R.id.linearLayout);
        }
    }
}
