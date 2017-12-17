package com.app.nirogstreet.activites;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.nirogstreet.R;
import com.app.nirogstreet.circularprogressbar.CircularProgressBar;
import com.app.nirogstreet.model.Course_Detail_model;
import com.app.nirogstreet.uttil.AppUrl;
import com.app.nirogstreet.uttil.ApplicationSingleton;
import com.app.nirogstreet.uttil.NetworkUtill;
import com.app.nirogstreet.uttil.SesstionManager;

import org.json.JSONObject;

import java.util.ArrayList;
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

/**
 * Created by Preeti on 13-12-2017.
 */

public class LoadHtmlPageActivity extends Activity {
    WebView web;
    String data;
    SesstionManager sesstionManager;
    String url = null;
    CircularProgressBar circularProgressBar;
    KnwledgeCompleteAsynctask knwledgeCompleteAsynctask;
    String id = null;
    ImageView downloadImageView;
    Course_Detail_model course_detail_model;
    String title = null;
    ImageView backImageView;
    int module_pos, topic_pos, file_pos;
    TextView title_side_left;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_content);
        web=(WebView)findViewById(R.id.web);
        circularProgressBar=(CircularProgressBar)findViewById(R.id.circularProgressBar);
        sesstionManager=new SesstionManager(LoadHtmlPageActivity.this);
        if(getIntent().hasExtra("data"))
        {
            data=getIntent().getStringExtra("data");
        }
        if (getIntent().hasExtra("url")) {
            data = getIntent().getStringExtra("url");
        }

        if (getIntent().hasExtra("course_detail_model")) {
            course_detail_model = (Course_Detail_model) getIntent().getSerializableExtra("course_detail_model");
        }
        backImageView = (ImageView) findViewById(R.id.back);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (getIntent().hasExtra("module_pos")) {
            module_pos = getIntent().getIntExtra("module_pos", -1);
        }
        if (getIntent().hasExtra("topic_pos")) {
            topic_pos = getIntent().getIntExtra("topic_pos", -1);
        }
        if (getIntent().hasExtra("file_pos")) {
            file_pos = getIntent().getIntExtra("file_pos", -1);
        }
        downloadImageView = (ImageView) findViewById(R.id.download);
        downloadImageView.setVisibility(View.GONE);
        title_side_left = (TextView) findViewById(R.id.title_side_left);
        title_side_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        if (getIntent().hasExtra("url")) {
            url = getIntent().getStringExtra("url");
        }
        if (getIntent().hasExtra("title")) {
            title = getIntent().getStringExtra("title");
            title_side_left.setText(title);

        }
        if (getIntent().hasExtra("id")) {
            id = getIntent().getStringExtra("id");
        }
        web.setWebViewClient(new AppWebViewClients());

        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setUseWideViewPort(false);

        web.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        web.loadDataWithBaseURL("", data, "text/html", "UTF-8", "");
    }
    public class AppWebViewClients extends WebViewClient {


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
            circularProgressBar.setVisibility(View.GONE);

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {


                    if (NetworkUtill.isNetworkAvailable(LoadHtmlPageActivity.this)) {
                        knwledgeCompleteAsynctask = new KnwledgeCompleteAsynctask();
                        knwledgeCompleteAsynctask.execute();
                    } else {
                        NetworkUtill.showNoInternetDialog(LoadHtmlPageActivity.this);
                    }
                }
            }, 3000);


        }
    }

    public class KnwledgeCompleteAsynctask extends AsyncTask<Void, Void, Void> {
        String authToken;
        JSONObject jo;
        String groupId, userId;

        int status1;
        HttpClient client;
        int pos;
        private String responseBody;


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
                if (jo != null) {


                    if (jo.has("response") && !jo.isNull("response")) {
                        JSONObject jsonObjectresponse = jo.getJSONObject("response");
                        if (jsonObjectresponse.has("message") && !jsonObjectresponse.isNull("message")) {
                            Toast.makeText(LoadHtmlPageActivity.this, jsonObjectresponse.getString("message"), Toast.LENGTH_LONG).show();
                            // addQualificationTextView.setVisibility(View.GONE);
                            course_detail_model.getModulesModels().get(module_pos).getTopic_under_modules().get(topic_pos).getFile_under_topics().get(file_pos).setUser_completed(1);
                            ApplicationSingleton.setCourse_detail_model(course_detail_model);
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


                String url = AppUrl.BaseUrl + "knowledge/complete";
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
                pairs.add(new BasicNameValuePair("courseID", id));
                pairs.add(new BasicNameValuePair("userID", sesstionManager.getUserDetails().get(SesstionManager.USER_ID) + ""));
                pairs.add(new BasicNameValuePair("status", "1"));
                httppost.setHeader("Authorization", "Basic " + authToken);

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
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

}
