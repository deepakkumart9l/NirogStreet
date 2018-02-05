package com.app.nirogstreet.activites;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.nirogstreet.R;
import com.app.nirogstreet.circularprogressbar.CircularProgressBar;
import com.app.nirogstreet.model.SpecializationModel;
import com.app.nirogstreet.model.UserDetailModel;
import com.app.nirogstreet.parser.ServicesParser;
import com.app.nirogstreet.parser.SpecialitiesParser;
import com.app.nirogstreet.uttil.AppUrl;
import com.app.nirogstreet.uttil.ApplicationSingleton;
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
 * Created by Preeti on 21-09-2017.
 */
public class SpecilizationAndService extends Activity {
    ImageView backImageView;
    EditText Services_name, sepcialization;
    UserDetailModel userDetailModel;
    boolean isSkip=false;
    boolean isServices = false;
    private static final int RESULT_CODE = 1;
    private String authToken, userId;
    boolean isServiceClicked = false;
    boolean student_intrests=false;
    boolean isSpecilizationClicked = false;
    TextView title_side;
    UpdateSpecializationAndServices updateSpecializationAndServices;
    CircularProgressBar circularProgressBar;
    ArrayList<SpecializationModel> multipleSelectedItemModels = new ArrayList<>();
    ArrayList<SpecializationModel> servicesMultipleSelectedModels = new ArrayList<>();
    TextView saveTv;
    TextView text_email,text_email1;
    private SesstionManager sesstionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.specilization_services);
        text_email=(TextView) findViewById(R.id.text_email);
        text_email1=(TextView)findViewById(R.id.text_email1);
        sesstionManager = new SesstionManager(SpecilizationAndService.this);
        if (sesstionManager.isUserLoggedIn()) {
            authToken = sesstionManager.getUserDetails().get(SesstionManager.AUTH_TOKEN);
            userId = sesstionManager.getUserDetails().get(SesstionManager.USER_ID);
        }
        if(getIntent().hasExtra("isSkip"))
        {
            isSkip=getIntent().getBooleanExtra("isSkip",false);
        }
        if(getIntent().hasExtra("student_intrests"))
        {
            student_intrests=getIntent().getBooleanExtra("student_intrests",false);
        }
        title_side = (TextView) findViewById(R.id.title_side);
        circularProgressBar = (CircularProgressBar) findViewById(R.id.circularProgressBar);
        Services_name = (EditText) findViewById(R.id.Services_name);
        backImageView = (ImageView) findViewById(R.id.back);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        saveTv = (TextView) findViewById(R.id.saveTv);
        saveTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtill.isNetworkAvailable(SpecilizationAndService.this)) {
                    updateSpecializationAndServices = new UpdateSpecializationAndServices();
                    updateSpecializationAndServices.execute();
                }
            }
        });
        sepcialization = (EditText) findViewById(R.id.sepcialization);
