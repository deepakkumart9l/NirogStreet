package com.app.nirogstreet.BharamTool;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.nirogstreet.R;

/**
 * Created by as on 2/22/2018.
 */

public class SwarnBhoota_Cat extends AppCompatActivity implements View.OnClickListener {

    LinearLayout mSwanbhoota_capsule_layout,mSwanbhoota_churan_layout;
ImageView backImageView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swarnbhoota_cat);
        backImageView=(ImageView)findViewById(R.id.back) ;
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mSwanbhoota_capsule_layout = (LinearLayout) findViewById(R.id.swanbhoota_capsule_layout);
        mSwanbhoota_capsule_layout.setOnClickListener(this);
        mSwanbhoota_churan_layout=(LinearLayout)findViewById(R.id.swanbhoota_churan_layout);
        mSwanbhoota_churan_layout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.swanbhoota_capsule_layout:
                Intent capsule = new Intent(SwarnBhoota_Cat.this, SwarnBhoota_Yog_Type.class);
                capsule.putExtra("type",1);
                startActivity(capsule);
                break;
            case R.id.swanbhoota_churan_layout:
                Intent churan = new Intent(SwarnBhoota_Cat.this, SwarnBhoota_Yog_Type.class);
                churan.putExtra("type",2);
                startActivity(churan);
                break;
        }
    }
}
