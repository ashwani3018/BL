package com.ns.utils;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.netoperation.model.MeBean;
import com.netoperation.model.RecoBean;
import com.netoperation.net.ApiManager;
import com.netoperation.util.THPPreferences;
import com.ns.activity.AppTabActivity;
import com.ns.activity.BaseAcitivityTHP;
import com.ns.activity.BecomeMemberActivity;
import com.ns.activity.DemoActivity;
import com.ns.activity.SignInAndUpActivity;
import com.ns.activity.THPImageGallaryActivity;
import com.ns.activity.THPImageGallaryVerticleActivity;
import com.ns.activity.THPPersonaliseActivity;
import com.ns.activity.THPUserProfileActivity;
import com.ns.activity.THP_BookmarkActivity;
import com.ns.activity.THP_DetailActivity;
import com.ns.activity.THP_WebActivity;
import com.ns.activity.THP_YouTubeFullScreenActivity;
import com.ns.model.ImageGallaryUrl;
import com.ns.thpremium.BuildConfig;
import com.ns.thpremium.R;
import com.twitter.sdk.android.core.TwitterCore;

import java.net.URLEncoder;
import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;


public class IntentUtil {


    /**
     * Updated requirement 11 Oct 2019
     * If user is relaunching app in Offline mode, do not consider as logged out for Any login type
     * If user comes online, hit this method again
     * If login API fails, consider as logout
     *
     * This is added as per @Ravi discussion requirement (12 Sep 2019)
     * If User is relaunching the app then,
     * If user had social logged-in then logout
     * If user had default logged-in then re-login with saved login id and passwd
     * If user default re-login is failed then mark as Not-Logged In
     * If Internet is not available or User is Not-Logged in then user should be redirected to SignUp and SignIn Screen.
     */
    public static void loginApiCall(Activity context) {

        String loginId = THPPreferences.getInstance(context).getLoginId();
        String loginPasswd = THPPreferences.getInstance(context).getLoginPasswd();
        String userName = THPPreferences.getInstance(context).getUserLoggedName();
        String deviceId = ResUtil.getDeviceId(context);

        String email = "";
        String mobile = "";

        boolean isFacebookLogin = false;
        boolean isTwitterLogin = false;
        boolean isGoogleLogin = false;
        boolean isDefaultLogin = false;


        if(ResUtil.isValidEmail(loginId)) {
            email = loginId;
            isDefaultLogin = true;
        } else if(ResUtil.isValidMobile(loginId)) {
            mobile = loginId;
            isDefaultLogin = true;
        } else if(loginId.equalsIgnoreCase("facebook")) {
            isFacebookLogin = true;
        } else if(loginId.equalsIgnoreCase("twitter")) {
            isTwitterLogin = true;
        } else if(loginId.equalsIgnoreCase("google")) {
            isGoogleLogin = true;
        }

        if(isFacebookLogin) {
            /*THPPreferences.getInstance(context).clearPref();
            // Facebook Logout
            if(AccessToken.getCurrentAccessToken() != null) {
                AccessToken.setCurrentAccessToken(null);
            }*/

            String socialId = loginPasswd;
            String userEmail = loginId;

            ApiManager.socialLogin(context, deviceId, BuildConfig.ORIGIN_URL, "Facebook", socialId, userEmail, userName, BuildConfig.SITEID)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(keyValueModel -> {
                        String userId = keyValueModel.getUserId();
                        if (ResUtil.isEmpty(userId)) {
                            THPPreferences.getInstance(context).setIsUserLoggedIn(false);
                            THPPreferences.getInstance(context).clearPref();
                            // Facebook Logout
                            if(AccessToken.getCurrentAccessToken() != null) {
                                AccessToken.setCurrentAccessToken(null);
                            }
                        } else {
                            THPPreferences.getInstance(context).setIsUserLoggedIn(true);

                        }
                    }, throwable -> {
                        THPPreferences.getInstance(context).setIsUserLoggedIn(false);
                        THPPreferences.getInstance(context).clearPref();
                        // Facebook Logout
                        if(AccessToken.getCurrentAccessToken() != null) {
                            AccessToken.setCurrentAccessToken(null);
                        }
                    });
        }
        else if(isTwitterLogin) {
            /*THPPreferences.getInstance(context).clearPref();
            try {
                // Twitter Logout
                if (TwitterCore.getInstance().getSessionManager().getActiveSession() != null) {
                    CookieSyncManager.createInstance(context);
                    CookieManager cookieManager = CookieManager.getInstance();
                    cookieManager.removeSessionCookie();
                    TwitterCore.getInstance().getSessionManager().clearActiveSession();
                }
            } catch (Exception ignore){
                Log.i("", "");
            }*/

            String socialId = loginPasswd;
            String userEmail = loginId;

            ApiManager.socialLogin(context, deviceId, BuildConfig.ORIGIN_URL, "Twitter", socialId, userEmail, userName, BuildConfig.SITEID)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(keyValueModel -> {
                        String userId = keyValueModel.getUserId();
                        if (ResUtil.isEmpty(userId)) {

                            THPPreferences.getInstance(context).setIsUserLoggedIn(false);
                            THPPreferences.getInstance(context).clearPref();
                            try {
                                // Twitter Logout
                                if (TwitterCore.getInstance().getSessionManager().getActiveSession() != null) {
                                    CookieSyncManager.createInstance(context);
                                    CookieManager cookieManager = CookieManager.getInstance();
                                    cookieManager.removeSessionCookie();
                                    TwitterCore.getInstance().getSessionManager().clearActiveSession();
                                }
                            } catch (Exception ignore){
                                Log.i("", "");
                            }
                        } else {
                            THPPreferences.getInstance(context).setIsUserLoggedIn(true);
                        }
                    }, throwable -> {
                        THPPreferences.getInstance(context).setIsUserLoggedIn(false);
                        THPPreferences.getInstance(context).clearPref();
                        try {
                            // Twitter Logout
                            if (TwitterCore.getInstance().getSessionManager().getActiveSession() != null) {
                                CookieSyncManager.createInstance(context);
                                CookieManager cookieManager = CookieManager.getInstance();
                                cookieManager.removeSessionCookie();
                                TwitterCore.getInstance().getSessionManager().clearActiveSession();
                            }
                        } catch (Exception ignore){
                            Log.i("", "");
                        }
                    });
        }
        else if(isGoogleLogin) {

            String socialId = loginPasswd;
            String userEmail = loginId;

            ApiManager.socialLogin(context, deviceId, BuildConfig.ORIGIN_URL, "Google", socialId, userEmail, userName, BuildConfig.SITEID)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(keyValueModel -> {
                        String userId = keyValueModel.getUserId();
                        if (ResUtil.isEmpty(userId)) {
                            THPPreferences.getInstance(context).setIsUserLoggedIn(false);
                            THPPreferences.getInstance(context).clearPref();

                            //logout
                            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                    .requestIdToken(context.getResources().getString(com.ns.thpremium.R.string.default_web_client_id))
                                    .build();

                            // Build a GoogleApiClient with access to the Google Sign-In API and the
                            // options specified by gso.
                            GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(context, gso);

                            // Google Logout
                            if(mGoogleSignInClient != null) {
                                GoogleSignInAccount googleAccount = GoogleSignIn.getLastSignedInAccount(context);
                                if(googleAccount!=null) {
                                    mGoogleSignInClient.signOut()
                                            .addOnCompleteListener(context, task -> {
                                                //Signed out
                                            });
                                }

                            }
                        } else {
                            THPPreferences.getInstance(context).setIsUserLoggedIn(true);
                        }
                    }, throwable -> {
                        THPPreferences.getInstance(context).setIsUserLoggedIn(false);
                        THPPreferences.getInstance(context).clearPref();

                        //logout
                        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                .requestIdToken(context.getResources().getString(com.ns.thpremium.R.string.default_web_client_id))
                                .build();

                        // Build a GoogleApiClient with access to the Google Sign-In API and the
                        // options specified by gso.
                        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(context, gso);

                        // Google Logout
                        if(mGoogleSignInClient != null) {
                            GoogleSignInAccount googleAccount = GoogleSignIn.getLastSignedInAccount(context);
                            if(googleAccount!=null) {
                                mGoogleSignInClient.signOut()
                                        .addOnCompleteListener(context, task -> {
                                            //Signed out
                                        });
                            }

                        }

                    });
        }
        else if(isDefaultLogin) {
            ApiManager.userLogin(null, email, mobile, BuildConfig.SITEID, loginPasswd, deviceId, BuildConfig.ORIGIN_URL, false)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(keyValueModel -> {

                                String userId = "";

                                if (keyValueModel.getState() != null && keyValueModel.getState().equalsIgnoreCase("success")) {
                                    userId = keyValueModel.getCode();
                                }

                                if (TextUtils.isEmpty(userId)) { // Fail
                                    THPPreferences.getInstance(context).setIsUserLoggedIn(false);
                                } else { // Success
                                    // Making server request to get User Info
                                    THPPreferences.getInstance(context).setIsUserLoggedIn(true);
                                }
                            }, throwable -> {
                                THPPreferences.getInstance(context).setIsUserLoggedIn(false);
                            },
                            () -> {

                            });
        }
        else {
            THPPreferences.getInstance(context).clearPref();
        }
    }

