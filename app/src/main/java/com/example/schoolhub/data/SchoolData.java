package com.example.schoolhub.data;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SchoolData {
    @SerializedName("_id")
    @Expose
    private String _id;
    @SerializedName("adminID")
    @Expose
    private String adminID;
    @SerializedName("schoolName")
    @Expose
    private String schoolName;
    @SerializedName("schoolEmail")
    @Expose
    private String schoolEmail;
    @SerializedName("schoolIcon")
    @Expose
    private String schoolIcon;
    @SerializedName("schoolAddress")
    @Expose
    private String schoolAddress;
    @SerializedName("contactNumber")
    @Expose
    private String contactNumber;
    @SerializedName("zipCode")
    @Expose
    private Integer zipCode;
    @SerializedName("aboutSchool")
    @Expose
    private String aboutSchool;
    @SerializedName("schoolType")
    @Expose
    private String schoolType;
    @SerializedName("educationLevel")
    @Expose
    private String educationLevel;
    @SerializedName("educationType")
    @Expose
    private String educationType;
    @SerializedName("schoolCoordinates")
    @Expose
    private SchoolCoordinates schoolCoordinates;
    @SerializedName("feeStructure")
    @Expose
    private List<FeeStructure> feeStructure = null;
    @SerializedName("images")
    @Expose
    private List<Image> images = null;

    public String getVideos() {
        return videos;
    }

    public void setVideos(String videos) {
        this.videos = videos;
    }

    @SerializedName("videos")
    @Expose
    private String videos;
    @SerializedName("teachers")
    @Expose
    private List<Teachers> teachers = null;

    public List<Teachers> getTeachers() { return teachers; }

    public void setTeachers(List<Teachers> teachers) { this.teachers = teachers; }

    public String get_id() { return _id; }

    public void set_id(String _id) { this._id = _id; }

    public String getAdminID() {
        return adminID;
    }

    public void setAdminID(String adminID) {
        this.adminID = adminID;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getSchoolAddress() {
        return schoolAddress;
    }

    public void setSchoolAddress(String schoolAddress) {
        this.schoolAddress = schoolAddress;
    }

    public String getSchoolEmail() {
        return schoolEmail;
    }

    public void setSchoolEmail(String schoolEmail) {
        this.schoolEmail = schoolEmail;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public Integer getZipCode() {
        return zipCode;
    }

    public void setZipCode(Integer zipCode) {
        this.zipCode = zipCode;
    }

    public String getAboutSchool() {
        return aboutSchool;
    }

    public void setAboutSchool(String aboutSchool) {
        this.aboutSchool = aboutSchool;
    }

    public String getSchoolType() {
        return schoolType;
    }

    public void setSchoolType(String schoolType) {
        this.schoolType = schoolType;
    }

    public String getEducationLevel() {
        return educationLevel;
    }

    public void setEducationLevel(String educationLevel) {
        this.educationLevel = educationLevel;
    }

    public String getEducationType() {
        return educationType;
    }

    public void setEducationType(String educationType) {
        this.educationType = educationType;
    }

    public SchoolCoordinates getSchoolCoordinates() {
        return schoolCoordinates;
    }

    public void setSchoolCoordinates(SchoolCoordinates schoolCoordinates) { this.schoolCoordinates = schoolCoordinates; }

    public List<FeeStructure> getFeeStructure() {
        return feeStructure;
    }

    public void setFeeStructure(List<FeeStructure> feeStructure) { this.feeStructure = feeStructure; }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public String getSchoolIcon() { return schoolIcon; }

    public void setSchoolIcon(String schoolIcon) { this.schoolIcon = schoolIcon; }
}
