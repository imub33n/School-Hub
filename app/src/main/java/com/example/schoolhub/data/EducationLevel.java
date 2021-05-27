package com.example.schoolhub.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EducationLevel {
    @SerializedName("primary")
    @Expose
    private Boolean primary;
    @SerializedName("middle")
    @Expose
    private Boolean middle;
    @SerializedName("higher")
    @Expose
    private Boolean higher;

    public Boolean getPrimary() {
        return primary;
    }

    public void setPrimary(Boolean primary) {
        this.primary = primary;
    }

    public Boolean getMiddle() {
        return middle;
    }

    public void setMiddle(Boolean middle) {
        this.middle = middle;
    }

    public Boolean getHigher() {
        return higher;
    }

    public void setHigher(Boolean higher) {
        this.higher = higher;
    }
}
