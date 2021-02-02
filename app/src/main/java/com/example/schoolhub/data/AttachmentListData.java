package com.example.schoolhub.data;

import android.os.Parcel;
import android.os.Parcelable;

public class AttachmentListData implements Parcelable {
    private String imageName;

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageID() {
        return imageID;
    }

    public void setImageID(String imageID) {
        this.imageID = imageID;
    }

    private String imageID;

    public static final Creator CREATOR = new Creator() {
        public AttachmentListData createFromParcel(Parcel in) {
            return new AttachmentListData(in);
        }

        public AttachmentListData[] newArray(int size) {
            return new AttachmentListData[size];
        }
    };




    public AttachmentListData() {
    }

    public AttachmentListData(Parcel in) {
        super();
        readFromParcel(in);
    }



    public void readFromParcel(Parcel in) {
        imageName = in.readString();
        imageID = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageName);
        dest.writeString(imageID);
    }
}
