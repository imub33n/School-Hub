package com.example.schoolhub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.kofigyan.stateprogressbar.StateProgressBar;

public class AddingSchool extends AppCompatActivity {
    public static String[] descriptionData = {"General\nInfo", "Photos/\nVideos", "Fee\nDescription", "Mark\nLocation"};
    EditText schoolName,schoolEmail,schoolPhoneNo,schoolZip,schoolAbout,schoolAddress;
    RadioGroup radioGroupSkolType,radioGroupEducationType;
    RadioButton radioButtonSkolType,radioButtonEducationType;
    CheckBox checkBoxPrimary,checkBoxMiddle,checkBoxHigher;
    public static String schoolNames,schoolEmails,schoolAddresss,schoolPhoneNos,schoolAbouts,SkolType,EducationType;
    public static String EducationLevel="";
    public static int schoolZipi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_school);
        StateProgressBar stateProgressBar = (StateProgressBar) findViewById(R.id.your_state_progress_bar_id);
        stateProgressBar.setStateDescriptionData(descriptionData);
        stateProgressBar.setStateDescriptionTypeface("fonts/RobotoSlab-Light.ttf");
        stateProgressBar.setStateNumberTypeface("fonts/Questrial-Regular.ttf");
        schoolName= findViewById(R.id.schoolName);
        schoolEmail= findViewById(R.id.schoolEmail);
        schoolPhoneNo= findViewById(R.id.schoolPhoneNo);
        schoolZip= findViewById(R.id.schoolZip);
        schoolAbout= findViewById(R.id.schoolAbout);
        schoolAddress= findViewById(R.id.schoolAddress);
        checkBoxPrimary = findViewById(R.id.checkBoxPrimary);
        checkBoxMiddle = findViewById(R.id.checkBoxMiddle);
        checkBoxHigher = findViewById(R.id.checkBoxHigher);

    }

    public void nextStep1(View view) {
        radioGroupSkolType =  findViewById(R.id. radioGroupSkolType);
        radioGroupEducationType = findViewById(R.id.radioGroupEducationType);
        radioButtonSkolType = (RadioButton) findViewById(radioGroupSkolType.getCheckedRadioButtonId());
        radioButtonEducationType = (RadioButton) findViewById(radioGroupEducationType.getCheckedRadioButtonId());
        if(schoolName.getText().toString().isEmpty()&&
                schoolEmail.getText().toString().isEmpty()&&
                schoolPhoneNo.getText().toString().isEmpty()&&
                schoolZip.getText().toString().isEmpty()&&
                schoolAbout.getText().toString().isEmpty()){
            Toast.makeText(this,"Fill all the fields to continue!", Toast.LENGTH_SHORT).show();
        }else if(schoolName.getText().toString().isEmpty()){
            Toast.makeText(this,"Write School Name", Toast.LENGTH_SHORT).show();
        }else if(schoolPhoneNo.getText().toString().isEmpty()){
            Toast.makeText(this,"Write a Contact Number for school", Toast.LENGTH_SHORT).show();
        }else if(schoolEmail.getText().toString().isEmpty()){
            Toast.makeText(this,"Write School Email", Toast.LENGTH_SHORT).show();
        }else if(schoolAbout.getText().toString().isEmpty()){
            Toast.makeText(this,"Fill About School section", Toast.LENGTH_SHORT).show();
        }else if(schoolZip.getText().toString().isEmpty()){
            Toast.makeText(this,"Write zip code for school", Toast.LENGTH_SHORT).show();
        }else if(schoolAddress.getText().toString().isEmpty()){
            Toast.makeText(this,"Write school address", Toast.LENGTH_SHORT).show();
        }else if(radioGroupSkolType.getCheckedRadioButtonId()==-1){
            Toast.makeText(getApplicationContext(), "Select type of students enrolled", Toast.LENGTH_SHORT).show();
        }else if(!(checkBoxPrimary.isChecked()||checkBoxMiddle.isChecked()||checkBoxHigher.isChecked())){
            Toast.makeText(this,"Select education level", Toast.LENGTH_SHORT).show();
        }else if(radioGroupEducationType.getCheckedRadioButtonId()==-1){
            Toast.makeText(getApplicationContext(), "Select field of education", Toast.LENGTH_SHORT).show();
        }else{
            schoolNames=schoolName.getText().toString();
            schoolPhoneNos=schoolPhoneNo.getText().toString();
            schoolEmails=schoolEmail.getText().toString();
            schoolZipi=Integer.parseInt(schoolZip.getText().toString());
            schoolAbouts=schoolAbout.getText().toString();
            schoolAddresss=schoolAddress.getText().toString();
            SkolType =radioButtonSkolType.getText().toString();
            EducationType=radioButtonEducationType.getText().toString();
            if(checkBoxPrimary.isChecked()){
                EducationLevel=EducationLevel.concat(" "+checkBoxPrimary.getText().toString());
            }
            if(checkBoxMiddle.isChecked()){
                EducationLevel=EducationLevel.concat(" "+checkBoxMiddle.getText().toString());
            }
            if(checkBoxHigher.isChecked()){
                EducationLevel=EducationLevel.concat(" "+checkBoxHigher.getText().toString());
            }
            Intent intent = new Intent(getApplicationContext(),AddingSchoolStep2.class );
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }


    }
}