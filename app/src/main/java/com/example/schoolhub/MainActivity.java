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
import androidx.appcompat.app.AppCompatDelegate;

import com.cometchat.pro.core.AppSettings;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.uikit.ui_resources.utils.Utils;
import com.example.schoolhub.AddingSchool.AddingSchool;
import com.example.schoolhub.data.PreferenceData;
import com.example.schoolhub.data.SchoolData;
import com.example.schoolhub.data.SchoolReviews;
import com.example.schoolhub.ui.home.HomeFragment;

import java.util.ArrayList;
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
    //chatAPI
    public static String appID = "323611fede35399";
    public static String region = "us";
    public static String authKey = "bdceaa21c369442ac6ddbbe1e68a7fc56596017a";
    public static String API_KEY = "3972dfed09f25aabc875f5e613e862b39db70fca";
    //data
    public static List<SchoolData> allSchools= new ArrayList<>();
    public static List<SchoolReviews> allSchoolReviews = new ArrayList<>();

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
        //get data
        //request review data
        Call<List<SchoolReviews>> call2er = retrofitInterface.getReviews();
        call2er.enqueue(new Callback<List<SchoolReviews>>() {
            @Override
            public void onResponse(Call<List<SchoolReviews>> call, Response<List<SchoolReviews>> response) {
                if (response.code() == 200) {
                    allSchoolReviews = response.body();
                }else {
                    Toast.makeText(MainActivity.this, "CODE: "+response.code(), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<List<SchoolReviews>> call, Throwable t) {
                Toast.makeText(MainActivity.this, ""+t, Toast.LENGTH_LONG).show();
            }
        });
        //schools data
        Call<List<SchoolData>> call = retrofitInterface.getSchoolData();
        call.enqueue(new Callback<List<SchoolData>>() {
            @Override
            public void onResponse(Call<List<SchoolData>> call, Response<List<SchoolData>> response) {
                if (response.code() == 200) {
                    allSchools =  response.body();
                }
            }
            @Override
            public void onFailure(Call<List<SchoolData>> call, Throwable t) {
                Toast.makeText(MainActivity.this, ""+t, Toast.LENGTH_LONG).show();
            }
        });
        //chat
        AppSettings appSettings=new AppSettings.AppSettingsBuilder().subscribePresenceForAllUsers().setRegion(region).build();
        CometChat.init(this, appID,appSettings, new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String successMessage) {
                //UIKitSettings.setAuthKey(authKey);
                CometChat.setSource("ui-kit","android","java");
                Log.d(TAG, "Initialization completed successfully");
            }

            @Override
            public void onError(CometChatException e) {
                Log.d(TAG, "Initialization failed with exception: " + e.getMessage());
            }
        });
//        //dark mode ke chuti
//        if(Utils.isDarkMode(getApplicationContext())){
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//        }
        //chat login chk
        if(PreferenceData.getUserLoggedInStatus(this)){
            if(Objects.equals(PreferenceData.getLoggedInUserData(getApplicationContext()).get("userType"),"School")){
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
                //dark mode ke chuti
                if(Utils.isDarkMode(getApplicationContext())){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
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
                    //overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
                }
            }, 3000);//timer set for 3 seconds
        }
   }
}