package com.app.nirogstreet.BharamTool;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.nirogstreet.R;
import com.app.nirogstreet.circularprogressbar.CircularProgressBar;
import com.app.nirogstreet.uttil.AppUrl;
import com.app.nirogstreet.uttil.NetworkUtill;
import com.app.nirogstreet.uttil.Query_Method;
import com.app.nirogstreet.uttil.SesstionManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by as on 4/23/2018.
 */

public class Classical_medical_Detail extends AppCompatActivity {
    TextView herbs_txt_name, herbs_descrptn_txt, title_side_left;
    CircularProgressBar mCircularProgressBar;
    String authToken, userId, res;
    LinearLayout mDiseassubcat_layout;
    int mDiseassub_cat_id;
    String lId;
    String name;
    ImageView backImageView;
    private SesstionManager session;
    LinearLayout mParent_layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.classical_medical_detail);
        session = new SesstionManager(Classical_medical_Detail.this);
        if (session.isUserLoggedIn()) {
            HashMap<String, String> userDetail = session.getUserDetails();
            authToken = userDetail.get(SesstionManager.AUTH_TOKEN);
            userId = userDetail.get(SesstionManager.USER_ID);
        }
        backImageView = (ImageView) findViewById(R.id.back);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        name = getIntent().getStringExtra("name");
        mDiseassub_cat_id = getIntent().getIntExtra("herbs_id", 0);
        mDiseassubcat_layout = (LinearLayout) findViewById(R.id.herbs_layout);
        mParent_layout = (LinearLayout) findViewById(R.id.parent_layout);
        mCircularProgressBar = (CircularProgressBar) findViewById(R.id.circularProgressBar);
        herbs_txt_name = (TextView) findViewById(R.id.herbs_txt_name);
        herbs_descrptn_txt = (TextView) findViewById(R.id.herbs_descrptn_txt);
        title_side_left = (TextView) findViewById(R.id.title_side_left);
        title_side_left.setText(name);
        if (NetworkUtill.isNetworkAvailable(Classical_medical_Detail.this)) {
            new Asyank_Listing().execute();
        } else {
            NetworkUtill.showNoInternetDialog(Classical_medical_Detail.this);
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
                url = AppUrl.BaseUrl + "knowledge/medical-detail";
                ContentValues values = new ContentValues();
                values.put("appID", AppUrl.APP_ID_VALUE_POST);
                values.put("product_id", mDiseassub_cat_id);
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
                mCircularProgressBar.setVisibility(View.GONE);
                try {
                    if (jo != null && jo.length() > 0) {
                        mParent_layout.setVisibility(View.VISIBLE);
                        String name = jo.getJSONObject("response").getJSONObject("detail").getString("name");
                        String descrptn = jo.getJSONObject("response").getJSONObject("detail").getString("detail");
                        herbs_txt_name.setText(name);
                        herbs_descrptn_txt.setText(descrptn);
                        JSONArray jsonArray = jo.getJSONObject("response").getJSONArray("product_sub_type");
                        if (jsonArray != null && jsonArray.length() > 0) {
                            mDiseassubcat_layout.setVisibility(View.VISIBLE);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT);
                                LayoutInflater layoutInflater = (LayoutInflater) Classical_medical_Detail.this.getSystemService(
                                        Context.LAYOUT_INFLATER_SERVICE);

                                View view = layoutInflater.inflate(
                                        R.layout.diseas_sub_list_view, null);
                                LinearLayout name_layout = (LinearLayout) view.findViewById(R.id.name_layout);
                                View first_view = (View) view.findViewById(R.id.first_view);
                                View second_view = (View) view.findViewById(R.id.second_view);
                                second_view.setVisibility(View.VISIBLE);
                                first_view.setVisibility(View.GONE);
                                TextView title = (TextView) view.findViewById(R.id.diseas_sub_cat_txt);
                                LinearLayout diseas_sub_layout = (LinearLayout) view.findViewById(R.id.diseas_sub_layout);
                                final String itanaryname = jsonObject.getString("name");
                                lId = jsonObject.getString("id");
                                if (itanaryname != null && itanaryname.length() > 0) {
                                    title.setText(Html.fromHtml(itanaryname));
                                } else {
                                    name_layout.setVisibility(View.GONE);
                                }
                                view.setLayoutParams(params);

                                diseas_sub_layout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(Classical_medical_Detail.this, Classical_Medical_Sub_Cat_Detail.class);
                                        intent.putExtra("sub_cat_id", lId);
                                        intent.putExtra("name", itanaryname);
                                        startActivity(intent);
                                    }
                                });
                                mDiseassubcat_layout.addView(view);
                            }
                        } else {
                            mDiseassubcat_layout.setVisibility(View.GONE);
                        }
                    } else {
                        mParent_layout.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public void cancelAsyncTask() {
            if (httpURLConnection != null)
                httpURLConnection.disconnect();
        }
    }
}
