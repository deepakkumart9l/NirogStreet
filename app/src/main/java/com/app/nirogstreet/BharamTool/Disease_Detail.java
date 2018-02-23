package com.app.nirogstreet.BharamTool;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.nirogstreet.R;
import com.app.nirogstreet.activites.CommentsActivity;
import com.app.nirogstreet.activites.PostDetailActivity;
import com.app.nirogstreet.adapter.CommentsRecyclerAdapter;
import com.app.nirogstreet.circularprogressbar.CircularProgressBar;
import com.app.nirogstreet.model.CommentsModel;
import com.app.nirogstreet.uttil.AppUrl;
import com.app.nirogstreet.uttil.ApplicationSingleton;
import com.app.nirogstreet.uttil.NetworkUtill;
import com.app.nirogstreet.uttil.Query_Method;
import com.app.nirogstreet.uttil.SesstionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
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
 * Created by as on 2/22/2018.
 */

public class Disease_Detail extends AppCompatActivity {
    CircularProgressBar mCircularProgressBar;
    private SesstionManager session;
    String res;
    String authToken, userId;
    private List<Bharam_Model> listing_models;
    ImageView mDiseasimg;
    int mDiseassub_cat_id;
    TextView mTitle_txt, mDescrptn_txt, mShoola_of_txt;
    LinearLayout mDiseassubcat_layout, mParent_layout;
    String lSubcat_id;
    EditText msgEditText;
    ImageView backImageView;
    LinearLayout comment;
    PostCommentAsyncTask postCommentAsyncTask;
    TextView comment_count_txt;
    String lId, totalcomment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.disaes_detail);
        backImageView = (ImageView) findViewById(R.id.back);
        msgEditText = (EditText) findViewById(R.id.etMessageBox);
        totalcomment = getIntent().getStringExtra("totalcomment");
        comment_count_txt = (TextView) findViewById(R.id.comment_count_txt);
        comment = (LinearLayout) findViewById(R.id.comment);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mDiseasimg = (ImageView) findViewById(R.id.imag);
        mDiseassub_cat_id = getIntent().getIntExtra("sub_cat_id", 0);
        comment_count_txt.setText(totalcomment + " Comments");
        comment_count_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Disease_Detail.this, CommentsActivity.class);
                intent.putExtra("feedId", mDiseassub_cat_id + "");
                intent.putExtra("type", "2");


                startActivity(intent);
            }
        });
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (msgEditText.getText().toString().length() != 0) {
                    if (NetworkUtill.isNetworkAvailable(Disease_Detail.this)) {
                        String str = msgEditText.getText().toString();
                        msgEditText.setText("");
                        postCommentAsyncTask = new PostCommentAsyncTask(mDiseassub_cat_id + "", str);
                        postCommentAsyncTask.execute();
                    } else {
                        NetworkUtill.showNoInternetDialog(Disease_Detail.this);
                    }
                }
            }
        });
        mTitle_txt = (TextView) findViewById(R.id.title_txt);
        mDescrptn_txt = (TextView) findViewById(R.id.descrptn_txt);
        mShoola_of_txt = (TextView) findViewById(R.id.shoola_of_txt);
        mParent_layout = (LinearLayout) findViewById(R.id.parent_layout);
        mDiseassubcat_layout = (LinearLayout) findViewById(R.id.diseassubcat_layout);
        mCircularProgressBar = (CircularProgressBar) findViewById(R.id.circularProgressBar);
        session = new SesstionManager(Disease_Detail.this);
        if (session.isUserLoggedIn()) {
            HashMap<String, String> userDetail = session.getUserDetails();
            authToken = userDetail.get(SesstionManager.AUTH_TOKEN);
            userId = userDetail.get(SesstionManager.USER_ID);
        }
        if (NetworkUtill.isNetworkAvailable(Disease_Detail.this)) {
            new Asyank_Listing().execute();
        } else {
            NetworkUtill.showNoInternetDialog(Disease_Detail.this);
        }
    }

    class Asyank_Listing extends AsyncTask<Void, Void, Void> {
        int result = 0;
        int code = 0;
        JSONObject jo;
        HttpURLConnection httpURLConnection;
        private String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mCircularProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                String url = "";
                url = AppUrl.BaseUrl + "knowledge/disease-detail";
                ContentValues values = new ContentValues();
                values.put("appID", "NRGSRT$(T(L5830FRU@!^AUSER");
                values.put("sub_cat_id", mDiseassub_cat_id);
                URL uri = new URL(url);
                httpURLConnection = (HttpURLConnection) uri.openConnection();
                httpURLConnection.addRequestProperty("Authorization", "Basic " + authToken);
                httpURLConnection.setReadTimeout(10000);
                httpURLConnection.setConnectTimeout(15000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(Query_Method.getQuery(values));
                writer.flush();
                writer.close();
                os.close();
                httpURLConnection.connect();
                BufferedReader br = new BufferedReader(new InputStreamReader((httpURLConnection.getInputStream())));
                StringBuilder sb = new StringBuilder();
                String output;
                while ((output = br.readLine()) != null) {
                    sb.append(output);
                }
                res = sb.toString();
                System.out.println("My Response :: " + res.toString());
                jo = new JSONObject(res);
                result = jo.getJSONObject("response").getInt("error");
                // code = jo.getInt("code");

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void r) {
            super.onPostExecute(r);
           /* if (code == 200) {*/
            mCircularProgressBar.setVisibility(View.GONE);
            if (result == 0) {
                mCircularProgressBar.setVisibility(View.GONE);
                try {
                    if (jo != null && jo.length() > 0) {
                        mParent_layout.setVisibility(View.VISIBLE);
                        String totalcomment=jo.getJSONObject("response").getString("total_comment");
                        comment_count_txt.setText(totalcomment);
                        JSONObject joj = jo.getJSONObject("response").getJSONObject("name");
                        JSONArray jsonArray = jo.getJSONObject("response").getJSONArray("disease_sub_list");
                        String image = joj.getString("img");
                        String des = joj.getString("description");
                        final String name = joj.getString("name");
                        lSubcat_id = joj.getString("sub_cat_id");
                        mTitle_txt.setText(name);
                        mDescrptn_txt.setText(Html.fromHtml(des));
                        if (image != null && image.length() > 0) {
                            mDiseasimg.setVisibility(View.VISIBLE);
                            Picasso.with(Disease_Detail.this)
                                    .load(image)
                                    .placeholder(R.drawable.user)
                                    .error(R.drawable.user)
                                    .into(mDiseasimg);
                        } else {
                            mDiseasimg.setVisibility(View.GONE);
                        }
                        if (Html.fromHtml(des) != null && Html.fromHtml(des).length() > 170)
                            makeTextViewResizable(mDescrptn_txt, 4, "view more", true);
                        else {
                            mTitle_txt.setText(Html.fromHtml(des));
                        }

                        if (jsonArray != null && jsonArray.length() > 0) {
                            mDiseassubcat_layout.setVisibility(View.VISIBLE);
                            mShoola_of_txt.setText("Shoola of " + name);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT);
                                LayoutInflater layoutInflater = (LayoutInflater) Disease_Detail.this.getSystemService(
                                        Context.LAYOUT_INFLATER_SERVICE);

                                View view = layoutInflater.inflate(
                                        R.layout.diseas_sub_list_view, null);
                                TextView title = (TextView) view.findViewById(R.id.diseas_sub_cat_txt);
                                LinearLayout diseas_sub_layout = (LinearLayout) view.findViewById(R.id.diseas_sub_layout);
                                final String itanaryname = jsonObject.getString("name");
                                lId = jsonObject.getString("id");
                                title.setText(Html.fromHtml(itanaryname));
                                view.setLayoutParams(params);

                                diseas_sub_layout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(Disease_Detail.this, Diseas_sub_cat_sub.class);
                                        intent.putExtra("id", lId);
                                        intent.putExtra("subcat_id", lSubcat_id);
                                        intent.putExtra("name", itanaryname);
                                        startActivity(intent);
                                    }
                                });
                                mDiseassubcat_layout.addView(view);
                            }
                        } else {
                            mShoola_of_txt.setVisibility(View.GONE);
                            mDiseassubcat_layout.setVisibility(View.GONE);
                        }
                    } else {
                        mParent_layout.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public void cancelAsyncTask() {
            if (httpURLConnection != null)
                httpURLConnection.disconnect();
        }
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

    public class PostCommentAsyncTask extends AsyncTask<Void, Void, Void> {
        String msg;
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

        public PostCommentAsyncTask(String feedId, String msg) {
            try {
                this.msg = URLEncoder.encode(msg, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            this.feedId = feedId;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mCircularProgressBar.setVisibility(View.VISIBLE);
        }


        @Override
        protected Void doInBackground(Void... voids) {
            try {


                String url = AppUrl.BaseUrl + "feed/comment";
                SSLSocketFactory sf = new SSLSocketFactory(
                        SSLContext.getDefault(),
                        SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                Scheme sch = new Scheme("https", 443, sf);
                client = new DefaultHttpClient();

                client.getConnectionManager().getSchemeRegistry().register(sch);
                HttpPost httppost = new HttpPost(url);
                HttpResponse response;

                // String credentials = email + ":" + password;

                List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                pairs.add(new BasicNameValuePair(AppUrl.APP_ID_PARAM, AppUrl.APP_ID_VALUE_POST));
                pairs.add(new BasicNameValuePair("userID", userId));
                pairs.add(new BasicNameValuePair("feedID", feedId));
                pairs.add(new BasicNameValuePair("message", msg));
                pairs.add(new BasicNameValuePair("post_type", "2"));
                pairs.add(new BasicNameValuePair("show_comment", "1"));
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
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mCircularProgressBar.setVisibility(View.GONE);
            try {
                if (jo != null) {
                    if (jo.has("responce") && !jo.isNull("responce")) {
                        JSONObject response = jo.getJSONObject("responce");
                        int totalLikes = 0;
                        boolean isuserLiked = false;
                        ArrayList<CommentsModel> subComment = new ArrayList<>();
                        String user_type_comment = null, title_comment = null;
                        String commentId = null, message = null, createdOn = null, userId = null, fname = null, lname = null, slug = null, userProfile_pic = null;
                        InputMethodManager inputMethodManager =
                                (InputMethodManager) Disease_Detail.this.getSystemService(
                                        Activity.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(
                                Disease_Detail.this.getCurrentFocus().getWindowToken(), 0);
                        Toast.makeText(Disease_Detail.this, response.getString("message"), Toast.LENGTH_LONG).show();

                        if (NetworkUtill.isNetworkAvailable(Disease_Detail.this)) {
                            new Asyank_Listing().execute();
                        } else {
                            NetworkUtill.showNoInternetDialog(Disease_Detail.this);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
