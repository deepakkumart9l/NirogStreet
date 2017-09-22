package com.app.nirogstreet.activites;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.nirogstreet.R;
import com.app.nirogstreet.circularprogressbar.CircularProgressBar;
import com.app.nirogstreet.uttil.AppUrl;
import com.app.nirogstreet.uttil.Methods;
import com.app.nirogstreet.uttil.NetworkUtill;
import com.app.nirogstreet.uttil.TypeFaceMethods;
import com.google.firebase.iid.FirebaseInstanceId;

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
 * Created by Preeti on 17-08-2017.
 */

public class RegisterActivity extends AppCompatActivity {
    SendOtpAsyncTask sendOtpAsyncTask;
    CheckBox checkbox;
    private int STORAGE_PERMISSION_CODE_VIDEO = 2;

    EditText firstNameEt, lastNameEt, phoneEt, emailEt, confirmpassEt, setPass;
    ImageView backImageView;
    TextView registerHeader, registerAs, AllreadyhaveAccount, signIn, sentTv;
    CircularProgressBar circularProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        checkbox = (CheckBox) findViewById(R.id.checkbox);
        circularProgressBar = (CircularProgressBar) findViewById(R.id.circularProgressBar);
        registerHeader = (TextView) findViewById(R.id.title_side);
        sentTv = (TextView) findViewById(R.id.sentTv);
        backImageView = (ImageView) findViewById(R.id.back);
        AllreadyhaveAccount = (TextView) findViewById(R.id.alreadyTv);
        signIn = (TextView) findViewById(R.id.signIn);
        registerAs = (TextView) findViewById(R.id.registerAs);
        firstNameEt = (EditText) findViewById(R.id.firstNameEt);
        phoneEt = (EditText) findViewById(R.id.phoneEt);
        lastNameEt = (EditText) findViewById(R.id.lastNameEt);
        confirmpassEt = (EditText) findViewById(R.id.confirmpassEt);
        emailEt = (EditText) findViewById(R.id.emailEt);
        setPass = (EditText) findViewById(R.id.passEt);
        checkPermissionGeneral();
        TypeFaceMethods.setRegularTypeFaceForTextView(registerAs, RegisterActivity.this);
        TypeFaceMethods.setRegularTypeFaceForTextView(registerHeader, RegisterActivity.this);
        TypeFaceMethods.setRegularTypeFaceForTextView(sentTv, RegisterActivity.this);

        TypeFaceMethods.setRegularTypeFaceForTextView(AllreadyhaveAccount, RegisterActivity.this);
        TypeFaceMethods.setRegularTypeFaceForTextView(signIn, RegisterActivity.this);

        TypeFaceMethods.setRegularTypeFaceEditText(phoneEt, RegisterActivity.this);
        TypeFaceMethods.setRegularTypeFaceEditText(setPass, RegisterActivity.this);

        TypeFaceMethods.setRegularTypeFaceEditText(emailEt, RegisterActivity.this);
        TypeFaceMethods.setRegularTypeFaceEditText(confirmpassEt, RegisterActivity.this);

