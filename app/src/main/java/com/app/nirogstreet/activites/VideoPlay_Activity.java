package com.app.nirogstreet.activites;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.app.nirogstreet.R;
import com.app.nirogstreet.circularprogressbar.CircularProgressBar;
import com.app.nirogstreet.uttil.AppUrl;
import com.app.nirogstreet.uttil.ApplicationSingleton;
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
 * Created by Preeti on 07-11-2017.
 */
public class VideoPlay_Activity extends AppCompatActivity {
    WebView displayVideo;
    String frameVideo;
    String myvideo, videotype;
    Bitmap bmThumbnail;
    VideoView videoView;
    WebView displayYoutubeVideo;
    String videoUrl[];
    ImageView imageView;
    CircularProgressBar circularProgressBar;
    SesstionManager sessionManager;
    private String authToken, userId;
    boolean imagedlt = false;
    TextView title_side_right, title_side_left;
    ImageView back;
    String feedid;
    Toolbar toolbar;
    boolean feedVideo = false, editVisible = false;
    private String videofeedid;
    ImageView img_share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallaryvideoviewgrid);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.statusbarcolor));
        }
        toolbar = (Toolbar) findViewById(R.id.tool);
        circularProgressBar = (CircularProgressBar) findViewById(R.id.circularProgressBar);
        img_share = (ImageView) findViewById(R.id.img_share);
        img_share.setVisibility(View.VISIBLE);
        title_side_right = (TextView) findViewById(R.id.title_side_right);
        title_side_right.setVisibility(View.GONE);
        title_side_left = (TextView) findViewById(R.id.title_side_left);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        title_side_left.setVisibility(View.GONE);
        title_side_right.setVisibility(View.GONE);
        if (getIntent().hasExtra("feedVideo")) {
            feedVideo = getIntent().getBooleanExtra("feedVideo", false);
            if (feedVideo)
                toolbar.setVisibility(View.GONE);
        }
        if (getIntent().hasExtra("editVisible")) {
            editVisible = getIntent().getBooleanExtra("editVisible", false);
            if (editVisible)
                toolbar.setVisibility(View.VISIBLE);
            else
                toolbar.setVisibility(View.GONE);
        }
        feedid = getIntent().getStringExtra("feedid");
        sessionManager = new SesstionManager(VideoPlay_Activity.this);
        imageView = (ImageView) findViewById(R.id.delete_);
        HashMap<String, String> userDetails = sessionManager.getUserDetails();
        authToken = userDetails.get(SesstionManager.AUTH_TOKEN);
        userId = userDetails.get(SesstionManager.USER_ID);
        circularProgressBar = (CircularProgressBar) findViewById(R.id.circularProgressBar);
        myvideo = getIntent().getStringExtra("video");
        videotype = getIntent().getStringExtra("videotype");
        videoView = (VideoView) findViewById(R.id.VideoView);
        videofeedid = getIntent().getStringExtra("videofeedid");
        displayYoutubeVideo = (WebView) findViewById(R.id.webView);

        if (videotype.equalsIgnoreCase("native")) {
            videoView.setVideoPath(myvideo);
            circularProgressBar.setVisibility(View.VISIBLE);
            MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(videoView);
            videoView.setMediaController(mediaController);
            try {
                videoView.seekTo(ApplicationSingleton.getVideoPostion());

            } catch (Exception e) {
                e.printStackTrace();
            }
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    circularProgressBar.setVisibility(View.GONE);

                    Log.i("TAG", "Duration = " +
                            videoView.getDuration());
                    videoView.start();
                }
            });


        } else if (videotype.equalsIgnoreCase("Youtube")) {
            videoView.setVisibility(View.GONE);
            displayYoutubeVideo.setVisibility(View.VISIBLE);
            if (myvideo != null) {
                frameVideo = "<iframe width=\"100%\" height=" + "250" + " src=\"https://www.youtube.com/embed/" + myvideo + "?" + "\" frameborder=\"0\" allowfullscreen></iframe>";
            }
           /* String link = myvideo;
            String pattern = "(?:videos\\/|v=)([\\w-]+)";
            Pattern compiledPattern = Pattern.compile(pattern);
            Matcher matcher = compiledPattern.matcher(link);
*/
           /* if (matcher.find()) {
                Log.e("Youtubeid=", "" + matcher.group());
                frameVideo = "<iframe width=\"100%\" height=" + "250" + " src=\"https://www.youtube.com/embed/" + matcher.group() + "?" + "\" frameborder=\"0\" allowfullscreen></iframe>";
            }*/
            /*if (myvideo.contains("=")) {
                videoUrl = myvideo.split("=");
            } else {
                videoUrl = myvideo.split("be/");
            }
            if (videoUrl != null && videoUrl.length > 0) {
                String video_id = videoUrl[1];
                if (video_id != null) {
                    frameVideo = "<iframe width=\"100%\" height=" + "250" + " src=\"https://www.youtube.com/embed/" + video_id + "?" + "\" frameborder=\"0\" allowfullscreen></iframe>";
                }
            }*/

            // String frameVideo = "<html><body>Video From YouTube<br><iframe width=\"420\" height=\"315\" src=\"+myvideo+\" frameborder=\"0\" allowfullscreen></iframe></body></html>";
            displayYoutubeVideo.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    return false;
                }
            });
            WebSettings webSettings = displayYoutubeVideo.getSettings();
            webSettings.setJavaScriptEnabled(true);
            if (frameVideo != null) {
                displayYoutubeVideo.loadData(frameVideo, "text/html", "utf-8");
            }
        }
        img_share.setVisibility(View.GONE);
        img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent intent = new Intent(AlbumClick_Img.this, Edit_Album.class);

                intent.putExtra("imageslist", myList);
                startActivity(intent);*/
               // sharePopup(img_share);
            }
        });
        imageView.setVisibility(View.GONE);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  showAlert();
            }
        });
    }

