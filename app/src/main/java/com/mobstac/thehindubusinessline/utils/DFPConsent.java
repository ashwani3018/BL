package com.mobstac.thehindubusinessline.utils;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.ads.consent.ConsentForm;
import com.google.ads.consent.ConsentFormListener;
import com.google.ads.consent.ConsentInfoUpdateListener;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.google.ads.consent.DebugGeography;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.ads.mediation.facebook.FacebookAdapter;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.mobstac.thehindubusinessline.BuildConfig;
import com.mobstac.thehindubusinessline.activity.InitialSetupActivity;
import com.mobstac.thehindubusinessline.activity.MainActivity;
import com.moengage.core.MoEngage;

import java.net.MalformedURLException;
import java.net.URL;

public class DFPConsent {

    public void init(final Context context, final boolean forceForConsentDialog) {

        final ConsentInformation consentInformation = ConsentInformation.getInstance(context);
        String networkCode = "22390678";
        String[] publisherIds = {networkCode};
        consentInformation.requestConsentInfoUpdate(publisherIds, new ConsentInfoUpdateListener() {
            @Override
            public void onConsentInfoUpdated(ConsentStatus consentStatus) {
                // User's consent status successfully updated.
                boolean isInEurope = ConsentInformation.getInstance(context).isRequestLocationInEeaOrUnknown();
                boolean isUserFromEurope = SharedPreferenceHelper.isUserFromEurope(context);

                SharedPreferenceHelper.setDfpConsentExecuted(context, true);
                SharedPreferenceHelper.setUserFromEurope(context, isInEurope);
                if(!isInEurope) {
                    SharedPreferenceHelper.setUserPreferAdsFree(context, false);
                }
                // MO-ENGAGE GDPR
                updateMoengageGDPR(context, isInEurope);

                if((mConsentSelectionListener != null && !isUserFromEurope) || forceForConsentDialog) {
                    mConsentSelectionListener.isUserInEurope(isInEurope);
                }

            }

            @Override
            public void onFailedToUpdateConsentInfo(String errorDescription) {
                // User's consent status failed to update.
                Log.i("","");
                consentInformation.reset();
            }
        });
    }

    private ConsentForm form;

    public void initUserConsentForm(final Context context) {
        URL privacyUrl = null;
        try {
            privacyUrl = new URL("http://www.thehindu.com/privacypolicy");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            // Handle error.
        }
        form = new ConsentForm.Builder(context, privacyUrl)
                .withListener(new ConsentFormListener() {
                    @Override
                    public void onConsentFormLoaded() {
                        // Consent form loaded successfully.
                        if(context instanceof InitialSetupActivity) {
                            InitialSetupActivity activity = (InitialSetupActivity) context;
                            if(!activity.isFinishing()) {
                                form.show();
                            }
                        } else if(context instanceof MainActivity) {
                            MainActivity activity = (MainActivity) context;
                            if(!activity.isFinishing()) {
                                form.show();
                            }
                        }
                        else {
                            form.show();
                        }
                    }

                    @Override
                    public void onConsentFormOpened() {
                        // Consent form was displayed.
                        Log.i("","");
                    }

                    @Override
                    public void onConsentFormClosed(
                            ConsentStatus consentStatus, Boolean userPrefersAdFree) {
                        // Consent form was closed.
                        SharedPreferenceHelper.setUserSelectedDfpConsent(context, true);
                        SharedPreferenceHelper.setUserPreferAdsFree(context, userPrefersAdFree);

                    }

                    @Override
                    public void onConsentFormError(String errorDescription) {
                        // Consent form error.
                        Log.i("","");
                    }
                })
//                .withPersonalizedAdsOption()
                .withNonPersonalizedAdsOption()
//                .withAdFreeOption()
                .build();

        form.load();

    }


    private ConsentSelectionListener mConsentSelectionListener;

    public void setConsentSelectionListener(ConsentSelectionListener consentSelectionListener) {
        this.mConsentSelectionListener = consentSelectionListener;
    }

    public interface ConsentSelectionListener {
        void isUserInEurope(boolean isInEurope);
    }


    public static void GDPR_Testing(Context context) {
        if(BuildConfig.DEBUG) {
            ConsentInformation.getInstance(context);
            /*boolean ENABLE_EEA = SharedPreferenceHelper.isGDPR_EEA(context);
            if (ENABLE_EEA) {
                ConsentInformation.getInstance(context).setDebugGeography(DebugGeography.DEBUG_GEOGRAPHY_EEA);
            } else {
                ConsentInformation.getInstance(context).setDebugGeography(DebugGeography.DEBUG_GEOGRAPHY_NOT_EEA);
            }*/

            ConsentInformation.getInstance(context).setDebugGeography(DebugGeography.DEBUG_GEOGRAPHY_EEA);
//            ConsentInformation.getInstance(context).setDebugGeography(DebugGeography.DEBUG_GEOGRAPHY_NOT_EEA);
        }
    }

