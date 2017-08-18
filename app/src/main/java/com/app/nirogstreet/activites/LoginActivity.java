package com.app.nirogstreet.activites;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
    LoginAsync loginAsync;
    CircularProgressBar circularProgressBar;
    TextView loginHeader, loginTv, registerHere;
    SesstionManager sesstionManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        sesstionManager = new SesstionManager(LoginActivity.this);
        emailEt = (EditText) findViewById(R.id.emailEt);
        setPass = (EditText) findViewById(R.id.passEt);
        loginHeader = (TextView) findViewById(R.id.title_side);
        loginTv = (TextView) findViewById(R.id.loginTv);
        backImageView = (ImageView) findViewById(R.id.back);
        circularProgressBar = (CircularProgressBar) findViewById(R.id.circularProgressBar);
        registerHere = (TextView) findViewById(R.id.registerHere);
        TypeFaceMethods.setRegularTypeFaceEditText(setPass, LoginActivity.this);
        TypeFaceMethods.setRegularTypeFaceEditText(emailEt, LoginActivity.this);
        TypeFaceMethods.setRegularTypeFaceForTextView(loginHeader, LoginActivity.this);
        TypeFaceMethods.setRegularTypeFaceForTextView(loginTv, LoginActivity.this);

        TypeFaceMethods.setRegularTypeFaceForTextView(registerHere, LoginActivity.this);
        registerHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
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
                    Toast.makeText(LoginActivity.this, "Username is Empty", Toast.LENGTH_LONG).show();
                } /*else if (!Methods.isValidUsername(username)) {
                    Toast.makeText(LoginActivity.this, "Username is not valid", Toast.LENGTH_LONG).show();
                }*/ else if (password == null || password.equals("") || password.trim().isEmpty()) {
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
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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

                String credentials = email + ":" + password;

                List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                pairs.add(new BasicNameValuePair(AppUrl.APP_ID_PARAM, AppUrl.APP_ID_VALUE_POST));
                String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                pairs.add(new BasicNameValuePair("device_token", refreshedToken));
                pairs.add(new BasicNameValuePair("type", "android"));
                String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                httppost.setHeader("Authorization", "Basic " + base64EncodedCredentials);
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
            Intent intent=new Intent(LoginActivity.this,OtpActivity.class);
            startActivity(intent);
            circularProgressBar.setVisibility(View.GONE);
            try {
                if (jo != null) {
                    String nextUri = null, authToken = null, userName = null, profileUrl = null, userId = null;
                    boolean success = false;
                    if (jo.has("success") && !jo.isNull("success"))

                    {
                        success = jo.getBoolean("success");
                    }
                    if (success) {
                        if (jo.has("nexturi") && !jo.isNull("nexturi")) {
                            nextUri = jo.getString("nexturi");
                        }
                        if (jo.has("authtoken") && !jo.isNull("authtoken")) {
                            authToken = jo.getString("authtoken");
                        }
                        if (jo.has("username") && !jo.isNull("username")) {
                            userName = jo.getString("username");
                        }
                        if (jo.has("profilepic") && !jo.isNull("profilepic")) {
                            profileUrl = jo.getString("profilepic");

                        }
                        if (jo.has("userID") && !jo.isNull("userID")) {
                            userId = jo.getString("userID");
                        }
                        sesstionManager.createUserLoginSession(userName, password, authToken, userName, profileUrl, userId);
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);

                        finish();
                    } else {
                        if (jo.has("message") && !jo.isNull("message")) {
                            if (jo.getString("message").equalsIgnoreCase("ok")) {
                                if (NetworkUtill.isNetworkAvailable(LoginActivity.this)) {
                                    loginAsync = new LoginAsync();
                                    loginAsync.execute();
                                } else
                                    NetworkUtill.showNoInternetDialog(LoginActivity.this);

                            } else {
                                Toast.makeText(LoginActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            //  bar.setVisibility(View.GONE);

        }
    }

}
