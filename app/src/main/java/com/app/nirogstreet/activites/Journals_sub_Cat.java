package com.app.nirogstreet.activites;

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
import android.widget.TextView;

import com.app.nirogstreet.BharamTool.Bharam_Model;
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
 * Created by as on 3/26/2018.
 */

public class Journals_sub_Cat extends AppCompatActivity implements OnItemClickListeners {
    ImageView backImageView;
    private SesstionManager session;
    TextView title_side_left;
    CircularProgressBar mCircularProgressBar;
    String authToken, userId, res;
    int page = 1;
    private List<Bharam_Model> listing_models;
    Journals_Adapter adapter;
    RecyclerView rv;
    private static final String PDF_MIME_TYPE = "application/pdf";
    private static final String HTML_MIME_TYPE = "text/html";
    int data_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.journal_sub_cat);
        data_value = getIntent().getIntExtra("value", 0);
        backImageView = (ImageView) findViewById(R.id.back);
        title_side_left = (TextView) findViewById(R.id.title_side_left);

        mCircularProgressBar = (CircularProgressBar) findViewById(R.id.circularProgressBar);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (data_value == 1) {
            title_side_left.setText("Ayurveda Magzines & Journals");
        } else if (data_value == 2) {
            title_side_left.setText("Books");
        } else if (data_value == 3) {
            title_side_left.setText("Audios");
        } else if (data_value == 4) {
            title_side_left.setText("Videos");
        } else if (data_value == 5) {
            title_side_left.setText("PPT");
        }

        rv = (RecyclerView) findViewById(R.id.typelist);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(Journals_sub_Cat.this);
        rv.setLayoutManager(llm);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setNestedScrollingEnabled(false);

        listing_models = new ArrayList<Bharam_Model>();
        if (NetworkUtill.isNetworkAvailable(Journals_sub_Cat.this)) {
            new Asyank_Listing().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            NetworkUtill.showNoInternetDialog(Journals_sub_Cat.this);
        }

        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1)) {
                    page++;
                    if (NetworkUtill.isNetworkAvailable(Journals_sub_Cat.this)) {
                        new Asyank_Listing().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        NetworkUtill.showNoInternetDialog(Journals_sub_Cat.this);
                    }
                }
            }
        });
        adapter = new Journals_Adapter(Journals_sub_Cat.this, listing_models);
        rv.setAdapter(adapter);
        adapter.setListener(Journals_sub_Cat.this);
    }

    @Override
    public void onItemClick(String v, int position, String detail, String name) {

        if (data_value == 1) {
            Intent intent = new Intent(Journals_sub_Cat.this, OpenDocument.class);
            intent.putExtra("url", detail);
            intent.putExtra("name", name);
            startActivity(intent);
        } else if (data_value == 2) {
            Intent intent = new Intent(Journals_sub_Cat.this, OpenDocument.class);
            intent.putExtra("url", detail);
            intent.putExtra("name", name);
            startActivity(intent);
        } else if (data_value == 3) {
            Intent intent = new Intent(Journals_sub_Cat.this, Audio_Play_Activity.class);
            intent.putExtra("url", detail);
            intent.putExtra("name", name);
            startActivity(intent);
        } else if (data_value == 4) {
            Intent intent = new Intent(Journals_sub_Cat.this, VideoPlay_Activity.class);
            intent.putExtra("videotype", "native");
            intent.putExtra("video", detail);
            intent.putExtra("name", name);
            startActivity(intent);
        } else if (data_value == 5) {
            Intent intent = new Intent(Journals_sub_Cat.this, OpenDocument.class);
            intent.putExtra("url", detail);
            intent.putExtra("name", name);
            startActivity(intent);
        }


        // showPDFUrl(Journals.this, detail);
        // Get tracker.
    }

    public void showPDFUrl(final Context context, final String pdfUrl) {

        if (isPDFSupported(context)) {
            downloadAndOpenPDF(context, pdfUrl);

        } else {

        }
        //askToOpenPDFThroughGoogleDrive( context, pdfUrl );
    }

    public static boolean isPDFSupported(Context context) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        final File tempFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "test.pdf");
        i.setDataAndType(Uri.fromFile(tempFile), PDF_MIME_TYPE);
        return context.getPackageManager().queryIntentActivities(i, PackageManager.MATCH_DEFAULT_ONLY).size() > 0;
    }

    public static File getAvailableFile(String pdfUrl, Context context) {
        // Get filename
        final String filename = pdfUrl.substring(pdfUrl.lastIndexOf("/") + 1);
        // The place where the downloaded PDF file will be put
        File tempFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), filename);
        return tempFile;
    }


    public void downloadAndOpenPDF(final Context context, final String pdfUrl) {
        final String filename = pdfUrl.substring(pdfUrl.lastIndexOf("/") + 1);
        // The place where the downloaded PDF file will be put
        final File tempFile = getAvailableFile(pdfUrl, context);
        if (tempFile.exists()) {
            openPDF(context, Uri.fromFile(tempFile));
            return;
        }

        // Show progress dialog while downloading
        //final ProgressDialog progress = ProgressDialog.show( context, context.getString( R.string.hello_world ), context.getString( R.string.app_name ), true );

        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Downloading, Please Wait!");
        //progress.setMax(100);
        // progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        // progress.setIndeterminate(true);
        // progress.setProgress((int) tempFile.length());
        progress.show();

        // Create the download request
        DownloadManager.Request r = new DownloadManager.Request(Uri.parse(pdfUrl));
        r.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, filename);
        final DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        BroadcastReceiver onComplete = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (!progress.isShowing()) {
                    return;
                }
                progress.dismiss();
                long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                Cursor c = dm.query(new DownloadManager.Query().setFilterById(downloadId));
                if (c.moveToFirst()) {
                    int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        openPDF(context, Uri.fromFile(tempFile));
                    }
                }
                c.close();
            }
        };
        context.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        // Enqueue the request
        dm.enqueue(r);
    }

    public static final void openPDF(Context context, Uri localUri) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(localUri, PDF_MIME_TYPE);
        context.startActivity(i);
    }

    class Asyank_Listing extends AsyncTask<Void, Void, Void> {
        int result = 0;
        int code = 0;
        JSONObject jo;
        HttpURLConnection httpURLConnection;
        private String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mCircularProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                String url = "";
                url = AppUrl.BaseUrl + "knowledge/elibrary";
                ContentValues values = new ContentValues();
                values.put("appID", "NRGSRT$(T(L5830FRU@!^AUSER");
                values.put("data_for", data_value);
                values.put("pageNo", page);

                URL uri = new URL(url);
                httpURLConnection = (HttpURLConnection) uri.openConnection();
                httpURLConnection.addRequestProperty("Authorization", "Basic " + authToken);
                httpURLConnection.setReadTimeout(10000);
                httpURLConnection.setConnectTimeout(15000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(Query_Method.getQuery(values));
                writer.flush();
                writer.close();
                os.close();
                httpURLConnection.connect();
                BufferedReader br = new BufferedReader(new InputStreamReader((httpURLConnection.getInputStream())));
                StringBuilder sb = new StringBuilder();
                String output;
                while ((output = br.readLine()) != null) {
                    sb.append(output);
                }
                res = sb.toString();
                jo = new JSONObject(res);
                result = jo.getJSONObject("response").getInt("error");
                // code = jo.getInt("code");

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void r) {
            super.onPostExecute(r);
            mCircularProgressBar.setVisibility(View.GONE);
            if (result == 0) {
                mCircularProgressBar.setVisibility(View.GONE);
                parseJsonFeed(jo);
                adapter.setData(listing_models);
            } else {
                page--;
            }
        }

        public void cancelAsyncTask() {
            if (httpURLConnection != null)
                httpURLConnection.disconnect();
        }

    }

    private void parseJsonFeed(JSONObject response) {
        try {
            JSONObject jsonObject = response.getJSONObject("response");
            JSONArray feedArray = jsonObject.getJSONArray("learning_data");
           /* if (page == 1)
                listing_models.clear();*/
            List<Bharam_Model> tempArray = new ArrayList<>();
            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);
                Bharam_Model item = new Bharam_Model();
                item.setName(feedObj.getString("name"));
                item.setDetail(feedObj.getString("e_file"));
                item.setThumbnail(feedObj.getString("thumb_nail"));
                tempArray.add(item);
            }
            listing_models.addAll(tempArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
