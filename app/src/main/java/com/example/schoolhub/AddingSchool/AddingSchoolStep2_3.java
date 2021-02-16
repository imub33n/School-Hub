package com.example.schoolhub.AddingSchool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.schoolhub.R;
import com.kofigyan.stateprogressbar.StateProgressBar;

public class AddingSchoolStep2_3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_school_step2_3);
        StateProgressBar stateProgressBar = (StateProgressBar) findViewById(R.id.your_state_progress_bar_id);
        stateProgressBar.setStateDescriptionData(AddingSchool.descriptionData);
    }

    public void uploadVideo(View view) {
//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_PICK);
//        intent.setType("video/*");
//
//        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
//        if (list.size() <= 0) {
//            //Log.d(TAG, "no video picker intent on this hardware");
//            Toast.makeText(this, "No video picker found on device", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        startActivityForResult(intent, GALLERY_RETURN);

        Intent intent2 = new Intent();
        intent2.setType("video/*");
        //intent.setType("*/*");
        intent2.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent2, "Select Video"), 1);

    }

    public void nextStep2_3(View view) {
        Intent intent = new Intent(getApplicationContext(),AddingSchoolStep3.class );
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    public void backStep2_3(View view) {
        Intent intent = new Intent(getApplicationContext(),AddingSchoolStep2.class );
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }
}