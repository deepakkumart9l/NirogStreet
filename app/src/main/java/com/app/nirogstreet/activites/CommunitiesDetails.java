package com.app.nirogstreet.activites;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.nirogstreet.R;
import com.app.nirogstreet.uttil.CustomPagerAdapter;
import com.app.nirogstreet.uttil.CustomPagerCommunitiesAdapter;
import com.app.nirogstreet.uttil.TypeFaceMethods;

import static com.app.nirogstreet.R.id.tab;

/**
 * Created by Preeti on 02-11-2017.
 */

public class CommunitiesDetails extends AppCompatActivity {
    CustomPagerCommunitiesAdapter customPagerCommunitiesAdapter;
    TabLayout tabLayout;
    ViewPager viewPager;
    String groupId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.communication_detail);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        viewPager.setOffscreenPageLimit(2);
        if (getIntent().hasExtra("groupId")) {
    groupId=getIntent().getStringExtra("groupId");
        }
        customPagerCommunitiesAdapter = new CustomPagerCommunitiesAdapter(getSupportFragmentManager(), tabLayout, this,groupId);
        // increase this limit if you have more tabs!
        //viewPager.setOffscreenPageLimit(2);

        viewPager.setAdapter(customPagerCommunitiesAdapter);
        tabLayout.setupWithViewPager(viewPager);
  /*  tabLayout.addTab(tabLayout.newTab().setText("COMMUNICATIONS"));
        tabLayout.addTab(tabLayout.newTab().setText("ABOUT"));*/
        LinearLayout tabLinearLayoutTimeline = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custum_comm, null);

        TextView tabOneTimeline = (TextView) tabLinearLayoutTimeline.findViewById(tab);
        tabOneTimeline.setText("COMMUNICATIONS");
        TypeFaceMethods.setRegularTypeBoldFaceTextView(tabOneTimeline, CommunitiesDetails.this);
        tabOneTimeline.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        ImageView oneImgTimeline = (ImageView) tabLinearLayoutTimeline.findViewById(R.id.icon);
        oneImgTimeline.setImageResource(R.drawable.home);
        oneImgTimeline.setVisibility(View.GONE);
        tabLayout.getTabAt(0).setCustomView(tabLinearLayoutTimeline);
        // tabLayout.setTabTextColors(Color.parseColor("#727272"), Color.parseColor("#ffffff"));

        LinearLayout tabLinearLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custum_comm, null);

        TextView tabOne = (TextView) tabLinearLayout.findViewById(tab);
        tabOne.setText("ABOUT");
        TypeFaceMethods.setRegularTypeBoldFaceTextView(tabOne, CommunitiesDetails.this);

        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        ImageView oneImg = (ImageView) tabLinearLayout.findViewById(R.id.icon);
        oneImg.setVisibility(View.GONE);
        tabLayout.getTabAt(1).setCustomView(tabLinearLayout);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) tab.setCustomView(R.layout.view_home_tab);
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {

                    case 0:
                       /* TextView tv = (TextView) tabLayout.getChildAt(position).findViewById(tab);
                        tv.setTextColor(Color.parseColor("#e82351"));
                        TextView tv1 = (TextView) tabLayout.getChildAt(1).findViewById(tab);
                        tv1.setTextColor(Color.parseColor("#b5b5b5"));
*/

                        break;
                    case 1:
                      /*  TextView tv2 = (TextView) tabLayout.getChildAt(position).findViewById(tab);
                        tv2.setTextColor(Color.parseColor("#e82351"));
                        TextView tv3 = (TextView) tabLayout.getChildAt(0).findViewById(tab);
                        tv3.setTextColor(Color.parseColor("#b5b5b5"));*/
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }
}
