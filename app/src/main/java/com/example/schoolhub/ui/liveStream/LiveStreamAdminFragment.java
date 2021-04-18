package com.example.schoolhub.ui.liveStream;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.schoolhub.Adapters.LivestreamRequestsAdapter;
import com.example.schoolhub.EditSchool;
import com.example.schoolhub.EditSchoolFeeStructure;
import com.example.schoolhub.EditSchoolPhotos;
import com.example.schoolhub.MainActivity;
import com.example.schoolhub.R;
import com.example.schoolhub.RequestsForSchoolAdmin;
import com.example.schoolhub.RetrofitInterface;
import com.example.schoolhub.SignIn;
import com.example.schoolhub.data.LiveStreamRequests;
import com.example.schoolhub.data.PreferenceData;
import com.example.schoolhub.data.SchoolData;
import com.example.schoolhub.ui.liveStream.LiveStreamCam;
import com.example.schoolhub.ui.liveStream.LiveStreamRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LiveStreamAdminFragment extends Fragment {
    Button liveRequestButton;
    TextView startLiveStream;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    List<SchoolData> schoolData;
    static String adminId= "";
    String adminIdGet;
    public static SchoolData yesSchoolData=new SchoolData();
    RecyclerView recyclerViewLivestreamRequest;
    LivestreamRequestsAdapter livestreamRequestsAdapter;
    ProgressBar progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.fragment_live_stream_admin, container, false);
        adminId=PreferenceData.getLoggedInUserData(getContext()).get("userID");
        liveRequestButton= root.findViewById(R.id.liveRequestButton);
        startLiveStream= root.findViewById(R.id.startLiveStream);
        progressBar = (ProgressBar) root.findViewById(R.id.progressBar);
        //retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);
        //listView
        recyclerViewLivestreamRequest = (RecyclerView) root.findViewById(R.id.recyclerViewLivestreamRequest);
        recyclerViewLivestreamRequest.setLayoutManager(new LinearLayoutManager(getContext()));

        Call<List<LiveStreamRequests>> call2 = retrofitInterface.getStreams();
        call2.enqueue(new Callback<List<LiveStreamRequests>>() {
            @Override
            public void onResponse(Call<List<LiveStreamRequests>> call, Response<List<LiveStreamRequests>> response) {
                if (response.code() == 200) {
//                    Toast.makeText(getContext(),"Size Matters: " +response.body().size(), Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    livestreamRequestsAdapter = new LivestreamRequestsAdapter(response.body(),getContext());
                    recyclerViewLivestreamRequest.setAdapter(livestreamRequestsAdapter);
                }else {
                    Toast.makeText(getContext(), "CODE: "+response.code(), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<List<LiveStreamRequests>> call, Throwable t) {

                Toast.makeText(getContext(), ""+t, Toast.LENGTH_LONG).show();
            }
        });

        Call<List<SchoolData>> call = retrofitInterface.getSchoolData();
        call.enqueue(new Callback<List<SchoolData>>() {
            @Override
            public void onResponse(Call<List<SchoolData>> call, Response<List<SchoolData>> response) {
                if (response.code() == 200) {
                    schoolData =  response.body();
                    for(int i=0;i<schoolData.size();i++){
                        adminIdGet=schoolData.get(i).getAdminID();
                        if(Objects.equals(adminIdGet, adminId)){
                            yesSchoolData=schoolData.get(i);
                            liveRequestButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getContext(), LiveStreamRequest.class );
                                    startActivity(intent);
                                }
                            });
                        }
                    }
                }else {
                    Toast.makeText(getContext(), "CODE: "+response.code(), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<List<SchoolData>> call, Throwable t) {

                Toast.makeText(getContext(), ""+t, Toast.LENGTH_LONG).show();
            }
        });

        startLiveStream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LiveStreamCam.class );
                startActivity(intent);
            }
        });
        return root;
    }
}