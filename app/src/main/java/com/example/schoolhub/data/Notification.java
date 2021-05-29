package com.example.schoolhub.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Notification {
    @SerializedName("_id")
    @Expose
    private String notificationID;
    @SerializedName("userID")
    @Expose
    private String userID;
    @SerializedName("notificationType")
    @Expose
    private String notificationType;
    @SerializedName("text")
    @Expose
    private String text;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getNotificationID() {
        return notificationID;
    }

}
