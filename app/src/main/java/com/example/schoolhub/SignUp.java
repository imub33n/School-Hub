package com.example.schoolhub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.User;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import static android.content.ContentValues.TAG;

public class SignUp extends AppCompatActivity {
    EditText email, phoneNo, password, userName;
    TextView lin;
    Button sup;

    private RadioGroup radioGroup;
    private RadioButton radioButton;

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
        //
        sup = (Button) findViewById(R.id.s_up);
        lin = (TextView) findViewById(R.id.loginBsi);
        userName = (EditText) findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.password);
        email = (EditText) findViewById(R.id.emailSup);
        phoneNo = (EditText) findViewById(R.id.phoneNo);

        lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(SignUp.this, SignIn.class);
                startActivity(it);
            }
        });
    }

    public void createAccount(View view) {
        radioGroup = (RadioGroup) findViewById(R.id.signupRadioGroup);
        radioButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());

        if (email.getText().toString().isEmpty()) {
            Toast.makeText(this, "!!Please write your email address!!", Toast.LENGTH_SHORT).show();
        } else if (password.getText().toString().isEmpty()) {
            Toast.makeText(this, "!!Please write your Password!!", Toast.LENGTH_SHORT).show();
        } else if (userName.getText().toString().isEmpty()) {
            Toast.makeText(this, "!!Please write your Username!!", Toast.LENGTH_SHORT).show();
        } else if (phoneNo.getText().toString().isEmpty()) {
            Toast.makeText(this, "!!Please enter your Phone No.!!", Toast.LENGTH_SHORT).show();
        } else if (radioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getApplicationContext(), "Please select Account type!!", Toast.LENGTH_SHORT).show();
        } else {

                HashMap<String, String> map = new HashMap<>();

                map.put("username", userName.getText().toString());
                map.put("email", email.getText().toString());
                map.put("password", password.getText().toString());
                map.put("phoneNumber", password.getText().toString());
                //Toast.makeText(SignUp.this, "type chk: "+radioButton.getText().toString(), Toast.LENGTH_LONG).show();
                map.put("type", radioButton.getText().toString());

                Call<HashMap<String, String>> call = retrofitInterface.executeSignup(map);
                call.enqueue(new Callback<HashMap<String, String>>() {
                    @Override
                    public void onResponse(Call<HashMap<String, String>> call, Response<HashMap<String, String>> response) {
                        if(response.isSuccessful()){
                            //create chat user
                            String authKey = MainActivity.authKey; // Replace with your App Auth Key
                            User user = new User();
                            user.setUid(response.body().get("_id")); // Replace with the UID for the user to be created
                            user.setName(response.body().get("username")); // Replace with the name of the user

                            CometChat.createUser(user, authKey, new CometChat.CallbackListener<User>() {
                                @Override
                                public void onSuccess(User user) {
                                    Log.d("createUser", user.toString());
                                    Toast.makeText(SignUp.this,"Signed up successfully", Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onError(CometChatException e) {
                                    Log.e("createUser", e.getMessage());
                                }
                            });

                            //Toast.makeText(SignUp.this,"Signed up successfully", Toast.LENGTH_LONG).show();
                            Intent it = new Intent(SignUp.this, SignIn.class);
                            startActivity(it);
                        } else {
                            if(response.code()==500){
                                Toast.makeText(SignUp.this,
                                        "User already exists", Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(SignUp.this,
                                        "Response Code: "+response.code(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<HashMap<String, String>> call, Throwable t) {
                        Toast.makeText(SignUp.this, " sup "+t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
        }
    }
}