        TypeFaceMethods.setRegularTypeFaceEditText(lastNameEt, RegisterActivity.this);
        TypeFaceMethods.setRegularTypeFaceEditText(firstNameEt, RegisterActivity.this);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.statusbarcolor));
        }
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        sentTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtill.isNetworkAvailable(RegisterActivity.this)) {
                    if (validation()) {
                        if(checkWriteExternalPermission()){
                        sendOtpAsyncTask = new SendOtpAsyncTask(phoneEt.getText().toString(), setPass.getText().toString(), lastNameEt.getText().toString(), firstNameEt.getText().toString(), emailEt.getText().toString());
                        sendOtpAsyncTask.execute();
                    }else {
                            checkPermissionGeneral();
                        }
                    }
                } else {
                    NetworkUtill.showNoInternetDialog(RegisterActivity.this);
                }
            }
        });
    }
    private boolean checkWriteExternalPermission()
    {

        String permission = "android.permission.READ_SMS";
        int res = checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }
    public void checkPermissionGeneral() {
        if (ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
            Log.e("", " Permission Already given ");
        } else {
            Log.e("", "Current app does not have READ_PHONE_STATE permission");
            requestPermissions(new String[]{Manifest.permission.READ_SMS,
                    Manifest.permission.READ_SMS}, STORAGE_PERMISSION_CODE_VIDEO);
        }
    }

    public class SendOtpAsyncTask extends AsyncTask<Void, Void, Void> {
        String responseBody;
        String mobile, email, fname, lname, password;
        CircularProgressBar bar;

        JSONObject jo;
        HttpClient client;

        public void cancelAsyncTask() {
            if (client != null && !isCancelled()) {
                cancel(true);
                client = null;
            }
        }

        public SendOtpAsyncTask(String mobile, String password, String lname, String fname, String email) {
            this.email = email;
            this.fname = fname;
            this.lname = lname;
            this.password = password;
            this.mobile = mobile;
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
                pairs.add(new BasicNameValuePair("User[mobile]", mobile));
                pairs.add(new BasicNameValuePair("device_token", refreshedToken));
                pairs.add(new BasicNameValuePair("type", "android"));
                pairs.add(new BasicNameValuePair("User[fname]", fname));
                pairs.add(new BasicNameValuePair("User[lname]", lname));
                pairs.add(new BasicNameValuePair("User[email]", email));
                pairs.add(new BasicNameValuePair("User[password]", password));
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
                    JSONObject dataJsonObject;

                    boolean status = false;
                    String otp = "";
                    JSONArray errorArray;
                    if (jo.has("data") && !jo.isNull("data")) {
                        dataJsonObject = jo.getJSONObject("data");

                        if (dataJsonObject.has("status") && !dataJsonObject.isNull("status")) {
                            status = dataJsonObject.getBoolean("status");

                                if (status) {

                                    dataJsonObject = jo.getJSONObject("data");
                                    if (dataJsonObject.has("OTP") && !dataJsonObject.isNull("OTP")) {
                                        otp = dataJsonObject.getString("OTP");
                                    }
                                    Intent intent = new Intent(RegisterActivity.this, OtpActivity.class);
                                    intent.putExtra("fname", firstNameEt.getText().toString());
                                    intent.putExtra("lname", lastNameEt.getText().toString());
                                    intent.putExtra("otp", otp);
                                    intent.putExtra("email", emailEt.getText().toString().trim());
                                    intent.putExtra("phone", phoneEt.getText().toString());
                                    intent.putExtra("password", setPass.getText().toString());
                                    startActivity(intent);
                                }



                             else {
                                if (dataJsonObject.has("message") && !dataJsonObject.isNull("message")) {
                                    errorArray = dataJsonObject.getJSONArray("message");
                                    for (int i = 0; i < errorArray.length(); i++) {
                                        String error = errorArray.getJSONObject(i).getString("error");
                                        Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }}

                }

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    protected boolean validation() {
        if (firstNameEt.getText().toString().length() == 0) {
            Toast.makeText(RegisterActivity.this, "Enter first name.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (lastNameEt.getText().toString().length() == 0) {
            Toast.makeText(RegisterActivity.this, "Enter last name.", Toast.LENGTH_SHORT).show();
            return false;

        } else if (phoneEt.getText().toString().length() == 0) {
            Toast.makeText(RegisterActivity.this, "Enter mobile number.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!Methods.isValidPhoneNumber(phoneEt.getText().toString())) {
            Toast.makeText(RegisterActivity.this, "Enter valid mobile number.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (emailEt.getText().toString().length() == 0) {
            Toast.makeText(RegisterActivity.this, "Enter email address.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!Methods.isValidEmailAddress(emailEt.getText().toString().trim())) {
            Toast.makeText(RegisterActivity.this, "Enter valid email address.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (setPass.getText().toString().length() == 0) {
            Toast.makeText(RegisterActivity.this, "Enter password.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!Methods.isValidPassword(setPass.getText().toString())) {
            Toast.makeText(RegisterActivity.this, "Enter valid password.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (confirmpassEt.getText().toString().length() == 0) {
            Toast.makeText(RegisterActivity.this, "Confirm password.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!confirmpassEt.getText().toString().equalsIgnoreCase(setPass.getText().toString())) {
            Toast.makeText(RegisterActivity.this, "Wrong confirm password.", Toast.LENGTH_SHORT).show();

            return false;

        } else if (!checkbox.isChecked()) {
            Toast.makeText(RegisterActivity.this, "Accept all terms and conditions.", Toast.LENGTH_SHORT).show();

            return false;
        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sendOtpAsyncTask != null && !sendOtpAsyncTask.isCancelled()) {
            sendOtpAsyncTask.cancelAsyncTask();
        }
    }
}
