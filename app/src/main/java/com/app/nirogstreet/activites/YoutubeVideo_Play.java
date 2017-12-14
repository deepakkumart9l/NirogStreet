package com.app.nirogstreet.activites;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.app.nirogstreet.R;

/**
 * Created by HP on 14-12-2017.
 */

public class YoutubeVideo_Play extends AppCompatActivity {
    String videourl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.youtube_video_play);
        videourl = getIntent().getStringExtra("videourl");
        String videoUrl[];
        String frameVideo = null;
        try {
            if (videourl.contains("=")) {
                videoUrl = videourl.split("=");
            } else {
                videoUrl = videourl.split("be/");
            }
            String video_id = videoUrl[1];
            frameVideo = "<iframe width=\"100%\" height=" + "100%" + " src=\"https://www.youtube.com/embed/" + video_id + "?" + "\" frameborder=\"0\" allowfullscreen></iframe>";
        } catch (Exception e) {
            e.printStackTrace();
        }

        WebView displayYoutubeVideo = (WebView) findViewById(R.id.mWebView);
        displayYoutubeVideo.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        WebSettings webSettings = displayYoutubeVideo.getSettings();
        webSettings.setJavaScriptEnabled(true);
        displayYoutubeVideo.loadData(frameVideo, "text/html", "utf-8");
    }
}
