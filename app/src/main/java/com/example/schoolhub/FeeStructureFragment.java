package com.example.schoolhub;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.schoolhub.data.FeeStructure;
import com.example.schoolhub.data.SchoolData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FeeStructureFragment extends Fragment {
    TextView tAddFee,tAddFee2,tAddFee3,tTutionFee,tTutionFee2,tTutionFee3,
            tExamFee,tExamFee2,tExamFee3,tSport,tSport2,tSport3,tLab,tLab2,tLab3,
            tLibrary,tLibrary2,tLibrary3,tOther,tOther2,tOther3,tMonthly,tMonthly2,tMonthly3,
            tTotalAddFee,tTotalAddFee2,tTotalAddFee3,group1,group2,group3,description;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    List<SchoolData> schoolData;
    List<FeeStructure> feeStructure = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_fee_structure, container, false);
        tAddFee=root.findViewById(R.id.tAddFee);
        tAddFee2=root.findViewById(R.id.tAddFee2);
        tAddFee3=root.findViewById(R.id.tAddFee3);
        tTutionFee=root.findViewById(R.id.tTutionFee);
        tTutionFee2=root.findViewById(R.id.tTutionFee2);
        tTutionFee3=root.findViewById(R.id.tTutionFee3);
        tExamFee=root.findViewById(R.id.tExamFee);
        tExamFee2=root.findViewById(R.id.tExamFee2);
        tExamFee3=root.findViewById(R.id.tExamFee3);
        tSport=root.findViewById(R.id.tSport);
        tSport2=root.findViewById(R.id.tSport2);
        tSport3=root.findViewById(R.id.tSport3);
        tLab=root.findViewById(R.id.tLab);
        tLab2=root.findViewById(R.id.tLab2);
        tLab3=root.findViewById(R.id.tLab3);
        tLibrary=root.findViewById(R.id.tLibrary);
        tLibrary2=root.findViewById(R.id.tLibrary2);
        tLibrary3=root.findViewById(R.id.tLibrary3);
        tOther=root.findViewById(R.id.tOther);
        tOther2=root.findViewById(R.id.tOther2);
        tOther3=root.findViewById(R.id.tOther3);
        tMonthly=root.findViewById(R.id.tMonthly);
        tMonthly2=root.findViewById(R.id.tMonthly2);
        tMonthly3=root.findViewById(R.id.tMonthly3);
        tTotalAddFee=root.findViewById(R.id.tTotalAddFee);
        tTotalAddFee2=root.findViewById(R.id.tTotalAddFee2);
        tTotalAddFee3=root.findViewById(R.id.tTotalAddFee3);
        group1=root.findViewById(R.id.group1);
        group2=root.findViewById(R.id.group2);
        group3=root.findViewById(R.id.group3);
        description=root.findViewById(R.id.descriptionTitle);
        //retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(SignIn.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);
        Call<List<SchoolData>> call = retrofitInterface.getSchoolData();
        call.enqueue(new Callback<List<SchoolData>>() {
            @Override
            public void onResponse(Call<List<SchoolData>> call, Response<List<SchoolData>> response) {
                if (response.code() == 200) {
                    Log.d("TAG",response.code()+"");
                    schoolData =  response.body();
                    //Toast.makeText(getContext(),schoolData.toString(),Toast.LENGTH_SHORT).show();

                    for(int i=0;i<schoolData.size();i++){
                        String adminIdGet=schoolData.get(i).getAdminID();
                        if(Objects.equals(adminIdGet, InformationSchoolFragment.adminId)){
                            feeStructure=schoolData.get(i).getFeeStructure();
                            //putting data
                            if(feeStructure.size()==1){
                                group1();
                                group1.setLayoutParams(
                                        new LinearLayout.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                0f));
                                description.setLayoutParams(
                                        new LinearLayout.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                1f));
                                tAddFee2.setLayoutParams(
                                        new LinearLayout.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                3f));
                                tExamFee2.setLayoutParams(
                                        new LinearLayout.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                3f));
                                tLab2.setLayoutParams(
                                        new LinearLayout.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                3f));
                                tLibrary2.setLayoutParams(
                                        new LinearLayout.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                3f));
                                tMonthly2.setLayoutParams(
                                        new LinearLayout.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                3f));
                                tOther2.setLayoutParams(
                                        new LinearLayout.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                3f));
                                tSport2.setLayoutParams(
                                        new LinearLayout.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                3f));
                                tTotalAddFee2.setLayoutParams(
                                        new LinearLayout.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                3f));
                                tTutionFee2.setLayoutParams(
                                        new LinearLayout.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                3f));
                            }else if(feeStructure.size()==2){
                                group1();
                                group2();

                                group3.setLayoutParams(
                                        new LinearLayout.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                2f));
                                description.setLayoutParams(
                                        new LinearLayout.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                2f));
                                tAddFee3.setLayoutParams(
                                        new LinearLayout.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                1.5f));
                                tExamFee3.setLayoutParams(
                                        new LinearLayout.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                1.5f));
                                tLab3.setLayoutParams(
                                        new LinearLayout.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                1.5f));
                                tLibrary3.setLayoutParams(
                                        new LinearLayout.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                1.5f));
                                tMonthly3.setLayoutParams(
                                        new LinearLayout.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                1.5f));
                                tOther3.setLayoutParams(
                                        new LinearLayout.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                1.5f));
                                tSport3.setLayoutParams(
                                        new LinearLayout.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                1.5f));
                                tTotalAddFee3.setLayoutParams(
                                        new LinearLayout.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                1.5f));
                                tTutionFee3.setLayoutParams(
                                        new LinearLayout.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                1.5f));
                            }else if(feeStructure.size()==3){
                                group1();
                                group2();
                                group3();
                            }
                        }
                    }
                }else { Toast.makeText(getContext(), "CODE: "+response.code(), Toast.LENGTH_LONG).show(); }
            }
            @Override
            public void onFailure(Call<List<SchoolData>> call, Throwable t) {
                Toast.makeText(getContext(), ""+t, Toast.LENGTH_LONG).show(); }
        });
        return root;
    }
    private void group1(){
        group1.setText(feeStructure.get(0).getGroup());
        tAddFee.setText(String.valueOf(feeStructure.get(0).getAdmissionFee()));
        tExamFee.setText(String.valueOf(feeStructure.get(0).getExamFee()));
        tTutionFee.setText(String.valueOf(feeStructure.get(0).getTutionFee()));
        tLab.setText(String.valueOf(feeStructure.get(0).getLabFee()));
        tLibrary.setText(String.valueOf(feeStructure.get(0).getLibraryFee()));
        tMonthly.setText(String.valueOf(feeStructure.get(0).getMonthlyFee()));
        tOther.setText(String.valueOf(feeStructure.get(0).getOthersFee()));
        tSport.setText(String.valueOf(feeStructure.get(0).getSportsFee()));
        tTotalAddFee.setText(String.valueOf(feeStructure.get(0).getTotalAdmissionFee()));
    }
    private void group2(){
        group2.setText(String.valueOf(feeStructure.get(1).getGroup()));
        tAddFee2.setText(String.valueOf(feeStructure.get(1).getAdmissionFee()));
        tExamFee2.setText(String.valueOf(feeStructure.get(1).getExamFee()));
        tTutionFee2.setText(String.valueOf(feeStructure.get(1).getTutionFee()));
        tLab2.setText(String.valueOf(feeStructure.get(1).getLabFee()));
        tLibrary2.setText(String.valueOf(feeStructure.get(1).getLibraryFee()));
        tMonthly2.setText(String.valueOf(feeStructure.get(1).getMonthlyFee()));
        tOther2.setText(String.valueOf(feeStructure.get(1).getOthersFee()));
        tSport2.setText(String.valueOf(feeStructure.get(1).getSportsFee()));
        tTotalAddFee2.setText(String.valueOf(feeStructure.get(1).getTotalAdmissionFee()));
    }
    private void group3(){
        group3.setText(String.valueOf(feeStructure.get(2).getGroup()));
        tAddFee3.setText(String.valueOf(feeStructure.get(2).getAdmissionFee()));
        tExamFee3.setText(String.valueOf(feeStructure.get(2).getExamFee()));
        tTutionFee3.setText(String.valueOf(feeStructure.get(2).getTutionFee()));
        tLab3.setText(String.valueOf(feeStructure.get(2).getLabFee()));
        tLibrary3.setText(String.valueOf(feeStructure.get(2).getLibraryFee()));
        tMonthly3.setText(String.valueOf(feeStructure.get(2).getMonthlyFee()));
        tOther3.setText(String.valueOf(feeStructure.get(2).getOthersFee()));
        tSport3.setText(String.valueOf(feeStructure.get(2).getSportsFee()));
        tTotalAddFee3.setText(String.valueOf(feeStructure.get(2).getTotalAdmissionFee()));
    }
}