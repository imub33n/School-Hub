package com.example.schoolhub.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchFilters {
    @SerializedName("distance")
    @Expose
    private Integer distance;
    @SerializedName("schoolType")
    @Expose
    private String schoolType;
    @SerializedName("educationLevel")
    @Expose
    private String educationLevel;
    @SerializedName("educationType")
    @Expose
    private String educationType;
    @SerializedName("fee")
    @Expose
    private Fee fee;
    @SerializedName("currentLocation")
    @Expose
    private SchoolCoordinates schoolCoordinates;

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
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

    public Fee getFee() {
        return fee;
    }

    public void setFee(Fee fee) {
        this.fee = fee;
    }

    public SchoolCoordinates getSchoolCoordinates() {
        return schoolCoordinates;
    }

    public void setSchoolCoordinates(SchoolCoordinates schoolCoordinates) {
        this.schoolCoordinates = schoolCoordinates;
    }
}
