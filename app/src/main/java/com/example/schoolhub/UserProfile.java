package com.example.schoolhub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.schoolhub.Adapters.PostViewAdapter;
import com.example.schoolhub.data.PostResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserProfile extends AppCompatActivity {

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    ProgressBar progressBar;
    List<PostResult> resource= new ArrayList<>();
    PostViewAdapter adapter;
    TextView postStatus,phoneNoProfile,userNameProfile,emailProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        //retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        postStatus= findViewById(R.id.postStatus);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        phoneNoProfile= findViewById(R.id.phoneNoProfile);
        emailProfile= findViewById(R.id.emailProfile);
        userNameProfile= findViewById(R.id.userNameProfile);

        //setData
        userNameProfile.setText(SignIn.userName);
        //recycler
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.postViewProfile);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        //get shitpostings
        Call<List<PostResult>> call = retrofitInterface.doGetListResources();
        call.enqueue(new Callback<List<PostResult>>() {
            @Override
            public void onResponse(Call<List<PostResult>> call, Response<List<PostResult>> response) {
                if (response.code() == 200) {
                    progressBar.setVisibility(View.INVISIBLE);
                    for(int i=0;i<response.body().size();i++){
                        if(Objects.equals(response.body().get(i).getUserID(),SignIn.userID)){
                            resource.add(response.body().get(i));
                        }
                        if(i==response.body().size()-1){
                            if(resource.size()==0){
                                postStatus.setText("No Posts Yet!");
                            }else{
                                adapter = new PostViewAdapter(resource,getApplicationContext());
                                adapter.setHasStableIds(true);
                                recyclerView.setAdapter(adapter);
                            }
                        }
                    }

                }else {
                    Toast.makeText(getApplicationContext(), "some response code", Toast.LENGTH_LONG).show();
                }

            }
            @Override
            public void onFailure(Call<List<PostResult>> call, Throwable t) {

                Toast.makeText(getApplicationContext(), ""+t, Toast.LENGTH_LONG).show();
            }
        });

    }
}