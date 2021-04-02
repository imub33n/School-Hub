package com.example.schoolhub.data;

import com.google.gson.annotations.SerializedName;

public class LoginResult {
    @SerializedName("_id")
    private String userID="";

    public String getUserID() {
        return userID;
    }

    @SerializedName("username")
    private String username="";

    public String getUsername() {
        return username;
    }
}