package com.example.schoolhub.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Image {
    @SerializedName("_id")
    @Expose
    private String _id;
    @SerializedName("path")
    @Expose
    private String path;

    public String get_id() { return _id; }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}