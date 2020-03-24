package com.mobstac.thehindubusinessline.adapter;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mobstac.thehindubusinessline.activity.AdsBaseActivity;
import com.mobstac.thehindubusinessline.fragments.SectionLandingPortfolioFragment;
import com.mobstac.thehindubusinessline.model.SectionTable;

import io.realm.OrderedRealmCollection;

/**
 * Created by ARVIND on 8/27/2016.
 */

public class SlidingSectionFragmentAdapter extends FragmentStatePagerAdapter {
    private OrderedRealmCollection<SectionTable> mSectionTables;
    private boolean isFromPortfolioScreen;
    private AdsBaseActivity mMainActivity;

    public SlidingSectionFragmentAdapter(AdsBaseActivity mainActivity, FragmentManager fm, OrderedRealmCollection<SectionTable> mSectionTables, boolean isFromPortfolioScreen) {
        super(fm);
        this.mSectionTables = mSectionTables;
        this.isFromPortfolioScreen = isFromPortfolioScreen;
        this.mMainActivity = mainActivity;
    }

    @Override
    public Fragment getItem(int position) {

        return SectionLandingPortfolioFragment.newInstance(mSectionTables.get(position).getSecId(),
                mSectionTables.get(position).getType(),
                mSectionTables.get(position).getSecName(), mSectionTables.get(position).getLink(), isFromPortfolioScreen,
                mSectionTables.get(position).getStaticPageUrl(), mSectionTables.get(position).isStaticPageEnable(),
                mSectionTables.get(position).getPositionInListView());

    }

    @Override
    public int getCount() {
        return mSectionTables.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mSectionTables.get(position).getSecName();
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
