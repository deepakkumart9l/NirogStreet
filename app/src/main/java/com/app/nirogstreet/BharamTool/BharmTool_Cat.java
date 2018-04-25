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

public class BharmTool_Cat extends AppCompatActivity implements View.OnClickListener {

    LinearLayout mSwanbhoota_layout, mDisease_layout, herbal_layout, classical_layout;
    ImageView backImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bharmtool_cat);
        backImageView = (ImageView) findViewById(R.id.back);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mSwanbhoota_layout = (LinearLayout) findViewById(R.id.swanbhoota_layout);
        mSwanbhoota_layout.setOnClickListener(this);
        mDisease_layout = (LinearLayout) findViewById(R.id.disease_layout);
        mDisease_layout.setOnClickListener(this);
        herbal_layout = (LinearLayout) findViewById(R.id.herbal_layout);
        herbal_layout.setOnClickListener(this);
        classical_layout = (LinearLayout) findViewById(R.id.classical_layout);
        classical_layout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.swanbhoota_layout:
                Intent intent = new Intent(BharmTool_Cat.this, SwarnBhoota_Cat.class);
                startActivity(intent);
                break;
            case R.id.disease_layout:
                Intent diseas = new Intent(BharmTool_Cat.this, Disease_Cat.class);
                startActivity(diseas);
                break;
            case R.id.herbal_layout:
                Intent herbal = new Intent(BharmTool_Cat.this, Herbal_Section.class);
                startActivity(herbal);
                break;
            case R.id.classical_layout:
                Intent herba2 = new Intent(BharmTool_Cat.this, Herbal_Section.class);
                herba2.putExtra("classical",3);
                startActivity(herba2);
                break;
        }
    }
}
