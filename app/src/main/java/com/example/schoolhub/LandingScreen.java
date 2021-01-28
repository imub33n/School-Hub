package com.example.schoolhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class LandingScreen extends AppCompatActivity {

    ViewPager2 viewPager;
    public int[] lst_images = {R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.a, R.drawable.b, R.drawable.c};
    SlideAdapter adapter;
    Handler sliderHandler= new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_screen);
        //sliderAdapter
        viewPager = (ViewPager2) findViewById(R.id.viewPagerSliderLandingPage);
        adapter= new SlideAdapter(lst_images);
        viewPager.setAdapter(adapter);

        viewPager.setClipToPadding(false);
        viewPager.setClipChildren(false);
        viewPager.setOffscreenPageLimit(3);
        viewPager.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(20));//margin next picture is 20
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float a = 1-Math.abs(position);
                page.setScaleY(0.85f+ a * 0.15f);
            }
        });
        viewPager.setPageTransformer(compositePageTransformer);
        //auto scroll
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable,3000);//oh yas 3 sec delay
                if(viewPager.getCurrentItem()==3){
                    viewPager.setCurrentItem(1);
                }
            }
        });

    }

    public void openSearchFragment(View view) {
        View v = getLayoutInflater().inflate(R.layout.fragment_filters, null);
        //full screen bottom sheet
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        LinearLayout linearLayout = v.findViewById(R.id.rootfrag);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
        params.height = getScreenHeight();
        linearLayout.setLayoutParams(params);
        dialog.setContentView(v);
        dialog.show();
        BottomSheetBehavior mBehavior;
        mBehavior = BottomSheetBehavior.from((View) v.getParent());
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
//            if(viewPager.getCurrentItem()==3){
//
//            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable,3000);
    }

    public void openSignIn(View view) {
        Intent it = new Intent( getApplicationContext() , SignIn.class);
        startActivity(it);
    }
}