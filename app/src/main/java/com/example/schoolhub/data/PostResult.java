package com.example.schoolhub.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PostResult {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("userID")
    @Expose
    private String userID;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("likes")
    @Expose
    private List<Likes> likes;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("comments")
    @Expose
    private List<Comment> comments;
    @SerializedName("image")
    @Expose
    private String image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Likes> getLikes() { return likes; }

    public void setLikes(List<Likes> likes) { this.likes = likes; }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}