package com.example.schoolhub.AddingSchool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.schoolhub.R;
import com.example.schoolhub.data.EducationLevel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kofigyan.stateprogressbar.StateProgressBar;

public class AddingSchool extends AppCompatActivity {
    public static String[] descriptionData = {"General\nInfo", "Photos/\nVideos", "Fee\nDescription", "Mark\nLocation"};
    EditText schoolName,schoolEmail,schoolPhoneNo,schoolZip,schoolAbout,schoolAddress;
    RadioGroup radioGroupSkolType,radioGroupEducationType;
    RadioButton radioButtonSkolType,radioButtonEducationType;
    CheckBox checkBoxPrimary,checkBoxMiddle,checkBoxHigher;
    FloatingActionButton floatingNextButton;
    public static String schoolNames,schoolEmails,schoolAddresss,schoolPhoneNos,schoolAbouts,SkolType,EducationType;
    public static EducationLevel EducationLevel= new EducationLevel();
    public static int schoolZipi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle saveThis= new Bundle();
        setContentView(R.layout.activity_adding_school);
        if (!hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
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
        floatingNextButton= findViewById(R.id.floatingNextButton);
        floatingNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioGroupSkolType =  findViewById(R.id. radioGroupSkolType);
                radioGroupEducationType = findViewById(R.id.radioGroupEducationType);
                radioButtonSkolType = (RadioButton) findViewById(radioGroupSkolType.getCheckedRadioButtonId());
                radioButtonEducationType = (RadioButton) findViewById(radioGroupEducationType.getCheckedRadioButtonId());
                if(schoolName.getText().toString().isEmpty()&&
                        schoolEmail.getText().toString().isEmpty()&&
                        schoolPhoneNo.getText().toString().isEmpty()&&
                        schoolZip.getText().toString().isEmpty()&&
                        schoolAbout.getText().toString().isEmpty()){
                    Toast.makeText(AddingSchool.this,"Fill all the fields to continue!", Toast.LENGTH_SHORT).show();
                }else if(schoolName.getText().toString().isEmpty()){
                    Toast.makeText(AddingSchool.this,"Write School Name", Toast.LENGTH_SHORT).show();
                }else if(schoolPhoneNo.getText().toString().isEmpty()){
                    Toast.makeText(AddingSchool.this,"Write a Contact Number for school", Toast.LENGTH_SHORT).show();
                }else if(schoolEmail.getText().toString().isEmpty()){
                    Toast.makeText(AddingSchool.this,"Write School Email", Toast.LENGTH_SHORT).show();
                }else if(schoolAbout.getText().toString().isEmpty()){
                    Toast.makeText(AddingSchool.this,"Fill About School section", Toast.LENGTH_SHORT).show();
                }else if(schoolZip.getText().toString().isEmpty()){
                    Toast.makeText(AddingSchool.this,"Write zip code for school", Toast.LENGTH_SHORT).show();
                }else if(schoolAddress.getText().toString().isEmpty()){
                    Toast.makeText(AddingSchool.this,"Write school address", Toast.LENGTH_SHORT).show();
                }else if(radioGroupSkolType.getCheckedRadioButtonId()==-1){
                    Toast.makeText(getApplicationContext(), "Select type of students enrolled", Toast.LENGTH_SHORT).show();
                }else if(!(checkBoxPrimary.isChecked()||checkBoxMiddle.isChecked()||checkBoxHigher.isChecked())){
                    Toast.makeText(AddingSchool.this,"Select education level", Toast.LENGTH_SHORT).show();
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
                        EducationLevel.setPrimary(true);
                        //EducationLevel=EducationLevel.concat(" "+checkBoxPrimary.getText().toString());
                    }else{
                        EducationLevel.setPrimary(false);
                    }
                    if(checkBoxMiddle.isChecked()){
                        EducationLevel.setMiddle(true);
                        //EducationLevel=EducationLevel.concat(" "+checkBoxMiddle.getText().toString());
                    }else{
                        EducationLevel.setMiddle(false);
                    }
                    if(checkBoxHigher.isChecked()){
                        EducationLevel.setHigher(true);
                        //EducationLevel=EducationLevel.concat(" "+checkBoxHigher.getText().toString());
                    }else{
                        EducationLevel.setHigher(false);
                    }

                    onSaveInstanceState(saveThis);
                    Intent intent = new Intent(getApplicationContext(),AddingSchoolStep2.class );
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
            }
        });
    }
    private boolean hasPermission(String permission) {
        return ActivityCompat.checkSelfPermission(AddingSchool.this, permission) == PackageManager.PERMISSION_GRANTED;
    }
}