package com.mobstac.thehindubusinessline.adapter;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.mobstac.thehindubusinessline.R;
import com.mobstac.thehindubusinessline.fragments.BookmarksFragment;
import com.netoperation.util.NetConstants;
import com.ns.contentfragment.THP_BookmarksFragment;

public class MergedBookmarkPagerAdapter extends FragmentStatePagerAdapter {

    private String mUserId;

    private String[] mTabTitle = {"Subscription", "Non Subscription"};
    private boolean mIsDayTheme;

    public MergedBookmarkPagerAdapter(FragmentManager fm, String userId, boolean isDayTheme) {
        super(fm);
        mUserId = userId;
        mIsDayTheme = isDayTheme;
    }

    @Override
    public Fragment getItem(int i) {
        if(i==0) {
            return THP_BookmarksFragment.getInstance(mUserId);
        }
        else {
            return BookmarksFragment.getInstance(NetConstants.RECO_bookmarks);
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTabTitle[position];
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

    public View getTabView(int position, Context context) {
        String tilte = mTabTitle[position];
        View v = LayoutInflater.from(context).inflate(R.layout.custom_tab_bookmark_merged, null);
        TextView tv = v.findViewById(com.ns.thpremium.R.id.textView);
        tv.setText(tilte);
        return v;
    }

    public void SetOnSelectView(Context context, TabLayout tabLayout, int position) {
        TabLayout.Tab tab = tabLayout.getTabAt(position);
        View selected = tab.getCustomView();
        TextView iv_text = selected.findViewById(R.id.textView);
        if(mIsDayTheme) {
            iv_text.setTextColor(context.getResources().getColor(com.ns.thpremium.R.color.boldBlackColor));
        } else {
            iv_text.setTextColor(context.getResources().getColor(com.ns.thpremium.R.color.dark_color_static_text));
        }
    }

    public void SetUnSelectView(Context context, TabLayout tabLayout, int position) {
        TabLayout.Tab tab = tabLayout.getTabAt(position);
        View selected = tab.getCustomView();
        TextView iv_text = selected.findViewById(R.id.textView);
        iv_text.setTextColor(context.getResources().getColor(com.ns.thpremium.R.color.greyColor_1));
    }
}
