/*
 * Copyright (c) 2014 Mobstac TM
 * All Rights Reserved.
 * @since May 30, 2014 
 * @author rajeshcp
 */
package com.mobstac.thehindubusinessline.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.UiModeManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import com.flurry.android.FlurryAgent;
import com.google.gson.Gson;
import com.mobstac.thehindubusinessline.BuildConfig;
import com.mobstac.thehindubusinessline.Businessline;
import com.mobstac.thehindubusinessline.R;
import com.mobstac.thehindubusinessline.database.ApiCallHandler;
import com.mobstac.thehindubusinessline.database.DatabaseJanitor;
import com.mobstac.thehindubusinessline.database.GetCompanyNameTask;
import com.mobstac.thehindubusinessline.model.HomeEvents;
import com.mobstac.thehindubusinessline.model.NotificationBean;
import com.mobstac.thehindubusinessline.model.SectionTable;
import com.mobstac.thehindubusinessline.model.UpdateModel;
import com.mobstac.thehindubusinessline.model.WidgetsTable;
import com.mobstac.thehindubusinessline.retrofit.RetrofitAPICaller;
import com.mobstac.thehindubusinessline.utils.Constants;
import com.mobstac.thehindubusinessline.utils.GoogleAnalyticsTracker;
import com.mobstac.thehindubusinessline.utils.NetworkUtils;
import com.mobstac.thehindubusinessline.utils.SharedPreferenceHelper;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmResults;

public class SplashActivity extends BaseActivity {

