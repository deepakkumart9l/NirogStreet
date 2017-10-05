package com.app.nirogstreet.activites;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.app.nirogstreet.R;
import com.app.nirogstreet.uttil.CustomPagerAdapter;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;


public class MainActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    private CustomPagerAdapter customPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
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
        LinearLayout tabLinearLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custum, null);

        TextView tabOne = (TextView) tabLinearLayout.findViewById(R.id.tab);
        tabOne.setText("Profile");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        ImageView oneImg = (ImageView) tabLinearLayout.findViewById(R.id.icon);
        oneImg.setImageResource(R.drawable.home);
        tabLayout.getTabAt(0).setCustomView(tabLinearLayout);

        LinearLayout tabtwo = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custum, null);
        TextView tabtwoText = (TextView) tabtwo.findViewById(R.id.tab);
        tabtwoText.setText("Blog");
        tabtwoText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        ImageView TwoImg = (ImageView) tabtwo.findViewById(R.id.icon);
        TwoImg.setImageResource(R.drawable.home);
        tabLayout.getTabAt(1).setCustomView(tabtwo);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) tab.setCustomView(R.layout.view_home_tab);
        }
    }
    public void setTabsVisibility(boolean displayTabs) {
        if (displayTabs) {
            tabLayout.setVisibility(View.VISIBLE);

        } else {
            tabLayout.setVisibility(View.GONE);
        }
    }

}
