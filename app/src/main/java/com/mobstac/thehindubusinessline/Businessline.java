package com.mobstac.thehindubusinessline;

import android.app.ActivityManager;
import android.app.Application;
import android.app.UiModeManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.multidex.MultiDexApplication;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;
import android.util.Log;

import com.comscore.Analytics;
import com.comscore.PublisherConfiguration;
import com.crashlytics.android.Crashlytics;
import com.flurry.android.FlurryAgent;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger;
import com.google.firebase.FirebaseApp;
import com.mobstac.thehindubusinessline.database.TheHinduRealmMigration;
import com.mobstac.thehindubusinessline.moengage.CustomNotification;
import com.mobstac.thehindubusinessline.moengage.MoEngagePreference;
import com.mobstac.thehindubusinessline.utils.Constants;
import com.mobstac.thehindubusinessline.utils.GoogleAnalyticsTracker;
import com.mobstac.thehindubusinessline.utils.LotameAppTracker;
import com.mobstac.thehindubusinessline.utils.SharedPreferenceHelper;
import com.moe.pushlibrary.MoEHelper;
import com.moengage.core.MoEngage;
import com.moengage.core.listeners.OnAppBackgroundListener;
import com.moengage.inapp.InAppManager;
import com.moengage.push.PushManager;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import com.taboola.android.api.TaboolaApi;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by arvind on 16/8/16.
 */
public class Businessline extends MultiDexApplication implements PushManager.OnTokenReceivedListener, OnAppBackgroundListener {
    public static final int DATABASE_SCHEMA_VERSION = 5;
    public static RealmConfiguration mRealmConfiguration;
    public static Realm mRealm;
    private static EventBus mEventBus;
    private final String TAG = "TheHinduApplication";

    public static Realm getRealmInstance() {
        if (mRealm == null) {
            mRealm = Realm.getInstance(getMyRealmConfiguration());
            return mRealm;
        }
        return mRealm;
    }

    private static RealmConfiguration getMyRealmConfiguration() {
        if (mRealmConfiguration == null) {
            mRealmConfiguration = new RealmConfiguration.Builder()
                    .name("TheHinduBusinessline.realm")
                    .schemaVersion(DATABASE_SCHEMA_VERSION)
                    .migration(new TheHinduRealmMigration())
                    .build();
        }
        return mRealmConfiguration;
    }

    public static EventBus getmEventBus() {
        if (mEventBus == null) {
            mEventBus = new EventBus();
        }
        return mEventBus;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        clearSharedPreference();

        //Fabric.with(this, new Crashlytics());
        GoogleAnalytics.getInstance(this).getLogger()
                .setLogLevel(Logger.LogLevel.VERBOSE);

        FlurryAgent.init(this, getResources().getString(R.string.flurryAppKey));
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        SharedPreferenceHelper.putBoolean(
                getApplicationContext(),
                Constants.PREFERENCES_HOME_REFRESH,
                false);

        boolean isNightModeEnabled = SharedPreferenceHelper.getBoolean(getApplicationContext(),
                Constants.DAY_MODE,
                false);

        UiModeManager uiManager = (UiModeManager) getSystemService(Context.UI_MODE_SERVICE);
        if (isNightModeEnabled) {
            uiManager.setNightMode(UiModeManager.MODE_NIGHT_YES);
        } else {
            uiManager.setNightMode(UiModeManager.MODE_NIGHT_NO);
        }



        initRealm(this);

        /*Comscore integration*/

        PublisherConfiguration myPublisherConfig = new PublisherConfiguration.Builder()
                .publisherId(getResources().getString(R.string.comscore_customer_c2)) // Provide your Publisher ID here.
                .publisherSecret(getResources().getString(R.string.comscore_publisher_secret)) // Provide your Publisher Secret here.
                .applicationName("Businessline")
                .build();
        Analytics.getConfiguration().addClient(myPublisherConfig);
        Analytics.start(this);

        /*Comscore ends*/

        GoogleAnalyticsTracker.init(this);
        LotameAppTracker.init(this);
        try {
            LotameAppTracker.sendDataToLotameAnalytics(this, getString(R.string.la_action), getString(R.string.la_app_open));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Firebase Initialisation
        FirebaseApp.initializeApp(this);

        //MoEngage Initialisation
        MoEngage moEngage =
                new MoEngage.Builder(this, Constants.MOENGAGE_APPID)//enter your own app id
                        .setLogLevel(com.moengage.core.Logger.VERBOSE)//enabling Logs for debugging
                        .enableLogsForSignedBuild() //Make sure this is removed before apps are pushed to production
                        .setNotificationSmallIcon(R.mipmap.icon_notification)//small icon should be flat, pictured face on, and must be white on a transparent background.
                        .setNotificationLargeIcon(R.mipmap.icon_notification)
                        .enableLocationServices()//enabled To track location and run geo-fence campaigns
                        .optOutNavBar()
                        .build();

        MoEngage.initialise(moEngage);

        boolean existing = false;
        if (MoEngagePreference.getInstance().isAppInstalledFirstTime(this)) {
            existing = true;
        }
        else {
            MoEngagePreference.getInstance().setAppInstalledFirstTime(this, false);
        }

        MoEHelper.getInstance(getApplicationContext()).setExistingUser(existing);

        //register for push token callback
        //required only if push token is required by the app
        PushManager.getInstance().setTokenObserver(this);

        //register for on background listener
        MoEHelper.getInstance(getApplicationContext()).registerAppBackgroundListener(this);

        PushManager.getInstance().setMessageListener(new CustomNotification());
        //MoEngage End

        Picasso.Builder builder = new Picasso.Builder(this);
        //builder.downloader(new OkHttpDownloader(this, Integer.MAX_VALUE));
        Picasso built = builder.build();
        Picasso.setSingletonInstance(built);

    }

    private void initRealm(Context context) {
        ActivityManager activityManager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo processInfo : activityManager.getRunningAppProcesses()) {
            if (android.os.Process.myPid() == processInfo.pid) {
                if (TextUtils.equals(processInfo.processName, BuildConfig.APPLICATION_ID)) {
                    Realm.init(context);
                    Realm.setDefaultConfiguration(getMyRealmConfiguration());
                }
                break;
            }
        }
    }

    @Override
    public void onTokenReceived(String s) {
        Log.i(TAG, "onTokenReceived: " + s);
        MoEngagePreference.getInstance().setPushToken(this, s);
    }

    private void clearSharedPreference() {

        boolean forThisVersionOnly = SharedPreferenceHelper.getBoolean(this, "for2.1Only", true);
        if (forThisVersionOnly) {
            final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.commit();
            editor.putBoolean("for2.1Only", false);
            editor.commit();

            SharedPreferences preferences = getSharedPreferences("SyncTimePref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor1 = preferences.edit();
            editor1.clear();
            editor1.commit();
        }
    }

    @Override
    public void goingToBackground() {
        //track event custom event here
    }
}
