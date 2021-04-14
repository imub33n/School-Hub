package com.example.schoolhub.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Likes {
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("like")
    @Expose
    private Boolean like;
    @SerializedName("userID")
    @Expose
    private String userID;

    public Likes(String username, Boolean like) {
        this.username = username;
        this.like = like;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getLike() { return like; }

    public void setLike(Boolean like) { this.like = like; }

    public String getUserID() { return userID; }

    public void setUserID(String userID) { this.userID = userID; }
}
