package com.example.schoolhub.ui.liveStream;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.schoolhub.R;

public class LiveStreamFragment extends Fragment {
    private LiveStreamViewModel LiveStreamViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        LiveStreamViewModel =
                ViewModelProviders.of(this).get(LiveStreamViewModel.class);
        View root = inflater.inflate(R.layout.fragment_live_stream, container, false);
        final TextView textView = root.findViewById(R.id.text_livestream);
        LiveStreamViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
