package com.mobstac.thehindubusinessline.utils;

import android.content.Context;
import android.content.Intent;

import com.flurry.android.FlurryAgent;
import com.mobstac.thehindubusinessline.R;

import java.io.IOException;

/**
 * Created by arvind on 22/11/16.
 */

public class SharingArticleUtil {

    public static void shareArticle(Context mContext, String mShareTitle, String mShareUrl) {
        Intent intent = getSharingIntent(mShareTitle, mShareUrl);
        if (intent != null) {
            try {
                LotameAppTracker.sendDataToLotameAnalytics(mContext, mContext.getString(R.string.la_action), mContext.getString(R.string.la_share));
            } catch (IOException e) {
                e.printStackTrace();
            }
            mContext.startActivity(intent);
            FlurryAgent.logEvent(mContext.getString(R.string.ga_section_share_button_clicked));
            GoogleAnalyticsTracker.setGoogleAnalyticsEvent(mContext, mContext.getString(R.string.ga_action), mContext.getString(R.string.ga_section_share_button_clicked), mContext.getString(R.string.ga_article_detail_lebel));
        }
    }

    public static Intent getSharingIntent(String mShareTitle, String mShareUrl) {

        if (mShareTitle == null) {
            mShareTitle = "";
        }
        if (mShareUrl == null) {
            mShareUrl = "";
        }
        Intent sharingIntent = new Intent(
                android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = mShareTitle
                + ": " + mShareUrl;
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                mShareTitle);
        sharingIntent
                .putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TITLE,
                mShareTitle);
        return Intent.createChooser(sharingIntent, "Share Via");
    }

}
