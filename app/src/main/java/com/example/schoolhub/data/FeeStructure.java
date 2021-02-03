package com.example.schoolhub.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FeeStructure {

    @SerializedName("group")
    @Expose
    private String group;
    @SerializedName("admissionFee")
    @Expose
    private Integer admissionFee;
    @SerializedName("tutionFee")
    @Expose
    private Integer tutionFee;
    @SerializedName("examFee")
    @Expose
    private Integer examFee;
    @SerializedName("sportsFee")
    @Expose
    private Integer sportsFee;
    @SerializedName("labFee")
    @Expose
    private Integer labFee;
    @SerializedName("libraryFee")
    @Expose
    private Integer libraryFee;
    @SerializedName("totalAdmissionFee")
    @Expose
    private Integer totalAdmissionFee;
    @SerializedName("monthlyFee")
    @Expose
    private Integer monthlyFee;
    @SerializedName("othersFee")
    @Expose
    private Integer othersFee;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Integer getAdmissionFee() {
        return admissionFee;
    }

    public void setAdmissionFee(Integer admissionFee) {
        this.admissionFee = admissionFee;
    }

    public Integer getTutionFee() {
        return tutionFee;
    }

    public void setTutionFee(Integer tutionFee) {
        this.tutionFee = tutionFee;
    }

    public Integer getExamFee() {
        return examFee;
    }

    public void setExamFee(Integer examFee) {
        this.examFee = examFee;
    }

    public Integer getSportsFee() {
        return sportsFee;
    }

    public void setSportsFee(Integer sportsFee) {
        this.sportsFee = sportsFee;
    }

    public Integer getLabFee() {
        return labFee;
    }

    public void setLabFee(Integer labFee) {
        this.labFee = labFee;
    }

    public Integer getLibraryFee() {
        return libraryFee;
    }

    public void setLibraryFee(Integer libraryFee) {
        this.libraryFee = libraryFee;
    }

    public Integer getTotalAdmissionFee() {
        return totalAdmissionFee;
    }

    public void setTotalAdmissionFee(Integer totalAdmissionFee) {
        this.totalAdmissionFee = totalAdmissionFee;
    }

    public Integer getMonthlyFee() {
        return monthlyFee;
    }

    public void setMonthlyFee(Integer monthlyFee) {
        this.monthlyFee = monthlyFee;
    }

    public Integer getOthersFee() {
        return othersFee;
    }

    public void setOthersFee(Integer othersFee) {
        this.othersFee = othersFee;
    }

}