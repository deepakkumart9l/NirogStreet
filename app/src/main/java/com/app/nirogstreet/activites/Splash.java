package com.app.nirogstreet.activites;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.nirogstreet.R;
import com.app.nirogstreet.uttil.AppUrl;
import com.app.nirogstreet.uttil.Methods;
import com.app.nirogstreet.uttil.NetworkUtill;
import com.app.nirogstreet.uttil.SesstionManager;
import com.app.nirogstreet.uttil.TypeFaceMethods;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.BuildConfig;
import com.crashlytics.android.core.CrashlyticsCore;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.crash.FirebaseCrash;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.SSLContext;

import bolts.AppLinks;
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
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.fabric.sdk.android.Fabric;

/**
 * Created by Preeti on 17-08-2017.
 */

public class Splash extends AppCompatActivity {
    SesstionManager sesstionManager;
    VersionUpdateCheckAsynctask versionUpdateCheckAsynctask;
    TextView thinkAyurveda, thinkNirog;
    String version, mReferralcode;
    Dialog dialogPopUp = null;
    String mBranchpostId, refer_user_mobile_number, refer_user_groupId, refer_user_userId;
    Branch branch;
    String user_fromLink, refer_community_name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sesstionManager = new SesstionManager(Splash.this);
        if (!AppUrl.AppBaseUrl.contains("appstage")) {
            Fabric.with(this, new Crashlytics.Builder().core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build()).build());
            FirebaseCrash.setCrashCollectionEnabled(false);
        }
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.splash_screen);

        AppEventsLogger logger = AppEventsLogger.newLogger(Splash.this);
        logger.logEvent("battledAnOrc");

        Uri targetUrl = AppLinks.getTargetUrlFromInboundIntent(Splash.this, getIntent());
        if (targetUrl != null) {
            Log.i("Activity", "App Link Target URL: " + targetUrl.toString());
        }
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


        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.statusbarcolor));
        }

        branch = Branch.getInstance(Splash.this);
        // Branch init
        branch.initSession(new Branch.BranchReferralInitListener() {
            @Override
            public void onInitFinished(JSONObject referringParams, BranchError error) {
                if (error == null) {
                    // params are the deep linked params associated with the link that the user clicked -> was re-directed to this app
                    // params will be empty if no data found
                    // ... insert custom logic here ...
                    Log.i("BRANCH SDK", referringParams.toString());
                    SharedPreferences sharedPref1 = getApplicationContext().getSharedPreferences("Branchid", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor1 = sharedPref1.edit();
                    try {
                        if (referringParams.has("postId")) {
                            mBranchpostId = referringParams.getString("postId");
                            if (mBranchpostId != null && mBranchpostId.length() > 0) {
                                editor1.putString("Branchid", mBranchpostId);
                                editor1.commit();
                            }
                        }
                        if (referringParams.has("refer_user_mobile_number")) {
                            refer_user_mobile_number = referringParams.getString("refer_user_mobile_number");
                            if (refer_user_mobile_number != null && refer_user_mobile_number.length() > 0) {
                                Branch.getInstance().setIdentity(refer_user_mobile_number);
                                editor1.putString("Refer_User_Number", refer_user_mobile_number);
                                editor1.commit();
                            }
                        }
                        if (referringParams.has("groupId")) {
                            refer_user_groupId = referringParams.getString("groupId");
                            if (refer_user_groupId != null && refer_user_groupId.length() > 0) {
                                editor1.putString("Refer_User_Group_Id", refer_user_groupId);
                                editor1.commit();
                            }
                        }
                        if (referringParams.has("community_name")) {
                            refer_community_name = referringParams.getString("community_name");
                            if (refer_community_name != null && refer_community_name.length() > 0) {
                                editor1.putString("refer_community_name", refer_community_name);
                                editor1.commit();
                            }
                        }
                        if (referringParams.has("userId")) {
                            refer_user_userId = referringParams.getString("userId");
                            if (refer_user_userId != null && refer_user_userId.length() > 0) {
                                editor1.putString("Refer_User_Id", refer_user_userId);
                                editor1.commit();
                            }
                        }
                        if (referringParams.has("user_fromLink")) {
                            user_fromLink = referringParams.getString("user_fromLink");
                            if (user_fromLink != null && user_fromLink.length() > 0) {
                                editor1.putString("user_fromLink", user_fromLink);
                                editor1.commit();
                            }
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.i("BRANCH SDK", error.getMessage());
                }
            }
        }, this.getIntent().getData(), this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);


        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

                if (NetworkUtill.isNetworkAvailable(Splash.this)) {
                    versionUpdateCheckAsynctask = new VersionUpdateCheckAsynctask();
                    versionUpdateCheckAsynctask.execute();
                } else {
                    try {
                        NetworkUtill.showNoInternetDialog(Splash.this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
               /* if (sesstionManager.isUserLoggedIn()) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                }*/
            }
        }, 3000);
    }

    public class VersionUpdateCheckAsynctask extends AsyncTask<Void, Void, Void> {
        JSONObject jo;
        HttpClient client;
        String responseBody;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mReferralcode = mBranchpostId;
        }

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
                SSLSocketFactory sf = new SSLSocketFactory(SSLContext.getDefault(),
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
                httppost.setHeader("Authorization", "Basic " + authToken);

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
                    if (jo.has("code") && !jo.isNull("code")) {
                        int code = jo.getInt("code");
                        if (code == AppUrl.INVALID_AUTH_CODE) {
                            Methods.logOutUser(Splash.this);
                        }
                    }
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
                                    addDialog(latestVersion, false);
                                } else {
                                    if (sesstionManager.isUserLoggedIn()) {
                                        /*SharedPreferences sharedPref = getSharedPreferences("Branchid", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPref.edit();
                                        mReferralcode = sharedPref.getString("Branchid", "");*/
                                        if (refer_user_mobile_number != null && refer_user_mobile_number.length() > 0) {
                                            branch.setIdentity(refer_user_mobile_number);
                                        }
                                        if (mReferralcode != null && mReferralcode.length() > 0) {
                                            Intent intent = new Intent(Splash.this, PostDetailActivity.class);
                                            intent.putExtra("feedId", mReferralcode);
                                            startActivity(intent);
                                            mBranchpostId = "";
                                        } else if (user_fromLink != null && user_fromLink.length() > 0) {
                                            Intent intent = new Intent(Splash.this, CommunitiesDetails.class);
                                            intent.putExtra("when_refer_community", 1);
                                            intent.putExtra("user_fromLink", 1);
                                            intent.putExtra("groupId", refer_user_groupId);
                                            intent.putExtra("refer_community_name", refer_community_name);
                                            intent.putExtra("refer_userId", refer_user_userId);
                                            startActivity(intent);
                                        } else {
                                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                            finish();
                                        }
                                    } else {
                                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                        intent.putExtra("mReferralcode", mReferralcode);
                                        startActivity(intent);
                                        finish();
                                    }
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
                if (sesstionManager.isUserLoggedIn()) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                }
                dialogPopUp.cancel();

                finish();
            }
        });

        final ProgressBar progressBar = (ProgressBar) dialogPopUp.findViewById(R.id.scroll);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.app.nirogstreet"));
                startActivity(intent);
            }
        });
        dialogPopUp.show();

    }

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onStart() {
        super.onStart();


    }
}
