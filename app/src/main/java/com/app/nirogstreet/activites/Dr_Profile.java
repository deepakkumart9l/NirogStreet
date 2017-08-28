package com.app.nirogstreet.activites;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.app.nirogstreet.R;
import com.app.nirogstreet.circularprogressbar.CircularProgressBar;
import com.app.nirogstreet.model.UserDetailModel;
import com.app.nirogstreet.parser.UserDetailPaser;
import com.app.nirogstreet.uttil.AppUrl;
import com.app.nirogstreet.uttil.ImageLoader;
import com.app.nirogstreet.uttil.NetworkUtill;
import com.app.nirogstreet.uttil.SesstionManager;
import com.app.nirogstreet.uttil.TypeFaceMethods;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

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
 * Created by Preeti on 22-08-2017.
 */

public class Dr_Profile extends AppCompatActivity {
    TextView nameTv, placeTv, emailTv, phoneTv, WebTv, yearOfBirthTv, yearOfExperienceTv, QualificationTv, aboutHeading, aboutDetail, QualificationSectionTv, SpecializationSectionHeadingTv, sepcilizationDetailTv, consultationFeesHeading, allTaxes, fee;
    CircularProgressBar circularProgressBar;
    String authToken, userId, email, mobile, userName;
    private SesstionManager sesstionManager;
    UserDetailAsyncTask userDetailAsyncTask;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_detail);
        nameTv = (TextView) findViewById(R.id.nameTv);
        placeTv = (TextView) findViewById(R.id.placeTv);
        emailTv = (TextView) findViewById(R.id.emailTv);
        phoneTv = (TextView) findViewById(R.id.phoneTv);
        WebTv = (TextView) findViewById(R.id.WebTv);
        aboutHeading = (TextView) findViewById(R.id.about);
        aboutDetail = (TextView) findViewById(R.id.about_detail);
        yearOfBirthTv = (TextView) findViewById(R.id.yearOfBirthTv);
        yearOfExperienceTv = (TextView) findViewById(R.id.yearOfExperienceTv);
        QualificationSectionTv = (TextView) findViewById(R.id.QualificationSectionTv);
        SpecializationSectionHeadingTv = (TextView) findViewById(R.id.SpecializationSectionHeadingTv);
        QualificationTv = (TextView) findViewById(R.id.QualificationTv);
        sepcilizationDetailTv = (TextView) findViewById(R.id.sepcilizationDetailTv);
        consultationFeesHeading = (TextView) findViewById(R.id.consultaionFees);
        allTaxes = (TextView) findViewById(R.id.allTaxes);
        circularProgressBar = (CircularProgressBar) findViewById(R.id.circularProgressBar);
        TypeFaceMethods.setRegularTypeFaceForTextView(emailTv, Dr_Profile.this);
        TypeFaceMethods.setRegularTypeFaceForTextView(phoneTv, Dr_Profile.this);
        TypeFaceMethods.setRegularTypeFaceForTextView(WebTv, Dr_Profile.this);
        TypeFaceMethods.setRegularTypeFaceForTextView(yearOfBirthTv, Dr_Profile.this);
        TypeFaceMethods.setRegularTypeFaceForTextView(yearOfExperienceTv, Dr_Profile.this);
        TypeFaceMethods.setRegularTypeFaceForTextView(QualificationTv, Dr_Profile.this);
        TypeFaceMethods.setRegularTypeBoldFaceTextView(nameTv, Dr_Profile.this);
        TypeFaceMethods.setRegularTypeBoldFaceTextView(placeTv, Dr_Profile.this);
        TypeFaceMethods.setRegularTypeBoldFaceTextView(sepcilizationDetailTv, Dr_Profile.this);
        TypeFaceMethods.setRegularTypeBoldFaceTextView(QualificationSectionTv, Dr_Profile.this);

        TypeFaceMethods.setRegularTypeBoldFaceTextView(SpecializationSectionHeadingTv, Dr_Profile.this);
        TypeFaceMethods.setRegularTypeBoldFaceTextView(consultationFeesHeading, Dr_Profile.this);

        TypeFaceMethods.setRegularTypeBoldFaceTextView(aboutHeading, Dr_Profile.this);
        TypeFaceMethods.setRegularTypeFaceForTextView(aboutDetail, Dr_Profile.this);
        sesstionManager = new SesstionManager(Dr_Profile.this);

        if (sesstionManager.isUserLoggedIn()) {
            authToken = sesstionManager.getUserDetails().get(SesstionManager.AUTH_TOKEN);
            userId = sesstionManager.getUserDetails().get(SesstionManager.USER_ID);
            email = sesstionManager.getUserDetails().get(SesstionManager.KEY_EMAIL);
            userName = sesstionManager.getUserDetails().get(SesstionManager.KEY_FNAME) + sesstionManager.getUserDetails().get(SesstionManager.KEY_LNAME);
            mobile = sesstionManager.getUserDetails().get(SesstionManager.MOBILE);
            emailTv.setText(email);
            phoneTv.setText(mobile);
            nameTv.setText(userName);

        }
        if (NetworkUtill.isNetworkAvailable(Dr_Profile.this)) {
            userDetailAsyncTask = new UserDetailAsyncTask();
            userDetailAsyncTask.execute();
        } else {
            NetworkUtill.showNoInternetDialog(Dr_Profile.this);
        }

    }

    public class UserDetailAsyncTask extends AsyncTask<Void, Void, Void> {
        String responseBody;


        JSONObject jo;
        HttpClient client;

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

                String url = AppUrl.AppBaseUrl + "user/profile";
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

                httppost.setEntity(new UrlEncodedFormEntity(pairs));
                httppost.setHeader("Authorization", "Basic " + authToken);

                response = client.execute(httppost);
                responseBody = EntityUtils.toString(response.getEntity());
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
                    JSONArray errorArray;
                    JSONObject dataJsonObject;
                    boolean status = false;
                    String auth_token = "", createdOn = "", id = "", email = "", mobile = "", user_type = "", lname = "", fname = "";
                    if (jo.has("data") && !jo.isNull("data")) {
                        dataJsonObject = jo.getJSONObject("data");

                        if (dataJsonObject.has("status") && !dataJsonObject.isNull("status"))

                        {
                            status = dataJsonObject.getBoolean("status");
                            if (!status) {
                                if (dataJsonObject.has("message") && !dataJsonObject.isNull("message")) {
                                    errorArray = dataJsonObject.getJSONArray("message");
                                    for (int i = 0; i < errorArray.length(); i++) {
                                        String error = errorArray.getJSONObject(i).getString("error");
                                        //  Toast.makeText(OtpActivity.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                UserDetailModel userDetailModel = UserDetailPaser.userDetailParser(dataJsonObject);
                                if (userDetailModel.getExperience() != null && !userDetailModel.getExperience().equalsIgnoreCase("")) {
                                    yearOfExperienceTv.setText(userDetailModel.getExperience()+" "+"years experience");
                                }
                                if (userDetailModel.getCity() != null && !userDetailModel.getCity().equalsIgnoreCase("")) {
                                    placeTv.setText(userDetailModel.getCity());
                                }
                                if (userDetailModel.getWebSite() != null && !userDetailModel.getWebSite().equalsIgnoreCase("")) {
                                    WebTv.setText(userDetailModel.getWebSite());
                                }
                                if (userDetailModel.getDob() != null && !userDetailModel.getDob().equalsIgnoreCase("")) {
                                    yearOfBirthTv.setText(userDetailModel.getDob());
                                }
                                if (userDetailModel.getAbout() != null && !userDetailModel.getAbout().equalsIgnoreCase("")) {
                                    aboutDetail.setText(userDetailModel.getAbout());
                                }
                            }

                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (userDetailAsyncTask != null && !userDetailAsyncTask.isCancelled()) {
            userDetailAsyncTask.cancelAsyncTask();
        }
    }
}
