package com.app.nirogstreet.activites;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.app.nirogstreet.R;
import com.app.nirogstreet.adapter.LikesAdapter;
import com.app.nirogstreet.circularprogressbar.CircularProgressBar;
import com.app.nirogstreet.model.LikesModel;
import com.app.nirogstreet.parser.LikeParser;
import com.app.nirogstreet.uttil.AppUrl;
import com.app.nirogstreet.uttil.CustomPagerAdapter;
import com.app.nirogstreet.uttil.NetworkUtill;
import com.app.nirogstreet.uttil.SesstionManager;
import com.app.nirogstreet.uttil.TypeFaceMethods;
import com.app.nirogstreet.uttil.Utils2;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.cast.framework.SessionManager;
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
import io.fabric.sdk.android.Fabric;
import me.leolin.shortcutbadger.ShortcutBadgeException;
import me.leolin.shortcutbadger.ShortcutBadger;


public class MainActivity extends AppCompatActivity {
    TabLayout tabLayout;
    int count = 1;
    public static ViewPager viewPager;
    Menu menu1;

    FrameLayout notiframe;
    TextView tv;
    RelativeLayout notifCount;

    int totalUnReadCount = 0;

    LinearLayout linearLayoutHeader;
    boolean doubleBackToExitPressedOnce = false;
    NotificationAsyncTask notificationAsyncTask;

    FrameLayout frameLayoutview_alert_red_circle;
    TextView view_alert_count_textviewTextView;

    ImageView searchImageView, notifictaionImageView;
    public TextView textViewTab, createTextView;

    private CustomPagerAdapter customPagerAdapter;
    ImageView searchgroupImageView;
    ImageView logout;
    SesstionManager sesstionManager;
    MenuItem item1, item;

