package com.app.nirogstreet.activites;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.nirogstreet.R;
import com.app.nirogstreet.circularprogressbar.CircularProgressBar;
import com.app.nirogstreet.model.ClinicDetailModel;
import com.app.nirogstreet.model.UserDetailModel;
import com.app.nirogstreet.parser.UserDetailPaser;
import com.app.nirogstreet.uttil.AppUrl;
import com.app.nirogstreet.uttil.ApplicationSingleton;
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
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Preeti on 22-08-2017.
 */

public class Dr_Profile extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.5f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.5f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;
    final Uri imageUri = Uri.parse("http://i.imgur.com/VIlcLfg.jpg");

    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;
    ImageView backimg;
    private AppBarLayout appbar;
    private CollapsingToolbarLayout collapsing;
    private ImageView coverImage;
    private FrameLayout framelayoutTitle;
    private RelativeLayout linearlayoutTitle;
    private TextView textviewTitle;
    TextView nameTv, placeTv, emailTv, phoneTv, WebTv, yearOfBirthTv, yearOfExperienceTv, QualificationTv, aboutHeading, aboutDetail, QualificationSectionTv, SpecializationSectionHeadingTv, sepcilizationDetailTv, consultationFeesHeading, allTaxes, fee, RegistrationSectionHeadingTv, ExperienceSectionTv, clinicAddressHeading, AwardSectionTv, MemberShipSectionTv;
    CircularProgressBar circularProgressBar;
    TextView specilizationTv;
    ImageView backImageView, editInfo, SpecilizationsevicesEdit, QualificationSectionEdit, RegistrationSectionEdit, ExperinceEdit, MemberShipEdit, AwardEdit, clinicAddressEdit;
    String authToken, userId, email, mobile, userName;
    private SesstionManager sesstionManager;
    UserDetailAsyncTask userDetailAsyncTask;
    TextView SpecilizationsevicesTextView, sevicesTextView, sevicesCsvTextView, spcilizationCsv;
    CircleImageView circleImageView;
    LinearLayout qualifictionLinearLayout, regisrtaionLay, experinceLay, clinicLay, awardLay, memberLay;
    UserDetailModel userDetailModel;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;

    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if (!mIsTheTitleVisible) {
                startAlphaAnimation(textviewTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
                backimg.setVisibility(View.GONE);

                textviewTitle.setText(nameTv.getText().toString());

            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(textviewTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);

                mIsTheTitleVisible = false;
            }
        }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (mIsTheTitleContainerVisible) {
                startAlphaAnimation(linearlayoutTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;

            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                textviewTitle.setText(nameTv.getText().toString());
                backimg.setVisibility(View.GONE);
                startAlphaAnimation(linearlayoutTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    public static void startAlphaAnimation(View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_detail);
        findViews();

        toolbar.setTitle("");
        appbar.addOnOffsetChangedListener(this);

        setSupportActionBar(toolbar);
        startAlphaAnimation(textviewTitle, 0, View.INVISIBLE);

        backimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);
        collapsingToolbarLayout.setTitle("demo");
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.black));
        collapsingToolbarLayout.setTitle("My App Title");
        toolbar = (Toolbar) findViewById(R.id.toolbar);


        editInfo = (ImageView) findViewById(R.id.editInfo);
        circleImageView = (CircleImageView) findViewById(R.id.pro);
        specilizationTv = (TextView) findViewById(R.id.specilizationTv);
        sevicesTextView = (TextView) findViewById(R.id.sevices);
        spcilizationCsv = (TextView) findViewById(R.id.spcilizationCsv);
        sevicesCsvTextView = (TextView) findViewById(R.id.sevicesCsv);
        SpecilizationsevicesTextView = (TextView) findViewById(R.id.Specilizationsevices);
        SpecilizationsevicesEdit = (ImageView) findViewById(R.id.SpecilizationsevicesEdit);
        MemberShipEdit = (ImageView) findViewById(R.id.MemberShipEdit);
        awardLay = (LinearLayout) findViewById(R.id.awardLay);
        AwardEdit = (ImageView) findViewById(R.id.AwardEdit);
        clinicAddressEdit = (ImageView) findViewById(R.id.clinicAddressEdit);
        AwardSectionTv = (TextView) findViewById(R.id.AwardSectionTv);
        MemberShipSectionTv = (TextView) findViewById(R.id.MemberShipSectionTv);
        backImageView = (ImageView) findViewById(R.id.back);
        memberLay = (LinearLayout) findViewById(R.id.memberLay);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ExperinceEdit = (ImageView) findViewById(R.id.ExperinceEdit);
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
        TypeFaceMethods.setRegularTypeFaceForTextView(sevicesTextView, Dr_Profile.this);
        TypeFaceMethods.setRegularTypeFaceForTextView(sevicesCsvTextView, Dr_Profile.this);
        TypeFaceMethods.setRegularTypeFaceForTextView(spcilizationCsv, Dr_Profile.this);
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
        TypeFaceMethods.setRegularTypeFaceForTextView(specilizationTv, Dr_Profile.this);

        TypeFaceMethods.setRegularTypeBoldFaceTextView(sepcilizationDetailTv, Dr_Profile.this);
        TypeFaceMethods.setRegularTypeBoldFaceTextView(QualificationSectionTv, Dr_Profile.this);
        TypeFaceMethods.setRegularTypeBoldFaceTextView(AwardSectionTv, Dr_Profile.this);
        TypeFaceMethods.setRegularTypeBoldFaceTextView(MemberShipSectionTv, Dr_Profile.this);
        TypeFaceMethods.setRegularTypeBoldFaceTextView(SpecilizationsevicesTextView, Dr_Profile.this);

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

    private void findViews() {
        appbar = (AppBarLayout) findViewById(R.id.appbar_layout);
        collapsing = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);
        coverImage = (ImageView) findViewById(R.id.imageview_placeholder);
        framelayoutTitle = (FrameLayout) findViewById(R.id.framelayout_title);
        linearlayoutTitle = (RelativeLayout) findViewById(R.id.linearlayout_title);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        textviewTitle = (TextView) findViewById(R.id.textview_title);
        backimg = (ImageView) findViewById(R.id.backimg);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;

        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);
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
                                ApplicationSingleton.setUserDetailModel(userDetailModel);
                                updateContactInfo();
                                updateQualification();
                                updateRegistrationAndDocument();
                                updateExperience();
                                updateClinicInfo();
                                updateAwards();
                                updateSpecilizationAndService();
                                updateMemberShip();
                                SpecilizationsevicesEdit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(Dr_Profile.this, SpecilizationAndService.class);
                                        intent.putExtra("userModel", userDetailModel);
                                        startActivity(intent);
                                    }
                                });
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
                                        Intent intent = new Intent(Dr_Profile.this, Experience.class);
                                        intent.putExtra("userModel", userDetailModel);
                                        startActivity(intent);
                                    }
                                });
                                MemberShipEdit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(Dr_Profile.this, MemberShip.class);
                                        intent.putExtra("userModel", userDetailModel);
                                        startActivity(intent);
                                    }
                                });
                                AwardEdit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(Dr_Profile.this, Award.class);
                                        intent.putExtra("userModel", userDetailModel);
                                        startActivity(intent);
                                    }
                                });
                                clinicAddressEdit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(Dr_Profile.this, AllClinicListing.class);
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


    @Override
    protected void onResume() {
        super.onResume();

        if (ApplicationSingleton.getUserDetailModel() != null) {
            userDetailModel = ApplicationSingleton.getUserDetailModel();
        }
        if (ApplicationSingleton.isContactInfoUpdated()) {
            updateContactInfo();
        }
        if (ApplicationSingleton.isClinicUpdated()) {
            updateClinicInfo();
            ApplicationSingleton.setIsClinicUpdated(false);
        }
        if (ApplicationSingleton.isMemberShipUpdated()) {
            updateMemberShip();
            ApplicationSingleton.setIsMemberShipUpdated(false);
        }
        if (ApplicationSingleton.isAwardUpdated()) {
            updateAwards();
            ApplicationSingleton.setIsAwardUpdated(false);
        }
        if (ApplicationSingleton.isExperinceUpdated()) {
            updateExperience();
            ApplicationSingleton.setIsExperinceUpdated(false);
        }
        if (ApplicationSingleton.isServicesAndSpecializationUpdated()) {

            updateSpecilizationAndService();
            ApplicationSingleton.setServicesAndSpecializationUpdated(false);
        }
        if (ApplicationSingleton.isQualificationUpdated()) {
            updateQualification();
            ApplicationSingleton.setIsQualificationUpdated(false);
        }
        if (ApplicationSingleton.isRegistrationUpdated()) {
            updateRegistrationAndDocument();
            ApplicationSingleton.setRegistrationUpdated(false);
        }
    }

    private void updateSpecilizationAndService() {
        if (userDetailModel != null) {
            if (userDetailModel.getSpecializationModels() != null && userDetailModel.getSpecializationModels().size() != 0) {
                spcilizationCsv.setText(getSelectedNameCsv());
            } else {
                spcilizationCsv.setVisibility(View.GONE);
                specilizationTv.setVisibility(View.GONE);
            }
            if (userDetailModel.getServicesModels() != null && userDetailModel.getServicesModels().size() != 0) {
                sevicesCsvTextView.setText(getSelectedServicesCsv());
                QualificationTv.setText(getSelectedNameCsv());

            } else {
                sevicesCsvTextView.setVisibility(View.GONE);
                sevicesTextView.setVisibility(View.GONE);
            }
            if (userDetailModel.getSpecializationModels() == null && userDetailModel.getServicesModels() == null) {
                SpecilizationsevicesEdit.setImageDrawable(getResources().getDrawable(R.drawable.add));
            } else {
                SpecilizationsevicesEdit.setImageDrawable(getResources().getDrawable(R.drawable.edit));
                SpecilizationsevicesTextView.setText("Service & Specialization");

            }
        }
    }

    private void updateExperience() {
        if (userDetailModel != null) {
            if (userDetailModel.getExperinceModels() == null || userDetailModel.getExperinceModels().size() == 0) {
                ExperienceSectionTv.setText("Add a Experience");
                ExperinceEdit.setImageDrawable(getResources().getDrawable(R.drawable.add));
                experinceLay.removeAllViews();

            } else {
                experinceLay.removeAllViews();
                ExperienceSectionTv.setText("Experience");
                ExperinceEdit.setImageDrawable(getResources().getDrawable(R.drawable.edit));

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

    private void updateMemberShip() {
        if (userDetailModel != null) {
            if (userDetailModel.getMemberShipModels() == null || userDetailModel.getMemberShipModels().size() == 0) {
                MemberShipSectionTv.setText("Add a Membership");
                memberLay.removeAllViews();

                MemberShipEdit.setImageResource(R.drawable.add);
            } else {
                memberLay.removeAllViews();
                MemberShipSectionTv.setText("Membership");
                MemberShipEdit.setImageDrawable(getResources().getDrawable(R.drawable.edit));

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                LayoutInflater layoutInflater = (LayoutInflater) getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                for (int i = 0; i < userDetailModel.getMemberShipModels().size(); i++) {
                    try {
                        View v = LayoutInflater.from(this).inflate(R.layout.profile_qualification_item, null);

                        TextView textView = (TextView) v.findViewById(R.id.clgName);
                        TextView degreename = (TextView) v.findViewById(R.id.degree_name);
                        TextView year = (TextView) v.findViewById(R.id.year_of_passing);
                        degreename.setVisibility(View.GONE);
                        year.setVisibility(View.GONE);
                        TypeFaceMethods.setRegularTypeFaceForTextView(textView, Dr_Profile.this);
                        textView.setText(userDetailModel.getMemberShipModels().get(i).getMembership());
                        v.setLayoutParams(params);

                        memberLay.addView(v);
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
                qualifictionLinearLayout.removeAllViews();

                QualificationSectionTv.setText("Add a Qualification");
                QualificationSectionEdit.setImageResource(R.drawable.add);

            } else {
                qualifictionLinearLayout.removeAllViews();
                QualificationSectionTv.setText("Qualification");
                QualificationSectionEdit.setImageDrawable(getResources().getDrawable(R.drawable.edit));


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
                        degreename.setText(userDetailModel.getQualificationModels().get(i).getClgName());
                        TypeFaceMethods.setRegularTypeFaceForTextView(textView, Dr_Profile.this);
                        textView.setText(userDetailModel.getQualificationModels().get(i).getDegreeName());
                        v.setLayoutParams(params);

                        qualifictionLinearLayout.addView(v);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }
        }

    }

    private void updateAwards() {
        if (userDetailModel != null) {
            if (userDetailModel.getAwardsModels() == null || userDetailModel.getAwardsModels().size() == 0) {
                AwardSectionTv.setText("Add a Award");
                AwardEdit.setImageResource(R.drawable.add);
                awardLay.removeAllViews();


            } else {
                awardLay.removeAllViews();
                AwardSectionTv.setText("Award");
                AwardEdit.setImageDrawable(getResources().getDrawable(R.drawable.edit));

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                LayoutInflater layoutInflater = (LayoutInflater) getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                for (int i = 0; i < userDetailModel.getAwardsModels().size(); i++) {
                    try {
                        View v = LayoutInflater.from(this).inflate(R.layout.profile_qualification_item, null);

                        TextView textView = (TextView) v.findViewById(R.id.clgName);
                        TextView degreename = (TextView) v.findViewById(R.id.degree_name);
                        TextView year = (TextView) v.findViewById(R.id.year_of_passing);
                        TypeFaceMethods.setRegularTypeFaceForTextView(textView, Dr_Profile.this);
                        TypeFaceMethods.setRegularTypeFaceForTextView(year, Dr_Profile.this);
                        year.setText(userDetailModel.getAwardsModels().get(i).getYear());
                        degreename.setVisibility(View.GONE);
                        TypeFaceMethods.setRegularTypeFaceForTextView(textView, Dr_Profile.this);
                        textView.setText(userDetailModel.getAwardsModels().get(i).getAwardName());
                        v.setLayoutParams(params);

                        awardLay.addView(v);
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
            if (userDetailModel.getProfile_pic() != null) {
                ImageLoader imageLoader = new ImageLoader(Dr_Profile.this);
                imageLoader.DisplayImage(Dr_Profile.this, userDetailModel.getProfile_pic(), circleImageView, null, 150, 150, R.drawable.default_image);
            }
        }
    }

    private void updateClinicInfo() {
        if (userDetailModel.getClinicDetailModels() == null || userDetailModel.getClinicDetailModels().size() == 0) {
            clinicAddressHeading.setText("Add a Clinic");
            clinicAddressEdit.setImageResource(R.drawable.add);
            clinicLay.removeAllViews();


        } else {
            clinicLay.removeAllViews();
            clinicAddressHeading.setText("Clinical Address");
            clinicAddressEdit.setImageDrawable(getResources().getDrawable(R.drawable.edit));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            for (int i = 0; i < userDetailModel.getClinicDetailModels().size(); i++) {
                try {
                    View v = LayoutInflater.from(this).inflate(R.layout.clinic_profile_layout, null);
                    TextView consultaionFeesTv = (TextView) v.findViewById(R.id.consultaionFees);
                    TextView feeTv = (TextView) v.findViewById(R.id.fee);
                    TypeFaceMethods.setRegularTypeFaceForTextView(feeTv, Dr_Profile.this);

                    TypeFaceMethods.setRegularTypeFaceForTextView(consultaionFeesTv, Dr_Profile.this);
                    TextView allTaxesTv = (TextView) v.findViewById(R.id.allTaxes);
                    TypeFaceMethods.setRegularTypeFaceForTextView(allTaxesTv, Dr_Profile.this);
                    TextView serviceTv = (TextView) v.findViewById(R.id.service);
                    serviceTv.setText(getSelectedClinicService(userDetailModel.getClinicDetailModels().get(i)));
                    TypeFaceMethods.setRegularTypeFaceForTextView(serviceTv, Dr_Profile.this);
                    serviceTv.setText(getSelectedServicesCsv());
                    TextView textView = (TextView) v.findViewById(R.id.clgName);
                    TextView degreename = (TextView) v.findViewById(R.id.degree_name);
                    TextView year = (TextView) v.findViewById(R.id.year_of_passing);
                    TypeFaceMethods.setRegularTypeFaceForTextView(textView, Dr_Profile.this);
                    TypeFaceMethods.setRegularTypeFaceForTextView(year, Dr_Profile.this);
                    year.setText("Pin Code :" + userDetailModel.getClinicDetailModels().get(i).getPincode());
                    degreename.setText(userDetailModel.getClinicDetailModels().get(i).getAddress() + " " + userDetailModel.getClinicDetailModels().get(i).getCity());
                    TypeFaceMethods.setRegularTypeFaceForTextView(textView, Dr_Profile.this);
                    textView.setText(userDetailModel.getClinicDetailModels().get(i).getName());
                    feeTv.setText(userDetailModel.getClinicDetailModels().get(i).getConsultation_fee());
                    v.setLayoutParams(params);

                    clinicLay.addView(v);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                fee.setText("Rs " + userDetailModel.getClinicDetailModels().get(0).getConsultation_fee());

            }
        }
    }

    public String getSelectedClinicService(ClinicDetailModel clinicDetailModel) {
        String languageCSV = "";

        if (clinicDetailModel != null && clinicDetailModel.getServicesModels() != null && clinicDetailModel.getServicesModels().size() > 0) {
            for (int i = 0; i < clinicDetailModel.getServicesModels().size(); i++) {
                String language = clinicDetailModel.getServicesModels().get(i).getSpecializationName();
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
                RegistrationSectionEdit.setImageResource(R.drawable.add);
                regisrtaionLay.removeAllViews();

            } else {
                regisrtaionLay.removeAllViews();
                RegistrationSectionHeadingTv.setText("Registration & Documents");
                RegistrationSectionEdit.setImageDrawable(getResources().getDrawable(R.drawable.edit));

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
