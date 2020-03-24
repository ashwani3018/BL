package com.mobstac.thehindubusinessline.utils;

import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mobstac.thehindubusinessline.R;

/**
 * Created by arvind on 7/6/16.
 */
public class GoogleAnalyticsTracker {
    public static final String TAG = "GoogleAnalyticsTracker";
    public static Tracker mTracker;

    public static void init(Context context) {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(context);
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
    }

    public static synchronized Tracker getTracker(Context context) {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(context);
            mTracker = analytics.newTracker(R.xml.global_tracker);
            // Enable Display Features.
            mTracker.enableAdvertisingIdCollection(true);
        }
        return mTracker;
    }

    public static void setGoogleAnalyticScreenName(Context context, String screenName) {
        Tracker t = getTracker(context);
        if (t != null) {
            t.setScreenName(screenName);
            t.send(new HitBuilders.ScreenViewBuilder().build());
        }
        FirebaseAnalyticsTracker.setFirebaseAnalyticsRecordScreen(context, screenName);
        FirebaseAnalyticsTracker.setFirebaseAnalyticsScreenName(context, screenName);
    }

    public static String getSectionName(String sectionName) {
        return "Section: " + sectionName;
    }

    public static String getGoogleArticleName(String article) {
        return "Article: " + article;
    }

    public static void setGoogleAnalyticsEvent(Context context, String category, String action, String actionLable) {
        Tracker t = getTracker(context);
        if (t != null) {
            t.send(new HitBuilders.EventBuilder()
                    .setCategory(category)
                    .setAction(action)
                    .setLabel(actionLable)
                    .build()
            );
        }
    }
}
