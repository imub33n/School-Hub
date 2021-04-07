package com.example.schoolhub.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Comment {
    @SerializedName("_id")
    @Expose
    private String cmntID;

    public String getCmntID() {
        return cmntID;
    }

    public void setCmntID(String cmntID) {
        this.cmntID = cmntID;
    }

    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("userID")
    @Expose
    private String userID;

    public Comment(String username, String text) {
        this.username = username;
        this.text = text;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUserID() { return userID; }

    public void setUserID(String userID) { this.userID = userID; }
}