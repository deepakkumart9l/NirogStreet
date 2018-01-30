package com.app.nirogstreet.activites;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.nirogstreet.R;
import com.app.nirogstreet.adapter.Notification_Adapter;
import com.app.nirogstreet.circularprogressbar.CircularProgressBar;
import com.app.nirogstreet.model.NotificationModel;
import com.app.nirogstreet.uttil.AppUrl;
import com.app.nirogstreet.uttil.ApplicationSingleton;
import com.app.nirogstreet.uttil.NetworkUtill;
import com.app.nirogstreet.uttil.SesstionManager;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
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
 * Created by Preeti on 28-10-2017.
 */
public class NotificationListing extends AppCompatActivity {
    SesstionManager sessionManager;
    ArrayList<NotificationModel> notificationModels;
    RecyclerView listView;
    CircularProgressBar circularProgressBar;
    TextView searchButtonTextView;
    NotificationAsyncTask notificationAsyncTask;
    private ImageView backImageView;
    String userId;
    LinearLayout no_notifications;
    RecyclerView rv;
    TextView info, info1;
    CardView first, second;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.noti_list_one);
        no_notifications = (LinearLayout) findViewById(R.id.no_list);
        first = (CardView) findViewById(R.id.first);
        second = (CardView) findViewById(R.id.second);
        searchButtonTextView = (TextView) findViewById(R.id.searchButton);
        searchButtonTextView.setText("Notification");
        info = (TextView) findViewById(R.id.info);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.statusbarcolor));
        }
        rv = (RecyclerView) findViewById(R.id.lv);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(NotificationListing.this);
        rv.setLayoutManager(llm);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        info1 = (TextView) findViewById(R.id.info1);
        backImageView = (ImageView) findViewById(R.id.back);
        circularProgressBar = (CircularProgressBar) findViewById(R.id.scroll);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        sessionManager = new SesstionManager(NotificationListing.this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        String authToken = user.get(SesstionManager.AUTH_TOKEN);
        userId = user.get(SesstionManager.USER_ID);
        if (NetworkUtill.isNetworkAvailable(NotificationListing.this)) {
            notificationAsyncTask = new NotificationAsyncTask(userId, authToken);
            notificationAsyncTask.execute();

        } else {
            NetworkUtill.showNoInternetDialog(NotificationListing.this);
        }

    }

    public class NotificationAsyncTask extends AsyncTask<Void, Void, Void> {
        String responseBody;
        String email, password;
        CircularProgressBar bar;
        String authToken;
        String userId;

        //PlayServiceHelper regId;
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

                String url = AppUrl.BaseUrl + "feed/notification";
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
                pairs.add(new BasicNameValuePair("userID", userId));

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
                    int groupRquest = -1, inviteRequest = -1;
                    if (jo.has("notificationdetail") && !jo.isNull("notificationdetail")) {
                        JSONObject jsonObject = jo.getJSONObject("notificationdetail");
                        if (jsonObject.has("groupRequest") && !jsonObject.isNull("groupRequest")) {
                            groupRquest = jsonObject.getInt("groupRequest");
                            first.setVisibility(View.VISIBLE);
                            info.setText(groupRquest + "");
                            ApplicationSingleton.setGroupRequestCount(groupRquest);
                            first.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(NotificationListing.this, GroupNotificationListing.class);
                                    startActivity(intent);
                                }
                            });
                        }

                        if (jsonObject.has("inviteCount") && !jsonObject.isNull("inviteCount")) {
                            inviteRequest = jsonObject.getInt("inviteCount");
                            second.setVisibility(View.VISIBLE);
                            info1.setText(inviteRequest + "");
                            ApplicationSingleton.setInvitationRequestCount(inviteRequest);
                            second.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(NotificationListing.this, InviteNotificationListing.class);
                                    startActivity(intent);
                                }
                            });
                        }

                        if (jsonObject.has("notification") && !jsonObject.isNull("notification")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("notification");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                String id = "", profile_pic = "", message = "", link_url = "", name = "", slug = "", time = "", post_id = "", event_id = "", group_id = "", courseID="",forum_id = "";
                                int unread = 0;
                                String notificationType="";
                                String appointment_id = "";
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                if (jsonObject1.has("profile_pic") && !jsonObject1.isNull("profile_pic")) {
                                    profile_pic = jsonObject1.getString("profile_pic");
                                }
                                if (jsonObject1.has("id") && !jsonObject1.isNull("id")) {
                                    id = jsonObject1.getString("id");
                                }
                                if (jsonObject1.has("message") && !jsonObject1.isNull("message")) {
                                    message = jsonObject1.getString("message");
                                }
                                if (jsonObject1.has("link_url") && !jsonObject1.isNull("link_url")) {
                                    link_url = jsonObject1.getString("link_url");
                                }
                                if (jsonObject1.has("name") && !jsonObject1.isNull("name")) {
                                    name = jsonObject1.getString("name");
                                }
                                if (jsonObject1.has("slug") && !jsonObject1.isNull("slug")) {
                                    slug = jsonObject1.getString("slug");
                                }
                                if (jsonObject1.has("time") && !jsonObject1.isNull("time")) {
                                    time = jsonObject1.getString("time");
                                }
                                if (jsonObject1.has("post_id") && !jsonObject1.isNull("post_id")) {
                                    post_id = jsonObject1.getString("post_id");
                                }
                                if (jsonObject1.has("appointment_id") && !jsonObject1.isNull("appointment_id")) {
                                    appointment_id = jsonObject1.getString("appointment_id");
                                }
                                if (jsonObject1.has("event_id") && !jsonObject1.isNull("event_id")) {
                                    event_id = jsonObject1.getString("event_id");
                                }
                                if (jsonObject1.has("community_id") && !jsonObject1.isNull("community_id")) {
                                    group_id = jsonObject1.getString("community_id");
                                }
                                if(jsonObject1.has("course_id")&&!jsonObject1.isNull("course_id"))
                                {
                                    courseID=jsonObject1.getString("course_id");
                                }
                                if (jsonObject1.has("forum_id") && !jsonObject1.isNull("forum_id")) {
                                    forum_id = jsonObject1.getString("forum_id");
                                }
                                if (jsonObject1.has("unread") && !jsonObject1.isNull("unread")) {
                                    unread = jsonObject1.getInt("unread");
                                }
                                if (jsonObject1.has("notification_type") && !jsonObject1.isNull("notification_type")) {
                                    notificationType = jsonObject1.getString("notification_type");
                                }
                                notificationModels.add(new NotificationModel(profile_pic, message, link_url, name, slug, time, post_id, group_id, event_id, forum_id, id, unread, appointment_id,courseID,notificationType));
                            }
                        } else {
                            no_notifications.setVisibility(View.VISIBLE);
                        }
                    }
                }

                if (notificationModels != null && notificationModels.size() > 0) {
                    final Notification_Adapter adapter = new Notification_Adapter(NotificationListing.this, notificationModels, authToken);
                    rv.setAdapter(adapter);

                } else {
                    no_notifications.setVisibility(View.VISIBLE);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            //  bar.setVisibility(View.GONE);

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ApplicationSingleton.getGroupRequestCount() != -1 && ApplicationSingleton.getGroupRequestCount() > 0) {
            info.setText(ApplicationSingleton.getGroupRequestCount() + "");
        } else {
            first.setVisibility(View.GONE);
        }
        if (ApplicationSingleton.getInvitationRequestCount() != -1 && ApplicationSingleton.getInvitationRequestCount() > 0) {
            info1.setText(ApplicationSingleton.getInvitationRequestCount() + "");
        } else {
            second.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (notificationAsyncTask != null && !notificationAsyncTask.isCancelled()) {
            notificationAsyncTask.cancelAsyncTask();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ApplicationSingleton.setGroupRequestCount(-1);
        ApplicationSingleton.setInvitationRequestCount(-1);
    }
}