package com.app.nirogstreet.activites;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.nirogstreet.R;
import com.app.nirogstreet.circularprogressbar.CircularProgressBar;
import com.app.nirogstreet.uttil.AppUrl;
import com.app.nirogstreet.uttil.ApplicationSingleton;
import com.app.nirogstreet.uttil.NetworkUtill;
import com.app.nirogstreet.uttil.SesstionManager;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
 * Created by Preeti on 22-02-2018.
 */

public class EditCommentActivity extends Activity {
    SesstionManager sessionManager;
    EditText editTextMessage;
    ImageView backImageView;
    boolean is_sub_comment = false;
    CircularProgressBar circularProgressBar;
    TextView postTextView;
    String type = "";
    EditFeedPostAsyncTak editFeedPostAsyncTak;
    boolean isEdit = false;

    String feedId, commentId;
    String msg = "";
    String comment;
    private String disscustionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_comment);
        if (getIntent().hasExtra("type")) {
            type = getIntent().getStringExtra("type");
        }
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.statusbarcolor));
        }

        sessionManager = new SesstionManager(EditCommentActivity.this);
        editTextMessage = (EditText) findViewById(R.id.editTextMessage);
        postTextView = (TextView) findViewById(R.id.post);
        backImageView = (ImageView) findViewById(R.id.back);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        circularProgressBar = (CircularProgressBar) findViewById(R.id.circularProgressBar);
        Intent intent = getIntent();
        if (intent.hasExtra("commentId")) {
            commentId = intent.getStringExtra("commentId");
        }
        if (getIntent().hasExtra("is_sub_comment")) {
            is_sub_comment = getIntent().getBooleanExtra("is_sub_comment", false);
        }
        if (intent.hasExtra("msg")) {
            msg = intent.getStringExtra("msg");
        }
        if (intent.hasExtra("feedId")) {
            feedId = intent.getStringExtra("feedId");
        }
        editTextMessage.setText(msg);
        postTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkUtill.isNetworkAvailable(EditCommentActivity.this)) {
                    if (editTextMessage.getText().toString() != null && editTextMessage.getText().toString().length() > 0) {
                        editFeedPostAsyncTak = new EditFeedPostAsyncTak(feedId, sessionManager.getUserDetails().get(SesstionManager.USER_ID), sessionManager.getUserDetails().get(SesstionManager.AUTH_TOKEN));
                        editFeedPostAsyncTak.execute();
                    } else {
                        Toast.makeText(EditCommentActivity.this, "Enter some text", Toast.LENGTH_LONG).show();
                    }
                } else {
                    NetworkUtill.showNoInternetDialog(EditCommentActivity.this);
                }
            }
        });

    }

    public class EditFeedPostAsyncTak extends AsyncTask<Void, Void, Void> {
        String authToken;
        JSONObject jo;
        String feedId, userId;
        int pos;
        String text;
        private String responseBody;
        HttpClient client;
        Context context;

        public void cancelAsyncTask() {
            if (client != null && !isCancelled()) {
                cancel(true);
                client = null;
            }
        }

        public EditFeedPostAsyncTak(String feedId, String userId, String authToken) {
            this.feedId = feedId;
            this.authToken = authToken;
            this.pos = pos;
            this.userId = userId;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            circularProgressBar.setVisibility(View.GONE);
            super.onPostExecute(aVoid);
            try {
                if (jo != null) {
                    if (jo.has("responce") && !jo.isNull("responce")) {
                        JSONObject jsonObject = jo.getJSONObject("responce");
                        if (jsonObject.has("message") && !jsonObject.isNull("message")) {
                            Toast.makeText(EditCommentActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                            // ApplicationSingleton.setEditFeedPostExecuted(true);
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


                String url = AppUrl.BaseUrl + "feed/comment";
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
                pairs.add(new BasicNameValuePair("userID", userId));
                if (is_sub_comment)
                    pairs.add(new BasicNameValuePair("subcommentID", commentId));
                else
                    pairs.add(new BasicNameValuePair("commentID", commentId));
                pairs.add(new BasicNameValuePair("feedID", feedId));
                pairs.add(new BasicNameValuePair("message", text));
                pairs.add(new BasicNameValuePair("post_type", type));
                pairs.add(new BasicNameValuePair("show_comment", "1"));

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
            try {
                text = URLEncoder.encode(editTextMessage.getText().toString(), "UTF-8");


            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
    }

}
