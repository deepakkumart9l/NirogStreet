package com.app.nirogstreet.activites;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.nirogstreet.R;
import com.app.nirogstreet.adapter.Courses_Listing_Adapter;
import com.app.nirogstreet.circularprogressbar.CircularProgressBar;
import com.app.nirogstreet.model.CoursesModel;
import com.app.nirogstreet.parser.Courses_Parser;
import com.app.nirogstreet.uttil.AppUrl;
import com.app.nirogstreet.uttil.ApplicationSingleton;
import com.app.nirogstreet.uttil.Event_For_Firebase;
import com.app.nirogstreet.uttil.NetworkUtill;
import com.app.nirogstreet.uttil.SesstionManager;
import com.app.nirogstreet.uttil.TypeFaceMethods;

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
 * Created by Preeti on 22-02-2018.
 */

public class CoursesListing extends Activity {
    int page = 1;
    RecyclerView recyclerView;
    ImageView imageViewback;
    private static final int REQUEST_FOR_ACTIVITY_CODE = 6;
    private static final int REQUEST_FOR_UPDAED = 7;
    ArrayList<CoursesModel> groupModelsTotal = new ArrayList<>();
    String authToken, userId;
    int totalPageCount;
    View userView, allView;
    RelativeLayout searchLayout;
    LinearLayoutManager linearLayoutManager;
    CircularProgressBar circularProgressBar;
    GroupsOfUserAsyncTask groupsOfUserAsyncTask;
    TextView createTextView;
    String logedinuserId;
    Button button;
    LinearLayout no_list;
    LinearLayout linearLayout1;
    Courses_Listing_Adapter groupListingAdapter;
    TextView myGroupTextView, otherGroupTextView;
    private boolean isLoading = false;
    ImageView imageViewBack;
    SesstionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.courses_listing_new);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.statusbarcolor));
        }

        sessionManager = new SesstionManager(CoursesListing.this);
        linearLayout1 = (LinearLayout) findViewById(R.id.linearLayout1);
        no_list = (LinearLayout) findViewById(R.id.no_list);
        button = (Button) findViewById(R.id.join_com);
        HashMap<String, String> userDetails = sessionManager.getUserDetails();
        authToken = userDetails.get(SesstionManager.AUTH_TOKEN);
        logedinuserId = userDetails.get(SesstionManager.USER_ID);
        userId = userDetails.get(SesstionManager.USER_ID);
        myGroupTextView = (TextView) findViewById(R.id.myGroup);
        myGroupTextView.setText("My Courses");
        circularProgressBar = (CircularProgressBar) findViewById(R.id.circularProgressBar);
        otherGroupTextView = (TextView) findViewById(R.id.otherGroup);
        otherGroupTextView.setText("All Courses");
        userView = (View) findViewById(R.id.userview);
        allView = (View) findViewById(R.id.allview);
        userView.setSelected(true);
        imageViewback = (ImageView) findViewById(R.id.back);
        imageViewback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        myGroupTextView.setTextColor(getResources().getColor(R.color.buttonBackground));
        otherGroupTextView.setTextColor(getResources().getColor(R.color.unselectedtext));

        myGroupTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.removeAllViews();
                TypeFaceMethods.setRegularTypeBoldFaceTextView(myGroupTextView, CoursesListing.this);
                TypeFaceMethods.setRegularTypeFaceForTextView(otherGroupTextView, CoursesListing.this);
                otherGroupTextView.setClickable(false);
                userView.setSelected(true);
                allView.setSelected(false);
                page = 1;
                isLoading = false;
                groupListingAdapter = null;

                groupModelsTotal = new ArrayList<CoursesModel>();
                recyclerView.setVisibility(View.GONE);
                myGroupTextView.setTextColor(getResources().getColor(R.color.buttonBackground));
                otherGroupTextView.setTextColor(getResources().getColor(R.color.unselectedtext));
                if (NetworkUtill.isNetworkAvailable(CoursesListing.this)) {
                    String url = AppUrl.BaseUrl + "knowledge/user-courses";

                    groupsOfUserAsyncTask = new GroupsOfUserAsyncTask(userId, authToken, url, true, CoursesListing.this, false);
                    groupsOfUserAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    NetworkUtill.showNoInternetDialog(CoursesListing.this);
                    myGroupTextView.setClickable(true);
                    otherGroupTextView.setClickable(true);

                }
            }
        });
        otherGroupTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page = 1;
                Event_For_Firebase.getEventCount(CoursesListing.this,"Feed_Learning_Screen_AllCourses_Screen_Visit");
              /*  TypeFaceMethods.setRegularTypeBoldFaceTextView(otherGroupTextView, CoursesListing.this);
                TypeFaceMethods.setRegularTypeFaceForTextView(myGroupTextView, context);*/
                groupModelsTotal = new ArrayList<CoursesModel>();
                no_list.setVisibility(View.GONE);
                recyclerView.removeAllViews();
                isLoading = false;
                myGroupTextView.setClickable(false);
                allView.setSelected(true);
                groupListingAdapter = null;
                recyclerView.setVisibility(View.GONE);
                otherGroupTextView.setSelected(true);
                userView.setSelected(false);

                otherGroupTextView.setTextColor(getResources().getColor(R.color.buttonBackground));
                myGroupTextView.setTextColor(getResources().getColor(R.color.unselectedtext));
                if (NetworkUtill.isNetworkAvailable(CoursesListing.this)) {
                    String url = AppUrl.BaseUrl + "knowledge/all-courses";
                    groupsOfUserAsyncTask = new GroupsOfUserAsyncTask(userId, authToken, url, false, CoursesListing.this, true);
                    groupsOfUserAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    NetworkUtill.showNoInternetDialog(CoursesListing.this);
                    myGroupTextView.setClickable(true);
                    otherGroupTextView.setClickable(true);

                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page = 1;
                groupModelsTotal = new ArrayList<CoursesModel>();
                userView.setSelected(false);
                recyclerView.removeAllViews();
                recyclerView.setVisibility(View.VISIBLE);
                no_list.setVisibility(View.GONE);
                isLoading = false;
                myGroupTextView.setClickable(false);
                allView.setSelected(true);
                groupListingAdapter = null;
                recyclerView.setVisibility(View.GONE);
                otherGroupTextView.setSelected(true);
                otherGroupTextView.setTextColor(getResources().getColor(R.color.buttonBackground));
                myGroupTextView.setTextColor(getResources().getColor(R.color.unselectedtext));
                if (NetworkUtill.isNetworkAvailable(CoursesListing.this)) {
                    String url = AppUrl.BaseUrl + "knowledge/all-courses";

                    groupsOfUserAsyncTask = new GroupsOfUserAsyncTask(userId, authToken, url, false, CoursesListing.this, true);
                    groupsOfUserAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    NetworkUtill.showNoInternetDialog(CoursesListing.this);
                    myGroupTextView.setClickable(true);
                    otherGroupTextView.setClickable(true);

                }
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        linearLayoutManager = new LinearLayoutManager(CoursesListing.this);

        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);

        recyclerView.setLayoutManager(linearLayoutManager);
        try {
            if (logedinuserId.equals(userId))
                if (NetworkUtill.isNetworkAvailable(CoursesListing.this)) {
                    otherGroupTextView.setSelected(true);
                    userView.setSelected(false);
                    allView.setSelected(true);

                    otherGroupTextView.setTextColor(getResources().getColor(R.color.buttonBackground));
                    myGroupTextView.setTextColor(getResources().getColor(R.color.unselectedtext));

                    String url = AppUrl.BaseUrl + "knowledge/all-courses";
                    groupsOfUserAsyncTask = new GroupsOfUserAsyncTask(userId, authToken, url, false, CoursesListing.this, true);
                    groupsOfUserAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    NetworkUtill.showNoInternetDialog(CoursesListing.this);
                    myGroupTextView.setClickable(true);
                    otherGroupTextView.setClickable(true);

                }
            else {
                if (NetworkUtill.isNetworkAvailable(CoursesListing.this)) {
                    String url = AppUrl.BaseUrl + "knowledge/all-courses";

                    groupsOfUserAsyncTask = new GroupsOfUserAsyncTask(userId, authToken, url, false, CoursesListing.this, true);
                    groupsOfUserAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    NetworkUtill.showNoInternetDialog(CoursesListing.this);
                    myGroupTextView.setClickable(true);
                    otherGroupTextView.setClickable(true);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ApplicationSingleton.isCourseSubscribe()) {
            userView.setSelected(true);
            allView.setSelected(false);
            page = 1;
            groupListingAdapter = null;
            groupModelsTotal = new ArrayList<CoursesModel>();
            recyclerView.setVisibility(View.GONE);
            myGroupTextView.setTextColor(getResources().getColor(R.color.black));

            otherGroupTextView.setTextColor(getResources().getColor(R.color.unselectedtext));
            if (NetworkUtill.isNetworkAvailable(CoursesListing.this)) {
                String url = AppUrl.BaseUrl + "knowledge/user-courses";

                groupsOfUserAsyncTask = new GroupsOfUserAsyncTask(userId, authToken, url, true, CoursesListing.this, false);
                groupsOfUserAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            } else {
                NetworkUtill.showNoInternetDialog(CoursesListing.this);
            }
            ApplicationSingleton.setCourseSubscribe(false);
        }

    }

    public class GroupsOfUserAsyncTask extends AsyncTask<Void, Void, Void> {
        Context context;
        HttpClient client;
        String userId;
        String authToken;
        JSONObject jo;
        String url;
        boolean isHide;
        SesstionManager sessionManager;
        boolean showJoin;
        private String responseBody;

        public void cancelAsyncTask() {
            if (client != null && !isCancelled()) {
                cancel(true);
                client = null;
            }
        }

        public GroupsOfUserAsyncTask(String userId, String authToken, String url, boolean isHide, Context context, boolean showJoin) {
            this.userId = userId;
            this.isHide = isHide;
            this.showJoin = showJoin;
            this.context = context;
            this.authToken = authToken;
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            circularProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
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
                pairs.add(new BasicNameValuePair("pageNo", page + ""));
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
            circularProgressBar.setVisibility(View.GONE);
            ArrayList<CoursesModel> groupModels = new ArrayList<>();
            groupModels = Courses_Parser.groupListingParser(jo);
            otherGroupTextView.setClickable(true);
            myGroupTextView.setClickable(true);
            if (page == 1) {
                groupModelsTotal = new ArrayList<>();
            }
            groupModelsTotal.addAll(groupModels);

            super.onPostExecute(aVoid);
            try {
                if (jo != null) {
                    recyclerView.setVisibility(View.VISIBLE);

                    if (jo.has("response") && !jo.isNull("response")) {
                        JSONObject jsonObjectResponce = jo.getJSONObject("response");
                        if (jsonObjectResponce.has("totalpage") && !jsonObjectResponce.isNull("totalpage")) {
                            totalPageCount = jsonObjectResponce.getInt("totalpage");

                        }
                    }
                    isLoading = false;
                    if (groupModelsTotal.size() == 0) {
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        recyclerView.setVisibility(View.VISIBLE);

                    }
                    if (isHide && groupModelsTotal.size() == 0) {
                        recyclerView.setVisibility(View.GONE);
                        no_list.setVisibility(View.VISIBLE);
                    }

                    if (groupListingAdapter == null && groupModelsTotal != null && groupModelsTotal.size() > 0) {
                        groupListingAdapter = new Courses_Listing_Adapter(groupModelsTotal, context, isHide, userId, showJoin);
                        recyclerView.setAdapter(groupListingAdapter);
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

                                            groupsOfUserAsyncTask = new GroupsOfUserAsyncTask(userId, authToken, url, isHide, context, showJoin);
                                            groupsOfUserAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    isLoading = true;
                                } else {
                                    recyclerView.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                    } else {
                        groupListingAdapter.notifyDataSetChanged();
                        // recyclerView.scrollToPosition(groupListingAdapter.getItemCount());
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (groupsOfUserAsyncTask != null && !groupsOfUserAsyncTask.isCancelled()) {
            groupsOfUserAsyncTask.cancelAsyncTask();
        }
    }
}