package com.example.schoolhub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Toast;

import com.example.schoolhub.data.AttachmentListData;
import com.kofigyan.stateprogressbar.StateProgressBar;

import java.util.ArrayList;

public class AddingSchoolStep2 extends AppCompatActivity {
    private static final int PICK_FROM_GALLERY = 101;
    RecyclerView newAttachmentListView;
    private ArrayList<AttachmentListData> newAttachmentList = new ArrayList<>();
    AttachmentListAdapter attachmentListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_school_step2);
        StateProgressBar stateProgressBar = (StateProgressBar) findViewById(R.id.your_state_progress_bar_id);
        stateProgressBar.setStateDescriptionData(AddingSchool.descriptionData);
        newAttachmentListView = (RecyclerView) findViewById(R.id.newAttachmentList);
//        uploadPhotosView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
    }

    public void backStep2(View view) {
        Intent intent = new Intent(getApplicationContext(),AddingSchool.class );
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    public void nextStep2(View view) {
        Intent intent = new Intent(getApplicationContext(),AddingSchoolStep3.class );
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
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
        if (requestCode == PICK_FROM_GALLERY && resultCode == this.RESULT_OK) {
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                for (int i = 0; i < count; i++) {
                    Uri returnUri = data.getClipData().getItemAt(i).getUri();
                    Cursor returnCursor = getContentResolver().query(returnUri, null, null, null, null);
                    int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                    returnCursor.moveToFirst();
                    System.out.println("PIYUSH NAME IS" + returnCursor.getString(nameIndex));
                    System.out.println("PIYUSH SIZE IS" + Long.toString(returnCursor.getLong(sizeIndex)));
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
                System.out.println("PIYUSH NAME IS" + returnCursor.getString(nameIndex));
                System.out.println("PIYUSH SIZE IS" + Long.toString(returnCursor.getLong(sizeIndex)));
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
    public void uploadVideo(View view) {
        Intent intent2 = new Intent();
        intent2.setType("video/*");
        //intent.setType("*/*");
        intent2.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent2, "Select Video"), PICK_FROM_GALLERY);
    }
}