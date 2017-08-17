package com.app.nirogstreet.activites;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.app.nirogstreet.R;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.BuildConfig;
import com.crashlytics.android.core.CrashlyticsCore;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Preeti on 17-08-2017.
 */

public class Splash  extends AppCompatActivity{
            /*try {
            ShortcutBadger.with(getApplicationContext()).remove();  //for 1.1.3
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }*/
    //  Fabric.with(this, new Crashlytics());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    Fabric.with(this, new Crashlytics.Builder().core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build()).build());
        Intent intent=new Intent(Splash.this,RegisterActivity.class);
        startActivity(intent);
    PackageInfo pInfo = null;

    if (Build.VERSION.SDK_INT < 16) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
    setContentView(R.layout.splash_screen);
    if (android.os.Build.VERSION.SDK_INT >= 21) {
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.statusbarcolor));
    }
}}
