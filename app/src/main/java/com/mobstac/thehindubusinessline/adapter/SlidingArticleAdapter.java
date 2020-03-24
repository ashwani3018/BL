package com.mobstac.thehindubusinessline.adapter;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.mobstac.thehindubusinessline.activity.MainActivity;
import com.mobstac.thehindubusinessline.fragments.ArticleDetailFragment;
import com.mobstac.thehindubusinessline.model.databasemodel.ArticleDetail;

import java.util.List;

/**
 * Created by ARVIND on 8/27/2016.
 */

public class SlidingArticleAdapter extends FragmentStatePagerAdapter {
    private String TAG = "SlidingArticleAdapter";
    private List<ArticleDetail> mArticleList;
    private MainActivity mMainActivity;

    public SlidingArticleAdapter(FragmentManager fm, List<ArticleDetail> mArticleList, MainActivity mainActivity) {
        super(fm);
        this.mArticleList = mArticleList;
        this.mMainActivity = mainActivity;
    }

    @Override
    public Fragment getItem(int position) {
        Log.i(TAG, "getItem: " + position);
        // Parallax, hide the toolbar before article click then article details screen show toolbar..
        mMainActivity.expandToolbar(true);
        return ArticleDetailFragment.newInstance(mArticleList.get(position).getAid(), mArticleList.get(position).getSid(), mArticleList.get(position).getAl(), true, mArticleList.get(position));
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
