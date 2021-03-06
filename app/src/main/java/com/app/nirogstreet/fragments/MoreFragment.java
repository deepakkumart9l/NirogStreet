package com.app.nirogstreet.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.app.nirogstreet.R;
import com.app.nirogstreet.activites.AddOrEditClinicDetail;
import com.app.nirogstreet.activites.Award;
import com.app.nirogstreet.activites.CreateDrProfile;
import com.app.nirogstreet.activites.Dr_Profile;
import com.app.nirogstreet.activites.Dr_Qualifications;
import com.app.nirogstreet.activites.Experience;
import com.app.nirogstreet.activites.MainActivity;
import com.app.nirogstreet.activites.MemberShip;
import com.app.nirogstreet.activites.RegistrationAndDocuments;
import com.app.nirogstreet.activites.SpecilizationAndService;
import com.app.nirogstreet.activites.Timings;
import com.app.nirogstreet.adapter.MoreAdapter;
import com.app.nirogstreet.adapter.TimelineAdapter;
import com.app.nirogstreet.circularprogressbar.CircularProgressBar;
import com.app.nirogstreet.model.FeedModel;
import com.app.nirogstreet.model.UserDetailModel;
import com.app.nirogstreet.parser.FeedParser;
import com.app.nirogstreet.parser.UserDetailPaser;
import com.app.nirogstreet.uttil.AppUrl;
import com.app.nirogstreet.uttil.ApplicationSingleton;
import com.app.nirogstreet.uttil.Event_For_Firebase;
import com.app.nirogstreet.uttil.ImageLoader;
import com.app.nirogstreet.uttil.NetworkUtill;
import com.app.nirogstreet.uttil.SesstionManager;
import com.google.android.gms.cast.framework.SessionManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
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
 * Created by Preeti on 31-10-2017.
 */

public class MoreFragment extends Fragment {
    View view;
    Context context;

    private UserDetailModel userDetailModel;
    RecyclerView recyclerView;
    int totalPageCount;

    UserFeedsAsyncTask userFeedsAsyncTask;
    FrameLayout customViewContainer;
    UserDetailAsyncTask userDetailAsyncTask;
    private boolean isLoading = false;

    int page = 1;
    CircularProgressBar circularProgressBar;
    MoreAdapter feedsAdapter;
    private SwipeRefreshLayout swipeLayout;

    private LinearLayoutManager linearLayoutManager;

    private ArrayList<FeedModel> totalFeeds;
    private SesstionManager session;
    String authToken, userId;

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
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

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
    public void onPause() {
        super.onPause();
        try {
            if (userDetailAsyncTask != null && !userDetailAsyncTask.isCancelled()) {
                userDetailAsyncTask.cancelAsyncTask();
            }
            if (userFeedsAsyncTask != null && !userFeedsAsyncTask.isCancelled()) {
                userFeedsAsyncTask.cancelAsyncTask();
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.more, container, false);
        Event_For_Firebase.getEventCount(getActivity(),"Feed_Profile_Visit");
        circularProgressBar = (CircularProgressBar) view.findViewById(R.id.circularProgressBar);
        session = new SesstionManager(context);
        customViewContainer = (FrameLayout) view.findViewById(R.id.customViewContainer);

        if (session.isUserLoggedIn()) {
            HashMap<String, String> userDetail = session.getUserDetails();
            authToken = userDetail.get(SesstionManager.AUTH_TOKEN);
            userId = userDetail.get(SesstionManager.USER_ID);
        }
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setNestedScrollingEnabled(true);
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        swipeLayout.setColorScheme(R.color.gplus_color_1,
                R.color.gplus_color_2,
                R.color.gplus_color_1,
                R.color.gplus_color_2);
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);


        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ((MainActivity) context).setTabText("You");
                totalFeeds = new ArrayList<>();
                feedsAdapter = null;
                page = 1;
                if (NetworkUtill.isNetworkAvailable(context))

                {
                    userDetailAsyncTask = new UserDetailAsyncTask();
                    userDetailAsyncTask.execute();
                } else {
                    NetworkUtill.showNoInternetDialog(context);
                }
                if (totalFeeds.size() > 0) {
                    //  recyclerView.scrollToPosition(0);
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

        totalFeeds = new ArrayList<>();


        if (NetworkUtill.isNetworkAvailable(context))

        {
            userDetailAsyncTask = new UserDetailAsyncTask();
            userDetailAsyncTask.execute();
        } else {
            NetworkUtill.showNoInternetDialog(context);
        }
        return view;

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
          //  circularProgressBar.setVisibility(View.VISIBLE);

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
                                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                userDetailModel = UserDetailPaser.userDetailParser(dataJsonObject);
                                ApplicationSingleton.setUserDetailModel(userDetailModel);
                                // updateContactInfo();
                                totalFeeds = new ArrayList<>();
                                feedsAdapter = null;
                                page = 1;
                                if (NetworkUtill.isNetworkAvailable(context)) {
                                    String url = AppUrl.BaseUrl + "feed/activity-update";
                                    userFeedsAsyncTask = new UserFeedsAsyncTask(context, circularProgressBar, url, authToken, userId);
                                    userFeedsAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                } else {
                                    NetworkUtill.showNoInternetDialog(context);
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

    private void updateContactInfo() {
        try {
            feedsAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
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
            if(circularProgressBar.getVisibility()==View.GONE)
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
                        feedModels = FeedParser.feedParserListRecent(jo, page);
                        totalFeeds.add(new FeedModel());
                        totalFeeds.addAll(1, feedModels);
                    } else {
                        feedModels = FeedParser.feedParserListRecent(jo, page);

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

                    feedsAdapter = new MoreAdapter(context, totalFeeds, getActivity(), "", customViewContainer, circularProgressBar);
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

                                        String url = AppUrl.BaseUrl + "feed/activity-update";
                                        userFeedsAsyncTask = new UserFeedsAsyncTask(context, circularProgressBar, url, authToken, userId);
                                        userFeedsAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);;
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
