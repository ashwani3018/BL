package com.mobstac.thehindubusinessline.adapter;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.mobstac.thehindubusinessline.activity.AdsBaseActivity;
import com.mobstac.thehindubusinessline.fragments.PortfolioArticleDetailFragment;
import com.mobstac.thehindubusinessline.model.databasemodel.ArticleDetail;

import java.util.List;

/**
 * Created by ARVIND on 8/27/2016.
 */

public class SlidingPortfolioArticleAdapter extends FragmentStatePagerAdapter {
    private String TAG = "SlidingArticleAdapter";
    private List<ArticleDetail> mArticleList;
    private boolean isFromPortfolioScreen;
    private AdsBaseActivity mMainActivity;

    public SlidingPortfolioArticleAdapter(AdsBaseActivity mainActivity, FragmentManager fm, List<ArticleDetail> mArticleList, boolean isFromPortfolioScreen) {
        super(fm);
        this.mArticleList = mArticleList;
        this.isFromPortfolioScreen = isFromPortfolioScreen;
        this.mMainActivity = mainActivity;
    }

    @Override
    public Fragment getItem(int position) {
        Log.i(TAG, "getItem: " + position);

        // Parallax, hide the toolbar before article click then article details screen show toolbar..
        mMainActivity.expandToolbar(true);

        return PortfolioArticleDetailFragment.newInstance(mArticleList.get(position).getAid(), mArticleList.get(position).getSid(),
                mArticleList.get(position).getAl(), true, mArticleList.get(position), isFromPortfolioScreen);
    }

    @Override
    public int getCount() {
        return mArticleList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mArticleList.get(position).getSname();
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
