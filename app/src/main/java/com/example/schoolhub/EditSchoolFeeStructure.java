package com.example.schoolhub;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.schoolhub.AddingSchool.AddingSchool;
import com.example.schoolhub.AddingSchool.AddingSchoolCompleted;
import com.example.schoolhub.AddingSchool.AddingSchoolStep3;
import com.example.schoolhub.data.EducationLevel;
import com.example.schoolhub.data.FeeStructure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import static android.content.ContentValues.TAG;

public class EditSchoolFeeStructure extends AppCompatActivity {
    EditText AddFee,AddFee2,AddFee3,TutionFee,TutionFee2,TutionFee3,ExamFee,ExamFee2,ExamFee3,
            sports,sports2,sports3,lab,lab2,lab3,library,library2,library3,totalAddFee,totalAddFee2,
            totalAddFee3,monthlyFee,monthlyFee2,monthlyFee3,others,others2,others3;
    TextView group1,group2,group3,statusFeeStructure;
    LinearLayout columnClasses,columnClasses2,columnClasses3;
    Toolbar toolbarEditFee;
    List<FeeStructure> feeStructures = new ArrayList<>();
    FeeStructure feeStructure= new FeeStructure();
    FeeStructure feeStructure2= new FeeStructure();
    FeeStructure feeStructure3= new FeeStructure();

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_school_fee_structure);

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
        group1=findViewById(R.id.group1);
        group2=findViewById(R.id.group2);
        group3=findViewById(R.id.group3);
        columnClasses= findViewById(R.id.columnClasses);
        columnClasses2=findViewById(R.id.columnClasses2);
        columnClasses3=findViewById(R.id.columnClasses3);
        statusFeeStructure=findViewById(R.id.statusFeeStructure);
        toolbarEditFee= findViewById(R.id.toolbarEditFee);
        toolbarEditFee.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                onBackPressed();
            }
        });
        //retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        String eLevel = "";
        if(AdminDashMainPage.yesSchoolData.getEducationLevel().getPrimary()
                && AdminDashMainPage.yesSchoolData.getEducationLevel().getMiddle()
                && AdminDashMainPage.yesSchoolData.getEducationLevel().getHigher()){
            eLevel="Primary/Middle/Higher";
        }else if(AdminDashMainPage.yesSchoolData.getEducationLevel().getPrimary()
                && AdminDashMainPage.yesSchoolData.getEducationLevel().getMiddle()
                && !AdminDashMainPage.yesSchoolData.getEducationLevel().getHigher()){
            eLevel="Primary/Middle";
        }else if(!AdminDashMainPage.yesSchoolData.getEducationLevel().getPrimary()
                && AdminDashMainPage.yesSchoolData.getEducationLevel().getMiddle()
                && AdminDashMainPage.yesSchoolData.getEducationLevel().getHigher()){
            eLevel="Middle/Higher";
        }else if(AdminDashMainPage.yesSchoolData.getEducationLevel().getPrimary()
                && !AdminDashMainPage.yesSchoolData.getEducationLevel().getMiddle()
                && AdminDashMainPage.yesSchoolData.getEducationLevel().getHigher()){
            eLevel="Primary/Higher";
        }else if(AdminDashMainPage.yesSchoolData.getEducationLevel().getPrimary()
                && !AdminDashMainPage.yesSchoolData.getEducationLevel().getMiddle()
                && !AdminDashMainPage.yesSchoolData.getEducationLevel().getHigher()){
            eLevel="Primary";
        }else if(!AdminDashMainPage.yesSchoolData.getEducationLevel().getPrimary()
                && AdminDashMainPage.yesSchoolData.getEducationLevel().getMiddle()
                && !AdminDashMainPage.yesSchoolData.getEducationLevel().getHigher()){
            eLevel="Middle";
        }else if(!AdminDashMainPage.yesSchoolData.getEducationLevel().getPrimary()
                && !AdminDashMainPage.yesSchoolData.getEducationLevel().getMiddle()
                && AdminDashMainPage.yesSchoolData.getEducationLevel().getHigher()){
            eLevel="Higher";
        }
        statusFeeStructure.setText("EducationLevel is set to "+eLevel);
        if(!AdminDashMainPage.yesSchoolData.getEducationLevel().getPrimary()){
            columnClasses.setLayoutParams(new LinearLayout.LayoutParams(0,0));
        }else{
            if(!AdminDashMainPage.yesSchoolData.getEducationLevel().getMiddle()){
                if(!AdminDashMainPage.yesSchoolData.getEducationLevel().getHigher()){
                    columnClasses.setLayoutParams(new LinearLayout.LayoutParams(300,0));
                }
            }
        }
        if(!AdminDashMainPage.yesSchoolData.getEducationLevel().getMiddle()){
            columnClasses2.setLayoutParams(new LinearLayout.LayoutParams(0,0));
        }else{
            if(!AdminDashMainPage.yesSchoolData.getEducationLevel().getPrimary()){
                if(!AdminDashMainPage.yesSchoolData.getEducationLevel().getHigher()){
                    columnClasses2.setLayoutParams(new LinearLayout.LayoutParams(300,0));
                }
            }
        }
        if(!AdminDashMainPage.yesSchoolData.getEducationLevel().getHigher()){
            columnClasses3.setLayoutParams(new LinearLayout.LayoutParams(0,0));
        }else{
            if(!AdminDashMainPage.yesSchoolData.getEducationLevel().getPrimary()){
                if(!AdminDashMainPage.yesSchoolData.getEducationLevel().getMiddle()){
                    columnClasses3.setLayoutParams(new LinearLayout.LayoutParams(300,0));
                }
            }
        }
        if(AdminDashMainPage.yesSchoolData.getFeeStructure().size()==1){
            group1();
        }else if(AdminDashMainPage.yesSchoolData.getFeeStructure().size()==2) {
            group1();
            group2();
        }else if(AdminDashMainPage.yesSchoolData.getFeeStructure().size()==3) {
            group1();
            group2();
            group3();
        }
    }
    private void group1(){
        group1.setText(String.valueOf(AdminDashMainPage.yesSchoolData.getFeeStructure().get(0).getGroup()));
        AddFee.setText(String.valueOf(AdminDashMainPage.yesSchoolData.getFeeStructure().get(0).getAdmissionFee()));
        ExamFee.setText(String.valueOf(AdminDashMainPage.yesSchoolData.getFeeStructure().get(0).getExamFee()));
        TutionFee.setText(String.valueOf(AdminDashMainPage.yesSchoolData.getFeeStructure().get(0).getTutionFee()));
        lab.setText(String.valueOf(AdminDashMainPage.yesSchoolData.getFeeStructure().get(0).getLabFee()));
        library.setText(String.valueOf(AdminDashMainPage.yesSchoolData.getFeeStructure().get(0).getLibraryFee()));
        monthlyFee.setText(String.valueOf(AdminDashMainPage.yesSchoolData.getFeeStructure().get(0).getMonthlyFee()));
        others.setText(String.valueOf(AdminDashMainPage.yesSchoolData.getFeeStructure().get(0).getOthersFee()));
        sports.setText(String.valueOf(AdminDashMainPage.yesSchoolData.getFeeStructure().get(0).getSportsFee()));
        totalAddFee.setText(String.valueOf(AdminDashMainPage.yesSchoolData.getFeeStructure().get(0).getTotalAdmissionFee()));
    }
    private void group2(){
        group2.setText(String.valueOf(AdminDashMainPage.yesSchoolData.getFeeStructure().get(1).getGroup()));
        AddFee2.setText(String.valueOf(AdminDashMainPage.yesSchoolData.getFeeStructure().get(1).getAdmissionFee()));
        ExamFee2.setText(String.valueOf(AdminDashMainPage.yesSchoolData.getFeeStructure().get(1).getExamFee()));
        TutionFee2.setText(String.valueOf(AdminDashMainPage.yesSchoolData.getFeeStructure().get(1).getTutionFee()));
        lab2.setText(String.valueOf(AdminDashMainPage.yesSchoolData.getFeeStructure().get(1).getLabFee()));
        library2.setText(String.valueOf(AdminDashMainPage.yesSchoolData.getFeeStructure().get(1).getLibraryFee()));
        monthlyFee2.setText(String.valueOf(AdminDashMainPage.yesSchoolData.getFeeStructure().get(1).getMonthlyFee()));
        others2.setText(String.valueOf(AdminDashMainPage.yesSchoolData.getFeeStructure().get(1).getOthersFee()));
        sports2.setText(String.valueOf(AdminDashMainPage.yesSchoolData.getFeeStructure().get(1).getSportsFee()));
        totalAddFee2.setText(String.valueOf(AdminDashMainPage.yesSchoolData.getFeeStructure().get(1).getTotalAdmissionFee()));
    }
    private void group3(){
        group3.setText(String.valueOf(AdminDashMainPage.yesSchoolData.getFeeStructure().get(2).getGroup()));
        AddFee3.setText(String.valueOf(AdminDashMainPage.yesSchoolData.getFeeStructure().get(2).getAdmissionFee()));
        ExamFee3.setText(String.valueOf(AdminDashMainPage.yesSchoolData.getFeeStructure().get(2).getExamFee()));
        TutionFee3.setText(String.valueOf(AdminDashMainPage.yesSchoolData.getFeeStructure().get(2).getTutionFee()));
        lab3.setText(String.valueOf(AdminDashMainPage.yesSchoolData.getFeeStructure().get(2).getLabFee()));
        library3.setText(String.valueOf(AdminDashMainPage.yesSchoolData.getFeeStructure().get(2).getLibraryFee()));
        monthlyFee3.setText(String.valueOf(AdminDashMainPage.yesSchoolData.getFeeStructure().get(2).getMonthlyFee()));
        others3.setText(String.valueOf(AdminDashMainPage.yesSchoolData.getFeeStructure().get(2).getOthersFee()));
        sports3.setText(String.valueOf(AdminDashMainPage.yesSchoolData.getFeeStructure().get(2).getSportsFee()));
        totalAddFee3.setText(String.valueOf(AdminDashMainPage.yesSchoolData.getFeeStructure().get(2).getTotalAdmissionFee()));
    }

    public void backEditEee(View view) {
        onBackPressed();
    }

    public void updateFeeStructure(View view) {

        if(AdminDashMainPage.yesSchoolData.getEducationLevel().getPrimary()){
            feeStructure.setGroup("primary");
            feeStructure.setAdmissionFee(Integer.parseInt(AddFee.getText().toString()));
            feeStructure.setExamFee(Integer.parseInt(ExamFee.getText().toString()));
            feeStructure.setLabFee(Integer.parseInt(lab.getText().toString()));
            feeStructure.setMonthlyFee(Integer.parseInt(monthlyFee.getText().toString()));
            feeStructure.setLibraryFee(Integer.parseInt(library.getText().toString()));
            feeStructure.setOthersFee(Integer.parseInt(others.getText().toString()));
            feeStructure.setSportsFee(Integer.parseInt(sports.getText().toString()));
            feeStructure.setTotalAdmissionFee(Integer.parseInt(totalAddFee.getText().toString()));
            feeStructure.setTutionFee(Integer.parseInt(TutionFee.getText().toString()));
            feeStructures.add(feeStructure);
        }
        if(AdminDashMainPage.yesSchoolData.getEducationLevel().getMiddle()){
            feeStructure2.setGroup("middle");
            feeStructure2.setAdmissionFee(Integer.parseInt(AddFee2.getText().toString()));
            feeStructure2.setExamFee(Integer.parseInt(ExamFee2.getText().toString()));
            feeStructure2.setLabFee(Integer.parseInt(lab2.getText().toString()));
            feeStructure2.setMonthlyFee(Integer.parseInt(monthlyFee2.getText().toString()));
            feeStructure2.setLibraryFee(Integer.parseInt(library2.getText().toString()));
            feeStructure2.setOthersFee(Integer.parseInt(others2.getText().toString()));
            feeStructure2.setSportsFee(Integer.parseInt(sports2.getText().toString()));
            feeStructure2.setTotalAdmissionFee(Integer.parseInt(totalAddFee2.getText().toString()));
            feeStructure2.setTutionFee(Integer.parseInt(TutionFee2.getText().toString()));
            feeStructures.add(feeStructure2);
        }
        if(AdminDashMainPage.yesSchoolData.getEducationLevel().getHigher()) {
            feeStructure3.setGroup("primary");
            feeStructure3.setAdmissionFee(Integer.parseInt(AddFee3.getText().toString()));
            feeStructure3.setExamFee(Integer.parseInt(ExamFee3.getText().toString()));
            feeStructure3.setLabFee(Integer.parseInt(lab3.getText().toString()));
            feeStructure3.setMonthlyFee(Integer.parseInt(monthlyFee3.getText().toString()));
            feeStructure3.setLibraryFee(Integer.parseInt(library3.getText().toString()));
            feeStructure3.setOthersFee(Integer.parseInt(others3.getText().toString()));
            feeStructure3.setSportsFee(Integer.parseInt(sports3.getText().toString()));
            feeStructure3.setTotalAdmissionFee(Integer.parseInt(totalAddFee3.getText().toString()));
            feeStructure3.setTutionFee(Integer.parseInt(TutionFee3.getText().toString()));
            feeStructures.add(feeStructure3);
        }
        Log.d(TAG, "updateFeeStructure:__s___ ");
        new AlertDialog.Builder(EditSchoolFeeStructure.this)
                //.setTitle("Updating school information!")
                .setMessage("Are you sure you want to update ?")
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Call<Void> call = retrofitInterface.editFeeStructure(AdminDashMainPage.yesSchoolData.get_id(),feeStructures);
                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if(response.isSuccessful()){
                                    Toast.makeText(EditSchoolFeeStructure.this,"Fee Structure Updated", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(EditSchoolFeeStructure.this,AdminDashboard.class );
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(EditSchoolFeeStructure.this,"Update Err: "+response.code(), Toast.LENGTH_LONG).show();
                                }
                            }
                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(EditSchoolFeeStructure.this,"Error: "+t, Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}