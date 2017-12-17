package com.app.nirogstreet.activites;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.nirogstreet.R;
import com.app.nirogstreet.circularprogressbar.CircularProgressBar;
import com.app.nirogstreet.model.Course_Detail_model;
import com.app.nirogstreet.parser.Course_Detail_Parser;
import com.app.nirogstreet.uttil.AppUrl;
import com.app.nirogstreet.uttil.ApplicationSingleton;
import com.app.nirogstreet.uttil.Methods;
import com.app.nirogstreet.uttil.NetworkUtill;
import com.app.nirogstreet.uttil.SesstionManager;
import com.app.nirogstreet.uttil.TypeFaceMethods;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;

import butterknife.BindView;
import butterknife.ButterKnife;
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
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Preeti on 11-12-2017.
 */

public class Knowledge_Centre_Detail extends Activity {
    TextView title_side_Tv;
    RelativeLayout relativeLayout;
    CorseDetailAsynctask corseDetailAsynctask;
    TextView addQualificationTextView;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    KnwledgeCompleteAsynctask knwledgeCompleteAsynctask;
    LinearLayout main_LinearLayout;
    Course_Detail_model course_detail_model;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    AcceptDeclineJoinAsyncTask acceptDeclineJoinAsyncTask;
    SesstionManager sesstionManager;
    CircleImageView auth_imageCircleImageView;
    TextView dr_name_TV;
    String courseId;
    boolean isHide = false;

    ImageView backImageView;
    TextView viewTv;
    CircularProgressBar circularProgressBar;

    TextView titleTv;

    TextView descriptionTv;

