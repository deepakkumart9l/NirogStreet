package com.app.nirogstreet.activites;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.nirogstreet.R;
import com.app.nirogstreet.adapter.CommentsRecyclerAdapter;
import com.app.nirogstreet.circularprogressbar.CircularProgressBar;
import com.app.nirogstreet.model.CommentsModel;
import com.app.nirogstreet.parser.CommentsParser;
import com.app.nirogstreet.uttil.AppUrl;
import com.app.nirogstreet.uttil.ApplicationSingleton;
import com.app.nirogstreet.uttil.NetworkUtill;
import com.app.nirogstreet.uttil.SesstionManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
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
 * Created by Preeti on 27-10-2017.
 */
public class CommentsActivity extends AppCompatActivity{
    SesstionManager sessionManager;
    GetCommentsAsynctask getCommentsAsynctask;
    CircularProgressBar circularProgressBar;
   // PostCommentAsyncTask postCommentAsyncTask;
    EditText editText;
    TextView sendImageView;
    ArrayList<CommentsModel> commentsModels;
    RecyclerView commentsrecyclerview;
    ImageView backImageView;
    CommentsRecyclerAdapter commentsAdapter;
    LinearLayoutManager linearLayoutManager1;
    boolean commentOnComment = true;
    String userId, authToken, feedId;
    private boolean albumupdate = false;
    private  boolean isHideCommentSection=false;
    private PostCommentAsyncTask postCommentAsyncTask;

