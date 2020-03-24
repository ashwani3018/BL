package com.mobstac.thehindubusinessline.moengage;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.util.Patterns;

import com.moe.pushlibrary.MoEHelper;
import com.moe.pushlibrary.PayloadBuilder;

import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by ashwani on 26/08/16.
 */
public class MoEngageTracker {

    private boolean mIsTrackingEnable = true;

    private static class MoEngageTrackerHelper {
        private static final MoEngageTracker MO_ENGAGE_TRACKER = new MoEngageTracker();
    }

    public static MoEngageTracker getInstance() {
        return MoEngageTrackerHelper.MO_ENGAGE_TRACKER;
    }

    private MoEngageTracker() {
    }

    private void setTrackingEnable(boolean isTrackingEnable) {
        mIsTrackingEnable = isTrackingEnable;
    }

    private boolean isTrackingEnable() {
        return mIsTrackingEnable;
    }

    public void sampleSendEvent(Context context) {
        PayloadBuilder builder = new PayloadBuilder();
        builder.putAttrString("From Screen", "MainActivity")
                .putAttrDate("Date", new Date())
                .putAttrString("To Screen", "Detail Activity");
        MoEHelper.getInstance(context).trackEvent("Detail", builder.build());
    }


    /**
     * Sets User Data.
     *
     * @param context
     */
    public void setUserData(Context context) {
        MoEHelper helper = MoEHelper.getInstance(context);
        helper.setUniqueId(Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));
//        helper.setUniqueId(System.currentTimeMillis());

