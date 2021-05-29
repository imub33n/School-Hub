package com.example.schoolhub.AddingSchool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.schoolhub.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kofigyan.stateprogressbar.StateProgressBar;

public class AddingSchoolStep3 extends AppCompatActivity {
    EditText AddFee,AddFee2,AddFee3,TutionFee,TutionFee2,TutionFee3,ExamFee,ExamFee2,ExamFee3,
            sports,sports2,sports3,lab,lab2,lab3,library,library2,library3,totalAddFee,totalAddFee2,
            totalAddFee3,monthlyFee,monthlyFee2,monthlyFee3,others,others2,others3;
    LinearLayout columnClasses,columnClasses2,columnClasses3;

    public static int iAddFee,iAddFee2,iAddFee3,iTutionFee,iTutionFee2,iTutionFee3,iExamFee,iExamFee2,iExamFee3,
            isports,isports2,isports3,ilab,ilab2,ilab3,ilibrary,ilibrary2,ilibrary3,itotalAddFee,itotalAddFee2,
            itotalAddFee3,imonthlyFee,imonthlyFee2,imonthlyFee3,iothers,iothers2,iothers3;

    FloatingActionButton floatingNextButton3,floatingBackButton3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_school_step3);
        StateProgressBar stateProgressBar = (StateProgressBar) findViewById(R.id.your_state_progress_bar_id);
        stateProgressBar.setStateDescriptionData(AddingSchool.descriptionData);

        AddFee=findViewById(R.id.AddFee);
        AddFee3=findViewById(R.id.AddFee3);
        AddFee2=findViewById(R.id.AddFee2);
        TutionFee=findViewById(R.id.TutionFee);
        TutionFee2=findViewById(R.id.TutionFee2);
        TutionFee3=findViewById(R.id.TutionFee3);
        ExamFee=findViewById(R.id.ExamFee);
        ExamFee2=findViewById(R.id.ExamFee2);
        ExamFee3=findViewById(R.id.ExamFee3);
        sports=findViewById(R.id.sports);
        sports2=findViewById(R.id.sports2);
        sports3=findViewById(R.id.sports3);
        lab=findViewById(R.id.lab);
        lab2=findViewById(R.id.lab2);
        lab3=findViewById(R.id.lab3);
        library=findViewById(R.id.library);
        library2=findViewById(R.id.library2);
        library3=findViewById(R.id.library3);
        totalAddFee=findViewById(R.id.totalAddFee);
        totalAddFee2=findViewById(R.id.totalAddFee2);
        totalAddFee3=findViewById(R.id.totalAddFee3);
        monthlyFee=findViewById(R.id.monthlyFee);
        monthlyFee2=findViewById(R.id.monthlyFee2);
        monthlyFee3=findViewById(R.id.monthlyFee3);
        others=findViewById(R.id.others);
        others2=findViewById(R.id.others2);
        others3=findViewById(R.id.others3);
        columnClasses= findViewById(R.id.columnClasses);
        columnClasses2=findViewById(R.id.columnClasses2);
        columnClasses3=findViewById(R.id.columnClasses3);

        floatingNextButton3=findViewById(R.id.floatingNextButton3);
        floatingBackButton3=findViewById(R.id.floatingBackButton3);

        if(!AddingSchool.EducationLevel.getPrimary()){
            columnClasses.setLayoutParams(new LinearLayout.LayoutParams(0,0));
        }else{
            if(!AddingSchool.EducationLevel.getMiddle()){
                if(!AddingSchool.EducationLevel.getHigher()){
                    columnClasses.setLayoutParams(new LinearLayout.LayoutParams(300,0));
                }
            }
        }
        if(!AddingSchool.EducationLevel.getMiddle()){
            columnClasses2.setLayoutParams(new LinearLayout.LayoutParams(0,0));
        }else{
            if(!AddingSchool.EducationLevel.getPrimary()){
                if(!AddingSchool.EducationLevel.getHigher()){
                    columnClasses2.setLayoutParams(new LinearLayout.LayoutParams(300,0));
                }
            }
        }
        if(!AddingSchool.EducationLevel.getHigher()){
            columnClasses3.setLayoutParams(new LinearLayout.LayoutParams(0,0));
        }else{
            if(!AddingSchool.EducationLevel.getPrimary()){
                if(!AddingSchool.EducationLevel.getMiddle()){
                    columnClasses3.setLayoutParams(new LinearLayout.LayoutParams(300,0));
                }
            }
        }
        floatingBackButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveInstanceState(savedInstanceState);
                Intent intent = new Intent(getApplicationContext(),AddingSchoolStep2.class );
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
        floatingNextButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(AddingSchool.EducationLevel.getPrimary()){
                    iAddFee=Integer.parseInt(AddFee.getText().toString());
                    iExamFee=Integer.parseInt(ExamFee.getText().toString());
                    ilab=Integer.parseInt(lab.getText().toString());
                    ilibrary=Integer.parseInt(library.getText().toString());
                    iTutionFee=Integer.parseInt(TutionFee.getText().toString());
                    isports=Integer.parseInt(sports.getText().toString());
                    itotalAddFee=Integer.parseInt(totalAddFee.getText().toString());
                    iothers=Integer.parseInt(others.getText().toString());
                    imonthlyFee=Integer.parseInt(monthlyFee.getText().toString());
                }
                if(AddingSchool.EducationLevel.getMiddle()){
                    iAddFee2=Integer.parseInt(AddFee2.getText().toString());
                    iExamFee2=Integer.parseInt(ExamFee2.getText().toString());
                    ilab2=Integer.parseInt(lab2.getText().toString());
                    ilibrary2=Integer.parseInt(library2.getText().toString());
                    iTutionFee2=Integer.parseInt(TutionFee2.getText().toString());
                    isports2=Integer.parseInt(sports2.getText().toString());
                    itotalAddFee2=Integer.parseInt(totalAddFee2.getText().toString());
                    iothers2=Integer.parseInt(others2.getText().toString());
                    imonthlyFee2=Integer.parseInt(monthlyFee2.getText().toString());
                }
                if(AddingSchool.EducationLevel.getHigher()) {
                    iAddFee3 = Integer.parseInt(AddFee3.getText().toString());
                    iExamFee3 = Integer.parseInt(ExamFee3.getText().toString());
                    ilab3 = Integer.parseInt(lab3.getText().toString());
                    ilibrary3 = Integer.parseInt(library3.getText().toString());
                    iTutionFee3 = Integer.parseInt(TutionFee3.getText().toString());
                    isports3 = Integer.parseInt(sports3.getText().toString());
                    itotalAddFee3 = Integer.parseInt(totalAddFee3.getText().toString());
                    iothers3 = Integer.parseInt(others3.getText().toString());
                    imonthlyFee3 = Integer.parseInt(monthlyFee3.getText().toString());
                }
                onSaveInstanceState(savedInstanceState);
                Intent it = new Intent( getApplicationContext() , AddingSchoolStep4.class);
                it.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(it);

            }
        });
    }
}