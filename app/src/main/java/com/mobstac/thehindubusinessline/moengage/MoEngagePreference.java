package com.mobstac.thehindubusinessline.moengage;

import android.content.Context;
import android.content.SharedPreferences;

import com.mobstac.thehindubusinessline.utils.Constants;


/**
 * Created by ashwani on 29/08/16.
 */
public class MoEngagePreference {

    private SharedPreferences mSharedPreference;
    private SharedPreferences.Editor mEditor;

    private MoEngagePreference() {
    }

    private static class MoEngagePreferenceHelper {
        private static final MoEngagePreference MO_ENGAGE_PREFERENCE = new MoEngagePreference();
    }

    public static MoEngagePreference getInstance() {
        return MoEngagePreferenceHelper.MO_ENGAGE_PREFERENCE;
    }

    private SharedPreferences getSharedPreference(Context context) {
        if (mSharedPreference == null || mEditor == null) {
            mSharedPreference = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
            mEditor = mSharedPreference.edit();
        }
        return mSharedPreference;
    }

    private SharedPreferences.Editor getEditor(Context context) {
        if (mEditor == null) {
            return getSharedPreference(context).edit();
        }
        return mEditor;
    }

    public boolean isFirstLaunch(Context context) {
        return getSharedPreference(context).contains(Constants.MOENGAGE_PREF_FIRST_LAUCH_KEY);
    }

    public void setMoEngageAppId(Context context, String moEngageAppId) {
        getEditor(context).putString(Constants.MOENGAGE_PREF_FIRST_LAUCH_KEY, moEngageAppId);
        getEditor(context).apply();
    }

    public boolean isAppInstalledFirstTime(Context context) {
        return getSharedPreference(context).getBoolean("AppInstalledFirstTime", true);
    }

    public void setAppInstalledFirstTime(Context context, boolean isInstalled) {
        getEditor(context).putBoolean("AppInstalledFirstTime", isInstalled);
        getEditor(context).commit();
    }

    public void setPushToken(Context context, String pushToekn) {
        getEditor(context).putString("pushToken", pushToekn);
        getEditor(context).apply();
    }

    public String getPushToken(Context context) {
        return getSharedPreference(context).getString("pushToken", "");
    }

    /********************************************
     * WATCHED VIDEO COUNT
     ********************************************/
    private void watchedVideoCountApply(Context context, int newCount) {
        getEditor(context).putInt(Constants.WATCHED_VIDEO_COUNT_KEY, newCount);
        getEditor(context).apply();
    }

    public int getWatchedVideoCount(Context context) {
        int count = 0;
        count = getSharedPreference(context).getInt(Constants.WATCHED_VIDEO_COUNT_KEY, 1);
        watchedVideoCountApply(context, count + 1);
        return count;
    }


    /********************************************
     * BOOKMARKED ARTICLE COUNT
     ********************************************/
    private void bookMarkedCountApply(Context context, int newCount) {
        getEditor(context).putInt(Constants.BOOK_MARKED_ARTICLES_COUNT_KEY, newCount);
        getEditor(context).apply();
    }

    public int getBookmarkedArticlesCount(Context context) {
        int count = 0;
        count = getSharedPreference(context).getInt(Constants.BOOK_MARKED_ARTICLES_COUNT_KEY, 1);
        bookMarkedCountApply(context, count + 1);
        return count;
    }

    /********************************************
     * READ ARTICLE COUNT
     ********************************************/
    private void readArticleCountApply(Context context, int newCount, String event) {
        getEditor(context).putInt(event, newCount);
        getEditor(context).apply();
    }

    public int getReadArticleCount(Context context, String event) {
        int count = 0;
        count = getSharedPreference(context).getInt(event, 1);
        readArticleCountApply(context, count + 1, event);
        return count;
    }


    /********************************************
     * COMMENT COUNT
     ********************************************/
    private void commentGivenByCurrentUserCountApply(Context context, int newCount) {
        getEditor(context).putInt(Constants.COMMENT_GIVEN_BY_CURRENT_USER_COUNT_KEY, newCount);
        getEditor(context).apply();
    }

    public int getCommentGivenByCurrentUserCount(Context context) {
        int count = 0;
        count = getSharedPreference(context).getInt(Constants.COMMENT_GIVEN_BY_CURRENT_USER_COUNT_KEY, 1);
        commentGivenByCurrentUserCountApply(context, count + 1);
        return count;
    }
}
