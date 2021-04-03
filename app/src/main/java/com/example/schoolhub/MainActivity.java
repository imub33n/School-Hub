package com.example.schoolhub;

import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.format.Formatter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.schoolhub.ui.home.HomeFragment;

public class MainActivity extends AppCompatActivity {
    public static String BASE_URL = "http://192.168.10.9:8080/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
//        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
//        BASE_URL = "http://"+ip+":8080/";
//        Toast.makeText(this,ip,Toast.LENGTH_SHORT).show();
        Intent it = new Intent( getApplicationContext() , LandingScreen.class);
        startActivity(it);
//admin@admin.com
//admin

//        final Handler handler = new Handler(Looper.getMainLooper());
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent intent= new Intent(getApplicationContext(), LandingScreen.class);
//                startActivity(intent);
//            }
//        }, 2000);//timer set for 2 seconds
   }
}