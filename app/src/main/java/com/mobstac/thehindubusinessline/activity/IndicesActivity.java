package com.mobstac.thehindubusinessline.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.flurry.android.FlurryAgent;
import com.mobstac.thehindubusinessline.R;
import com.mobstac.thehindubusinessline.adapter.IndicesTabViewPagerAdapter;
import com.mobstac.thehindubusinessline.model.IndicesSection;
import com.mobstac.thehindubusinessline.utils.AppUtils;
import com.mobstac.thehindubusinessline.utils.GoogleAnalyticsTracker;
import com.mobstac.thehindubusinessline.utils.NetworkUtils;
import com.mobstac.thehindubusinessline.view.CustomTabLayout;
import com.mobstac.thehindubusinessline.view.CustomViewPager;
import com.ns.utils.IntentUtil;
import com.ns.utils.NetUtils;
import com.ns.utils.THPConstants;

import java.util.ArrayList;
import java.util.List;

public class IndicesActivity extends AdsBaseActivity {
    private final String TAG = "BL_IndicesActivity";
    private CustomViewPager mViewPager;
    private CustomTabLayout mTabLayout;
    private IndicesTabViewPagerAdapter mIndicesTabViewPagerAdapter;
    private int selectedTabPosition = 0;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_indices;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_bar);
        setBottomNavigationViewSelectedItem(R.id.menu_market);
        AppUtils.disableShiftMode(mBottomNavigationView);
        setSupportActionBar(mToolbar);
        hideToolbarLogo();
        mToolbar.setTitle("   Indices");
        mToolbar.setSubtitle("");
        if (getIntent().hasExtra("selectedPosition")) {
            selectedTabPosition = getIntent().getIntExtra("selectedPosition", 0);
        }
        List<IndicesSection> mIndicesSection = new ArrayList<>();
        mIndicesSection.add(new IndicesSection(0, "BSE"));
        mIndicesSection.add(new IndicesSection(1, "NSE"));
        mIndicesSection.add(new IndicesSection(2, "Currency"));
        mIndicesSection.add(new IndicesSection(3, "Commodities"));

        mViewPager = (CustomViewPager) findViewById(R.id.viewpager);
        mTabLayout = (CustomTabLayout) findViewById(R.id.tabs);
        mIndicesTabViewPagerAdapter = new IndicesTabViewPagerAdapter(getSupportFragmentManager(), mIndicesSection);
        mViewPager.setAdapter(mIndicesTabViewPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(selectedTabPosition, true);
        if (!NetworkUtils.isNetworkAvailable(this)) {
            showSnackBar(mViewPager);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setBottomNavigationViewSelectedItem(R.id.menu_market);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG, "onCreateOptionsMenu: ");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.portfolio_menu, menu);
        createOverFlowMenuOption(menu);
        hideHomeButton();
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        hideHomeButton();
        List<Integer> mShowMenues = new ArrayList<>();
        mShowMenues.add(R.id.search);
        mShowMenues.add(R.id.action_overflow);
        showMenus(mShowMenues, menu);
        if (mToolbar != null) {
            mToolbar.setTitle("   Indices");
            mToolbar.setSubtitle("");
        }
        hideToolbarLogo();
        return super.onPrepareOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_launch_crown:
                if(!NetUtils.isConnected(IndicesActivity.this)) {
                    showSnackBar(findViewById(R.id.FRAME_CONTENT));
                    return true;
                }
                THPConstants.FLOW_TAB_CLICK = THPConstants.TAP_CROWN;
                // Open Subscription page
                IntentUtil.openTHPScreen(IndicesActivity.this, isUserLoggedIn(), -1, isHasFreePlan(), isHasSubscriptionPlan(), THPConstants.TAP_CROWN);
                return true;

            case android.R.id.home:
                FlurryAgent.logEvent(getString(R.string.ga_sidebar_category));
                GoogleAnalyticsTracker.setGoogleAnalyticsEvent(this, getString(R.string.ga_action), getString(R.string.ga_sidebar_category), "Section Fragment");
                finish();
                return true;
            case R.id.search:
                GoogleAnalyticsTracker.setGoogleAnalyticsEvent(this, getString(R.string.ga_action), "Search: Search Button Clicked ", "Main Activity");
                FlurryAgent.logEvent("Search: Search Button clicked");
                Intent intent =new Intent(this, SearchActivity.class);
                intent.putExtra("search_key", false);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

