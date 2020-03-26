package com.mobstac.thehindubusinessline.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.RelativeLayout;

import com.facebook.AccessToken;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.mobstac.thehindubusinessline.Businessline;
import com.mobstac.thehindubusinessline.R;
import com.mobstac.thehindubusinessline.model.BookmarkBean;
import com.mobstac.thehindubusinessline.utils.Constants;
import com.mobstac.thehindubusinessline.utils.DFPConsent;
import com.mobstac.thehindubusinessline.utils.SharedPreferenceHelper;
import com.netoperation.net.ApiManager;
import com.netoperation.util.THPPreferences;
import com.ns.alerts.Alerts;
import com.ns.utils.CallBackRelogin;
import com.ns.utils.IntentUtil;
import com.ns.utils.NetUtils;
import com.ns.utils.THPConstants;
import com.twitter.sdk.android.core.TwitterCore;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.realm.Realm;

/**
 * Created by arvind on 20/9/16.
 */
public abstract class AdsBaseActivity extends BaseActivity {
    private final String TAG = "AdsBaseActivity";
    public Toolbar mToolbar;
    private View mHomeBottomLine;
    private RelativeLayout mBottomAdContainer;
    private PublisherAdView mBannerPublisherAdView;
    private PublisherInterstitialAd mPublisherInterstitialAd;
    private List<BookmarkBean> mUnBookmarkedLsit;
    private AppBarLayout mAppBarLayout;
    private CoordinatorLayout mRootLayout;



