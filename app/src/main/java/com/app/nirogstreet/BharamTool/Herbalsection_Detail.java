package com.app.nirogstreet.BharamTool;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.nirogstreet.R;
import com.app.nirogstreet.activites.ViewMore_Data;
import com.app.nirogstreet.circularprogressbar.CircularProgressBar;
import com.app.nirogstreet.uttil.AppUrl;
import com.app.nirogstreet.uttil.NetworkUtill;
import com.app.nirogstreet.uttil.Query_Method;
import com.app.nirogstreet.uttil.SesstionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

/**
 * Created by as on 4/21/2018.
 */

public class Herbalsection_Detail extends AppCompatActivity {
    CircularProgressBar mCircularProgressBar;
    private SesstionManager session;
    String res;
    String authToken, userId;
    int mDiseassub_cat_id;
    TextView mTitle_txt, mShoola_of_txt, title_side_left;
    ImageView backImageView, herbsimage;
    WebView webView;
    LinearLayout parent_layout;

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.herbal_section_detail);
        backImageView = (ImageView) findViewById(R.id.back);
        title_side_left = (TextView) findViewById(R.id.title_side_left);
        parent_layout = (LinearLayout) findViewById(R.id.parent_layout);
        mCircularProgressBar = (CircularProgressBar) findViewById(R.id.circularProgressBar);
        if (getIntent().hasExtra("name")) {
            title_side_left.setText(getIntent().getStringExtra("name"));
        }
        if (getIntent().hasExtra("herbs_id")) {
            mDiseassub_cat_id = getIntent().getIntExtra("herbs_id", 0);
        }
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTitle_txt = (TextView) findViewById(R.id.title_txt);
        herbsimage = (ImageView) findViewById(R.id.imag);
        webView = (WebView) findViewById(R.id.descrptn_webview);
        session = new SesstionManager(Herbalsection_Detail.this);
        if (session.isUserLoggedIn()) {
            HashMap<String, String> userDetail = session.getUserDetails();
            authToken = userDetail.get(SesstionManager.AUTH_TOKEN);
            userId = userDetail.get(SesstionManager.USER_ID);
        }
        if (NetworkUtill.isNetworkAvailable(Herbalsection_Detail.this)) {
            new Asyank_Listing().execute();
        } else {
            NetworkUtill.showNoInternetDialog(Herbalsection_Detail.this);
        }


    }

    class Asyank_Listing extends AsyncTask<Void, Void, Void> {
        int result = 0;
        int code = 0;
        JSONObject jo;
        HttpURLConnection httpURLConnection;
        private String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mCircularProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                String url = "";
                url = AppUrl.BaseUrl + "knowledge/herb-detail";
                ContentValues values = new ContentValues();
                values.put("appID", AppUrl.APP_ID_VALUE_POST);
                values.put("herb_id", mDiseassub_cat_id);
                URL uri = new URL(url);
                httpURLConnection = (HttpURLConnection) uri.openConnection();
                httpURLConnection.addRequestProperty("Authorization", "Basic " + authToken);
                httpURLConnection.setReadTimeout(10000);
                httpURLConnection.setConnectTimeout(15000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(Query_Method.getQuery(values));
                writer.flush();
                writer.close();
                os.close();
                httpURLConnection.connect();
                BufferedReader br = new BufferedReader(new InputStreamReader((httpURLConnection.getInputStream())));
                StringBuilder sb = new StringBuilder();
                String output;
                while ((output = br.readLine()) != null) {
                    sb.append(output);
                }
                res = sb.toString();
                System.out.println("My Response :: " + res.toString());
                jo = new JSONObject(res);
                result = jo.getJSONObject("response").getInt("error");
                // code = jo.getInt("code");

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void r) {
            super.onPostExecute(r);
           /* if (code == 200) {*/
            mCircularProgressBar.setVisibility(View.GONE);
            if (result == 0) {
                parent_layout.setVisibility(View.VISIBLE);
                mCircularProgressBar.setVisibility(View.GONE);
                try {
                    if (jo != null && jo.length() > 0) {
                        JSONObject joj = jo.getJSONObject("response").getJSONObject("detail");
                        String image = joj.getString("herb_img");
                        String engname = joj.getString("eng_name");
                        String hindiname = joj.getString("hindi_name");
                        String description = joj.getString("description");

                        String name = engname + " (" + hindiname + ")";
                        if (name != null && name.length() > 0) {
                            mTitle_txt.setText(name);
                        }
                        if (image != null && image.length() > 0) {
                            Picasso.with(Herbalsection_Detail.this)
                                    .load(image)
                                    .placeholder(R.drawable.user)
                                    .error(R.drawable.user)
                                    .into(herbsimage);
                        }

                        webView.loadDataWithBaseURL(null, description, "text/html", "UTF-8", null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                parent_layout.setVisibility(View.GONE);
            }

        }

        public void cancelAsyncTask() {
            if (httpURLConnection != null)
                httpURLConnection.disconnect();
        }
    }


}
