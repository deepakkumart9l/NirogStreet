package com.app.nirogstreet.activites;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.app.nirogstreet.R;
import com.app.nirogstreet.circularprogressbar.CircularProgressBar;
import com.app.nirogstreet.model.SpecializationModel;
import com.app.nirogstreet.uttil.AppUrl;
import com.app.nirogstreet.uttil.SesstionManager;
import com.app.nirogstreet.uttil.TypeFaceMethods;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.conn.scheme.Scheme;
import cz.msebera.android.httpclient.conn.ssl.SSLSocketFactory;
import cz.msebera.android.httpclient.entity.mime.HttpMultipartMode;
import cz.msebera.android.httpclient.entity.mime.MultipartEntityBuilder;
import cz.msebera.android.httpclient.entity.mime.content.FileBody;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;

/**
 * Created by Preeti on 25-08-2017.
 */

public class Dr_Qualifications extends AppCompatActivity {
    CircularProgressBar circularProgressBar;
    TextView addQualificationTextView, addTextView;
    SesstionManager sesstionManager;
    TextView titileText, skipTextView;
    boolean isSkip = false;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qualifications);
        sesstionManager = new SesstionManager(Dr_Qualifications.this);
        if (getIntent().hasExtra("isSkip")) {
            isSkip = getIntent().getBooleanExtra("isSkip", false);
        }

        skipTextView = (TextView) findViewById(R.id.skip);
        if (isSkip) {
            skipTextView.setVisibility(View.VISIBLE);
        } else {
            skipTextView.setVisibility(View.GONE);
        }
        skipTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dr_Qualifications.this, RegistrationAndDocuments.class);
                intent.putExtra("isSkip", true);
                startActivity(intent);
            }
        });
        TypeFaceMethods.setRegularTypeFaceForTextView(skipTextView, Dr_Qualifications.this);

        titileText = (TextView) findViewById(R.id.title_side_left);
        TypeFaceMethods.setRegularTypeFaceForTextView(titileText, Dr_Qualifications.this);
        addQualificationTextView = (TextView) findViewById(R.id.addQualification);
        TypeFaceMethods.setRegularTypeFaceForTextView(addQualificationTextView, Dr_Qualifications.this);
        addQualificationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dr_Qualifications.this, EditQualificationDetailOrAddQualificationsDetails.class);
                startActivity(intent);
            }
        });

    }


}
