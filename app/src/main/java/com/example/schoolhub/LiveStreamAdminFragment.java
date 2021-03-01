package com.example.schoolhub;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.schoolhub.AddingSchool.AddingSchoolStep2;

import java.util.Calendar;

public class LiveStreamAdminFragment extends Fragment {
    Button liveRequestButton;
    TextView startLiveStream;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.fragment_live_stream_admin, container, false);
        liveRequestButton= root.findViewById(R.id.liveRequestButton);
        startLiveStream= root.findViewById(R.id.startLiveStream);
        startLiveStream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        liveRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LiveStreamRequest.class );
//                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
        return root;
    }
}