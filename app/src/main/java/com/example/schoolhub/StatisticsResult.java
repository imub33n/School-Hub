package com.example.schoolhub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.example.schoolhub.Adapters.GraphAdapter;
import com.google.android.material.tabs.TabLayout;

public class StatisticsResult extends AppCompatActivity {
    public Toolbar schoolComparisonNav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics_result);


        schoolComparisonNav=findViewById(R.id.schoolComparisonNav);
        schoolComparisonNav.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                onBackPressed();
            }
        });

    }
}