package com.example.schoolhub.ui.liveStream;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.schoolhub.HomePanel;
import com.example.schoolhub.R;

public class LiveStreamFragment extends Fragment {
    Button viewLivestream;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_live_stream, container, false);
        viewLivestream = root.findViewById(R.id.text_livestream);
        viewLivestream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent( getContext() , LiveStreamView.class);
                startActivity(it);
            }
        });

        return root;
    }
}
