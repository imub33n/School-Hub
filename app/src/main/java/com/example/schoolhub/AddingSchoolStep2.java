package com.example.schoolhub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
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
import com.example.schoolhub.data.AttachmentListData;
import com.kofigyan.stateprogressbar.StateProgressBar;

import java.util.ArrayList;
import static android.content.ContentValues.TAG;

public class AddingSchoolStep2 extends AppCompatActivity {
    private static final int PICK_FROM_GALLERY = 101;
    private static final int PICK_Video_FROM_GALLERY = 11;
    RecyclerView newAttachmentListView;
    public static ArrayList<AttachmentListData> newAttachmentList = new ArrayList<>();
    public static String nameVideo,videoUri="";
    AttachmentListAdapter attachmentListAdapter;
    TextView videoName;
    ImageView attachedVideoId,cancelVideo;
    RelativeLayout videoLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_school_step2);
        StateProgressBar stateProgressBar = (StateProgressBar) findViewById(R.id.your_state_progress_bar_id);
        stateProgressBar.setStateDescriptionData(AddingSchool.descriptionData);
        videoLayout= findViewById(R.id.videoLayout);
        videoName=findViewById(R.id.videoName);
        attachedVideoId=findViewById(R.id.attachedImageId);
        cancelVideo= findViewById(R.id.cancelAttachment);
        newAttachmentListView = (RecyclerView) findViewById(R.id.newAttachmentList);
        cancelVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoName.setText("");
                nameVideo="";
                videoUri="";
                videoLayout.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
            }
        });
    }

    public void backStep2(View view) {
        Intent intent = new Intent(getApplicationContext(),AddingSchool.class );
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    public void nextStep2(View view) {
        //Toast.makeText(this,AttachmentListAdapter.newAttachmentList.size(),Toast.LENGTH_LONG).show();
        Log.d(TAG, "nextStep2:_________________________ "+newAttachmentList.size());
        if(newAttachmentList.size()<3){
            Toast.makeText(this,"Please select at least 3 photos+video",Toast.LENGTH_LONG).show();
        }else if(newAttachmentList.size()>20){
            Toast.makeText(this,"Please select at most 20 photos+video",Toast.LENGTH_LONG).show();
        }else if(videoUri==""){
            Toast.makeText(this,"Please select a video",Toast.LENGTH_LONG).show();
        }else{
            Intent intent = new Intent(getApplicationContext(),AddingSchoolStep3.class );
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }

    }
    public void uploadVideo(View view) {
        Uri uri = Uri.parse(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());
        Intent intent2 = new Intent();
        intent2.setType("video/*");
        intent2.putExtra(Intent.EXTRA_STREAM,uri);
        intent2.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent2, "Select Video"), PICK_Video_FROM_GALLERY);
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
        //for video
        if (requestCode == PICK_Video_FROM_GALLERY){
            if (data.getData() != null) {
                Uri videoUrl = data.getData();
                Cursor returnCursor = getContentResolver().query(videoUrl, null, null, null, null);
                int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                returnCursor.moveToFirst();
                nameVideo=returnCursor.getString(nameIndex);
                videoUri= videoUrl.toString();
                videoName.setText(nameVideo);
                videoLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                if (videoUri.isEmpty()||videoUri.equals(null)||videoUri.equals("")) {

                } else {
                    Glide.with(this)
                            .load(videoUri)
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)         //ALL or NONE as your requirement
                            .thumbnail(Glide.with(this).load(R.drawable.ic_image_loading))
                            .error(R.drawable.ic_image_error)
                            .into(attachedVideoId);
                }
            }
        }
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

}