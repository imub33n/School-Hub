package com.example.schoolhub;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.kofigyan.stateprogressbar.StateProgressBar;

public class AddingSchoolCompleted extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_school_completed);
        StateProgressBar stateProgressBar = (StateProgressBar) findViewById(R.id.your_state_progress_bar_id);
        stateProgressBar.setStateDescriptionData(AddingSchool.descriptionData);
        stateProgressBar.setStateDescriptionTypeface("fonts/RobotoSlab-Light.ttf");
        stateProgressBar.setStateNumberTypeface("fonts/Questrial-Regular.ttf");
    }
}