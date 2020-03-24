package com.mobstac.thehindubusinessline.fragments;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.mobstac.thehindubusinessline.R;
import com.mobstac.thehindubusinessline.activity.AdsBaseActivity;
import com.mobstac.thehindubusinessline.activity.MainActivity;
import com.mobstac.thehindubusinessline.tts.TTSUtil;
import com.mobstac.thehindubusinessline.utils.Constants;
import com.mobstac.thehindubusinessline.utils.DFPConsent;
import com.mobstac.thehindubusinessline.utils.GoogleAnalyticsTracker;
import com.mobstac.thehindubusinessline.utils.SharedPreferenceHelper;
import com.ns.tts.TTSPreference;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment implements CompoundButton.OnCheckedChangeListener , DFPConsent.ConsentSelectionListener{

    private static final String TAG = "SettingsFragment";
    private AdsBaseActivity mMainActivity;
    private SeekBar mSeekBar;
    private TextView mSizeTextView, mSmallArticleTextView, mMediumArticleTextView,
            mLargeArticleTextView, mXLargeTextView, mCityTextView;
    private ImageView mSizeImageView;
    private LinearLayout mSizeLayout;
    private RelativeLayout mArticleSizeLayout;
    private RelativeLayout mCityInterestLayout;
    private Switch mLocationASwitch;
    private Switch mDayModeASwitch;
    private Switch mPushNotification;
    /*TTS Implementation*/
    private RelativeLayout mTtsSettingLayout;
    private TextView tts1TV;
    private TextView tts2TV;

    private DFPConsent mDfpConsent;
    private ProgressDialog mProgressDialog;


    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mMainActivity = (AdsBaseActivity) activity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (AdsBaseActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.layout_settings, container, false);
        if (mMainActivity != null) {
            mMainActivity.showBottomNavigationBar(false);
        }
        mSizeTextView = (TextView) mRootView.findViewById(R.id.textView_size);
        mSeekBar = (SeekBar) mRootView.findViewById(R.id.seekbar_font_size);
        mSizeImageView = (ImageView) mRootView.findViewById(R.id.imageView_size);
        mSizeLayout = (LinearLayout) mRootView.findViewById(R.id.layout_font_size);
        mArticleSizeLayout = (RelativeLayout) mRootView.findViewById(R.id.layout_article_size);
        mCityInterestLayout = (RelativeLayout) mRootView.findViewById(R.id.layout_city_interest);
        mSmallArticleTextView = (TextView) mRootView.findViewById(R.id.textview_article_small);
        mMediumArticleTextView = (TextView) mRootView.findViewById(R.id.textview_article_medium);
        mLargeArticleTextView = (TextView) mRootView.findViewById(R.id.textview_article_large);
        mXLargeTextView = (TextView) mRootView.findViewById(R.id.textview_article_xlarge);
        mCityTextView = (TextView) mRootView.findViewById(R.id.textView_city_interest);
        mLocationASwitch = (Switch) mRootView.findViewById(R.id.switch_location);
        mDayModeASwitch = (Switch) mRootView.findViewById(R.id.switch_daymode);
        mPushNotification = (Switch) mRootView.findViewById(R.id.switch_push_notification);


        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion == Build.VERSION_CODES.JELLY_BEAN
                || currentapiVersion == Build.VERSION_CODES.JELLY_BEAN_MR1
                || currentapiVersion == Build.VERSION_CODES.JELLY_BEAN_MR2) {
            mDayModeASwitch.setVisibility(View.GONE);
        } else {
            // do something for phones running an SDK before lollipop
        }


        mLocationASwitch.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/SerifOffc.ttf"));
        mDayModeASwitch.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/SerifOffc.ttf"));
        mPushNotification.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/SerifOffc.ttf"));


        mSeekBar.incrementProgressBy(1);
        mSeekBar.setMax(3);

        setUserPreferences();

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                resetTextColors();
                Log.d(TAG, "onProgressChanged: " + progress);
                GoogleAnalyticsTracker.setGoogleAnalyticsEvent(getActivity(), "Setting", "Setting Screen: Article font size changed",
                        "Setting Fragment");
                FlurryAgent.logEvent("Setting Screen: Article font size changed");
                switch (progress) {
                    case 0:
                        mSizeTextView.setText(getActivity().getString(R.string.info_article_small));
                        mSmallArticleTextView.setTextColor(getActivity()
                                .getResources().getColor(R.color.color_primary));
                        break;
                    case 1:
                        mSizeTextView.setText(getActivity().getString(R.string.info_article_medium));
                        mMediumArticleTextView.setTextColor(getActivity()
                                .getResources().getColor(R.color.color_primary));
                        break;
                    case 2:
                        mSizeTextView.setText(getActivity().getString(R.string.info_article_large));
                        mLargeArticleTextView.setTextColor(getActivity()
                                .getResources().getColor(R.color.color_primary));
                        break;
                    case 3:
                        mSizeTextView.setText(getActivity().getString(R.string.info_article_xlarge));
                        mXLargeTextView.setTextColor(getActivity()
                                .getResources().getColor(R.color.color_primary));
                        break;

                }
                SharedPreferenceHelper.putInt(mMainActivity, Constants.SELECTED_FONT_SIZE, progress + 1);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mSizeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleAnalyticsTracker.setGoogleAnalyticsEvent(getActivity(), "Setting", "Setting Screen: Article font button clicked",
                        "Setting Fragment");
                FlurryAgent.logEvent("Setting Screen: Article font button clicked");
                adjustFontSize();
            }
        });

        mArticleSizeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleAnalyticsTracker.setGoogleAnalyticsEvent(getActivity(), "Setting", "Setting Screen: Article font button clicked",
                        "Setting Fragment");
                FlurryAgent.logEvent("Setting Screen: Article font button clicked");
                adjustFontSize();
            }
        });

        mCityInterestLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleAnalyticsTracker.setGoogleAnalyticsEvent(getActivity(), "Setting", "Setting Screen: Cities of intrest dropdown clicked",
                        "Setting Fragment");
                FlurryAgent.logEvent("Setting Screen: Cities of intrest dropdown clicked");
                (getActivity()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.FRAME_CONTENT, new CitiesInterestFragment().newInstance(true))
                        .addToBackStack(null).commit();
            }
        });

        // GDPR Setting Option
        boolean isUserFromEurope = SharedPreferenceHelper.isUserFromEurope(getActivity());
        if(isUserFromEurope) {
            mRootView.findViewById(R.id.gdprSettingTxt).setVisibility(View.VISIBLE);
            mRootView.findViewById(R.id.gdprSettingTxt).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setupDFPConsent();
                }
            });
        }

        return mRootView;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ttsSettingSetup();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (mMainActivity != null) {
            mMainActivity.hideToolbarLogo();
            mMainActivity.setToolbarBackButton(R.mipmap.arrow_back);
            mMainActivity.setToolbarTitle(getResources().getString(R.string.setting_menu));
        }

        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                FragmentManager manager = mMainActivity.getSupportFragmentManager();
                if (manager.getBackStackEntryCount() > 1) {
                    manager.popBackStack();
                } else {
                    mMainActivity.finish();
                }

                GoogleAnalyticsTracker.setGoogleAnalyticsEvent(
                        getActivity(),
                        getString(R.string.ga_action),
                        "Settings: Back button clicked",
                        getString(R.string.setting_menu));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void adjustFontSize() {
        if (mSizeLayout.getVisibility() == View.GONE) {
            mSizeLayout.setVisibility(View.VISIBLE);
        } else {
            mSizeLayout.setVisibility(View.GONE);
        }
    }

    private void setUserPreferences() {
        int progress = SharedPreferenceHelper.getInt(mMainActivity, Constants.SELECTED_FONT_SIZE, 1);
        mSeekBar.setProgress(progress - 1);
        switch (progress - 1) {
            case 0:
                mSizeTextView.setText(getActivity().getString(R.string.info_article_small));
                mSmallArticleTextView.setTextColor(getActivity()
                        .getResources().getColor(R.color.color_primary));
                break;
            case 1:
                mSizeTextView.setText(getActivity().getString(R.string.info_article_medium));
                mMediumArticleTextView.setTextColor(getActivity()
                        .getResources().getColor(R.color.color_primary));
                break;
            case 2:
                mSizeTextView.setText(getActivity().getString(R.string.info_article_large));
                mLargeArticleTextView.setTextColor(getActivity()
                        .getResources().getColor(R.color.color_primary));
                break;
            case 3:
                mSizeTextView.setText(getActivity().getString(R.string.info_article_xlarge));
                mXLargeTextView.setTextColor(getActivity()
                        .getResources().getColor(R.color.color_primary));
                break;

        }

        boolean location = SharedPreferenceHelper.getBoolean(getActivity(), Constants.DETECT_LOCATION, false);
        boolean notificationEnabled = SharedPreferenceHelper.getBoolean(mMainActivity, Constants.NOTIFICATIONS, true);
        boolean daymode = SharedPreferenceHelper.getBoolean(getActivity(), Constants.DAY_MODE, false);
        if (ActivityCompat.checkSelfPermission(mMainActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mMainActivity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationASwitch.setChecked(true);
        } else {
            mLocationASwitch.setChecked(false);
        }
//        mLocationASwitch.setChecked(location);
        mLocationASwitch.setOnCheckedChangeListener(this);
        mDayModeASwitch.setChecked(daymode);
        mDayModeASwitch.setOnCheckedChangeListener(this);
        mPushNotification.setChecked(notificationEnabled);
        mPushNotification.setOnCheckedChangeListener(this);
    }

    private void resetTextColors() {
        mSmallArticleTextView.setTextColor(getActivity().getResources().getColor(R.color.text_warm_grey));
        mMediumArticleTextView.setTextColor(getActivity().getResources().getColor(R.color.text_warm_grey));
        mLargeArticleTextView.setTextColor(getActivity().getResources().getColor(R.color.text_warm_grey));
        mXLargeTextView.setTextColor(getActivity().getResources().getColor(R.color.text_warm_grey));

    }

    @Override
    public void onResume() {
        super.onResume();

        List<String> mSelectedCities = SharedPreferenceHelper.getStringArrayPref(
                getActivity(),
                Constants.PREFERENCES_CITY_INTEREST_NAME);

        mCityTextView.setText("");
        for (int i = 0; i < mSelectedCities.size(); i++) {
            String interestedCities = "";
            if (mCityTextView.getText().length() > 0) {
                interestedCities = mCityTextView.getText() + "," + mSelectedCities.get(i);
                mCityTextView.setText(interestedCities);
            } else {
                interestedCities = mSelectedCities.get(i);
                mCityTextView.setText(interestedCities);
            }
        }

        GoogleAnalyticsTracker.setGoogleAnalyticScreenName(getActivity(), "Setting Screen");
        FlurryAgent.logEvent("Setting Screen");
        FlurryAgent.onPageView();
        setTTSValues();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
        switch (compoundButton.getId()) {
            case R.id.switch_daymode:
                GoogleAnalyticsTracker.setGoogleAnalyticsEvent(getActivity(), "Setting", "Setting Screen: Night mode button clicked",
                        "Setting Fragment");
                FlurryAgent.logEvent("Setting Screen: Night mode button clicked");
                SharedPreferenceHelper.putBoolean(getContext(), Constants.DAY_MODE, checked);

                /*if (checked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }*/

                UiModeManager uiManager = (UiModeManager) getActivity().getSystemService(Context.UI_MODE_SERVICE);
                if (checked) {
                    // uiManager.enableCarMode(0);
                    uiManager.setNightMode(UiModeManager.MODE_NIGHT_YES);
                } else {
                    // uiManager.disableCarMode(0);
                    uiManager.setNightMode(UiModeManager.MODE_NIGHT_NO);
                }


                Intent returnIntent = new Intent(mMainActivity, MainActivity.class);
                returnIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(returnIntent);


                break;
            case R.id.switch_location:
                GoogleAnalyticsTracker.setGoogleAnalyticsEvent(getActivity(), "Setting", "Setting Screen: Detect location button clicked", "Setting Fragment");
                FlurryAgent.logEvent("Setting Screen: Detect location button clicked");
                SharedPreferenceHelper.putBoolean(getActivity(), Constants.DETECT_LOCATION, checked);
                break;
            case R.id.switch_push_notification:
                GoogleAnalyticsTracker.setGoogleAnalyticsEvent(getActivity(), "Setting", "Setting Screen: Push Notification  button clicked", "Setting Fragment");
                FlurryAgent.logEvent("Setting Screen: Push Notification button clicked");
                SharedPreferenceHelper.putBoolean(getActivity(), Constants.NOTIFICATIONS, checked);
                break;
        }

    }

    private void ttsSettingSetup() {

        mTtsSettingLayout = (RelativeLayout) getView().findViewById(R.id.ttsSettingLayout);
        tts1TV = (TextView) getView().findViewById(R.id.tts1);
        tts2TV = (TextView) getView().findViewById(R.id.tts2);


        mTtsSettingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TTSUtil.openTTSAppSettingScreen(getActivity());
            }
        });

        setTTSValues();

    }


    private void setTTSValues() {
        if (tts2TV != null) {
            final TTSPreference ttsPreference = TTSPreference.getInstance(getActivity());
            tts2TV.setText(ttsPreference.getDisplayName() + ", " + ttsPreference.getSpeedInString());
        }
    }

    private void setupDFPConsent() {
        mProgressDialog = ProgressDialog.show(getActivity(), null, "Please wait.");
        mDfpConsent = new DFPConsent();
        mDfpConsent.setConsentSelectionListener(this);
        mDfpConsent.init(getActivity(), true);
    }

    @Override
    public void isUserInEurope(boolean isInEurope) {
        if(mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        mDfpConsent.initUserConsentForm(getActivity());
    }
}
