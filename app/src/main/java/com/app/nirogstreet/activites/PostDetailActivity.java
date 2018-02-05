package com.app.nirogstreet.activites;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.nirogstreet.R;
import com.app.nirogstreet.adapter.CommentsRecyclerAdapter;
import com.app.nirogstreet.adapter.PostDetailAdapter;
import com.app.nirogstreet.circularprogressbar.CircularProgressBar;
import com.app.nirogstreet.model.CommentsModel;
import com.app.nirogstreet.model.FeedModel;
import com.app.nirogstreet.parser.CommentsParser;
import com.app.nirogstreet.parser.FeedParser;
import com.app.nirogstreet.uttil.AppUrl;
import com.app.nirogstreet.uttil.NetworkUtill;
import com.app.nirogstreet.uttil.SesstionManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
 * Created by Preeti on 28-11-2017.
 */

public class PostDetailActivity extends Activity {
    String feedId;
    String id;
    SesstionManager sessionManager;
    PostDetailAsyncTask postDetailAsyncTask;
    private FrameLayout customViewContainer;

    GetCommentsAsynctask getCommentsAsynctask;
    String userId, authToken;
    CircularProgressBar circularProgressBar;
    TextView noContentTextView;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager, linearLayoutManager1;
    CommentsRecyclerAdapter commentsAdapter = null;

    ArrayList<CommentsModel> commentsModels = new ArrayList<>();
    boolean openMain=false;

