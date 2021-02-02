package com.example.schoolhub;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.schoolhub.data.AttachmentListData;

import java.util.ArrayList;

public class AttachmentListAdapter extends RecyclerView.Adapter<AttachmentListAdapter.AttachmentListViewHolder> {
    public ArrayList<AttachmentListData> newAttachmentList;
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
            Glide.with(mActivity)
                    .load(userImage)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)         //ALL or NONE as your requirement
                    .thumbnail(Glide.with(mActivity).load(R.drawable.ic_image_loading))
                    .error(R.drawable.a)
                    .into(holder.attachedImageId);
//            Picasso.with(mActivity)
//                    .load(userImage)
//                    .placeholder(R.drawable.ic_image_loading)
//                    .fit().centerCrop()
//                    .into( holder.attachedImageId);
        }
    }

    @Override
    public int getItemCount() {
        return newAttachmentList.size();
    }

    class AttachmentListViewHolder extends RecyclerView.ViewHolder {
        ImageView attachedImageId,cancelAttachment;
        TextView attachedImageName;



        public AttachmentListViewHolder(View view, final Activity activity, ArrayList<AttachmentListData> attachmentList) {
            super(view);
            attachedImageId= view.findViewById(R.id.attachedImageId);
            attachedImageName= view.findViewById(R.id.attachedImageName);
            cancelAttachment= view.findViewById(R.id.cancelAttachment);
            cancelAttachment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();

                    attachmentList.remove(pos);
                    notifyDataSetChanged();
                }
            });
        }
    }
}
