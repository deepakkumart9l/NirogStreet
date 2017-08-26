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
import android.widget.TextView;

import com.app.nirogstreet.R;
import com.app.nirogstreet.uttil.NetworkUtill;
import com.app.nirogstreet.uttil.SesstionManager;
import com.app.nirogstreet.uttil.TypeFaceMethods;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.BuildConfig;
import com.crashlytics.android.core.CrashlyticsCore;

import java.util.Timer;
import java.util.TimerTask;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Preeti on 17-08-2017.
 */

public class Splash extends AppCompatActivity {
    SesstionManager sesstionManager;
    TextView thinkAyurveda, thinkNirog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sesstionManager = new SesstionManager(Splash.this);

        Fabric.with(this, new Crashlytics.Builder().core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build()).build());

        PackageInfo pInfo = null;

        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.splash_screen);
        thinkAyurveda = (TextView) findViewById(R.id.think);
        thinkNirog = (TextView) findViewById(R.id.nirog);
        TypeFaceMethods.setRegularTypeBoldFaceTextView(thinkAyurveda, Splash.this);
        TypeFaceMethods.setRegularTypeBoldFaceTextView(thinkNirog, Splash.this);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.statusbarcolor));
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

                if (sesstionManager.isUserLoggedIn()) {
                    startActivity(new Intent(getApplicationContext(), EditQualificationDetailOrAddQualificationsDetails.class));
                    finish();
                } else {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                }
            }
        }, 5000);
    }

}
