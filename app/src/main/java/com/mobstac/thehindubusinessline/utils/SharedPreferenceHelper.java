/*
 * Copyright (c) 2014 Mobstac TM
 * All Rights Reserved.
 * @since May 30, 2014 
 * @author rajeshcp
 */
package com.mobstac.thehindubusinessline.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mobstac.thehindubusinessline.model.NotificationBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rajeshcp
 */
public final class SharedPreferenceHelper {

    /**
     * @return of type SharedPreferenceHelper
     * Constructor function
     * @author rajeshcp
     * @since May 30, 2014
     */
    public SharedPreferenceHelper() {
    }

    /**
     * @param context      of type Context
     * @param key          of type String
     * @param defaultValue of type boolean
     * @return of type boolean
     * function which will return the shared preference value for the key
     * @author rajeshcp
     * @since May 30, 2014
     */
    public static boolean getBoolean(final Context context, final String key, final boolean defaultValue) {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return pref.getBoolean(key, defaultValue);
    }

    /**
     * @param context of type Context
     * @param key     of type String
     * @param value   of type boolean
     *                function which will save the shared preference value for the key
     * @author rajeshcp
     * @since May 30, 2014
     */
    public static void putBoolean(final Context context, final String key, final boolean value) {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        Editor editor = pref.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * @param context of type Context
     * @param key     of type String
     * @param value   of type long
     *                function which will save the shared preference value for the key
     * @author rajeshcp
     * @since May 30, 2014
     */
    public static void putLong(final Context context, final String key, final long value) {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        Editor editor = pref.edit();
        editor.putLong(key, value);
        editor.commit();
    }


    /**
     * @param context      of type Context
     * @param key          of type String
     * @param defaultValue of type String
     * @return of type String
     * function which will return the shared preference value for the key
     * @author rajeshcp
     * @since May 30, 2014
     */
    public static String getString(final Context context, final String key, final String defaultValue) {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return pref.getString(key, defaultValue);
    }

    /**
     * @param context of type Context
     * @param key     of type String
     * @param value   of type String
     *                function which will save the shared preference value for the key
     * @author rajeshcp
     * @since May 30, 2014
     */
    public static void putString(final Context context, final String key, final String value) {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * @param context of type Context
     * @param key     of type String
     * @param value   of type int
     *                function which will save the shared preference value for the key
     * @author rajeshcp
     * @since May 30, 2014
     */
    public static String putInt(final Context context, final String key, final int value) {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        Editor editor = pref.edit();
        editor.putInt(key, value);
        editor.commit();
        return key;
    }


    /**
     * @param context      of type Context
     * @param key          of type String
     * @param defaultValue of type String
     * @return of type int
     * function which will return the shared preference value for the key
     * @author rajeshcp
     * @since May 30, 2014
     */
    public static int getInt(final Context context, final String key, final int defaultValue) {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return pref.getInt(key, defaultValue);
    }

    /**
     * @param context      of type Context
     * @param key          of type String
     * @param defaultValue of type long
     * @return of type long
     * function which will return the shared preference value for the key
     * @author rajeshcp
     * @since May 30, 2014
     */
    public static long getLong(final Context context, final String key, final int defaultValue) {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return pref.getLong(key, defaultValue);
    }

    public static void setTextSizeInPref(Context context, int size) {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        Editor editor = pref.edit();
        editor.putInt(Constants.SAVE_TEXT_SIZE, size);
        editor.commit();
    }

    public static int getTextSizeFromPref(final Context context) {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return pref.getInt(Constants.SAVE_TEXT_SIZE, 18);
    }

    public static void setFirstTap(Context context, boolean isFirstTap) {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        Editor editor = pref.edit();
        editor.putBoolean(Constants.FIRST_TAP, isFirstTap);
        editor.commit();
    }

    public static boolean getFirstTap(final Context context) {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return pref.getBoolean(Constants.FIRST_TAP, false);
    }

    public static void setThirdSwipe(Context context, boolean isThirdSwipe) {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        Editor editor = pref.edit();
        editor.putBoolean(Constants.THIRD_SWIPE, isThirdSwipe);
        editor.commit();
    }

    public static boolean getThirdSwipe(final Context context) {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return pref.getBoolean(Constants.THIRD_SWIPE, false);
    }


    public static void setStringArrayPref(Context ctx, String key, List<String> values) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = prefs.edit();
        JSONArray a = new JSONArray();
        for (int i = 0; i < values.size(); i++) {
            a.put(values.get(i));
        }
        if (!values.isEmpty()) {
            editor.putString(key, a.toString());
        } else {
            editor.putString(key, null);
        }
        editor.commit();
    }

    public static List<String> getStringArrayPref(Context ctx, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        String json = prefs.getString(key, null);
        List<String> urls = new ArrayList<String>();
        if (json != null) {
            try {
                JSONArray a = new JSONArray(json);
                for (int i = 0; i < a.length(); i++) {
                    String url = a.optString(i);
                    urls.add(url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return urls;
    }


    public static List<NotificationBean> getDataFromSharedPreferences(Context context) {
        Gson gson = new Gson();
        List<NotificationBean> productFromShared;
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String jsonPreferences = sharedPref.getString(Constants.NOTIFICATION_STORE, "");
        productFromShared = gson.fromJson(jsonPreferences, new TypeToken<List<NotificationBean>>() {}.getType());
        return productFromShared;
    }

    private static void setDataFromSharedPreferences(Context context, NotificationBean curProduct) {
        Gson gson = new Gson();
        String jsonCurProduct = gson.toJson(curProduct);
        JSONArray array = new JSONArray();
        try {
            array.put(new JSONObject(jsonCurProduct));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(Constants.NOTIFICATION_STORE, array.toString());
        editor.commit();
    }

    public static void addInJSONArray(Context context, NotificationBean productToAdd) {
        Gson gson = new Gson();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String jsonSaved = sharedPref.getString(Constants.NOTIFICATION_STORE, "");
        String jsonNewproductToAdd = gson.toJson(productToAdd);
        JSONArray jsonArrayProduct = new JSONArray();

        try {
            if (jsonSaved.length() != 0) {
                jsonArrayProduct = new JSONArray(jsonSaved);
                jsonArrayProduct.put(new JSONObject(jsonNewproductToAdd));
                //SAVE NEW ARRAY
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(Constants.NOTIFICATION_STORE, jsonArrayProduct.toString());
                editor.commit();
            } else {
                setDataFromSharedPreferences(context, productToAdd);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static boolean isUserSelectedDfpConsent(Context context) {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return pref.getBoolean("isUserSelectedDfpConsent", false);
    }

    public static boolean isDfpConsentExecuted(Context context) {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return pref.getBoolean("isDfpConsentExecuted", false);
    }

    public static boolean isUserPreferAdsFree(Context context) {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return pref.getBoolean("isUserPreferAdsFree", false) && pref.getBoolean("isUserFromEurope", false);
    }

    public static boolean isUserFromEurope(Context context) {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return pref.getBoolean("isUserFromEurope", false);
    }


    public static void setUserSelectedDfpConsent(Context context, boolean isUserSelectedDfpConsent) {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        Editor editor = pref.edit();
        editor.putBoolean("isUserSelectedDfpConsent", isUserSelectedDfpConsent);
        editor.apply();
    }

    public static void setDfpConsentExecuted(Context context, boolean isDfpConsentExecuted) {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        Editor editor = pref.edit();
        editor.putBoolean("isDfpConsentExecuted", isDfpConsentExecuted);
        editor.apply();
    }

    public static void setUserPreferAdsFree(Context context, boolean isUserPreferAdsFree) {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        Editor editor = pref.edit();
        editor.putBoolean("isUserPreferAdsFree", isUserPreferAdsFree);
        editor.apply();
    }

    public static void setUserFromEurope(Context context, boolean isUserFromEurope) {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        Editor editor = pref.edit();
        editor.putBoolean("isUserFromEurope", isUserFromEurope);
        editor.apply();
    }
}
