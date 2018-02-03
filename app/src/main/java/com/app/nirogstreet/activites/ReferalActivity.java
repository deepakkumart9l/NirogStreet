package com.app.nirogstreet.activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.nirogstreet.R;
import com.app.nirogstreet.uttil.ApplicationSingleton;
import com.app.nirogstreet.uttil.SesstionManager;

/**
 * Created by as on 2/3/2018.
 */

public class ReferalActivity extends AppCompatActivity {

    LinearLayout imgshare;
    TextView txt_share;
    String shareText;
ImageView backInImageView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.referallactivitry);
        imgshare = (LinearLayout) findViewById(R.id.share_ettiquets);
        txt_share = (TextView) findViewById(R.id.txt_share);
backInImageView=(ImageView)findViewById(R.id.back);
        final SesstionManager sesstionManager=new SesstionManager(ReferalActivity.this);
        backInImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        shareText = txt_share.getText().toString();
        imgshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String refer_code;

                refer_code    =ApplicationSingleton.getUserDetailModel().getReferral_code();

                if (refer_code != null && refer_code.length() > 0)
                    try {
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
String str="Hey, become a part of the ever-growing community of Ayurveda Doctors along with me. Sign up with my code "+refer_code+" & get started. Download: http://bit.ly/2GKz95N";
                        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, str);
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "I found this Referral ");
                        startActivity(Intent.createChooser(shareIntent, "Share with"));
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                    }
            }
        });
    }
}
