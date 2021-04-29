package com.example.schoolhub.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SchoolsLandingModel {
    private int image;
    private String title;
    private String desc;
    private List<SchoolData> schools;


    public SchoolsLandingModel(int image, String title, String desc) {
        this.image = image;
        this.title = title;
        this.desc = desc;
    }
    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<SchoolData> getSchools() { return schools; }

    public void setSchools(List<SchoolData> schools) { this.schools = schools; }
}


