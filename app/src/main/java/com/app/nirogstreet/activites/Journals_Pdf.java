package com.app.nirogstreet.activites;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.app.nirogstreet.R;
import com.app.nirogstreet.circularprogressbar.CircularProgressBar;
import com.google.android.gms.vision.text.Text;

import static com.app.nirogstreet.R.id.webView1;
import static com.app.nirogstreet.R.id.webview;

/**
 * Created by as on 3/16/2018.
 */

public class Journals_Pdf extends AppCompatActivity {
    WebView webView;
    ProgressDialog pDialog;
    String name, detailpdf;
    TextView title_side_left;
    CircularProgressBar mCircularProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.journals_pdf_webview);
        title_side_left = (TextView) findViewById(R.id.title_side_left);
        mCircularProgressBar = (CircularProgressBar) findViewById(R.id.circularProgressBar);
        name = getIntent().getStringExtra("name");
        detailpdf = getIntent().getStringExtra("detail");
        title_side_left.setText(name);
        init();
        listener();


    }

    private void init() {
        webView = (WebView) findViewById(webView1);
        webView.getSettings().setJavaScriptEnabled(true);
        mCircularProgressBar.setVisibility(View.VISIBLE);
        String myPdfUrl = detailpdf;

        webView.loadUrl(myPdfUrl);

    }

    private void listener() {
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mCircularProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mCircularProgressBar.setVisibility(View.GONE);
            }
        });
    }
}
