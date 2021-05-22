package com.example.schoolhub.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SchoolHubReview {
    public String getId() {
        return id;
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

    public String getUserProfilePic() {
        return userProfilePic;
    }

    public void setUserProfilePic(String userProfilePic) {
        this.userProfilePic = userProfilePic;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public List<ReplySchoolHubReview> getReply() {
        return reply;
    }

    public void setReply(List<ReplySchoolHubReview> reply) {
        this.reply = reply;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("userID")
    @Expose
    private String userID;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("userProfilePic")
    @Expose
    private String userProfilePic;
    @SerializedName("reviewText")
    @Expose
    private String reviewText;
    @SerializedName("rating")
    @Expose
    private Integer rating;
    @SerializedName("reply")
    @Expose
    private List<ReplySchoolHubReview> reply;
    @SerializedName("date")
    @Expose
    private String date;
}
