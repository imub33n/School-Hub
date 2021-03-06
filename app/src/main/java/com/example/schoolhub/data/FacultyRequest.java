package com.example.schoolhub.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FacultyRequest {
    @SerializedName("_id")
    @Expose
    private String requestID;
    @SerializedName("teacherID")
    @Expose
    private String teacherID;
    @SerializedName("teacherName")
    @Expose
    private String teacherName;
    @SerializedName("teacherEmail")
    @Expose
    private String teacherEmail;
    @SerializedName("schoolID")
    @Expose
    private String schoolID;
    @SerializedName("status")
    @Expose
    private String statusRequest;
    @SerializedName("teacherProfilePic")
    @Expose
    private String teacherProfilePic;
    @SerializedName("adminID")
    @Expose
    private String adminID;

    public String getAdminID() { return adminID; }

    public void setAdminID(String adminID) { this.adminID = adminID; }

    public String getTeacherEmail() { return teacherEmail; }

    public void setTeacherEmail(String teacherEmail) { this.teacherEmail = teacherEmail; }

    public String getRequestID() {
        return requestID;
    }

    public String getTeacherID() {
        return teacherID;
    }

    public void setTeacherID(String teacherID) {
        this.teacherID = teacherID;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getSchoolID() {
        return schoolID;
    }

    public void setSchoolID(String schoolID) {
        this.schoolID = schoolID;
    }

    public String getStatusRequest() {
        return statusRequest;
    }

    public void setStatusRequest(String statusRequest) {
        this.statusRequest = statusRequest;
    }

    public String getTeacherProfilePic() {
        return teacherProfilePic;
    }

    public void setTeacherProfilePic(String teacherProfilePic) {
        this.teacherProfilePic = teacherProfilePic;
    }
}