    LogoutAsyncTask logoutAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        frameLayoutview_alert_red_circle = (FrameLayout) findViewById(R.id.view_alert_red_circle);
        frameLayoutview_alert_red_circle.setVisibility(View.GONE);
        // frameLayoutview_alert_red_circle.setVisibility(View.VISIBLE);
        view_alert_count_textviewTextView = (TextView) findViewById(R.id.view_alert_count_textview);
        // view_alert_count_textviewTextView.setText("655");
        frameLayoutview_alert_red_circle = (FrameLayout) findViewById(R.id.view_alert_red_circle);
        frameLayoutview_alert_red_circle.setVisibility(View.GONE);
        textViewTab = (TextView) findViewById(R.id.textTab);
        TypeFaceMethods.setRegularTypeBoldFaceTextView(textViewTab,MainActivity.this);
        logout = (ImageView) findViewById(R.id.logout);
        sesstionManager = new SesstionManager(MainActivity.this);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDialog();
            }
        });
        searchgroupImageView = (ImageView) findViewById(R.id.searchgroup);
        searchgroupImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CommunitySearchActivity.class);
                startActivity(intent);
            }
        });
        linearLayoutHeader = (LinearLayout) findViewById(R.id.actionbar);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        createTextView = (TextView) findViewById(R.id.create);
        TypeFaceMethods.setRegularTypeFaceForTextView(createTextView, MainActivity.this);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        viewPager.setOffscreenPageLimit(3);

        customPagerAdapter = new CustomPagerAdapter(getSupportFragmentManager(), tabLayout, this);
        // increase this limit if you have more tabs!
        //viewPager.setOffscreenPageLimit(2);

        viewPager.setAdapter(customPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                switch (position) {
                    case 0:
                        searchgroupImageView.setVisibility(View.GONE);
                        searchImageView.setVisibility(View.VISIBLE);
                        createTextView.setVisibility(View.GONE);
                        notiframe.setVisibility(View.VISIBLE);
                        logout.setVisibility(View.GONE);
                        setTabText("Home");
                        break;
                    case 1:
                        createTextView.setVisibility(View.GONE);
                        notiframe.setVisibility(View.GONE);
                        searchgroupImageView.setVisibility(View.GONE);
                        logout.setVisibility(View.GONE);
                        searchImageView.setVisibility(View.GONE);
                        setTabText("Knowledge Centre");
                        break;
                    case 2:
                        searchImageView.setVisibility(View.GONE);
                        createTextView.setVisibility(View.VISIBLE);
                        notiframe.setVisibility(View.GONE);
                        logout.setVisibility(View.GONE);
                        searchgroupImageView.setVisibility(View.VISIBLE);

                        setTabText("Community");
                      /*  createTextView.setVisibility(View.GONE);
                        notiframe.setVisibility(View.GONE);
                        searchgroupImageView.setVisibility(View.GONE);
                        logout.setVisibility(View.GONE);
                        searchImageView.setVisibility(View.GONE);
                        setTabText("You");*/
                        break;
                    case 3:
                        createTextView.setVisibility(View.GONE);
                        notiframe.setVisibility(View.GONE);
                        searchgroupImageView.setVisibility(View.GONE);
                        logout.setVisibility(View.GONE);
                        searchImageView.setVisibility(View.GONE);
                        setTabText("You");
                        break;

                }
            }

            @Override
            public void onPageSelected(int position) {

            }


            @Override
            public void onPageScrollStateChanged(int state) {


            }

        });
        searchImageView = (ImageView) findViewById(R.id.search);
        createTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateCommunity.class);
                startActivity(intent);

            }
        });
        LinearLayout tabLinearLayoutTimeline = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custum, null);

        TextView tabOneTimeline = (TextView) tabLinearLayoutTimeline.findViewById(R.id.tab);
        tabOneTimeline.setText("HOME");
        TypeFaceMethods.setRegularTypeBoldFaceTextView(tabOneTimeline, MainActivity.this);

        tabOneTimeline.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        ImageView oneImgTimeline = (ImageView) tabLinearLayoutTimeline.findViewById(R.id.icon);
        oneImgTimeline.setImageResource(R.drawable.home_);
        tabLayout.getTabAt(0).setCustomView(tabLinearLayoutTimeline);
        LinearLayout tabLinearLayout1 = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custum, null);

        TextView tabOneknwledge = (TextView) tabLinearLayout1.findViewById(R.id.tab);
        tabOneknwledge.setText("KNOWLEDGE CENTRE");
        TypeFaceMethods.setRegularTypeBoldFaceTextView(tabOneknwledge, MainActivity.this);

        tabOneknwledge.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        ImageView oneImglnw = (ImageView) tabLinearLayout1.findViewById(R.id.icon);
        oneImglnw.setImageResource(R.drawable.knwoledge);
        tabLayout.getTabAt(1).setCustomView(tabLinearLayout1);

        LinearLayout tabLinearLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custum, null);

        TextView tabOne = (TextView) tabLinearLayout.findViewById(R.id.tab);
        tabOne.setText("COMMUNITIES");
        TypeFaceMethods.setRegularTypeBoldFaceTextView(tabOne, MainActivity.this);

        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        ImageView oneImg = (ImageView) tabLinearLayout.findViewById(R.id.icon);
        oneImg.setImageResource(R.drawable.comm);
        tabLayout.getTabAt(2).setCustomView(tabLinearLayout);

        LinearLayout tabtwo = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custum, null);
        TextView tabtwoText = (TextView) tabtwo.findViewById(R.id.tab);
        tabtwoText.setText("YOU");
        TypeFaceMethods.setRegularTypeBoldFaceTextView(tabtwoText, MainActivity.this);

        tabtwoText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        ImageView TwoImg = (ImageView) tabtwo.findViewById(R.id.icon);
        TwoImg.setImageResource(R.drawable.youlay);
        tabLayout.getTabAt(3).setCustomView(tabtwo);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) tab.setCustomView(R.layout.view_home_tab);
        }
        notifictaionImageView = (ImageView) findViewById(R.id.notifictaion);
        notifictaionImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NotificationListing.class);
                startActivity(intent);
            }
        });

        notiframe = (FrameLayout) findViewById(R.id.notiframe);
        notiframe.setVisibility(View.VISIBLE);
        notiframe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frameLayoutview_alert_red_circle.setVisibility(View.GONE);
                Intent intent = new Intent(MainActivity.this, NotificationListing.class);
                startActivity(intent);
            }
        });
        searchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivityForResult(intent, 4);
            }
        });
    }

    public void setTabsVisibility(boolean displayTabs) {
        if (displayTabs) {
            linearLayoutHeader.setVisibility(View.VISIBLE);
            tabLayout.setVisibility(View.VISIBLE);

        } else {
            linearLayoutHeader.setVisibility(View.GONE);

            tabLayout.setVisibility(View.GONE);
        }
    }

    public void setTabText(String tabText) {
        if (textViewTab != null)

            textViewTab.setText(tabText);
        textViewTab.setVisibility(View.VISIBLE);
    }

    public static int selectedFragment() {
        return viewPager.getCurrentItem();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_notification, menu);
        menu1 = menu;
        item1 = menu.findItem(R.id.noti);
        HashMap<String, String> userDetails = sesstionManager.getUserDetails();
        String userId = userDetails.get(SesstionManager.USER_ID);

        MenuItemCompat.setActionView(item1, R.layout.actionbar_badge);
        item = menu.findItem(R.id.action_settings);
        try {
            LayerDrawable icon = (LayerDrawable) item1.getIcon();
            Utils2.setBadgeCount(this, icon, 2000);

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            notifCount = (RelativeLayout) MenuItemCompat.getActionView(item1);
            tv = (TextView) notifCount.findViewById(R.id.actionbar_notifcation_textview);
            tv.setText("12");
            if (NetworkUtill.isNetworkAvailable(MainActivity.this)) {
                notificationAsyncTask = new NotificationAsyncTask(userId, "", "");
                notificationAsyncTask.execute();

            } else {
                NetworkUtill.showNoInternetDialog(MainActivity.this);
            }
        } catch (Exception e)

        {
            e.printStackTrace();
        }
        if (totalUnReadCount == 0) {
            tv.setVisibility(View.GONE);
        }
        // Update LayerDrawable's BadgeDrawable
        notifCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelNotificationOnLogout();
                totalUnReadCount = 0;
                tv.setText("");
                tv.setVisibility(View.GONE);
                Intent intent = new Intent(MainActivity.this, NotificationListing.class);
                startActivity(intent);
            }
        });
        // if (isMenuVisible) {
        item.setVisible(true);//
        item1.setVisible(true);
       /* } else {
            item.setVisible(false);//
            item1
                    .setVisible(false);
        }*/
        return super.onCreateOptionsMenu(menu);
    }


    public void setDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Are you sure you want to Logout.");// Add the buttons
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                if (NetworkUtill.isNetworkAvailable(MainActivity.this)) {
                    logoutAsyncTask = new LogoutAsyncTask("");
                    logoutAsyncTask.execute();
                } else {
                    NetworkUtill.showNoInternetDialog(MainActivity.this);
                    NetworkUtill.showNoInternetDialog(MainActivity.this);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                dialog.cancel();
            }
        });
        builder.show();
