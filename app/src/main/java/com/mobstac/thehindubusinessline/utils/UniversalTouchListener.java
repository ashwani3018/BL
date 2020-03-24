package com.mobstac.thehindubusinessline.utils;

import android.view.MotionEvent;
import android.view.View;

import com.mobstac.thehindubusinessline.fragments.SlidingSectionFragment;

/**
 * Created by arvind on 19/1/17.
 */

public class UniversalTouchListener implements View.OnTouchListener {
    private View view;
    private boolean isEnable;

    public UniversalTouchListener(View view, boolean isEnable) {
        this.isEnable = isEnable;
        this.view = view;
        this.view.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (SlidingSectionFragment.mViewPager != null) {
            SlidingSectionFragment.mViewPager.setPagingEnabled(isEnable);
        }
        return false;
    }
}
