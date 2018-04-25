package com.app.nirogstreet.uttil;

import android.content.Context;
import android.os.Bundle;

import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by as on 3/16/2018.
 */

public class Event_For_Firebase {

    private static FirebaseAnalytics mFirebaseAnalytics;
    private static AppEventsLogger logger;

    public static FirebaseAnalytics getEventCount(Context context, String eventName) {

        if (mFirebaseAnalytics == null) {
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        }
        Bundle params2 = new Bundle();
        params2.putString(FirebaseAnalytics.Param.VALUE, eventName);
        mFirebaseAnalytics.logEvent(eventName, params2);
        return mFirebaseAnalytics;
    }

    /* public static void getEventCount(Context context, String eventName) {
        *//* FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        Bundle params2 = new Bundle();
        params2.putString(FirebaseAnalytics.Param.VALUE, eventName);
        mFirebaseAnalytics.logEvent(eventName, params2);*//*
    }*/
  /*  public static AppEventsLogger getFaceboobEventCount(Context context, String eventName) {

        if (logger == null) {
            logger = AppEventsLogger.newLogger(context);
        }
        Bundle params2 = new Bundle();
        params2.putString(AppEventsLogger., eventName);
        logger.logEvent(eventName, params2);
        return logger;
    }*/
    public static void logAppEventsLoggerEvent(Context context,String eventName) {
        if (logger == null) {
            logger = AppEventsLogger.newLogger(context);
        }
        logger.logEvent(eventName);
    }
}
