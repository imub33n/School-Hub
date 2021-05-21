package com.example.schoolhub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.schoolhub.AddingSchool.AddingSchool;
import com.example.schoolhub.data.LoginResult;
import com.example.schoolhub.data.PreferenceData;
import com.example.schoolhub.data.SchoolData;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import static android.content.ContentValues.TAG;

public class SignIn extends AppCompatActivity {

    EditText password,email;
    TextView sup;
    Button lin;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    //public static String userName,userID,userType="";
    List<SchoolData> schoolData;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

//    public static String BASE_URL = "http://10.113.61.179:8080/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        //Toast.makeText(this,MainActivity.BASE_URL,Toast.LENGTH_SHORT).show();
        //References
        lin = (Button) findViewById(R.id.loginBsi);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        sup = (TextView) findViewById(R.id.s_up);
//        email.setText("mubeenafzal3@gmail.com");
//        password.setText("12345");
        //retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);
        sup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent it = new Intent( getApplicationContext() , SignUp.class);
                startActivity(it);
            }
        });
        lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allowingUserToLogin();
            }
        });
    }

    private void allowingUserToLogin(){
        radioGroup = (RadioGroup) findViewById(R.id.signipRadioGroup);
        radioButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
        if((email.getText().toString()).isEmpty()){
            Toast.makeText(this, "!!Please write your email address!!", Toast.LENGTH_SHORT).show();
        }else if((password.getText().toString()).isEmpty()){
            Toast.makeText(this, "!!Please write your password!!", Toast.LENGTH_SHORT).show();
        }else if(radioGroup.getCheckedRadioButtonId() == -1){
            Toast.makeText(getApplicationContext(), "Please select Account type!!", Toast.LENGTH_SHORT).show();
        }else{
            HashMap<String, String> map = new HashMap<>();

            map.put("email", email.getText().toString());
            map.put("password", password.getText().toString());
            map.put("type",radioButton.getText().toString());
            try{
                Call<LoginResult> call = retrofitInterface.executeLogin(map);
                call.enqueue(new Callback<LoginResult>() {
                    @Override
                    public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                        if (response.isSuccessful()) {

                            LoginResult result = response.body();

                            PreferenceData.setUserLoggedInStatus(getApplicationContext(),true);
                            PreferenceData.setLoggedInUserData(getApplicationContext(),result.getUsername(),result.getUserID(),result.getType(),result.getEmail(),result.getProfilePic());

                            if(Objects.equals(radioButton.getText().toString(),"School")){
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
                                                if(Objects.equals(adminIdGet, result.getUserID())){
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
                            //Toast.makeText(SignIn.this, result.getUserID(), Toast.LENGTH_LONG).show();
                        } else if (response.code() == 401) {
                            Toast.makeText(SignIn.this, "Wrong Credentials",
                                    Toast.LENGTH_LONG).show();
                        } else if (response.code() == 500) {
                            Toast.makeText(SignIn.this, "Email error",
                                    Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(SignIn.this, "Err "+response.code(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<LoginResult> call, Throwable t) {
                        Toast.makeText(SignIn.this, t.getMessage()+"ERR",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }catch(Exception ex){
                Toast.makeText(SignIn.this, "Retrofit not working ", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void goBackFromSignIn(View view) {
        Intent it = new Intent( getApplicationContext() , LandingScreen.class);
        startActivity(it);
    }
}
