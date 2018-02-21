package com.app.nirogstreet.activites;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.nirogstreet.R;
import com.app.nirogstreet.adapter.User_Activity_Adapter;
import com.app.nirogstreet.circularprogressbar.CircularProgressBar;
import com.app.nirogstreet.model.FeedModel;
import com.app.nirogstreet.model.UserDetailModel;
import com.app.nirogstreet.parser.FeedParser;
import com.app.nirogstreet.parser.UserDetailPaser;
import com.app.nirogstreet.uttil.AppUrl;
import com.app.nirogstreet.uttil.ApplicationSingleton;
import com.app.nirogstreet.uttil.NetworkUtill;
import com.app.nirogstreet.uttil.SesstionManager;
import com.google.android.gms.cast.framework.SessionManager;

import org.json.JSONObject;

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
 * Created by Preeti on 20-02-2018.
 */

public class MyActivities extends Activity {

    private UserDetailModel userDetailModel;
    RecyclerView recyclerView;
    int totalPageCount;

    UserFeedsAsyncTask userFeedsAsyncTask;
    FrameLayout customViewContainer;
    private boolean isLoading = false;

    int page = 1;
    CircularProgressBar circularProgressBar;
    User_Activity_Adapter feedsAdapter;
    private SwipeRefreshLayout swipeLayout;

    private LinearLayoutManager linearLayoutManager;

