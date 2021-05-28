package com.example.schoolhub;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.cometchat.pro.core.AppSettings;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.uikit.ui_resources.utils.Utils;
import com.example.schoolhub.Adapters.SearchResultAdapter;
import com.example.schoolhub.Adapters.SearchUserAdapter;
import com.example.schoolhub.data.LoginResult;
import com.example.schoolhub.data.PreferenceData;
import com.example.schoolhub.data.SchoolData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;

public class HomePanel extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    Button logout,reviewAndFeedback;
    RadioButton studentButton,teacherButton;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    FirebaseStorage storage= FirebaseStorage.getInstance();
    TextView statusUserSearch;
    EditText searchUserEditText;
    List<LoginResult> loginResults;
    RecyclerView recyclerView;
    SearchUserAdapter searchUserAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_panel);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        logout = findViewById(R.id.logout);
        //dark mode ke chuti
        if(Utils.isDarkMode(getApplicationContext())){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        //chat
        AppSettings appSettings=new AppSettings.AppSettingsBuilder().subscribePresenceForAllUsers().setRegion(MainActivity.region).build();
        CometChat.init(this, MainActivity.appID,appSettings, new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String successMessage) {
                //UIKitSettings.setAuthKey(authKey);
                CometChat.setSource("ui-kit","android","java");
                Log.d(TAG, "Initialization completed successfully");
            }

            @Override
            public void onError(CometChatException e) {
                Log.d(TAG, "Initialization failed with exception: " + e.getMessage());
            }
        });



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
                FirebaseMessaging.getInstance().deleteToken();
                finish();
                Intent it = new Intent( HomePanel.this , LandingScreen.class);
                it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
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
            StorageReference storageRef = storage.getReferenceFromUrl(Objects.requireNonNull(PreferenceData.getLoggedInUserData(HomePanel.this).get("userPic")));
            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(getApplicationContext())
                            .load(uri)
                            .circleCrop()
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)         //ALL or NONE as your requirement
                            .thumbnail(Glide.with(getApplicationContext()).load(R.drawable.ic_img_loading))
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

    public void inflateSearch(MenuItem item) {
        openSearchFragment();
    }
    public void openSearchFragment() {
        View v = getLayoutInflater().inflate(R.layout.fragment_search_user, null);
        //full screen bottom sheet
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        LinearLayout linearLayout = v.findViewById(R.id.rootfragUser);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
        params.height = getScreenHeight();
        linearLayout.setLayoutParams(params);
        dialog.setContentView(v);
        dialog.show();
        BottomSheetBehavior mBehavior;
        mBehavior = BottomSheetBehavior.from((View) v.getParent());
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        //yes
        searchUserEditText =v.findViewById(R.id.searchUserEditText);
        statusUserSearch =v.findViewById(R.id.statusUserSearch);
        teacherButton =v.findViewById(R.id.teacherButton);
        studentButton =v.findViewById(R.id.studentButton);
        //recycler
        recyclerView = (RecyclerView) v.findViewById(R.id.searchUserResultRecycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchUserEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                searchit();
            }
        });
        teacherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchit();
            }
        });
        studentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchit();
            }
        });
        searchUserEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, 0);
                recyclerView.setLayoutParams(layoutParams);
                statusUserSearch.setText("");
            }
        });
    }
    private int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
    private void searchit(){
        ///
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, 0);
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams3.gravity = Gravity.CENTER;
        if(loginResults!=null){
            loginResults.clear();
        }
        statusUserSearch.setLayoutParams(layoutParams3);
        statusUserSearch.setText("Searching..");
//
        HashMap<String, String> mapTypeUser = new HashMap<>();
        if(teacherButton.isChecked()){
            mapTypeUser.put("type","Teacher");
        }else if(studentButton.isChecked()){
            mapTypeUser.put("type","Student");
        }else{
            mapTypeUser.put("type","");
        }

        Call<List<LoginResult>> call = retrofitInterface.getUser(searchUserEditText.getText().toString(),mapTypeUser);
        call.enqueue(new Callback<List<LoginResult>>() {
            @Override
            public void onResponse(Call<List<LoginResult>> call, Response<List<LoginResult>> response) {
                loginResults=response.body();
                if(response.code()==200){
                    recyclerView.setLayoutParams(layoutParams2);
                    if(loginResults.size()>0){
                        statusUserSearch.setText("");
                        statusUserSearch.setLayoutParams(layoutParams);
                        searchUserAdapter = new SearchUserAdapter(loginResults,getApplicationContext());
                        recyclerView.setAdapter(searchUserAdapter);
                    }else if(loginResults.size()==0){
                        statusUserSearch.setText("No User Found");
                    }


                }
                if(!response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "search Err: "+response.code(), Toast.LENGTH_LONG).show();
                    statusUserSearch.setText("Err Database: "+response.code());
                }
            }
            @Override
            public void onFailure(Call<List<LoginResult>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Connection error: "+t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void openNotifications(MenuItem item) {
        Intent it = new Intent( getApplicationContext() , Notifications.class);
        startActivity(it);
    }
}