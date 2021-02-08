package com.example.schoolhub;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.schoolhub.data.PostResult;
import com.example.schoolhub.data.SchoolData;
import com.google.android.material.tabs.TabLayout;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;

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