    /*
     * Similar to @loginApiCall(), Do not clear preferences, and sessions of social profiles if api connection fails
     * */
    public static void loginApiCall2(Activity context, CallBackRelogin callBackReloginListener) {
        String loginId = THPPreferences.getInstance(context).getLoginId();
        String loginPasswd = THPPreferences.getInstance(context).getLoginPasswd();
        String userName = THPPreferences.getInstance(context).getUserLoggedName();
        String deviceId = ResUtil.getDeviceId(context);

        String email = "";
        String mobile = "";

        boolean isFacebookLogin = false;
        boolean isTwitterLogin = false;
        boolean isGoogleLogin = false;
        boolean isDefaultLogin = false;


        if (ResUtil.isValidEmail(loginId)) {
            email = loginId;
            isDefaultLogin = true;
        } else if (ResUtil.isValidMobile(loginId)) {
            mobile = loginId;
            isDefaultLogin = true;
        } else if (loginId.equalsIgnoreCase("facebook")) {
            isFacebookLogin = true;
        } else if (loginId.equalsIgnoreCase("twitter")) {
            isTwitterLogin = true;
        } else if (loginId.equalsIgnoreCase("google")) {
            isGoogleLogin = true;
        }

        if (isFacebookLogin) {

            String socialId = loginPasswd;
            String userEmail = loginId;

            ApiManager.socialLogin(context, deviceId, BuildConfig.ORIGIN_URL, "Facebook", socialId, userEmail, userName, BuildConfig.SITEID)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(keyValueModel -> {
                        THPPreferences.getInstance(context).setIsRelogginSuccess(true);
                        String userId = keyValueModel.getUserId();
                        if (ResUtil.isEmpty(userId)) {
                            THPPreferences.getInstance(context).setIsUserLoggedIn(false);
                            THPPreferences.getInstance(context).clearPref();
                            // Facebook Logout
                            if (AccessToken.getCurrentAccessToken() != null) {
                                AccessToken.setCurrentAccessToken(null);
                            }
                            callBackReloginListener.OnFailure();
                        } else {
                            THPPreferences.getInstance(context).setIsUserLoggedIn(true);
                            callBackReloginListener.OnSuccess();
                        }
                    }, throwable -> {
                        THPPreferences.getInstance(context).setIsRelogginSuccess(false);
                        callBackReloginListener.OnFailure();
                        //THPPreferences.getInstance(context).setIsUserLoggedIn(false);
                        //THPPreferences.getInstance(context).clearPref();
                        // Facebook Logout
                        /*if(AccessToken.getCurrentAccessToken() != null) {
                            AccessToken.setCurrentAccessToken(null);
                        }*/
                    });
        } else if (isTwitterLogin) {

            String socialId = loginPasswd;
            String userEmail = loginId;

            ApiManager.socialLogin(context, deviceId, BuildConfig.ORIGIN_URL, "Twitter", socialId, userEmail, userName, BuildConfig.SITEID)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(keyValueModel -> {
                        THPPreferences.getInstance(context).setIsRelogginSuccess(true);
                        String userId = keyValueModel.getUserId();
                        if (ResUtil.isEmpty(userId)) {

                            THPPreferences.getInstance(context).setIsUserLoggedIn(false);
                            THPPreferences.getInstance(context).clearPref();
                            try {
                                // Twitter Logout
                                if (TwitterCore.getInstance().getSessionManager().getActiveSession() != null) {
                                    CookieSyncManager.createInstance(context);
                                    CookieManager cookieManager = CookieManager.getInstance();
                                    cookieManager.removeSessionCookie();
                                    TwitterCore.getInstance().getSessionManager().clearActiveSession();
                                }
                            } catch (Exception ignore) {
                                Log.i("", "");
                            }
                            callBackReloginListener.OnFailure();
                        } else {
                            THPPreferences.getInstance(context).setIsUserLoggedIn(true);
                            callBackReloginListener.OnSuccess();
                        }
                    }, throwable -> {
                        THPPreferences.getInstance(context).setIsRelogginSuccess(false);
                        callBackReloginListener.OnFailure();
                        //THPPreferences.getInstance(context).setIsUserLoggedIn(false);
                        //THPPreferences.getInstance(context).clearPref();
                        /*try {
                            // Twitter Logout
                            if (TwitterCore.getInstance().getSessionManager().getActiveSession() != null) {
                                CookieSyncManager.createInstance(context);
                                CookieManager cookieManager = CookieManager.getInstance();
                                cookieManager.removeSessionCookie();
                                TwitterCore.getInstance().getSessionManager().clearActiveSession();
                            }
                        } catch (Exception ignore){
                            Log.i("", "");
                        }*/
                    });
        } else if (isGoogleLogin) {

            String socialId = loginPasswd;
            String userEmail = loginId;

            ApiManager.socialLogin(context, deviceId, BuildConfig.ORIGIN_URL, "Google", socialId, userEmail, userName, BuildConfig.SITEID)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(keyValueModel -> {
                        THPPreferences.getInstance(context).setIsRelogginSuccess(true);
                        String userId = keyValueModel.getUserId();
                        if (ResUtil.isEmpty(userId)) {
                            THPPreferences.getInstance(context).setIsUserLoggedIn(false);
                            THPPreferences.getInstance(context).clearPref();

                            //logout
                            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                    .requestIdToken(context.getResources().getString(com.ns.thpremium.R.string.default_web_client_id))
                                    .build();

                            // Build a GoogleApiClient with access to the Google Sign-In API and the
                            // options specified by gso.
                            GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(context, gso);

                            // Google Logout
                            if (mGoogleSignInClient != null) {
                                GoogleSignInAccount googleAccount = GoogleSignIn.getLastSignedInAccount(context);
                                if (googleAccount != null) {
                                    mGoogleSignInClient.signOut()
                                            .addOnCompleteListener(context, task -> {
                                                //Signed out
                                            });
                                }

                            }
                            callBackReloginListener.OnFailure();
                        } else {
                            THPPreferences.getInstance(context).setIsUserLoggedIn(true);
                            callBackReloginListener.OnSuccess();
                        }
                    }, throwable -> {
                        THPPreferences.getInstance(context).setIsRelogginSuccess(false);
                        callBackReloginListener.OnFailure();
                        //THPPreferences.getInstance(context).setIsUserLoggedIn(false);
                        //THPPreferences.getInstance(context).clearPref();

                        //logout
                        /*GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                .requestIdToken(context.getResources().getString(com.ns.thpremium.R.string.default_web_client_id))
                                .build();*/

                        // Build a GoogleApiClient with access to the Google Sign-In API and the
                        // options specified by gso.
                        //GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(context, gso);

                        // Google Logout
                        /*if(mGoogleSignInClient != null) {
                            GoogleSignInAccount googleAccount = GoogleSignIn.getLastSignedInAccount(context);
                            if(googleAccount!=null) {
                                mGoogleSignInClient.signOut()
                                        .addOnCompleteListener(context, (OnCompleteListener<Void>) task -> {
                                            //Signed out
                                        });
                            }

                        }*/

                    });
        } else if (isDefaultLogin) {
            ApiManager.userLogin(null, email, mobile, BuildConfig.SITEID, loginPasswd, deviceId, BuildConfig.ORIGIN_URL, false)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(keyValueModel -> {
                                THPPreferences.getInstance(context).setIsRelogginSuccess(true);
                                String userId = "";

                                if (keyValueModel.getState() != null && keyValueModel.getState().equalsIgnoreCase("success")) {
                                    userId = keyValueModel.getCode();
                                }

                                if (TextUtils.isEmpty(userId)) { // Fail
                                    THPPreferences.getInstance(context).setIsUserLoggedIn(false);
                                    callBackReloginListener.OnFailure();
                                } else { // Success
                                    // Making server request to get User Info
                                    THPPreferences.getInstance(context).setIsUserLoggedIn(true);
                                    callBackReloginListener.OnSuccess();
                                }
                            }, throwable -> {
                                THPPreferences.getInstance(context).setIsRelogginSuccess(false);
                                callBackReloginListener.OnFailure();
                                //THPPreferences.getInstance(context).setIsUserLoggedIn(false);
                            },
                            () -> {

                            });
        } else {
            THPPreferences.getInstance(context).setIsRelogginSuccess(true); //Consider as true, in case of FRESH LAUNCH or, User not Logged in
            //THPPreferences.getInstance(context).clearPref();
        }
    }