// Set other dialog properties

// Create the AlertDialog
        AlertDialog dialog = builder.create();
    }


    public class NotificationAsyncTask extends AsyncTask<Void, Void, Void> {
        String responseBody;
        String email, password;
        CircularProgressBar bar;
        String lang, authToken;
        String userId;

        //PlayServiceHelper regId;
        public NotificationAsyncTask(String userId, String lang, String authToken) {
            this.userId = userId;
            this.lang = lang;
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
                response = client.execute(httppost);

                responseBody = EntityUtils
                        .toString(response.getEntity());
                jo = new JSONObject(responseBody);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {

                if (jo != null) {
                    if (jo.has("notificationdetail") && !jo.isNull("notificationdetail")) {
                        JSONObject jsonObject = jo.getJSONObject("notificationdetail");
                        if (jsonObject.has("notification") && !jsonObject.isNull("notification")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("notification");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                String profile_pic = "", message = "", link_url = "", name = "", slug = "", time = "";
                                int msg;
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                if (jsonObject1.has("totalUnreadmsg") && !jsonObject1.isNull("totalUnreadmsg")) {
                                    totalUnReadCount = jsonObject1.getInt("totalUnreadmsg");
                                    if (totalUnReadCount > 0) {

                                        frameLayoutview_alert_red_circle.setVisibility(View.VISIBLE);
                                        view_alert_count_textviewTextView.setText(totalUnReadCount + "");
                                        try {
                                            ShortcutBadger.setBadge(getApplicationContext(), totalUnReadCount); //for 1.1.4+
                                            ShortcutBadger.with(getApplicationContext()).count(totalUnReadCount); //for 1.1.3

                                        } catch (ShortcutBadgeException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        frameLayoutview_alert_red_circle.setVisibility(View.GONE);
                                        try {
                                            ShortcutBadger.with(getApplicationContext()).remove();  //for 1.1.3

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    break;
                                }

                            }
                            if (jsonArray.length() == 0) {
                                frameLayoutview_alert_red_circle.setVisibility(View.GONE);

                            }
                        }
                    }
                    if (totalUnReadCount != 0) {
                        tv.setVisibility(View.VISIBLE);
                        tv.setText(totalUnReadCount + "");
                    } else {
                        tv.setVisibility(View.GONE);
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            //  bar.setVisibility(View.GONE);

        }

    }

    public void cancelNotificationOnLogout() {
        try {
            ShortcutBadger.with(getApplicationContext()).remove();  //for 1.1.3

        } catch (Exception e) {
            e.printStackTrace();
        }
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.cancelAll();
    }

    public class LogoutAsyncTask extends AsyncTask<Void, Void, Void> {
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

        public LogoutAsyncTask(String feedId) {
            this.feedId = feedId;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                if (jo != null) {
                    if (jo.has("status") && !jo.isNull("status")) {
                        boolean status = jo.getBoolean("status");
                        if (status) {
                            sesstionManager.logoutUser();

                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                String url = AppUrl.AppBaseUrl + "user/logout";
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
                httppost.setHeader("authorization", "Nirogstreet " + sesstionManager.getUserDetails().get(SesstionManager.AUTH_TOKEN));

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
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        count = 1;
        HashMap<String, String> userDetails = sesstionManager.getUserDetails();
        String userId = userDetails.get(SesstionManager.USER_ID);
        if (NetworkUtill.isNetworkAvailable(MainActivity.this)) {
            notificationAsyncTask = new NotificationAsyncTask(
                    userId, "", "");
            notificationAsyncTask.execute();

        } else {
            NetworkUtill.showNoInternetDialog(MainActivity.this);
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click back again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);

    }

}
