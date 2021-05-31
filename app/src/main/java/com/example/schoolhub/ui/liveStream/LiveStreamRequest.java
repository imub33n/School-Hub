package com.example.schoolhub.ui.liveStream;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.schoolhub.AdminDashboard;
import com.example.schoolhub.MainActivity;
import com.example.schoolhub.R;
import com.example.schoolhub.RetrofitInterface;
import com.example.schoolhub.ReviewAndFeedback;
import com.example.schoolhub.SendNotification;
import com.example.schoolhub.SignIn;
import com.example.schoolhub.SignUp;
import com.example.schoolhub.data.PreferenceData;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import static android.content.ContentValues.TAG;

public class LiveStreamRequest extends AppCompatActivity {
    public Toolbar toolbarEdit;
    Button btnDatePicker,btnTimePicker_end, btnTimePicker,sendRequestButton;
    EditText txtDate, txtTime, txtTime_end,titleStream,descriptionStream;
//    RadioGroup groupPrivacy;
//    RadioButton buttonPrivacy;
    private int mYear, mMonth, mDay, mHour, mMinute, mHour_end, mMinute_end;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    SimpleDateFormat formatter = new SimpleDateFormat("d/M/yyyy");
    Date date = new Date();
    Calendar cStartTime= Calendar.getInstance();

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_stream_request);
        toolbarEdit = (Toolbar) findViewById(R.id.yesnav);
        btnDatePicker=(Button)findViewById(R.id.btn_date);
        btnTimePicker=(Button)findViewById(R.id.btn_time);
        btnTimePicker_end=(Button)findViewById(R.id.btn_time_end);
        txtDate=(EditText)findViewById(R.id.in_date);
        txtTime=(EditText)findViewById(R.id.in_time);
        txtTime_end=(EditText)findViewById(R.id.in_time_end);
        titleStream=(EditText)findViewById(R.id.titleStream);
        descriptionStream=(EditText)findViewById(R.id.descriptionStream);
        sendRequestButton=(Button)findViewById(R.id.sendRequestButton);
        retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
        sendRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //groupPrivacy=findViewById(R.id.groupPrivacy);
                //buttonPrivacy=findViewById(groupPrivacy.getCheckedRadioButtonId());
                LocalDateTime now = LocalDateTime.now();
                String currentTime=dtf.format(now);
                if(titleStream.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Write a title for Livestream", Toast.LENGTH_LONG).show();
                }else if(descriptionStream.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please provide brief description about Livestream", Toast.LENGTH_LONG).show();
                }else if(txtDate.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please select a date for Livestream", Toast.LENGTH_LONG).show();
                }else if(txtTime.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please select a stating time for Livestream", Toast.LENGTH_LONG).show();
                }else if(txtTime_end.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please select a ending time for Livestream", Toast.LENGTH_LONG).show();
                }