if(student_intrests)
{
    text_email1.setVisibility(View.GONE);
    text_email.setVisibility(View.GONE);
    Services_name.setVisibility(View.GONE);
    title_side.setText("Interests");
}
        if (getIntent().hasExtra("userModel")) {
            userDetailModel = (UserDetailModel) getIntent().getSerializableExtra("userModel");
            sepcialization.setText(getSelectedNameCsv());
            Services_name.setText(getSelectedServicesCsv());
            if (userDetailModel.getSpecializationModels() != null && userDetailModel.getSpecializationModels().size() != 0) {
                multipleSelectedItemModels = userDetailModel.getSpecializationModels();
            }
            if (userDetailModel.getServicesModels() != null && userDetailModel.getServicesModels().size() != 0) {
                servicesMultipleSelectedModels = userDetailModel.getServicesModels();
            }
        }
        Services_name.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!isServiceClicked) {
                    isServiceClicked = true;
                    isServices = true;
                    Intent intent = new Intent(SpecilizationAndService.this, Multi_Select_Search_specialization.class);
                    intent.putExtra("isService", true);

                    if (multipleSelectedItemModels != null && multipleSelectedItemModels.size() > 0)
                        intent.putExtra("list", servicesMultipleSelectedModels);

                    else if (userDetailModel.getSpecializationModels() != null && userDetailModel.getSpecializationModels().size() > 0)
                        intent.putExtra("list", userDetailModel.getSpecializationModels());

                    startActivityForResult(intent, RESULT_CODE);
                }
                return false;
            }
        });
        Services_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        sepcialization.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!isSpecilizationClicked) {
                    isSpecilizationClicked = true;
                    isServices = false;
                    Intent intent = new Intent(SpecilizationAndService.this, Multi_Select_Search_specialization.class);
                    if (multipleSelectedItemModels != null && multipleSelectedItemModels.size() > 0)
                        intent.putExtra("list", multipleSelectedItemModels);

                    else if (userDetailModel.getSpecializationModels() != null && userDetailModel.getSpecializationModels().size() > 0)
                        intent.putExtra("list", userDetailModel.getSpecializationModels());

                    startActivityForResult(intent, RESULT_CODE);
                }
                return false;
            }
        });
        sepcialization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    public String getSelectedNameCsv() {
        String languageCSV = "";

        if (userDetailModel != null && userDetailModel.getSpecializationModels() != null && userDetailModel.getSpecializationModels().size() > 0) {
            for (int i = 0; i < userDetailModel.getSpecializationModels().size(); i++) {
                String language = userDetailModel.getSpecializationModels().get(i).getSpecializationName();
                if (language != null && !language.trim().isEmpty()
                        && languageCSV != null && !languageCSV.trim().isEmpty())
                    languageCSV = languageCSV + ", ";
                languageCSV = languageCSV + language;

            }
        }
        return languageCSV;
    }

    public String getSelectedServicesCsv() {
        String languageCSV = "";

        if (userDetailModel != null && userDetailModel.getServicesModels() != null && userDetailModel.getServicesModels().size() > 0) {
            for (int i = 0; i < userDetailModel.getServicesModels().size(); i++) {
                String language = userDetailModel.getServicesModels().get(i).getSpecializationName();
                if (language != null && !language.trim().isEmpty()
                        && languageCSV != null && !languageCSV.trim().isEmpty())
                    languageCSV = languageCSV + ", ";
                languageCSV = languageCSV + language;

            }
        }
        return languageCSV;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isServiceClicked = false;
        isSpecilizationClicked = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_CODE) {
            if (data != null) {
                if (!isServices) {
                    String s = data.getStringExtra("friendsCsv");
                    sepcialization.setText(s);
                    System.out.print(s);
                    multipleSelectedItemModels = (ArrayList<SpecializationModel>) data.getSerializableExtra("list");
                } else {
                    String s = data.getStringExtra("friendsCsv");
                    Services_name.setText(s);
                    System.out.print(s);
                    servicesMultipleSelectedModels = (ArrayList<SpecializationModel>) data.getSerializableExtra("list");
                }
            }
        }
    }

    public class UpdateSpecializationAndServices extends AsyncTask<Void, Void, Void> {
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

                String url = AppUrl.AppBaseUrl + "user/services-specializations";
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

                String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                if (multipleSelectedItemModels != null && multipleSelectedItemModels.size() > 0) {
                    for (int i = 0; i < multipleSelectedItemModels.size(); i++) {
                        pairs.add(new BasicNameValuePair("Specializations[name][" + i + "]", multipleSelectedItemModels.get(i).getSpecializationName()));
                        if (multipleSelectedItemModels.get(i).getId() != null)
                            pairs.add(new BasicNameValuePair("Specializations[id][" + i + "]", multipleSelectedItemModels.get(i).getId()));
                        else
                            pairs.add(new BasicNameValuePair("Specializations[id][" + i + "]", ""));


                    }
                }
                if (servicesMultipleSelectedModels != null && servicesMultipleSelectedModels.size() > 0) {
                    for (int i = 0; i < servicesMultipleSelectedModels.size(); i++) {
                        pairs.add(new BasicNameValuePair("Services[name][" + i + "]", servicesMultipleSelectedModels.get(i).getSpecializationName()));
                        if (servicesMultipleSelectedModels.get(i).getId() != null)
                            pairs.add(new BasicNameValuePair("Services[id][" + i + "]", servicesMultipleSelectedModels.get(i).getId()));
                        else
                            pairs.add(new BasicNameValuePair("Services[id][" + i + "]", ""));

                    }
                }
                httppost.setHeader("Authorization", "Basic " + authToken);

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
                                UserDetailModel userDetailModel = ApplicationSingleton.getUserDetailModel();
                                if (dataJsonObject.has("services") && !dataJsonObject.isNull("services")) {
                                    ArrayList<SpecializationModel> specializationModels = ServicesParser.serviceParser(dataJsonObject);
                                    userDetailModel.setServicesModels(specializationModels);
                                }
                                if (dataJsonObject.has("specialities") && !dataJsonObject.isNull("specialities")) {
                                    ArrayList<SpecializationModel> specializationModels = SpecialitiesParser.specilities(dataJsonObject);
                                    userDetailModel.setSpecializationModels(specializationModels);
                                }
                                if(dataJsonObject.has("profile_complete")&&!dataJsonObject.isNull("profile_complete"))
                                {
                                    ApplicationSingleton.getUserDetailModel().setProfile_complete(dataJsonObject.getInt("profile_complete"));
                                }
                                ApplicationSingleton.setUserDetailModel(userDetailModel);
                                ApplicationSingleton.setServicesAndSpecializationUpdated(true);
                                if(isSkip)
                                {
                                    Intent intent=new Intent(SpecilizationAndService.this,RegistrationAndDocuments.class);
                                    intent.putExtra("isSkip",true);
                                    startActivity(intent);
                                }else
                                finish();
                            } else {
                                if (dataJsonObject.has("message") && !dataJsonObject.isNull("message")) {
                                    errorArray = dataJsonObject.getJSONArray("message");
                                    for (int i = 0; i < errorArray.length(); i++) {
                                        String error = errorArray.getJSONObject(i).getString("error");
                                        Toast.makeText(SpecilizationAndService.this, error, Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onPause() {
        super.onPause();
        if (updateSpecializationAndServices != null && !updateSpecializationAndServices.isCancelled()) {
            updateSpecializationAndServices.cancelAsyncTask();
        }
    }
}
