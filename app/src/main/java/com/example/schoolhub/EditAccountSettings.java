package com.example.schoolhub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.schoolhub.data.PreferenceData;
import com.example.schoolhub.data.SchoolReviews;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditAccountSettings extends AppCompatActivity {
    public Toolbar toolbarEdit;
    TextView deleteAccount;

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_academic_info);
        toolbarEdit = (Toolbar) findViewById(R.id.toolbarEditSchool);
        deleteAccount = findViewById(R.id.deleteAccount);

        //retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder2 = new AlertDialog.Builder(EditAccountSettings.this);
                builder2.setMessage("Are you sure, You want to delete School Permanently?");
                builder2.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                // delete ...
                                HashMap<String, String> map = new HashMap<>();

                                map.put("adminID", AdminDashMainPage.yesSchoolData.getAdminID());
                                Call<Void> call2er = retrofitInterface.deleteSchool(AdminDashMainPage.yesSchoolData.get_id(),map);
                                call2er.enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        if (response.isSuccessful()) {
                                            Toast.makeText(EditAccountSettings.this,"School Deleted",Toast.LENGTH_LONG).show();
                                        }else {
                                            Toast.makeText(EditAccountSettings.this, "CODE: "+response.code(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {
                                        Toast.makeText(EditAccountSettings.this, "Connection Err: "+t, Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                builder2.create();
                builder2.show();
            }
        });

        toolbarEdit.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                onBackPressed();
            }
        });

    }
}