    private static final String TAG = "SplashActivity";
    private UpdateModel mUpdateModel;
    private Dialog mDialog;
    private boolean isUpdateCheckServiceExecuted = false;
    private boolean isDataLoadingDone = false;
    private AlertDialog.Builder builder;
    private ImageView mLoadingImageView;
    private boolean isHomeNeedToBeFetched;
    private boolean isHomeFetched;
    private boolean isInternestDone;
    private RealmResults<SectionTable> mSectionData;
    private int mSectionSize;

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_splash);
        Log.i(TAG, "onCreate: ");
        mLoadingImageView = (ImageView) findViewById(R.id.imageView_loading_logo);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                List<NotificationBean> mList = SharedPreferenceHelper.getDataFromSharedPreferences(SplashActivity.this);
                SharedPreferenceHelper.putString(SplashActivity.this, Constants.NOTIFICATION_STORE, "");
                if (mList != null && mList.size() > 0) {
                    for (final NotificationBean bean : mList) {
                        Realm mRealm = Businessline.getRealmInstance();
                        if (mRealm != null) {
                            mRealm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.copyToRealm(bean);
                                }
                            });
                        }
                    }
                }

                boolean isNightModeEnabled = SharedPreferenceHelper.getBoolean(getApplicationContext(),
                        Constants.DAY_MODE,
                        false);

                UiModeManager uiManager = (UiModeManager) getSystemService(Context.UI_MODE_SERVICE);
                if (isNightModeEnabled) {
                    uiManager.setNightMode(UiModeManager.MODE_NIGHT_YES);
                } else {
                    uiManager.setNightMode(UiModeManager.MODE_NIGHT_NO);
                }


                mSectionData = DatabaseJanitor.getSectionList(0);
                mSectionData.addChangeListener(new OrderedRealmCollectionChangeListener<RealmResults<SectionTable>>() {
                    @Override
                    public void onChange(RealmResults<SectionTable> sectionTables, OrderedCollectionChangeSet changeSet) {
                        mSectionSize = sectionTables.size();
                        finishSplash();
                    }
                });
                Businessline.getmEventBus().register(SplashActivity.this);

                boolean isNetworkAvailable = NetworkUtils.isNetworkAvailable(SplashActivity.this);

                if (isNetworkAvailable) {
                    checkForUpdate();
                    isHomeNeedToBeFetched = !NetworkUtils.isSectionNotNeedToSync(SplashActivity.this, "Home");
                } else {
                    isUpdateCheckServiceExecuted = true;
                    isHomeNeedToBeFetched = false;
                }


                boolean isVC60 = SharedPreferenceHelper.getBoolean(SplashActivity.this, "isVC30FirstSetup", true);

                boolean syncStatus = true;
                if (isVC60) {
                    syncStatus = false;
                } else {
                    syncStatus = NetworkUtils.isSectionNotNeedToSync(SplashActivity.this, Constants.SECTION_LIST);
                }
                if (!syncStatus && isNetworkAvailable) {
                    Log.i(TAG, " FetchSections");
                    new GetCompanyNameTask(SplashActivity.this).execute(Constants.COMPANY_NAME_LIST_URL);
                    ApiCallHandler.fetchSectionCall(SplashActivity.this);
                } else {
                    isDataLoadingDone = true;
                    if (isHomeNeedToBeFetched && isNetworkAvailable) {
                        callHomeApi();
                    } else {
                        isHomeFetched = true;
                        finishSplash();
                    }
                }

                SharedPreferenceHelper.putBoolean(SplashActivity.this, Constants.FIRST_TAP, false);
                SharedPreferenceHelper.putBoolean(SplashActivity.this, Constants.THIRD_SWIPE, false);
                builder = new AlertDialog.Builder(SplashActivity.this);

            }
        }, 100);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
        GoogleAnalyticsTracker.setGoogleAnalyticScreenName(this, getString(R.string.ga_splash_screen));
        FlurryAgent.logEvent(getString(R.string.ga_splash_screen));
        FlurryAgent.onPageView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mSectionData.removeAllChangeListeners();
    }

    @Override
    public void finish() {
        super.finish();
        Log.i(TAG, "onFinish");
        mSectionData.removeAllChangeListeners();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.i(TAG, "onBackPressed");
    }

    @Subscribe
    public void onEvent(HomeEvents event) {
        Log.i(TAG, "onEvent: " + event.getMessage());
        switch (event.getMessage()) {
            case Constants.EVENT_INSERTED_SECTION_API_DATABASE:
                Log.i(TAG, "onEvent: EVENT_INSERTED_SECTION_API_DATABASE");
                SharedPreferenceHelper.putBoolean(SplashActivity.this, "isVC30FirstSetup", false);
                SharedPreferenceHelper.putInt(SplashActivity.this, Constants.PREFERENCE_SECTION_COUNT, 0);
                isDataLoadingDone = true;

                if (isHomeNeedToBeFetched && NetworkUtils.isNetworkAvailable(SplashActivity.this)) {
                    callHomeApi();
                } else {
                    isHomeFetched = true;
                }

                if (isUpdateCheckServiceExecuted) {
                    if (mUpdateModel != null) {
                        String severVersionCode = mUpdateModel.getVersion_code();
                        int serverVersionNumber = 0;
                        try {
                            serverVersionNumber = Integer.parseInt(severVersionCode);
                        } catch (Exception e) {
                            serverVersionNumber = 34;
                            e.printStackTrace();
                        }
                        int appVersionCode;
                        try {
                            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                            appVersionCode = pInfo.versionCode;
                        } catch (PackageManager.NameNotFoundException e) {
                            appVersionCode = 0;
                            e.printStackTrace();
                        }

                        if (appVersionCode < serverVersionNumber) {
                            if (!isFinishing()) {
                                showUpdateDialog(mUpdateModel.getApp_store_url(), mUpdateModel.getMessage(), mUpdateModel.getForce_upgrade(), mUpdateModel.getRemind_me());
                            }
                        } else {
                            finishSplash();
                        }
                    } else {
                        finishSplash();
                    }
                }
                break;

            case Constants.EVENT_HOME_FAILURE:
                Log.i(TAG, "onEvent: EVENT_HOME_FAILURE");
//                RealmResults<SectionTable> mSectionList = DatabaseJanitor.getSectionList(0);
                if (mSectionSize > 0) {
                    isDataLoadingDone = true;
                    isHomeFetched = true;
                    finishSplash();
                } else {
                    return;
                }
                break;
            case Constants.EVENT_INSERTED_WIDGET_API_DATABASE:
                Log.i(TAG, "onEvent: EVENT_INSERTED_WIDGET_API_DATABASE");
                SharedPreferenceHelper.putBoolean(SplashActivity.this, Constants.IS_HOME_FIRST_TIME, true);
                SharedPreferenceHelper.putInt(SplashActivity.this, Constants.PREFERENCE_SECTION_COUNT, 0);
                SharedPreferenceHelper.putBoolean(SplashActivity.this, "isVC30FirstSetup", false);
//                SharedPreferenceHelper.putBoolean(SplashActivity.this, Constants.PREFERENCES_HOME_REFRESH, false);
                isHomeFetched = true;
                finishSplash();
                break;
            case Constants.EVENT_INSERTED_NEWSFEED_API_DATABASE:
                Log.i(TAG, "onEvent: EVENT_INSERTED_NEWSFEED_API_DATABASE");
                final List<WidgetsTable> mWidgetsList = DatabaseJanitor.getWidgetsTable();
                SharedPreferenceHelper.putInt(SplashActivity.this, Constants.PREFERENCE_SECTION_COUNT, 0);
                for (WidgetsTable mWidget : mWidgetsList) {
                    String id = String.valueOf(mWidget.getSecId());
                    String type = mWidget.getType();
                    String mSectionName = mWidget.getSecName();
                    long syncTime = NetworkUtils.getSyncTimePref(SplashActivity.this).getLong(mSectionName, 0);
                    Log.i(TAG, "onEvent: syncTime for section " + mSectionName + " " + syncTime);
                    ApiCallHandler.fetchSectionContent(SplashActivity.this, id, type, true, syncTime);
                }
                break;

        }
    }


    private void finishSplash() {
        Log.i(TAG, "finishSplash: outside of check");
        if (mDialog != null && mDialog.isShowing()) {
            return;
        } else if (isDataLoadingDone && isUpdateCheckServiceExecuted && isHomeFetched) {
            Log.i(TAG, "finishSplash: inside check condition");
            Businessline.getmEventBus().unregister(SplashActivity.this);
//            RealmResults<SectionTable> mSectionList = DatabaseJanitor.getSectionList(0);
            if (mSectionSize > 0) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }
       /* startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();*/
    }


    private void checkForUpdate() {
        Log.i(TAG, "checkForUpdate: ");
        long lastUpdated = SharedPreferenceHelper.getLong(this, Constants.LAST_UPDATE_TIME, -1);
        if ((lastUpdated == -1) || ((lastUpdated) < System.currentTimeMillis())) {
            Log.i(TAG, "checkForUpdate: execcute");
            new checkUpdateTask().execute();
        } else {
            Log.i(TAG, "checkForUpdate: else");
            isUpdateCheckServiceExecuted = true;
            if (isDataLoadingDone) {
                Log.i(TAG, "checkForUpdate: finishsplash");
                finishSplash();
            }
        }


    }

    private void showUpdateDialog(final String app_store_url, String message, final boolean isForceUpdate, String remindMeTimeInMillies) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        try {
                            if (app_store_url != null)
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(app_store_url)));
                            finish();
                        } catch (android.content.ActivityNotFoundException anfe) {

                        }
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        if (isForceUpdate) {
                            finish();
                        } else {
                            mDialog.cancel();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Log.i(TAG, "run: dialog");
                                    finishSplash();
                                }
                            }, 500);
                        }
                        break;
                }
            }
        };
        builder.setCancelable(isForceUpdate);
        builder.setTitle("A new update available");
        builder.setMessage(message);

        /*WebView wv = new WebView(getApplicationContext());
        wv.loadData(message, "text/html", "utf-8");
        builder.setView(wv);*/


        builder.setPositiveButton("Download", dialogClickListener);
        if (isForceUpdate) {
            builder.setNegativeButton("Cancel", dialogClickListener);
        } else {
            builder.setNegativeButton("Remind Me Later", dialogClickListener);
        }
        mDialog = builder.create();
        mDialog.show();
    }

    private void callHomeApi() {
        Log.i(TAG, "callHomeApi: ");
        long sycTime = NetworkUtils.getSyncTimePref(SplashActivity.this).getLong("Home", 0);
        ApiCallHandler.fetchHomeData(SplashActivity.this, true, sycTime, false);
    }

    private class checkUpdateTask extends AsyncTask<Void, Void, UpdateModel> {

        @Override
        protected UpdateModel doInBackground(Void... params) {
            URL url;
            HttpURLConnection connection = null;
            try {
                int versionCode = BuildConfig.VERSION_CODE;
                int osVersion = android.os.Build.VERSION.SDK_INT;
                url = new URL(RetrofitAPICaller.FORCE_UPGRADE);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("device_type", "android");
                jsonObject.put("app_version", versionCode);
                jsonObject.put("os_version", osVersion);

                OutputStream os = connection.getOutputStream();
                os.write((jsonObject.toString()).getBytes("UTF-8"));
                os.close();

                //Get SingUpDetail
                InputStream is = connection.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuffer response = new StringBuffer();
                while ((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                rd.close();
                Gson gson = new Gson();
                return gson.fromJson(response.toString(), UpdateModel.class);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(UpdateModel updateModel) {
            super.onPostExecute(updateModel);

            SplashActivity.this.mUpdateModel = updateModel;
            isUpdateCheckServiceExecuted = true;

            if (updateModel == null) {
                Log.i(TAG, "onPostExecute: finishsplash" + updateModel);
                finishSplash();
                return;
            }

            String severVersionCode = updateModel.getVersion_code();
            int serverVersionNumber = 0;
            try {
                serverVersionNumber = Integer.parseInt(severVersionCode);
            } catch (Exception e) {
                serverVersionNumber = 34;
                e.printStackTrace();
            }
            int appVersionCode;
            try {
                appVersionCode = BuildConfig.VERSION_CODE;
                if (!updateModel.getForce_upgrade()) { // If force updrade is TRUE then it won't save time.
                    SharedPreferenceHelper.putLong(SplashActivity.this, Constants.LAST_UPDATE_TIME, System.currentTimeMillis() + Long.valueOf(updateModel.getRemind_me()));
                }

                if (appVersionCode <= serverVersionNumber) {
                    SharedPreferenceHelper.putBoolean(SplashActivity.this, Constants.IS_LIVE, true);
                } else {
                    SharedPreferenceHelper.putBoolean(SplashActivity.this, Constants.IS_LIVE, false);
                }

            } catch (Exception e) {
                appVersionCode = 0;
                e.printStackTrace();
            }

            if ((appVersionCode < serverVersionNumber) && isDataLoadingDone) {
                if (!isFinishing()) {
                    showUpdateDialog(updateModel.getApp_store_url(), updateModel.getMessage(), updateModel.getForce_upgrade(), updateModel.getRemind_me());
                }
            } else if (isDataLoadingDone) {
                Log.i(TAG, "onPostExecute: ");
                finishSplash();
            }

        }
    }
}
