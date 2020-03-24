/*
 * Copyright (c) 2014 Mobstac TM
 * All Rights Reserved.
 * @since Jun 3, 2014 
 * @author rajeshcp
 */
package com.mobstac.thehindubusinessline.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * @author rajeshcp
 */
public class NetworkUtils {

    /**
     * @param context of type Context
     * @return of type boolean
     * function which will check the network availability
     * @author rajeshcp
     * @since Jun 3, 2014
     */
    private static final short SYNC_UP_MINUTE = 10;
    private static SharedPreferences sharedPreferences;
    private static final long SYNC_UP_DURATION = SYNC_UP_MINUTE * 1000 * 60;

    public static final boolean isNetworkAvailable(final Context context) {
        boolean result = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        result = (info != null && info.isConnected());
        return result;
    }

    /**
     * Check if there is fast connectivity
     *
     * @param context
     * @return of type
     * @author rajeshcp
     * @since Sep 30, 2014
     */
    public static boolean isConnectedFast(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return (info != null && info.isConnected() && isConnectionFast(info.getType(), info.getSubtype()));
    }

    /**
     * Check the connection type is fast or not
     *
     * @param type
     * @param subType
     * @return of type
     * @author rajeshcp
     * @Copied http://stackoverflow.com/questions/13007707/detect-if-connection-is-wifi-3g-or-edge-in-android
     * @since Sep 30, 2014
     */
    public static boolean isConnectionFast(int type, int subType) {
        if (type == ConnectivityManager.TYPE_WIFI) {
            return true;
        } else if (type == ConnectivityManager.TYPE_MOBILE) {
            switch (subType) {
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    return false; // ~ 14-64 kbps
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    return true; // ~ 400-1000 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    return true; // ~ 600-1400 kbps
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    return false; // ~ 100 kbps
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    return true; // ~ 2-14 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPA:
                    return true; // ~ 700-1700 kbps
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                    return true; // ~ 1-23 Mbps
                case TelephonyManager.NETWORK_TYPE_UMTS:
                    return true; // ~ 400-7000 kbps
            /*
	         * Above API level 7, make sure to set android:targetSdkVersion 
	         * to appropriate level to use these
	         */
                case TelephonyManager.NETWORK_TYPE_EHRPD: // API level 11
                    return true; // ~ 1-2 Mbps
                case TelephonyManager.NETWORK_TYPE_EVDO_B: // API level 9
                    return true; // ~ 5 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPAP: // API level 13
                    return true; // ~ 10-20 Mbps
                case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8
                    return false; // ~25 kbps
                case TelephonyManager.NETWORK_TYPE_LTE: // API level 11
                    return true; // ~ 10+ Mbps
                // Unknown
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                default:
                    return false;
            }
        } else {
            return false;
        }
    }


    public static void saveSectionSyncTimePref(Context context, String sectionName) {
        SharedPreferences.Editor editor = getSyncTimePref(context).edit();
        editor.putLong(sectionName, System.currentTimeMillis());
        editor.commit();
    }

    public static boolean isSectionNotNeedToSync(Context context, String sectionName) {
        long lastSyncTime = getSyncTimePref(context).getLong(sectionName, 0);
        long currentTimeInMilli = System.currentTimeMillis();
        long lastSyncDuration = currentTimeInMilli - lastSyncTime;
        if (lastSyncDuration >= SYNC_UP_DURATION) {
            return false;
        }
        return true;
    }

/*
    public static boolean isPrefKeyIsRunning(String type, String id, Context context) {
        return getSyncTimePref(context).getBoolean(syncPrefKeyForRunning(type, id), false);
    }
*/

    private static String syncPrefKeyForRunning(String type, String id) {
        return type + "$" + id + "$R";
    }

    //    Running Status
/*    public static void setRunningContentSyncPref(Context context, String type, String id) {
        SharedPreferences.Editor editor = getSyncTimePref(context).edit();
        editor.putBoolean(syncPrefKeyForRunning(type, id), true);
        editor.commit();
    }*/

   /* public static void saveContentSyncPref(Context context, String type, String id) {
        SharedPreferences.Editor editor = getSyncTimePref(context).edit();
        editor.putBoolean(syncPrefKey(type, id), true);
        editor.commit();
    }
*/

    private static String syncPrefKey(String type, String id) {
        return type + "$" + id;
    }

   /* public static void resetRunningSpecificSectionPref(Context context, String type, String id) {
        SharedPreferences.Editor editor = getSyncTimePref(context).edit();
        editor.putBoolean(syncPrefKeyForRunning(type, id), false);
        editor.commit();
    }*/

    public static SharedPreferences getSyncTimePref(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences("SyncTimePref", Context.MODE_PRIVATE);
        }
        return sharedPreferences;
    }

}
