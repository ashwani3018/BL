package com.ns.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;

import com.ns.loginfragment.SignInAndUpFragment;
import com.ns.loginfragment.SignInFragment;
import com.ns.loginfragment.SignUpFragment;
import com.ns.thpremium.R;
import com.ns.utils.FragmentUtil;
import com.ns.utils.THPFirebaseAnalytics;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SignInAndUpActivity extends BaseAcitivityTHP {

    @Override
    public int layoutRes() {
        return R.layout.activity_sign_in_and_up;
    }

    public String mFrom;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        printHashKey();
       // configureTwitter();
       setContentView(layoutRes());

        mFrom = getIntent().getExtras().getString("from");

        SignInAndUpFragment fragment = SignInAndUpFragment.getInstance(mFrom);
        FragmentUtil.pushFragmentAnim(this, R.id.parentLayout, fragment, FragmentUtil.FRAGMENT_NO_ANIMATION, true);

    }


    public void printHashKey(){
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.mobstac.thehindu",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

//    private void configureTwitter() {
//        TwitterConfig config = new TwitterConfig.Builder(this)
//                .logger(new DefaultLogger(Log.DEBUG))//enable logging when app is in debug mode
//                .twitterAuthConfig(new TwitterAuthConfig(getResources().getString(R.string.CONSUMER_KEY), getResources().getString(R.string.CONSUMER_SECRET)))//pass the created app Consumer KEY and Secret also called API Key and Secret
//                .debug(true)//enable debug mode
//                .build();
//
//        //finally initialize twitter with created configs
//        Twitter.initialize(config);
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == TwitterAuthConfig.DEFAULT_AUTH_REQUEST_CODE) {
            try {
                SignInFragment.getInstance().onActivityResult(requestCode, resultCode, data);
                SignUpFragment.getInstance().onActivityResult(requestCode, resultCode, data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        /*for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            try {
                SignInFragment signInFragment = ((SignInFragment) fragment);
                signInFragment.onActivityResult(requestCode, resultCode, data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        THPFirebaseAnalytics.setFirbaseAnalyticsScreenRecord(this, "SignInAndUpActivity Screen", SignInAndUpActivity.class.getSimpleName());
    }
}
