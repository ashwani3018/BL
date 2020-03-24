package com.mobstac.thehindubusinessline.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mobstac.thehindubusinessline.fragments.ForexFragment;
import com.mobstac.thehindubusinessline.fragments.GoldFragment;
import com.mobstac.thehindubusinessline.fragments.StockDetailsFragment;
import com.mobstac.thehindubusinessline.model.IndicesSection;

import java.util.List;

/**
 * Created by 9920 on 11/9/2017.
 */

public class IndicesTabViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<IndicesSection> mIndicesSection;

    public IndicesTabViewPagerAdapter(FragmentManager fm, List<IndicesSection> mIndicesSection) {
        super(fm);
        this.mIndicesSection = mIndicesSection;
    }

    @Override
    public Fragment getItem(int position) {

        switch (mIndicesSection.get(position).getViewType()) {
            case 0:
                return StockDetailsFragment.newInstance(0, "percentageChange", "desc");
            case 1:
                return StockDetailsFragment.newInstance(1, "percentageChange", "desc");
            case 2:
                return new ForexFragment();
            case 3:
                return new GoldFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return mIndicesSection.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mIndicesSection.get(position).getSectionName();
    }
}
