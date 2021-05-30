package com.example.schoolhub.AddingSchool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.schoolhub.Adapters.AttachmentListAdapter;
import com.example.schoolhub.R;
import com.example.schoolhub.data.AttachmentListData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kofigyan.stateprogressbar.StateProgressBar;

import java.util.ArrayList;
import static android.content.ContentValues.TAG;

public class AddingSchoolStep2 extends AppCompatActivity {
    private static final int PICK_FROM_GALLERY = 101;

    RecyclerView newAttachmentListView;
    public static ArrayList<AttachmentListData> newAttachmentList = new ArrayList<>();
    public static String videoUri="";
    AttachmentListAdapter attachmentListAdapter;
    FloatingActionButton floatingNextButton2,floatingBackButton2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle saveThis= new Bundle();
        setContentView(R.layout.activity_adding_school_step2);
        StateProgressBar stateProgressBar = (StateProgressBar) findViewById(R.id.your_state_progress_bar_id);
        stateProgressBar.setStateDescriptionData(AddingSchool.descriptionData);
        floatingBackButton2=findViewById(R.id.floatingBackButton2);
        floatingNextButton2=findViewById(R.id.floatingNextButton2);
        newAttachmentListView = (RecyclerView) findViewById(R.id.newAttachmentList);
        floatingBackButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveInstanceState(saveThis);
                onBackPressed();
            }
        });
        floatingNextButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newAttachmentList.size()<3){
                    Toast.makeText(AddingSchoolStep2.this,"Please select at least 3 photos+video",Toast.LENGTH_LONG).show();
                }else if(newAttachmentList.size()>20){
                    Toast.makeText(AddingSchoolStep2.this,"Please select at most 20 photos+video",Toast.LENGTH_LONG).show();
                }
                else{
                    onSaveInstanceState(saveThis);
                    Intent intent = new Intent(getApplicationContext(),AddingSchoolStep3.class );
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
            }
        });
    }

    public void selectPhotos(View view) {
        Uri uri = Uri.parse(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());
        Intent intent = new Intent();
        intent.setType("image/*");
        //intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_STREAM,uri);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_FROM_GALLERY);
        //intent.getClipData().getItemAt(0);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //for photos
        if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK) {
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                for (int i = 0; i < count; i++) {
                    Uri returnUri = data.getClipData().getItemAt(i).getUri();
                    Cursor returnCursor = getContentResolver().query(returnUri, null, null, null, null);
                    int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                    returnCursor.moveToFirst();
                    AttachmentListData attachmentListData = new AttachmentListData();
                    attachmentListData.setImageName(returnCursor.getString(nameIndex));
                    attachmentListData.setImageID(returnUri.toString());
                    newAttachmentList.add(attachmentListData);
                }

            } else if (data.getData() != null) {
                Uri returnUri = data.getData();

                Cursor returnCursor =
                        getContentResolver().query(returnUri, null, null, null, null);
                int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                returnCursor.moveToFirst();
                AttachmentListData attachmentListData = new AttachmentListData();
                attachmentListData.setImageName(returnCursor.getString(nameIndex));
                attachmentListData.setImageID(returnUri.toString());
                newAttachmentList.add(attachmentListData);
            }
            generateNewAttachmentList(newAttachmentList);
        }
    }
    private void generateNewAttachmentList(ArrayList<AttachmentListData> newAttachmentList) {
        newAttachmentListView.setHasFixedSize(true);
        LinearLayoutManager MyLayoutManager = new LinearLayoutManager(this);
        MyLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        newAttachmentListView.setLayoutManager(MyLayoutManager);
        attachmentListAdapter = new AttachmentListAdapter(newAttachmentList, this);
        newAttachmentListView.setAdapter(attachmentListAdapter);
    }

}
//<TextView
//android:id="@+id/uploadVideos"
//android:layout_width="wrap_content"
//android:layout_height="wrap_content"
//android:layout_gravity="center"
//android:layout_margin="10dp"
//android:clickable="true"
//android:onClick="uploadVideo"
//android:drawableTint="#000"
//android:drawableLeft="@drawable/video"
//android:text="  Select Video"
//android:textColor="#000" />