    public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                try {


                    ViewTreeObserver obs = tv.getViewTreeObserver();
                    obs.removeGlobalOnLayoutListener(this);
                    if (maxLine == 0) {
                        int lineEndIndex = tv.getLayout().getLineEnd(0);
                        String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                        tv.setText(text);
                        tv.setMovementMethod(LinkMovementMethod.getInstance());
                        tv.setText(
                                addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                        viewMore), TextView.BufferType.SPANNABLE);
                    } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                        int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                        String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                        tv.setText(text);
                        tv.setMovementMethod(LinkMovementMethod.getInstance());
                        tv.setText(
                                addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                        viewMore), TextView.BufferType.SPANNABLE);
                    } else {
                        int lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                        String text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                        tv.setText(text);
                        tv.setMovementMethod(LinkMovementMethod.getInstance());
                        tv.setText(
                                addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, lineEndIndex, expandText,
                                        viewMore), TextView.BufferType.SPANNABLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
                                                                            final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {
            ssb.setSpan(new ClickableSpan() {

                @Override
                public void onClick(View widget) {

                    if (viewMore) {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, -1, "view less", false);
                    } else {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, 3, "view more", true);
                    }

                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.knwoledge_detail);
        title_side_Tv = (TextView) findViewById(R.id.title_side);
        dr_name_TV = (TextView) findViewById(R.id.dr_name);
        backImageView = (ImageView) findViewById(R.id.back);
        relativeLayout=(RelativeLayout)findViewById(R.id.rel);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        viewTv = (TextView) findViewById(R.id.view);
        if (getIntent().hasExtra("courseID")) {
            courseId = getIntent().getStringExtra("courseID");
        }
        if (getIntent().hasExtra("isHide")) {
            isHide = getIntent().getBooleanExtra("isHide", false);
        }
        auth_imageCircleImageView = (CircleImageView) findViewById(R.id.auth_image);
        addQualificationTextView = (TextView) findViewById(R.id.addQualification);
        TypeFaceMethods.setRegularTypeBoldFaceTextView(addQualificationTextView, Knowledge_Centre_Detail.this);
        sesstionManager = new SesstionManager(Knowledge_Centre_Detail.this);
        main_LinearLayout = (LinearLayout) findViewById(R.id.mainCrad);
        circularProgressBar = (CircularProgressBar) findViewById(R.id.circularProgressBar);
        TypeFaceMethods.setRegularTypeBoldFaceTextView(title_side_Tv, this);
        TypeFaceMethods.setRegularTypeBoldFaceTextView(dr_name_TV, this);
        TypeFaceMethods.setRegularTypeFaceForTextView(viewTv, this);
        if (isHide) {
            addQualificationTextView.setVisibility(View.GONE);
        } else {
            addQualificationTextView.setVisibility(View.VISIBLE);
        }
        if (NetworkUtill.isNetworkAvailable(Knowledge_Centre_Detail.this)) {
            corseDetailAsynctask = new CorseDetailAsynctask("", "");
            corseDetailAsynctask.execute();

        } else {
            NetworkUtill.showNoInternetDialog(Knowledge_Centre_Detail.this);
        }
        addQualificationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtill.isNetworkAvailable(Knowledge_Centre_Detail.this)) {
                    acceptDeclineJoinAsyncTask = new AcceptDeclineJoinAsyncTask();
                    acceptDeclineJoinAsyncTask.execute();
                } else {
                    NetworkUtill.showNoInternetDialog(Knowledge_Centre_Detail.this);
                }
            }
        });
    }

    public class AcceptDeclineJoinAsyncTask extends AsyncTask<Void, Void, Void> {
        String authToken;
        JSONObject jo;
        String groupId, userId;

        int status1;
        HttpClient client;
        int pos;
        private String responseBody;

        public AcceptDeclineJoinAsyncTask() {

        }

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
                if (jo != null) {


                    if (jo.has("response") && !jo.isNull("response")) {
                        JSONObject jsonObjectresponse = jo.getJSONObject("response");
                        if (jsonObjectresponse.has("message") && !jsonObjectresponse.isNull("message")) {
                            ApplicationSingleton.setCourseSubscribe(true);
                            isHide=true;
                            Toast.makeText(Knowledge_Centre_Detail.this, jsonObjectresponse.getString("message"), Toast.LENGTH_LONG).show();
                            addQualificationTextView.setVisibility(View.GONE);


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


                String url = AppUrl.BaseUrl + "knowledge/subscribe";
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
                pairs.add(new BasicNameValuePair("courseID", courseId));
                pairs.add(new BasicNameValuePair("userID", sesstionManager.getUserDetails().get(SesstionManager.USER_ID) + ""));
                pairs.add(new BasicNameValuePair("status", "1"));
                httppost.setHeader("Authorization", "Basic " + authToken);

                httppost.setEntity(new UrlEncodedFormEntity(pairs));
                response = client.execute(httppost);

                responseBody = EntityUtils
                        .toString(response.getEntity());
                jo = new JSONObject(responseBody);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ApplicationSingleton.setCourse_detail_model(null);
    }

    public class CorseDetailAsynctask extends AsyncTask<Void, Void, Void> {
        JSONObject jo;
        String authToken, userId, feedFrom;
        private String responseBody;
        String locationString;
        String messageText;
        String groupName;
        HttpClient client;
        Context context;
        String noteText;

        public CorseDetailAsynctask(String userid, String authToken) {
            this.feedFrom = feedFrom;
            this.userId = userid;
            this.authToken = authToken;

        }

        public void cancelAsyncTask() {
            if (client != null && !isCancelled()) {
                cancel(true);
                client = null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            circularProgressBar.setVisibility(View.VISIBLE);


        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {

                String url = AppUrl.BaseUrl + "knowledge/detail";
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
                pairs.add(new BasicNameValuePair("userID", sesstionManager.getUserDetails().get(SesstionManager.USER_ID)));
                pairs.add(new BasicNameValuePair("courseID", courseId));
                httppost.setHeader("Authorization", "Basic " + sesstionManager.getUserDetails().get(SesstionManager.AUTH_TOKEN));
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
                    course_detail_model = Course_Detail_Parser.course_detail_Parser(jo);

                    if (course_detail_model != null) {
                        ApplicationSingleton.setCourse_detail_model(course_detail_model);
                        title_side_Tv.setText(course_detail_model.getName());

                        if (course_detail_model.getAuthor_detail_module().getProfile_pic() != null)
                            Picasso.with(Knowledge_Centre_Detail.this)
                                    .load(course_detail_model.getAuthor_detail_module().getProfile_pic())
                                    .placeholder(R.drawable.user)
                                    .error(R.drawable.user)
                                    .into(auth_imageCircleImageView);
                        dr_name_TV.setText(course_detail_model.getAuthor_detail_module().getName());
                        relativeLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Knowledge_Centre_Detail.this, Dr_Profile.class);
                                if (!course_detail_model.getAuthor_detail_module().getId().equalsIgnoreCase(sesstionManager.getUserDetails().get(SesstionManager.USER_ID)))

                                    intent.putExtra("UserId", course_detail_model.getAuthor_detail_module().getId());
                                startActivity(intent);
                            }
                        });
                        auth_imageCircleImageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Knowledge_Centre_Detail.this, Dr_Profile.class);
                                if (!course_detail_model.getAuthor_detail_module().getId().equalsIgnoreCase(sesstionManager.getUserDetails().get(SesstionManager.USER_ID)))

                                    intent.putExtra("UserId", course_detail_model.getAuthor_detail_module().getId());
                                startActivity(intent);
                            }
                        });
                        update();
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
        update();
    }

    private void update() {
        if (ApplicationSingleton.getCourse_detail_model() != null) {

            main_LinearLayout.removeAllViews();
            course_detail_model = ApplicationSingleton.getCourse_detail_model();
            if (course_detail_model.getModulesModels() != null && course_detail_model.getModulesModels().size() > 0)
                for (int k = 0; k < course_detail_model.getModulesModels().size(); k++) {
                    View mainCards = LayoutInflater.from(Knowledge_Centre_Detail.this).inflate(R.layout.knwoledge_centre_main_card, null);
                    LinearLayout linearLayoutmain = (LinearLayout) mainCards.findViewById(R.id.main_lay);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    LayoutInflater layoutInflater = (LayoutInflater) getSystemService(
                            Context.LAYOUT_INFLATER_SERVICE);

                    TextView titleTextView = (TextView) mainCards.findViewById(R.id.title);
                    int pos = k + 1;
                    titleTextView.setText("Module " + pos + " : " + course_detail_model.getModulesModels().get(k).getName());
                    TextView descriptionTextView = (TextView) mainCards.findViewById(R.id.description);
                    descriptionTextView.setText(course_detail_model.getModulesModels().get(k).getDescription());
                    TypeFaceMethods.setRegularTypeBoldFaceTextView(titleTextView, Knowledge_Centre_Detail.this);
                    TypeFaceMethods.setRegularTypeFaceForTextView(descriptionTextView, Knowledge_Centre_Detail.this);
                    if (descriptionTextView!=null&&descriptionTextView.getText().length() > 170)
                        makeTextViewResizable(descriptionTextView, 4, "view more", true);
                    else {
                        descriptionTextView.setText(course_detail_model.getModulesModels().get(k).getDescription());
                    }
                    if (course_detail_model.getModulesModels().get(k).getTopic_under_modules() != null && course_detail_model.getModulesModels().get(k).getTopic_under_modules().size() > 0)
                        for (int i = 0; i < course_detail_model.getModulesModels().get(k).getTopic_under_modules().size(); i++) {
                            try {
                                View v = LayoutInflater.from(Knowledge_Centre_Detail.this).inflate(R.layout.module_item, null);
                                LinearLayout linearLayout = (LinearLayout) v.findViewById(R.id.lay);
                                TextView titleTextViewModule = (TextView) v.findViewById(R.id.title);
                                int pos1 = i + 1;
                                titleTextViewModule.setText(pos1 + ". " + course_detail_model.getModulesModels().get(k).getTopic_under_modules().get(i).getName());
                                TypeFaceMethods.setRegularTypeBoldFaceTextView(titleTextViewModule, Knowledge_Centre_Detail.this);
                                if (course_detail_model.getModulesModels().get(k).getTopic_under_modules().get(i).getFile_under_topics() != null && course_detail_model.getModulesModels().get(k).getTopic_under_modules().get(i).getFile_under_topics().size() > 0)
                                    for (int j = 0; j < course_detail_model.getModulesModels().get(k).getTopic_under_modules().get(i).getFile_under_topics().size(); j++) {
                                        View v1 = LayoutInflater.from(Knowledge_Centre_Detail.this).inflate(R.layout.module_item_check, null);
                                        TextView fileName = (TextView) v1.findViewById(R.id.txt);
                                        ImageView checkBox = (ImageView) v1.findViewById(R.id.checkbox);
                                        TypeFaceMethods.setRegularTypeFaceForTextView(fileName, Knowledge_Centre_Detail.this);
                                        String filename = course_detail_model.getModulesModels().get(k).getTopic_under_modules().get(i).getFile_under_topics().get(j).getName();
                                        String doc_Type = course_detail_model.getModulesModels().get(k).getTopic_under_modules().get(i).getFile_under_topics().get(j).getDoc_type();
                                        ImageView src = (ImageView) v1.findViewById(R.id.image);
                                        if (doc_Type.equalsIgnoreCase("3")) {
                                            src.setImageResource(R.drawable.kc_vdo);
                                            final int finalI = i;
                                            final int finalJ = j;
                                            final int finalK = k;
                                            v1.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if(isHide) {
                                                        Intent intent = new Intent(Knowledge_Centre_Detail.this, Module_Detail_Activity.class);
                                                        intent.putExtra("url", course_detail_model.getModulesModels().get(finalK).getTopic_under_modules().get(finalI).getFile_under_topics().get(finalJ).getKc_file());
                                                        intent.putExtra("id", course_detail_model.getModulesModels().get(finalK).getTopic_under_modules().get(finalI).getFile_under_topics().get(finalJ).getId());
                                                        intent.putExtra("title", course_detail_model.getModulesModels().get(finalK).getTopic_under_modules().get(finalI).getFile_under_topics().get(finalJ).getName());
                                                        intent.putExtra("course_detail_model", course_detail_model);
                                                        intent.putExtra("module_pos", finalK);
                                                        intent.putExtra("topic_pos", finalI);
                                                        intent.putExtra("file_pos", finalJ);
                                                        startActivity(intent);
                                                    }
                                                }
                                            });
                                        }
                                        if(doc_Type.equalsIgnoreCase("1"))
                                        {
                                            final int finalI = i;
                                            final int finalJ = j;
                                            final int finalK = k;
                                            v1.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if(isHide){

                                                        Intent intent = new Intent(Knowledge_Centre_Detail.this, LoadHtmlPageActivity.class);
                                                        intent.putExtra("id", course_detail_model.getModulesModels().get(finalK).getTopic_under_modules().get(finalI).getFile_under_topics().get(finalJ).getId());
                                                        intent.putExtra("title", course_detail_model.getModulesModels().get(finalK).getTopic_under_modules().get(finalI).getFile_under_topics().get(finalJ).getName());
                                                        intent.putExtra("course_detail_model", course_detail_model);
                                                        intent.putExtra("module_pos", finalK);
                                                        intent.putExtra("topic_pos", finalI);
                                                        intent.putExtra("file_pos", finalJ);
                                                        intent.putExtra("url", course_detail_model.getModulesModels().get(finalK).getTopic_under_modules().get(finalI).getFile_under_topics().get(finalJ).getKc_file());
                                                        startActivity(intent);}
                                                 /*   verifyStoragePermissions(Knowledge_Centre_Detail.this);

                                                    String extntion = course_detail_model.getModulesModels().get(finalK).getTopic_under_modules().get(finalI).getFile_under_topics().get(finalJ).getKc_file().substring(course_detail_model.getModulesModels().get(finalK).getTopic_under_modules().get(finalI).getFile_under_topics().get(finalJ).getKc_file().lastIndexOf(".") + 1);
                                                    String filename1 = course_detail_model.getModulesModels().get(finalK).getTopic_under_modules().get(finalI).getFile_under_topics().get(finalJ).getKc_file().substring(course_detail_model.getModulesModels().get(finalK).getTopic_under_modules().get(finalI).getFile_under_topics().get(finalJ).getKc_file().lastIndexOf("/") + 1);
                                                    Methods.downloadFile(course_detail_model.getModulesModels().get(finalK).getTopic_under_modules().get(finalI).getFile_under_topics().get(finalJ).getKc_file(), Knowledge_Centre_Detail.this, extntion, course_detail_model.getModulesModels().get(finalK).getTopic_under_modules().get(finalI).getFile_under_topics().get(finalJ).getName());
                                                    Methods.showProgress(course_detail_model.getModulesModels().get(finalK).getTopic_under_modules().get(finalI).getFile_under_topics().get(finalJ).getKc_file(), Knowledge_Centre_Detail.this);
                                                    if (NetworkUtill.isNetworkAvailable(Knowledge_Centre_Detail.this)) {
                                                        knwledgeCompleteAsynctask = new KnwledgeCompleteAsynctask(course_detail_model.getModulesModels().get(finalK).getTopic_under_modules().get(finalI).getFile_under_topics().get(finalJ).getId(), finalK, finalI, finalJ);
                                                        knwledgeCompleteAsynctask.execute();
                                                    } else {
                                                        NetworkUtill.showNoInternetDialog(Knowledge_Centre_Detail.this);
                                                    }*/
                                                }
                                            });

                                        }
                                        if (doc_Type.equalsIgnoreCase("2")) {
                                            final int finalI = i;
                                            final int finalJ = j;
                                            final int finalK = k;
                                            v1.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if(isHide){
                                                    String s[] = course_detail_model.getModulesModels().get(finalK).getTopic_under_modules().get(finalI).getFile_under_topics().get(finalJ).getKc_file().split("documents/");
                                                    String s1[] = s[1].split("\\.");
                                                    if (course_detail_model.getModulesModels().get(finalK).getTopic_under_modules().get(finalI).getFile_under_topics().get(finalJ).getKc_file().contains("\\.")) {
                                                        //feedModel.setDoc_name(feedModel.getDoc_name().replace("\\.", ""));
                                                    }
                                                    Intent intent = new Intent(Knowledge_Centre_Detail.this, DocumentWebView.class);
                                                    intent.putExtra("id", course_detail_model.getModulesModels().get(finalK).getTopic_under_modules().get(finalI).getFile_under_topics().get(finalJ).getId());
                                                    intent.putExtra("title", course_detail_model.getModulesModels().get(finalK).getTopic_under_modules().get(finalI).getFile_under_topics().get(finalJ).getName());
                                                    intent.putExtra("course_detail_model", course_detail_model);
                                                    intent.putExtra("module_pos", finalK);
                                                    intent.putExtra("topic_pos", finalI);
                                                    intent.putExtra("file_pos", finalJ);
                                                    intent.putExtra("url", course_detail_model.getModulesModels().get(finalK).getTopic_under_modules().get(finalI).getFile_under_topics().get(finalJ).getKc_file());
                                                    startActivity(intent);}
                                                 /*   verifyStoragePermissions(Knowledge_Centre_Detail.this);

                                                    String extntion = course_detail_model.getModulesModels().get(finalK).getTopic_under_modules().get(finalI).getFile_under_topics().get(finalJ).getKc_file().substring(course_detail_model.getModulesModels().get(finalK).getTopic_under_modules().get(finalI).getFile_under_topics().get(finalJ).getKc_file().lastIndexOf(".") + 1);
                                                    String filename1 = course_detail_model.getModulesModels().get(finalK).getTopic_under_modules().get(finalI).getFile_under_topics().get(finalJ).getKc_file().substring(course_detail_model.getModulesModels().get(finalK).getTopic_under_modules().get(finalI).getFile_under_topics().get(finalJ).getKc_file().lastIndexOf("/") + 1);
                                                    Methods.downloadFile(course_detail_model.getModulesModels().get(finalK).getTopic_under_modules().get(finalI).getFile_under_topics().get(finalJ).getKc_file(), Knowledge_Centre_Detail.this, extntion, course_detail_model.getModulesModels().get(finalK).getTopic_under_modules().get(finalI).getFile_under_topics().get(finalJ).getName());
                                                    Methods.showProgress(course_detail_model.getModulesModels().get(finalK).getTopic_under_modules().get(finalI).getFile_under_topics().get(finalJ).getKc_file(), Knowledge_Centre_Detail.this);
                                                    if (NetworkUtill.isNetworkAvailable(Knowledge_Centre_Detail.this)) {
                                                        knwledgeCompleteAsynctask = new KnwledgeCompleteAsynctask(course_detail_model.getModulesModels().get(finalK).getTopic_under_modules().get(finalI).getFile_under_topics().get(finalJ).getId(), finalK, finalI, finalJ);
                                                        knwledgeCompleteAsynctask.execute();
                                                    } else {
                                                        NetworkUtill.showNoInternetDialog(Knowledge_Centre_Detail.this);
                                                    }*/
                                                }
                                            });
                                        }
                                        if (course_detail_model.getModulesModels().get(k).getTopic_under_modules().get(i).getFile_under_topics().get(j).getUser_completed()
                                                == 1) {
                                            checkBox.setVisibility(View.VISIBLE);
                                        } else {
                                            checkBox.setVisibility(View.GONE);

                                        }
                                        fileName.setText(filename);
                                        linearLayout.addView(v1);
                                    }
                                linearLayoutmain.addView(v);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    main_LinearLayout.addView(mainCards);

                }


        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (corseDetailAsynctask != null && !corseDetailAsynctask.isCancelled()) {
            corseDetailAsynctask.cancelAsyncTask();
        }
        if (acceptDeclineJoinAsyncTask != null && !acceptDeclineJoinAsyncTask.isCancelled()) {
            acceptDeclineJoinAsyncTask.cancelAsyncTask();
        }
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    public class KnwledgeCompleteAsynctask extends AsyncTask<Void, Void, Void> {
        String authToken;
        JSONObject jo;
        String groupId, userId;
        String id;
        int module_pos, topic_pos, file_pos;
        HttpClient client;
        int pos;
        private String responseBody;

        public KnwledgeCompleteAsynctask(String id, int module_pos, int topic_pos, int file_pos) {
            this.id = id;
            this.module_pos = module_pos;
            this.topic_pos = topic_pos;
            this.file_pos = file_pos;

        }

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
                if (jo != null) {


                    if (jo.has("response") && !jo.isNull("response")) {
                        JSONObject jsonObjectresponse = jo.getJSONObject("response");
                        if (jsonObjectresponse.has("message") && !jsonObjectresponse.isNull("message")) {
                            Toast.makeText(Knowledge_Centre_Detail.this, jsonObjectresponse.getString("message"), Toast.LENGTH_LONG).show();
                            // addQualificationTextView.setVisibility(View.GONE);
                            course_detail_model.getModulesModels().get(module_pos).getTopic_under_modules().get(topic_pos).getFile_under_topics().get(file_pos).setUser_completed(1);
                            ApplicationSingleton.setCourse_detail_model(course_detail_model);
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


                String url = AppUrl.BaseUrl + "knowledge/complete";
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
                pairs.add(new BasicNameValuePair("courseID", id));
                pairs.add(new BasicNameValuePair("userID", sesstionManager.getUserDetails().get(SesstionManager.USER_ID) + ""));
                pairs.add(new BasicNameValuePair("status", "1"));
                httppost.setHeader("Authorization", "Basic " + authToken);

                httppost.setEntity(new UrlEncodedFormEntity(pairs));
                response = client.execute(httppost);

                responseBody = EntityUtils
                        .toString(response.getEntity());
                jo = new JSONObject(responseBody);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

}
