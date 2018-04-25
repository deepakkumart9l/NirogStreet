package com.app.nirogstreet.activites;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.nirogstreet.R;
import com.app.nirogstreet.circularprogressbar.CircularProgressBar;
import com.app.nirogstreet.uttil.AppUrl;
import com.app.nirogstreet.uttil.ApplicationSingleton;
import com.app.nirogstreet.uttil.Event_For_Firebase;
import com.app.nirogstreet.uttil.SesstionManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
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
import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.SharingHelper;
import io.branch.referral.util.ContentMetadata;
import io.branch.referral.util.LinkProperties;
import io.branch.referral.util.ShareSheetStyle;

/**
 * Created by as on 2/3/2018.
 */

public class ReferalActivity extends AppCompatActivity {

    LinearLayout imgshare;
    TextView txt_share;
    String shareText;
    ImageView backInImageView;
    CircularProgressBar circularProgressBar;
    private SesstionManager sessionManager;
    String userId, authToken;
    TextView referal_point_txt, txt_code;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.referallactivitry);
        Event_For_Firebase.getEventCount(ReferalActivity.this, "Feed_Profile_Refer_Visit");

        imgshare = (LinearLayout) findViewById(R.id.share_ettiquets);
        referal_point_txt = (TextView) findViewById(R.id.referal_point_txt);
        txt_code = (TextView) findViewById(R.id.txt_code);
        txt_share = (TextView) findViewById(R.id.txt_share);
        backInImageView = (ImageView) findViewById(R.id.back);
        sessionManager = new SesstionManager(ReferalActivity.this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        authToken = user.get(SesstionManager.AUTH_TOKEN);
        userId = user.get(SesstionManager.USER_ID);
        circularProgressBar = (CircularProgressBar) findViewById(R.id.circularProgressBar);
        final SesstionManager sesstionManager = new SesstionManager(ReferalActivity.this);
        backInImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        shareText = txt_share.getText().toString();
        imgshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Event_For_Firebase.getEventCount(ReferalActivity.this, "Feed_Profile_Refer_ShareNow_Click");
                String refer_code, usermobilenumber;
                refer_code = ApplicationSingleton.getUserDetailModel().getReferral_code();
                usermobilenumber = ApplicationSingleton.getUserDetailModel().getMobile();

                if (refer_code != null && refer_code.length() > 0)
                    try {
                        String str = "Hey, become a part of the ever-growing community of Ayurveda Doctors along with me.";

                        BranchUniversalObject buo = new BranchUniversalObject()
                                .setCanonicalIdentifier("content/12345")
                                .setTitle(str)
                                .setContentDescription("My Content Description")
                                .setContentImageUrl("https://lorempixel.com/400/400")
                                .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                                //.setLocalIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                                .setContentMetadata(new ContentMetadata().addCustomMetadata("referCode", refer_code));

                        LinkProperties lp = new LinkProperties()
                                .setChannel("facebook")
                                .setFeature("sharing")
                                //.setCampaign("content 123 launch")
                                .setStage("new user")
                                .addControlParameter("$desktop_url", "http://example.com/home")
                                .addControlParameter("refer_code", refer_code)
                                .addControlParameter("refer_user_mobile_number", usermobilenumber)
                                .addControlParameter("custom_random", Long.toString(Calendar.getInstance().getTimeInMillis()));
                        buo.generateShortUrl(ReferalActivity.this, lp, new Branch.BranchLinkCreateListener() {
                            @Override
                            public void onLinkCreate(String url, BranchError error) {
                                if (error == null) {
                                    Log.i("BRANCH SDK", "got my Branch link to share: " + url);
                                }
                            }
                        });
                        ShareSheetStyle ss = new ShareSheetStyle(ReferalActivity.this, "Check this out!", "Sign up with my code " + '"' + refer_code + '"' + " & get started. Download:")
                                .setCopyUrlStyle(ContextCompat.getDrawable(ReferalActivity.this, android.R.drawable.ic_menu_send), "Copy", "Added to clipboard")
                                .setMoreOptionStyle(ContextCompat.getDrawable(ReferalActivity.this, android.R.drawable.ic_menu_search), "Show more")
                                .addPreferredSharingOption(SharingHelper.SHARE_WITH.WHATS_APP)
                                .addPreferredSharingOption(SharingHelper.SHARE_WITH.FACEBOOK)
                                .addPreferredSharingOption(SharingHelper.SHARE_WITH.EMAIL)
                                .addPreferredSharingOption(SharingHelper.SHARE_WITH.MESSAGE)
                                .setAsFullWidthStyle(true)
                                .setSharingTitle("Share With")
                                .addPreferredSharingOption(SharingHelper.SHARE_WITH.HANGOUT);


                        buo.showShareSheet(ReferalActivity.this, lp, ss, new Branch.BranchLinkShareListener() {
                            @Override
                            public void onShareLinkDialogLaunched() {
                            }

                            @Override
                            public void onShareLinkDialogDismissed() {
                            }

                            @Override
                            public void onLinkShareResponse(String sharedLink, String sharedChannel, BranchError error) {
                                Log.e("sharelink", "" + sharedLink);
                            }

                            @Override
                            public void onChannelSelected(String channelName) {
                            }
                        });
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                    }
            }
        });
    }

    public class ReferUser_Details extends AsyncTask<Void, Void, Void> {
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

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                circularProgressBar.setVisibility(View.GONE);
                if (jo != null) {
                    int referals_point = jo.getJSONObject("response").getJSONObject("userDetail").getInt("referral_points");
                    String referal_code = jo.getJSONObject("response").getJSONObject("userDetail").getString("referral_code");
                    referal_point_txt.setText("" + referals_point);
                    txt_code.setText(referal_code);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                String url = AppUrl.AppBaseUrl + "user/referral-points";
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
                pairs.add(new BasicNameValuePair("userID", userId));
                httppost.setHeader("Authorization", "Basic " + authToken);
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
            circularProgressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        new ReferUser_Details().execute();
    }
}
