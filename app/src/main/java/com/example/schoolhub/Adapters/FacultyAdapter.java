package com.example.schoolhub.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.schoolhub.AdminDashMainPage;
import com.example.schoolhub.MainActivity;
import com.example.schoolhub.R;
import com.example.schoolhub.RetrofitInterface;
import com.example.schoolhub.data.Teachers;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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

public class FacultyAdapter extends RecyclerView.Adapter<FacultyAdapter.ViewHolder>{
    List<Teachers> teachersList;
    Context context;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
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
//        Log.d(TAG, "onBindViewHolder:_______________this___________ "+context.getClass().getSimpleName());
        if(Objects.equals(context.getClass().getSimpleName(),"RequestsForSchoolAdmin")){
            holder.delete_icon.setVisibility(View.VISIBLE);
        }
        holder.delete_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
                builder2.setMessage("Remove from school faculty?");
                builder2.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // delete ...
                        HashMap<String, String> mapID = new HashMap<>();

                        mapID.put("teacherID", teachersList.get(position).getTeacherID());
                        Call<Void> call2er = retrofitInterface.deleteTeacher(AdminDashMainPage.yesSchoolData.get_id(),mapID);
                        call2er.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(context,"Faculty Member Removed",Toast.LENGTH_LONG).show();
                                    teachersList.remove(position);
                                    notifyDataSetChanged();
                                }else {
                                    Toast.makeText(context, "CODE: "+response.code(), Toast.LENGTH_LONG).show();
                                }
                            }
                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(context, "Connection Err: "+t, Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                builder2.create();
                builder2.show();
            }
        });
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
        ImageView delete_icon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameFaculty =itemView.findViewById(R.id.nameFaculty);
            eMailFaculty =itemView.findViewById(R.id.eMailFaculty);
            imageViewFaculty =itemView.findViewById(R.id.imageViewFaculty);
            delete_icon =itemView.findViewById(R.id.delete_icon);
            retrofit = new Retrofit.Builder()
                    .baseUrl(MainActivity.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            retrofitInterface = retrofit.create(RetrofitInterface.class);
        }
    }
}
