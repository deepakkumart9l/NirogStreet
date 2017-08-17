package com.app.nirogstreet.activites;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.app.nirogstreet.R;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}
