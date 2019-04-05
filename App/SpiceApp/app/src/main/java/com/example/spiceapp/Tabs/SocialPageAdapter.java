package com.example.spiceapp.Tabs;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * Layout manager for the Social Tab Layout
 * Constructor takes a FragmentManager object
 * @author Ryan Simpson
 */

//Handler class for our TabLayout
public class SocialPageAdapter extends FragmentPagerAdapter {

    //List of Tab activitivies
    private final List<Fragment> mFragmentList = new ArrayList<>();
    //List of Tab names
    private final List<String> mFragmentTitleList = new ArrayList<>();

    //Get tab title
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

    //Add a new tab
    public void addFragment(Fragment fragment, String title){
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    //Constructor
    public SocialPageAdapter(FragmentManager fm) {
        super(fm);
    }

    //Get tab activity
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    //Get the number of tabs
    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}
