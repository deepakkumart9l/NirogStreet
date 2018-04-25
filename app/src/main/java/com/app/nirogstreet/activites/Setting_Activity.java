package com.app.nirogstreet.activites;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.nirogstreet.R;
import com.app.nirogstreet.uttil.AppUrl;
import com.app.nirogstreet.uttil.NetworkUtill;
import com.app.nirogstreet.uttil.SesstionManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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

/**
 * Created by as on 4/19/2018.
 */

public class Setting_Activity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout lay_referto_friend, lay_privacypolicy, lay_termsof_use, lay_aboutus, lay_changepass, lay_feedback, lay_logout;
    TextView title_side_left;
    private SesstionManager sesstionManager;
    LogoutAsyncTask logoutAsyncTask;
    ImageView back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);
        sesstionManager = new SesstionManager(Setting_Activity.this);
        title_side_left = (TextView) findViewById(R.id.title_side_left);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title_side_left.setTextColor(Color.parseColor("#ffffff"));
        title_side_left.setText("Setting");

        lay_referto_friend = (LinearLayout) findViewById(R.id.lay_referto_friend);
        lay_referto_friend.setOnClickListener(this);
        lay_privacypolicy = (LinearLayout) findViewById(R.id.lay_privacypolicy);
        lay_privacypolicy.setOnClickListener(this);
        lay_termsof_use = (LinearLayout) findViewById(R.id.lay_termsof_use);
        lay_termsof_use.setOnClickListener(this);
        lay_aboutus = (LinearLayout) findViewById(R.id.lay_aboutus);
        lay_aboutus.setOnClickListener(this);
        lay_changepass = (LinearLayout) findViewById(R.id.lay_changepass);
        lay_changepass.setOnClickListener(this);
        lay_feedback = (LinearLayout) findViewById(R.id.lay_feedback);
        lay_feedback.setOnClickListener(this);
        lay_logout = (LinearLayout) findViewById(R.id.lay_logout);
        lay_logout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lay_referto_friend:
                Intent intent3 = new Intent(Setting_Activity.this, ReferalActivity.class);
                startActivity(intent3);
                break;
            case R.id.lay_privacypolicy:
                Intent intent2 = new Intent(Setting_Activity.this, OpenDocument.class);
                intent2.putExtra("setting_url", "https://nirogstreet.com/site/privacy-policy");
                intent2.putExtra("setting_section", 2);
                startActivity(intent2);
                break;
            case R.id.lay_termsof_use:
                Intent intent1 = new Intent(Setting_Activity.this, OpenDocument.class);
                intent1.putExtra("setting_url", "https://nirogstreet.com/site/terms-of-services");
                intent1.putExtra("setting_section", 2);
                startActivity(intent1);
                break;
            case R.id.lay_aboutus:
                Intent intent = new Intent(Setting_Activity.this, OpenDocument.class);
                intent.putExtra("setting_url", "https://nirogstreet.com/site/about-us");
                intent.putExtra("setting_section", 2);
                startActivity(intent);
                break;
            case R.id.lay_changepass:
                Intent intent4 = new Intent(Setting_Activity.this, Change_Password.class);
                startActivity(intent4);
                break;
            case R.id.lay_feedback:
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO,
                        Uri.fromParts("mailto", "info@nirogstreet.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback|NirogStreet");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
                break;
            case R.id.lay_logout:
                shDialog(getString(R.string.want_to_logout));

                break;
        }
    }

    public class LogoutAsyncTask extends AsyncTask<Void, Void, Void> {
        JSONObject jo;
        String feedId;


        private String responseBody;
        HttpClient client;
        Context context;

        public void cancelAsyncTask() {
            if (client != null && !isCancelled()) {
                cancel(true);
                client = null;
            }
        }

        public LogoutAsyncTask(String feedId) {
            this.feedId = feedId;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                if (jo != null) {
                    if (jo.has("status") && !jo.isNull("status")) {
                        boolean status = jo.getBoolean("status");
                        if (status) {
                            sesstionManager.logoutUser();
                            sesstionManager.languageLogOut();
                            Intent intent = new Intent(Setting_Activity.this, LoginActivity.class);
                            intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                String url = AppUrl.AppBaseUrl + "user/logout";
                SSLSocketFactory sf = new SSLSocketFactory(
                        SSLContext.getDefault(),
                        SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                client = new DefaultHttpClient();
                Scheme sch = new Scheme("https", 443, sf);
                client.getConnectionManager().getSchemeRegistry().register(sch);
                HttpPost httppost = new HttpPost(url);
                HttpResponse response;
                List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                pairs.add(new BasicNameValuePair(AppUrl.APP_ID_PARAM, AppUrl.APP_ID_VALUE_POST));
                httppost.setHeader("authorization", "Nirogstreet " + sesstionManager.getUserDetails().get(SesstionManager.AUTH_TOKEN));

                httppost.setEntity(new UrlEncodedFormEntity(pairs));
                response = client.execute(httppost);

                responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");
                jo = new JSONObject(responseBody);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

    public void shDialog(String str) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(Setting_Activity.this);
        builder1.setTitle(getString(R.string.want_to_logout));
        builder1.setCancelable(true);
        builder1.setPositiveButton(getString(R.string.logout),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (NetworkUtill.isNetworkAvailable(Setting_Activity.this)) {
                            logoutAsyncTask = new LogoutAsyncTask("");
                            logoutAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        } else {
                            NetworkUtill.showNoInternetDialog(Setting_Activity.this);
                        }
                        dialog.cancel();
                    }
                });
        builder1.setNegativeButton(getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
