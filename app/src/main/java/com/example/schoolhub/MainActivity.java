package com.example.schoolhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Login Activity";
    FirebaseDatabase database;
    DatabaseReference users;
    EditText password,email;
    TextView sup;
    Button lin;
    FirebaseAuth mAuth;
    Boolean emailChk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent it = new Intent( getApplicationContext() ,HomePanel.class);
        startActivity(it);
//
//        //DataBase
//        database= FirebaseDatabase.getInstance();
//        users =database.getReference("Users");
//        //
//        lin = (Button) findViewById(R.id.loginBsi);
//        email = (EditText) findViewById(R.id.email);
//        password = (EditText) findViewById(R.id.password);
//        sup = (TextView) findViewById(R.id.s_up);
////        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/LatoLight.ttf");
////        Typeface custom_font1 = Typeface.createFromAsset(getAssets(), "fonts/LatoRegular.ttf");
////        lin.setTypeface(custom_font1);
////        sup.setTypeface(custom_font);
////        email.setTypeface(custom_font);
////        password.setTypeface(custom_font);
//        sup.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                Intent it = new Intent( getApplicationContext() , SignUp.class);
//                startActivity(it);
//            }
//        });
//        lin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                allowingUserToLogin();
//
//            }
//        });
//    }
//
//    private void allowingUserToLogin(){
//        if((email.getText().toString()).isEmpty()){
//            Toast.makeText(this, "!!Please write your email address!!", Toast.LENGTH_SHORT).show();
//        }else if((password.getText().toString()).isEmpty()){
//            Toast.makeText(this, "!!Please write your password!!", Toast.LENGTH_SHORT).show();
//        }else{
//            try {
//                mAuth = FirebaseAuth.getInstance();
//                mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
//                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                if (task.isSuccessful()) {
//                                    VerifyEmailAddress();
//                                } else {
//                                    Toast.makeText( getApplicationContext() , task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });
//            } catch (Exception e){
//                Log.e(TAG, e.getMessage());
//            }
//        }
//    }
//    private void VerifyEmailAddress(){
//        FirebaseUser user=mAuth.getCurrentUser();
//        emailChk=user.isEmailVerified();
//        if(emailChk){
//            Intent it = new Intent( getApplicationContext() ,HomePanel.class);
//            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK  );
//            startActivity(it);
//        }else{
//            Toast.makeText(this, "!!Please verify your Account!!", Toast.LENGTH_SHORT).show();
//            mAuth.signOut();
//        }
    }
}