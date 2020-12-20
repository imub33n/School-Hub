package com.example.schoolhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

public class SignUp extends AppCompatActivity {
    EditText email,phoneNo,password,userName;
    TextView lin;
    Button sup;
    FirebaseDatabase database;
    DatabaseReference users;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth =FirebaseAuth.getInstance();
        //DataBase
        database= FirebaseDatabase.getInstance();
        users =database.getReference("Users");

        //
        sup = (Button) findViewById(R.id.s_up);
        lin = (TextView) findViewById(R.id.loginBsi);
        userName = (EditText) findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.password);
        email = (EditText) findViewById(R.id.emailSup);
        phoneNo = (EditText) findViewById(R.id.phoneNo);
//        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/LatoLight.ttf");
//        Typeface custom_font1 = Typeface.createFromAsset(getAssets(), "fonts/LatoRegular.ttf");
//        phoneNo.setTypeface(custom_font);
//        sup.setTypeface(custom_font1);
//        password.setTypeface(custom_font);
//        lin.setTypeface(custom_font);
//        userName.setTypeface(custom_font);
//        email.setTypeface(custom_font);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();

        sup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewAccount();
            }
        });

        lin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent it = new Intent(SignUp.this,MainActivity.class);
                startActivity(it);
            }
        });
    }
    private void createNewAccount(){
        if(email.getText().toString().isEmpty()){
            Toast.makeText(this, "!!Please write your email address!!", Toast.LENGTH_SHORT).show();
        }else if(password.getText().toString().isEmpty()){
            Toast.makeText(this, "!!Please write your Password!!", Toast.LENGTH_SHORT).show();
        }else if(userName.getText().toString().isEmpty()){
            Toast.makeText(this, "!!Please write your Username!!", Toast.LENGTH_SHORT).show();
        }else if(phoneNo.getText().toString().isEmpty()){
            Toast.makeText(this, "!!Please enter your Phone No.!!", Toast.LENGTH_SHORT).show();
        }else{
            System.out.println("else)");
            mAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                System.out.println("SendEmailVerificationMessage()");
                                SendEmailVerificationMessage();

                            }else{
                                Toast.makeText(SignUp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void SendEmailVerificationMessage(){
        FirebaseUser user=mAuth.getCurrentUser();
        if(user!= null){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(SignUp.this, "!!Please check your email for verification message!!", Toast.LENGTH_SHORT).show();
                        Intent it = new Intent(SignUp.this,MainActivity.class);
                        //it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(it);
                        //mAuth.signOut();
                    }else{
                        Toast.makeText(SignUp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        mAuth.signOut();
                    }
                }
            });
        }
    }
}