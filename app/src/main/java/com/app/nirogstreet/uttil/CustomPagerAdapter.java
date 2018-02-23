package com.app.nirogstreet.uttil;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.app.nirogstreet.activites.MainActivity;
import com.app.nirogstreet.fragments.BlogsFragment;
import com.app.nirogstreet.fragments.CommunitiesFragment;
import com.app.nirogstreet.fragments.Courses_Fragment;
import com.app.nirogstreet.fragments.Learning_Fragment;
import com.app.nirogstreet.fragments.MoreFragment;
import com.app.nirogstreet.fragments.ProfileFragment;
import com.app.nirogstreet.fragments.TimeLineFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Preeti on 05-10-2017.
 */
public class CustomPagerAdapter extends FragmentStatePagerAdapter {
    TabLayout tabLayout;
    Context context;
    private final List<String> tabTitles = new ArrayList<String>() {{
        add("normal");
        add("medium");
        add("etc");
    }};

    private List<Fragment> tabs = new ArrayList<>();

    public CustomPagerAdapter(FragmentManager fragmentManager, TabLayout tabLayout, Context context) {
        super(fragmentManager);
        this.tabLayout = tabLayout;
        this.context = context;
        initializeTabs();
    }

    private void initializeTabs() {
        tabs.add(HostFragment.newInstance(new TimeLineFragment()));
        tabs.add(HostFragment.newInstance(new Learning_Fragment()));
        tabs.add(HostFragment.newInstance(new CommunitiesFragment()));
        tabs.add(HostFragment.newInstance(new MoreFragment()));

    }

    @Override

    public Fragment getItem(int position) {


        return tabs.get(position);

    }

    @Override
    public int getCount() {
        return tabs.size();
    }
}
