package com.ns.adapter;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.netoperation.util.NetConstants;
import com.ns.contentfragment.AppTabListingFragment;

public class AppTabPagerAdapter extends FragmentStatePagerAdapter {

    private String mUserId;
    private String[] tabNames;
    public AppTabPagerAdapter(FragmentManager fm, String userId, String[] tabNames) {
        super(fm);
        mUserId = userId;
        this.tabNames = tabNames;
    }

    @Override
    public Fragment getItem(int i) {
        /*if(i==0) {
            return AppTabListingFragment.getInstance(mUserId, NetConstants.BREIFING_ALL);
        }
        else if(i==1) {
            return AppTabListingFragment.getInstance(mUserId, NetConstants.RECO_personalised);
        }
        else if(i==2) {
            return AppTabListingFragment.getInstance(mUserId, NetConstants.RECO_suggested);
        }
        else {
            return null;
        }*/
        return AppTabListingFragment.getInstance(mUserId, NetConstants.RECO_personalised);
    }

    @Override
    public int getCount() {
        return 1;
    }



    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabNames[position];
    }

    @Override
    public Parcelable saveState() {
        Bundle bundle = (Bundle) super.saveState();
        if (bundle != null) {
            bundle.putParcelableArray("states", null);
            return bundle;
        }
        return super.saveState();
    }


}
