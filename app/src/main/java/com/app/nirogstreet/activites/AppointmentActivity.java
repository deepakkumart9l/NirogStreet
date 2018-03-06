package com.app.nirogstreet.activites;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.nirogstreet.R;
import com.app.nirogstreet.adapter.AppointmentAdapter;
import com.app.nirogstreet.adapter.MemberListingAdapter;
import com.app.nirogstreet.circularprogressbar.CircularProgressBar;
import com.app.nirogstreet.model.AppointmentModel;
import com.app.nirogstreet.model.UserList;
import com.app.nirogstreet.parser.AppointmentParser;
import com.app.nirogstreet.uttil.AppUrl;
import com.app.nirogstreet.uttil.NetworkUtill;
import com.app.nirogstreet.uttil.SesstionManager;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.json.JSONArray;
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
 * Created by Preeti on 17-12-2017.
 */

public class AppointmentActivity extends Activity {
    ImageView backImageView;
    ArrayList<AppointmentModel> appointmentModelsTotal = new ArrayList<>();
    RecyclerView recyclerview;
    SesstionManager sesstionManager;
    AppointmentAdapter appointmentAdapter;
    GetAppointmentAsynctask getAppointmentAsynctask;
    CircularProgressBar circularProgressBar;
    LinearLayout no_appointment, notverified;
    boolean openMain = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appointment_list);
        sesstionManager = new SesstionManager(AppointmentActivity.this);
        backImageView = (ImageView) findViewById(R.id.back);
        no_appointment = (LinearLayout) findViewById(R.id.no_appointment);
        notverified = (LinearLayout) findViewById(R.id.notverified);
        circularProgressBar = (CircularProgressBar) findViewById(R.id.circularProgressBar);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (openMain) {
                    Intent intent1 = new Intent(AppointmentActivity.this, MainActivity.class);
                    startActivity(intent1);
                    finish();
                }

                finish();
            }
        });
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        LinearLayoutManager llm = new LinearLayoutManager(AppointmentActivity.this);
        recyclerview.setHasFixedSize(true);
        if (getIntent().hasExtra("openMain")) {
            openMain = getIntent().getBooleanExtra("openMain", false);
        }
        recyclerview.setLayoutManager(llm);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        if (NetworkUtill.isNetworkAvailable(AppointmentActivity.this)) {
            getAppointmentAsynctask = new GetAppointmentAsynctask("");
            getAppointmentAsynctask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            NetworkUtill.showNoInternetDialog(AppointmentActivity.this);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (openMain) {
            Intent intent1 = new Intent(AppointmentActivity.this, MainActivity.class);
            startActivity(intent1);
            finish();
        }
    }

    public class GetAppointmentAsynctask extends AsyncTask<Void, Void, Void> {
        JSONObject jo;
        String groupId;


        private String responseBody;
        HttpClient client;

        public void cancelAsyncTask() {
            if (client != null && !isCancelled()) {
                cancel(true);
                client = null;
            }
        }

        public GetAppointmentAsynctask(String groupId) {
            this.groupId = groupId;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            circularProgressBar.setVisibility(View.GONE);
            try {
                if (jo != null) {
                    if (jo.has("response") && !jo.isNull("response")) {
                        JSONObject jsonObject = jo.getJSONObject("response");
                        int verified = -1;
                        if (jsonObject.has("appointments") && !jsonObject.isNull("appointments")) {
                            JSONObject jsonObject1 = jsonObject.getJSONObject("appointments");
                            if (jsonObject1.has("dr_verify") && !jsonObject1.isNull("dr_verify")) {
                                verified = jsonObject1.getInt("dr_verify");
                                if (verified == 1) {
                                    recyclerview.setVisibility(View.VISIBLE);
                                    notverified.setVisibility(View.GONE);
                                } else {
                                    recyclerview.setVisibility(View.GONE);
                                    notverified.setVisibility(View.VISIBLE);
                                }
                            }

                            if (jsonObject1.has("new") && !jsonObject1.isNull("new")) {
                                JSONArray jsonArray = jsonObject1.getJSONArray("new");
                                ArrayList<AppointmentModel> appointmentModels = AppointmentParser.appointmentModels(jsonArray, "Upcoming");
                                if (appointmentModels != null && appointmentModels.size() > 0) {
                                    appointmentModelsTotal.addAll(appointmentModels);
                                }
                            }
                            if (jsonObject1.has("old") && !jsonObject1.isNull("old")) {
                                JSONArray jsonArray = jsonObject1.getJSONArray("old");
                                ArrayList<AppointmentModel> appointmentModels = AppointmentParser.appointmentModels(jsonArray, "Previous");
                                if (appointmentModels != null && appointmentModels.size() > 0) {
                                    appointmentModelsTotal.addAll(appointmentModels);
                                }
                            }
                            if (appointmentModelsTotal.size() > 0) {
                                appointmentAdapter = new AppointmentAdapter(appointmentModelsTotal, AppointmentActivity.this);
                                recyclerview.setAdapter(appointmentAdapter);
                            } else {
                                if (verified == 1) {
                                    recyclerview.setVisibility(View.GONE);
                                    no_appointment.setVisibility(View.VISIBLE);
                                }
                            }
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


                String url = AppUrl.AppBaseUrl + "user/dr-appointments";
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
                pairs.add(new BasicNameValuePair("userID", sesstionManager.getUserDetails().get(SesstionManager.USER_ID)));
                httppost.setHeader("Authorization", "Basic " + sesstionManager.getUserDetails().get(SesstionManager.AUTH_TOKEN));
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

}
