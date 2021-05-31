package com.example.schoolhub.Adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.schoolhub.R;
import com.example.schoolhub.data.AttachmentListData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class AttachmentListAdapter extends RecyclerView.Adapter<AttachmentListAdapter.AttachmentListViewHolder> {
    public static ArrayList<AttachmentListData> newAttachmentList;
    public Activity mActivity;


    public AttachmentListAdapter(ArrayList<AttachmentListData> list, Activity activity) {
        newAttachmentList = list;
        mActivity = activity;
    }

    @Override
    public AttachmentListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_newattachment_list, parent, false);

        AttachmentListViewHolder holder = new AttachmentListViewHolder(view, mActivity, newAttachmentList);

        return holder;
    }

    @Override
    public void onBindViewHolder(final AttachmentListViewHolder holder, int position) {
        holder.attachedImageName.setText((newAttachmentList.get(position).getImageName()));
        String userImage = newAttachmentList.get(position).getImageID();
        if (userImage.isEmpty()||userImage.equals(null)||userImage.equals("")) {

        } else {
            try{
                Glide.with(mActivity)
                        .load(userImage)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)         //ALL or NONE as your requirement
                        .thumbnail(Glide.with(mActivity).load(R.drawable.ic_img_loading))
                        .error(R.drawable.ic_image_error)
                        .into(holder.attachedImageId);
            }catch(Exception e){
                Log.d(TAG, "Photo loading failed : "+ e);
            }
        }
    }

    @Override
    public int getItemCount() {
        return newAttachmentList.size();
    }

    class AttachmentListViewHolder extends RecyclerView.ViewHolder {
        ImageView attachedImageId,cancelAttachment;
        TextView attachedImageName;
        FloatingActionButton floatingNextButton2;


        public AttachmentListViewHolder(View view, final Activity activity, ArrayList<AttachmentListData> attachmentList) {
            super(view);
            attachedImageId= view.findViewById(R.id.attachedImageId);
            attachedImageName= view.findViewById(R.id.attachedImageName);
            cancelAttachment= view.findViewById(R.id.cancelAttachment);
            floatingNextButton2= view.findViewById(R.id.floatingNextButton2);
            cancelAttachment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    attachmentList.remove(pos);
                    notifyDataSetChanged();
                }
            });
//            floatingNextButton2.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.d(TAG, "onClick: "+attachmentList);
//                    Intent intent = new Intent(activity,AddingSchoolStep3.class );
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                    activity.startActivity(intent);
//                }
//            });

        }
    }
}
