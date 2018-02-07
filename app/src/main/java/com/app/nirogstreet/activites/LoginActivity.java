package com.app.nirogstreet.activites;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.nirogstreet.R;
import com.app.nirogstreet.circularprogressbar.CircularProgressBar;
import com.app.nirogstreet.uttil.AppUrl;
import com.app.nirogstreet.uttil.Methods;
import com.app.nirogstreet.uttil.NetworkUtill;
import com.app.nirogstreet.uttil.SesstionManager;
import com.app.nirogstreet.uttil.TypeFaceMethods;
import com.google.firebase.iid.FirebaseInstanceId;
import com.rx2androidnetworking.Rx2AndroidNetworking;

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

public class LoginActivity extends AppCompatActivity {
    EditText emailEt, setPass;
    String username, password;
    ImageView backImageView;
    private int STORAGE_PERMISSION_CODE_VIDEO = 2;

    LoginAsync loginAsync;
    CircularProgressBar circularProgressBar;
    TextView loginHeader, loginTv, loginWithOtp;
    LinearLayout registerHere;
    private int passwordNotVisible = 1;
    ImageButton img_lock;
    LoginViaOTPAsync loginViaOTPAsync;
    SesstionManager sesstionManager;
    TextView forgot;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setContentView(R.layout.new_login);
        sesstionManager = new SesstionManager(LoginActivity.this);
        img_lock = (ImageButton) findViewById(R.id.img_lock);
        emailEt = (EditText) findViewById(R.id.emailEt);
        forgot = (TextView) findViewById(R.id.forgot);
        loginWithOtp = (TextView) findViewById(R.id.loginWithOtp);
        loginWithOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailEt.getText().length() == 0) {
                    Toast.makeText(LoginActivity.this, "Email/Phone is Empty", Toast.LENGTH_LONG).show();
                } else if (!Methods.validCellPhone(emailEt.getText().toString()) || !Methods.isValidPhoneNumber(emailEt.getText().toString())) {
                    Toast.makeText(LoginActivity.this, "Enter valid Phone No", Toast.LENGTH_LONG).show();

                } else {
                    if (NetworkUtill.isNetworkAvailable(LoginActivity.this)) {checkPermissionGeneral(); {

                        }
                    } else {
                        NetworkUtill.showNoInternetDialog(LoginActivity.this);
                    }
                }
            }
        });

        // TypeFaceMethods.setRegularTypeFaceForTextView(forgot,LoginActivity.this);
        setPass = (EditText) findViewById(R.id.passEt);
        img_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText paswword = (EditText) findViewById(R.id.passEt);
                if (passwordNotVisible == 1) {
                    img_lock.setBackgroundResource(R.drawable.unlock);
                    paswword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordNotVisible = 0;
                } else {
                    img_lock.setBackgroundResource(R.drawable.lock);
                    paswword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordNotVisible = 1;
                }


                paswword.setSelection(paswword.length());

            }
        });

        loginHeader = (TextView) findViewById(R.id.title_side);
        loginTv = (TextView) findViewById(R.id.loginTv);
        circularProgressBar = (CircularProgressBar) findViewById(R.id.circularProgressBar);
        registerHere = (LinearLayout) findViewById(R.id.registerHere);

        registerHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPassword.class);
                startActivity(intent);
            }
        });
        loginTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager inputManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                username = emailEt.getText().toString();
                password = setPass.getText().toString();

                if (username == null || username.equals("") || username.trim().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Email/Phone is Empty", Toast.LENGTH_LONG).show();
                } else if (password == null || password.equals("") || password.trim().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Password is Empty", Toast.LENGTH_LONG).show();
                } else {

                    if (NetworkUtill.isNetworkAvailable(LoginActivity.this)) {
                        loginAsync = new LoginAsync();
                        loginAsync.execute();
                    } else
                        NetworkUtill.showNoInternetDialog(LoginActivity.this);
                }


            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SesstionManager sesstionManager = new SesstionManager(LoginActivity.this);
        if (sesstionManager.isUserLoggedIn()) {
            finish();
        }
    }
    public void checkPermissionGeneral() {
        if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
            loginViaOTPAsync = new LoginViaOTPAsync();
            loginViaOTPAsync.execute();
            Log.e("", " Permission Already given ");
        } else {
            Log.e("", "Current app does not have READ_PHONE_STATE permission");
            requestPermissions(new String[]{Manifest.permission.READ_SMS,
                    Manifest.permission.READ_SMS}, STORAGE_PERMISSION_CODE_VIDEO);
        }
    }
    private boolean checkWriteExternalPermission() {

        String permission = "android.permission.READ_SMS";
        int res = checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 2) {
            loginViaOTPAsync = new LoginViaOTPAsync();
            loginViaOTPAsync.execute();
        }
    }

    public class LoginAsync extends AsyncTask<Void, Void, Void> {
        String responseBody;
        String email, password;
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

        @Override
        protected void onPreExecute() {
            circularProgressBar.setVisibility(View.VISIBLE);
            email = emailEt.getText().toString();
            password = setPass.getText().toString();
            //  bar = (ProgressBar) findViewById(R.id.progressBar);
            //   bar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                String url = AppUrl.AppBaseUrl + "user/login";
                SSLSocketFactory sf = new SSLSocketFactory(
                        SSLContext.getDefault(),
                        SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                Scheme sch = new Scheme("https", 443, sf);
                client = new DefaultHttpClient();

                client.getConnectionManager().getSchemeRegistry().register(sch);
                HttpPost httppost = new HttpPost(url);
                HttpResponse response;

                String credentials = email.toString().trim() + ":" + password;

                List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                pairs.add(new BasicNameValuePair(AppUrl.APP_ID_PARAM, AppUrl.APP_ID_VALUE_POST));
                String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                pairs.add(new BasicNameValuePair("device_token", refreshedToken));
                pairs.add(new BasicNameValuePair("type", "android"));
                String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                httppost.setHeader("Authorization", "Basic " + base64EncodedCredentials);
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

                    String auth_token = "", createdOn = "",referral_code="", id = "", email = "", mobile = "", user_type = "", lname = "", fname = "",title="";
                    if (jo.has("data") && !jo.isNull("data")) {
                        dataJsonObject = jo.getJSONObject("data");

                        if (dataJsonObject.has("status") && !dataJsonObject.isNull("status"))

                        {
                            status = dataJsonObject.getBoolean("status");
                            if (!status) {
                                Toast.makeText(LoginActivity.this, dataJsonObject.getString("message"), Toast.LENGTH_SHORT).show();


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
                                        if (userJsonObject.has("Title") && !userJsonObject.isNull("Title")) {
                                            title = userJsonObject.getString("Title");
                                        }
                                        sesstionManager.createUserLoginSession(fname, lname, email, auth_token, mobile, createdOn, id, user_type,referral_code,title);
                                        Intent intent1 = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent1);
                                        finish();
                                    }
                                }

                            }
                        }
                    } else {
                        if (jo.has("message") && !jo.isNull("message")) {
                            if (jo.getString("message").equalsIgnoreCase("OK")) {
                                if (NetworkUtill.isNetworkAvailable(LoginActivity.this)) {
                                    loginAsync = new LoginAsync();
                                    loginAsync.execute();

                                } else
                                    NetworkUtill.showNoInternetDialog(LoginActivity.this);
                            }
                        }

                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    public class LoginViaOTPAsync extends AsyncTask<Void, Void, Void> {
        String responseBody;
        String phone, password;
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

        @Override
        protected void onPreExecute() {
            circularProgressBar.setVisibility(View.VISIBLE);
            phone = emailEt.getText().toString();
            password = setPass.getText().toString();
            //  bar = (ProgressBar) findViewById(R.id.progressBar);
            //   bar.setVisibility(View.VISIBLE);
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
                pairs.add(new BasicNameValuePair("mobile", phone));

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

                    String auth_token = "", createdOn = "", id = "", email = "", mobile = "", user_type = "", lname = "", fname = "";
                    if (jo.has("data") && !jo.isNull("data")) {
                        dataJsonObject = jo.getJSONObject("data");

                        if (dataJsonObject.has("status") && !dataJsonObject.isNull("status"))

                        {
                            status = dataJsonObject.getBoolean("status");
                            if (!status) {
                                Toast.makeText(LoginActivity.this, dataJsonObject.getString("message"), Toast.LENGTH_SHORT).show();


                            } else {
                                if (dataJsonObject.has("userID") && !dataJsonObject.isNull("userID")) {

                                    String userId = dataJsonObject.getString("userID");
                                    Intent intent = new Intent(LoginActivity.this, LoginWithOTP.class);
                                    intent.putExtra("UserId", userId);
                                    intent.putExtra("phone", phone);
                                    startActivity(intent);
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

}
