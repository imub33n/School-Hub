package com.example.schoolhub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class AddingSchoolStep3 extends AppCompatActivity {
    LinearLayout column2,column3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_school_step3);
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
}