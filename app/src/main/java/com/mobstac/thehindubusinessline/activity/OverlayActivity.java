package com.mobstac.thehindubusinessline.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.mobstac.thehindubusinessline.R;
import com.mobstac.thehindubusinessline.utils.Constants;
import com.mobstac.thehindubusinessline.utils.GoogleAnalyticsTracker;
import com.mobstac.thehindubusinessline.utils.SharedPreferenceHelper;

public class OverlayActivity extends BaseActivity {
    private String TAG = "OverlayActivity";
    public static final int TYPE_ARTICLE = 0;
    public static final int TYPE_HOME = 1;
    private int overlayType;
    private String firstArticleTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overlay);
        if (getIntent().hasExtra(Constants.OVERLAY_TYPE)) {
            overlayType = getIntent().getIntExtra(Constants.OVERLAY_TYPE, 0);
        }
        if (getIntent().hasExtra(Constants.OVERLAY_LIST)) {
            firstArticleTitle = getIntent().getStringExtra(Constants.OVERLAY_LIST);
        }

        switch (overlayType) {
            case TYPE_HOME:
                findViewById(R.id.home_layout).setVisibility(View.VISIBLE);
                findViewById(R.id.navigationMenu).setVisibility(View.VISIBLE);
                findViewById(R.id.overflow).setVisibility(View.VISIBLE);
                if (firstArticleTitle != null) {
                    ((TextView) findViewById(R.id.textview_banner)).setText(firstArticleTitle);
                }
                break;
            case TYPE_ARTICLE:
                findViewById(R.id.article_layout).setVisibility(View.VISIBLE);
                findViewById(R.id.textSize).setVisibility(View.VISIBLE);
                findViewById(R.id.bookmark).setVisibility(View.VISIBLE);
                findViewById(R.id.audio).setVisibility(View.VISIBLE);
                break;

        }

        GoogleAnalyticsTracker.setGoogleAnalyticScreenName(this, getString(R.string.ga_overlay_screen));
        FlurryAgent.logEvent(getString(R.string.ga_overlay_screen));
        FlurryAgent.onPageView();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (overlayType) {
            case TYPE_ARTICLE:
                SharedPreferenceHelper.putBoolean(OverlayActivity.this, Constants.ARTICLE_OVERLAY_SCREEN_LOADED, false);
                break;
            case TYPE_HOME:
                SharedPreferenceHelper.putBoolean(OverlayActivity.this, Constants.HOME_OVERLAY_SCREEN_LOADED, false);
                break;
        }
        finish();
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onBackPressed() {
        switch (overlayType) {
            case TYPE_ARTICLE:
                SharedPreferenceHelper.putBoolean(OverlayActivity.this, Constants.ARTICLE_OVERLAY_SCREEN_LOADED, false);
                break;
            case TYPE_HOME:
                SharedPreferenceHelper.putBoolean(OverlayActivity.this, Constants.HOME_OVERLAY_SCREEN_LOADED, false);
                break;
        }
        super.onBackPressed();
    }
}
