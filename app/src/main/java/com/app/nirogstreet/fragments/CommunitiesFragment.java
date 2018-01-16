package com.app.nirogstreet.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.AppLaunchChecker;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.nirogstreet.R;
import com.app.nirogstreet.adapter.GroupListingAdapter;
import com.app.nirogstreet.circularprogressbar.CircularProgressBar;
import com.app.nirogstreet.model.GroupModel;
import com.app.nirogstreet.parser.Group_Listing_Parser;
import com.app.nirogstreet.uttil.AppUrl;
import com.app.nirogstreet.uttil.ApplicationSingleton;
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
 * Created by Preeti on 01-11-2017.
 */

public class CommunitiesFragment extends Fragment {
    int page = 1;
    RecyclerView recyclerView;
    Button button;
    LinearLayout no_list;

    ImageView imageViewback;
    private static final int REQUEST_FOR_ACTIVITY_CODE = 6;
    private static final int REQUEST_FOR_UPDAED = 7;

    ArrayList<GroupModel> groupModelsTotal = new ArrayList<>();

    String authToken, userId;
    int totalPageCount;
    View userView, allView;
    RelativeLayout searchLayout;
    LinearLayoutManager linearLayoutManager;

    CircularProgressBar circularProgressBar;
    GroupsOfUserAsyncTask groupsOfUserAsyncTask;
    GroupsOfUserAsyncTaskAll groupsOfUserAsyncTaskAll;
    TextView createTextView;
    String logedinuserId;
    LinearLayout linearLayout1;
    GroupListingAdapter groupListingAdapter, allGroupListingAdapter;
    TextView myGroupTextView, otherGroupTextView;
    private boolean isLoading = false;
    ImageView imageViewBack;
    View view;
    Context context;
    SesstionManager sessionManager;

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
        if (ApplicationSingleton.isJoinedCommunity()) {

            page = 1;
            groupModelsTotal = new ArrayList<GroupModel>();
            userView.setSelected(false);
            recyclerView.removeAllViews();
            isLoading = false;
            no_list.setVisibility(View.GONE);

            //  TypeFaceMethods.setRegularTypeBoldFaceTextView(otherGroupTextView, context);
            myGroupTextView.setClickable(false);
            allView.setSelected(true);
            allGroupListingAdapter = null;
            recyclerView.setVisibility(View.GONE);
            otherGroupTextView.setSelected(true);
            otherGroupTextView.setTextColor(getResources().getColor(R.color.buttonBackground));
            myGroupTextView.setTextColor(getResources().getColor(R.color.unselectedtext));
            if (NetworkUtill.isNetworkAvailable(context)) {
                String url = AppUrl.BaseUrl + "group/all-groups-new";

                groupsOfUserAsyncTaskAll = new GroupsOfUserAsyncTaskAll(userId, authToken, url, false, context, true);
                groupsOfUserAsyncTaskAll.execute();
            } else {
                NetworkUtill.showNoInternetDialog(context);
            }
            ApplicationSingleton.setIsJoinedCommunity(false);
        }
        if (ApplicationSingleton.isGroupCreated() || ApplicationSingleton.isGroupUpdated()) {
            userView.setSelected(true);
            allView.setSelected(false);
            no_list.setVisibility(View.GONE);
            page = 1;
            //  TypeFaceMethods.setRegularTypeBoldFaceTextView(myGroupTextView, context);
            // TypeFaceMethods.setRegularTypeFaceForTextView(otherGroupTextView, context);
            groupListingAdapter = null;
            groupModelsTotal = new ArrayList<GroupModel>();
            recyclerView.setVisibility(View.GONE);
            myGroupTextView.setTextColor(getResources().getColor(R.color.black));
            otherGroupTextView.setTextColor(getResources().getColor(R.color.unselectedtext));
            if (NetworkUtill.isNetworkAvailable(context)) {
                String url = AppUrl.BaseUrl + "group/index";

                groupsOfUserAsyncTask = new GroupsOfUserAsyncTask(userId, authToken, url, true, context, false);
                groupsOfUserAsyncTask.execute();

            } else {
                NetworkUtill.showNoInternetDialog(context);
            }
            ApplicationSingleton.setIsGroupCreated(false);
            ApplicationSingleton.setIsGroupUpdated(false);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.communities_listing, container, false);