    private PostDetailAdapter feedsAdapter;
    RecyclerView commentsrecyclerview;
    private ImageView backImageView;
    TextView sendImageView;
    private EditText editText;
    private PostCommentAsyncTask postCommentAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_detail);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.statusbarcolor));
        }

        //  collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        sendImageView = (TextView) findViewById(R.id.commentTV);
        sendImageView.setEnabled(false);
        sendImageView.setClickable(false);
        backImageView = (ImageView) findViewById(R.id.back);
        if(getIntent().hasExtra("openMain"))
        {
            openMain=getIntent().getBooleanExtra("openMain",false);
        }
        noContentTextView = (TextView) findViewById(R.id.noContent);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(openMain)
                {
                    Intent intent1 = new Intent(PostDetailActivity.this, MainActivity.class);
                    startActivity(intent1);
                    finish();
                }
                finish();
            }
        });
        editText = (EditText) findViewById(R.id.etMessageBox);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() == 0) {
                    sendImageView.setEnabled(false);

                    sendImageView.setClickable(false);
                } else {
                    sendImageView.setClickable(true);
                    sendImageView.setEnabled(true);

                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        sendImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkUtill.isNetworkAvailable(PostDetailActivity.this)) {
                    if (editText.getText() != null && editText.getText().length() > 0) {
                        String str=editText.getText().toString();
                        editText.setText("");
                        postCommentAsyncTask = new PostCommentAsyncTask(feedId,str);
                        postCommentAsyncTask.execute();
                    } else {
                        Toast.makeText(PostDetailActivity.this, "write something", Toast.LENGTH_LONG).show();
                    }
                } else {
                    NetworkUtill.showNoInternetDialog(PostDetailActivity.this);
                }
            }
        });
        sessionManager = new SesstionManager(PostDetailActivity.this);
        userId = sessionManager.getUserDetails().get(SesstionManager.USER_ID);
        authToken = sessionManager.getUserDetails().get(SesstionManager.AUTH_TOKEN);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        commentsrecyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setNestedScrollingEnabled(true);
        customViewContainer = (FrameLayout) findViewById(R.id.customViewContainer);
        circularProgressBar = (CircularProgressBar) findViewById(R.id.circularProgressBar);
        linearLayoutManager = new LinearLayoutManager(PostDetailActivity.this);
        linearLayoutManager1 = new LinearLayoutManager(PostDetailActivity.this);
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        commentsrecyclerview.setLayoutManager(linearLayoutManager1);
        recyclerView.setLayoutManager(linearLayoutManager);

        Intent intent = getIntent();
        if (intent.hasExtra("feedId")) {
            feedId = intent.getStringExtra("feedId");
        }
        if (NetworkUtill.isNetworkAvailable(PostDetailActivity.this)) {
            postDetailAsyncTask = new PostDetailAsyncTask(feedId, userId, authToken);
            postDetailAsyncTask.execute();
        } else {
            NetworkUtill.showNoInternetDialog(PostDetailActivity.this);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(openMain)
        {
            Intent intent1 = new Intent(PostDetailActivity.this, MainActivity.class);
            startActivity(intent1);
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (postDetailAsyncTask != null && !postDetailAsyncTask.isCancelled()) {
            postDetailAsyncTask.cancelAsyncTask();
        }
        if (getCommentsAsynctask != null && !getCommentsAsynctask.isCancelled()) {
            getCommentsAsynctask.cancelAsyncTask();
        }
        if (postCommentAsyncTask != null && !postCommentAsyncTask.isCancelled()) {
            postCommentAsyncTask.cancelAsyncTask();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (NetworkUtill.isNetworkAvailable(PostDetailActivity.this)) {
            postDetailAsyncTask = new PostDetailAsyncTask(feedId, userId, authToken);
            postDetailAsyncTask.execute();
        } else {
            NetworkUtill.showNoInternetDialog(PostDetailActivity.this);
        }
    }

    public class PostDetailAsyncTask extends AsyncTask<Void, Void, Void> {
        String authToken;
        JSONObject jo;
        String feedId, userId;


        private String responseBody;
        HttpClient client;
        Context context;

        public void cancelAsyncTask() {
            if (client != null && !isCancelled()) {
                cancel(true);
                client = null;
            }
        }

        public PostDetailAsyncTask(String feedId, String userId, String authToken) {
            this.feedId = feedId;
            this.authToken = authToken;
            this.userId = userId;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            circularProgressBar.setVisibility(View.VISIBLE);

            super.onPostExecute(aVoid);
            try {
                if (jo != null) {
                    int ispostDeletd = 0;
                    if (jo.has("detail") && !jo.isNull("detail")) {
                        JSONArray jsonArray = jo.getJSONArray("detail");
                        if (jsonArray != null) {
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            if (jsonObject != null) {
                                if (jsonObject.has("postdeleted") && !jsonObject.isNull("postdeleted")) {
                                    ispostDeletd = jsonObject.getInt("postdeleted");

                                }
                            }
                            if (ispostDeletd == 0) {
                                if (jsonObject.has("user_id") && !jsonObject.isNull("user_id")) {
                                    JSONObject jsonObject1 = jsonObject.getJSONObject("user_id");
                                    if (jsonObject1.has("id") && !jsonObject1.isNull("id")) {
                                        id = jsonObject1.getString("id");

                                    }
                                }

                                recyclerView.setVisibility(View.VISIBLE);
                                ArrayList<FeedModel> feedModels = new ArrayList<>();
                                feedModels.addAll(FeedParser.singlePostFeed(jsonArray.getJSONObject(0)));
                                feedsAdapter = new PostDetailAdapter(PostDetailActivity.this, feedModels, PostDetailActivity.this, "", customViewContainer, circularProgressBar);
                                recyclerView.setAdapter(feedsAdapter);
                                if (NetworkUtill.isNetworkAvailable(PostDetailActivity.this)) {
                                    getCommentsAsynctask = new GetCommentsAsynctask(feedId);
                                    getCommentsAsynctask.execute();

                                } else {
                                    NetworkUtill.showNoInternetDialog(PostDetailActivity.this);
                                }
                            } else {
                                recyclerView.setVisibility(View.GONE);
                                noContentTextView.setVisibility(View.VISIBLE);
                                circularProgressBar.setVisibility(View.GONE);
                                commentsrecyclerview.setVisibility(View.GONE);
                                editText.setVisibility(View.GONE);
                                sendImageView.setVisibility(View.GONE);
                                noContentTextView.setText("Sorry, this content isn't available right now\n" +
                                        "The post you followed may have been deleted.");
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.print(e);
            }

        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                String url = AppUrl.BaseUrl + "feed/single-post";
                SSLSocketFactory sf = new SSLSocketFactory(SSLContext.getDefault(), SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                Scheme sch = new Scheme("https", 443, sf);
                client = new DefaultHttpClient();
                client.getConnectionManager().getSchemeRegistry().register(sch);
                HttpPost httppost = new HttpPost(url);
                HttpResponse response;
                List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                pairs.add(new BasicNameValuePair(AppUrl.APP_ID_PARAM, AppUrl.APP_ID_VALUE_POST));
                pairs.add(new BasicNameValuePair("userID", userId));
                pairs.add(new BasicNameValuePair("postID", feedId));
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
                commentsModels = CommentsParser.commentsParser(jo);

                if (commentsAdapter == null && commentsModels.size() > 0) {

                    commentsAdapter = new CommentsRecyclerAdapter(PostDetailActivity.this, commentsModels, feedId, true);
                    commentsrecyclerview.setAdapter(commentsAdapter);
                    /*final AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams)
                            collapsingToolbarLayout.getLayoutParams();
                    params.setScrollFlags(
                            AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                                    | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED
                    );
                    collapsingToolbarLayout.setLayoutParams(params);*/
                } else {
                    if (commentsModels.size() > 0)
                        commentsAdapter = new CommentsRecyclerAdapter(PostDetailActivity.this, commentsModels, feedId, true);
                    commentsrecyclerview.setAdapter(commentsAdapter);
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

                // String credentials = email + ":" + password;

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
        protected void onPreExecute() {
            super.onPreExecute();

            circularProgressBar.setVisibility(View.VISIBLE);
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
            circularProgressBar.setVisibility(View.GONE);
            try {
                if (jo != null) {
                    if (jo.has("responce") && !jo.isNull("responce")) {
                        JSONObject response = jo.getJSONObject("responce");
                        int totalLikes = 0;
                        boolean isuserLiked = false;
                        ArrayList<CommentsModel> subComment = new ArrayList<>();

                        String commentId = null, message = null, createdOn = null, userId = null, fname = null, lname = null, slug = null, userProfile_pic = null;
                        InputMethodManager inputMethodManager =
                                (InputMethodManager) PostDetailActivity.this.getSystemService(
                                        Activity.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(
                                PostDetailActivity.this.getCurrentFocus().getWindowToken(), 0);
                        Toast.makeText(PostDetailActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();

                        if (response.has("comment") && !response.isNull("comment")) {
                            JSONObject jsonObject = response.getJSONObject("comment");

                            if (jsonObject.has("id") && !jsonObject.isNull("id")) {
                                commentId = jsonObject.getString("id");

                            }
                            if (jsonObject.has("message") && !jsonObject.isNull("message")) {
                                message = jsonObject.getString("message");
                            }
                            if (jsonObject.has("created") && !jsonObject.isNull("created")) {
                                createdOn = jsonObject.getString("created");
                            }
                            if (jsonObject.has("totalLikes") && !jsonObject.isNull("totalLikes")) {
                                totalLikes = jsonObject.getInt("totalLikes");
                            }
                            if (jsonObject.has("user_hasLiked") && !jsonObject.isNull("user_hasLiked")) {
                                int userLike = jsonObject.getInt("user_hasLiked");
                                if (userLike == 1) {
                                    isuserLiked = true;
                                } else {
                                    isuserLiked = false;

                                }
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

                                commentsModels.add(new CommentsModel(fname, lname, slug, userId, commentId, userProfile_pic, "", createdOn, message, totalLikes, isuserLiked, subComment));
                                if (commentsAdapter == null && commentsModels.size() > 0) {
                                    commentsAdapter = new CommentsRecyclerAdapter(PostDetailActivity.this, commentsModels, feedId, true);
                                    commentsrecyclerview.setAdapter(commentsAdapter);
                              /*  final AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams)
                                        collapsingToolbarLayout.getLayoutParams();
                                params.setScrollFlags(
                                        AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                                                | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED

                                );*/
                                    feedsAdapter.notifyItemChanged(0, new Integer(1));
                                    /// collapsingToolbarLayout.setLayoutParams(params);
                                } else {
                                    commentsAdapter.notifyItemInserted(commentsModels.size() - 1);
                                    commentsAdapter.notifyItemRangeChanged(0, commentsModels.size());
                                    commentsAdapter.notifyDataSetChanged();
                                    commentsrecyclerview.smoothScrollToPosition(feedsAdapter.getItemCount());

                                    feedsAdapter.notifyItemChanged(0, new Integer(1));

                                }
                                editText.setText("");
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

