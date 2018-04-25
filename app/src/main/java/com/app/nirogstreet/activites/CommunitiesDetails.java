package com.app.nirogstreet.activites;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.nirogstreet.R;
import com.app.nirogstreet.circularprogressbar.CircularProgressBar;
import com.app.nirogstreet.fragments.About_Fragment;
import com.app.nirogstreet.uttil.AppUrl;
import com.app.nirogstreet.uttil.CustomPagerAdapter;
import com.app.nirogstreet.uttil.CustomPagerCommunitiesAdapter;
import com.app.nirogstreet.uttil.Event_For_Firebase;
import com.app.nirogstreet.uttil.ImageLoader;
import com.app.nirogstreet.uttil.LetterTileProvider;
import com.app.nirogstreet.uttil.NetworkUtill;
import com.app.nirogstreet.uttil.SesstionManager;
import com.app.nirogstreet.uttil.TypeFaceMethods;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONObject;

import java.util.ArrayList;
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
import de.hdodenhof.circleimageview.CircleImageView;

import static com.app.nirogstreet.R.id.tab;

/**
 * Created by Preeti on 02-11-2017.
 */

public class CommunitiesDetails extends AppCompatActivity {
    CustomPagerCommunitiesAdapter customPagerCommunitiesAdapter;
    TabLayout tabLayout;
    ViewPager viewPager;
    static TextView textTab;
    String groupId, refer_userId, refer_community_name;
    int user_invitation;
    int when_refer_community = -1;
    int user_fromLink;
    boolean openMain = false;
    public static ImageView moreImageView;
    static LetterTileProvider mLetterTileProvider;
    public static RoundedImageView circleImageView;
    CheckStatus_User checkStatus_user;
    ImageView backImageView;
    SesstionManager sesstionManager;
    int accept;
    public static CircleImageView proo;
    String authToken, userId;
    int statusData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.communication_detail);
        sesstionManager = new SesstionManager(CommunitiesDetails.this);
        authToken = sesstionManager.getUserDetails().get(SesstionManager.AUTH_TOKEN);
        userId = sesstionManager.getUserDetails().get(SesstionManager.USER_ID);
        Event_For_Firebase.getEventCount(CommunitiesDetails.this, "Feed_Communities_MyCommunities_Community_Feed_Screen_Visit");
        Event_For_Firebase.logAppEventsLoggerEvent(CommunitiesDetails.this,"Feed_Communities_MyCommunities_Community_Feed_Screen_Visit");
        proo = (CircleImageView) findViewById(R.id.proo);
        if (getIntent().hasExtra("openMain")) {
            openMain = getIntent().getBooleanExtra("openMain", false);
        }
        if (getIntent().hasExtra("groupId")) {
            groupId = getIntent().getStringExtra("groupId");
        }
        if (getIntent().hasExtra("refer_userId")) {
            refer_userId = getIntent().getStringExtra("refer_userId");
        }
        if (getIntent().hasExtra("refer_community_name")) {
            refer_community_name = getIntent().getStringExtra("refer_community_name");
        }

        if (getIntent().hasExtra("when_refer_community")) {
            when_refer_community = getIntent().getIntExtra("when_refer_community", 0);
        }
        if (getIntent().hasExtra("user_fromLink")) {
            user_fromLink = getIntent().getIntExtra("user_fromLink", 0);
        }
        textTab = (TextView) findViewById(R.id.textTab);
        backImageView = (ImageView) findViewById(R.id.back);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (openMain) {
                    Intent intent1 = new Intent(CommunitiesDetails.this, MainActivity.class);
                    startActivity(intent1);
                    finish();
                } else if (user_fromLink == 1) {
                    Intent intent1 = new Intent(CommunitiesDetails.this, MainActivity.class);
                    startActivity(intent1);
                    finish();
                } else {
                    finish();
                }

            }
        });
        mLetterTileProvider = new LetterTileProvider(CommunitiesDetails.this);

        circleImageView = (RoundedImageView) findViewById(R.id.pro);
        moreImageView = (ImageView) findViewById(R.id.more);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        viewPager.setOffscreenPageLimit(2);


        if (getIntent().hasExtra("user_invitation")) {
            user_invitation = getIntent().getIntExtra("user_invitation", 0);

            SharedPreferences sharedPref1 = getApplicationContext().getSharedPreferences("user_invitation", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor1 = sharedPref1.edit();
            editor1.putInt("user_invitation", user_invitation);
            editor1.commit();
        }
        if (user_fromLink == 1) {
            checkStatus_user = new CheckStatus_User(groupId, sesstionManager.getUserDetails().get(SesstionManager.USER_ID), authToken);
            checkStatus_user.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            checking();
        }

    }

    @Override
    public void onBackPressed() {
        if (openMain) {
            Intent intent1 = new Intent(CommunitiesDetails.this, MainActivity.class);
            startActivity(intent1);
            finish();
        } else if (when_refer_community == 1) {
            Intent intent1 = new Intent(CommunitiesDetails.this, MainActivity.class);
            startActivity(intent1);
            finish();
        }
        super.onBackPressed();
    }

    public static void setNameAndCoverPic(String name, String url) {
        textTab.setText(name);

    }
    // ImageLoader imageLoader=new ImageLoader(CommunitiesDetails.this);

    private static void setBanner(String url) {
        if (url != null && !url.contains("banner-default") && !url.contains("tempimages")) {
           /* Glide.with(CommunitiesDetails.this)
                    .load(url) // Uri of the picture
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .crossFade()
                    .override(100, 100)
                    .into(circleImageView);*/


            // imageLoader1.getInstance().displayImage(groupModel.getGroupBanner(),  holder.groupIconImageView, defaultOptions);
        } else {
            circleImageView.setImageBitmap(mLetterTileProvider.getLetterTile(url));

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public class CheckStatus_User extends AsyncTask<Void, Void, Void> {
        String authToken;
        JSONObject jo;
        String groupId, userId;

        int status1;
        HttpClient client;
        int addedType;
        private String responseBody;

        public CheckStatus_User(String groupId, String userId, String authToken) {
            this.groupId = groupId;
            this.authToken = authToken;
            this.userId = userId;
        }

        public void cancelAsyncTask() {
            if (client != null && !isCancelled()) {
                cancel(true);
                client = null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                String url = AppUrl.BaseUrl + "group/check-status";
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
                pairs.add(new BasicNameValuePair("groupID", groupId));
                httppost.setHeader("Authorization", "Basic " + authToken);

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
                    if (jo.has("response") && !jo.isNull("response")) {
                        JSONObject jsonObjectresponse = jo.getJSONObject("response");
                      /*  if (jsonObjectresponse.has("check_status") && !jsonObjectresponse.isNull("check_status")) {*/
                        statusData = jsonObjectresponse.getInt("check_status");
                        checking();
                        // }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public class AcceptDeclineJoinAsyncTask_FromLink extends AsyncTask<Void, Void, Void> {
        String authToken;
        JSONObject jo;
        String groupId, userId;

        int status1;
        HttpClient client;
        int addedType;
        private String responseBody;

        public AcceptDeclineJoinAsyncTask_FromLink(String groupId, String userId, String authToken, int status, int addedType) {
            this.groupId = groupId;
            this.status1 = status;
            this.authToken = authToken;
            this.addedType = addedType;
            this.userId = userId;
        }

        public void cancelAsyncTask() {
            if (client != null && !isCancelled()) {
                cancel(true);
                client = null;
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            try {
                if (jo != null) {


                    if (jo.has("response") && !jo.isNull("response")) {
                        JSONObject jsonObjectresponse = jo.getJSONObject("response");
                        if (jsonObjectresponse.has("message") && !jsonObjectresponse.isNull("message")) {
                            JSONObject jsonObject = jsonObjectresponse.getJSONObject("message");
                            accept = jsonObject.getInt("accept");
                            if (jsonObject.has("message") && !jsonObject.isNull("message")) {
                                Toast.makeText(CommunitiesDetails.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                                if (accept == 1) {
                                    if (NetworkUtill.isNetworkAvailable(CommunitiesDetails.this)) {
                                        customPagerCommunitiesAdapter = new CustomPagerCommunitiesAdapter(getSupportFragmentManager(), tabLayout, CommunitiesDetails.this, groupId, user_fromLink, refer_userId);
                                        // increase this limit if you have more tabs!
                                        //viewPager.setOffscreenPageLimit(2);

                                        viewPager.setAdapter(customPagerCommunitiesAdapter);
                                        tabLayout.setupWithViewPager(viewPager);
  /*  tabLayout.addTab(tabLayout.newTab().setText("COMMUNICATIONS"));
        tabLayout.addTab(tabLayout.newTab().setText("ABOUT"));*/
                                        LinearLayout tabLinearLayoutTimeline = (LinearLayout) LayoutInflater.from(CommunitiesDetails.this).inflate(R.layout.custum_comm, null);

                                        TextView tabOneTimeline = (TextView) tabLinearLayoutTimeline.findViewById(tab);
                                        tabOneTimeline.setText("FEED");
                                        tabOneTimeline.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                                        ImageView oneImgTimeline = (ImageView) tabLinearLayoutTimeline.findViewById(R.id.icon);
                                        oneImgTimeline.setImageResource(R.drawable.home);
                                        oneImgTimeline.setVisibility(View.GONE);
                                        tabLayout.getTabAt(0).setCustomView(tabLinearLayoutTimeline);
                                        // tabLayout.setTabTextColors(Color.parseColor("#727272"), Color.parseColor("#ffffff"));

                                        LinearLayout tabLinearLayout = (LinearLayout) LayoutInflater.from(CommunitiesDetails.this).inflate(R.layout.custum_comm, null);

                                        TextView tabOne = (TextView) tabLinearLayout.findViewById(tab);
                                        tabOne.setText("ABOUT");

                                        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                                        ImageView oneImg = (ImageView) tabLinearLayout.findViewById(R.id.icon);
                                        oneImg.setVisibility(View.GONE);
                                        tabLayout.getTabAt(1).setCustomView(tabLinearLayout);

                                        for (int i = 0; i < tabLayout.getTabCount(); i++) {
                                            TabLayout.Tab tab = tabLayout.getTabAt(i);
                                            if (tab != null)
                                                tab.setCustomView(R.layout.view_home_tab);
                                        }

                                        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                            @Override
                                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                                            }

                                            @Override
                                            public void onPageSelected(int position) {
                                                switch (position) {

                                                    case 0:
                       /* TextView tv = (TextView) tabLayout.getChildAt(position).findViewById(tab);
                        tv.setTextColor(Color.parseColor("#e82351"));
                        TextView tv1 = (TextView) tabLayout.getChildAt(1).findViewById(tab);
                        tv1.setTextColor(Color.parseColor("#b5b5b5"));
*/

                                                        break;
                                                    case 1:
                      /*  TextView tv2 = (TextView) tabLayout.getChildAt(position).findViewById(tab);
                        tv2.setTextColor(Color.parseColor("#e82351"));
                        TextView tv3 = (TextView) tabLayout.getChildAt(0).findViewById(tab);
                        tv3.setTextColor(Color.parseColor("#b5b5b5"));*/
                                                        break;
                                                }
                                            }

                                            @Override
                                            public void onPageScrollStateChanged(int state) {

                                            }
                                        });

                                    } else {
                                        NetworkUtill.showNoInternetDialog(CommunitiesDetails.this);
                                    }
                                } else if (accept == 0) {
                                    Intent intent1 = new Intent(CommunitiesDetails.this, MainActivity.class);
                                    startActivity(intent1);
                                    finish();
                                }
                            }
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
                String url = AppUrl.BaseUrl + "group/invite";
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
                pairs.add(new BasicNameValuePair("userID", refer_userId));
                pairs.add(new BasicNameValuePair("groupID", groupId));
                pairs.add(new BasicNameValuePair("invited_to", userId));
                pairs.add(new BasicNameValuePair("status", status1 + ""));
                pairs.add(new BasicNameValuePair("addedType", addedType + ""));
                httppost.setHeader("Authorization", "Basic " + authToken);

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
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }


    public void checking() {
        if (user_fromLink == 1) {
            if (statusData == 3 || statusData == 0 || statusData == 2 && user_fromLink == 1) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(CommunitiesDetails.this);
                if (refer_community_name != null && refer_community_name.length() > 0) {
                    builder.setTitle(refer_community_name);
                }
                builder.setMessage("Join the community");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (NetworkUtill.isNetworkAvailable(CommunitiesDetails.this)) {
                            AcceptDeclineJoinAsyncTask_FromLink acceptDeclineJoinAsyncTask = new AcceptDeclineJoinAsyncTask_FromLink(groupId, userId, authToken, 1, 5);
                            acceptDeclineJoinAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        } else {
                            NetworkUtill.showNoInternetDialog(CommunitiesDetails.this);
                        }
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // No location service, no Activity
                        if (NetworkUtill.isNetworkAvailable(CommunitiesDetails.this)) {
                            AcceptDeclineJoinAsyncTask_FromLink acceptDeclineJoinAsyncTask = new AcceptDeclineJoinAsyncTask_FromLink(groupId, userId, authToken, 2, 6);
                            acceptDeclineJoinAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        } else {
                            NetworkUtill.showNoInternetDialog(CommunitiesDetails.this);
                        }
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            } else {
                customPagerCommunitiesAdapter = new CustomPagerCommunitiesAdapter(getSupportFragmentManager(), tabLayout, this, groupId, user_fromLink, refer_userId);
                // increase this limit if you have more tabs!
                //viewPager.setOffscreenPageLimit(2);

                viewPager.setAdapter(customPagerCommunitiesAdapter);
                tabLayout.setupWithViewPager(viewPager);
  /*  tabLayout.addTab(tabLayout.newTab().setText("COMMUNICATIONS"));
        tabLayout.addTab(tabLayout.newTab().setText("ABOUT"));*/
                LinearLayout tabLinearLayoutTimeline = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custum_comm, null);

                TextView tabOneTimeline = (TextView) tabLinearLayoutTimeline.findViewById(tab);
                tabOneTimeline.setText("FEED");
                tabOneTimeline.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                ImageView oneImgTimeline = (ImageView) tabLinearLayoutTimeline.findViewById(R.id.icon);
                oneImgTimeline.setImageResource(R.drawable.home);
                oneImgTimeline.setVisibility(View.GONE);
                tabLayout.getTabAt(0).setCustomView(tabLinearLayoutTimeline);
                // tabLayout.setTabTextColors(Color.parseColor("#727272"), Color.parseColor("#ffffff"));

                LinearLayout tabLinearLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custum_comm, null);

                TextView tabOne = (TextView) tabLinearLayout.findViewById(tab);
                tabOne.setText("ABOUT");

                tabOne.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                ImageView oneImg = (ImageView) tabLinearLayout.findViewById(R.id.icon);
                oneImg.setVisibility(View.GONE);
                tabLayout.getTabAt(1).setCustomView(tabLinearLayout);

                for (int i = 0; i < tabLayout.getTabCount(); i++) {
                    TabLayout.Tab tab = tabLayout.getTabAt(i);
                    if (tab != null) tab.setCustomView(R.layout.view_home_tab);
                }

                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        switch (position) {

                            case 0:
                       /* TextView tv = (TextView) tabLayout.getChildAt(position).findViewById(tab);
                        tv.setTextColor(Color.parseColor("#e82351"));
                        TextView tv1 = (TextView) tabLayout.getChildAt(1).findViewById(tab);
                        tv1.setTextColor(Color.parseColor("#b5b5b5"));
*/

                                break;
                            case 1:
                      /*  TextView tv2 = (TextView) tabLayout.getChildAt(position).findViewById(tab);
                        tv2.setTextColor(Color.parseColor("#e82351"));
                        TextView tv3 = (TextView) tabLayout.getChildAt(0).findViewById(tab);
                        tv3.setTextColor(Color.parseColor("#b5b5b5"));*/
                                break;
                        }
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
            }
        } else {
            customPagerCommunitiesAdapter = new CustomPagerCommunitiesAdapter(getSupportFragmentManager(), tabLayout, this, groupId, user_fromLink, refer_userId);


            // increase this limit if you have more tabs!
            //viewPager.setOffscreenPageLimit(2);

            viewPager.setAdapter(customPagerCommunitiesAdapter);
            tabLayout.setupWithViewPager(viewPager);
  /*  tabLayout.addTab(tabLayout.newTab().setText("COMMUNICATIONS"));
        tabLayout.addTab(tabLayout.newTab().setText("ABOUT"));*/
            LinearLayout tabLinearLayoutTimeline = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custum_comm, null);

            TextView tabOneTimeline = (TextView) tabLinearLayoutTimeline.findViewById(tab);
            tabOneTimeline.setText("FEED");
            tabOneTimeline.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            ImageView oneImgTimeline = (ImageView) tabLinearLayoutTimeline.findViewById(R.id.icon);
            oneImgTimeline.setImageResource(R.drawable.home);
            oneImgTimeline.setVisibility(View.GONE);
            tabLayout.getTabAt(0).setCustomView(tabLinearLayoutTimeline);
            // tabLayout.setTabTextColors(Color.parseColor("#727272"), Color.parseColor("#ffffff"));

            LinearLayout tabLinearLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custum_comm, null);

            TextView tabOne = (TextView) tabLinearLayout.findViewById(tab);
            tabOne.setText("ABOUT");

            tabOne.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            ImageView oneImg = (ImageView) tabLinearLayout.findViewById(R.id.icon);
            oneImg.setVisibility(View.GONE);
            tabLayout.getTabAt(1).setCustomView(tabLinearLayout);

            for (int i = 0; i < tabLayout.getTabCount(); i++) {
                TabLayout.Tab tab = tabLayout.getTabAt(i);
                if (tab != null) tab.setCustomView(R.layout.view_home_tab);
            }

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    switch (position) {

                        case 0:
                       /* TextView tv = (TextView) tabLayout.getChildAt(position).findViewById(tab);
                        tv.setTextColor(Color.parseColor("#e82351"));
                        TextView tv1 = (TextView) tabLayout.getChildAt(1).findViewById(tab);
                        tv1.setTextColor(Color.parseColor("#b5b5b5"));
*/

                            break;
                        case 1:
                      /*  TextView tv2 = (TextView) tabLayout.getChildAt(position).findViewById(tab);
                        tv2.setTextColor(Color.parseColor("#e82351"));
                        TextView tv3 = (TextView) tabLayout.getChildAt(0).findViewById(tab);
                        tv3.setTextColor(Color.parseColor("#b5b5b5"));*/
                            break;
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
    }
}