        sessionManager = new SesstionManager(context);
        linearLayout1 = (LinearLayout) view.findViewById(R.id.linearLayout1);
        no_list = (LinearLayout) view.findViewById(R.id.no_list);
        button = (Button) view.findViewById(R.id.join_com);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page = 1;
                groupModelsTotal = new ArrayList<GroupModel>();
                userView.setSelected(false);
                recyclerView.removeAllViews();
                recyclerView.setVisibility(View.VISIBLE);
                no_list.setVisibility(View.GONE);
                isLoading = false;
                //  TypeFaceMethods.setRegularTypeBoldFaceTextView(otherGroupTextView, context);
                // TypeFaceMethods.setRegularTypeFaceForTextView(myGroupTextView, context);
                myGroupTextView.setClickable(false);
                allView.setSelected(true);
                allGroupListingAdapter = null;
                recyclerView.setVisibility(View.GONE);
                otherGroupTextView.setSelected(true);
                otherGroupTextView.setTextColor(getResources().getColor(R.color.buttonBackground));
                myGroupTextView.setTextColor(getResources().getColor(R.color.unselectedtext));
                if (NetworkUtill.isNetworkAvailable(context)) {
                    String url = AppUrl.BaseUrl + "group/all-groups-new";

                    groupsOfUserAsyncTaskAll = new GroupsOfUserAsyncTaskAll(userId, authToken, url, false, context, true);
                    groupsOfUserAsyncTaskAll.execute();
                } else {
                    NetworkUtill.showNoInternetDialog(context);
                }

            }
        });
        HashMap<String, String> userDetails = sessionManager.getUserDetails();
        authToken = userDetails.get(SesstionManager.AUTH_TOKEN);
        logedinuserId = userDetails.get(SesstionManager.USER_ID);
        userId = userDetails.get(SesstionManager.USER_ID);
        myGroupTextView = (TextView) view.findViewById(R.id.myGroup);
        circularProgressBar = (CircularProgressBar) view.findViewById(R.id.circularProgressBar);
        otherGroupTextView = (TextView) view.findViewById(R.id.otherGroup);
        userView = (View) view.findViewById(R.id.userview);
        allView = (View) view.findViewById(R.id.allview);
        userView.setSelected(true);
        imageViewback = (ImageView) view.findViewById(R.id.back);
        myGroupTextView.setTextColor(getResources().getColor(R.color.buttonBackground));
        otherGroupTextView.setTextColor(getResources().getColor(R.color.unselectedtext));
        //   TypeFaceMethods.setRegularTypeBoldFaceTextView(myGroupTextView, context);
        //   TypeFaceMethods.setRegularTypeFaceForTextView(otherGroupTextView, context);

        myGroupTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.removeAllViews();
                otherGroupTextView.setClickable(false);
                //   TypeFaceMethods.setRegularTypeBoldFaceTextView(myGroupTextView, context);
                //  TypeFaceMethods.setRegularTypeFaceForTextView(otherGroupTextView, context);
                myGroupTextView.setClickable(false);
                userView.setSelected(true);
                allView.setSelected(false);
                page = 1;
                isLoading = false;
                groupListingAdapter = null;

                groupModelsTotal = new ArrayList<GroupModel>();
                recyclerView.setVisibility(View.GONE);
                myGroupTextView.setTextColor(getResources().getColor(R.color.buttonBackground));
                otherGroupTextView.setTextColor(getResources().getColor(R.color.unselectedtext));
                if (NetworkUtill.isNetworkAvailable(context)) {
                    String url = AppUrl.BaseUrl + "group/index";

                    groupsOfUserAsyncTask = new GroupsOfUserAsyncTask(userId, authToken, url, true, context, false);
                    groupsOfUserAsyncTask.execute();
                } else {
                    NetworkUtill.showNoInternetDialog(context);
                    myGroupTextView.setClickable(true);
                    otherGroupTextView.setClickable(true);
                }
            }
        });
        otherGroupTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page = 1;
                groupModelsTotal = new ArrayList<GroupModel>();
                userView.setSelected(false);
                recyclerView.removeAllViews();
                isLoading = false;
                no_list.setVisibility(View.GONE);
                otherGroupTextView.setClickable(false);                //  TypeFaceMethods.setRegularTypeBoldFaceTextView(otherGroupTextView, context);
                // TypeFaceMethods.setRegularTypeFaceForTextView(myGroupTextView, context);
                myGroupTextView.setClickable(false);
                allView.setSelected(true);
                allGroupListingAdapter = null;
                recyclerView.setVisibility(View.GONE);
                otherGroupTextView.setSelected(true);
                otherGroupTextView.setTextColor(getResources().getColor(R.color.buttonBackground));
                myGroupTextView.setTextColor(getResources().getColor(R.color.unselectedtext));
                if (NetworkUtill.isNetworkAvailable(context)) {
                    String url = AppUrl.BaseUrl + "group/all-groups-new";

                    groupsOfUserAsyncTaskAll = new GroupsOfUserAsyncTaskAll(userId, authToken, url, false, context, true);
                    groupsOfUserAsyncTaskAll.execute();
                } else {
                    NetworkUtill.showNoInternetDialog(context);
                    myGroupTextView.setClickable(true);
                    otherGroupTextView.setClickable(true);

                }
            }
        });
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        linearLayoutManager = new LinearLayoutManager(context);

        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        if (logedinuserId.equals(userId))
            if (NetworkUtill.isNetworkAvailable(context)) {
                String url = AppUrl.BaseUrl + "group/index";

                groupsOfUserAsyncTask = new GroupsOfUserAsyncTask(userId, authToken, url, true, context, false);
                groupsOfUserAsyncTask.execute();
            } else {
                NetworkUtill.showNoInternetDialog(context);
                myGroupTextView.setClickable(true);
                otherGroupTextView.setClickable(true);

            }
        else {
            if (NetworkUtill.isNetworkAvailable(context)) {
                String url = AppUrl.BaseUrl + "group/all-groups-new";

                groupsOfUserAsyncTaskAll = new GroupsOfUserAsyncTaskAll(userId, authToken, url, false, context, true);
                groupsOfUserAsyncTask.execute();
            } else {
                NetworkUtill.showNoInternetDialog(context);
                myGroupTextView.setClickable(true);
                otherGroupTextView.setClickable(true);

            }
        }

        return view;
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
            ArrayList<GroupModel> groupModels = new ArrayList<>();
            myGroupTextView.setTextColor(getResources().getColor(R.color.buttonBackground));
            otherGroupTextView.setTextColor(getResources().getColor(R.color.unselectedtext));
            userView.setSelected(true);
            allView.setSelected(false);
            groupModels = Group_Listing_Parser.groupListingParser(jo, showJoin);
            otherGroupTextView.setClickable(true);
            myGroupTextView.setClickable(true);
            groupModelsTotal.addAll(groupModels);
            super.onPostExecute(aVoid);
            try {
                if (jo != null)

                {
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
                    if (isHide && groupModels.size() == 0) {
                        recyclerView.setVisibility(View.GONE);
                        no_list.setVisibility(View.VISIBLE);
                    }
                    if (groupListingAdapter == null && groupModelsTotal != null && groupModelsTotal.size() > 0) {
                        groupListingAdapter = new GroupListingAdapter(groupModelsTotal, context, isHide, userId, showJoin);
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
                                            groupsOfUserAsyncTask.execute();
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

    public class GroupsOfUserAsyncTaskAll extends AsyncTask<Void, Void, Void> {
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

        public GroupsOfUserAsyncTaskAll(String userId, String authToken, String url, boolean isHide, Context context, boolean showJoin) {
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
            ArrayList<GroupModel> groupModels = new ArrayList<>();
            groupModels = Group_Listing_Parser.groupListingParser(jo, showJoin);
            otherGroupTextView.setClickable(true);
            otherGroupTextView.setSelected(true);
            userView.setSelected(false);
            allView.setSelected(true);
            otherGroupTextView.setTextColor(getResources().getColor(R.color.buttonBackground));
            myGroupTextView.setTextColor(getResources().getColor(R.color.unselectedtext));

            myGroupTextView.setClickable(true);
            groupModelsTotal.addAll(groupModels);
            super.onPostExecute(aVoid);
            try {
                if (jo != null)

                {
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
                    if (isHide && groupModels.size() == 0) {
                        recyclerView.setVisibility(View.GONE);
                        no_list.setVisibility(View.VISIBLE);
                    }
                    if (allGroupListingAdapter == null && groupModelsTotal != null && groupModelsTotal.size() > 0) {
                        allGroupListingAdapter = new GroupListingAdapter(groupModelsTotal, context, isHide, userId, showJoin);
                        recyclerView.setAdapter(allGroupListingAdapter);
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

                                            groupsOfUserAsyncTaskAll = new GroupsOfUserAsyncTaskAll(userId, authToken, url, isHide, context, showJoin);
                                            groupsOfUserAsyncTaskAll.execute();
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
                        allGroupListingAdapter.notifyDataSetChanged();
                        // recyclerView.scrollToPosition(groupListingAdapter.getItemCount());
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
