package com.app.nirogstreet.uttil;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.style.ClickableSpan;
import android.view.View;

import com.app.nirogstreet.activites.OpenDocument;

/**
 * Created by Preeti on 05-01-2018.
 */
public class GoToURLSpan extends ClickableSpan {
    String url;
    Context context;
    int in_app;

    public GoToURLSpan(String url, Context context, int in_app) {
        this.url = url;
        this.context = context;
        this.in_app = in_app;
    }


    public void onClick(View view) {
        Uri webPage; //http:<URL> or https:<URL>
        if (in_app == 0) {
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                webPage = Uri.parse("http://" + url);
            } else {
                webPage = Uri.parse(url);
            }
            Intent intent = new Intent(Intent.ACTION_VIEW, webPage);
            context.startActivity(intent);
        } else {
            Intent intent1 = new Intent(context, OpenDocument.class);
            intent1.putExtra("url", url);
            intent1.putExtra("in_app_url",1);
            context.startActivity(intent1);
        }
    }
}


