package com.example.schoolhub.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.schoolhub.FaculityFragment;
import com.example.schoolhub.FeeStructureFragment;
import com.example.schoolhub.InformationSchoolFragment;
import com.example.schoolhub.ReviewsFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {
    private String tabTitles[]= new String[]{
            "Info",
            "Fee Structure",
            "Faculty",
            "Ratings & Reviews"
    };
    public PagerAdapter(@NonNull FragmentManager fm) {
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
                return new InformationSchoolFragment();
            case 1:
                return new FeeStructureFragment();
            case 2:
                return new FaculityFragment();
            case 3:
                return new ReviewsFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