        /*String gmail = null;
        final Pattern emailPattern = Patterns.EMAIL_ADDRESS;
        final Account[] accounts = AccountManager.get(context).getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches() && account.type != null && account.type.equalsIgnoreCase("com.google")) {
                gmail = account.name;
                if (gmail != null) {
                    helper.setEmail(gmail);
                    break;
                }
            }
        }*/
        helper.setFullName(getDeviceName() + ", OS is " + Build.VERSION.RELEASE);
    }


    /**
     * Sends, total count of article read by user.
     *
     * @param context
     */
    public void totalCountOfArticlesRead(Context context, String event) {
        if (!isTrackingEnable()) {
            return;
        }
        PayloadBuilder builder = new PayloadBuilder();
        /*builder.putAttrInt("total count",
                MoEngagePreference.getInstance().getReadArticleCount(context , event));*/
        MoEHelper.getInstance(context).trackEvent(event, builder.build());
    }

    /**
     * Sends, User regional interested news content.
     *
     * @param context
     * @param sectionName
     */
    public void regionalNewsContentInterests(Context context, String sectionName) {
        if (!isTrackingEnable()) {
            return;
        }
        PayloadBuilder builder = new PayloadBuilder();
        builder.putAttrString("sectionName", sectionName);
        MoEHelper.getInstance(context).trackEvent("Regional News Content Interests", builder.build());
    }

    /**
     * Sends, Number of watched videos by user.
     *
     * @param context
     */
    public void numberOfWatchedVideos(Context context) {
        if (!isTrackingEnable()) {
            return;
        }
        PayloadBuilder builder = new PayloadBuilder();
        builder.putAttrInt("total count", MoEngagePreference.getInstance().getWatchedVideoCount(context));
        MoEHelper.getInstance(context).trackEvent("Number Of Videos Watched", builder.build());
    }

    /**
     * Sends, Bookmarked article count.
     *
     * @param context
     */
    public void numberOfArticleBookmarked(Context context) {
        if (!isTrackingEnable()) {
            return;
        }
        PayloadBuilder builder = new PayloadBuilder();
        builder.putAttrInt("total count", MoEngagePreference.getInstance().getBookmarkedArticlesCount(context));
        MoEHelper.getInstance(context).trackEvent("Number Of Article Bookmarked", builder.build());
    }

    /**
     * Sends, total comment count given by the user.
     *
     * @param context
     */
    public void numberOfCommentSendByUser(Context context, String articleID) {
        if (!isTrackingEnable()) {
            return;
        }
        PayloadBuilder builder = new PayloadBuilder();
        builder.putAttrInt("total count", MoEngagePreference.getInstance().getCommentGivenByCurrentUserCount(context));
        builder.putAttrString("current article id", articleID);
        MoEHelper.getInstance(context).trackEvent("Total No Of Comments", builder.build());
    }

    /**
     * Sends, Chosen Notifications.
     *
     * @param context
     */
    public void notificationsChosen(Context context, List<Object> selectedNotificationList) {
        if (!isTrackingEnable()) {
            return;
        }
        PayloadBuilder builder = new PayloadBuilder();
        if (selectedNotificationList != null) {
            final int size = selectedNotificationList.size();
            for (int i = 0; i < size; i++) {
                builder.putAttrString("Notification Chosen " + i, "id :: nc id, name :: nc name");
            }
        }

        MoEHelper.getInstance(context).trackEvent("Notification Chosen", builder.build());
    }

    /**
     * Sends, Chosen Section on Home Screen.
     *
     * @param context
     */
    public void sectionsChosen(Context context, List<Object> selectedSectionsList) {
        if (!isTrackingEnable()) {
            return;
        }
        PayloadBuilder builder = new PayloadBuilder();
        if (selectedSectionsList != null) {
            final int size = selectedSectionsList.size();
            for (int i = 0; i < size; i++) {
                builder.putAttrString("Section Chosen " + i, "id :: sc id, name :: sc name");
            }
        }

        MoEHelper.getInstance(context).trackEvent("Sections Chosen", builder.build());
    }

    /**
     * Sends, When an article is opened.
     *
     * @param context
     * @param articleId
     * @param categoryName
     */
    public void openedAnArticle(Context context, String articleId, String categoryName) {
        if (!isTrackingEnable()) {
            return;
        }
        PayloadBuilder builder = new PayloadBuilder();
        builder.putAttrString("article id", articleId);
        builder.putAttrString("category name", categoryName);
        MoEHelper.getInstance(context).trackEvent("Opened An Article", builder.build());
    }

    /**
     * Sends, Selected Menu Options
     *
     * @param context
     * @param clickedMenuName
     * @param categoryName
     */
    public void menuCategoryClicked(Context context, String clickedMenuName, String categoryName) {
        if (!isTrackingEnable()) {
            return;
        }
        PayloadBuilder builder = new PayloadBuilder();
        builder.putAttrString("clicked menu", clickedMenuName);
        builder.putAttrString("category name", categoryName);
        MoEHelper.getInstance(context).trackEvent("Menu Category Clicked", builder.build());
    }

    /**
     * Sends, Search String
     *
     * @param context
     * @param searchString
     */
    public void searchEvent(Context context, String searchString) {
        if (!isTrackingEnable()) {
            return;
        }
        PayloadBuilder builder = new PayloadBuilder();
        builder.putAttrString("search query", searchString);
        MoEHelper.getInstance(context).trackEvent("Search Event", builder.build());
    }

    /**
     * Sends, Shared Article Event.
     *
     * @param context
     * @param articleId
     */
    public void sharedAnEvent(Context context, String articleId, String articleCategoryName) {
        if (!isTrackingEnable()) {
            return;
        }
        PayloadBuilder builder = new PayloadBuilder();
        builder.putAttrString("article id", articleId);
        builder.putAttrString("article category", articleCategoryName);
        MoEHelper.getInstance(context).trackEvent("Shared An Article Event", builder.build());
    }


    public void bookmarkedAnArticle(Context context, String articleId, String articleCategoryName) {
        if (!isTrackingEnable()) {
            return;
        }

        PayloadBuilder builder = new PayloadBuilder();
        builder.putAttrString("article id", articleId);
        builder.putAttrString("article category", articleCategoryName);
        MoEHelper.getInstance(context).trackEvent("Bookmarked An Article Event", builder.build());
    }

    public void removedBookmarkedAnArticle(Context context, String articleId, String articleCategoryName) {
        if (!isTrackingEnable()) {
            return;
        }

        PayloadBuilder builder = new PayloadBuilder();
        builder.putAttrString("article id", articleId);
        builder.putAttrString("article category", articleCategoryName);
        MoEHelper.getInstance(context).trackEvent("Removed Bookmarked An Article Event", builder.build());
    }

    public void commentedOnArticle(Context context, String articleId, String articleCategoryName) {
        if (!isTrackingEnable()) {
            return;
        }

        PayloadBuilder builder = new PayloadBuilder();
        builder.putAttrString("article id", articleId);
        builder.putAttrString("article category", articleCategoryName);
        MoEHelper.getInstance(context).trackEvent("Commented On An Article Event", builder.build());
    }

    public void playedAVideo(Context context, String articleId, String articleCategoryName) {
        if (!isTrackingEnable()) {
            return;
        }

        PayloadBuilder builder = new PayloadBuilder();
        builder.putAttrString("article id", articleId);
        builder.putAttrString("article category", articleCategoryName);
        MoEHelper.getInstance(context).trackEvent("Played A Video Event", builder.build());
    }

    public void slideShow(Context context, String articleId, String articleCategoryName) {
        if (!isTrackingEnable()) {
            return;
        }

        PayloadBuilder builder = new PayloadBuilder();
        builder.putAttrString("article id", articleId);
        builder.putAttrString("article category", articleCategoryName);
        MoEHelper.getInstance(context).trackEvent("Slide Show Event", builder.build());
    }

    private String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }


}
