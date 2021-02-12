package com.example.schoolhub;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.schoolhub.Adapters.PagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class SchoolDetailsDash extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_school_details_dash, container, false);

        //pages inside
        ViewPager vp = root.findViewById(R.id.tabViewPagerAdminDash);
        PagerAdapter pA= new PagerAdapter(getActivity().getSupportFragmentManager());
        vp.setAdapter(pA);
        TabLayout tL=root.findViewById(R.id.toolbarAdminDash);
        tL.setupWithViewPager(vp);
        return root;
    }
}