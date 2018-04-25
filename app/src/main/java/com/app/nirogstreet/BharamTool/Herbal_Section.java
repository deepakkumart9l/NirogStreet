package com.app.nirogstreet.BharamTool;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.nirogstreet.R;
import com.app.nirogstreet.circularprogressbar.CircularProgressBar;
import com.app.nirogstreet.listeners.OnItemClickListeners;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by as on 4/21/2018.
 */

public class Herbal_Section extends AppCompatActivity implements OnItemClickListeners {
    RecyclerView mRecyclerView;
    RecyclerView mTypelist;
    private List<Bharam_Model> listing_models;
    String res;
    TextView title_sideTextView;
    Herbalsection_Adapter adapter;
    ImageView backImageView;
    int productquantity;
    int mType, diseas;
    private SesstionManager session;
    CircularProgressBar mCircularProgressBar;
    String authToken, userId;
    int classical;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swarnbhoota_yog_type);
        if (getIntent().hasExtra("classical")) {
            classical = getIntent().getIntExtra("classical", 0);
        }
        title_sideTextView = (TextView) findViewById(R.id.title_side_left);
        if (classical == 3) {
            title_sideTextView.setText("Clasical Medical Preparation");
        } else {
            title_sideTextView.setText("Herbs Section");
        }
        backImageView = (ImageView) findViewById(R.id.back);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mCircularProgressBar = (CircularProgressBar) findViewById(R.id.circularProgressBar);
        mRecyclerView = (RecyclerView) findViewById(R.id.typelist);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(Herbal_Section.this);
        mRecyclerView.setLayoutManager(llm);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setNestedScrollingEnabled(false);
        session = new SesstionManager(Herbal_Section.this);
        if (session.isUserLoggedIn()) {
            HashMap<String, String> userDetail = session.getUserDetails();
            authToken = userDetail.get(SesstionManager.AUTH_TOKEN);
            userId = userDetail.get(SesstionManager.USER_ID);
        }
        if (NetworkUtill.isNetworkAvailable(Herbal_Section.this)) {
            new Asyank_Listing().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            NetworkUtill.showNoInternetDialog(Herbal_Section.this);
        }
    }

    @Override
    public void onItemClick(String v, int id, String total, String name) {
        // Toast.makeText(Disease_Cat.this, "Position " + position, Toast.LENGTH_SHORT).show();
        if (classical == 3) {
            Intent newIntent = new Intent(Herbal_Section.this, Classical_medical_Detail.class);
            newIntent.putExtra("herbs_id", id);
            newIntent.putExtra("name", name);
            startActivity(newIntent);
        } else {
            Intent newIntent = new Intent(Herbal_Section.this, Herbalsection_Detail.class);
            newIntent.putExtra("herbs_id", id);
            newIntent.putExtra("name", name);
            startActivity(newIntent);
        }
    }

    class Asyank_Listing extends AsyncTask<Void, Void, Void> {
        int result = 0;
        JSONObject jo;
        HttpURLConnection httpURLConnection;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mCircularProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                String url = "";
                if (classical == 3) {
                    url = AppUrl.BaseUrl + "knowledge/medical-list";
                } else {
                    url = AppUrl.BaseUrl + "knowledge/herb-list";
                }
                ContentValues values = new ContentValues();
                values.put("appID", "NRGSRT$(T(L5830FRU@!^AUSER");
                values.put("pageNo", "1");
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
                listing_models = null;
                listing_models = new ArrayList<Bharam_Model>();
                parseJsonFeed(jo);
                if (listing_models != null && listing_models.size() > 0) {
                    adapter = new Herbalsection_Adapter(Herbal_Section.this, listing_models, classical);
                    mRecyclerView.setAdapter(adapter);
                    adapter.setListener(Herbal_Section.this);
                }
            }
        }

        public void cancelAsyncTask() {
            if (httpURLConnection != null)
                httpURLConnection.disconnect();
        }

    }

    private void parseJsonFeed(JSONObject response) {
        try {
            listing_models.clear();
            JSONArray feedArray = response.getJSONObject("response").getJSONArray("name");
            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);
                Bharam_Model item = new Bharam_Model();
                if (feedObj.has("id")) {
                    item.setId(feedObj.getInt("id"));
                }
                if (feedObj.has("name")) {
                    item.setName(feedObj.getString("name"));
                }
                if (feedObj.has("hindi_name")) {
                    item.setHindiname(feedObj.getString("hindi_name"));
                }
                if (feedObj.has("eng_name")) {
                    item.setEngname(feedObj.getString("eng_name"));
                }
                if (feedObj.has("herb_img")) {
                    item.setThumbnail(feedObj.getString("herb_img"));
                }

                listing_models.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
