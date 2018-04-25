package com.app.nirogstreet.activites;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.app.nirogstreet.R;
import com.app.nirogstreet.circularprogressbar.CircularProgressBar;
import com.app.nirogstreet.uttil.AppUrl;
import com.app.nirogstreet.uttil.NetworkUtill;
import com.app.nirogstreet.uttil.SesstionManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
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
 * Created by as on 4/19/2018.
 */

public class Change_Password extends AppCompatActivity {
    EditText oldpassEt, newpassEt;
    private SesstionManager sessionManager;
    String userId, authToken, oldpass, newpass;
    CircularProgressBar circularProgressBar;
    Button loginTv;
    ChangePass changePass;
    ImageButton old_pass_img_lock, new_pass_img_lock;
    private int passwordNotVisible = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.change_password);
        oldpassEt = (EditText) findViewById(R.id.oldpassEt);
        old_pass_img_lock = (ImageButton) findViewById(R.id.old_pass_img_lock);
        new_pass_img_lock = (ImageButton) findViewById(R.id.new_pass_img_lock);

        newpassEt = (EditText) findViewById(R.id.newpassEt);
        loginTv = (Button) findViewById(R.id.loginTv);
        sessionManager = new SesstionManager(Change_Password.this);
        circularProgressBar = (CircularProgressBar) findViewById(R.id.circularProgressBar);
        HashMap<String, String> user = sessionManager.getUserDetails();
        authToken = user.get(SesstionManager.AUTH_TOKEN);
        userId = user.get(SesstionManager.USER_ID);
        loginTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (oldpassEt.getText().length() == 0) {
                    Toast.makeText(Change_Password.this, "Enter old password", Toast.LENGTH_LONG).show();
                } else if (newpassEt.getText().length() == 0) {
                    Toast.makeText(Change_Password.this, "Enter new password", Toast.LENGTH_LONG).show();

                } else {
                    if (NetworkUtill.isNetworkAvailable(Change_Password.this)) {
                        changePass = new ChangePass();
                        changePass.execute();
                    } else {
                        NetworkUtill.showNoInternetDialog(Change_Password.this);
                    }
                }
            }
        });
        old_pass_img_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText paswword = (EditText) findViewById(R.id.oldpassEt);
                if (passwordNotVisible == 1) {
                    old_pass_img_lock.setBackgroundResource(R.drawable.unlock);
                    paswword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordNotVisible = 0;
                } else {
                    old_pass_img_lock.setBackgroundResource(R.drawable.lock);
                    paswword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordNotVisible = 1;
                }


                paswword.setSelection(paswword.length());

            }
        });
        new_pass_img_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText paswword = (EditText) findViewById(R.id.newpassEt);
                if (passwordNotVisible == 1) {
                    new_pass_img_lock.setBackgroundResource(R.drawable.unlock);
                    paswword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordNotVisible = 0;
                } else {
                    new_pass_img_lock.setBackgroundResource(R.drawable.lock);
                    paswword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordNotVisible = 1;
                }


                paswword.setSelection(paswword.length());

            }
        });
    }

    public class ChangePass extends AsyncTask<Void, Void, Void> {
        JSONObject jo;
        String feedId;
        private String responseBody;
        HttpClient client;
        Context context;

        public void cancelAsyncTask() {
            if (client != null && !isCancelled()) {
                cancel(true);
                client = null;
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                circularProgressBar.setVisibility(View.GONE);
                if (jo != null) {
                    if (jo.has("status") && !jo.isNull("status")) {
                        boolean status = jo.getBoolean("status");
                        if (status == true) {
                            Toast.makeText(Change_Password.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(Change_Password.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
                            finish();
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
                String url = AppUrl.AppBaseUrl + "user/reset-password";
                SSLSocketFactory sf = new SSLSocketFactory(
                        SSLContext.getDefault(),
                        SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                client = new DefaultHttpClient();
                Scheme sch = new Scheme("https", 443, sf);
                client.getConnectionManager().getSchemeRegistry().register(sch);
                HttpPost httppost = new HttpPost(url);
                HttpResponse response;
                List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                pairs.add(new BasicNameValuePair(AppUrl.APP_ID_PARAM, AppUrl.APP_ID_VALUE_POST));
                pairs.add(new BasicNameValuePair("userID", userId));
                pairs.add(new BasicNameValuePair("oldPassword", oldpass));
                pairs.add(new BasicNameValuePair("newPassword", newpass));
                pairs.add(new BasicNameValuePair("confirm_password", newpass));
                httppost.setHeader("Authorization", "Basic " + authToken);
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
            oldpass = oldpassEt.getText().toString();
            newpass = newpassEt.getText().toString();
        }
    }
}
