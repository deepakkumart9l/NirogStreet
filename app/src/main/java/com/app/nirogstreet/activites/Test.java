package com.app.nirogstreet.activites;

import android.app.Activity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Patterns;
import android.widget.TextView;

import com.app.nirogstreet.R;
import com.app.nirogstreet.uttil.GoToURLSpan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Preeti on 05-01-2018.
 */

public class Test extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        TextView textView=(TextView) findViewById(R.id.text);
        String text = textView.getText().toString();

        int i=0;
        SpannableString spannableString = new SpannableString(text);
        Matcher urlMatcher = Patterns.WEB_URL.matcher(text);
        while(urlMatcher.find()) {
            String url = urlMatcher.group(i);
            int start = urlMatcher.start(i);
            int end = urlMatcher.end(i++);
            if(isValidUrl(url)){
               /* if(url.startsWith("http")||url.startsWith("www."))
                    //spannableString.setSpan(new GoToURLSpan(url), start, end, 0);
            }*/


        }
        textView.setText(spannableString);

        textView.setMovementMethod(new LinkMovementMethod());
    }}
    private boolean isValidUrl(String url) {
        Pattern p = Patterns.WEB_URL;
        Matcher m = p.matcher(url.toLowerCase());
        return m.matches();
    }
    private void stripUnderlines(TextView textView) {
        Spannable s = new SpannableString(textView.getText());
        URLSpan[] spans = s.getSpans(0, s.length(), URLSpan.class);
        for (URLSpan span: spans) {
            int start = s.getSpanStart(span);
            int end = s.getSpanEnd(span);
            s.removeSpan(span);
            span = new URLSpanNoUnderline(span.getURL());
            s.setSpan(span, start, end, 0);
        }
        textView.setText(s);
    }
    private class URLSpanNoUnderline extends URLSpan {
        public URLSpanNoUnderline(String url) {
            super(url);
        }
        @Override public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
        }
    }
}
