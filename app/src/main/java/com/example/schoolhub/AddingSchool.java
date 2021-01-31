package com.example.schoolhub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AddingSchool extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_school);


//        @Override
//        public void onClick(View v) {
//
//            switch (v.getId()) {
//                case R.id.btnNext:
//                    Intent intent = new Intent(getApplicationContext(), );
//                    startActivity(intent);
//                    break;
//
//                case R.id.btnBack:
//                    finish();
//                    break;
//            }
//        }
    }

    public void nextStep1(View view) {
        Intent intent = new Intent(getApplicationContext(),AddingSchoolStep2.class );
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }
}