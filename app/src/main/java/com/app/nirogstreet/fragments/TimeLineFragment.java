package com.app.nirogstreet.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.app.nirogstreet.R;
import com.app.nirogstreet.activites.MainActivity;
import com.app.nirogstreet.activites.PostingActivity;
import com.app.nirogstreet.adapter.TimelineAdapter;
import com.app.nirogstreet.circularprogressbar.CircularProgressBar;
import com.app.nirogstreet.model.FeedModel;
import com.app.nirogstreet.parser.FeedParser;
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
 * Created by Preeti on 27-10-2017.
 */

public class TimeLineFragment extends Fragment {
    Context context;
    RecyclerView recyclerView;
    int totalPageCount;
    private boolean isLoading = false;
    FrameLayout customViewContainer;
    int page = 1;
    CircularProgressBar circularProgressBar;
    TimelineAdapter feedsAdapter;
    private SwipeRefreshLayout swipeLayout;

    private LinearLayoutManager linearLayoutManager;

    View view;
    private ArrayList<FeedModel> totalFeeds;
    private UserFeedsAsyncTask userFeedsAsyncTask;
    private SesstionManager session;
    String authToken, userId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.statusbarcolor));
        }

    }

    @Override
    public void onResume() {
        super.onResume();
       /* ((MainActivity) context).setTabText("Timeline");*/
        try {

            if (ApplicationSingleton.getPostSelectedPostion() != -1) {
                if (ApplicationSingleton.getNoOfComment() != -1) {
                    totalFeeds.get(ApplicationSingleton.getPostSelectedPostion()).setTotal_comments(ApplicationSingleton.getNoOfComment() + "");
                    feedsAdapter.notifyItemChanged(ApplicationSingleton.getPostSelectedPostion());
                    ApplicationSingleton.setNoOfComment(-1);
                }
                if (ApplicationSingleton.getTotalLike() != -1) {
                    totalFeeds.get(ApplicationSingleton.getPostSelectedPostion()).setTotal_comments(ApplicationSingleton.getTotalLike() + "");

                    if (ApplicationSingleton.isCurruntUserLiked())
                        totalFeeds.get(ApplicationSingleton.getPostSelectedPostion()).setUser_has_liked(1);
                    else
                        totalFeeds.get(ApplicationSingleton.getPostSelectedPostion()).setUser_has_liked(0);
                    feedsAdapter.notifyItemChanged(ApplicationSingleton.getPostSelectedPostion());

                }
                ApplicationSingleton.setPostSelectedPostion(-1);

            }
            boolean postsuccess = false;

            SharedPreferences sharedPref = getActivity().getSharedPreferences("imgvidupdate", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            postsuccess = sharedPref.getBoolean("imgvidupdate", false);
            if (ApplicationSingleton.isProfilePostExecuted() || ApplicationSingleton.isEditFeedPostExecuted()) {
                totalFeeds = new ArrayList<>();
                feedsAdapter = null;
                page = 1;
                editor.clear();
                editor.commit();
                if (NetworkUtill.isNetworkAvailable(context)) {
                    String url = AppUrl.BaseUrl + "feed/home";
                    userFeedsAsyncTask = new UserFeedsAsyncTask(context, circularProgressBar, url, authToken, userId);
                    userFeedsAsyncTask.execute();
                } else {
                    NetworkUtill.showNoInternetDialog(context);
                }
                ApplicationSingleton.setIsProfilePostExecuted(false);
            }

            if (feedsAdapter != null && totalFeeds != null && totalFeeds.size() > 0 && ApplicationSingleton.getPost_position() != -1) {

                if (ApplicationSingleton.isCommented()) {
                    totalFeeds.get(ApplicationSingleton.getPost_position()).setTotal_comments(ApplicationSingleton.getNo_of_count() + "");
                    feedsAdapter.notifyItemChanged(ApplicationSingleton.getPost_position());
                    ApplicationSingleton.setIsCommented(false);
                    ApplicationSingleton.setPost_position(-1);
                    ApplicationSingleton.setNo_of_count(-1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.timeline_feed, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setNestedScrollingEnabled(true);
        customViewContainer = (FrameLayout) view.findViewById(R.id.customViewContainer);

        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        swipeLayout.setColorScheme(R.color.gplus_color_1,
                R.color.gplus_color_2,
                R.color.gplus_color_1,
                R.color.gplus_color_2);
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);


        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ((MainActivity) context).setTabText("Home");
                totalFeeds = new ArrayList<>();
                feedsAdapter = null;
                page = 1;
                if (NetworkUtill.isNetworkAvailable(context)) {
                    String url = AppUrl.BaseUrl + "feed/home";
                    userFeedsAsyncTask = new UserFeedsAsyncTask(context, circularProgressBar, url, authToken, userId);
                    userFeedsAsyncTask.execute();
                } else {
                    NetworkUtill.showNoInternetDialog(context);
                }
                if (totalFeeds.size() > 0) {
                    recyclerView.scrollToPosition(0);
                }
            }

        });
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        circularProgressBar = (CircularProgressBar) view.findViewById(R.id.circularProgressBar);
        session = new SesstionManager(context);
        if (session.isUserLoggedIn()) {
            HashMap<String, String> userDetail = session.getUserDetails();
            authToken = userDetail.get(SesstionManager.AUTH_TOKEN);
            userId = userDetail.get(SesstionManager.USER_ID);
        }
        if (NetworkUtill.isNetworkAvailable(context)) {
            String url = AppUrl.BaseUrl + "feed/home";
            userFeedsAsyncTask = new UserFeedsAsyncTask(context, circularProgressBar, url, authToken, userId);
            userFeedsAsyncTask.execute();
        } else {
            NetworkUtill.showNoInternetDialog(context);
        }
        totalFeeds = new ArrayList<>();



        return view;
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

                if (feedsAdapter == null) {
                    // appBarLayout.setExpanded(true);
                    if (totalFeeds.size() > 0) {
                        feedsAdapter = new TimelineAdapter(context, totalFeeds, getActivity(), "", customViewContainer,circularProgressBar);
                        recyclerView.setAdapter(feedsAdapter);
                    }
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

                                        String url = AppUrl.BaseUrl + "feed/home";
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

}
