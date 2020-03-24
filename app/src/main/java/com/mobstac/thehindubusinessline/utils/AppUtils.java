package com.mobstac.thehindubusinessline.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.mobstac.thehindubusinessline.R;
import com.mobstac.thehindubusinessline.activity.MainActivity;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ashwani on 24/12/15.
 */
public class AppUtils {

    public static final String MMM_dd_yyyy_hh_mm_a = "MMM dd, yyyy hh:mm a";
    public static final String SEARCH_DATE_FORMAT = "E, dd MMM yyyy";
    public static final String SEARCH_COMMENT_FORMAT = "E, dd MMM yyyy, hh:mm a";
    public static final String dd_MMM_yy = "EEE, MMM dd, yyyy";

    public final static String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
    private final static String post_comment = "yyyy/MM/dd HH:mm:ss";

    public static long changeStringToMillis(String dateInString) {
        SimpleDateFormat formatter = new SimpleDateFormat(yyyy_MM_dd_HH_mm_ss);
        try {
            Date date = formatter.parse(dateInString);
            return date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static long changeStringToMillisGMT(String dateInString) {
        SimpleDateFormat formatter = new SimpleDateFormat(yyyy_MM_dd_HH_mm_ss);
        try {
            formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date date = formatter.parse(dateInString);
            return date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static long changeCommentDateStringToMillis(String dateInString) {
        SimpleDateFormat formatter = new SimpleDateFormat(post_comment);
        try {
            Date date = formatter.parse(dateInString);
            return date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String getRefreshedDateTime(String timeInMillis) {
        String result = "";
        try {
            long date_updated = Long.parseLong(timeInMillis);
            Date time_updated = new Date(date_updated);
            result = "" + new SimpleDateFormat("hh:mm a, dd MMM yyyy").format(time_updated);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Last refreshed at: " + result;
    }

    public static String getTopNewsFormattedDate(long date_updated , boolean check) {
        String result = "";
        try {
            Date time_updated = new Date(date_updated);
            if (check) {
                result = "Updated: "
                        + new SimpleDateFormat(MMM_dd_yyyy_hh_mm_a).format(time_updated) + " IST";
            } else {
                result =  new SimpleDateFormat(MMM_dd_yyyy_hh_mm_a).format(time_updated) + " IST";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getSearchedNewsFormattedDate(long date_updated) {
        String result = "";
        try {
            Date time_updated = new Date(date_updated);
            result = "" + new SimpleDateFormat(SEARCH_DATE_FORMAT).format(time_updated);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getCommentFormattedDate(long date_updated) {
        String result = "";
        try {
            Date time_updated = new Date(date_updated);
            result = new SimpleDateFormat(SEARCH_COMMENT_FORMAT).format(time_updated) + " IST";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getDurationFormattedDate(long date_created) {
        String result = "";
        long currentTime = System.currentTimeMillis();
        Date time_created = new Date(date_created);

        if (currentTime - date_created < 60 * 1000) {
            result = ((currentTime - date_created) / 1000)
                    + " sec ago";
        } else if (currentTime - date_created < 60 * 60 * 1000) {
            result = ((currentTime - date_created) / (60 * 1000))
                    + " min ago";
        } else if (currentTime - date_created < 24 * 60 * 60 * 1000) {
            long t = (currentTime - date_created) / (60 * 60 * 1000);
            if (t == 1)
                result = t + " hr ago";
            else
                result = t + " hrs ago";
        } else {
            result = new SimpleDateFormat(dd_MMM_yy).format(time_created);
//            result = "1d";
        }
        return result;
    }

    public static String getDd_MMM_yy(long timeInMilliSeconds) {
        String result = "";
        try {
            Date time_updated = new Date(timeInMilliSeconds);
            result = new SimpleDateFormat(dd_MMM_yy).format(time_updated);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void hideKeyBoard(Context context, View view) {
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

    }


    @SuppressLint("RestrictedApi")
    public static void disableShiftMode(BottomNavigationView view) {
        /*BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                //noinspection RestrictedApi
//                item.setShiftingMode(false);
                item.setShifting(false);
                // set once again checked value, so view will be updated
                //noinspection RestrictedApi
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (Exception e) {
            Log.e("BNVHelper", "Unable to get shift mode field", e);
        }*/
    }

    public static String encodeCredentialsForBasicAuthorization() {
        final String userAndPassword = "AppClient" + ":" + "2a233eb8ed02ef89b240028d7739c473";
        return "Basic " + Base64.encodeToString(userAndPassword.getBytes(), Base64.NO_WRAP);
    }


    public static String getDateFormateChange(String dates){

        String lastUpdateDate= null;
        try {
            Date d = new SimpleDateFormat("dd-MMM-yyyy HH:mm").parse(dates);
            lastUpdateDate = new SimpleDateFormat("dd-MMM-yyyy").format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return lastUpdateDate;
    }

    public static String getTestDeviceId(Context context) {
       return SharedPreferenceHelper.getString( context,"testAds", "");
    }

    public static int getArticleIdFromArticleUrls(String url) {
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

    /**
     * Hides the keyboard displayed for the SearchEditText.
     *
     * @param view The view to detach the keyboard from.
     */
    public static void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
