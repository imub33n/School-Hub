package com.example.schoolhub.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LiveStreamRequests {
    @SerializedName("schoolID")
    @Expose
    private String schoolID;
    @SerializedName("schoolName")
    @Expose
    private String schoolName;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("startTime")
    @Expose
    private String startTime;
    @SerializedName("endTime")
    @Expose
    private String endTime;
    @SerializedName("currentTime")
    @Expose
    private String currentTime;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("privacy")
    @Expose
    private String privacy;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("resourceURI")
    @Expose
    private String resourceURI;
    @SerializedName("isLive")
    @Expose
    private Boolean isLive;

    public String getSchoolID() {
        return schoolID;
    }

    public void setSchoolID(String schoolID) {
        this.schoolID = schoolID;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResourceURI() { return resourceURI; }

    public void setResourceURI(String resourceURI) { this.resourceURI = resourceURI; }

    public Boolean getLive() { return isLive; }

    public void setLive(Boolean live) { isLive = live; }
}
