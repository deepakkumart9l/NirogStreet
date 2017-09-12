package com.app.nirogstreet.uttil;

import android.app.Application;

import com.app.nirogstreet.model.UserDetailModel;

/**
 * Created by Preeti on 12-09-2017.
 */

public class ApplicationSingleton extends Application {
    public static UserDetailModel getUserDetailModel() {
        return userDetailModel;
    }

    public static void setUserDetailModel(UserDetailModel userDetailModel) {
        ApplicationSingleton.userDetailModel = userDetailModel;
    }

    private static UserDetailModel userDetailModel;
}