//                else if(buttonPrivacy==null){
//                    Toast.makeText(getApplicationContext(), "Please select privacy type", Toast.LENGTH_LONG).show();
//                }
                else{
                    HashMap<String, String> map = new HashMap<>();

                    map.put("schoolID", PreferenceData.getLoggedInUserData(getApplicationContext()).get("userID"));
                    map.put("schoolName",LiveStreamAdminFragment.yesSchoolData.getSchoolName());
                    map.put("date",txtDate.getText().toString());
                    map.put("startTime",txtTime.getText().toString());
                    map.put("endTime",txtTime_end.getText().toString());
                    map.put("currentTime",currentTime);
                    map.put("title",titleStream.getText().toString());
                    map.put("description",descriptionStream.getText().toString());
                    map.put("privacy","Public");

                    Call<Void> call = retrofitInterface.postStreamRequest(map);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.code() == 200) {
                                //notiStart
                                String title="Request for a Live Stream";
                                String subTitle = LiveStreamAdminFragment.yesSchoolData.getSchoolName()+" requested for a stream";
                                new SendNotification(title,subTitle, PreferenceData.getLoggedInUserData(LiveStreamRequest.this).get("userID"),MainActivity.SuperAdminID);
                                //notiEnd
                                Toast.makeText(LiveStreamRequest.this, "Request Sent to Admin!", Toast.LENGTH_LONG).show();
                                finish();
//                                onBackPressed();
                            }else{
                                Toast.makeText(LiveStreamRequest.this, "Error Code: "+response.code(), Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "Connection Error: "+t.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
        toolbarEdit.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                onBackPressed();
            }
        });
        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(LiveStreamRequest.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                txtDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

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
                if(txtDate.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please select date first", Toast.LENGTH_LONG).show();
                }else{
                    Calendar datetime = Calendar.getInstance();
                    Calendar c = Calendar.getInstance();
                    mHour = c.get(Calendar.HOUR_OF_DAY);
                    mMinute = c.get(Calendar.MINUTE);

                    // Launch Time Picker Dialog
                    TimePickerDialog timePickerDialog = new TimePickerDialog(LiveStreamRequest.this,
                            new TimePickerDialog.OnTimeSetListener() {

                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay,
                                                      int minute) {
                                    datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                    datetime.set(Calendar.MINUTE, minute);
                                    if(formatter.format(date).equals(txtDate.getText().toString())){
                                        if (datetime.getTimeInMillis() >= c.getTimeInMillis()) {
                                            //it's after current
                                            cStartTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                            cStartTime.set(Calendar.MINUTE, minute);
                                            String a=String.valueOf(hourOfDay);
                                            String b=String.valueOf(minute);
                                            if(a.length()==1){
                                                a="0"+hourOfDay;
                                            }
                                            if(b.length()==1){
                                                b="0"+minute;
                                            }
                                            txtTime.setText(a + ":" + b);
//                                        txtTime.setText(String.format("%02d:%02d %s", hour == 0 ? 12 : hour, minute, hourOfDay < 12 ? "AM" : "PM"));
                                        } else {
                                            //it's before current'
                                            Toast.makeText(getApplicationContext(), "Invalid Time", Toast.LENGTH_LONG).show();
                                        }
                                    }else if(!formatter.format(date).equals(txtDate.getText().toString())){
                                        //it's after current
                                        cStartTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                        cStartTime.set(Calendar.MINUTE, minute);
                                        String a=String.valueOf(hourOfDay);
                                        String b=String.valueOf(minute);
                                        if(a.length()==1){
                                            a="0"+hourOfDay;
                                        }
                                        if(b.length()==1){
                                            b="0"+minute;
                                        }
                                        txtTime.setText(a + ":" + b);
                                    }
                                }
                            }, mHour, mMinute, false);
                    timePickerDialog.show();
                }
            }
        });
        btnTimePicker_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtDate.getText().toString().isEmpty() ||txtTime.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please select date and start time first", Toast.LENGTH_LONG).show();
                }else{
                    // Get Current Time
                    Calendar datetime = Calendar.getInstance();
                    final Calendar c = Calendar.getInstance();
                    mHour_end = c.get(Calendar.HOUR_OF_DAY);
                    mMinute_end = c.get(Calendar.MINUTE);

                    // Launch Time Picker Dialog
                    TimePickerDialog timePickerDialog = new TimePickerDialog(LiveStreamRequest.this,
                            new TimePickerDialog.OnTimeSetListener() {

                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay,
                                                      int minute) {
                                    datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                    datetime.set(Calendar.MINUTE, minute);
                                    if(hourOfDay < cStartTime.get(Calendar.HOUR_OF_DAY)){
                                        Toast.makeText(getApplicationContext(), "Ending time can not be before starting time.", Toast.LENGTH_LONG).show();
                                    }else if(cStartTime.get(Calendar.HOUR_OF_DAY)==hourOfDay){
                                        if(minute<cStartTime.get(Calendar.MINUTE)){
                                            Toast.makeText(getApplicationContext(), "Ending time can not be before starting time.", Toast.LENGTH_LONG).show();
                                        }else{
                                            String a=String.valueOf(hourOfDay);
                                            String b=String.valueOf(minute);
                                            if(a.length()==1){
                                                a="0"+hourOfDay;
                                            }
                                            if(b.length()==1){
                                                b="0"+minute;
                                            }
                                            txtTime_end.setText( a + ":" + b);
                                        }
                                    }else{
                                        String a=String.valueOf(hourOfDay);
                                        String b=String.valueOf(minute);
                                        if(a.length()==1){
                                            a="0"+hourOfDay;
                                        }
                                        if(b.length()==1){
                                            b="0"+minute;
                                        }
                                        txtTime_end.setText( a + ":" + b);
                                    }
//                                    Log.d(TAG, "onTimeSet:_________________ "+cStartTime.get(Calendar.HOUR_OF_DAY)+"_______"+cStartTime.getTimeInMillis()+"_______"+cStartTime.getTime());
//                                    if (datetime.getTimeInMillis() >= cStartTime.getTimeInMillis()) {
//                                        //it's after current
////                                        int hour = hourOfDay % 12;
//
////                                        txtTime_end.setText(String.format("%02d:%02d %s", hour == 0 ? 12 : hour, minute, hourOfDay < 12 ? "AM" : "PM"));
//                                    } else {
//                                        //it's before current'
//                                        Toast.makeText(getApplicationContext(), "Ending time can not be before starting time.", Toast.LENGTH_LONG).show();
//                                    }
                                }
                            }, mHour_end, mMinute_end, false);
                    timePickerDialog.show();
                }

            }
        });
    }
}