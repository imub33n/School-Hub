package com.example.schoolhub.Adapters;

import android.content.Context;
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
import com.example.schoolhub.InformationSchoolFragment;
import com.example.schoolhub.MainActivity;
import com.example.schoolhub.R;
import com.example.schoolhub.RetrofitInterface;
import com.example.schoolhub.SendNotification;
import com.example.schoolhub.UserProfile;
import com.example.schoolhub.data.FacultyRequest;
import com.example.schoolhub.data.PreferenceData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;

public class RequestFacultyAdapter extends RecyclerView.Adapter<RequestFacultyAdapter.ViewHolder>{
    List<FacultyRequest> facultyRequests;
    Context context;
    FirebaseStorage storage= FirebaseStorage.getInstance();
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    public RequestFacultyAdapter(List<FacultyRequest> body, Context context) {
        this.facultyRequests=body;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_faculty_requests,parent,false);
        RequestFacultyAdapter.ViewHolder viewHolder = new RequestFacultyAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nameFaculty.setText(facultyRequests.get(position).getTeacherName());
        holder.emailFaculity.setText(facultyRequests.get(position).getTeacherEmail());
        try{
            StorageReference storageRef = storage.getReferenceFromUrl(facultyRequests.get(position).getTeacherProfilePic());
            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    try{
                        Glide.with(context)
                                .load(uri)
                                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)         //ALL or NONE as your requirement
                                .thumbnail(Glide.with(context).load(R.drawable.ic_img_loading))
                                .error(R.drawable.ic_image_error)
                                .into(holder.imageFaculty);

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
                            .into(holder.imageFaculty);
                }
            });
        }catch(Exception e){
            Log.d(TAG, "Photo loading failed : "+ e);
        }
        HashMap<String, String> mapRequestStatus = new HashMap<>();
        holder.acceptRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapRequestStatus.put("status", "Accepted");
                mapRequestStatus.put("schoolID", AdminDashMainPage.yesSchoolData.get_id());

                Call<Void> called = retrofitInterface.teacherRequest(facultyRequests.get(position).getRequestID(),mapRequestStatus);
                called.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.code() == 200) {
                            //notiStart
                            String title="Join Request";
                            String subTitle = "Added to "+AdminDashMainPage.yesSchoolData.getSchoolName()+" faculty";
                            new SendNotification(title,subTitle, PreferenceData.getLoggedInUserData(context).get("userID"), facultyRequests.get(position).getTeacherID());
                            //notiEnd
                            Toast.makeText(context, "Teacher added to school", Toast.LENGTH_LONG).show();
                            facultyRequests.remove(position);
                            notifyDataSetChanged();
                        }else {
                            Toast.makeText(context, "Err Code: "+response.code(), Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(context, "Status update error: "+t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        holder.rejectRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapRequestStatus.put("status", "Rejected");
                mapRequestStatus.put("schoolID", AdminDashMainPage.yesSchoolData.get_id());
                Call<Void> called = retrofitInterface.teacherRequest(facultyRequests.get(position).getRequestID(),mapRequestStatus);
                called.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.code() == 200) {
                            //notiStart
                            String title="Join Request";
                            String subTitle = ""+AdminDashMainPage.yesSchoolData.getSchoolName()+" rejected your join request!";
                            new SendNotification(title,subTitle, PreferenceData.getLoggedInUserData(context).get("userID"), facultyRequests.get(position).getTeacherID());
                            //notiEnd
                            Toast.makeText(context, "Removed", Toast.LENGTH_LONG).show();
                            facultyRequests.remove(position);
                            notifyDataSetChanged();
                        }else {
                            Toast.makeText(context, "Err Code: "+response.code(), Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(context, "Status update error: "+t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return facultyRequests.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imageFaculty;
        ImageView rejectRequest,acceptRequest;
        TextView emailFaculity,nameFaculty;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameFaculty=itemView.findViewById(R.id.nameFaculty);
            emailFaculity=itemView.findViewById(R.id.emailFaculity);
            imageFaculty=itemView.findViewById(R.id.imageFaculty);
            acceptRequest=itemView.findViewById(R.id.acceptRequest);
            rejectRequest=itemView.findViewById(R.id.rejectRequest);
            //retrofit
            retrofit = new Retrofit.Builder()
                    .baseUrl(MainActivity.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            retrofitInterface = retrofit.create(RetrofitInterface.class);
        }
    }
}