    private void updateMoengageGDPR(Context context, boolean isEEA) {

        /*//true if you don't want to track user data, else false
        MoEngage.optOutDataTracking(context, true);
        //true if you don't want users to receive push
        MoEngage.optOutPushNotification(context, true);
        //true if you don't want users to receive in-app
        MoEngage.optOutInAppNotification(context, true);*/

        MoEngage.optOutDataTracking(context, isEEA);
        MoEngage.optOutPushNotification(context, isEEA);
        MoEngage.optOutInAppNotification(context, isEEA);
    }

    public static Bundle GDPRStatusBundle(Context context) {
        Bundle nonPersonalizedAdsReqBundle = null;
        final ConsentStatus consentStatus = ConsentInformation.getInstance(context).getConsentStatus();
        switch (consentStatus) {
            case PERSONALIZED:

                break;

            case NON_PERSONALIZED:
                nonPersonalizedAdsReqBundle = new Bundle();
                nonPersonalizedAdsReqBundle.putString("npa", "1");
                break;

            case UNKNOWN:

                break;
        }
        return nonPersonalizedAdsReqBundle;
    }





    public static PublisherAdRequest getDefaultUrlAdsRequest(Context context) {
        Bundle nonPersonalizedAdsReqBundle = GDPRStatusBundle(context);
        PublisherAdRequest request;
        if(nonPersonalizedAdsReqBundle != null) {
            Bundle extras = new FacebookAdapter.FacebookExtrasBundleBuilder()
                    .setNativeAdChoicesIconExpandable(false)
                    .build();
            request = new PublisherAdRequest.Builder()
                    .addNetworkExtrasBundle(AdMobAdapter.class, nonPersonalizedAdsReqBundle)
                    .addNetworkExtrasBundle(FacebookAdapter.class, extras)
                    .setContentUrl(Constants.THE_HINDU_URL)
                    .build();
        } else {
            request = new PublisherAdRequest.Builder().setContentUrl(Constants.THE_HINDU_URL)
                    .build();
        }

        return request;
    }

    public static PublisherAdRequest getCustomUrlAdsRequest(Context context, String url) {
        Bundle nonPersonalizedAdsReqBundle = GDPRStatusBundle(context);
        PublisherAdRequest request;
        if(nonPersonalizedAdsReqBundle != null) {
            Bundle extras = new FacebookAdapter.FacebookExtrasBundleBuilder()
                    .setNativeAdChoicesIconExpandable(false)
                    .build();
            request = new PublisherAdRequest.Builder()
                    .addNetworkExtrasBundle(AdMobAdapter.class, nonPersonalizedAdsReqBundle)
                    .addNetworkExtrasBundle(FacebookAdapter.class, extras)
                    .setContentUrl(Constants.THE_HINDU_URL)
                    .build();
        } else {
            request = new PublisherAdRequest.Builder().setContentUrl(url)
                    .build();
        }

        return request;
    }


    public static PublisherAdRequest getNoUrlAdsRequest(Context context) {
        Bundle nonPersonalizedAdsReqBundle = GDPRStatusBundle(context);
        PublisherAdRequest request;
        if(nonPersonalizedAdsReqBundle != null) {
            Bundle extras = new FacebookAdapter.FacebookExtrasBundleBuilder()
                    .setNativeAdChoicesIconExpandable(false)
                    .build();
            request = new PublisherAdRequest.Builder()
                    .addNetworkExtrasBundle(AdMobAdapter.class, nonPersonalizedAdsReqBundle)
                    .addNetworkExtrasBundle(FacebookAdapter.class, extras)
                    .setContentUrl(Constants.THE_HINDU_URL)
                    .build();
        } else {
            request = new PublisherAdRequest.Builder().build();
        }

        return request;
    }




    public static PublisherAdRequest getWithoutUrlAdsRequest(Context context) {
        Bundle nonPersonalizedAdsReqBundle = GDPRStatusBundle(context);
        PublisherAdRequest request;
        if(nonPersonalizedAdsReqBundle != null) {
            Bundle extras = new FacebookAdapter.FacebookExtrasBundleBuilder()
                    .setNativeAdChoicesIconExpandable(false)
                    .build();
            request = new PublisherAdRequest.Builder()
                    .addNetworkExtrasBundle(AdMobAdapter.class, nonPersonalizedAdsReqBundle)
                    .addNetworkExtrasBundle(FacebookAdapter.class, extras)
                    .setContentUrl(Constants.THE_HINDU_URL)
                    .build();
        } else {
            request = new PublisherAdRequest.Builder().build();
        }

        return request;
    }


}
