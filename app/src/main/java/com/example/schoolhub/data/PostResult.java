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

    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("comments")
    @Expose
    private List<Object> comments;
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

    public List<Object> getComments() {
        return comments;
    }

    public void setComments(List<Object> comments) {
        this.comments = comments;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}

/*
 @SerializedName("userID")
    public String userID;
    @SerializedName("username")
    public String username;
    @SerializedName("text")
    public String text;
    @SerializedName("image")
    public String image;
    @SerializedName("likes")
    public String likes;
    @SerializedName("time")
    public String time;
    @SerializedName("comments")
    public List<Comment> comment = new ArrayList();
    public static class Comment {
        @SerializedName("username")
        public String username;
        @SerializedName("text")
        public String text;

    }
    //public List<Comment> comments;

//    public String getUserID() {
//        return userID;
//    }
//
//    public void setUserID(String userID) {
//        this.userID = userID;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public String getText() {
//        return text;
//    }
//
//    public void setText(String text) {
//        this.text = text;
//    }
//
//    public String getImage() {
//        return image;
//    }
//
//    public void setImage(String image) {
//        this.image = image;
//    }
//
//    public Integer getLikes() {
//        return likes;
//    }
//
//    public void setLikes(Integer likes) {
//        this.likes = likes;
//    }
//
//    public String getTime() {
//        return time;
//    }
//
//    public void setTime(String time) {
//        this.time = time;
//    }
//
//    public List<Comment> getComments() {
//        return comments;
//    }
//
//    public void setComments(List<Comment> comments) {
//        this.comments = comments;
//    }

//    public static class Comment {
//        @SerializedName("username")
//        @Expose
//        private String username;
//        @SerializedName("text")
//        @Expose
//        private String text;
//
//        public String getUsername() {
//            return username;
//        }
//
//        public void setUsername(String username) {
//            this.username = username;
//        }
//
//        public String getText() {
//            return text;
//        }
//
//        public void setText(String text) {
//            this.text = text;
//        }
//
//    }
*/