    public abstract int getLayoutRes();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutRes());
        initView();
        Log.i(TAG, "onCreate: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mBannerPublisherAdView != null) {
            mBannerPublisherAdView.resume();
        }
        Log.i(TAG, "onResume: ");
    }

    @Override
    protected void onPause() {
        if (mBannerPublisherAdView != null) {
            mBannerPublisherAdView.pause();
        }

        super.onPause();
        Log.i(TAG, "onPause: ");
    }

    @Override
    protected void onDestroy() {
        if (mBannerPublisherAdView != null) {
            mBannerPublisherAdView.destroy();
        }
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
    }

    @Override
    public void finish() {
        super.finish();
        Log.i(TAG, "finish: ");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.i(TAG, "onBackPressed: ");
    }

    private void initView() {
        mBottomAdContainer = (RelativeLayout) findViewById(R.id.homeBottomAdHolder);
        mHomeBottomLine = findViewById(R.id.homeBottomLine);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mRootLayout = (CoordinatorLayout) findViewById(R.id.rootLayout);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.appBarLayoutId);
    }

    // Parallax, show the toolbar
    public void expandToolbar(boolean show){

        if (show) {
         //   setToolbarTitle("");
            hideToolbarLogo();
        }


//        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) mAppBarLayout.getLayoutParams();
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) mAppBarLayout.getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        if(behavior!=null) {
            behavior.setTopAndBottomOffset(0);
            behavior.onNestedPreScroll(mRootLayout, mAppBarLayout, null, 0, 1, new int[2]);
        }
    }

    public void loadFullScreenAds(boolean isFromArticle) {
        // If user is in Europe, and selected "Pay for the ad-free version" then ads should not display
        if(SharedPreferenceHelper.isUserPreferAdsFree(this)) {
            return;
        }
        mPublisherInterstitialAd = new PublisherInterstitialAd(this);
        mPublisherInterstitialAd.setAdUnitId(getString(R.string.dfp_interstitial));

        mPublisherInterstitialAd.loadAd(DFPConsent.getDefaultUrlAdsRequest(this));

        mPublisherInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Log.i("Ads", "onAdLoaded");
                mPublisherInterstitialAd.show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Log.i("Ads", "onAdFailedToLoad");
            }

            @Override
            public void onAdOpened() {
                Log.i("Ads", "onAdOpened");
            }

            @Override
            public void onAdLeftApplication() {
                Log.i("Ads", "onAdLeftApplication");
            }

            @Override
            public void onAdClosed() {
                Log.i("Ads", "onAdClosed");
            }
        });

    }

    public void createBannerAdRequest(boolean isFromRestOfScreen, boolean isFromArticlePage, String contentUrl) {
        // If user is in Europe, and selected "Pay for the ad-free version" then ads should not display
        if(SharedPreferenceHelper.isUserPreferAdsFree(this)) {
            return;
        }

        /*if(contentUrl == null || contentUrl.isEmpty()) {
            contentUrl = "https://www.thehindubusinessline.com/economy/budget/feeder/";
        }*/
        mBannerPublisherAdView = null;
        hideBottomAdView();
        mBannerPublisherAdView = new PublisherAdView(this);
        mBannerPublisherAdView.setAdSizes(AdSize.BANNER);
        String unitId = getUnitId(isFromRestOfScreen, isFromArticlePage);
        Log.i("Ads", "Unit ID: " + unitId);
        mBannerPublisherAdView.setAdUnitId(unitId);
        mBannerPublisherAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Log.i("Ads", "onAdLoaded");
                mBottomAdContainer.removeAllViews();
                mBottomAdContainer.setVisibility(View.VISIBLE);
                mBottomAdContainer.addView(mBannerPublisherAdView);
                mHomeBottomLine.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Log.i("Ads", "onAdFailedToLoad" + errorCode);
                mHomeBottomLine.setVisibility(View.GONE);
                mBottomAdContainer.setVisibility(View.GONE);

            }

            @Override
            public void onAdOpened() {
                Log.i("Ads", "onAdOpened");
            }

            @Override
            public void onAdLeftApplication() {
                Log.i("Ads", "onAdLeftApplication");
            }

            @Override
            public void onAdClosed() {
                Log.i("Ads", "onAdClosed");
            }
        });
        String url;
        if (contentUrl != null && !TextUtils.isEmpty(contentUrl)) {
            url = contentUrl;
        } else {
            url = Constants.THE_HINDU_URL;
        }
        try {
            mBannerPublisherAdView.loadAd(DFPConsent.getCustomUrlAdsRequest(this, url));
        } catch (Exception e) {
            mBannerPublisherAdView.loadAd(DFPConsent.getNoUrlAdsRequest(this));
        }
    }

    private String getUnitId(boolean isFromRestOfScreen, boolean isFromArticlePage) {
        // sticky change for ravi 29nov2018
        return isFromRestOfScreen ? (isFromArticlePage ? getString(R.string.dfp_all_sticky) : getString(R.string.dfp_all_sticky)) : getString(R.string.dfp_all_sticky);
//        return isFromRestOfScreen ? (isFromArticlePage ? getString(R.string.dfp_article_sticky) : getString(R.string.dfp_ros_banner)) : getString(R.string.dfp_home_sticky);
    }

    public void hideBottomAdView() {
        if (mBottomAdContainer != null) {
            mBottomAdContainer.removeAllViews();
            mBottomAdContainer.setVisibility(View.GONE);
        }
        if (mHomeBottomLine != null) {
            mHomeBottomLine.setVisibility(View.GONE);
        }
    }

    public void hideToolbarLogo() {
        if (mToolbar != null) {
            mToolbar.setLogo(null);
        }
    }

    public void setToolbarLogo(int id) {
        if (mToolbar != null) {
            mToolbar.setLogo(id);
        }
    }

    public void setToolbarBackButton(@DrawableRes int resId) {
        if (mToolbar != null) {
            mToolbar.setNavigationIcon(resId);
            mToolbar.setLogo(null);
        }
    }

    public void hideHomeButton() {
        if (mToolbar != null) {
            mToolbar.setNavigationIcon(null);
        }
    }

    public void setToolbarHomeButton() {
        if (mToolbar != null) {
            mToolbar.setNavigationIcon(R.mipmap.menu);
        }
    }

    public void setToolbarTitle(String title) {
        if (mToolbar != null) {
            mToolbar.setTitle(title);
        }
    }

    public void setToolbarBackground(int color) {
        if (mToolbar != null) {
            mToolbar.setBackgroundColor(color);
        }
    }

    public void showMenus(List<Integer> mItemList, Menu menu) {
        if (menu != null) {
            for (Integer menuItem : mItemList) {
                menu.findItem(menuItem).setVisible(true);
            }
        }
    }

    public boolean checkUnBookmarkList(BookmarkBean bean) {
        if (mUnBookmarkedLsit != null) {
            return mUnBookmarkedLsit.contains(bean);
        } else {
            return false;
        }
    }

    public void deleteUnBookmarkedArticleFromDatabase() {
        if (mUnBookmarkedLsit != null && mUnBookmarkedLsit.size() > 0) {
            Realm mRealm = Businessline.getRealmInstance();
            for (BookmarkBean bean : mUnBookmarkedLsit) {
                mRealm.beginTransaction();
                bean.deleteFromRealm();
                mRealm.commitTransaction();
            }
            mUnBookmarkedLsit.clear();
        }
    }

    public void unBookmarkedList(BookmarkBean bean) {
        if (mUnBookmarkedLsit == null) {
            mUnBookmarkedLsit = new ArrayList<BookmarkBean>();
        }
        if (mUnBookmarkedLsit.contains(bean)) {
            mUnBookmarkedLsit.remove(bean);
        } else {
            mUnBookmarkedLsit.add(bean);
        }
    }



}
