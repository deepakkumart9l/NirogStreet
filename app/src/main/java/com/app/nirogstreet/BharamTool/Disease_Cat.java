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
import android.widget.Toast;

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
 * Created by as on 2/22/2018.
 */

public class Disease_Cat extends AppCompatActivity implements OnItemClickListeners {
    RecyclerView mRecyclerView;
    RecyclerView mTypelist;
    private List<Bharam_Model> listing_models;
    String res;
    BharamTool_Adapter adapter;
    ImageView backImageView;
    int productquantity;
    int mType, diseas;
    private SesstionManager session;
    CircularProgressBar mCircularProgressBar;
    String authToken, userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swarnbhoota_yog_type);
        backImageView=(ImageView)findViewById(R.id.back) ;
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mCircularProgressBar = (CircularProgressBar) findViewById(R.id.circularProgressBar);
        mRecyclerView = (RecyclerView) findViewById(R.id.typelist);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(Disease_Cat.this);
        mRecyclerView.setLayoutManager(llm);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setNestedScrollingEnabled(false);
        session = new SesstionManager(Disease_Cat.this);
        if (session.isUserLoggedIn()) {
            HashMap<String, String> userDetail = session.getUserDetails();
            authToken = userDetail.get(SesstionManager.AUTH_TOKEN);
            userId = userDetail.get(SesstionManager.USER_ID);
        }
        if (NetworkUtill.isNetworkAvailable(Disease_Cat.this)) {
            new Asyank_Listing().execute();
        } else {
            NetworkUtill.showNoInternetDialog(Disease_Cat.this);
        }
    }

    @Override
    public void onItemClick(String v, int id,String total) {
        // Toast.makeText(Disease_Cat.this, "Position " + position, Toast.LENGTH_SHORT).show();
        Intent newIntent = new Intent(Disease_Cat.this, Disease_Sub_Cat.class);
        newIntent.putExtra("diseas_id",id);
        startActivity(newIntent);

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
                url = AppUrl.BaseUrl + "knowledge/disease-list";
                ContentValues values = new ContentValues();
                values.put("appID", "NRGSRT$(T(L5830FRU@!^AUSER");
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
                    adapter = new BharamTool_Adapter(Disease_Cat.this, listing_models);
                    mRecyclerView.setAdapter(adapter);
                    adapter.setListener(Disease_Cat.this);
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
                item.setId(feedObj.getInt("id"));
                item.setName(feedObj.getString("name"));

                listing_models.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
