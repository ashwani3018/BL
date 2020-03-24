package com.ns.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.netoperation.net.ApiManager;
import com.netoperation.retrofit.ServiceFactory;
import com.netoperation.util.THPPreferences;
import com.ns.contentfragment.AppTabFragment;
import com.ns.loginfragment.AccountCreatedFragment;
import com.ns.thpremium.BuildConfig;
import com.ns.thpremium.R;
import com.ns.utils.FragmentUtil;
import com.ns.utils.ResUtil;
import com.ns.utils.THPConstants;
import com.ns.utils.THPFirebaseAnalytics;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class AppTabActivity extends BaseAcitivityTHP {

    private String mFrom;
    private AppTabFragment appTabFragment;


    @Override
    public int layoutRes() {
        return R.layout.activity_apptab;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Fetch latest userinfo from server
        fetchLatestUserInfo();

        if(getIntent() != null && getIntent().getExtras()!= null) {
            mFrom = getIntent().getStringExtra("from");
        }

        if(BuildConfig.IS_PRODUCTION) {
            ServiceFactory.BASE_URL = BuildConfig.PRODUCTION_BASE_URL;
        } else {
            ServiceFactory.BASE_URL = BuildConfig.STATGGING_BASE_URL;
        }

        ApiManager.getUserProfile(this)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userProfile -> {
                    String taIn = THPConstants.FLOW_TAB_CLICK;
                    int tabIndex = getIntent().getIntExtra("tabIndex", 0);
                    appTabFragment = AppTabFragment.getInstance(mFrom, userProfile.getUserId(), tabIndex);

                    FragmentUtil.pushFragmentAnim(this, R.id.parentLayout, appTabFragment, FragmentUtil.FRAGMENT_NO_ANIMATION, true);

                    // THis below condition will be executed when user creates normal Sign-UP
                    if(mFrom != null && !TextUtils.isEmpty(mFrom) && mFrom.equalsIgnoreCase(THPConstants.FROM_USER_SignUp)) {
                        AccountCreatedFragment accountCreated = AccountCreatedFragment.getInstance("");
                        FragmentUtil.addFragmentAnim(this, R.id.parentLayout, accountCreated, FragmentUtil.FRAGMENT_NO_ANIMATION, false);
                    }
                });

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if(intent != null) {
            mFrom = intent.getStringExtra("from");
            if(appTabFragment != null && mFrom != null) {
                appTabFragment.updateFromValue(mFrom);
//                appTabFragment.updateTabIndex();
            }
            Log.i("", "");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null) {
            boolean isKillToAppTabActivity = data.getBooleanExtra("isKillToAppTabActivity", false);
            if(isKillToAppTabActivity) {
                finish();
            }
        }
    }

    String mUserId = "";

    /**
     * Fetch latest userinfo from server
     */
    private void fetchLatestUserInfo() {
        ApiManager.getUserProfile(this)
                .observeOn(AndroidSchedulers.mainThread())
                .delay(3, TimeUnit.SECONDS)
                .subscribe(userProfile -> {
                    mUserId = userProfile.getUserId();
                        // Fetch latest userinfo from server
                        ApiManager.getUserInfo(this, BuildConfig.SITEID,
                                ResUtil.getDeviceId(this), mUserId,
                                THPPreferences.getInstance(this).getLoginId(),
                                THPPreferences.getInstance(this).getLoginPasswd())
                                .subscribe(val->{
                                    Log.i("", "");
                                }, thr->{
                                    Log.i("", "");
                                });
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        THPFirebaseAnalytics.setFirbaseAnalyticsScreenRecord(this, "AppTabActivity Screen", AppTabActivity.class.getSimpleName());
    }
}
