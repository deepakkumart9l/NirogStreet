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
import com.app.nirogstreet.circularprogressbar.CircularProgressBar;
import com.app.nirogstreet.uttil.CustomPagerAdapter;
import com.app.nirogstreet.uttil.CustomPagerCommunitiesAdapter;
import com.app.nirogstreet.uttil.ImageLoader;
import com.app.nirogstreet.uttil.LetterTileProvider;
import com.app.nirogstreet.uttil.TypeFaceMethods;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.makeramen.roundedimageview.RoundedImageView;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.app.nirogstreet.R.id.tab;

/**
 * Created by Preeti on 02-11-2017.
 */

public class CommunitiesDetails extends AppCompatActivity {
    CustomPagerCommunitiesAdapter customPagerCommunitiesAdapter;
    TabLayout tabLayout;
    ViewPager viewPager;
    static TextView textTab;
    String groupId;
    boolean openMain=false;
    public static ImageView moreImageView;
    static LetterTileProvider mLetterTileProvider;
    public static RoundedImageView circleImageView;
    ImageView backImageView;
    public  static CircleImageView proo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.communication_detail);
        proo=(CircleImageView)findViewById(R.id.proo);
        if(getIntent().hasExtra("openMain"))
        {
            openMain=getIntent().getBooleanExtra("openMain",false);
        }
        textTab = (TextView) findViewById(R.id.textTab);
        backImageView = (ImageView) findViewById(R.id.back);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(openMain)
                {
                    Intent intent1 = new Intent(CommunitiesDetails.this, MainActivity.class);
                    startActivity(intent1);
                    finish();
                }
                finish();
            }
        });
        mLetterTileProvider = new LetterTileProvider(CommunitiesDetails.this);

        circleImageView = (RoundedImageView) findViewById(R.id.pro);
        moreImageView = (ImageView) findViewById(R.id.more);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        viewPager.setOffscreenPageLimit(2);
        if (getIntent().hasExtra("groupId")) {
            groupId = getIntent().getStringExtra("groupId");
        }
        customPagerCommunitiesAdapter = new CustomPagerCommunitiesAdapter(getSupportFragmentManager(), tabLayout, this, groupId);
        // increase this limit if you have more tabs!
        //viewPager.setOffscreenPageLimit(2);

        viewPager.setAdapter(customPagerCommunitiesAdapter);
        tabLayout.setupWithViewPager(viewPager);
  /*  tabLayout.addTab(tabLayout.newTab().setText("COMMUNICATIONS"));
        tabLayout.addTab(tabLayout.newTab().setText("ABOUT"));*/
        LinearLayout tabLinearLayoutTimeline = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custum_comm, null);

        TextView tabOneTimeline = (TextView) tabLinearLayoutTimeline.findViewById(tab);
        tabOneTimeline.setText("FEED");
        tabOneTimeline.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        ImageView oneImgTimeline = (ImageView) tabLinearLayoutTimeline.findViewById(R.id.icon);
        oneImgTimeline.setImageResource(R.drawable.home);
        oneImgTimeline.setVisibility(View.GONE);
        tabLayout.getTabAt(0).setCustomView(tabLinearLayoutTimeline);
        // tabLayout.setTabTextColors(Color.parseColor("#727272"), Color.parseColor("#ffffff"));

        LinearLayout tabLinearLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custum_comm, null);

        TextView tabOne = (TextView) tabLinearLayout.findViewById(tab);
        tabOne.setText("ABOUT");

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

    @Override
    public void onBackPressed() {
        if(openMain)
        {
            Intent intent1 = new Intent(CommunitiesDetails.this, MainActivity.class);
            startActivity(intent1);
            finish();
        }
        super.onBackPressed();
    }

    public static void setNameAndCoverPic(String name, String url) {
        textTab.setText(name);

    }
    // ImageLoader imageLoader=new ImageLoader(CommunitiesDetails.this);

    private static void setBanner(String url) {
        if (url != null && !url.contains("banner-default") && !url.contains("tempimages")) {
           /* Glide.with(CommunitiesDetails.this)
                    .load(url) // Uri of the picture
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .crossFade()
                    .override(100, 100)
                    .into(circleImageView);*/


            // imageLoader1.getInstance().displayImage(groupModel.getGroupBanner(),  holder.groupIconImageView, defaultOptions);
        } else {
            circleImageView.setImageBitmap(mLetterTileProvider.getLetterTile(url));

        }
    }
}
