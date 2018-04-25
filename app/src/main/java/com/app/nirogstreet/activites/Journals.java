package com.app.nirogstreet.activites;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.nirogstreet.BharamTool.BharamTool_Adapter;
import com.app.nirogstreet.BharamTool.Bharam_Model;
import com.app.nirogstreet.BharamTool.SwarnBhoota_Yog_Type;
import com.app.nirogstreet.R;
import com.app.nirogstreet.adapter.Journals_Adapter;
import com.app.nirogstreet.circularprogressbar.CircularProgressBar;
import com.app.nirogstreet.listeners.OnItemClickListeners;
import com.app.nirogstreet.uttil.AppUrl;
import com.app.nirogstreet.uttil.NetworkUtill;
import com.app.nirogstreet.uttil.Query_Method;
import com.app.nirogstreet.uttil.SesstionManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Preeti on 23-02-2018.
 */
public class Journals extends AppCompatActivity implements View.OnClickListener {
    ImageView backImageView;
    private SesstionManager session;
    CircularProgressBar mCircularProgressBar;
    String authToken, userId, res;
    private List<Bharam_Model> listing_models;
    Journals_Adapter adapter;
    RecyclerView rv;
    private static final String PDF_MIME_TYPE = "application/pdf";
    private static final String HTML_MIME_TYPE = "text/html";

    LinearLayout mLayout_journal_magzine, mBooks_layout, mAudio_video_layout, mPpt_layout,mVideo_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.journals_activity);
        mLayout_journal_magzine = (LinearLayout) findViewById(R.id.layout_journal_magzine);
        mLayout_journal_magzine.setOnClickListener(this);
        mBooks_layout = (LinearLayout) findViewById(R.id.books_layout);
        mBooks_layout.setOnClickListener(this);
        mAudio_video_layout = (LinearLayout) findViewById(R.id.audio_video_layout);
        mAudio_video_layout.setOnClickListener(this);
        mVideo_layout=(LinearLayout)findViewById(R.id.video_layout);
        mVideo_layout.setOnClickListener(this);
        mPpt_layout = (LinearLayout) findViewById(R.id.ppt_layout);
        mPpt_layout.setOnClickListener(this);
        backImageView = (ImageView) findViewById(R.id.back);
        mCircularProgressBar = (CircularProgressBar) findViewById(R.id.circularProgressBar);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_journal_magzine:
                Intent intent = new Intent(Journals.this, Journals_sub_Cat.class);
                intent.putExtra("value",1);
                startActivity(intent);
                break;
            case R.id.books_layout:
                Intent intent1 = new Intent(Journals.this, Journals_sub_Cat.class);
                intent1.putExtra("value",2);
                startActivity(intent1);
                break;
            case R.id.audio_video_layout:
                Intent intent2 = new Intent(Journals.this, Journals_sub_Cat.class);
                intent2.putExtra("value",3);
                startActivity(intent2);
                break;
            case R.id.video_layout:
                Intent intent3 = new Intent(Journals.this, Journals_sub_Cat.class);
                intent3.putExtra("value",4);
                startActivity(intent3);
                break;
            case R.id.ppt_layout:
                Intent intent4 = new Intent(Journals.this, Journals_sub_Cat.class);
                intent4.putExtra("value",5);
                startActivity(intent4);
                break;
        }
    }

}
