package com.example.schoolhub.ui.liveStream;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolhub.Adapters.LivestreamRequestsAdapter;
import com.example.schoolhub.Adapters.LivestreamViewAdapter;
import com.example.schoolhub.HomePanel;
import com.example.schoolhub.MainActivity;
import com.example.schoolhub.R;
import com.example.schoolhub.RetrofitInterface;
import com.example.schoolhub.data.LiveStreamRequests;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LiveStreamFragment extends Fragment {
    Button viewLivestream;
    RecyclerView recyclerViewLivestreams;
    LivestreamViewAdapter livestreamViewAdapter;
    List<LiveStreamRequests> liveStreams= new ArrayList<>();
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    ProgressBar progressBar;
    DateTimeFormatter df = DateTimeFormatter.ofPattern("d/M/yyyy");

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_live_stream, container, false);
        recyclerViewLivestreams = root.findViewById(R.id.recyclerViewLivestreams);
        recyclerViewLivestreams.setLayoutManager(new LinearLayoutManager(getContext()));
        progressBar = (ProgressBar) root.findViewById(R.id.progressBar);

        //Retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        //current date
        LocalDate dateCurrent = LocalDate.parse(df.format(LocalDateTime.now()), df);

        //All Streams
        Call<List<LiveStreamRequests>> call2 = retrofitInterface.getStreams();
        call2.enqueue(new Callback<List<LiveStreamRequests>>() {
            @Override
            public void onResponse(Call<List<LiveStreamRequests>> call, Response<List<LiveStreamRequests>> response) {
                if (response.code() == 200) {
                    for(int i=0;i<response.body().size();i++){
                        LocalDate dateStream = LocalDate.parse(response.body().get(i).getDate(), df);
                        if(dateStream.isAfter(dateCurrent) || dateStream.isEqual(dateCurrent)){
                            liveStreams.add(response.body().get(i));
                        }
                    }
                    progressBar.setVisibility(View.INVISIBLE);

                    livestreamViewAdapter = new LivestreamViewAdapter(liveStreams,getContext());
                    recyclerViewLivestreams.setAdapter(livestreamViewAdapter);
                }else {
                    Toast.makeText(getContext(), "CODE: "+response.code(), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<List<LiveStreamRequests>> call, Throwable t) {

                Toast.makeText(getContext(), ""+t, Toast.LENGTH_LONG).show();
            }
        });

        return root;
    }
}
