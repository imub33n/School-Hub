package com.example.schoolhub;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class LiveStreamAdminFragment extends Fragment {
    Button btnDatePicker,btnTimePicker_end, btnTimePicker;
    EditText txtDate, txtTime, txtTime_end;
    private int mYear, mMonth, mDay, mHour, mMinute, mHour_end, mMinute_end;
    String mint,hour,mint_end,hour_end;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.fragment_live_stream_admin, container, false);
        btnDatePicker=(Button)root.findViewById(R.id.btn_date);
        btnTimePicker=(Button)root.findViewById(R.id.btn_time);
        btnTimePicker_end=(Button)root.findViewById(R.id.btn_time_end);
        txtDate=(EditText)root.findViewById(R.id.in_date);
        txtTime=(EditText)root.findViewById(R.id.in_time);
        txtTime_end=(EditText)root.findViewById(R.id.in_time_end);

        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });
        btnTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Time
                Calendar datetime = Calendar.getInstance();
                Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                datetime.set(Calendar.MINUTE, minute);
                                if (datetime.getTimeInMillis() >= c.getTimeInMillis()) {
                                    //it's after current
                                    int hour = hourOfDay % 12;
                                    txtTime.setText(String.format("%02d:%02d %s", hour == 0 ? 12 : hour, minute, hourOfDay < 12 ? "AM" : "PM"));
                                } else {
                                    //it's before current'
                                    Toast.makeText(getContext(), "Invalid Time", Toast.LENGTH_LONG).show();
                                }
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });
        btnTimePicker_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Time
                Calendar datetime = Calendar.getInstance();
                final Calendar c = Calendar.getInstance();
                mHour_end = c.get(Calendar.HOUR_OF_DAY);
                mMinute_end = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                datetime.set(Calendar.MINUTE, minute);
                                if (datetime.getTimeInMillis() >= c.getTimeInMillis()) {
                                    //it's after current
                                    int hour = hourOfDay % 12;
                                    txtTime_end.setText(String.format("%02d:%02d %s", hour == 0 ? 12 : hour, minute, hourOfDay < 12 ? "AM" : "PM"));
                                } else {
                                    //it's before current'
                                    Toast.makeText(getContext(), "Invalid Time", Toast.LENGTH_LONG).show();
                                }
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });
        return root;
    }
}