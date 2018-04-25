package com.app.nirogstreet.activites;

/**
 * Created by as on 12/18/2017.
 */

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.MimeTypeMap;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.nirogstreet.R;
import com.app.nirogstreet.circularprogressbar.CircularProgressBar;
import com.app.nirogstreet.model.Course_Detail_model;
import com.app.nirogstreet.parser.Course_Detail_Parser;
import com.app.nirogstreet.uttil.AppUrl;
import com.app.nirogstreet.uttil.ApplicationSingleton;
import com.app.nirogstreet.uttil.Event_For_Firebase;
import com.app.nirogstreet.uttil.Methods;
import com.app.nirogstreet.uttil.NetworkUtill;
import com.app.nirogstreet.uttil.SesstionManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
import de.hdodenhof.circleimageview.CircleImageView;

import static com.app.nirogstreet.activites.OpenDocument.getAvailableFile;

public class Knowledge_Centre_Detail extends Activity {
    TextView title_side_Tv;
    RelativeLayout relativeLayout;
    CorseDetailAsynctask corseDetailAsynctask;
    TextView addQualificationTextView;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    KnwledgeCompleteAsynctask knwledgeCompleteAsynctask;
    LinearLayout main_LinearLayout;
    int k_Module_pos, i_Tpoic_pos, j_File_pos;
    Course_Detail_model course_detail_model;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    AcceptDeclineJoinAsyncTask acceptDeclineJoinAsyncTask;
    SesstionManager sesstionManager;
    CircleImageView auth_imageCircleImageView;
    TextView dr_name_TV;
    private static final String PDF_MIME_TYPE = "application/pdf";
    String courseId;
    boolean isHide = false;

    ImageView backImageView;
    TextView viewTv, profile_complete_txt;
    CircularProgressBar circularProgressBar;
    SeekBar seekBar_luminosite;
    private boolean openMain=false;

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
        Event_For_Firebase.getEventCount(Knowledge_Centre_Detail.this,"Feed_Learning_Screen_AllCourses_Course_Screen_Visit");
        title_side_Tv = (TextView) findViewById(R.id.title_side);
        dr_name_TV = (TextView) findViewById(R.id.dr_name);
        backImageView = (ImageView) findViewById(R.id.back);
        profile_complete_txt = (TextView) findViewById(R.id.profile_complete_txt);
        relativeLayout = (RelativeLayout) findViewById(R.id.rel);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (openMain) {
                    Intent intent1 = new Intent(Knowledge_Centre_Detail.this, MainActivity.class);
                    startActivity(intent1);
                    finish();
                }

