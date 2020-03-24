package com.mobstac.thehindubusinessline.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

import com.mobstac.thehindubusinessline.listener.CustomScrollListenerForScrollView;

/**
 * Created by 9920 on 10/16/2017.
 */

public class CustomArticleScrollView extends ScrollView {

    private boolean mScrollable = true;

    private CustomScrollListenerForScrollView scrollViewListener = null;

    public CustomArticleScrollView(Context context) {
        super(context);
    }

    public CustomArticleScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomArticleScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setScrollable(boolean isScrollable) {
        this.mScrollable = isScrollable;
    }

    public void setScrollViewListener(CustomScrollListenerForScrollView scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mScrollable)
                    return super.onTouchEvent(ev);
                    // only continue to handle the touch event if scrolling enabled
                else {
//                    launchLoginPopupActivity();
                    if (scrollViewListener != null) {
                        scrollViewListener.showTransprentView(true);
                    }
                    return mScrollable; // mScrollable is always false at this point
                }
                // if we can scroll pass the event to the superclass
            default:
                return super.onTouchEvent(ev);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // Don't do anything with intercepted touch events if
        // we are not scrollable
        if (!mScrollable) {
//            launchLoginPopupActivity();
            if (scrollViewListener != null) {
                scrollViewListener.showTransprentView(true);
            }
            return false;
        } else
            return super.onInterceptTouchEvent(ev);
    }
/*
    private void launchLoginPopupActivity() {
        Intent intent = new Intent(getContext(), ShowLoginActivity.class);
        intent.putExtra(ShowLoginActivity.FROM_SCREEN, ShowLoginActivity.LOGIN_VIEW);
        getContext().startActivity(intent);
    }*/
}
