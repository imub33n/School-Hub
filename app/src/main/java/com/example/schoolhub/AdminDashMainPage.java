package com.example.schoolhub;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.User;
import com.example.schoolhub.data.EducationLevel;
import com.example.schoolhub.data.PreferenceData;
import com.example.schoolhub.data.SchoolData;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;

public class AdminDashMainPage extends Fragment {
    //retrofit
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    List<SchoolData> schoolData;
    public static SchoolData yesSchoolData=new SchoolData();
    public static EducationLevel yesEducationLevel =new EducationLevel();
    static String adminId="";
    String adminIdGet;
    Intent intent;
    CardView editGeneralCard,editPhotosCard,editFeeCard,editSettingsCard,editRequestCard;
    ProgressBar progressBar;
    Boolean firstTime= true;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_admin_dash_main_page, container, false);
        adminId= PreferenceData.getLoggedInUserData(getContext()).get("userID");
        editGeneralCard=root.findViewById(R.id.editGeneralCard);
        editPhotosCard=root.findViewById(R.id.editPhotosCard);
        editFeeCard=root.findViewById(R.id.editFeeCard);
        editSettingsCard=root.findViewById(R.id.editSettingsCard);
        editRequestCard=root.findViewById(R.id.editRequestCard);
        progressBar = (ProgressBar) root.findViewById(R.id.progressBar3);
        //retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);

        Call<List<SchoolData>> call = retrofitInterface.getSchoolData();
        call.enqueue(new Callback<List<SchoolData>>() {
            @Override
            public void onResponse(Call<List<SchoolData>> call, Response<List<SchoolData>> response) {
                if (response.code() == 200) {

                    //chat
                    if (CometChat.getLoggedInUser() == null) {
                        CometChat.login(PreferenceData.getLoggedInUserData(getContext()).get("userID"), MainActivity.authKey, new CometChat.CallbackListener<User>() {
                            @Override
                            public void onSuccess(User user) {
                                Log.d(TAG, "Login Successful : " + user.toString());
                            }

                            @Override
                            public void onError(CometChatException e) {
                                Log.d(TAG, "Login failed with exception: " + e.getMessage());
                            }
                        });
                    } else {
                        // User already logged in
                        Log.d(TAG, "Already loggedIn");
                    }
                    //endChat
                    progressBar.setVisibility(View.INVISIBLE);
                    schoolData =  response.body();
                    for(int i=0;i<schoolData.size();i++){
                        adminIdGet=schoolData.get(i).getAdminID();
                        if(Objects.equals(adminIdGet, adminId)){
                            yesSchoolData=schoolData.get(i);
                            yesEducationLevel=schoolData.get(i).getEducationLevel();
                            editGeneralCard.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    intent = new Intent(getContext(),EditSchool.class );
                                    startActivity(intent);
                                }
                            });
                            editFeeCard.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    intent = new Intent(getContext(),EditSchoolFeeStructure.class );
                                    startActivity(intent);
                                }
                            });
                            editPhotosCard.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    intent = new Intent(getContext(),EditSchoolPhotos.class );
                                    startActivity(intent);
                                }
                            });
                            editRequestCard.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    intent = new Intent(getContext(),RequestsForSchoolAdmin.class );
                                    startActivity(intent);
                                }
                            });
                            editSettingsCard.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    intent = new Intent(getContext(), EditAccountSettings.class );
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
        return root;
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        if(firstTime){
//            firstTime=false;
//        }else{
//            getActivity().finish();
//            startActivity(getActivity().getIntent());
//            firstTime=true;
//        }
//    }
}