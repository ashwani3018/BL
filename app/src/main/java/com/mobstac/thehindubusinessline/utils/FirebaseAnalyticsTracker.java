package com.mobstac.thehindubusinessline.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

public class FirebaseAnalyticsTracker {

    static void setFirebaseAnalyticsScreenName(Context context, String screenName){
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);

        Bundle bundle = new Bundle();
        bundle.putString("screenName", screenName);
        mFirebaseAnalytics.logEvent("Screens", bundle);
    }

    static void setFirebaseAnalyticsRecordScreen(Context context, String screenName){
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        mFirebaseAnalytics.setCurrentScreen((Activity) context, screenName, ((Activity) context).getLocalClassName());
    }

    public static void setFirebaseAnalyticsScreenTimings(Context context , String category , long time , String label, String variable){
        if(context == null) {
            return;
        }
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, category);
        bundle.putString(FirebaseAnalytics.Param.LEVEL_NAME, label);
        bundle.putString(FirebaseAnalytics.Param.ITEM_VARIANT, variable);
        mFirebaseAnalytics.logEvent("time_taken", bundle);

    }
}