    private ArrayList<FeedModel> totalFeeds;
    private SesstionManager session;
    String authToken, userId, profile_pic, name, title,userType;
    ImageView backImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_new);
        session = new SesstionManager(MyActivities.this);
        customViewContainer = (FrameLayout) findViewById(R.id.customViewContainer);
        backImageView = (ImageView) findViewById(R.id.back);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (session.isUserLoggedIn()) {
            HashMap<String, String> userDetail = session.getUserDetails();
            authToken = userDetail.get(SesstionManager.AUTH_TOKEN);
            // userId = userDetail.get(SesstionManager.USER_ID);
        }
        if (getIntent().hasExtra("userId")) {
            userId = getIntent().getStringExtra("userId");
        }
        if (getIntent().hasExtra("profile_pic")) {
            profile_pic = getIntent().getStringExtra("profile_pic");

        }
        if (getIntent().hasExtra("name")) {
            name = getIntent().getStringExtra("name");
        }
        if (getIntent().hasExtra("title")) {
            title = getIntent().getStringExtra("title");
        }
        if (getIntent().hasExtra("userType")) {
            userType = getIntent().getStringExtra("userType");
        }
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setNestedScrollingEnabled(true);
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        swipeLayout.setColorScheme(R.color.gplus_color_1,
                R.color.gplus_color_2,
                R.color.gplus_color_1,
                R.color.gplus_color_2);
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);


        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                totalFeeds = new ArrayList<>();
                feedsAdapter = null;
                page = 1;
                if (NetworkUtill.isNetworkAvailable(MyActivities.this))

                {
                    String url = AppUrl.BaseUrl + "my-activity-new";
                    userFeedsAsyncTask = new UserFeedsAsyncTask(MyActivities.this, circularProgressBar, url, authToken, userId);
                    userFeedsAsyncTask.execute();
                } else {
                    NetworkUtill.showNoInternetDialog(MyActivities.this);
                }
                if (totalFeeds.size() > 0) {
                    //  recyclerView.scrollToPosition(0);
                }
            }

        });
        linearLayoutManager = new LinearLayoutManager(MyActivities.this);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        circularProgressBar = (CircularProgressBar) findViewById(R.id.circularProgressBar);
        session = new SesstionManager(MyActivities.this);
        if (session.isUserLoggedIn()) {
            HashMap<String, String> userDetail = session.getUserDetails();
            authToken = userDetail.get(SesstionManager.AUTH_TOKEN);
           // userId = userDetail.get(SesstionManager.USER_ID);
        }

        totalFeeds = new ArrayList<>();


        if (NetworkUtill.isNetworkAvailable(MyActivities.this))

        {
            String url = AppUrl.BaseUrl + "feed/my-activity-new";
            userFeedsAsyncTask = new UserFeedsAsyncTask(MyActivities.this, circularProgressBar, url, authToken, userId);
            userFeedsAsyncTask.execute();
        } else {
            NetworkUtill.showNoInternetDialog(MyActivities.this);
        }

    }

    public class UserFeedsAsyncTask extends AsyncTask<Void, Void, Void> {
        CircularProgressBar circularProgressBar;
        Context context;
        String url;
        HttpClient client;
        ArrayList<FeedModel> feedModels;
        String userId;
        String authToken;
        JSONObject jo;
        SessionManager sessionManager;

        private String responseBody;

        public UserFeedsAsyncTask(Context context, CircularProgressBar circularProgressBar, String url, String authToken, String userId) {
            this.circularProgressBar = circularProgressBar;
            this.context = context;
            this.authToken = authToken;
            this.userId = userId;
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            //circularProgressBar.setVisibility(View.VISIBLE);
            if (circularProgressBar.getVisibility() == View.GONE)
                swipeLayout.setRefreshing(true);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                SSLSocketFactory sf = new SSLSocketFactory(SSLContext.getDefault(),
                        SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                Scheme sch = new Scheme("https", 443, sf);
                client = new DefaultHttpClient();
                client.getConnectionManager().getSchemeRegistry().register(sch);
                HttpPost httppost = new HttpPost(url);
                HttpResponse response;
                List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                pairs.add(new BasicNameValuePair(AppUrl.APP_ID_PARAM, AppUrl.APP_ID_VALUE_POST));
                pairs.add(new BasicNameValuePair("userID", userId));
                pairs.add(new BasicNameValuePair("pageNo", page + ""));
                httppost.setHeader("Authorization", "Basic " + authToken);
                httppost.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
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
            swipeLayout.setRefreshing(false);
            circularProgressBar.setVisibility(View.GONE);

            swipeLayout.setRefreshing(false);
            //  System.out.print(feedModels.size());
            try {
                if (jo != null) {

                    if (page == 1) {
                        feedModels = FeedParser.feedParserList(jo, page);
                        totalFeeds.add(new FeedModel());
                        totalFeeds.addAll(1, feedModels);
                    } else {
                        feedModels = FeedParser.feedParserList(jo, page);

                        totalFeeds.addAll(feedModels);
                    }
                    if (jo.has("totalpage") && !jo.isNull("totalpage")) {
                        totalPageCount = jo.getInt("totalpage");
                    }
                } else {
                    feedModels.add(new FeedModel());

                    totalFeeds.addAll(feedModels);

                }

                isLoading = false;

                if (feedsAdapter == null && totalFeeds.size() > 0) {
                    // appBarLayout.setExpanded(true);

                    feedsAdapter = new User_Activity_Adapter(context, totalFeeds, MyActivities.this, "", customViewContainer, circularProgressBar, name, profile_pic, title,userType,userId);
                    recyclerView.setAdapter(feedsAdapter);
                    recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            int totalItemCount = linearLayoutManager.getItemCount();
                            int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                            if (!isLoading && (totalItemCount - 1) <= (lastVisibleItem)) {
                                try {
                                    String has_more = "";
                                    if (page < totalPageCount) {
                                        page++;

                                        String url = AppUrl.BaseUrl + "feed/my-activity-new";
                                        userFeedsAsyncTask = new UserFeedsAsyncTask(context, circularProgressBar, url, authToken, userId);
                                        userFeedsAsyncTask.execute();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                isLoading = true;
                            }
                        }
                    });
                } else {
                    feedsAdapter.notifyDataSetChanged();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void cancelAsyncTask() {
            if (client != null && !isCancelled()) {
                cancel(true);
                client = null;
            }
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            if (ApplicationSingleton.isContactInfoUpdated()) {
                if (feedsAdapter != null) {
                    feedsAdapter.notifyDataSetChanged();
                }
                ApplicationSingleton.setIsContactInfoUpdated(false);
            }
            if (ApplicationSingleton.getPostEditPosition() != -1) {
                if (ApplicationSingleton.getFeedModelPostEdited() != null) {
                    totalFeeds.set(ApplicationSingleton.getPostEditPosition(), ApplicationSingleton.getFeedModelPostEdited());
                    feedsAdapter.notifyItemChanged(ApplicationSingleton.getPostEditPosition());

                }
                ApplicationSingleton.setPostEditPosition(-1);
                ApplicationSingleton.setFeedModelPostEdited(null);
            }
            if (ApplicationSingleton.getPostSelectedPostion() != -1) {
                if (ApplicationSingleton.getNoOfComment() != -1) {
                    totalFeeds.get(ApplicationSingleton.getPostSelectedPostion()).setTotal_comments(ApplicationSingleton.getNoOfComment() + "");
                    feedsAdapter.notifyItemChanged(ApplicationSingleton.getPostSelectedPostion());
                    ApplicationSingleton.setNoOfComment(-1);
                }
                if (ApplicationSingleton.getTotalLike() != -1) {
                    totalFeeds.get(ApplicationSingleton.getPostSelectedPostion()).setTotal_likes(ApplicationSingleton.getTotalLike() + "");

                    if (ApplicationSingleton.isCurruntUserLiked())
                        totalFeeds.get(ApplicationSingleton.getPostSelectedPostion()).setUser_has_liked(1);
                    else
                        totalFeeds.get(ApplicationSingleton.getPostSelectedPostion()).setUser_has_liked(0);
                    feedsAdapter.notifyItemChanged(ApplicationSingleton.getPostSelectedPostion());

                }
                ApplicationSingleton.setPostSelectedPostion(-1);

            }

            if (ApplicationSingleton.isContactInfoUpdated()) {
                updateContactInfo();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateContactInfo() {
        try {
            feedsAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        try {

            if (userFeedsAsyncTask != null && !userFeedsAsyncTask.isCancelled()) {
                userFeedsAsyncTask.cancelAsyncTask();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
