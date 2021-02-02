package com.example.schoolhub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.kofigyan.stateprogressbar.StateProgressBar;

public class AddingSchoolStep3 extends AppCompatActivity {
    LinearLayout column2,column3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_school_step3);
        StateProgressBar stateProgressBar = (StateProgressBar) findViewById(R.id.your_state_progress_bar_id);
        stateProgressBar.setStateDescriptionData(AddingSchool.descriptionData);
        column2 =findViewById(R.id.columnClasses2);
        column3 =findViewById(R.id.columnClasses3);
    }

    public void backStep3(View view) {
        Intent intent = new Intent(getApplicationContext(),AddingSchoolStep2.class );
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    public void addColumn(View view) {
//        ViewGroup.LayoutParams layoutParams = column2.getLayoutParams();
//        float pixels =  100 * getResources().getDisplayMetrics().density;
//        layoutParams.width = (int) pixels;
//        column2.setLayoutParams(layoutParams);

    }

    public void nextStep3(View view) {
        Intent it = new Intent( getApplicationContext() , AddingSchoolStep4.class);
        it.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(it);
    }
}