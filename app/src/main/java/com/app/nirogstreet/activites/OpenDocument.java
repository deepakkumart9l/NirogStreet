package com.app.nirogstreet.activites;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Path;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.nirogstreet.R;
import com.app.nirogstreet.circularprogressbar.CircularProgressBar;
import com.app.nirogstreet.model.Course_Detail_model;
import com.app.nirogstreet.uttil.NetworkUtill;
import com.app.nirogstreet.uttil.SesstionManager;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Preeti on 17-12-2017.
 */

public class OpenDocument extends Activity {
    WebView mwebView;
    String ul;
    ImageView downloadImageView;
    SesstionManager sesstionManager;
    private static final String GOOGLE_DRIVE_PDF_READER_PREFIX = "http://drive.google.com/viewer?url=";
    private static final String PDF_MIME_TYPE = "application/pdf";
    private static final String HTML_MIME_TYPE = "text/html";
    String url = null;
    CircularProgressBar circularProgressBar;
    String id = null;
    Course_Detail_model course_detail_model;
    String title = null;
    ImageView backImageView;
    int module_pos, topic_pos, file_pos;
    TextView title_side_left;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.web_content);
        if (getIntent().hasExtra("url")) {
            url = getIntent().getStringExtra("url");
        }


        backImageView = (ImageView) findViewById(R.id.back);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        title_side_left = (TextView) findViewById(R.id.title_side_left);
        title_side_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        downloadImageView = (ImageView) findViewById(R.id.download);
        downloadImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPDFUrl(url);

            }
        });
        sesstionManager = new SesstionManager(OpenDocument.this);
        if (getIntent().hasExtra("url")) {
            url = getIntent().getStringExtra("url");
        }
        if (getIntent().hasExtra("title")) {
            title = getIntent().getStringExtra("title");
            title_side_left.setText(title);

        }
        if (getIntent().hasExtra("id")) {
            id = getIntent().getStringExtra("id");
        }
        circularProgressBar = (CircularProgressBar) findViewById(R.id.circularProgressBar);
        circularProgressBar.setVisibility(View.VISIBLE);
        mwebView = (WebView) findViewById(R.id.web);
        mwebView.setWebViewClient(new AppWebViewClients());
        mwebView.getSettings().setJavaScriptEnabled(true);
        mwebView.getSettings().setUseWideViewPort(false);
        mwebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        mwebView.loadUrl("http://docs.google.com/gview?embedded=true&url="
                + url);
    }

    public void showPDFUrl(final String pdfUrl) {

        if (isPDFSupported()) {
            downloadAndOpenPDF(pdfUrl);

        } else {

        }
        //askToOpenPDFThroughGoogleDrive( context, pdfUrl );
    }

    public static File getAvailableFile(String pdfUrl, Context context) {
        // Get filename
        final String filename = pdfUrl.substring(pdfUrl.lastIndexOf("/") + 1);
        // The place where the downloaded PDF file will be put
        File tempFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), filename);
        return tempFile;
    }

    public void downloadAndOpenPDF(final String pdfUrl) {
        final String filename = pdfUrl.substring(pdfUrl.lastIndexOf("/") + 1);
        // The place where the downloaded PDF file will be put
        final File tempFile = getAvailableFile(pdfUrl, OpenDocument.this);
        if (tempFile.exists()) {
            // If we have downloaded the file before, just go ahead and show it.

            Toast.makeText(OpenDocument.this, R.string.already_present, Toast.LENGTH_LONG).show();
            return;
        }

        // Show progress dialog while downloading
        //final ProgressDialog progress = ProgressDialog.show( context, context.getString( R.string.hello_world ), context.getString( R.string.app_name ), true );

        final ProgressDialog progress = new ProgressDialog(OpenDocument.this);
        progress.setMessage("Downloading, Please Wait!");
        //progress.setMax(100);
        // progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        // progress.setIndeterminate(true);
        // progress.setProgress((int) tempFile.length());
        progress.show();

        // Create the download request
        DownloadManager.Request r = new DownloadManager.Request(Uri.parse(pdfUrl));
        r.setDestinationInExternalFilesDir(OpenDocument.this, Environment.DIRECTORY_DOWNLOADS, filename);
        final DownloadManager dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        BroadcastReceiver onComplete = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (!progress.isShowing()) {
                    return;
                }
                context.unregisterReceiver(this);

                progress.dismiss();


                ;

                long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

                Cursor c = dm.query(new DownloadManager.Query().setFilterById(downloadId));

                if (c.moveToFirst()) {
                    int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        ///btnguidelisting.setText("Open guide for tablet/iPad");
                        //   openPDF(context, Uri.fromFile(tempFile));
                    }
                }
                c.close();
            }
        };
        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        // Enqueue the request
        dm.enqueue(r);
    }


    public boolean isPDFSupported() {
        Intent i = new Intent(Intent.ACTION_VIEW);
        File tempFile = new File(OpenDocument.this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "test.pdf");
        i.setDataAndType(Uri.fromFile(tempFile), PDF_MIME_TYPE);
        return getPackageManager().queryIntentActivities(i, PackageManager.MATCH_DEFAULT_ONLY).size() > 0;
    }
    public class AppWebViewClients extends WebViewClient {


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
            circularProgressBar.setVisibility(View.GONE);

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {



                }
            }, 3000);


        }
    }

}

