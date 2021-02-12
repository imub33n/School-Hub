package com.example.schoolhub;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EditSchoolFeeStructure extends AppCompatActivity {
    EditText To,From,To2,From2,To3,From3,
            AddFee,AddFee2,AddFee3,TutionFee,TutionFee2,TutionFee3,ExamFee,ExamFee2,ExamFee3,
            sports,sports2,sports3,lab,lab2,lab3,library,library2,library3,totalAddFee,totalAddFee2,
            totalAddFee3,monthlyFee,monthlyFee2,monthlyFee3,others,others2,others3;
    public static String To_From,To_From2,To_From3;
    public static int iAddFee,iAddFee2,iAddFee3,iTutionFee,iTutionFee2,iTutionFee3,iExamFee,iExamFee2,iExamFee3,
            isports,isports2,isports3,ilab,ilab2,ilab3,ilibrary,ilibrary2,ilibrary3,itotalAddFee,itotalAddFee2,
            itotalAddFee3,imonthlyFee,imonthlyFee2,imonthlyFee3,iothers,iothers2,iothers3;
    public static int progress=0;
    public static boolean step1=false,step2=false,step3=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_school_fee_structure);

        To= findViewById(R.id.To);
        From=findViewById(R.id.From);
        To2= findViewById(R.id.To2);
        From2=findViewById(R.id.From2);
        To3= findViewById(R.id.To3);
        From3=findViewById(R.id.From3);
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
        List<String> splitStr =  Arrays.stream(AdminDashMainPage.yesSchoolData.getFeeStructure().get(0).getGroup().split("-"))
                .map(String::trim)
                .collect(Collectors.toList());
        From.setText(String.valueOf(splitStr.get(0)));
        To.setText(String.valueOf(splitStr.get(1)));
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
        List<String> splitStr =  Arrays.stream(AdminDashMainPage.yesSchoolData.getFeeStructure().get(1).getGroup().split("-"))
                .map(String::trim)
                .collect(Collectors.toList());
        From2.setText(String.valueOf(splitStr.get(0)));
        To2.setText(String.valueOf(splitStr.get(1)));
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
        List<String> splitStr =  Arrays.stream(AdminDashMainPage.yesSchoolData.getFeeStructure().get(2).getGroup().split("-"))
                .map(String::trim)
                .collect(Collectors.toList());
        From3.setText(String.valueOf(splitStr.get(0)));
        To3.setText(String.valueOf(splitStr.get(1)));
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
}