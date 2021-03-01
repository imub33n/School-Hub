package com.example.schoolhub;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static android.content.ContentValues.TAG;

public class SearchResultFragment extends Fragment implements LocationListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_search_result, container, false);

        return root;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
//        Location l1=new Location("One");
//        l1.setLatitude(location.getLatitude());
//        l1.setLongitude(location.getLongitude());
//
//        Location l2=new Location("Two");
//        l2.setLatitude(Double.parseDouble("33.6381982"));
//        l2.setLongitude(Double.parseDouble("73.1467504"));
//
//        float distance_bw_one_and_two=l1.distanceTo(l2);
//        Log.d(TAG, "onLocationChanged:__________________________________ "+distance_bw_one_and_two);
    }
}
