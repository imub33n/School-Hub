package com.example.schoolhub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import com.example.schoolhub.data.AttachmentListData;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class EditSchoolPhotos extends AppCompatActivity {
    private static final int PICK_FROM_GALLERY = 101;
    private static final int PICK_Video_FROM_GALLERY = 11;
    //RecyclerView newAttachmentListView;
    public static ArrayList<AttachmentListData> newAttachmentList = new ArrayList<>();
    public static String nameVideo,videoUri="",namePhoto,photoUri="";
    AttachmentListAdapter attachmentListAdapter;
    TextView videoName,photoName;
    ImageView attachedVideoId,cancelVideo,attachedPhotoId,cancelPhoto;
    RelativeLayout videoLayout,photoLayout;
    public Toolbar toolbarEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_school_photos);
        toolbarEdit = (Toolbar) findViewById(R.id.toolbarEditSchool);
        videoLayout= findViewById(R.id.videoLayout);
        videoName=findViewById(R.id.videoName);
        attachedVideoId=findViewById(R.id.attachedImageId);
        cancelVideo= findViewById(R.id.cancelAttachment);
        photoLayout= findViewById(R.id.photoLayout);
        photoName=findViewById(R.id.photoName);
        attachedPhotoId=findViewById(R.id.attachedPhotoId);
        cancelPhoto= findViewById(R.id.cancelPhotoAttachment);
        //newAttachmentListView = (RecyclerView) findViewById(R.id.newAttachmentList);
        cancelVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoName.setText("");
                nameVideo="";
                videoUri="";
                videoLayout.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
            }
        });
        cancelPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoName.setText("");
                namePhoto="";
                photoUri="";
                photoLayout.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
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

    public void updatePhotos(View view) {
        Log.d(TAG, "nextStep2:_________________________ "+newAttachmentList.size());
        if(newAttachmentList.size()<3){
            Toast.makeText(this,"Please select at least 3 photos+video",Toast.LENGTH_LONG).show();
        }else if(newAttachmentList.size()>20){
            Toast.makeText(this,"Please select at most 20 photos+video",Toast.LENGTH_LONG).show();
        }else if(videoUri==""){
            Toast.makeText(this,"Please select a video",Toast.LENGTH_LONG).show();
        }else{
//            Intent intent = new Intent(getApplicationContext(),AddingSchoolStep3.class );
//            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//            startActivity(intent);
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
        intent.putExtra(Intent.EXTRA_STREAM,uri);
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
            if (data.getData() != null) {
                Uri returnUri = data.getData();
                Cursor returnCursor = getContentResolver().query(returnUri, null, null, null, null);
                int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                returnCursor.moveToFirst();
                namePhoto=returnCursor.getString(nameIndex);
                photoUri= returnUri.toString();
                photoName.setText(namePhoto);
                photoLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                if (photoUri.isEmpty()||photoUri.equals(null)||photoUri.equals("")) {

                } else {
                    Glide.with(this)
                            .load(returnUri)
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)         //ALL or NONE as your requirement
                            .thumbnail(Glide.with(this).load(R.drawable.ic_image_loading))
                            .error(R.drawable.ic_image_error)
                            .into(attachedPhotoId);
                }
            }
        }
    }
}
