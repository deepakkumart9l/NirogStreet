package com.app.nirogstreet.activites;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.nirogstreet.R;

import org.sufficientlysecure.htmltextview.HtmlResImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

/**
 * Created by as on 3/27/2018.
 */

public class ViewMore_Data extends AppCompatActivity {
    HtmlTextView descrptn_txt;
    String descrptn, name;
    TextView title_side_left;
    ImageView backImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_view_more_text);
        descrptn = getIntent().getStringExtra("description");
        name = getIntent().getStringExtra("name");
        descrptn_txt = (HtmlTextView) findViewById(R.id.descrptn_txt);
        title_side_left = (TextView) findViewById(R.id.title_side_left);
        backImageView = (ImageView) findViewById(R.id.back);

        descrptn_txt.setHtml(descrptn, new HtmlResImageGetter(descrptn_txt));
        title_side_left.setText(name);

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
