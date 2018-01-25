package com.app.nirogstreet.activites;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.nirogstreet.R;
import com.app.nirogstreet.circularprogressbar.CircularProgressBar;
import com.app.nirogstreet.model.MultipleSelectedItemModel;
import com.app.nirogstreet.uttil.AppUrl;
import com.app.nirogstreet.uttil.ApplicationSingleton;
import com.app.nirogstreet.uttil.NetworkUtill;

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
 * Created by Preeti on 22-12-2017.
 */

public class PublicShare extends Activity {
    TextView textViewshare, textViewmsg;
    TextView friendstextCsv;
ShareOnFriendsTimeLineAsyncTask shareOnFriendsTimeLineAsyncTask;
    ArrayList<MultipleSelectedItemModel> multipleSelectedItemModels = new ArrayList<>();
    private static final int RESULT_CODE = 1;
    private ImageView backImageView;
    CircularProgressBar circularProgressBar;
    boolean shareOnGroup = false;
    String parnetFeedId="";
    TextView group_friends;
    private boolean imagedlt = false;
    TextView textTab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.public_share);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.statusbarcolor));
        }
        textTab = (TextView) findViewById(R.id.textTab);
        textViewmsg = (TextView) findViewById(R.id.msg);
        Intent intent = getIntent();
        circularProgressBar = (CircularProgressBar) findViewById(R.id.circularProgressBar);
        textViewshare = (TextView) findViewById(R.id.share);
        if (intent.hasExtra("shareOnGroup")) {
            shareOnGroup = intent.getBooleanExtra("shareOnGroup", false);
            if (shareOnGroup) {
                textTab.setText("Share in community ");
            }
        }
        if(intent.hasExtra("parentFeedId"))
        {
            parnetFeedId=intent.getStringExtra("parentFeedId");
        }
        final String userId = intent.getStringExtra("userId");
        final String feedId = intent.getStringExtra("feedId");
        textViewshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkUtill.isNetworkAvailable(PublicShare.this)) {

                        shareOnFriendsTimeLineAsyncTask = new ShareOnFriendsTimeLineAsyncTask(userId, feedId);
                        shareOnFriendsTimeLineAsyncTask.execute();
                    } else {
                        Toast.makeText(PublicShare.this, "Select friends", Toast.LENGTH_LONG).show();
                    }

            }
        });
        friendstextCsv = (TextView) findViewById(R.id.textCsv);
        backImageView = (ImageView) findViewById(R.id.back);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
          }
    public class ShareOnFriendsTimeLineAsyncTask extends AsyncTask<Void, Void, Void> {
        String responseBody;
        String email, password;
        CircularProgressBar bar;
        //PlayServiceHelper regId;
        String feedId, Userid;
        JSONObject jo;
        String shareMsg;
        HttpClient client;

        public ShareOnFriendsTimeLineAsyncTask(String userId, String feedId) {
            this.Userid = userId;
            this.feedId = feedId;
            shareMsg = textViewmsg.getText().toString();
        }

        public void cancelAsyncTask() {
            if (client != null && !isCancelled()) {
                cancel(true);
                client = null;
            }
        }

        @Override
        protected void onPreExecute() {
            circularProgressBar.setVisibility(View.VISIBLE);

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                String url = AppUrl.BaseUrl + "feed/share";
                SSLSocketFactory sf = new SSLSocketFactory(
                        SSLContext.getDefault(),
                        SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                Scheme sch = new Scheme("https", 443, sf);
                client = new DefaultHttpClient();

                client.getConnectionManager().getSchemeRegistry().register(sch);
                HttpPost httppost = new HttpPost(url);
                HttpResponse response;

                String credentials = email + ":" + password;

                List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                pairs.add(new BasicNameValuePair(AppUrl.APP_ID_PARAM, AppUrl.APP_ID_VALUE_POST));

                pairs.add(new BasicNameValuePair("userID", Userid));
                if (!parnetFeedId.equalsIgnoreCase(""))
                    pairs.add(new BasicNameValuePair("feedID", parnetFeedId));
                else
                pairs.add(new BasicNameValuePair("feedID", feedId));

                    pairs.add(new BasicNameValuePair("shareType", "1"));

                if (shareMsg != null) {
                    pairs.add(new BasicNameValuePair("shareMessage", shareMsg));
                }
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
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            circularProgressBar.setVisibility(View.GONE);
            try {
                if (jo != null) {
                    if (jo.has("status") && !jo.isNull("status")) {
                        if (jo.has("responce") && !jo.isNull("responce")) {
                            JSONObject jsonObjectResponce = jo.getJSONObject("responce");
                            ApplicationSingleton.setIsProfilePostExecuted(true);

                                finish();

                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
