package com.app.nirogstreet.activites;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.nirogstreet.R;
import com.app.nirogstreet.uttil.AppUrl;
import com.app.nirogstreet.uttil.NetworkUtill;
import com.app.nirogstreet.uttil.SesstionManager;
import com.app.nirogstreet.uttil.TypeFaceMethods;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.BuildConfig;
import com.crashlytics.android.core.CrashlyticsCore;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.SSLContext;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.conn.scheme.Scheme;
import cz.msebera.android.httpclient.conn.ssl.SSLSocketFactory;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;
import io.fabric.sdk.android.Fabric;

/**
 * Created by Preeti on 17-08-2017.
 */

public class Splash extends AppCompatActivity {
    SesstionManager sesstionManager;
    VersionUpdateCheckAsynctask versionUpdateCheckAsynctask;

    TextView thinkAyurveda, thinkNirog;
    String version;

    Dialog dialogPopUp = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sesstionManager = new SesstionManager(Splash.this);

        Fabric.with(this, new Crashlytics.Builder().core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build()).build());


        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.splash_screen);
        PackageInfo pInfo = null;
        try {

            pInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            version = pInfo.versionName;
            // versionTV.setText(version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
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
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                }
            }
        }, 3000);
    }

    public class VersionUpdateCheckAsynctask extends AsyncTask<Void, Void, Void> {
        JSONObject jo;
        HttpClient client;
        String responseBody;


        public void cancelAsyncTask() {
            if (client != null && !isCancelled()) {
                cancel(true);
                client = null;
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                String url = AppUrl.AppBaseUrl + "user/app-version";
                SSLSocketFactory sf = new SSLSocketFactory(
                        SSLContext.getDefault(),
                        SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                Scheme sch = new Scheme("https", 443, sf);
                client = new DefaultHttpClient();

                client.getConnectionManager().getSchemeRegistry().register(sch);
                HttpPost httppost = new HttpPost(url);
                HttpResponse response;


                List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                pairs.add(new BasicNameValuePair(AppUrl.APP_ID_PARAM, AppUrl.APP_ID_VALUE_POST));
                HashMap<String, String> user = sesstionManager.getUserDetails();
                String authToken = user.get(SesstionManager.AUTH_TOKEN);
                pairs.add(new BasicNameValuePair("auth_token", authToken));


                httppost.setEntity(new UrlEncodedFormEntity(pairs));
                response = client.execute(httppost);

                responseBody = EntityUtils
                        .toString(response.getEntity());
                jo = new JSONObject(responseBody);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {


                if (jo != null) {
                    float minVersion, latestVersion;
                    if (jo.has("android") && !jo.isNull("android")) {
                        JSONObject jsonObject = jo.getJSONObject("android");
                        if (jsonObject.has("minversion") && !jsonObject.isNull("minversion")) {
                            minVersion = Float.parseFloat(jsonObject.getString("minversion"));
                            if (Float.compare(minVersion, Float.parseFloat(version)) > 0) {

                                addDialog(minVersion, true);
                            } else if (jsonObject.has("latestversion") && !jsonObject.isNull("latestversion")) {
                                latestVersion = Float.parseFloat(jsonObject.getString("latestversion"));
                                if (Float.compare(latestVersion, Float.parseFloat(version)) > 0) {
                                    addDialog(latestVersion, false
                                    );
                                } else {
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    finish();
                                }
                            }
                        }
                       /* if (status == 1) {
                            if (jo.has("isAppUpdated") && !jo.isNull("isAppUpdated")) {xe
                                int isAppUpdated = jo.getInt("isAppUpdated");
                                if (isAppUpdated == 0) {
                                    addDialog();
                                } else {
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    finish();
                                }

                            } else {
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                            }

                        }*//* else {*/
                           /* try {
                                ShortcutBadger.with(getApplicationContext()).remove();  //for 1.1.3

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            session.logoutUser();
                            cancelNotificationOnLogout();
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            finish();*/
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void addDialog(float minversion, boolean checkIsMin) {

        dialogPopUp = new Dialog(Splash.this);
        dialogPopUp.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogPopUp.setCanceledOnTouchOutside(false);
        dialogPopUp.setContentView(R.layout.dialog_app_update);
        TextView skip = (TextView) dialogPopUp.findViewById(R.id.skip);

        if (checkIsMin && Float.compare(minversion, Float.parseFloat(version)) > 0) {
            dialogPopUp.findViewById(R.id.skip).setVisibility(View.GONE);
        } else {
            dialogPopUp.findViewById(R.id.skip).setVisibility(View.VISIBLE);

        }
        TextView update = (TextView) dialogPopUp.findViewById(R.id.update);
        TextView titleText = (TextView) dialogPopUp.findViewById(R.id.Txt);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                dialogPopUp.cancel();

                finish();
            }
        });

        final ProgressBar progressBar = (ProgressBar) dialogPopUp.findViewById(R.id.scroll);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=empwin.com.app.com"));
                startActivity(intent);
            }
        });
        dialogPopUp.show();

    }

}
