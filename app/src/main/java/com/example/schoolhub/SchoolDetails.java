package com.example.schoolhub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.example.schoolhub.Adapters.PagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class SchoolDetails extends AppCompatActivity {

    public Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_details);

        toolbar = (Toolbar) findViewById(R.id.toolbar2);

        ViewPager vp = findViewById(R.id.tabViewPager);
        PagerAdapter pA= new PagerAdapter(getSupportFragmentManager());
        vp.setAdapter(pA);
        TabLayout tL=findViewById(R.id.toolbar3);
        tL.setupWithViewPager(vp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                onBackPressed();
            }
        });
    }

}