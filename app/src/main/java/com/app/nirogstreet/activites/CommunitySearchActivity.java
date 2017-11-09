package com.app.nirogstreet.activites;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.nirogstreet.R;
import com.app.nirogstreet.adapter.GroupListingAdapter;
import com.app.nirogstreet.circularprogressbar.CircularProgressBar;
import com.app.nirogstreet.model.GroupModel;
import com.app.nirogstreet.parser.Group_Listing_Parser;
import com.app.nirogstreet.uttil.AppUrl;
import com.app.nirogstreet.uttil.NetworkUtill;
import com.app.nirogstreet.uttil.SesstionManager;

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
 * Created by Preeti on 09-11-2017.
 */

public class CommunitySearchActivity  extends AppCompatActivity {
    CircularProgressBar circularProgressBar;
    SesstionManager sessionManager;
    SearchEventAsync eventListingAsyncTask;
    EditText searchET;
    private String authToken, userId;
    ImageView imageViewSearch;
    ImageView backImageView;
    private String text = "";
    private String query = "";
    private RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    private boolean isLoading = false;
    boolean isEvent = false;
    int totalPageCount;
    int page = 1;
    private ArrayList<GroupModel> groupModelsTotal = new ArrayList<>();
    private GroupListingAdapter groupListingAdapter = null;



   /* */

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchgroup);
        sessionManager = new SesstionManager(CommunitySearchActivity.this);
        searchET = (EditText) findViewById(R.id.searchET);
        backImageView=(ImageView)findViewById(R.id.back) ;
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        linearLayoutManager = new LinearLayoutManager(CommunitySearchActivity.this);

        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        circularProgressBar = (CircularProgressBar) findViewById(R.id.scroll);
        if (getIntent().hasExtra("isEvent")) {
            isEvent = getIntent().getBooleanExtra("isEvent", false);
        }
        HashMap<String, String> userDetails = sessionManager.getUserDetails();
        authToken = userDetails.get(SesstionManager.AUTH_TOKEN);
        userId = userDetails.get(SesstionManager.USER_ID);
        imageViewSearch = (ImageView) findViewById(R.id.searchButton);
        imageViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                page = 1;

                    groupModelsTotal = new ArrayList<GroupModel>();
                    groupListingAdapter = null;

                if (searchET.getText().toString().length() != 0) {
                    if (NetworkUtill.isNetworkAvailable(CommunitySearchActivity.this)) {
                        eventListingAsyncTask = new SearchEventAsync(searchET.getText().toString(), true);
                        eventListingAsyncTask.execute();
                    } else {
                        NetworkUtill.showNoInternetDialog(CommunitySearchActivity.this);
                    }
                } else {
                    Toast.makeText(CommunitySearchActivity.this, "enter name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        searchET.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                text = s.toString();
                page=1;

                    groupListingAdapter=null;
                    groupModelsTotal=new ArrayList<GroupModel>();

                query = text;
        /*if (searchModels != null) {
            mRecyclerView.setAdapter(null);
        }*/
                if (eventListingAsyncTask != null)
                    eventListingAsyncTask.cancel(true);
                // setVisibilty(0);
                if (NetworkUtill.isNetworkAvailable(CommunitySearchActivity.this)) {
                    if (searchET.getText().toString().length() == 0) {
                        eventListingAsyncTask = new SearchEventAsync("", true);
                        eventListingAsyncTask.execute();
                    } else {
                        eventListingAsyncTask = new SearchEventAsync(searchET.getText().toString(), true);
                        eventListingAsyncTask.execute();
                    }
                } else {
                    NetworkUtill.showNoInternetDialog(CommunitySearchActivity.this);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });

        if (NetworkUtill.isNetworkAvailable(CommunitySearchActivity.this)) {
            if (searchET.getText().toString().length() == 0) {
                eventListingAsyncTask = new SearchEventAsync("", true);
                eventListingAsyncTask.execute();
            }
        } else {
            NetworkUtill.showNoInternetDialog(CommunitySearchActivity.this);
        }
    }

    public class SearchEventAsync extends AsyncTask<Void, Void, Void> {
        String responseBody;
        String email, password;
        CircularProgressBar bar;
        //PlayServiceHelper regId;
        boolean isHide;
        String strTobeSearch;
        JSONObject jo;
        HttpClient client;
        String searchvalue;

        public SearchEventAsync(String str, boolean isHide) {
            strTobeSearch = str;
            this.isHide = isHide;

        }

        public void cancelAsyncTask() {
            if (client != null && !isCancelled()) {
                cancel(true);
                client = null;
            }
        }

        @Override
        protected void onPreExecute() {
            circularProgressBar.setVisibility(View.VISIBLE);
            searchvalue = searchET.getText().toString();
            //  bar = (ProgressBar) findViewById(R.id.progressBar);
            //   bar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                String url;

                    url = AppUrl.BaseUrl + "group/search";


                SSLSocketFactory sf = new SSLSocketFactory(
                        SSLContext.getDefault(),
                        SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                Scheme sch = new Scheme("https", 443, sf);
                client = new DefaultHttpClient();

                client.getConnectionManager().getSchemeRegistry().register(sch);
                HttpPost httppost = new HttpPost(url);
                HttpResponse response;
                String credentials = email + ":" + password;

                List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                pairs.add(new BasicNameValuePair(AppUrl.APP_ID_PARAM, AppUrl.APP_ID_VALUE_POST));

                pairs.add(new BasicNameValuePair("searchKey", strTobeSearch));
                pairs.add(new BasicNameValuePair("userID", userId));
                pairs.add(new BasicNameValuePair("pageNo", page + ""));
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
            recyclerView.setVisibility(View.VISIBLE);

                ArrayList<GroupModel> groupModels = new ArrayList<>();
                groupModels = Group_Listing_Parser.groupListingParser(jo);
                groupModelsTotal.addAll(groupModels);
                super.onPostExecute(aVoid);
                try {
                    if (jo != null) {
                        if (jo.has("response") && !jo.isNull("response")) {
                            JSONObject jsonObjectResponce = jo.getJSONObject("response");
                            if (jsonObjectResponce.has("totalpage") && !jsonObjectResponce.isNull("totalpage")) {
                                totalPageCount = jsonObjectResponce.getInt("totalpage");
                            }
                        }
                        isLoading = false;


                        if (groupListingAdapter == null && groupModelsTotal != null && groupModelsTotal.size() > 0) {
                            groupListingAdapter = new GroupListingAdapter(groupModelsTotal, CommunitySearchActivity.this, isHide, userId);
                            recyclerView.setAdapter(groupListingAdapter);
                            recyclerView.setVisibility(View.VISIBLE);
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

                                                eventListingAsyncTask = new SearchEventAsync(strTobeSearch, false);
                                                eventListingAsyncTask.execute();
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
    }


