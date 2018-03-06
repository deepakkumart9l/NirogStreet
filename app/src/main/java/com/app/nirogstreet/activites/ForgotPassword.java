package com.app.nirogstreet.activites;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
 * Created by Preeti on 29-11-2017.
 */

public class ForgotPassword
        extends AppCompatActivity

{
    EditText emailEt;
    String username, password;
    ImageView backImageView;
    LoginAsync loginAsync;
    CircularProgressBar circularProgressBar;
    TextView loginHeader, registerHere;
    Button loginTv;
    SesstionManager sesstionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_forgot_pass);
        emailEt = (EditText) findViewById(R.id.emailEt);
        loginHeader = (TextView) findViewById(R.id.title_side);
        loginTv = (Button) findViewById(R.id.loginTv);

        circularProgressBar = (CircularProgressBar) findViewById(R.id.circularProgressBar);

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

                if (username == null || username.equals("") || username.trim().isEmpty()){
                    Toast.makeText(ForgotPassword.this, "Email is Empty", Toast.LENGTH_LONG).show();
                }else{

                    if (Methods.isValidEmailAddress(username.trim())) {
                        if (NetworkUtill.isNetworkAvailable(ForgotPassword.this)) {
                            loginAsync = new LoginAsync();
                            loginAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        } else
                            NetworkUtill.showNoInternetDialog(ForgotPassword.this);
                    }else {
                        Toast.makeText(ForgotPassword.this, "Invalid Email", Toast.LENGTH_LONG).show();

                    }
                }
        /*      Intent intent=new Intent(LoginActivity.this,PostingActivity.class);
                startActivity(intent);
*/

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
            //  bar = (ProgressBar) findViewById(R.id.progressBar);
            //   bar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                String url = AppUrl.AppBaseUrl + "user/forgot-password";
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
                pairs.add(new BasicNameValuePair("email", email.toString().trim()));
                String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
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
                    JSONArray dataJsonObject;
                    int status = -1;
                    String auth_token = "", createdOn = "", id = "", email = "", mobile = "", user_type = "", lname = "", fname = "";
                    if (jo.has("data") && !jo.isNull("data")) {
                        dataJsonObject = jo.getJSONArray("data");

                        if (dataJsonObject.getJSONObject(0).has("result") && !dataJsonObject.getJSONObject(0).isNull("result"))

                        {
                            status = dataJsonObject.getJSONObject(0).getInt("result");
                            if (status != 1) {
                                if (dataJsonObject.getJSONObject(0).has("message") && !dataJsonObject.getJSONObject(0).isNull("message"))
                                    Toast.makeText(ForgotPassword.this, dataJsonObject.getJSONObject(0).getString("message"), Toast.LENGTH_SHORT).show();


                            } else {
                                if (dataJsonObject.getJSONObject(0).has("message") && !dataJsonObject.getJSONObject(0).isNull("message")) {
                                    Toast.makeText(ForgotPassword.this, dataJsonObject.getJSONObject(0).getString("message"), Toast.LENGTH_LONG).show();
                                    ;

                                    Intent intent = new Intent(ForgotPassword.this, LoginActivity.class);
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


