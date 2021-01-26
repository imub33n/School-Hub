package com.example.schoolhub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.schoolhub.data.LoginResult;
import com.example.schoolhub.ui.home.HomeFragment;
import com.google.android.material.navigation.NavigationView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignIn extends AppCompatActivity {

    EditText password,email;
    TextView sup;
    Button lin;
    public static String userName,userID;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    public static String BASE_URL = "http://192.168.10.9:8080/";
//InetAddress.getLocalHost().getHostAddress()
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //References
        lin = (Button) findViewById(R.id.loginBsi);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        sup = (TextView) findViewById(R.id.s_up);

        //retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
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
        if((email.getText().toString()).isEmpty()){
            Toast.makeText(this, "!!Please write your email address!!", Toast.LENGTH_SHORT).show();
        }else if((password.getText().toString()).isEmpty()){
            Toast.makeText(this, "!!Please write your password!!", Toast.LENGTH_SHORT).show();
        }else{
            HashMap<String, String> map = new HashMap<>();

            map.put("email", email.getText().toString());
            map.put("password", password.getText().toString());
            try{
                Call<LoginResult> call = retrofitInterface.executeLogin(map);
                call.enqueue(new Callback<LoginResult>() {
                    @Override
                    public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {

                        if (response.code() == 200) {
                            LoginResult result = response.body();
                            userID=result.getUserID();
                            userName=result.getUsername();
                            Intent it = new Intent( getApplicationContext() , HomePanel.class);
                            startActivity(it);
                            Toast.makeText(SignIn.this, result.getUserID(), Toast.LENGTH_LONG).show();
                        } else if (response.code() == 401) {
                            Toast.makeText(SignIn.this, "Wrong Credentials",
                                    Toast.LENGTH_LONG).show();
                        } else if (response.code() == 500) {
                            Toast.makeText(SignIn.this, "Email error",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<LoginResult> call, Throwable t) {
                        Toast.makeText(SignIn.this, t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }catch(Exception ex){
                Toast.makeText(SignIn.this, "Retrofit not working ", Toast.LENGTH_LONG).show();
            }



        }
    }
}
