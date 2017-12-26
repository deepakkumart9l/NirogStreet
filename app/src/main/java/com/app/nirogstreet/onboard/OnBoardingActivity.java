package com.app.nirogstreet.onboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.nirogstreet.R;
import com.app.nirogstreet.activites.LoginActivity;
import com.app.nirogstreet.activites.MainActivity;
import com.app.nirogstreet.activites.Splash;
import com.app.nirogstreet.uttil.SesstionManager;

import java.util.ArrayList;

public class OnBoardingActivity extends AppCompatActivity {


    private LinearLayout pager_indicator;
    private int dotsCount;
    private ImageView[] dots;


    private ViewPager onboard_pager;

    private OnBoard_Adapter mAdapter;

    private Button btn_get_started;

    int previous_pos = 0;
    boolean flag, onboard_flag;

    ArrayList<OnBoardItem> onBoardItems = new ArrayList<>();
    SesstionManager sesstionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);
        sesstionManager = new SesstionManager(OnBoardingActivity.this);
        flag = false;
        btn_get_started = (Button) findViewById(R.id.btn_get_started);
        onboard_pager = (ViewPager) findViewById(R.id.pager_introduction);
        pager_indicator = (LinearLayout) findViewById(R.id.viewPagerCountDots);

        SharedPreferences sharedPref = this.getSharedPreferences("onboardDetail", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        onboard_flag = sharedPref.getBoolean("onboard", false);

        if (onboard_flag == false) {
            firstOnBoard();
            flag = true;
            SharedPreferences sharedPref1 = getApplicationContext().getSharedPreferences("onboardDetail", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor1 = sharedPref1.edit();
            editor1.putBoolean("onboard", flag);
            editor1.commit();
        } else {
            Intent intent = new Intent(OnBoardingActivity.this, Splash.class);
            startActivity(intent);
        }
    }

    public void firstOnBoard() {
        loadData();
        mAdapter = new OnBoard_Adapter(this, onBoardItems);
        onboard_pager.setAdapter(mAdapter);
        onboard_pager.setCurrentItem(0);
        onboard_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                // Change the current position intimation

                for (int i = 0; i < dotsCount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(OnBoardingActivity.this, R.drawable.non_selected_item_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(OnBoardingActivity.this, R.drawable.selected_item_dot));


                int pos = position + 1;

                if (pos == dotsCount && previous_pos == (dotsCount - 1))
                    show_animation();
                else if (pos == (dotsCount - 1) && previous_pos == dotsCount)
                    hide_animation();

                previous_pos = pos;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btn_get_started.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sesstionManager.isUserLoggedIn()) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                }
            }
        });
        setUiPageViewController();
    }

    // Load data into the viewpager

    public void loadData() {

        int[] header = {R.string.ob_header1, R.string.ob_header2, R.string.ob_header3, R.string.ob_header4};
        int[] desc = {R.string.ob_des1, R.string.ob_des2, R.string.ob_des3, R.string.ob_des4};
        int[] imageId = {R.drawable.welcome, R.drawable.knowledge, R.drawable.community, R.drawable.connect};

        for (int i = 0; i < imageId.length; i++) {
            OnBoardItem item = new OnBoardItem();
            item.setImageID(imageId[i]);
            item.setTitle(getResources().getString(header[i]));
            item.setDescription(getResources().getString(desc[i]));

            onBoardItems.add(item);
        }
    }
    // Button bottomUp animation

    public void show_animation() {
        Animation show = AnimationUtils.loadAnimation(this, R.anim.slide_up_anim);

        btn_get_started.startAnimation(show);

        show.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                btn_get_started.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                btn_get_started.clearAnimation();

            }

        });


    }

    // Button Topdown animation

    public void hide_animation() {
        Animation hide = AnimationUtils.loadAnimation(this, R.anim.slide_down_anim);

        btn_get_started.startAnimation(hide);

        hide.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                btn_get_started.clearAnimation();
                btn_get_started.setVisibility(View.GONE);

            }

        });


    }

    // setup the
    private void setUiPageViewController() {

        dotsCount = mAdapter.getCount();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(OnBoardingActivity.this, R.drawable.non_selected_item_dot));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(6, 0, 6, 0);

            pager_indicator.addView(dots[i], params);
        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(OnBoardingActivity.this, R.drawable.selected_item_dot));
    }


}
