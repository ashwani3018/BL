package com.mobstac.thehindubusinessline.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.mobstac.thehindubusinessline.R;
import com.mobstac.thehindubusinessline.adapter.MergedBookmarkPagerAdapter;
import com.ns.activity.BaseAcitivityTHP;
import com.ns.contentfragment.AppTabFragment;
import com.ns.utils.THPFirebaseAnalytics;
import com.ns.view.text.CustomTextView;

public class BookmarkMergedActivity extends BaseAcitivityTHP {

    String mUserId = "";
    boolean mIsOnline = true;
    int mSize = 10;

    private TabLayout tabLayout;
    private ViewPager bookmarkViewPager;
    private MergedBookmarkPagerAdapter pagerAdapter;

    @Override
    public int layoutRes() {
        return R.layout.activity_tab_bookmark_merged;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getIntent() != null) {
            mUserId = getIntent().getStringExtra("userId");
        }

        CustomTextView title_tv = findViewById(R.id.title_tv_merged);
        title_tv.setText("Read Later");

        bookmarkViewPager = findViewById(R.id.bookmarkViewPager);
        tabLayout = findViewById(R.id.tabLayout);

        pagerAdapter = new MergedBookmarkPagerAdapter(getSupportFragmentManager(), mUserId, mIsDayTheme);
        bookmarkViewPager.setAdapter(pagerAdapter);

        tabLayout.setupWithViewPager(bookmarkViewPager, true);

        // Iterate over all tabs and set the custom view
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(pagerAdapter.getTabView(i, this));
        }

        // To select default tab
        pagerAdapter.SetOnSelectView(this, tabLayout, 0);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                pagerAdapter.SetOnSelectView(BookmarkMergedActivity.this, tabLayout, pos);
                bookmarkViewPager.setCurrentItem(pos);
                THPFirebaseAnalytics.setFirbaseAnalyticsScreenRecord(BookmarkMergedActivity.this, "Read Later : " + tab.getText(), AppTabFragment.class.getSimpleName());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                pagerAdapter.SetUnSelectView(BookmarkMergedActivity.this, tabLayout, pos);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // Back button click listener
        findViewById(R.id.backBtn).setOnClickListener(v->{
            finish();
        });



    }


    @Override
    protected void onResume() {
        super.onResume();
//        AppFirebaseAnalytics.setFirbaseAnalyticsScreenRecord(BookmarkMergedActivity.this, "BookmarkMergedActivity Screen", BookmarkMergedActivity.class.getSimpleName());

    }
}
