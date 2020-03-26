package com.mobstac.thehindubusinessline.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.flurry.android.FlurryAgent;
import com.mobstac.thehindubusinessline.R;
import com.mobstac.thehindubusinessline.database.ApiCallHandler;
import com.mobstac.thehindubusinessline.fragments.SlidingPortfolioSectionFragment;
import com.mobstac.thehindubusinessline.utils.AppUtils;
import com.mobstac.thehindubusinessline.utils.Constants;
import com.mobstac.thehindubusinessline.utils.GoogleAnalyticsTracker;
import com.mobstac.thehindubusinessline.utils.SharedPreferenceHelper;

public class PortfolioActivity extends AdsBaseActivity {

    private final String TAG = "BL_PortfolioActivity";

    @Override
    public int getLayoutRes() {
        return R.layout.activity_portfolio;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_bar);
        setBottomNavigationViewSelectedItem(R.id.menu_portfolio);
        AppUtils.disableShiftMode(mBottomNavigationView);
        setSupportActionBar(mToolbar);
        launchSlidingProtfolioSection();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setBottomNavigationViewSelectedItem(R.id.menu_portfolio);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_user:
                boolean isUserLoggedIn = SharedPreferenceHelper.getBoolean(this, Constants.PORTFOLIO_IS_USER_LOGIN, false);
                if (!isUserLoggedIn) {
                    startActivity(new Intent(this, LoginActivity.class));
                } else {
                    startActivity(new Intent(this, UserProfileActivity.class));
                }
                GoogleAnalyticsTracker.setGoogleAnalyticsEvent(this, getString(R.string.ga_action), "User Profile: User Profile Button Clicked", "Portfolio Activity");
                FlurryAgent.logEvent("User Profile: User Profile Button Clicked");
                return true;
            case R.id.search:
                GoogleAnalyticsTracker.setGoogleAnalyticsEvent(this, getString(R.string.ga_action), "Search: Search Button Clicked ", "Main Activity");
                FlurryAgent.logEvent("Search: Search Button clicked");
                Intent intent =new Intent(this, SearchActivity.class);
                intent.putExtra("search_key", true);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.portfolio_menu, menu);
        setToolbarBackButton(R.mipmap.arrow_back);
        createOverFlowMenuOption(menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        setToolbarBackButton(R.mipmap.arrow_back);
        return super.onPrepareOptionsMenu(menu);

    }

    SlidingPortfolioSectionFragment fragment;

    private void launchSlidingProtfolioSection() {
        fragment = SlidingPortfolioSectionFragment.newInstance( 0, "   Portfolio", ApiCallHandler.FROM_PORTFOLIO);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.add(R.id.FRAME_CONTENT, fragment, null)
                .addToBackStack(null)
                .commit();
    }

    public void changeFragmenAtIndex(int pos) {
        fragment.changePagerAtIndex(pos);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }
}