    private static Bundle startActivityAnim(Context context) {
        Bundle bndlAnimation = ActivityOptions.makeCustomAnimation(context,
                R.anim.activity_in, R.anim.activity_out).toBundle();
        return bndlAnimation;
    }

    public static void openTHPScreen(Context context, boolean isUserLoggedIn, int tabIndex, boolean isHasFreePlan, boolean isHasSubcription, String from) {

        // This is for testing
        /*if(1==1) {
            IntentUtil.openContentListingActivity(context, "", tabIndex);
            return;
        }*/

        if(isUserLoggedIn && (isHasSubcription || isHasFreePlan)) {
            IntentUtil.openContentListingActivity(context, from, tabIndex);
        }
        else if(isUserLoggedIn && !isHasFreePlan) { // THis is for sort time, may be in future in logic or implementation will come
            IntentUtil.openSubscriptionActivity(context, THPConstants.FROM_SUBSCRIPTION_EXPLORE);
        }
        else {
            IntentUtil.openMemberActivity(context, "");
        }

    }

    public static void openContentListingActivity(Context context, String from, int tabIndex) {
        Intent intent = new Intent(context, AppTabActivity.class);
        intent.putExtra("from", from);
        intent.putExtra("tabIndex", tabIndex);
        context.startActivity(intent, startActivityAnim(context));
    }

