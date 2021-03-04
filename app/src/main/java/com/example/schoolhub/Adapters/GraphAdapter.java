package com.example.schoolhub.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.schoolhub.Graphs.BarGraphFragment;
import com.example.schoolhub.Graphs.PieChartFragment;
import com.example.schoolhub.LineGraphFragment;

public class GraphAdapter extends FragmentStatePagerAdapter{

    private String tabTitles[]= new String[]{
            "Pie Chart",
            "Bar Graph",
            "Line Graph"
    };
    public GraphAdapter(@NonNull FragmentManager fm) {
        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new PieChartFragment();
            case 1:
                return new BarGraphFragment();
            case 2:
                return new LineGraphFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
