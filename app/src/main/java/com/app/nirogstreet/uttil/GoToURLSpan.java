package com.app.nirogstreet.uttil;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * Created by Preeti on 05-01-2018.
 */
public class GoToURLSpan extends ClickableSpan {
    String url;
    Context context;

    public GoToURLSpan(String url,Context context){
        this.url = url;
        this.context=context;
    }

    public void onClick(View view) {
        Uri webPage ; //http:<URL> or https:<URL>

        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            webPage = Uri.parse("http://" + url);
        }else {
            webPage=Uri.parse(url);
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, webPage);
        context.startActivity(intent);
    }
}


