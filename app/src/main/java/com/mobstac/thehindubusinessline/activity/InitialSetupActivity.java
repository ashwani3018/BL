package com.mobstac.thehindubusinessline.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.mobstac.thehindubusinessline.R;
import com.mobstac.thehindubusinessline.Businessline;
import com.mobstac.thehindubusinessline.database.ApiCallHandler;
import com.mobstac.thehindubusinessline.database.GetCompanyNameTask;
import com.mobstac.thehindubusinessline.fragments.InitialSetupCityFragment;
import com.mobstac.thehindubusinessline.fragments.InitialSetupSectionFragment;
import com.mobstac.thehindubusinessline.model.HomeEvents;
import com.mobstac.thehindubusinessline.utils.Constants;
import com.mobstac.thehindubusinessline.utils.DFPConsent;
import com.mobstac.thehindubusinessline.utils.GoogleAnalyticsTracker;
import com.mobstac.thehindubusinessline.utils.NetworkUtils;
import com.mobstac.thehindubusinessline.utils.SharedPreferenceHelper;
import com.mobstac.thehindubusinessline.view.CustomViewPager;

import org.greenrobot.eventbus.Subscribe;

public class InitialSetupActivity extends BaseActivity implements DFPConsent.ConsentSelectionListener{

    private CustomViewPager mOnBoardingViewPager;
    private final int NUMBER_OF_SCREENS = 1;
    private LinearLayout mBottomStickyLayout;
    private RelativeLayout mRootLayout;
    private Button mSkipButton;
    private Button mNextButton;
    private InitalSetupPagerAdapter mInitalSetupPagerAdapter;
    private DFPConsent mDfpConsent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_setup);

        mOnBoardingViewPager = (CustomViewPager) findViewById(R.id.viewpager_initial_setup);
        mRootLayout = (RelativeLayout) findViewById(R.id.layout_splash_root);
        mBottomStickyLayout = (LinearLayout) findViewById(R.id.saveLayout);
        mBottomStickyLayout.setVisibility(View.GONE);
        mNextButton = (Button) findViewById(R.id.button_feed_save);
        mSkipButton = (Button) findViewById(R.id.button_feed_skip);
        mNextButton.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Lato-Regular.ttf"));
        mSkipButton.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Lato-Regular.ttf"));
        mOnBoardingViewPager.setPagingEnabled(false);


        mOnBoardingViewPager.beginFakeDrag();
        Businessline.getmEventBus().register(InitialSetupActivity.this);

        mRootLayout.setVisibility(View.VISIBLE);
        mOnBoardingViewPager.setVisibility(View.GONE);

        /** sharedPreference boolean to indicate the status of onBoard finish status*/
        boolean isInterestSelected = SharedPreferenceHelper.getBoolean(
                InitialSetupActivity.this,
                Constants.IS_INTEREST_SELECTED,
                false
        );
        /** if/else to decide the flow of app
         * if block -- launch {@link SplashActivity} as user finished onBoarding screen
         * else block -- trigger a server request to get the list of section
         * */
        if (isInterestSelected) {
            boolean isUserSelectedDfpConsent = SharedPreferenceHelper.isUserSelectedDfpConsent(this);
            boolean isDfpConsentExecuted = SharedPreferenceHelper.isDfpConsentExecuted(this);
            boolean isUserFromEurope = SharedPreferenceHelper.isUserFromEurope(this);

            if(!isUserFromEurope && isDfpConsentExecuted) {
                startActivity(new Intent(InitialSetupActivity.this, SplashActivity.class));
                finish();
            }
            else if((isUserFromEurope && isDfpConsentExecuted) && !isUserSelectedDfpConsent) {
                setupDFPConsent();
            }
            else {
                startActivity(new Intent(InitialSetupActivity.this, SplashActivity.class));
                finish();
            }

        } else {
            if (NetworkUtils.isNetworkAvailable(this)) {
                new GetCompanyNameTask(this).execute(Constants.COMPANY_NAME_LIST_URL);
                ApiCallHandler.fetchSectionCall(InitialSetupActivity.this);
                GoogleAnalyticsTracker.setGoogleAnalyticScreenName(this, getString(R.string.ga_onboarding_screen));
                FlurryAgent.logEvent(getString(R.string.ga_onboarding_screen));
                FlurryAgent.onPageView();
            } else {
                showSnackBar(R.string.snackbar_title_text);
            }
        }

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = mOnBoardingViewPager.getCurrentItem();
                switch (position) {
                    case 0:
                        Fragment fragment = getCurrentFragmet();
                        InitialSetupSectionFragment initialSetupSectionFragment = null;
                        if (fragment instanceof com.mobstac.thehindubusinessline.fragments.InitialSetupSectionFragment) {
                            initialSetupSectionFragment = (InitialSetupSectionFragment) fragment;
                        }
                        if (initialSetupSectionFragment != null) {
                            initialSetupSectionFragment.saveButtonClicked();
                        }
                        break;
                    case 1:

                        if(!NetworkUtils.isNetworkAvailable(InitialSetupActivity.this)) {
                            showSnackBar(R.string.snackbar_title_text);
                            return;
                        }

                        boolean isUserSelectedDfpConsent = SharedPreferenceHelper.isUserSelectedDfpConsent(InitialSetupActivity.this);
                        boolean isDfpConsentExecuted = SharedPreferenceHelper.isDfpConsentExecuted(InitialSetupActivity.this);
                        boolean isUserFromEurope = SharedPreferenceHelper.isUserFromEurope(InitialSetupActivity.this);

                        if(isUserFromEurope && isDfpConsentExecuted && !isUserSelectedDfpConsent) {
                            Toast.makeText(InitialSetupActivity.this, "Please complete user consent.", Toast.LENGTH_LONG).show();
                            mDfpConsent = new DFPConsent();
                            mDfpConsent.setConsentSelectionListener(InitialSetupActivity.this);
                            mDfpConsent.initUserConsentForm(InitialSetupActivity.this);
                            return;
                        }
                        Fragment fragment1 = getCurrentFragmet();
                        InitialSetupCityFragment initialSetupCityFragment = null;
                        if (fragment1 instanceof com.mobstac.thehindubusinessline.fragments.InitialSetupCityFragment) {
                            initialSetupCityFragment = (InitialSetupCityFragment) fragment1;
                        }
                        if (initialSetupCityFragment != null) {
                            initialSetupCityFragment.saveButtonClicked();
                        }
                        break;
                }
            }
        });

        mSkipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = mOnBoardingViewPager.getCurrentItem();
                switch (position) {
                    case 0:

                        if(!NetworkUtils.isNetworkAvailable(InitialSetupActivity.this)) {
                            showSnackBar(R.string.snackbar_title_text);
                            return;
                        }

                        boolean isUserSelectedDfpConsent = SharedPreferenceHelper.isUserSelectedDfpConsent(InitialSetupActivity.this);
                        boolean isDfpConsentExecuted = SharedPreferenceHelper.isDfpConsentExecuted(InitialSetupActivity.this);
                        boolean isUserFromEurope = SharedPreferenceHelper.isUserFromEurope(InitialSetupActivity.this);

                        if(isUserFromEurope && isDfpConsentExecuted && !isUserSelectedDfpConsent) {
                            Toast.makeText(InitialSetupActivity.this, "Please complete user consent.", Toast.LENGTH_LONG).show();
                            mDfpConsent = new DFPConsent();
                            mDfpConsent.setConsentSelectionListener(InitialSetupActivity.this);
                            mDfpConsent.initUserConsentForm(InitialSetupActivity.this);
                            return;
                        }

                        SharedPreferenceHelper.putBoolean(
                                InitialSetupActivity.this,
                                Constants.IS_INTEREST_SELECTED,
                                true);

                        SharedPreferenceHelper.putBoolean(
                                InitialSetupActivity.this,
                                Constants.PREFERENCES_FETCH_PERSONALIZE_FEED,
                                false);
                        // start the SplashActivity -- since the user skipped the onBoarding sequence
                        startActivity(new Intent(InitialSetupActivity.this, SplashActivity.class));
                        finish();
                        break;
                    case 1:
                        mOnBoardingViewPager.setCurrentItem(0, true);
                        break;
                }
            }
        });
    }



    /**
     * The following method is a event bus implementation
     * the event Constants.EVENT_INSERTED_SECTION_API_DATABASE is broadcasted
     * from {@link ApiCallHandler} insertSectionData() method
     *
     * @param event
     */


    @Subscribe
    public void onEvent(HomeEvents event) {
        switch (event.getMessage()) {
            case Constants.EVENT_INSERTED_SECTION_API_DATABASE:
                mOnBoardingViewPager.setVisibility(View.VISIBLE);
                mRootLayout.setVisibility(View.GONE);
                mBottomStickyLayout.setVisibility(View.VISIBLE);
                mInitalSetupPagerAdapter = new InitalSetupPagerAdapter(getSupportFragmentManager());
                mOnBoardingViewPager.setAdapter(mInitalSetupPagerAdapter);
                break;
            case Constants.EVENT_HOME_FAILURE:
                boolean isInterestSelected = SharedPreferenceHelper.getBoolean(
                        InitialSetupActivity.this,
                        Constants.IS_INTEREST_SELECTED,
                        false
                );
                if(isInterestSelected) {
                    startHomeActivity();
                } else {
                    showSnackBar(R.string.snackbar_something_went_wrong);
                }
                break;
        }

        boolean isUserSelectedDfpConsent = SharedPreferenceHelper.isUserSelectedDfpConsent(this);
        boolean isDfpConsentExecuted = SharedPreferenceHelper.isDfpConsentExecuted(this);
        if (!isUserSelectedDfpConsent || !isDfpConsentExecuted) {
            setupDFPConsent();
        }
    }

    private void startHomeActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void setupDFPConsent() {
        DFPConsent.GDPR_Testing(this);
        mDfpConsent = new DFPConsent();
        mDfpConsent.setConsentSelectionListener(this);
        mDfpConsent.init(this, true);
    }

    @Override
    public void isUserInEurope(boolean isInEurope) {
        mDfpConsent.initUserConsentForm(this);
    }


    private Snackbar mSnackbar;

    /**
     * shows the snackbar when the internet is not available
     */
    public void showSnackBar(int msgResId) {
        if(mSnackbar != null) {
            return;
        }


        mSnackbar = Snackbar.make(mOnBoardingViewPager, "", Snackbar.LENGTH_INDEFINITE);
        Snackbar.SnackbarLayout mSnackbarView = (Snackbar.SnackbarLayout) mSnackbar.getView();
        mSnackbarView.setBackgroundColor(Color.BLACK);
        TextView textView = (TextView) mSnackbarView.findViewById(android.support.design.R.id.snackbar_text);
        TextView textView1 = (TextView) mSnackbarView.findViewById(android.support.design.R.id.snackbar_action);
        textView.setVisibility(View.INVISIBLE);
        textView1.setVisibility(View.INVISIBLE);
        View snackView = getLayoutInflater().inflate(R.layout.snackbar_layout, null);
        snackView.findViewById(R.id.action_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                startActivity(intent);
            }
        });

        if(msgResId == R.string.snackbar_something_went_wrong) {
            snackView.findViewById(R.id.action_button).setVisibility(View.GONE);
        } else {
            snackView.findViewById(R.id.action_button).setVisibility(View.VISIBLE);
        }

        TextView snackbar_textMsg = snackView.findViewById(R.id.snackbar_msg);
        snackbar_textMsg.setText(msgResId);

        mSnackbarView.addView(snackView);
        mSnackbar.show();
    }

    private class InitalSetupPagerAdapter extends FragmentStatePagerAdapter {
        private SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

        public InitalSetupPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return InitialSetupSectionFragment.newInstance(false);
                case 1:
                    return InitialSetupCityFragment.newInstance(true);
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return NUMBER_OF_SCREENS;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }


        public Fragment getRegisteredFragment(int position) {
            return registeredFragments.get(position);
        }
    }


    public void setNextButtonText(String nextButtonText) {
        if (mNextButton != null) {
            mNextButton.setText(nextButtonText);
        }
    }

    public void setSkipButtonText(String skipButtonText) {
        if (mSkipButton != null) {
            mSkipButton.setText(skipButtonText);
        }
    }

    private Fragment getCurrentFragmet() {
        int position = mOnBoardingViewPager.getCurrentItem();
        if (mInitalSetupPagerAdapter != null) {
            return mInitalSetupPagerAdapter.getRegisteredFragment(position);
        } else {
            return null;
        }
    }

    public void setViewPagerItem(int position) {
        if (mOnBoardingViewPager != null) {
            mOnBoardingViewPager.setCurrentItem(position, true);
        }
    }

}
