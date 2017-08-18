package com.app.nirogstreet.uttil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.app.nirogstreet.activites.LoginActivity;

import java.util.HashMap;

/**
 * Created by Preeti on 18-08-2017.
 */

public class SesstionManager {
    SharedPreferences pref, pref1;
    SharedPreferences.Editor editor, editor1;
    Context context;
    int PRIVATE_MODE = 0;
    public static final String AUTH_TOKEN = "Auth_Token";
    private static final String PREFER_LANGUAGE = "Language";

    private static final String PREFER_NAME = "AndroidExamplePref";
    private static final String IS_USER_LOGIN = "IsUserLoggedIn";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_LANGUAGE = "language";
    public static final String USER_NAME = "username";
    public static final String USER_ID = "user_Id";


    public static final String PROFILE_URl = "Url";


    public SesstionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        pref1 = context.getSharedPreferences(PREFER_LANGUAGE, PRIVATE_MODE);
        editor1 = pref1.edit();
        editor = pref.edit();
    }

    public void createUserLoginSession(String name, String email, String auth_token, String userName, String profileUrl,String userId) {
        editor.putBoolean(IS_USER_LOGIN, true);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(AUTH_TOKEN, auth_token);
        editor.putString(USER_NAME, userName);
        editor.putString(PROFILE_URl, profileUrl);
        editor.putString(USER_ID,userId);
        //  editor.putString(KEY_LANGUAGE,lang);
        editor.commit();
    }

    public boolean checkLogedIn() {
        if (!isUserLoggedIn()) {
            Intent i = new Intent(context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(i);
            return true;
        } else
            return false;
    }

    public boolean isUserLoggedIn() {
        return pref.getBoolean(IS_USER_LOGIN, false);
    }

    public HashMap<String, String> getUserDetails() {

        HashMap<String, String> user = new HashMap<String, String>();

        user.put(AUTH_TOKEN, pref.getString(AUTH_TOKEN, null));
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        user.put(USER_NAME, pref.getString(USER_NAME, null));
        user.put(PROFILE_URl, pref.getString(PROFILE_URl, null));user.put(USER_ID,pref.getString(USER_ID,null));
        return user;
    }

    public void logoutUser() {

        editor.clear();
        editor.commit();

        /*Intent i = new Intent(context, LoginActivity.class);

        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(i);*/
    }



    public void updateLanguage(String language) {

        languageLogOut();
        editor1.putString(KEY_LANGUAGE, language);
        editor1.commit();
    }

    public void languageLogOut() {
        editor1.clear();
        editor1.commit();

    }

}
