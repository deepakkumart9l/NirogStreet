package com.app.nirogstreet.uttil;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.app.nirogstreet.fragments.AboutCommunities;
import com.app.nirogstreet.fragments.About_Fragment;
import com.app.nirogstreet.fragments.Communication_Feed_Fragment;
import com.app.nirogstreet.fragments.TimeLineFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Preeti on 02-11-2017.
 */

public class CustomPagerCommunitiesAdapter extends FragmentStatePagerAdapter

{
    TabLayout tabLayout;
    String groupId;
    Context context;
    private final List<String> tabTitles = new ArrayList<String>() {{
        add("normal");
        add("medium");
        add("etc");
    }};

    private List<Fragment> tabs = new ArrayList<>();

    public CustomPagerCommunitiesAdapter(FragmentManager fragmentManager, TabLayout tabLayout, Context context,String groupId) {
        super(fragmentManager);
        this.tabLayout = tabLayout;
        this.context = context;
        this.groupId=groupId;
        initializeTabs();
    }

    private void initializeTabs() {
        tabs.add(HostFragment.newInstance(new Communication_Feed_Fragment().getInstance(groupId)));
        tabs.add(HostFragment.newInstance(new About_Fragment().getInstance(groupId)));

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