                finish();
            }
        });
        seekBar_luminosite = (SeekBar) findViewById(R.id.seekBar_luminosite);
        seekBar_luminosite.setClickable(false);
        seekBar_luminosite.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        viewTv = (TextView) findViewById(R.id.view);
        if (getIntent().hasExtra("courseID")) {
            courseId = getIntent().getStringExtra("courseID");
        }
        if (getIntent().hasExtra("openMain")) {
            openMain = getIntent().getBooleanExtra("openMain", false);
        }
        if (getIntent().hasExtra("isHide")) {
            isHide = getIntent().getBooleanExtra("isHide", false);
        }
        auth_imageCircleImageView = (CircleImageView) findViewById(R.id.auth_image);
        addQualificationTextView = (TextView) findViewById(R.id.addQualification);
        sesstionManager = new SesstionManager(Knowledge_Centre_Detail.this);
        main_LinearLayout = (LinearLayout) findViewById(R.id.mainCrad);
        circularProgressBar = (CircularProgressBar) findViewById(R.id.circularProgressBar);

        if (isHide) {

            addQualificationTextView.setVisibility(View.GONE);
        } else {
            seekBar_luminosite.setVisibility(View.GONE);
            profile_complete_txt.setVisibility(View.GONE);
            addQualificationTextView.setVisibility(View.VISIBLE);
        }
        if (NetworkUtill.isNetworkAvailable(Knowledge_Centre_Detail.this)) {
            corseDetailAsynctask = new CorseDetailAsynctask("", "");
            corseDetailAsynctask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        } else {
            NetworkUtill.showNoInternetDialog(Knowledge_Centre_Detail.this);
        }
        addQualificationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (NetworkUtill.isNetworkAvailable(Knowledge_Centre_Detail.this)) {
                    Event_For_Firebase.getEventCount(Knowledge_Centre_Detail.this,"Feed_Learning_Screen_AllCourses_Course_Screen_EnrollButton_Click");
                    acceptDeclineJoinAsyncTask = new AcceptDeclineJoinAsyncTask();
                    acceptDeclineJoinAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    NetworkUtill.showNoInternetDialog(Knowledge_Centre_Detail.this);
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (openMain) {
            Intent intent1 = new Intent(Knowledge_Centre_Detail.this, MainActivity.class);
            startActivity(intent1);
            finish();
        }
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
                            isHide = true;
                            Toast.makeText(Knowledge_Centre_Detail.this, jsonObjectresponse.getString("message"), Toast.LENGTH_LONG).show();
                            addQualificationTextView.setVisibility(View.GONE);
                            seekBar_luminosite.setVisibility(View.VISIBLE);
                            profile_complete_txt.setVisibility(View.VISIBLE);
                            seekBar_luminosite.setProgress(0);
                            profile_complete_txt.setText("0 %");

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

                String url = AppUrl.BaseUrl + "knowledge/detail-new";
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
                    if (jo.has("response") && !jo.isNull("response")) {
                        JSONObject jsonObject = jo.getJSONObject("response");
                        if (jsonObject.has("completed") && !jsonObject.isNull("completed")) {
                            seekBar_luminosite.setProgress(jsonObject.getInt("completed"));
                            profile_complete_txt.setText(jsonObject.getInt("completed") + " %");
                        }
                    }
                    if (course_detail_model != null) {
                        ApplicationSingleton.setCourse_detail_model(course_detail_model);
                        title_side_Tv.setText(course_detail_model.getName());

                        if (course_detail_model.getAuthor_detail_module().getProfile_pic() != null)

                            Glide.with(Knowledge_Centre_Detail.this)
                                    .load(course_detail_model.getAuthor_detail_module().getProfile_pic()).placeholder(R.drawable.user)
                                    .centerCrop()
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .crossFade()
                                    .override(100, 100)
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
        if (ApplicationSingleton.isDocOpen()) {
            try {
                if (course_detail_model.getModulesModels().get(k_Module_pos).getTopic_under_modules().get(i_Tpoic_pos).getFile_under_topics().get(j_File_pos).getUser_completed() != 1)
                    if (NetworkUtill.isNetworkAvailable(Knowledge_Centre_Detail.this)) {
                        knwledgeCompleteAsynctask = new KnwledgeCompleteAsynctask(course_detail_model.getModulesModels().get(k_Module_pos).getTopic_under_modules().get(i_Tpoic_pos).getFile_under_topics().get(j_File_pos).getId(), k_Module_pos, i_Tpoic_pos, j_File_pos, course_detail_model.getModulesModels().get(k_Module_pos).getTopic_under_modules().get(i_Tpoic_pos).getFile_under_topics().get(j_File_pos).getRoot_id());
                        knwledgeCompleteAsynctask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        NetworkUtill.showNoInternetDialog(Knowledge_Centre_Detail.this);
                    }
                ApplicationSingleton.setIsDocOpen(false);
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
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

                    if (descriptionTextView != null && descriptionTextView.getText().length() > 170)
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
                                if (course_detail_model.getModulesModels().get(k).getTopic_under_modules().get(i).getFile_under_topics() != null && course_detail_model.getModulesModels().get(k).getTopic_under_modules().get(i).getFile_under_topics().size() > 0)
                                    for (int j = 0; j < course_detail_model.getModulesModels().get(k).getTopic_under_modules().get(i).getFile_under_topics().size(); j++) {
                                        View v1 = LayoutInflater.from(Knowledge_Centre_Detail.this).inflate(R.layout.module_item_check, null);
                                        TextView fileName = (TextView) v1.findViewById(R.id.txt);
                                        ImageView checkBox = (ImageView) v1.findViewById(R.id.checkbox);
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
                                                    if (isHide) {
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
                                        if (doc_Type.equalsIgnoreCase("1")) {
                                            final int finalI = i;
                                            final int finalJ = j;
                                            final int finalK = k;
                                            v1.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if (isHide) {

                                                        Intent intent = new Intent(Knowledge_Centre_Detail.this, LoadHtmlPageActivity.class);
                                                        intent.putExtra("id", course_detail_model.getModulesModels().get(finalK).getTopic_under_modules().get(finalI).getFile_under_topics().get(finalJ).getId());
                                                        intent.putExtra("title", course_detail_model.getModulesModels().get(finalK).getTopic_under_modules().get(finalI).getFile_under_topics().get(finalJ).getName());
                                                        intent.putExtra("course_detail_model", course_detail_model);
                                                        intent.putExtra("module_pos", finalK);
                                                        intent.putExtra("topic_pos", finalI);
                                                        intent.putExtra("file_pos", finalJ);
                                                        intent.putExtra("url", course_detail_model.getModulesModels().get(finalK).getTopic_under_modules().get(finalI).getFile_under_topics().get(finalJ).getKc_file());
                                                        startActivityForResult(intent, 1);


                                                    }


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
                                                    if (isHide) {
                                                        String s[] = course_detail_model.getModulesModels().get(finalK).getTopic_under_modules().get(finalI).getFile_under_topics().get(finalJ).getKc_file().split("documents/");
                                                        String s1[] = s[1].split("\\.");
                                                        if (course_detail_model.getModulesModels().get(finalK).getTopic_under_modules().get(finalI).getFile_under_topics().get(finalJ).getKc_file().contains("\\.")) {
                                                            //feedModel.setDoc_name(feedModel.getDoc_name().replace("\\.", ""));
                                                        }
                                                        k_Module_pos = finalK;
                                                        i_Tpoic_pos = finalI;
                                                        j_File_pos = finalJ;
                                                        ApplicationSingleton.setIsDocOpen(true);

                                                        String ext = course_detail_model.getModulesModels().get(finalK).getTopic_under_modules().get(finalI).getFile_under_topics().get(finalJ).getKc_file().substring(course_detail_model.getModulesModels().get(finalK).getTopic_under_modules().get(finalI).getFile_under_topics().get(finalJ).getKc_file().lastIndexOf(".") + 1, course_detail_model.getModulesModels().get(finalK).getTopic_under_modules().get(finalI).getFile_under_topics().get(finalJ).getKc_file().length());
                                                       Intent intent = new Intent(Knowledge_Centre_Detail.this, DocumentWebView.class);
                                                        intent.putExtra("id", course_detail_model.getModulesModels().get(finalK).getTopic_under_modules().get(finalI).getFile_under_topics().get(finalJ).getId());
                                                        intent.putExtra("title", course_detail_model.getModulesModels().get(finalK).getTopic_under_modules().get(finalI).getFile_under_topics().get(finalJ).getName());
                                                        intent.putExtra("course_detail_model", course_detail_model);
                                                        intent.putExtra("module_pos", finalK);
                                                        intent.putExtra("topic_pos", finalI);
                                                        intent.putExtra("file_pos", finalJ);
                                                        intent.putExtra("url", course_detail_model.getModulesModels().get(finalK).getTopic_under_modules().get(finalI).getFile_under_topics().get(finalJ).getKc_file());
                                                        startActivity(intent);

                                                        String url;
                                                        if (course_detail_model.getModulesModels().get(finalK).getTopic_under_modules().get(finalI).getFile_under_topics().get(finalJ).getKc_file().contains(".DOC"))

                                                        {
                                                            url = course_detail_model.getModulesModels().get(finalK).getTopic_under_modules().get(finalI).getFile_under_topics().get(finalJ).getKc_file().replace(".DOC", ".doc");
                                                        } else {
                                                            url = course_detail_model.getModulesModels().get(finalK).getTopic_under_modules().get(finalI).getFile_under_topics().get(finalJ).getKc_file();
                                                        }
                                                       // showPDFUrl(Knowledge_Centre_Detail.this, url, ext);

                                                    }


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
        if (knwledgeCompleteAsynctask != null && !knwledgeCompleteAsynctask.isCancelled()) {
            knwledgeCompleteAsynctask.cancelAsyncTask();
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
        String rootID;
        private String responseBody;

        public KnwledgeCompleteAsynctask(String id, int module_pos, int topic_pos, int file_pos, String rootID) {
            this.id = id;
            this.module_pos = module_pos;
            this.topic_pos = topic_pos;
            this.file_pos = file_pos;
            this.rootID = rootID;

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
                        JSONObject jsonObject = jo.getJSONObject("response");
                        if (jsonObject.has("completed") && !jsonObject.isNull("completed")) {
                            seekBar_luminosite.setProgress(jsonObject.getInt("completed"));
                            profile_complete_txt.setText(jsonObject.getInt("completed") + " %");
                        }
                        if (jsonObjectresponse.has("message") && !jsonObjectresponse.isNull("message")) {
                            Toast.makeText(Knowledge_Centre_Detail.this, jsonObjectresponse.getString("message"), Toast.LENGTH_LONG).show();
                            // addQualificationTextView.setVisibility(View.GONE);
                            course_detail_model.getModulesModels().get(module_pos).getTopic_under_modules().get(topic_pos).getFile_under_topics().get(file_pos).setUser_completed(1);
                            update();
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
                pairs.add(new BasicNameValuePair("rootID", rootID));
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                int module = data.getIntExtra("module", -1);
                int topic = data.getIntExtra("topic", -1);
                int file = data.getIntExtra("file", -1);
                if (course_detail_model.getModulesModels().get(module).getTopic_under_modules().get(topic).getFile_under_topics().get(file).getUser_completed() != 1)
                    if (NetworkUtill.isNetworkAvailable(Knowledge_Centre_Detail.this)) {
                        knwledgeCompleteAsynctask = new KnwledgeCompleteAsynctask(course_detail_model.getModulesModels().get(module).getTopic_under_modules().get(topic).getFile_under_topics().get(file).getId(), module, topic, file, course_detail_model.getModulesModels().get(module).getTopic_under_modules().get(topic).getFile_under_topics().get(file).getRoot_id());
                        knwledgeCompleteAsynctask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        NetworkUtill.showNoInternetDialog(Knowledge_Centre_Detail.this);
                    }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
            }
        }
    }

    public void showPDFUrl(final Context context, final String pdfUrl, String ext) {
try {
    if (isPDFSupported(context, ext)) {
        downloadAndOpenPDF(context, pdfUrl);
    } else {
    }
}catch (Exception e)
{
    e.printStackTrace();
}
        //askToOpenPDFThroughGoogleDrive( context, pdfUrl );
    }

    public static boolean isPDFSupported(Context context, String ext) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        final File tempFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "test." + ext);
        String url = Uri.fromFile(tempFile).toString();
        i.setDataAndType(Uri.fromFile(tempFile), getMimeType(url));
        return context.getPackageManager().queryIntentActivities(i, PackageManager.MATCH_DEFAULT_ONLY).size() > 0;
    }

    public void downloadAndOpenPDF(final Context context, final String pdfUrl) {
        final String filename = pdfUrl.substring(pdfUrl.lastIndexOf("/") + 1);
        // The place where the downloaded PDF file will be put
        final File tempFile = getAvailableFile(pdfUrl, context);
        if (tempFile.exists()) {
            // If we have downloaded the file before, just go ahead and show it.
            openPDF(context, Uri.fromFile(tempFile));
            return;
        }
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Downloading, Please Wait!");
        //progress.setMax(100);
        // progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        // progress.setIndeterminate(true);
        // progress.setProgress((int) tempFile.length());
        progress.show();

        // Create the download request
        DownloadManager.Request r = new DownloadManager.Request(Uri.parse(pdfUrl));
        r.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, filename);
        final DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        BroadcastReceiver onComplete = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try{
                if (!progress.isShowing()) {
                    return;
                }
                context.unregisterReceiver(this);

                progress.dismiss();
                long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                Cursor c = dm.query(new DownloadManager.Query().setFilterById(downloadId));
                if (c.moveToFirst()) {
                    int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        openPDF(context, Uri.fromFile(tempFile));
                    }
                }
                c.close();
            }catch (Exception e)
                {e.printStackTrace();
                }
            }
        };
        context.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        // Enqueue the request
        dm.enqueue(r);
    }

    public static final void openPDF(Context context, Uri localUri) {
        try {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setDataAndType(localUri, getMimeType(localUri.toString()));
            context.startActivity(i);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private static String getMimeType(String url) {
        String parts[] = url.split("\\.");
        String extension = parts[parts.length - 1];
        String type = null;
        if (extension != null) {
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            type = mime.getMimeTypeFromExtension(extension);
        }
        return type;
    }
}
