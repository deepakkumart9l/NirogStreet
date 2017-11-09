package com.app.nirogstreet.activites;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.app.nirogstreet.R;
import com.app.nirogstreet.uttil.CustomPagerAdapter;
import com.app.nirogstreet.uttil.TypeFaceMethods;
import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;


public class MainActivity extends AppCompatActivity {
    TabLayout tabLayout;
    public static ViewPager viewPager;
    FrameLayout notiframe;

    FrameLayout frameLayoutview_alert_red_circle;

    ImageView searchImageView, notifictaionImageView;
    public TextView textViewTab, createTextView;

    private CustomPagerAdapter customPagerAdapter;
    ImageView searchgroupImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        frameLayoutview_alert_red_circle = (FrameLayout) findViewById(R.id.view_alert_red_circle);
        frameLayoutview_alert_red_circle.setVisibility(View.GONE);
        textViewTab = (TextView) findViewById(R.id.textTab);
        searchgroupImageView = (ImageView) findViewById(R.id.searchgroup);
        searchgroupImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,CommunitySearchActivity.class);
                startActivity(intent);
            }
        });
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        createTextView = (TextView) findViewById(R.id.create);
        TypeFaceMethods.setRegularTypeFaceForTextView(createTextView, MainActivity.this);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        viewPager.setOffscreenPageLimit(1);

        customPagerAdapter = new CustomPagerAdapter(getSupportFragmentManager(), tabLayout, this);
        // increase this limit if you have more tabs!
        //viewPager.setOffscreenPageLimit(2);

        viewPager.setAdapter(customPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                switch (position) {
                    case 0:
                        searchgroupImageView.setVisibility(View.GONE);
                        searchImageView.setVisibility(View.VISIBLE);
                        createTextView.setVisibility(View.GONE);
                        notiframe.setVisibility(View.VISIBLE);

                        setTabText("Home");
                        break;
                    case 1:
                        searchImageView.setVisibility(View.GONE);
                        createTextView.setVisibility(View.VISIBLE);
                        notiframe.setVisibility(View.GONE);
                        searchgroupImageView.setVisibility(View.VISIBLE);

                        setTabText("Community");
                        break;
                    case 2:
                        createTextView.setVisibility(View.GONE);
                        notiframe.setVisibility(View.GONE);
                        searchgroupImageView.setVisibility(View.GONE);

                        searchImageView.setVisibility(View.GONE);
                        setTabText("You");
                        break;

                }
            }

            @Override
            public void onPageSelected(int position) {

            }


            @Override
            public void onPageScrollStateChanged(int state) {


            }

        });
        searchImageView = (ImageView) findViewById(R.id.search);
        createTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateCommunity.class);
                startActivity(intent);

            }
        });
        LinearLayout tabLinearLayoutTimeline = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custum, null);

        TextView tabOneTimeline = (TextView) tabLinearLayoutTimeline.findViewById(R.id.tab);
        tabOneTimeline.setText("HOME");
        TypeFaceMethods.setRegularTypeBoldFaceTextView(tabOneTimeline, MainActivity.this);

        tabOneTimeline.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        ImageView oneImgTimeline = (ImageView) tabLinearLayoutTimeline.findViewById(R.id.icon);
        oneImgTimeline.setImageResource(R.drawable.home);
        tabLayout.getTabAt(0).setCustomView(tabLinearLayoutTimeline);

        LinearLayout tabLinearLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custum, null);

        TextView tabOne = (TextView) tabLinearLayout.findViewById(R.id.tab);
        tabOne.setText("COMMUNITIES");
        TypeFaceMethods.setRegularTypeBoldFaceTextView(tabOne, MainActivity.this);

        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        ImageView oneImg = (ImageView) tabLinearLayout.findViewById(R.id.icon);
        oneImg.setImageResource(R.drawable.home);
        tabLayout.getTabAt(1).setCustomView(tabLinearLayout);

        LinearLayout tabtwo = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custum, null);
        TextView tabtwoText = (TextView) tabtwo.findViewById(R.id.tab);
        tabtwoText.setText("YOU");
        TypeFaceMethods.setRegularTypeBoldFaceTextView(tabtwoText, MainActivity.this);

        tabtwoText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        ImageView TwoImg = (ImageView) tabtwo.findViewById(R.id.icon);
        TwoImg.setImageResource(R.drawable.home);
        tabLayout.getTabAt(2).setCustomView(tabtwo);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) tab.setCustomView(R.layout.view_home_tab);
        }
        notifictaionImageView = (ImageView) findViewById(R.id.notifictaion);
        notifictaionImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NotificationListing.class);
                startActivity(intent);
            }
        });

        notiframe = (FrameLayout) findViewById(R.id.notiframe);
        notiframe.setVisibility(View.VISIBLE);
        notiframe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frameLayoutview_alert_red_circle.setVisibility(View.GONE);
                Intent intent = new Intent(MainActivity.this, NotificationListing.class);
                startActivity(intent);
            }
        });
        searchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivityForResult(intent, 4);
            }
        });
    }

    public void setTabsVisibility(boolean displayTabs) {
        if (displayTabs) {
            tabLayout.setVisibility(View.VISIBLE);

        } else {
            tabLayout.setVisibility(View.GONE);
        }
    }

    public void setTabText(String tabText) {
        if (textViewTab != null)

            textViewTab.setText(tabText);
        textViewTab.setVisibility(View.VISIBLE);
    }

    public static int selectedFragment() {
        return viewPager.getCurrentItem();
    }

}
