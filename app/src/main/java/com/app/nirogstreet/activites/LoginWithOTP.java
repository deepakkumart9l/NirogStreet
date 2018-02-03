package com.app.nirogstreet.activites;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.nirogstreet.R;
import com.app.nirogstreet.circularprogressbar.CircularProgressBar;
import com.app.nirogstreet.uttil.AppUrl;
import com.app.nirogstreet.uttil.Methods;
import com.app.nirogstreet.uttil.NetworkUtill;
import com.app.nirogstreet.uttil.SesstionManager;
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
 * Created by Preeti on 27-12-2017.
 */

public class LoginWithOTP extends AppCompatActivity {
    BroadcastReceiver receiver;
    ImageView backImageView;
    String fname, email, pass, phone, otp = null;
    TextView loginHeader, resendOtp, timerTextView;
    Button VerifyTv;
LoginAsync loginAsync;
    String UserId,phoneNo;
    SesstionManager sesstionManager;
    EditText editTextOtpOne, editTextOtpTwo, editTextOtpThree, editTextOtpFour, editPhone;

    CircularProgressBar circularProgressBar;
    private SendOtpAsyncTask sendOtpAsyncTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_otp_activity);
        if(getIntent().hasExtra("UserId"))
        {
            UserId=getIntent().getStringExtra("UserId");
        }
        if(getIntent().hasExtra("phone"))
        {
            phoneNo=getIntent().getStringExtra("phone");
        }
        sesstionManager = new SesstionManager(LoginWithOTP.this);
        backImageView = (ImageView) findViewById(R.id.back);
        timerTextView = (TextView) findViewById(R.id.timerTextView);

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (getIntent().hasExtra("fname")) {
            fname = getIntent().getStringExtra("fname");
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
        VerifyTv = (Button) findViewById(R.id.loginTvotp);
        loginHeader = (TextView) findViewById(R.id.title_side);
        resendOtp = (TextView) findViewById(R.id.resendOtp);
        editTextOtpOne = (EditText) findViewById(R.id.otpEtOne);
        editTextOtpTwo = (EditText) findViewById(R.id.otpEtTwo);
        editTextOtpThree = (EditText) findViewById(R.id.otpEtThree);
        editTextOtpFour = (EditText) findViewById(R.id.otpEtFour);
        editPhone = (EditText) findViewById(R.id.editPhone);
        editPhone.setText(phoneNo);
        editPhone.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                editPhone.setFocusable(true);
                return false;
            }
        });
        editTextOtpOne.requestFocus();

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
                    Toast.makeText(LoginWithOTP.this, "Enter valid one time password", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        resendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtill.isNetworkAvailable(LoginWithOTP.this)) {
                    resendOtp.setVisibility(View.GONE);
                    timerTextView.setVisibility(View.VISIBLE);
                    new CountDownTimer(15000, 1000) {

                        public void onTick(long millisUntilFinished) {

                            timerTextView.setText("00:" + millisUntilFinished / 1000 +" sec");
                        }

                        public void onFinish() {
                            timerTextView.setVisibility(View.GONE);
                            resendOtp.setVisibility(View.VISIBLE);
                        }
                    }.start();
                    sendOtpAsyncTask = new SendOtpAsyncTask(phone);
                    sendOtpAsyncTask.execute();

                } else {
                    NetworkUtill.showNoInternetDialog(LoginWithOTP.this);
                }
            }
        });
        VerifyTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    String str = editTextOtpOne.getText().toString() + editTextOtpTwo.getText().toString() + editTextOtpThree.getText().toString() + editTextOtpFour.getText().toString();
                    phone = editPhone.getText().toString();
                    if (NetworkUtill.isNetworkAvailable(LoginWithOTP.this)) {
                        if (phone != null && str != null) {
                            String otp=editTextOtpOne.getText().toString()+editTextOtpTwo.getText().toString()+editTextOtpThree.getText().toString()+editTextOtpFour.getText().toString();
                           loginAsync = new LoginAsync(str, editPhone.getText().toString());
                            loginAsync.execute();
                        }

                    } else {
                        NetworkUtill.showNoInternetDialog(LoginWithOTP.this);
                    }
                }
            }
        });
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
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
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        };

    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();


    }

    private boolean validate() {
        if (editPhone.getText().toString().length() == 0) {
            Toast.makeText(LoginWithOTP.this, "Enter mobile number", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!Methods.isValidPhoneNumber(editPhone.getText().toString())||!Methods.validCellPhone(editPhone.getText().toString())) {
            Toast.makeText(LoginWithOTP.this, "Enter valid mobile number", Toast.LENGTH_SHORT).show();
            return false;
        } else if (editTextOtpOne.getText().length() == 0 || editTextOtpTwo.getText().length() == 0 || editTextOtpThree.getText().length() == 0 || editTextOtpFour.getText().length() == 0) {
            Toast.makeText(LoginWithOTP.this, "Enter  Otp ", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;

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
            circularProgressBar.setVisibility(View.GONE);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                String url = AppUrl.AppBaseUrl + "user/login-via-otp";
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


                pairs.add(new BasicNameValuePair("mobile", mobile));

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
                        boolean status = false;

                        if (code == 200) {
                            if (jo.has("data") && !jo.isNull("data"))

                            {
                                dataJsonObject = jo.getJSONObject("data");

                                if (dataJsonObject.has("status") && !dataJsonObject.isNull("status"))

                                {
                                    status = dataJsonObject.getBoolean("status");
                                    if (!status) {
                                        Toast.makeText(LoginWithOTP.this, dataJsonObject.getString("message"), Toast.LENGTH_SHORT).show();


                                    } else {
                                        if (dataJsonObject.has("userID") && !dataJsonObject.isNull("userID")) {

                                            UserId=dataJsonObject.getString("userID");

                                        }

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
    public class LoginAsync extends AsyncTask<Void, Void, Void> {
        String responseBody;
        String email, password;
        CircularProgressBar bar;
        //PlayServiceHelper regId;
String otp,phone;
        JSONObject jo;
        HttpClient client;
public LoginAsync(String otp,String phone)
{
    this.otp=otp;
    this.phone=phone;
}
        public void cancelAsyncTask() {
            if (client != null && !isCancelled()) {
                cancel(true);
                client = null;
            }
        }

        @Override
        protected void onPreExecute() {
            circularProgressBar.setVisibility(View.VISIBLE);
            email = editPhone.getText().toString();
            //  bar = (ProgressBar) findViewById(R.id.progressBar);
            //   bar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                String url = AppUrl.AppBaseUrl + "user/login-with-otp";
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
                pairs.add(new BasicNameValuePair("device_token", refreshedToken));
                pairs.add(new BasicNameValuePair("type", "android"));
                pairs.add(new BasicNameValuePair("userID",UserId));
                pairs.add(new BasicNameValuePair("otp",otp));
                pairs.add(new BasicNameValuePair("mobile",phone));
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
         /*   Intent intent=new Intent(LoginActivity.this,OtpActivity.class);
            startActivity(intent);*/
            circularProgressBar.setVisibility(View.GONE);

            circularProgressBar.setVisibility(View.GONE);
            try {
                if (jo != null) {
                    JSONArray errorArray;
                    JSONObject dataJsonObject;
                    boolean status = false;

                    String auth_token = "", createdOn = "", id = "", email = "", mobile = "", user_type = "", lname = "", fname = "",referral_code="";
                    if (jo.has("data") && !jo.isNull("data")) {
                        dataJsonObject = jo.getJSONObject("data");

                        if (dataJsonObject.has("status") && !dataJsonObject.isNull("status"))

                        {
                            status = dataJsonObject.getBoolean("status");
                            if (!status) {
                                Toast.makeText(LoginWithOTP.this, dataJsonObject.getString("message"), Toast.LENGTH_SHORT).show();


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
                                        if (userJsonObject.has("referral_code") && !userJsonObject.isNull("referral_code")) {
                                            referral_code = userJsonObject.getString("referral_code");
                                        }
                                        sesstionManager.createUserLoginSession(fname, lname, email, auth_token, mobile, createdOn, id, user_type,referral_code);
                                        Intent intent1 = new Intent(LoginWithOTP.this, MainActivity.class);
                                        startActivity(intent1);
                                        finish();
                                    }
                                }

                            }
                        }
                    } else {
                        if (jo.has("message") && !jo.isNull("message")) {
                            if (jo.getString("message").equalsIgnoreCase("OK")) {
                                if (NetworkUtill.isNetworkAvailable(LoginWithOTP.this)) {
                                    loginAsync = new LoginAsync(otp,phone);
                                    loginAsync.execute();

                                } else
                                    NetworkUtill.showNoInternetDialog(LoginWithOTP.this);
                            }
                        }

                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

}
