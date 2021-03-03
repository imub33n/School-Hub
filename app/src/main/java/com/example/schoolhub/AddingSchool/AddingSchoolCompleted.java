package com.example.schoolhub.AddingSchool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.schoolhub.AdminDashMainPage;
import com.example.schoolhub.AdminDashboard;
import com.example.schoolhub.HomePanel;
import com.example.schoolhub.R;
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

    public void goHomeSchoolAdded(View view) {
        Intent it = new Intent( getApplicationContext() , AdminDashboard.class);
        startActivity(it);
    }
}