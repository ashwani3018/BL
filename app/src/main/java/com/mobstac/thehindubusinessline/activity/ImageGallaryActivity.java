package com.mobstac.thehindubusinessline.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.mobstac.thehindubusinessline.R;
import com.mobstac.thehindubusinessline.adapter.ImagePagerAdapter;
import com.mobstac.thehindubusinessline.model.ImageGallaryUrl;
import com.mobstac.thehindubusinessline.utils.GoogleAnalyticsTracker;

import java.util.ArrayList;

public class ImageGallaryActivity extends BaseActivity {

    private final String TAG = "ImageGallaryActivity";

    private ViewPager mImageViewPager;
    private TextView mErrorText;
    private ProgressBar mProgressBar;
    private LinearLayout mProgressContainer;
    private ArrayList<ImageGallaryUrl> mImageUrlList;
    private TextView mSelectionIndicatorView;
    private int mSelectedPosition;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_gallary);

        mImageUrlList = getIntent().getParcelableArrayListExtra("ImageUrl");
        mSelectedPosition = getIntent().getIntExtra("selectedPosition", 0);

        mImageViewPager = (ViewPager) findViewById(R.id.imagePager);
        mProgressBar = (ProgressBar) findViewById(R.id.section_progress);
        mProgressContainer = (LinearLayout) findViewById(R.id.progress_container);
        mErrorText = (TextView) findViewById(R.id.error_text);
        mSelectionIndicatorView = (TextView) findViewById(R.id.selection_indicator);

        if (mImageUrlList != null && mImageUrlList.size() > 0) {
            mProgressContainer.setVisibility(View.GONE);
            mImageViewPager.setAdapter(new ImagePagerAdapter(this, mImageUrlList));
            mImageViewPager.setCurrentItem(mSelectedPosition);
            if (mImageUrlList.size() == 1) {
                mSelectionIndicatorView.setVisibility(View.GONE);
            } else {
                mSelectionIndicatorView.setText("1 of " + mImageUrlList.size());
            }
            FlurryAgent.logEvent(getString(R.string.flurry_image_view) + ": URL : " + mImageUrlList.get(0).getImageUrl());
            FlurryAgent.onPageView();
            GoogleAnalyticsTracker.setGoogleAnalyticScreenName(ImageGallaryActivity.this, "Image Screen: URL :  " + mImageUrlList.get(0).getImageUrl());
        } else {
            mProgressBar.setVisibility(View.GONE);
            mErrorText.setVisibility(View.VISIBLE);
        }
        mImageViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                FlurryAgent.logEvent(getString(R.string.flurry_image_view) + ": URL : " + mImageUrlList.get(position).getImageUrl());
                FlurryAgent.onPageView();
                int imageOriginalPosition = position + 1;
                mSelectionIndicatorView.setText("" + imageOriginalPosition + " of " + mImageUrlList.size());
                GoogleAnalyticsTracker.setGoogleAnalyticScreenName(ImageGallaryActivity.this, "Image Screen: URL : " + mImageUrlList.get(position).getImageUrl());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (mImageUrlList != null && mImageUrlList.size() > 1) {
            mSelectionIndicatorView.setVisibility(View.VISIBLE);
        } else {
            mSelectionIndicatorView.setVisibility(View.GONE);
        }
    }
}
