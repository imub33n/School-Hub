package com.example.schoolhub.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashMap;

public class PreferenceData {
    static final String PREF_LOGGEDIN_USER_Name = "logged_in_email";
    static final String PREF_LOGGEDIN_USER_ID = "logged_in_id";
    static final String PREF_LOGGEDIN_USER_Type = "logged_in_type";
    static final String PREF_USER_LOGGEDIN_STATUS = "logged_in_status";

    public static SharedPreferences getSharedPreferences(Context ctx)
    {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setLoggedInUserData(Context ctx, String name,String id,String type)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_LOGGEDIN_USER_Name, name);
        editor.putString(PREF_LOGGEDIN_USER_ID, id);
        editor.putString(PREF_LOGGEDIN_USER_Type, type);
        editor.commit();
    }

    public static HashMap<String, String> getLoggedInUserData(Context ctx)
    {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put("username",getSharedPreferences(ctx).getString(PREF_LOGGEDIN_USER_Name, ""));
        user.put("userID",getSharedPreferences(ctx).getString(PREF_LOGGEDIN_USER_ID, ""));
        user.put("userType",getSharedPreferences(ctx).getString(PREF_LOGGEDIN_USER_Type, ""));
        return user;
    }

    public static void setUserLoggedInStatus(Context ctx, boolean status)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean(PREF_USER_LOGGEDIN_STATUS, status);
        editor.commit();
    }

    public static boolean getUserLoggedInStatus(Context ctx)
    {
        return getSharedPreferences(ctx).getBoolean(PREF_USER_LOGGEDIN_STATUS, false);
    }

    public static void clearLoggedInEmailAddress(Context ctx)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.remove(PREF_LOGGEDIN_USER_Type);
        editor.remove(PREF_LOGGEDIN_USER_ID);
        editor.remove(PREF_LOGGEDIN_USER_Name);
        editor.remove(PREF_USER_LOGGEDIN_STATUS);
        editor.commit();
    }
}
