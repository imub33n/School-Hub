package com.example.schoolhub.Adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.schoolhub.R;
import com.example.schoolhub.data.Teachers;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.cometchat.pro.uikit.ui_components.shared.cometchatReaction.fragment.FragmentReactionObject.TAG;

public class FacultyAdapter extends RecyclerView.Adapter<FacultyAdapter.ViewHolder>{
    List<Teachers> teachersList;
    Context context;

    FirebaseStorage storage= FirebaseStorage.getInstance();

    public FacultyAdapter(List<Teachers> teachers, Context context) {
        this.teachersList=teachers;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_faculty , parent ,false);
        FacultyAdapter.ViewHolder viewHolder = new FacultyAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nameFaculty.setText(teachersList.get(position).getTeacherName());
        holder.eMailFaculty.setText(teachersList.get(position).getTeacherEmail());
        if(teachersList.get(position).getTeacherProfilePic().isEmpty()){

        }else{
            StorageReference storageRef = storage.getReferenceFromUrl(teachersList.get(position).getTeacherProfilePic());
            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(context)
                            .load(uri)
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)         //ALL or NONE as your requirement
                            .thumbnail(Glide.with(context).load(R.drawable.ic_img_loading))
                            .error(R.drawable.ic_image_error)
                            .into(holder.imageViewFaculty);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                    Glide.with(context)
                            .load(R.drawable.ic_image_error)
                            .fitCenter()
                            .into(holder.imageViewFaculty);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return teachersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imageViewFaculty;
        TextView nameFaculty,eMailFaculty;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameFaculty =itemView.findViewById(R.id.nameFaculty);
            eMailFaculty =itemView.findViewById(R.id.eMailFaculty);
            imageViewFaculty =itemView.findViewById(R.id.imageViewFaculty);
        }
    }
}
