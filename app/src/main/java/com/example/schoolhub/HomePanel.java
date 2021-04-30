package com.example.schoolhub;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.example.schoolhub.data.LoginResult;
import com.example.schoolhub.data.PreferenceData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;

public class HomePanel extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    Button logout,reviewAndFeedback;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    FirebaseStorage storage= FirebaseStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_panel);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        logout = findViewById(R.id.logout);
        reviewAndFeedback= findViewById(R.id.reviewAndFeedback);
        //retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);
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
        MenuItem item = menu.findItem(R.id.action_profile);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        if (item != null) {
            //getting userData
            Call<List<LoginResult>> call2 = retrofitInterface.userData(PreferenceData.getLoggedInUserData(getApplicationContext()).get("userID"));
            call2.enqueue(new Callback<List<LoginResult>>() {
                @Override
                public void onResponse(Call<List<LoginResult>> call, Response<List<LoginResult>> response) {
                    if (response.code() == 200) {
                        //setData

                        StorageReference storageRef = storage.getReferenceFromUrl(response.body().get(0).getProfilePic());
                        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(getApplicationContext())
                                        .load(uri)
                                        .circleCrop()
                                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)         //ALL or NONE as your requirement
                                        .thumbnail(Glide.with(getApplicationContext()).load(R.drawable.ic_image_loading))
                                        .error(R.drawable.ic_image_error)
                                        .into(new CustomTarget<Drawable>() {
                                            @Override
                                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                                item.setIcon(resource);
                                            }

                                            @Override
                                            public void onLoadCleared(@Nullable Drawable placeholder) {

                                            }
                                        });
                            }
                        });

                    }else {
                        Toast.makeText(getApplicationContext(), "Some response code: "+ response.code(), Toast.LENGTH_LONG).show();
                    }

                }
                @Override
                public void onFailure(Call<List<LoginResult>> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), ""+t, Toast.LENGTH_LONG).show();
                }
            });
        }
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
        it.putExtra("EXTRA_USER_ID", PreferenceData.getLoggedInUserData(getApplicationContext()).get("userID"));
        startActivity(it);
    }
}