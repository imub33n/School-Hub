package com.example.schoolhub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.schoolhub.Adapters.RequestFacultyAdapter;
import com.example.schoolhub.data.FacultyRequest;
import com.example.schoolhub.data.PreferenceData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RequestsForSchoolAdmin extends AppCompatActivity {
    public Toolbar toolbarEdit;
    TextView heading;

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    RecyclerView recyclerViewRequestFaculty;
    RequestFacultyAdapter requestFacultyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests_for_school_admin);
        toolbarEdit = (Toolbar) findViewById(R.id.toolbarEditSchool);
        heading= findViewById(R.id.heading);

        //ListView
        recyclerViewRequestFaculty = (RecyclerView) findViewById(R.id.recyclerViewRequestFaculty);
        recyclerViewRequestFaculty.setLayoutManager(new LinearLayoutManager(this));

        //retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
        //get requests
        Call<List<FacultyRequest>> call = retrofitInterface.getFaculty();
        call.enqueue(new Callback<List<FacultyRequest>>() {
            @Override
            public void onResponse(Call<List<FacultyRequest>> call, Response<List<FacultyRequest>> response) {
                if (response.code() == 200) {
                    for(int i=0;i<response.body().size();i++){
                        if(!response.body().get(i).getSchoolID().equals(PreferenceData.getLoggedInUserData(getApplicationContext()).get("userID"))){
                            response.body().remove(i);
                        }
                    }
                    if(response.body().size()==0){
                        heading.setText("No New Requests");
                    }else{
                        requestFacultyAdapter = new RequestFacultyAdapter(response.body(),getApplicationContext());
                        recyclerViewRequestFaculty.setAdapter(requestFacultyAdapter);
                    }
                }else {
                    Toast.makeText(RequestsForSchoolAdmin.this, "Err Code: "+response.code(), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<List<FacultyRequest>> call, Throwable t) {
                Toast.makeText(RequestsForSchoolAdmin.this, "Err Connection: "+t, Toast.LENGTH_LONG).show();
            }
        });

        toolbarEdit.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                onBackPressed();
            }
        });

    }
}