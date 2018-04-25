package com.app.nirogstreet.BharamTool;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.nirogstreet.R;
import com.app.nirogstreet.circularprogressbar.CircularProgressBar;
import com.app.nirogstreet.uttil.AppUrl;
import com.app.nirogstreet.uttil.NetworkUtill;
import com.app.nirogstreet.uttil.Query_Method;
import com.app.nirogstreet.uttil.SesstionManager;

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

public class Classical_Medical_Sub_Cat_Detail extends AppCompatActivity {
    CircularProgressBar mCircularProgressBar;
    private SesstionManager session;
    String res;
    ImageView backImageView;
    String authToken, userId;
    String mimeType = "text/html";

    String encoding = "utf-8";
    TextView title_side_left, mDose_descrptn_txt, mIndication_descrptn_txt, mAction_descrptn_txt,
            mContraindications_descrptn_txt, mAnupan_descrptn_txt;
    LinearLayout mParent_layout, mDose_layout, mIndiction_layout, mAction_layout, mContraindiction_layout, mAnupan_layout;
TextView dose_name,indication_name,action_name,contraindications_name,anupan_name;
    WebView webView;
    String mSwarnbhoota_cat_id;
    View dose_view, indication_view, contadiction_view, action_view, anupan_view;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.classical_sub_sub_cat_detail);
        backImageView = (ImageView) findViewById(R.id.back);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mSwarnbhoota_cat_id = getIntent().getStringExtra("sub_cat_id");
        dose_name=(TextView)findViewById(R.id.dose_name);
        indication_name=(TextView)findViewById(R.id.indication_name);
        action_name=(TextView)findViewById(R.id.action_name);
        contraindications_name=(TextView)findViewById(R.id.contraindications_name);
        anupan_name=(TextView)findViewById(R.id.anupan_name);
        title_side_left = (TextView) findViewById(R.id.title_side_left);
        dose_view = (View) findViewById(R.id.dose_view);
        indication_view = (View) findViewById(R.id.indication_view);
        action_view = (View) findViewById(R.id.action_view);
        contadiction_view = (View) findViewById(R.id.contadiction_view);
        anupan_view = (View) findViewById(R.id.anupan_view);

        String title;
        if (getIntent().hasExtra("name")) {
            title = getIntent().getStringExtra("name");
            title_side_left.setText(title);

        }
        mCircularProgressBar = (CircularProgressBar) findViewById(R.id.circularProgressBar);
        mIndiction_layout = (LinearLayout) findViewById(R.id.indiction_layout);
        mDose_layout = (LinearLayout) findViewById(R.id.dose_layout);
        mAction_layout = (LinearLayout) findViewById(R.id.action_layout);
        mContraindiction_layout = (LinearLayout) findViewById(R.id.contraindiction_layout);
        mAnupan_layout = (LinearLayout) findViewById(R.id.anupan_layout);

        mDose_descrptn_txt = (TextView) findViewById(R.id.dose_descrptn_txt);
        mIndication_descrptn_txt = (TextView) findViewById(R.id.indication_descrptn_txt);
        mAnupan_descrptn_txt = (TextView) findViewById(R.id.anupan_descrptn_txt);
        mContraindications_descrptn_txt = (TextView) findViewById(R.id.contraindications_descrptn_txt);
        mAction_descrptn_txt = (TextView) findViewById(R.id.action_descrptn_txt);
        mParent_layout = (LinearLayout) findViewById(R.id.parent_layout);
        session = new SesstionManager(Classical_Medical_Sub_Cat_Detail.this);
        if (session.isUserLoggedIn()) {
            HashMap<String, String> userDetail = session.getUserDetails();
            authToken = userDetail.get(SesstionManager.AUTH_TOKEN);
            userId = userDetail.get(SesstionManager.USER_ID);
        }
        webView = (WebView) findViewById(R.id.webview1);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        WebSettings settings = webView.getSettings();
        settings.setBuiltInZoomControls(false);
        settings.setSupportZoom(true);
        if (NetworkUtill.isNetworkAvailable(Classical_Medical_Sub_Cat_Detail.this)) {
            new Asyank_Listing().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            NetworkUtill.showNoInternetDialog(Classical_Medical_Sub_Cat_Detail.this);
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
                url = AppUrl.BaseUrl + "knowledge/sub-medical-detail";
                ContentValues values = new ContentValues();
                values.put("appID", "NRGSRT$(T(L5830FRU@!^AUSER");
                values.put("sub_type_id", mSwarnbhoota_cat_id);
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

        @RequiresApi(api = Build.VERSION_CODES.N)
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
                        JSONObject joj = jo.getJSONObject("response").getJSONObject("detail");
                        String des = joj.getString("detail");
                        if (des != null && des.length() > 0) {
                            webView.setVisibility(View.VISIBLE);
                            webView.loadDataWithBaseURL("fake://not/needed", des, mimeType, encoding, "");
                        } else {
                            webView.setVisibility(View.GONE);
                        }
                        if (joj.has("ingredients") && joj.getString("ingredients") != null && joj.getString("ingredients").length() > 0) {
                            dose_name.setText("Ingredients");
                            dose_view.setVisibility(View.VISIBLE);
                            mDose_layout.setVisibility(View.VISIBLE);
                            mDose_descrptn_txt.setText(Html.fromHtml(joj.getString("ingredients")));
                        } else {
                            mDose_layout.setVisibility(View.GONE);
                            dose_view.setVisibility(View.GONE);
                        }

                        if (joj.has("properties") && joj.getString("properties") != null && joj.getString("properties").length() > 0) {
                            indication_name.setText("Uses & Property");
                            indication_view.setVisibility(View.VISIBLE);
                            mIndiction_layout.setVisibility(View.VISIBLE);
                            mIndication_descrptn_txt.setText(joj.getString("properties"));
                        } else {
                            mIndiction_layout.setVisibility(View.GONE);
                            indication_view.setVisibility(View.GONE);
                        }
                        if (joj.has("anupana") && joj.getString("anupana") != null && joj.getString("anupana").length() > 0) {
                            action_name.setText("Doses & Anupan ");
                            anupan_view.setVisibility(View.VISIBLE);
                            mAnupan_layout.setVisibility(View.VISIBLE);
                            mAction_descrptn_txt.setText(joj.getString("anupana"));
                        } else {
                            mAnupan_layout.setVisibility(View.GONE);
                            anupan_view.setVisibility(View.GONE);
                        }
                        if (joj.has("product_references") && joj.getString("product_references") != null && joj.getString("product_references").length() > 0) {
                            anupan_name.setText("References");
                            action_view.setVisibility(View.VISIBLE);
                            mAction_layout.setVisibility(View.VISIBLE);
                            mAnupan_descrptn_txt.setText(joj.getString("product_references"));
                        } else {
                            mAction_layout.setVisibility(View.GONE);
                            action_view.setVisibility(View.GONE);
                        }

                        if (joj.has("contraindications") && joj.getString("contraindications") != null && joj.getString("contraindications").length() > 0) {
                            contadiction_view.setVisibility(View.VISIBLE);
                            mContraindiction_layout.setVisibility(View.VISIBLE);
                            mContraindications_descrptn_txt.setText(joj.getString("contraindications"));
                        } else {
                            contadiction_view.setVisibility(View.GONE);
                            mContraindiction_layout.setVisibility(View.GONE);
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
