package com.app.nirogstreet.activites;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.nirogstreet.R;
import com.app.nirogstreet.adapter.GroupNotificationAdapter;
import com.app.nirogstreet.adapter.InvitationNotificationAdapter;
import com.app.nirogstreet.circularprogressbar.CircularProgressBar;
import com.app.nirogstreet.model.GroupNotificationModel;
import com.app.nirogstreet.parser.GroupNotificationParser;
import com.app.nirogstreet.uttil.AppUrl;
import com.app.nirogstreet.uttil.NetworkUtill;
import com.app.nirogstreet.uttil.SesstionManager;
import com.google.firebase.iid.FirebaseInstanceId;

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
 * Created by Preeti on 03-01-2018.
 */
public class InviteNotificationListing extends Activity {
    SesstionManager sessionManager;
    ArrayList<GroupNotificationModel> notificationModels, notificationModelsTotal;
    RecyclerView listView;
    LinearLayoutManager llm;

    CircularProgressBar circularProgressBar;
    TextView searchButtonTextView;
    InvitationNotificationAdapter adapter;
    NotificationAsyncTask notificationAsyncTask;
    private boolean isLoading = false;


    private ImageView backImageView;
    String userId;
    boolean openMain = false;
    LinearLayout no_notifications;
    RecyclerView rv;
    private int totalPageCount;
    int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.noti_list);
        no_notifications = (LinearLayout) findViewById(R.id.no_list);
        searchButtonTextView = (TextView) findViewById(R.id.searchButton);
        if (getIntent().hasExtra("openMain")) {
            openMain = getIntent().getBooleanExtra("openMain", false);
        }
        searchButtonTextView.setText("Notification");
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.statusbarcolor));
        }
        notificationModelsTotal = new ArrayList<>();
        rv = (RecyclerView) findViewById(R.id.lv);
        rv.setHasFixedSize(true);
        llm = new LinearLayoutManager(InviteNotificationListing.this);
        rv.setLayoutManager(llm);
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        backImageView = (ImageView) findViewById(R.id.back);
        circularProgressBar = (CircularProgressBar) findViewById(R.id.scroll);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (openMain) {
                    Intent intent1 = new Intent(InviteNotificationListing.this, MainActivity.class);
                    startActivity(intent1);
                    finish();
                }
                finish();
            }
        });
        sessionManager = new SesstionManager(InviteNotificationListing.this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        String authToken = user.get(SesstionManager.AUTH_TOKEN);
        userId = user.get(SesstionManager.USER_ID);
        if (NetworkUtill.isNetworkAvailable(InviteNotificationListing.this)) {
            notificationAsyncTask = new NotificationAsyncTask(userId, authToken);
            notificationAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            NetworkUtill.showNoInternetDialog(InviteNotificationListing.this);
        }
    }

    @Override
    public void onBackPressed() {
        if (openMain) {
            Intent intent1 = new Intent(InviteNotificationListing.this, MainActivity.class);
            startActivity(intent1);
            finish();
        }
        super.onBackPressed();
    }

    public class NotificationAsyncTask extends AsyncTask<Void, Void, Void> {
        String responseBody;
        String email, password;
        CircularProgressBar bar;
        String authToken;
        String userId;

        public NotificationAsyncTask(String userId, String authToken) {
            this.userId = userId;
            this.authToken = authToken;
        }

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
            circularProgressBar.setVisibility(View.VISIBLE);
            //  bar = (ProgressBar) findViewById(R.id.progressBar);
            //   bar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                String url = AppUrl.BaseUrl + "group/invited-community";
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
                String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                pairs.add(new BasicNameValuePair("userID", sessionManager.getUserDetails().get(SesstionManager.USER_ID)));

                httppost.setEntity(new UrlEncodedFormEntity(pairs));
                httppost.setHeader("Authorization", "Basic " + authToken);

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
                notificationModels = new ArrayList<>();

                if (jo != null) {
                    if (jo.has("response") && !jo.isNull("response")) {
                        JSONObject jsonObject = jo.getJSONObject("response");
                        if (jsonObject.has("totalpage") && !jsonObject.isNull("totalpage")) {
                            totalPageCount = jsonObject.getInt("totalpage");
                        }
                    }
                    notificationModels = GroupNotificationParser.groupNotificationModels(jo);
                    notificationModelsTotal.addAll(notificationModels);
                }
                isLoading = false;

                if (notificationModelsTotal != null && notificationModelsTotal.size() > 0) {
                    adapter = new InvitationNotificationAdapter(InviteNotificationListing.this, notificationModelsTotal, authToken);
                    rv.setAdapter(adapter);
                    rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            int totalItemCount = llm.getItemCount();
                            int lastVisibleItem = llm.findLastVisibleItemPosition();

                            if (!isLoading && (totalItemCount - 1) <= (lastVisibleItem)) {
                                try {
                                    String has_more = "";
                                    if (page < totalPageCount) {
                                        page++;

                                        notificationAsyncTask = new NotificationAsyncTask(userId, authToken);
                                        notificationAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                isLoading = true;
                            }
                        }
                    });

                } else {
                    if (adapter == null)
                        no_notifications.setVisibility(View.VISIBLE);
                    else
                        adapter.notifyDataSetChanged();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

    }


    @Override
    protected void onPause() {
        super.onPause();
        if (notificationAsyncTask != null && !notificationAsyncTask.isCancelled()) {
            notificationAsyncTask.cancelAsyncTask();
        }
    }

}
