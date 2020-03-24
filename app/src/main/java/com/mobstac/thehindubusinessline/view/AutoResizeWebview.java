/*
 * Copyright (c) 2015 Mobstac TM
 * All Rights Reserved.
 * @since Jan 7, 2015 
 * @author rajeshcp
 */
package com.mobstac.thehindubusinessline.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.mobstac.thehindubusinessline.utils.Constants;
import com.mobstac.thehindubusinessline.utils.SharedPreferenceHelper;

/**
 * @author rajeshcp
 */
public class AutoResizeWebview extends WebView implements SharedPreferences.OnSharedPreferenceChangeListener {


    private int currentIndex = -1;
    private Context mContext;

    public AutoResizeWebview(Context context) {
        this(context, null);
        mContext = context;
        setPrefChangeListener();
    }


    public AutoResizeWebview(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
        mContext = context;
        setPrefChangeListener();
    }

    public AutoResizeWebview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        setPrefChangeListener();
    }

    private void setPrefChangeListener() {
        if (currentIndex == -1) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
            int newIndex = prefs.getInt(Constants.SELECTED_FONT_SIZE, 1);
            currentIndex = 1;
            setSize(newIndex);
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
            pref.registerOnSharedPreferenceChangeListener(this);
        }
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public void setSize(int newIndex) {
        currentIndex = newIndex;
        if (VERSION.SDK_INT >= VERSION_CODES.ICE_CREAM_SANDWICH) {
            getSettings().setTextZoom(100 + ((currentIndex - 1) * 10));
        } else {
            switch (currentIndex) {
                case 1: {
                    getSettings().setTextSize(WebSettings.TextSize.SMALLER);
                    break;
                }
                case 2: {
                    getSettings().setTextSize(WebSettings.TextSize.NORMAL);
                    break;
                }
                case 3: {
                    getSettings().setTextSize(WebSettings.TextSize.LARGER);
                    break;
                }
                case 4: {
                    getSettings().setTextSize(WebSettings.TextSize.LARGEST);
                    break;
                }
            }
        }
        invalidate();

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        setSize(SharedPreferenceHelper.getInt(mContext, Constants.SELECTED_FONT_SIZE, 1));
    }

}
