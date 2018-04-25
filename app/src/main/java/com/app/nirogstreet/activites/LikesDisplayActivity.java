package com.app.nirogstreet.activites;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.app.nirogstreet.R;
import com.app.nirogstreet.adapter.LikesAdapter;
import com.app.nirogstreet.circularprogressbar.CircularProgressBar;
import com.app.nirogstreet.model.LikesModel;
import com.app.nirogstreet.parser.LikeParser;
import com.app.nirogstreet.uttil.AppUrl;
import com.app.nirogstreet.uttil.Event_For_Firebase;
import com.app.nirogstreet.uttil.Methods;
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
 * Created by Preeti on 27-10-2017.
 */
public class LikesDisplayActivity extends AppCompatActivity {
    SesstionManager sessionManager;
    GetLikesAsynctask getLikesAsynctask;
    CircularProgressBar circularProgressBar;
    RecyclerView recyclerView;
    LikesAdapter likesAdapter;

    String userId, authToken, feedId;
    private ImageView backImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.likicountlisting);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.statusbarcolor));
        }
        Event_For_Firebase.getEventCount(LikesDisplayActivity.this,"Feed_Post_Screen_Visit_Like_Click");
        circularProgressBar = (CircularProgressBar) findViewById(R.id.scroll);
        recyclerView = (RecyclerView) findViewById(R.id.lv);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(LikesDisplayActivity.this);
        recyclerView.setLayoutManager(llm);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        feedId = getIntent().getStringExtra("feedId");
        sessionManager = new SesstionManager(LikesDisplayActivity.this);
        HashMap<String, String> userDetails = sessionManager.getUserDetails();
        authToken = userDetails.get(SesstionManager.AUTH_TOKEN);
        userId = userDetails.get(SesstionManager.USER_ID);
        backImageView = (ImageView) findViewById(R.id.back);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if (NetworkUtill.isNetworkAvailable(LikesDisplayActivity.this)) {
            getLikesAsynctask = new GetLikesAsynctask(feedId);
            getLikesAsynctask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {

            NetworkUtill.showNoInternetDialog(LikesDisplayActivity.this);
        }

    }

    public class GetLikesAsynctask extends AsyncTask<Void, Void, Void> {
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

        public GetLikesAsynctask(String feedId) {
            this.feedId = feedId;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            circularProgressBar.setVisibility(View.GONE);
            try {
                if (jo != null) {
                    if (jo.has("code") && !jo.isNull("code")) {
                        int code = jo.getInt("code");
                        if (code == AppUrl.INVALID_AUTH_CODE) {
                            Methods.logOutUser(LikesDisplayActivity.this);
                        }
                    }
                    ArrayList<LikesModel> likesModels = new ArrayList<>();
                    likesModels = LikeParser.likesParser(jo);
                    if (likesAdapter == null) {
                        likesAdapter = new LikesAdapter(LikesDisplayActivity.this, likesModels);
                        recyclerView.setAdapter(likesAdapter);
                        final ArrayList<LikesModel> finalLikesModels = likesModels;
                 /*   recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent resultIntent = new Intent(LikesDisplayActivity.this, ProfileActivity.class);
                            LikesModel likesModel = finalLikesModels.get(i);
                            resultIntent.putExtra("userId", likesModel.getUserId());
                            startActivity(resultIntent);
                        }
                    });*/
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                String url = AppUrl.BaseUrl + "feed/feed-likes";
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
                pairs.add(new BasicNameValuePair("feedID", feedId));
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
        protected void onPreExecute() {
            super.onPreExecute();
            circularProgressBar.setVisibility(View.VISIBLE);
        }
    }
}
