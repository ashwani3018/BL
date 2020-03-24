package com.mobstac.thehindubusinessline.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mobstac.thehindubusinessline.R;
import com.mobstac.thehindubusinessline.fragments.CommentsListFragment;
import com.mobstac.thehindubusinessline.fragments.PostCommentFragment;

/**
 * Created by ashwani on 5/22/17.
 */

public class CommentActivity extends AdsBaseActivity {
    private String TAG = "CommentActivity";
    public static final String ARTICLE_ID = "article_id";
    public static final String ARTICLE_TIME = "article_time";
    public static final String ARTICLE_LINK = "article_link";
    public static final String ARTICLE_TITLE = "article_title";
    public static final String COMMENT_COUNT = "comment_count";
    public static final String IS_POST_COMMENT = "is_post_comment";
    public static final String IMAGE_URL = "imgUrl";

    private String mArticleID;
    private String mArticleTime;
    private String mArticleLink;
    private String mCommentCount;
    private String mArticleTitle;
    private boolean isPostScreen;
    private FrameLayout mFrameLayout;
    private String mImgUrl;

    //    private VmaxAdView mBottomBannerAdView;
//    private VmaxAdListener mBottomBannerAdListener;
    private View mHomeBottomLine;
    private Toolbar mToolbar;

    CommentsListFragment fragment;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_detail;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            mArticleID = getIntent().getStringExtra(ARTICLE_ID);
            mArticleTitle = getIntent().getStringExtra(ARTICLE_TITLE);
            mArticleLink = getIntent().getStringExtra(ARTICLE_LINK);
            mArticleTime = getIntent().getStringExtra(ARTICLE_TIME);
            mImgUrl = getIntent().getStringExtra(IMAGE_URL);
            isPostScreen = getIntent().getBooleanExtra(IS_POST_COMMENT, false);
        }

        if (!isPostScreen) {
            mCommentCount = getIntent().getStringExtra(COMMENT_COUNT);
        }
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitle(null);
        mToolbar.setNavigationIcon(R.mipmap.arrow_back);
        mToolbar.setLogo(null);

