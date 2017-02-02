package com.digital.ayaz.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.digital.ayaz.fragment.MuseumFragment;
import com.digital.ayaz.fragment.NightClubFragment;

public class ViewPagerAdapterTourist extends FragmentPagerAdapter {

    CharSequence mTitle[];

    public ViewPagerAdapterTourist(FragmentManager fm, CharSequence mTitles[]) {
        super(fm);
        this.mTitle = mTitles;

    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                MuseumFragment museumFragment = new MuseumFragment();
                return museumFragment;
            case 1:
                NightClubFragment nightClubFragment = new NightClubFragment();
                return nightClubFragment;
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitle[position];
    }

    @Override
    public int getCount() {
        return 2;
    }
}