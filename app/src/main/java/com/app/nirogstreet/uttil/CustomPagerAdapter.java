package com.app.nirogstreet.uttil;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.app.nirogstreet.activites.MainActivity;
import com.app.nirogstreet.fragments.BlogsFragment;
import com.app.nirogstreet.fragments.ProfileFragment;

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
        tabs.add(HostFragment.newInstance(new ProfileFragment()));
        tabs.add(HostFragment.newInstance(new BlogsFragment()));

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