//        mBottomBannerAdView = (VmaxAdView) findViewById(R.id.homeBottomAdHolder);
        mHomeBottomLine = findViewById(R.id.homeBottomLine);
        mFrameLayout = (FrameLayout) findViewById(R.id.parentLayout);

        addDetailFragment();

    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (mBottomBannerAdView != null) {
//            mBottomBannerAdView.onResume();
//        }
    }

    @Override
    protected void onPause() {
//        if (mBottomBannerAdView != null) {
//            mBottomBannerAdView.onPause();
//        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
//        if (mBottomBannerAdView != null) {
//            mBottomBannerAdView.onDestroy();
//        }
        super.onDestroy();

    }

    @Override
    public void finish() {
//        if (mBottomBannerAdView != null) {
//            mBottomBannerAdView.finish();
//        }
        super.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setToolbarTitle(String title) {
        if (mToolbar != null) {
            mToolbar.setTitle(title);
        }
    }

    private void addDetailFragment() {
        if (isPostScreen) {
            PostCommentFragment postCommentFragment = PostCommentFragment.newInstance(mArticleID, mArticleTitle, mArticleLink, mArticleTime);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.FRAME_CONTENT, postCommentFragment).commit();
        } else {
             fragment = CommentsListFragment.getInstance(mArticleID, mCommentCount, mArticleTitle,
                    mArticleLink, mArticleTime, mImgUrl);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.FRAME_CONTENT, fragment).commit();
        }

    }


    public void showSnackBar() {
        Snackbar mSnackbar = Snackbar.make(mFrameLayout, "", BaseTransientBottomBar.LENGTH_LONG);
        Snackbar.SnackbarLayout mSnackbarView = (Snackbar.SnackbarLayout) mSnackbar.getView();
        mSnackbarView.setBackgroundColor(Color.BLACK);
        TextView textView = (TextView) mSnackbarView.findViewById(android.support.design.R.id.snackbar_text);
        TextView textView1 = (TextView) mSnackbarView.findViewById(android.support.design.R.id.snackbar_action);
        textView.setVisibility(View.INVISIBLE);
        textView1.setVisibility(View.INVISIBLE);
        View snackView = getLayoutInflater().inflate(R.layout.snackbar_layout, null);
        snackView.findViewById(R.id.action_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                startActivity(intent);
            }
        });
        mSnackbarView.addView(snackView);
        mSnackbar.show();
    }

    public void addFragmentToBackStack(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.FRAME_CONTENT, fragment).addToBackStack(null).commit();
    }

 /*   public void createBannerAdRequest(boolean isFromROS) {
        if (mBottomBannerAdView != null) {
            Log.i(TAG, "createBannerAdRequest: ");
            mBottomBannerAdView.setVisibility(View.GONE);
            mHomeBottomLine.setVisibility(View.GONE);
            mBottomBannerAdListener = null;
            String adSpotId;
            if (isFromROS) {
                Log.i(TAG, "createBannerAdRequest: Ros");
                adSpotId = getString(R.string.vmax_adspot_id_ros_bottom);
            } else {
                Log.i(TAG, "createBannerAdRequest: Home");
                adSpotId = getString(R.string.vmax_adspot_id_home_bottom);
            }
            mBottomBannerAdView.setAdSpotId(adSpotId);
            mBottomBannerAdView.setUxType(VmaxAdView.UX_INLINE_DISPLAY);
            initializeBottomBannerAdListener();
            Map tempAdSettings = new HashMap<>();
            tempAdSettings.put(VmaxAdSettings.AdSettings_sbd, VmaxAdSize.AdSize_320x50);
            mBottomBannerAdView.setAdSettings(tempAdSettings);
            mBottomBannerAdView.setAdListener(mBottomBannerAdListener);
            mBottomBannerAdView.setRefresh(false);
            mBottomBannerAdView.cacheAd();
        }
    }

    private void initializeBottomBannerAdListener() {
        mBottomBannerAdListener = new VmaxAdListener() {

            @Override
            public void didInteractWithAd(VmaxAdView adView) {
                Log.i(TAG, "didInteractWithAd: HomeBottomBannerAdListener");
            }

            @Override
            public void adViewDidLoadAd(VmaxAdView adView) {
                Log.i(TAG, "adViewDidLoadAd: HomeBottomBannerAdListener");
            }

            @Override
            public void willPresentAd(VmaxAdView adView) {
                Log.i(TAG, "willPresentAd: HomeBottomBannerAdListener");
//                mBottomBannerAdView.setVisibility(View.VISIBLE);
//                mHomeBottomLine.setVisibility(View.VISIBLE);
            }

            @Override
            public void willDismissAd(VmaxAdView adView) {
                Log.i(TAG, "willDismissAd: HomeBottomBannerAdListener");
                mBottomBannerAdView.setVisibility(View.GONE);
                mHomeBottomLine.setVisibility(View.GONE);

            }

            @Override
            public void adViewDidCacheAd(VmaxAdView adView) {
                Log.i(TAG, "adViewDidCacheAd: HomeBottomBannerAdListener");
                if (adView != null) {
                    Log.i(TAG, "adViewDidCacheAd Cache Ads: HomeBottomBannerAdListener");
                  *//*  if (!isShowBottomAds()) {
                        mBottomBannerAdView.setVisibility(View.GONE);
                        return;
                    } else {*//*
                    mBottomBannerAdView.showAd();
                    mBottomBannerAdView.setVisibility(View.VISIBLE);
                    mHomeBottomLine.setVisibility(View.VISIBLE);
//                    }
                }
            }

            @Override
            public VmaxAdView didFailedToLoadAd(String arg0) {
                Log.i(TAG, "didFailedToLoadAd: HomeBottomBannerAdListener" + arg0);
                return null;
            }

            @Override
            public VmaxAdView didFailedToCacheAd(String Error) {
                Log.i(TAG, "didFailedToCacheAd: HomeBottomBannerAdListener" + Error);
                return null;
            }

            @Override
            public void willLeaveApp(VmaxAdView adView) {
            }

            @Override
            public void onVideoView(boolean isVideoComplete, int watchedMillis, int totalDurationMillis) {
            }


            @Override
            public void onAdExpand() {
            }

            @Override
            public void onAdCollapsed() {
            }
        };
    }*/



    @Override
    public void onBackPressed() {
        if(fragment != null && fragment.canGoBack()) {

        } else {
            super.onBackPressed();
        }
    }
}