    @Override
    protected void onResume() {
        super.onResume();
        commentsAdapter = null;
        commentsModels = new ArrayList<>();
        if (NetworkUtill.isNetworkAvailable(CommentsActivity.this)) {
            getCommentsAsynctask = new GetCommentsAsynctask(feedId);
            getCommentsAsynctask.execute();
        } else {

            NetworkUtill.showNoInternetDialog(CommentsActivity.this);
        }

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comments_listing);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.statusbarcolor));
        }
        if(getIntent().hasExtra("hideCommentSection"))
        {
            isHideCommentSection=getIntent().getBooleanExtra("hideCommentSection",false);
        }
        if (getIntent().hasExtra("commentsOnComment")) {
            commentOnComment = getIntent().getBooleanExtra("commentsOnComment", false);
        }
        circularProgressBar = (CircularProgressBar) findViewById(R.id.scroll);
        commentsrecyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        commentsrecyclerview.setNestedScrollingEnabled(true);
        linearLayoutManager1 = new LinearLayoutManager(CommentsActivity.this);
        ((SimpleItemAnimator) commentsrecyclerview.getItemAnimator()).setSupportsChangeAnimations(false);

        linearLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        commentsrecyclerview.setLayoutManager(linearLayoutManager1);

        sendImageView = (TextView) findViewById(R.id.commentTV);

        sendImageView.setEnabled(true);
        sendImageView.setClickable(true);
        backImageView = (ImageView) findViewById(R.id.back);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        editText = (EditText) findViewById(R.id.etMessageBox);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        if(isHideCommentSection)
        {
            editText.setVisibility(View.GONE);
            sendImageView.setVisibility(View.GONE);
        }
        feedId = getIntent().getStringExtra("feedId");
        sessionManager = new SesstionManager(CommentsActivity.this);
        HashMap<String, String> userDetails = sessionManager.getUserDetails();
        authToken = userDetails.get(SesstionManager.AUTH_TOKEN);
        userId = userDetails.get(SesstionManager.USER_ID);
        sendImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkUtill.isNetworkAvailable(CommentsActivity.this)) {
                    if (editText.getText() != null&&editText.getText().length()>0) {
                        String text= editText.getText().toString();
                        editText.setText("");
                        postCommentAsyncTask = new PostCommentAsyncTask(feedId,text);

                        postCommentAsyncTask.execute();
                    } else {
                        Toast.makeText(CommentsActivity.this, "write somting", Toast.LENGTH_LONG).show();
                    }
                } else {
                    NetworkUtill.showNoInternetDialog(CommentsActivity.this);
                }
            }
        });

    }
    public class GetCommentsAsynctask extends AsyncTask<Void, Void, Void> {
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

        public GetCommentsAsynctask(String feedId) {
            this.feedId = feedId;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            circularProgressBar.setVisibility(View.GONE);
            if (jo != null) {
                commentsModels = new ArrayList<>();
                commentsModels = CommentsParser.commentsParser(jo);
                if (commentsAdapter == null) {
                    commentsAdapter = new CommentsRecyclerAdapter(CommentsActivity.this, commentsModels, feedId, commentOnComment);
                    commentsrecyclerview.setAdapter(commentsAdapter);
                    /*listView.setAdapter(commentsAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent resultIntent = new Intent(CommentsActivity.this, ProfileActivity.class);
                            CommentsModel likesModel = commentsModels.get(i);
                            resultIntent.putExtra("userId", likesModel.getUserId());
                            startActivity(resultIntent);
                        }
                    });*/
                }
            }

        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {


                String url = AppUrl.BaseUrl + "feed/feed-comments";
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
                pairs.add(new BasicNameValuePair("feedID", feedId));
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
        }
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
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            circularProgressBar.setVisibility(View.GONE);
            try {
                if (jo != null) {
                    if (jo.has("responce") && !jo.isNull("responce")) {
                        JSONObject response = jo.getJSONObject("responce");
                        int totalLikes = 0;
                        boolean isuserLiked = false;
                        InputMethodManager inputMethodManager =
                                (InputMethodManager) CommentsActivity.this.getSystemService(
                                        Activity.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(
                                CommentsActivity.this.getCurrentFocus().getWindowToken(), 0);
                        ArrayList<CommentsModel> subComment = new ArrayList<>();
                        Toast.makeText(CommentsActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                        String commentId = null, message = null, createdOn = null, userId = null, fname = null, lname = null, slug = null, userProfile_pic = null;
                        if (response.has("comment") && !response.isNull("comment")) {
                            JSONObject jsonObject = response.getJSONObject("comment");
                            if (jsonObject.has("id") && !jsonObject.isNull("id")) {
                                commentId = jsonObject.getString("id");

                            }
                            if (jsonObject.has("message") && !jsonObject.isNull("message")) {
                                message = jsonObject.getString("message");
                            }
                            if (jsonObject.has("totalLike") && !jsonObject.isNull("totalLike")) {
                                totalLikes = jsonObject.getInt("totalLike");
                            }
                            if (jsonObject.has("user_hasLiked") && !jsonObject.isNull("user_hasLiked")) {
                                int userLike = jsonObject.getInt("user_hasLiked");
                                if (userLike == 1) {
                                    isuserLiked = true;
                                } else {
                                    isuserLiked = false;

                                }
                            }
                            if (jsonObject.has("created") && !jsonObject.isNull("created")) {
                                createdOn = jsonObject.getString("created");
                            }
                            if (jsonObject.has("subcumment") && !jsonObject.isNull("subcumment")) {

                                JSONArray subComments = jsonObject.getJSONArray("subcumment");
                                for (int k = 0; k < subComments.length(); k++) {
                                    JSONObject sub_commentObject = subComments.getJSONObject(k);
                                    String userIdSubComment = "", fnameSubComment = "", lnameSubComment = "", userProfile_picSubComment = "", slugSubComment = "", subCommentmsg = "", subCommentCreatedOn = "";
                                    if (sub_commentObject.has("userdetail") && !sub_commentObject.isNull("userdetail")) {
                                        JSONObject userDetail = sub_commentObject.getJSONObject("userdetail");
                                        {
                                            if (userDetail.has("id") && !userDetail.isNull("id")) {
                                                userIdSubComment = userDetail.getString("id");
                                            }
                                            if (userDetail.has("name") && !userDetail.isNull("name")) {
                                                fnameSubComment = userDetail.getString("name");
                                            }
                                            if (userDetail.has("lname") && !userDetail.isNull("lname")) {
                                                lnameSubComment = userDetail.getString("lname");
                                            }
                                            if (userDetail.has("profile_pic") && !userDetail.isNull("profile_pic")) {
                                                userProfile_picSubComment = userDetail.getString("profile_pic");
                                            }
                                            if (userDetail.has("slug") && !userDetail.isNull("slug")) {
                                                slugSubComment = userDetail.getString("slug");
                                            }
                                        }
                                    }
                                    if (sub_commentObject.has("message") && !sub_commentObject.isNull("message")) {
                                        subCommentmsg = sub_commentObject.getString("message");
                                    }
                                    if (sub_commentObject.has("created") && !sub_commentObject.isNull("created")) {
                                        subCommentCreatedOn = sub_commentObject.getString("created");
                                    }
                                    subComment.add(new CommentsModel(fnameSubComment, lnameSubComment, userIdSubComment, userIdSubComment, "", userProfile_picSubComment, "", subCommentCreatedOn, subCommentmsg, 0, false, null));

                                }
                            }
                            if (jsonObject.has("userdetail") && !jsonObject.isNull("userdetail")) {
                                JSONObject userDetail = jsonObject.getJSONObject("userdetail");
                                {
                                    if (userDetail.has("id") && !userDetail.isNull("id")) {
                                        userId = userDetail.getString("id");
                                    }
                                    if (userDetail.has("name") && !userDetail.isNull("name")) {
                                        fname = userDetail.getString("name");
                                    }
                                    if (userDetail.has("lname") && !userDetail.isNull("lname")) {
                                        lname = userDetail.getString("lname");
                                    }
                                    if (userDetail.has("profile_pic") && !userDetail.isNull("profile_pic")) {
                                        userProfile_pic = userDetail.getString("profile_pic");
                                    }
                                    if (userDetail.has("slug") && !userDetail.isNull("slug")) {
                                        slug = userDetail.getString("slug");
                                    }

                                }
                            }
                            commentsModels.add(new CommentsModel(fname, lname, slug, userId, commentId, userProfile_pic, "", createdOn, message, totalLikes, isuserLiked, subComment));
                            commentsAdapter.notifyDataSetChanged();
                            ApplicationSingleton.setIsCommented(true);
                            ApplicationSingleton.setNo_of_count(commentsAdapter.getItemCount());
                            commentsrecyclerview.scrollToPosition(commentsAdapter.getItemCount() - 1);
                            editText.setText("");

                          /*  albumupdate = true;
                            SharedPreferences sharedPref2 = getApplicationContext().getSharedPreferences("imgvidupdate", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor2 = sharedPref2.edit();
                            editor2.putBoolean("imgvidupdate", albumupdate);
                            editor2.commit();*/
                            //  finish();
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
                pairs.add(new BasicNameValuePair("show_comment","1"));
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

        }
    }

}