/*
    public void showAlert() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(VideoPlay_Activity.this);
        builder1.setCancelable(false);
        // builder1.setTitle(getResources().getString(R.string.datecannotbeblank));
        builder1.setTitle("Do you want to delete this photo?");
        builder1.setPositiveButton(VideoPlay_Activity.this.getString(R.string.Ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (NetworkUtill.isNetworkAvailable(VideoPlay_Activity.this)) {
                    DeletepostAsyncTask deletepostAsyncTask = new DeletepostAsyncTask(userId, authToken);
                    deletepostAsyncTask.execute();
                } else {
                    NetworkUtill.showNoInternetDialog(VideoPlay_Activity.this);
                }
                dialog.cancel();
            }
        });
        builder1.setNegativeButton(VideoPlay_Activity.this.getString(R.string.Cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
*/

/*
    public class DeletepostAsyncTask extends AsyncTask<Void, Void, Void> {
        String authToken;
        JSONObject jo;
        String userId;
        private String responseBody;
        HttpClient client;
        Context context;

        public void cancelAsyncTask() {
            if (client != null && !isCancelled()) {
                cancel(true);
                client = null;
            }
        }

        public DeletepostAsyncTask(String userId, String authToken) {
            this.authToken = authToken;
            this.userId = userId;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //  circularProgressBar.setVisibility(View.GONE);
            try {
                if (jo != null) {
                    imagedlt = true;
                    Toast.makeText(VideoPlay_Activity.this, jo.getJSONObject("responce").getString("message"), Toast.LENGTH_SHORT).show();
                    SharedPreferences sharedPref1 = getApplicationContext().getSharedPreferences("imgvidupdate", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor1 = sharedPref1.edit();
                    editor1.putBoolean("imgvidupdate", imagedlt);
                    editor1.commit();
                    finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                String url = AppUrl.baseUrl + "feed/delete";
                SSLSocketFactory sf = new SSLSocketFactory(SSLContext.getDefault(), SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                Scheme sch = new Scheme("https", 443, sf);
                client = new DefaultHttpClient();
                client.getConnectionManager().getSchemeRegistry().register(sch);
                HttpPost httppost = new HttpPost(url);
                HttpResponse response;
                List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                pairs.add(new BasicNameValuePair(AppUrl.APP_ID_PARAM, AppUrl.APP_ID_VALUE_POST));
                pairs.add(new BasicNameValuePair("userID", userId));
                pairs.add(new BasicNameValuePair("feedID", videofeedid));
                httppost.setHeader("Authorization", "Basic " + authToken);
                httppost.setEntity(new UrlEncodedFormEntity(pairs));
                response = client.execute(httppost);
                responseBody = EntityUtils.toString(response.getEntity());
                jo = new JSONObject(responseBody);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //  circularProgressBar.setVisibility(View.VISIBLE);
        }
    }
*/

/*
    public void sharePopup(ImageView view) {
        PopupMenu popup = new PopupMenu(VideoPlay_Activity.this, view);
        popup.getMenuInflater().inflate(R.menu.popup_menu_share, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.publicshare:
                        if (NetworkUtill.isNetworkAvailable(VideoPlay_Activity.this)) {
                            SharePublicAsyncTask sharePublicAsyncTask = new SharePublicAsyncTask(videofeedid, userId, authToken);
                            sharePublicAsyncTask.execute();
                        } else {
                            NetworkUtill.showNoInternetDialog(VideoPlay_Activity.this);
                        }
                        break;
                    case R.id.friendstimeline:
                        Intent intent = new Intent(VideoPlay_Activity.this, ShareOnFriendsTimeline.class);
                    {
                        intent.putExtra("feedId", videofeedid);
                        intent.putExtra("userId", userId);
                        startActivity(intent);
                    }
                    break;
                    case R.id.groupstimeline:
                        Intent intent1 = new Intent(VideoPlay_Activity.this, ShareOnFriendsTimeline.class);
                    {
                        intent1.putExtra("feedId", videofeedid);
                        intent1.putExtra("userId", userId);
                        intent1.putExtra("shareOnGroup", true);
                        startActivity(intent1);
                    }
                }
                return true;
            }
        });

        popup.show();//showing popup menu
    }
*/

/*
    public class SharePublicAsyncTask extends AsyncTask<Void, Void, Void> {
        String authToken;
        JSONObject jo;
        String feedId, userId;


        private String responseBody;
        HttpClient client;
        Context context;

        public void cancelAsyncTask() {
            if (client != null && !isCancelled()) {
                cancel(true);
                client = null;
            }
        }

        public SharePublicAsyncTask(String feedId, String userId, String authToken) {
            this.feedId = feedId;
            this.authToken = authToken;
            this.userId = userId;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                if (jo != null) {
                    boolean status = jo.getBoolean("status");
                    if (status == true) {
                        Toast.makeText(VideoPlay_Activity.this, "Post has been shared sucessfully", Toast.LENGTH_LONG).show();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                String url = AppUrl.BaseUrl + "feed/share";
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
                pairs.add(new BasicNameValuePair("shreType", "1"));
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
*/

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        try {
            ApplicationSingleton.setVideoPostion(videoView.getCurrentPosition());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation ==
                Configuration.ORIENTATION_LANDSCAPE) {
            // Change things
        } else if (newConfig.orientation ==
                Configuration.ORIENTATION_PORTRAIT) {
            // Change other things
        }
    }
}
