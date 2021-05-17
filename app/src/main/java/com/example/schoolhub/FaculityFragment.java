package com.example.schoolhub;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.schoolhub.Adapters.FacultyAdapter;
import com.example.schoolhub.Adapters.RequestFacultyAdapter;
import com.example.schoolhub.data.FacultyRequest;
import com.example.schoolhub.data.PreferenceData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FaculityFragment extends Fragment {
    TextView join_faculty;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    FacultyRequest facultyRequest = new FacultyRequest();

    RecyclerView recyclerViewFaculty;
    FacultyAdapter facultyAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =inflater.inflate(R.layout.fragment_faculity, container, false);
        join_faculty=root.findViewById(R.id.join_faculty);

        //ListView
        recyclerViewFaculty = (RecyclerView) root.findViewById(R.id.recyclerViewFaculty);
        recyclerViewFaculty.setLayoutManager(new LinearLayoutManager(getContext()));
        //set recycler
        facultyAdapter = new FacultyAdapter(InformationSchoolFragment.thisSchoolData.getTeachers(),getContext());
        recyclerViewFaculty.setAdapter(facultyAdapter);
        //enable button if teacher
        if(PreferenceData.getLoggedInUserData(getContext()).get("userType").equals("Teacher")){
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER;
            params.setMargins(0,25,0,0);
            join_faculty.setLayoutParams(params);
        }
        //retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);

        join_faculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facultyRequest.setSchoolID(InformationSchoolFragment.thisSchoolData.get_id());
                facultyRequest.setAdminID(InformationSchoolFragment.thisSchoolData.getAdminID());
                facultyRequest.setTeacherID(PreferenceData.getLoggedInUserData(getContext()).get("userID"));
                facultyRequest.setTeacherName(PreferenceData.getLoggedInUserData(getContext()).get("username"));
                facultyRequest.setTeacherEmail(PreferenceData.getLoggedInUserData(getContext()).get("userEmail"));
                facultyRequest.setTeacherProfilePic(PreferenceData.getLoggedInUserData(getContext()).get("userPic"));
                //POST REQUEST Faculty
                Call<Void> call = retrofitInterface.postTeacherRequest(facultyRequest);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.code() == 200) {
                            Toast.makeText(getContext(), "Request Sent to School Admin!", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getContext(), "Error Code: "+response.code(), Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getContext(), "Connection Error: "+t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        return root;

    }
}