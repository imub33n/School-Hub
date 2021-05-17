package com.example.schoolhub.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Teachers {
    @SerializedName("teacherID")
    @Expose
    private String teacherID;
    @SerializedName("teacherName")
    @Expose
    private String teacherName;
    @SerializedName("teacherEmail")
    @Expose
    private String teacherEmail;
    @SerializedName("teacherProfilePic")
    @Expose
    private String teacherProfilePic;

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

    public String getTeacherEmail() {
        return teacherEmail;
    }

    public void setTeacherEmail(String teacherEmail) {
        this.teacherEmail = teacherEmail;
    }

    public String getTeacherProfilePic() {
        return teacherProfilePic;
    }

    public void setTeacherProfilePic(String teacherProfilePic) {
        this.teacherProfilePic = teacherProfilePic;
    }
}
