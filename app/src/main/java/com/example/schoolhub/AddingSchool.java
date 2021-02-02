package com.example.schoolhub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.kofigyan.stateprogressbar.StateProgressBar;

public class AddingSchool extends AppCompatActivity {
    public static String[] descriptionData = {"General Info", "Photos/Videos", "Fee Description", "Mark Location"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_school);
        StateProgressBar stateProgressBar = (StateProgressBar) findViewById(R.id.your_state_progress_bar_id);
        stateProgressBar.setStateDescriptionData(descriptionData);


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