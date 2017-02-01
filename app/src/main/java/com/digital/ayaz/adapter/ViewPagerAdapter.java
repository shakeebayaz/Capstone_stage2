package com.digital.ayaz.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.digital.ayaz.fragment.CafeFragment;
import com.digital.ayaz.fragment.Restaurants;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    CharSequence Titles[];
    public ViewPagerAdapter(FragmentManager fm, CharSequence mTitles[]) {
        super(fm);
        this.Titles = mTitles;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                CafeFragment cafeFragment = new CafeFragment();
                return cafeFragment;

            case 1:
                Restaurants restaurants = new Restaurants();
                return restaurants;
            default:
                return null;
        }

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }
    @Override
    public int getCount() {
        return 2;
    }
}