package com.mobstac.thehindubusinessline.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.mobstac.thehindubusinessline.R;
import com.mobstac.thehindubusinessline.utils.ArticleUtil;
import com.mobstac.thehindubusinessline.utils.Constants;
import com.mobstac.thehindubusinessline.utils.GoogleAnalyticsTracker;
import com.mobstac.thehindubusinessline.utils.NetworkUtils;
import com.taboola.android.TaboolaWidget;
import com.taboola.android.listeners.TaboolaEventListener;
import com.taboola.android.utils.SdkDetailsHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragment extends Fragment {

    private final String TAG = "BaseFragment";
//    private MainActivity mMainActivity;

    public BaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        mMainActivity = (MainActivity) context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        mMainActivity = (MainActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        return textView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "onViewCreated: ");
       /* if(this instanceof HomeFragment) {
            mMainActivity.hideRosBottomBanner();
            mMainActivity.showHomeBottomAdBanner();
        } else {
            mMainActivity.hideHomeBottomAdBanner();
            mMainActivity.showRosBottomBanner();
        }*/
    }

    public abstract void getDataFromDB();

    public abstract void showLoadingFragment();

    public abstract void hideLoadingFragment();


    public int getArticleIdFromArticleUrl(String url) {
        Pattern p = Pattern.compile("article(\\d+)");
        Matcher m = p.matcher(url);
        int mArticleId = 0;
        if (m.find()) {
            try {
                mArticleId = Integer.parseInt(m.group(1));
            } catch (Exception e) {
                return 0;
            }
        }

        return mArticleId;
    }

    private boolean isRecommendationNotNeedToLoadFromServer;
    private TaboolaWidget taboolaWidget;

    public void loadTaboolaWidgets(String articleLink){
        if(getView() != null && getActivity() != null && !isRecommendationNotNeedToLoadFromServer) {

            taboolaWidget = getView().findViewById(R.id.taboola_widget);

            if(articleLink != null) {
                articleLink = articleLink.replace("http://", "");
                articleLink = articleLink.replace("https://", "");
                articleLink = articleLink.replace("www.", "");
            }

            final int height = SdkDetailsHelper.getDisplayHeight(getActivity());
            ViewGroup.LayoutParams params = taboolaWidget.getLayoutParams();
            if (params == null) {
                params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
                taboolaWidget.setLayoutParams(params);
            } else {
                params.height = height;
            }

            Resources res = getResources();
            taboolaWidget.setPublisher(res.getString(R.string.taboola_pub));
            taboolaWidget.setMode(res.getString(R.string.taboola_mode));
            taboolaWidget.setPageType(res.getString(R.string.taboola_pagetype));
            taboolaWidget.setPlacement(res.getString(R.string.taboola_placement));
            taboolaWidget.setTargetType(res.getString(R.string.taboola_targetype));
            taboolaWidget.setPageUrl(articleLink);
            taboolaWidget.setFocusable(false);
            taboolaWidget.setInterceptScroll(true);

            HashMap<String, String> optionalPageCommands = new HashMap<>();
            optionalPageCommands.put("useOnlineTemplate", "true");
            optionalPageCommands.put("keepDependencies", "true"); // You must call TaboolaWidget.onDestroy() when it's time to destroy the widget
            taboolaWidget.setExtraProperties(optionalPageCommands);

            isRecommendationNotNeedToLoadFromServer = true;

            taboolaWidget.setTaboolaEventListener(new TaboolaEventListener() {
                @Override
                public boolean taboolaViewItemClickHandler(String url, boolean isOrganic) {
                    if (isOrganic) {
                        int articleId = getArticleIdFromArticleUrl(url);
                        ArticleUtil.launchDetailActivity(getActivity(), articleId, null, url, false, null);

                        FlurryAgent.logEvent(getString(R.string.ga_article_taboola_organic_clicked));
                        GoogleAnalyticsTracker.setGoogleAnalyticsEvent(getActivity(), "Taboola Item Click",
                                getString(R.string.ga_article_taboola_organic_clicked),
                                getString(R.string.ga_article_detail_lebel));

                        return false;
                    } else {
                        FlurryAgent.logEvent(getString(R.string.ga_article_taboola_nonorganic_clicked));
                        GoogleAnalyticsTracker.setGoogleAnalyticsEvent(getActivity(), "Taboola Item Click",
                                getString(R.string.ga_article_taboola_nonorganic_clicked),
                                getString(R.string.ga_article_detail_lebel));

                    }
                    return true;
                }

                @Override
                public void taboolaViewResizeHandler(TaboolaWidget taboolaWidget, int i) {
                    Log.d(TAG, "taboolaViewResizeHandler: " + taboolaWidget + " int  " + i);
                }
            });

            taboolaWidget.fetchContent();
        }

    }

    @Override
    public void onDestroy() {
        if(taboolaWidget != null) {
            taboolaWidget.onDestroy();
            taboolaWidget = null;
        }
        super.onDestroy();
    }

}
