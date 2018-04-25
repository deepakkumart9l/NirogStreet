package com.app.nirogstreet.uttil;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.app.nirogstreet.activites.CommunitiesDetails;
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
    int user_fromLink;
    String refer_userId;
    private final List<String> tabTitles = new ArrayList<String>() {{
        add("normal");
        add("medium");
        add("etc");
    }};

    private List<Fragment> tabs = new ArrayList<>();

    public CustomPagerCommunitiesAdapter(FragmentManager fragmentManager, TabLayout tabLayout, Context context,String groupId,int user_fromLink,String refer_userId) {
        super(fragmentManager);
        this.tabLayout = tabLayout;
        this.context = context;
        this.groupId=groupId;
        this.user_fromLink=user_fromLink;
        this.refer_userId=refer_userId;


        initializeTabs();
    }



    private void initializeTabs() {
        tabs.add(HostFragment.newInstance(new Communication_Feed_Fragment().getInstance(groupId,user_fromLink)));
        tabs.add(HostFragment.newInstance(new About_Fragment().getInstance(groupId,user_fromLink,refer_userId,user_fromLink)));

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