    public static void openContentListingActivity(Context context, String from) {
        Intent intent = new Intent(context, AppTabActivity.class);
        intent.putExtra("from", from);
        context.startActivity(intent);
    }

    public static void openMemberActivity(Context context, String from) {
        Intent intent = new Intent(context, BecomeMemberActivity.class);
        intent.putExtra("from", from);
        context.startActivity(intent);
    }

    public static void openUserProfileActivity(Context context, String from) {
        if(context instanceof AppCompatActivity) {
            AppCompatActivity tt = (AppCompatActivity) context;
            Intent intent = new Intent(tt, THPUserProfileActivity.class);
            intent.putExtra("from", from);
            tt.startActivityForResult(intent, 100);
        } else {
            Intent intent = new Intent(context, THPUserProfileActivity.class);
            intent.putExtra("from", from);
            context.startActivity(intent);
        }
    }

    public static void openSignInOrUpActivity(Context context, String from) {
        Intent intent = new Intent(context, SignInAndUpActivity.class);
        intent.putExtra("from", from);
        if(context instanceof BaseAcitivityTHP) {
            BaseAcitivityTHP baseAcitivityTHP = (BaseAcitivityTHP) context;
            baseAcitivityTHP.startActivityForResult(intent, 100);
        } else {
            context.startActivity(intent);
        }
    }

