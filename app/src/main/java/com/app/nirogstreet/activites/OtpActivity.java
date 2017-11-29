package com.app.nirogstreet.activites;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.nirogstreet.R;
import com.app.nirogstreet.circularprogressbar.CircularProgressBar;
import com.app.nirogstreet.uttil.AppUrl;
import com.app.nirogstreet.uttil.NetworkUtill;
import com.app.nirogstreet.uttil.SesstionManager;
import com.app.nirogstreet.uttil.TypeFaceMethods;
import com.google.firebase.iid.FirebaseInstanceId;

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
 * Created by Preeti on 18-08-2017.
 */

public class OtpActivity extends AppCompatActivity {
    BroadcastReceiver receiver;
    ImageView backImageView;
    String fname, lname, email, pass, phone, otp = null;
    TextView loginHeader, VerifyTv, resendOtp;
    SesstionManager sesstionManager;
    RegistrationAsyncTask registrationAsyncTask;
    EditText editTextOtpOne, editTextOtpTwo, editTextOtpThree, editTextOtpFour, editPhone;

    CircularProgressBar circularProgressBar;
    private SendOtpAsyncTask sendOtpAsyncTask;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_activity);
        sesstionManager = new SesstionManager(OtpActivity.this);
        backImageView = (ImageView) findViewById(R.id.back);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (getIntent().hasExtra("fname")) {
            fname = getIntent().getStringExtra("fname");
        }
        if (getIntent().hasExtra("lname")) {
            lname = getIntent().getStringExtra("lname");
        }
        if (getIntent().hasExtra("email")) {
            email = getIntent().getStringExtra("email");
        }
        if (getIntent().hasExtra("password")) {
            pass = getIntent().getStringExtra("password");
        }
        if (getIntent().hasExtra("phone")) {
            phone = getIntent().getStringExtra("phone");
        }
        if (getIntent().hasExtra("otp")) {
            otp = getIntent().getStringExtra("otp");
        }
        circularProgressBar = (CircularProgressBar) findViewById(R.id.circularProgressBar);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equalsIgnoreCase("otp")) {
                    final String message = intent.getStringExtra("message");
                    otp = message;
                    //Do whatever you want with the code here
                    if (message != null) {
                        char arr[] = new char[4];
                        for (int i = 0; i < otp.length(); i++) {
                            arr[i] = otp.charAt(i);
                        }
                        editTextOtpOne.setText(String.valueOf(arr[0]));
                        editTextOtpTwo.setText(String.valueOf(arr[1]));
                        editTextOtpThree.setText(String.valueOf(arr[2]));
                        editTextOtpFour.setText(String.valueOf(arr[3]));
                    }

                }
            }
        };
        VerifyTv = (TextView) findViewById(R.id.loginTv);
        loginHeader = (TextView) findViewById(R.id.title_side);
        resendOtp = (TextView) findViewById(R.id.resendOtp);
        editTextOtpOne = (EditText) findViewById(R.id.otpEtOne);
        editTextOtpTwo = (EditText) findViewById(R.id.otpEtTwo);
        editTextOtpThree = (EditText) findViewById(R.id.otpEtThree);
        editTextOtpFour = (EditText) findViewById(R.id.otpEtFour);
        editPhone = (EditText) findViewById(R.id.editPhone);
        if (otp != null) {
          /*  char arr[] = new char[4];
            for (int i = 0; i < otp.length(); i++) {
                arr[i] = otp.charAt(i);
            }
            editTextOtpOne.setText(String.valueOf(arr[0]));
            editTextOtpTwo.setText(String.valueOf(arr[1]));
            editTextOtpThree.setText(String.valueOf(arr[2]));
            editTextOtpFour.setText(String.valueOf(arr[3]));*/
        }

        editPhone.setEnabled(false);
        editPhone.setText(phone);
        TypeFaceMethods.setRegularTypeFaceForTextView(VerifyTv, OtpActivity.this);
        TypeFaceMethods.setRegularTypeFaceForTextView(resendOtp, OtpActivity.this);

        TypeFaceMethods.setRegularTypeFaceEditText(editPhone, OtpActivity.this);
        TypeFaceMethods.setRegularTypeFaceEditText(editTextOtpOne, OtpActivity.this);
        TypeFaceMethods.setRegularTypeFaceEditText(editTextOtpTwo, OtpActivity.this);
        TypeFaceMethods.setRegularTypeFaceEditText(editTextOtpThree, OtpActivity.this);
        TypeFaceMethods.setRegularTypeFaceEditText(editTextOtpFour, OtpActivity.this);

        TypeFaceMethods.setRegularTypeFaceForTextView(loginHeader, OtpActivity.this);

        editTextOtpOne.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editTextOtpOne.getText().toString().length() == 1)     //size as per your requirement

                    editTextOtpTwo.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editTextOtpTwo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editTextOtpTwo.getText().toString().length() == 1)     //size as per your requirement

                    editTextOtpThree.requestFocus();
                if (editTextOtpTwo.getText().toString().length() == 0)     //size as per your requirement

                    editTextOtpOne.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editTextOtpThree.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editTextOtpThree.getText().toString().length() == 1)     //size as per your requirement

                    editTextOtpFour.requestFocus();
                if (editTextOtpThree.getText().toString().length() == 0)     //size as per your requirement

                    editTextOtpTwo.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editTextOtpFour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editTextOtpFour.getText().toString().length() == 0)     //size as per your requirement

                    editTextOtpThree.requestFocus();
                if (editTextOtpOne.getText().toString().length() == 0 || editTextOtpTwo.getText().toString().length() == 0 || editTextOtpThree.getText().toString().length() == 0 || editTextOtpFour.getText().toString().length() == 0) {
                    Toast.makeText(OtpActivity.this, "Enter valid one time password", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        resendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtill.isNetworkAvailable(OtpActivity.this)) {
                    sendOtpAsyncTask = new SendOtpAsyncTask(phone);
                    sendOtpAsyncTask.execute();

                } else {
                    NetworkUtill.showNoInternetDialog(OtpActivity.this);
                }
            }
        });
        VerifyTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtill.isNetworkAvailable(OtpActivity.this)) {
                    if (phone != null && otp != null) {
                        registrationAsyncTask = new RegistrationAsyncTask(fname, lname, email, pass, phone, otp);
                        registrationAsyncTask.execute();
                    }

                } else {
                    NetworkUtill.showNoInternetDialog(OtpActivity.this);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        if (sendOtpAsyncTask != null && !sendOtpAsyncTask.isCancelled()) {
            sendOtpAsyncTask.cancelAsyncTask();
        }
        super.onPause();
    }

    public class RegistrationAsyncTask extends AsyncTask<Void, Void, Void> {
        String responseBody;
        String fname, lname, email, password, mobile, otp;
        CircularProgressBar bar;
        //PlayServiceHelper regId;

        JSONObject jo;
        HttpClient client;

        public void cancelAsyncTask() {
            if (client != null && !isCancelled()) {
                cancel(true);
                client = null;
            }
        }

        public RegistrationAsyncTask(String fname, String lname, String email, String password, String mobile, String otp) {
            this.email = email;
            this.fname = fname;
            this.otp = otp;
            this.lname = lname;
            this.password = password;
            this.mobile = mobile;
        }

        @Override
        protected void onPreExecute() {
            circularProgressBar.setVisibility(View.VISIBLE);

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                String url = AppUrl.AppBaseUrl + "user/register";
                SSLSocketFactory sf = new SSLSocketFactory(
                        SSLContext.getDefault(),
                        SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                Scheme sch = new Scheme("https", 443, sf);
                client = new DefaultHttpClient();

                client.getConnectionManager().getSchemeRegistry().register(sch);
                HttpPost httppost = new HttpPost(url);
                HttpResponse response;

                String credentials = email + ":" + password;

                List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                pairs.add(new BasicNameValuePair(AppUrl.APP_ID_PARAM, AppUrl.APP_ID_VALUE_POST));
                String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                pairs.add(new BasicNameValuePair("device_token", refreshedToken));
                pairs.add(new BasicNameValuePair("type", "android"));
                pairs.add(new BasicNameValuePair("User[fname]", fname));
                pairs.add(new BasicNameValuePair("User[lname]", lname));
                pairs.add(new BasicNameValuePair("User[email]", email));
                pairs.add(new BasicNameValuePair("User[password]", password));
                pairs.add(new BasicNameValuePair("User[mobile]", mobile));
                pairs.add(new BasicNameValuePair("otp", otp));

                String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                //  httppost.setHeader("Authorization", "Basic " + base64EncodedCredentials);
                httppost.setEntity(new UrlEncodedFormEntity(pairs));
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
                                        Toast.makeText(OtpActivity.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                if (dataJsonObject.has("message") && !dataJsonObject.isNull("message")) {
                                    JSONObject message = dataJsonObject.getJSONObject("message");

                                    if (message.has("user") && !message.isNull("user")) {
                                        JSONObject userJsonObject = message.getJSONObject("user");
                                        if (userJsonObject.has("fname") && !userJsonObject.isNull("fname")) {
                                            fname = userJsonObject.getString("fname");
                                        }
                                        if (userJsonObject.has("lname") && !userJsonObject.isNull("lname")) {
                                            lname = userJsonObject.getString("lname");
                                        }
                                        if (userJsonObject.has("id") && !userJsonObject.isNull("id")) {
                                            id = userJsonObject.getString("id");
                                        }
                                        if (userJsonObject.has("email") && !userJsonObject.isNull("email")) {
                                            email = userJsonObject.getString("email");
                                        }
                                        if (userJsonObject.has("mobile") && !userJsonObject.isNull("mobile")) {
                                            mobile = userJsonObject.getString("mobile");
                                        }
                                        if (userJsonObject.has("user_type") && !userJsonObject.isNull("user_type")) {
                                            user_type = userJsonObject.getString("user_type");
                                        }
                                        if (userJsonObject.has("auth_token") && !userJsonObject.isNull("auth_token")) {
                                            auth_token = userJsonObject.getString("auth_token");
                                        }
                                        if (userJsonObject.has("createdOn") && !userJsonObject.isNull("createdOn")) {
                                            createdOn = userJsonObject.getString("createdOn");
                                        }
                                        sesstionManager.createUserLoginSession(fname, lname, email, auth_token, mobile, createdOn, id, user_type);
                                        Intent intent = new Intent(OtpActivity.this, CreateDrProfile.class);
                                        intent.putExtra("isSkip",true);
                                        startActivity(intent);
                                        finish();
                                    }
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

    public class SendOtpAsyncTask extends AsyncTask<Void, Void, Void> {
        String responseBody;
        String mobile;
        CircularProgressBar bar;

        JSONObject jo;
        HttpClient client;

        public void cancelAsyncTask() {
            if (client != null && !isCancelled()) {
                cancel(true);
                client = null;
            }
        }

        public SendOtpAsyncTask(String mobile) {

            this.mobile = mobile;
        }

        @Override
        protected void onPreExecute() {
            circularProgressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                String url = AppUrl.AppBaseUrl + "user/send-otp";
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
                String refreshedToken = FirebaseInstanceId.getInstance().getToken();

                pairs.add(new BasicNameValuePair("User[mobileno]", mobile));

                pairs.add(new BasicNameValuePair("User[mobile]", mobile));
                pairs.add(new BasicNameValuePair("device_token", refreshedToken));
                pairs.add(new BasicNameValuePair("type", "android"));
                pairs.add(new BasicNameValuePair("User[fname]", fname));
                pairs.add(new BasicNameValuePair("User[lname]", lname));
                pairs.add(new BasicNameValuePair("User[email]", email));
                pairs.add(new BasicNameValuePair("User[password]", pass));
                pairs.add(new BasicNameValuePair("is_registration", "1"));
                httppost.setEntity(new UrlEncodedFormEntity(pairs));
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
                    if (jo != null) {
                        JSONObject dataJsonObject;

                        int code = 0;
                        if (jo.has("code") && !jo.isNull("code")) {
                            code = jo.getInt("code");
                        }
                        if (code == 200) {
                            if (jo.has("data") && !jo.isNull("data"))

                            {
                                dataJsonObject = jo.getJSONObject("data");
                                if (dataJsonObject.has("OTP") && !dataJsonObject.isNull("OTP")) {
                                    otp = dataJsonObject.getString("OTP");
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

/*
    public class VerifyOtpAsyncTask extends AsyncTask<Void, Void, Void> {
        String responseBody;
        String mobile, otp;
        CircularProgressBar bar;

        JSONObject jo;
        HttpClient client;

        public void cancelAsyncTask() {
            if (client != null && !isCancelled()) {
                cancel(true);
                client = null;
            }
        }

        public VerifyOtpAsyncTask(String mobile, String otp) {

            this.mobile = mobile;
            this.otp = otp;
        }

        @Override
        protected void onPreExecute() {
            circularProgressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                String url = AppUrl.AppBaseUrl + "user/verify-otp";
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

                pairs.add(new BasicNameValuePair("User[mobileno]", mobile));
                pairs.add(new BasicNameValuePair("otp", otp));

                httppost.setEntity(new UrlEncodedFormEntity(pairs));
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
                    JSONObject dataJsonObject;

                    int code = 0;
                    if (jo.has("code") && !jo.isNull("code")) {
                        code = jo.getInt("code");
                    }
                    if (code == 200) {
                        Toast.makeText(OtpActivity.this, "Otp sent on your mobile " + editPhone.getText().toString(), Toast.LENGTH_SHORT).show();
                        if (jo.has("data") && !jo.isNull("data"))

                        {
                            dataJsonObject = jo.getJSONObject("data");

                        }


                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }
*/

}
