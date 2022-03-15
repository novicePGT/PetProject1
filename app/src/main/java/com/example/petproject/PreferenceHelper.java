package com.example.petproject;

import android.content.Context;
import android.content.SharedPreferences;

//쉐어드를 사용하기 때문에 쉐어드 관련 메서드들을 모아놓은 헬퍼 클래스이다.
public class PreferenceHelper {
    private final String INTRO = "intro";
    private final String userEmail = "userEmail";
    private final String userPassword = "userPassword";
    private SharedPreferences app_prefs;
    private Context context;

    public PreferenceHelper(Context context) {
        app_prefs = context.getSharedPreferences("shared", 0);
        this.context = context;
    }

    public void putIsLogin(boolean loginOrOut) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putBoolean(INTRO, loginOrOut);
        edit.apply();
    }

    public void putUserEmail(String loginOrOut) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(userEmail, loginOrOut);
        edit.apply();
    }

    public String getUserEmail() {
        return app_prefs.getString(userEmail, "");
    }

    public void putUserPassword(String loginOrOut) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(userPassword, loginOrOut);
        edit.apply();
    }

    public String getUserPassword() {
        return app_prefs.getString(userPassword, "");
    }
}