    public static void openSubscriptionActivity(Context context, String from) {
        Intent intent = new Intent(context, THPUserProfileActivity.class);
        intent.putExtra("from", from);
        context.startActivity(intent);
    }

    public static void openDetailActivity(Context context, String from, String url, int clickedPosition, String articleId) {
        Intent intent = new Intent(context, THP_DetailActivity.class);
        intent.putExtra("from", from);
        intent.putExtra("url", url);
        intent.putExtra("clickedPosition", clickedPosition);
        intent.putExtra("articleId", articleId);
        context.startActivity(intent);
    }

    public static void openWebActivity(Context context, String from, String url) {
        Intent intent = new Intent(context, THP_WebActivity.class);
        intent.putExtra("from", from);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }


    public static void openCommentActivity(Context context, RecoBean articlesBean) {
        String Vukkle_API_KEY = BuildConfig.VUUKLE_API_KEY;

        String articleId = articlesBean.getArticleId();
        String articleLink = articlesBean.getArticleUrl();
        String articleTitle = articlesBean.getArticletitle();
        String imgUrl = "";
        if(articlesBean.getThumbnailUrl().size() > 0) {
            imgUrl = articlesBean.getThumbnailUrl().get(0);
        }

        Intent intent = new Intent(context, THP_WebActivity.class);

        String host = BuildConfig.ORIGIN_URL;

        String url = "https://cdn.vuukle.com/amp.html?" +
                "apiKey="+ Vukkle_API_KEY+"&" +
                "host="+host+"&" +
                "socialAuth=false"+"&" +
                "id="+articleId+"&" +
                "img="+imgUrl+"&" +
                "title="+ URLEncoder.encode(articleTitle)+"&" +
                "url="+articleLink;
        intent.putExtra("from", context.getResources().getString(R.string.comments));
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    /**
     * Ask the current default engine to launch the matching INSTALL_TTS_DATA activity
     * so the required TTS files are properly installed.
     */
    public static void installVoiceData(Context activity) {
        Intent intent = new Intent(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setPackage("com.google.android.tts"/*replace with the package name of the target TTS engine*/);
        try {
            Log.v("installVoiceData", "Installing voice data: " + intent.toUri(0));
            activity.startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Log.e("installVoiceData", "Failed to install TTS data, no acitivty found for " + intent + ")");
        }
    }

    public static void openHorizontalGalleryActivity(Context context, ArrayList<ImageGallaryUrl> imageGallaryUrls, ArrayList<MeBean> meBeanList, int position) {
        if (meBeanList != null && meBeanList.size() > 0) {
            imageGallaryUrls = new ArrayList<>();
            for (MeBean imageBean : meBeanList) {
                imageGallaryUrls.add(new ImageGallaryUrl(imageBean.getListingImgUrl(), imageBean.getBigImgUrl(), imageBean.getCa(), ""));
            }
        }

        if (imageGallaryUrls != null) {
            Intent intent = new Intent(context, THPImageGallaryActivity.class);
            intent.putParcelableArrayListExtra("ImageUrl", imageGallaryUrls);
            intent.putExtra("selectedPosition", position);
            context.startActivity(intent);
        }
    }




    public static void openVerticleGalleryActivity(Context context, ArrayList<MeBean> mImageList, String title) {
        ArrayList<ImageGallaryUrl> mImageUrlList = null;
        if (mImageList != null && mImageList.size() > 0) {
            mImageUrlList = new ArrayList<ImageGallaryUrl>();
            for (MeBean imageBean : mImageList) {
                mImageUrlList.add(new ImageGallaryUrl(imageBean.getListingImgUrl(), imageBean.getBigImgUrl(), imageBean.getCa(), title));
            }
        }
        if (mImageUrlList != null) {
            Intent intent = new Intent(context, THPImageGallaryVerticleActivity.class);
            intent.putParcelableArrayListExtra("ImageUrl", mImageUrlList);
            intent.putExtra("title", title);
            context.startActivity(intent);
        }

    }

    public static void openPersonaliseActivity(Context context, String from) {
        Intent intent = new Intent(context, THPPersonaliseActivity.class);
        intent.putExtra("from", from);
        context.startActivity(intent);
    }

    public static void openBookmarkActivity(Context context, String from, String userId) {
        Intent intent = new Intent(context, THP_BookmarkActivity.class);
        intent.putExtra("from", from);
        intent.putExtra("userId", userId);
        context.startActivity(intent);
    }

    public static void openYoutubeActivity(Context context, String videoId) {
            Intent intent = new Intent(context, THP_YouTubeFullScreenActivity.class);
            intent.putExtra("videoId", videoId);
            context.startActivity(intent);
    }

    public static void openTheHinduMainActivity(Context context) {
        Intent intent = new Intent();
        intent.setAction("com.mobstac.thehindu.mainactivity.opening");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public static void simpleDemo(Context context) {
        Intent intent = new Intent(context, DemoActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public static void openAppSetting(Context context) {
        Intent intent = new Intent();
        intent.setAction(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",
                "com.mobstac.thehindu", null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void clearAllPreviousActivity(AppCompatActivity activity) {
        ActivityCompat.finishAffinity(activity);
    }

}
