package com.app.nirogstreet.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import okhttp3.internal.http.HttpMethod;

import static com.makeramen.roundedimageview.RoundedImageView.TAG;

/**
 * Created by Preeti on 27-10-2017.
 */

public class TimeLineFragment extends Fragment {
    Context context;
    RecyclerView recyclerView;
    int totalPageCount;
    private boolean isLoading = false;
    StringRequest stringRequest;
    FrameLayout customViewContainer;
    int page = 1;
    CircularProgressBar circularProgressBar;
    TimelineAdapter feedsAdapter;
    private SwipeRefreshLayout swipeLayout;
    FrameLayout floatingActionButton;
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
            if (ApplicationSingleton.getPostEditPosition() != -1) {
                if (ApplicationSingleton.getFeedModelPostEdited() != null) {
                    totalFeeds.set(ApplicationSingleton.getPostEditPosition(), ApplicationSingleton.getFeedModelPostEdited());
                    feedsAdapter.notifyItemChanged(ApplicationSingleton.getPostEditPosition());

                }
                ApplicationSingleton.setPostEditPosition(-1);
                ApplicationSingleton.setFeedModelPostEdited(null);
            }
            if (ApplicationSingleton.getPostEditPosition() != -1) {
                if (ApplicationSingleton.isPostDeleted()) {
                    totalFeeds.remove(ApplicationSingleton.getPostEditPosition());
                    feedsAdapter.notifyDataSetChanged();

                }
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
                    // volley(url);
                    userFeedsAsyncTask = new UserFeedsAsyncTask(context, circularProgressBar, url, authToken, userId);
                    userFeedsAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
        floatingActionButton = (FrameLayout) view.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PostingActivity.class);

                context.startActivity(intent);
            }
        });
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
                   //  volley(url);
                    userFeedsAsyncTask = new UserFeedsAsyncTask(context, circularProgressBar, url, authToken, userId);
                    userFeedsAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    NetworkUtill.showNoInternetDialog(context);
                }
                if (totalFeeds.size() > 0) {
                    if (page == 1) {
                        recyclerView.smoothScrollToPosition(0);

                    }
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
           // volley(url);
            userFeedsAsyncTask = new UserFeedsAsyncTask(context, circularProgressBar, url, authToken, userId);
            userFeedsAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

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
            String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
            System.out.print("Strat Time" + currentDateTimeString);
            swipeLayout.setRefreshing(true);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
            /*    SSLSocketFactory sf = new SSLSocketFactory(SSLContext.getDefault(),
                        SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                Scheme sch = new Scheme("https", 443, sf);*/
                client = new DefaultHttpClient();
                // client.getConnectionManager().getSchemeRegistry().register(sch);
                //  HttpMethod method = new HeadMethod("http://stackoverflow.com/");
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
                Log.e(TAG, "doInBackground: ", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
            System.out.print("end Time" + currentDateTimeString);
            //  System.out.println(String.format("%s %s %d: %s", method.getName(), method.getURI(), method.getStatusCode(), watch.toString()));
            swipeLayout.setRefreshing(false);
            circularProgressBar.setVisibility(View.GONE);

            swipeLayout.setRefreshing(false);
            //  System.out.print(feedModels.size());
            try {
                if (jo != null) {

                    if (page == 1) {
                        feedModels = FeedParser.feedParserList(jo, page);
                        totalFeeds.add(new FeedModel());

                        recyclerView.smoothScrollToPosition(0);


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
                        feedsAdapter = new TimelineAdapter(context, totalFeeds, getActivity(), "", customViewContainer, circularProgressBar);
                        recyclerView.setAdapter(feedsAdapter);
                        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                super.onScrolled(recyclerView, dx, dy);

                                int totalItemCount = linearLayoutManager.getItemCount();
                                int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                                if (linearLayoutManager.findFirstVisibleItemPosition() == 0) {
                                    floatingActionButton.setVisibility(View.GONE);
                                } else {
                                    floatingActionButton.setVisibility(View.VISIBLE);
                                }
                                if (!isLoading && (totalItemCount - 1) <= (lastVisibleItem)) {
                                    try {
                                        String has_more = "";
                                        if (page < totalPageCount) {
                                            page++;

                                            String url = AppUrl.BaseUrl + "feed/home";
                                            userFeedsAsyncTask = new UserFeedsAsyncTask(context, circularProgressBar, url, authToken, userId);
                                            userFeedsAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    isLoading = true;
                                }
                            }
                        });
                    }

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
    public void onPause() {
        super.onPause();
        swipeLayout.setRefreshing(false);
        if (userFeedsAsyncTask != null && !userFeedsAsyncTask.isCancelled()) {
            userFeedsAsyncTask.cancelAsyncTask();
        }
    }


    public void volley(String url) {
        swipeLayout.setRefreshing(true);

        RequestQueue queue = Volley.newRequestQueue(context);
        //this is the url where you want to send the request
        //TODO: replace with your own url to send request, as I am using my own localhost for this tutorial
       // String url = "http://api.nirogstreet.com/v2/feed/home";

        // Request a string response from the provided URL.
         stringRequest= new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        swipeLayout.setRefreshing(false);
                        circularProgressBar.setVisibility(View.GONE);

                        swipeLayout.setRefreshing(false);
                        // Display the response string.
                        Log.e("eresponse", "" + response);
                        setData(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                swipeLayout.setRefreshing(false);
                circularProgressBar.setVisibility(View.GONE);

                swipeLayout.setRefreshing(false);
                Log.e("That didn't work!", "");
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(AppUrl.APP_ID_PARAM, AppUrl.APP_ID_VALUE_POST);
                params.put("userID", userId);
                params.put("pageNo", page+"");
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


    public void setData(String response) {
        swipeLayout.setRefreshing(false);
        circularProgressBar.setVisibility(View.GONE);

        swipeLayout.setRefreshing(false);
        //  System.out.print(feedModels.size());
        try {
            String responseBody = null;
            JSONObject jo;
            ArrayList<FeedModel> feedModels = new ArrayList<>();


            //  responseBody = EntityUtils.toString(response, "UTF-8");
            jo = new JSONObject(response);
            if (jo != null) {

                if (page == 1) {
                    feedModels = FeedParser.feedParserList(jo, page);
                    totalFeeds.add(new FeedModel());

                    recyclerView.smoothScrollToPosition(0);


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
                    feedsAdapter = new TimelineAdapter(context, totalFeeds, getActivity(), "", customViewContainer, circularProgressBar);
                    recyclerView.setAdapter(feedsAdapter);
                    recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            int totalItemCount = linearLayoutManager.getItemCount();
                            int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                            if (linearLayoutManager.findFirstVisibleItemPosition() == 0) {
                                floatingActionButton.setVisibility(View.GONE);
                            } else {
                                floatingActionButton.setVisibility(View.VISIBLE);
                            }
                            if (!isLoading && (totalItemCount - 1) <= (lastVisibleItem)) {
                                try {
                                    String has_more = "";
                                    if (page < totalPageCount) {
                                        page++;

                                        String url = AppUrl.BaseUrl + "feed/home";
                                       volley(url);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                isLoading = true;
                            }
                        }
                    });
                }

            } else {
                feedsAdapter.notifyDataSetChanged();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
