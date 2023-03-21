package com.example.retrofitexamfinal;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceHelper
{
    private final String INTRO = "intro";
    private final String nickname = "nickname";
    private final String pwCheck = "pwCheck";
    private SharedPreferences app_prefs;
    private Context context;

    public PreferenceHelper(Context context)
    {
        app_prefs = context.getSharedPreferences("shared", 0);
        this.context = context;
    }

    public void putIsLogin(boolean loginOrOut)
    {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putBoolean(INTRO, loginOrOut);
        edit.apply();
    }

    public void putName(String loginOrOut)
    {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(nickname, loginOrOut);
        edit.apply();
    }

    public String getId()
    {
        return app_prefs.getString(nickname, "");
    }

    public void putHobby(String loginOrOut)
    {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(pwCheck, loginOrOut);
        edit.apply();
    }

    public String getPw()
    {
        return app_prefs.getString(pwCheck, "");
    }
}