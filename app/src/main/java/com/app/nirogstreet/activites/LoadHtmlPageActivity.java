package com.app.nirogstreet.activites;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import com.app.nirogstreet.R;

/**
 * Created by Preeti on 13-12-2017.
 */

public class LoadHtmlPageActivity extends Activity {
    WebView web;
    String data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_content);
        web=(WebView)findViewById(R.id.web);
        if(getIntent().hasExtra("data"))
        {
            data=getIntent().getStringExtra("data");
        }
        WebView webview = (WebView)this.findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadDataWithBaseURL("", data, "text/html", "UTF-8", "");
    }
}
