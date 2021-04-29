package com.example.schoolhub;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.example.schoolhub.data.PreferenceData;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import static android.content.ContentValues.TAG;

public class HomePanel extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    Button logout,reviewAndFeedback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_panel);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        logout = findViewById(R.id.logout);
        reviewAndFeedback= findViewById(R.id.reviewAndFeedback);
        reviewAndFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent( HomePanel.this , ReviewAndFeedback.class);
                startActivity(it);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceData.setUserLoggedInStatus(getApplicationContext(),false);
                PreferenceData.clearLoggedInEmailAddress(getApplicationContext());
                CometChat.logout(new CometChat.CallbackListener<String>() {
                    @Override
                    public void onSuccess(String successMessage) {
                        Log.d(TAG, "Logout completed successfully");
                    }
                    @Override
                    public void onError(CometChatException e) {
                        Log.d(TAG, "Logout failed with exception: " + e.getMessage());
                    }
                });
                finish();
                Intent it = new Intent( HomePanel.this , LandingScreen.class);
                startActivity(it);
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.nameNavHeader);
        navUsername.setText("Hi "+ PreferenceData.getLoggedInUserData(getApplicationContext()).get("username"));
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_search, R.id.nav_statistics)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_panel, menu);

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void openProfile(MenuItem item) {
        Intent it = new Intent( getApplicationContext() , UserProfile.class);
        startActivity(it);
    }
}