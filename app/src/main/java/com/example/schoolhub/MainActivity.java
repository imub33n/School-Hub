package com.example.schoolhub;

import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.schoolhub.AddingSchool.AddingSchool;
import com.example.schoolhub.data.PreferenceData;
import com.example.schoolhub.data.SchoolData;
import com.example.schoolhub.ui.home.HomeFragment;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {
    public static String BASE_URL = "http://192.168.10.8:8080/";
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    List<SchoolData> schoolData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        if(PreferenceData.getUserLoggedInStatus(this)){
            if(Objects.equals(PreferenceData.getLoggedInUserData(getApplicationContext()).get("userID"),"School")){
                Call<List<SchoolData>> call2 = retrofitInterface.getSchoolData();
                call2.enqueue(new Callback<List<SchoolData>>() {
                    @Override
                    public void onResponse(Call<List<SchoolData>> call, Response<List<SchoolData>> response) {
                        if (response.code() == 200) {
                            Log.d("TAG",response.code()+"");
                            schoolData =  response.body();
                            boolean checkThis = false;
                            //Toast.makeText(getContext(),schoolData.toString(),Toast.LENGTH_SHORT).show();
                            for(int i=0;i<schoolData.size();i++){
                                String adminIdGet=schoolData.get(i).getAdminID();
                                if(Objects.equals(adminIdGet,PreferenceData.getLoggedInUserData(getApplicationContext()).get("userID"))){
                                    checkThis = true;
                                    Intent it = new Intent( getApplicationContext() , AdminDashboard.class);
                                    startActivity(it);
                                }
                            }
                            if(!checkThis){
                                Intent it = new Intent( getApplicationContext() , AddingSchool.class);
                                startActivity(it);
                            }
                        }else {
                            Toast.makeText(getApplicationContext(), "CODE: "+response.code(), Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<List<SchoolData>> call2, Throwable t) {
                        Toast.makeText(getApplicationContext(), ""+t, Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                Intent it = new Intent( getApplicationContext() , HomePanel.class);
                startActivity(it);
            }
        }else{
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent= new Intent(getApplicationContext(), LandingScreen.class);
                    startActivity(intent);
                }
            }, 2000);//timer set for 2 seconds
        }
   }
}