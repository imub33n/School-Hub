package com.example.schoolhub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class UserSignIn extends AppCompatActivity {
    public static String User;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_in);
    }

    public void signInUser(View view) {
        Intent intent= new Intent(getApplicationContext(), SignIn.class);
        startActivity(intent);
        //getting photo tag for photo that called on Click method
        int taag = Integer.parseInt((String)view.getTag());
        //every tagged photo has a specific name, pizza size and price
        switch (taag) {
            case 0:
                User = "School Admin";
                break;
            case 1:
                User = "Parent";
                break;
            case 2:
                User = "Student";
                break;
            case 3:
                User = "Teacher";
                break;
        }
    }
}