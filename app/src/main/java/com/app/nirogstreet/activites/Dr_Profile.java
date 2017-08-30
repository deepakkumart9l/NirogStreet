package com.app.nirogstreet.activites;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.Serializable;
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
    TextView nameTv, placeTv, emailTv, phoneTv, WebTv, yearOfBirthTv, yearOfExperienceTv, QualificationTv, aboutHeading, aboutDetail, QualificationSectionTv, SpecializationSectionHeadingTv, sepcilizationDetailTv, consultationFeesHeading, allTaxes, fee, RegistrationSectionHeadingTv, ExperienceSectionTv, clinicAddressHeading;
    CircularProgressBar circularProgressBar;
    ImageView backImageView, editInfo, QualificationSectionEdit, RegistrationSectionEdit,ExperinceEdit;
    String authToken, userId, email, mobile, userName;
    private SesstionManager sesstionManager;
    UserDetailAsyncTask userDetailAsyncTask;
    LinearLayout qualifictionLinearLayout, regisrtaionLay, experinceLay, clinicLay;
    UserDetailModel userDetailModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_detail);
        editInfo = (ImageView) findViewById(R.id.editInfo);
        backImageView = (ImageView) findViewById(R.id.back);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ExperinceEdit=(ImageView)findViewById(R.id.ExperinceEdit);
        RegistrationSectionEdit = (ImageView) findViewById(R.id.RegistrationSectionEdit);
        QualificationSectionEdit = (ImageView) findViewById(R.id.QualificationSectionEdit);
        experinceLay = (LinearLayout) findViewById(R.id.experinceLay);
        qualifictionLinearLayout = (LinearLayout) findViewById(R.id.QualificatonLinearLayout);
        regisrtaionLay = (LinearLayout) findViewById(R.id.regisrtaionLay);
        clinicLay = (LinearLayout) findViewById(R.id.clinicLay);
        clinicAddressHeading = (TextView) findViewById(R.id.clinicAddressHeading);
        fee = (TextView) findViewById(R.id.fee);
        nameTv = (TextView) findViewById(R.id.nameTv);
        ExperienceSectionTv = (TextView) findViewById(R.id.ExperienceSectionTv);
        placeTv = (TextView) findViewById(R.id.placeTv);
        emailTv = (TextView) findViewById(R.id.emailTv);
        phoneTv = (TextView) findViewById(R.id.phoneTv);
        WebTv = (TextView) findViewById(R.id.WebTv);
        RegistrationSectionHeadingTv = (TextView) findViewById(R.id.RegistrationSectionHeadingTv);
        aboutHeading = (TextView) findViewById(R.id.about);
        aboutDetail = (TextView) findViewById(R.id.about_detail);
        aboutDetail.setLineSpacing(1, 1);
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
        TypeFaceMethods.setRegularTypeBoldFaceTextView(fee, Dr_Profile.this);

        TypeFaceMethods.setRegularTypeBoldFaceTextView(ExperienceSectionTv, Dr_Profile.this);

        TypeFaceMethods.setRegularTypeBoldFaceTextView(sepcilizationDetailTv, Dr_Profile.this);
        TypeFaceMethods.setRegularTypeBoldFaceTextView(QualificationSectionTv, Dr_Profile.this);

        TypeFaceMethods.setRegularTypeBoldFaceTextView(SpecializationSectionHeadingTv, Dr_Profile.this);
        TypeFaceMethods.setRegularTypeFaceForTextView(consultationFeesHeading, Dr_Profile.this);
        TypeFaceMethods.setRegularTypeBoldFaceTextView(RegistrationSectionHeadingTv, Dr_Profile.this);

        TypeFaceMethods.setRegularTypeBoldFaceTextView(aboutHeading, Dr_Profile.this);
        TypeFaceMethods.setRegularTypeBoldFaceTextView(clinicAddressHeading, Dr_Profile.this);

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
                                        Toast.makeText(Dr_Profile.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                userDetailModel = UserDetailPaser.userDetailParser(dataJsonObject);
                                updateContactInfo();
                                updateQualification();
                                updateRegistrationAndDocument();
                                updateExperience();
                                updateClinicInfo();
                                editInfo.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(Dr_Profile.this, CreateDrProfile.class);
                                        intent.putExtra("userModel", (Serializable) userDetailModel);
                                        startActivity(intent);
                                    }
                                });
                                QualificationSectionEdit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(Dr_Profile.this, Dr_Qualifications.class);
                                        intent.putExtra("userModel", userDetailModel);
                                        startActivity(intent);
                                    }
                                });
                                RegistrationSectionEdit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(Dr_Profile.this, RegistrationAndDocuments.class);
                                        intent.putExtra("userModel", userDetailModel);
                                        startActivity(intent);
                                    }
                                });
                                ExperinceEdit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(Dr_Profile.this, AddOrEditExperience.class);
                                        intent.putExtra("userModel", userDetailModel);
                                        startActivity(intent);
                                    }
                                });
                            }

                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    private void updateExperience() {
        if (userDetailModel != null) {
            if (userDetailModel.getExperinceModels() == null || userDetailModel.getExperinceModels().size() == 0) {
                ExperienceSectionTv.setText("Add a Experience");
            } else {
                experinceLay.removeAllViews();
                ExperienceSectionTv.setText("Experience");

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                LayoutInflater layoutInflater = (LayoutInflater) getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                for (int i = 0; i < userDetailModel.getExperinceModels().size(); i++) {
                    try {
                        View v = LayoutInflater.from(this).inflate(R.layout.profile_qualification_item, null);

                        TextView textView = (TextView) v.findViewById(R.id.clgName);
                        TextView degreename = (TextView) v.findViewById(R.id.degree_name);
                        TextView year = (TextView) v.findViewById(R.id.year_of_passing);
                        TypeFaceMethods.setRegularTypeFaceForTextView(textView, Dr_Profile.this);
                        TypeFaceMethods.setRegularTypeFaceForTextView(year, Dr_Profile.this);
                        year.setText(userDetailModel.getExperinceModels().get(i).getStart_time() + " - " + userDetailModel.getExperinceModels().get(i).getEnd_time());
                        degreename.setVisibility(View.GONE);
                        TypeFaceMethods.setRegularTypeFaceForTextView(textView, Dr_Profile.this);
                        textView.setText(userDetailModel.getExperinceModels().get(i).getOrganizationName());
                        v.setLayoutParams(params);

                        experinceLay.addView(v);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }
        }

    }

    private void updateQualification() {
        if (userDetailModel != null) {
            if (userDetailModel.getQualificationModels() == null || userDetailModel.getQualificationModels().size() == 0) {
                QualificationSectionTv.setText("Add a Qualification");
            } else {
                qualifictionLinearLayout.removeAllViews();
                QualificationSectionTv.setText("Qualification");

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                LayoutInflater layoutInflater = (LayoutInflater) getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                for (int i = 0; i < userDetailModel.getQualificationModels().size(); i++) {
                    try {
                        View v = LayoutInflater.from(this).inflate(R.layout.profile_qualification_item, null);

                        TextView textView = (TextView) v.findViewById(R.id.clgName);
                        TextView degreename = (TextView) v.findViewById(R.id.degree_name);
                        TextView year = (TextView) v.findViewById(R.id.year_of_passing);
                        TypeFaceMethods.setRegularTypeFaceForTextView(textView, Dr_Profile.this);
                        TypeFaceMethods.setRegularTypeFaceForTextView(year, Dr_Profile.this);
                        year.setText(userDetailModel.getQualificationModels().get(i).getPassingYear());
                        degreename.setText(userDetailModel.getQualificationModels().get(i).getDegreeName());
                        TypeFaceMethods.setRegularTypeFaceForTextView(textView, Dr_Profile.this);
                        textView.setText(userDetailModel.getQualificationModels().get(i).getClgName());
                        v.setLayoutParams(params);

                        qualifictionLinearLayout.addView(v);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }
        }

    }


    private void updateContactInfo() {
        if (userDetailModel != null) {
            if (userDetailModel.getExperience() != null && !userDetailModel.getExperience().equalsIgnoreCase("")) {
                yearOfExperienceTv.setText(userDetailModel.getExperience() + " " + "years experience");
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
            if (userDetailModel != null && userDetailModel.getSpecializationModels() != null) {
                QualificationTv.setText(getSelectedNameCsv());
            }
        }
    }

    private void updateClinicInfo() {
        if (userDetailModel.getClinicDetailModels() == null || userDetailModel.getClinicDetailModels().size() == 0) {
            clinicAddressHeading.setText("Add a Clinic");
        } else {
            clinicLay.removeAllViews();
            clinicAddressHeading.setText("Clinical Address");

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            for (int i = 0; i < userDetailModel.getClinicDetailModels().size(); i++) {
                try {
                    View v = LayoutInflater.from(this).inflate(R.layout.profile_qualification_item, null);

                    TextView textView = (TextView) v.findViewById(R.id.clgName);
                    TextView degreename = (TextView) v.findViewById(R.id.degree_name);
                    TextView year = (TextView) v.findViewById(R.id.year_of_passing);
                    TypeFaceMethods.setRegularTypeFaceForTextView(textView, Dr_Profile.this);
                    TypeFaceMethods.setRegularTypeFaceForTextView(year, Dr_Profile.this);
                    year.setText("Pin Code :" + userDetailModel.getClinicDetailModels().get(i).getPincode());
                    degreename.setText(userDetailModel.getClinicDetailModels().get(i).getAddress() + " " + userDetailModel.getClinicDetailModels().get(i).getCity());
                    TypeFaceMethods.setRegularTypeFaceForTextView(textView, Dr_Profile.this);
                    textView.setText(userDetailModel.getClinicDetailModels().get(i).getName());
                    v.setLayoutParams(params);

                    clinicLay.addView(v);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                fee.setText("Rs " + userDetailModel.getClinicDetailModels().get(0).getConsultation_fee());

            }
        }
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

    public void updateRegistrationAndDocument() {
        if (userDetailModel != null) {
            if (userDetailModel.getRegistrationAndDocumenModels() == null || userDetailModel.getRegistrationAndDocumenModels().size() == 0) {
                RegistrationSectionHeadingTv.setText("Add a registration & documents");
            } else {
                regisrtaionLay.removeAllViews();
                RegistrationSectionHeadingTv.setText("Registration & Documents");

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                LayoutInflater layoutInflater = (LayoutInflater) getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                for (int i = 0; i < userDetailModel.getRegistrationAndDocumenModels().size(); i++) {
                    try {
                        View v = LayoutInflater.from(this).inflate(R.layout.profile_qualification_item, null);

                        TextView textView = (TextView) v.findViewById(R.id.clgName);
                        TextView degreename = (TextView) v.findViewById(R.id.degree_name);
                        TextView year = (TextView) v.findViewById(R.id.year_of_passing);
                        TypeFaceMethods.setRegularTypeFaceForTextView(textView, Dr_Profile.this);
                        TypeFaceMethods.setRegularTypeFaceForTextView(year, Dr_Profile.this);
                        year.setText(userDetailModel.getRegistrationAndDocumenModels().get(i).getCouncil_year());
                        degreename.setText(userDetailModel.getRegistrationAndDocumenModels().get(i).getCouncil_registration_number());
                        TypeFaceMethods.setRegularTypeFaceForTextView(textView, Dr_Profile.this);
                        textView.setText(userDetailModel.getRegistrationAndDocumenModels().get(i).getCouncil_registration_number());
                        v.setLayoutParams(params);

                        regisrtaionLay.addView(v);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
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
