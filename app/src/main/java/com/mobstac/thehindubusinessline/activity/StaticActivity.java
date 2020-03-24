package com.mobstac.thehindubusinessline.activity;

import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.RenderProcessGoneDetail;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.mobstac.thehindubusinessline.R;
import com.mobstac.thehindubusinessline.utils.Constants;
import com.mobstac.thehindubusinessline.utils.GoogleAnalyticsTracker;
import com.mobstac.thehindubusinessline.utils.SharedPreferenceHelper;
import com.mobstac.thehindubusinessline.view.AutoResizeWebview;

/**
 * Created by arvind on 14/10/16.
 */

public class StaticActivity extends  BaseActivity {
    public static final String ARG_PARAM1 = "param1";
    private ActionBar mActionBar;
    private String mParam1;
    private TextView mTextview;
    private FrameLayout mContainer;
    private Toolbar mToolbar;
    private ProgressBar mProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_view);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        mParam1 = getIntent().getStringExtra(ARG_PARAM1);
        mContainer = (FrameLayout) findViewById(R.id.container);

        if (mParam1 != null) {
            if (mParam1.equalsIgnoreCase(getString(R.string.info_help))) {
                View view = getLayoutInflater().inflate(R.layout.how_to, null);
                mTextview = (TextView) view.findViewById(R.id.how_to_content);
                String content = getResources().getString(R.string.how_to);
                content = Html.fromHtml(content).toString();
                mTextview.setText(content);
                mContainer.addView(view);
            } else if (mParam1.equalsIgnoreCase(getString(R.string.info_termsconditions))) {




                View view = getLayoutInflater().inflate(R.layout.layout_privacy_policy, null);
                AutoResizeWebview webview = (AutoResizeWebview) view.findViewById(R.id.privacy_policy);

               webview.getSettings().setJavaScriptEnabled(true);
                webview.setWebViewClient(new WebViewClient() {


                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        super.onPageStarted(view, url, favicon);
                        mProgressBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        return true;
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        mProgressBar.setVisibility(View.GONE);
                    }


                });
                webview.loadUrl("http://www.thehindu.com/termsofuse/");
                mContainer.addView(view);
                mProgressBar.setVisibility(View.GONE);




                

            } else if (mParam1.equalsIgnoreCase(getString(R.string.info_aboutus))) {
                View view = getLayoutInflater().inflate(R.layout.about_us, null);
                TextView mTextView = (TextView) view.findViewById(R.id.textview_aboutsu_logo);
                TextView mAutoResizeWebview = (TextView) view.findViewById(R.id.aboutUsWebView);
//                AutoResizeWebview mAutoResizeWebview = (AutoResizeWebview) view.findViewById(R.id.aboutUsWebView);
//                showDescription(Constants.ABOUT_US_TEXT, mAutoResizeWebview);


                String content = getResources().getString(R.string.about_us);
                content = Html.fromHtml(content).toString();
                mAutoResizeWebview.setText(content);



                boolean b;
                b = SharedPreferenceHelper.getBoolean(this,
                        Constants.DAY_MODE,
                        false);
                Log.d("TAG", "onCreate: StaticActivity isNightModeEnabled "+b);

                UiModeManager uiManager = (UiModeManager) getSystemService(Context.UI_MODE_SERVICE);
                if (b) {
                    Drawable img = getResources().getDrawable(R.mipmap.logo);
                    img.setBounds(0, 0, 30, 30);
                    mTextView.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                    uiManager.setNightMode(UiModeManager.MODE_NIGHT_YES);
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    Drawable img = getResources().getDrawable(R.mipmap.logo);
                    img.setBounds(0, 0, 30, 30);
                    mTextView.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                    uiManager.setNightMode(UiModeManager.MODE_NIGHT_NO);
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                mContainer.addView(view);
                mProgressBar.setVisibility(View.GONE);
            } else if (mParam1.equalsIgnoreCase(getString(R.string.privacy_policy))) {
                View view = getLayoutInflater().inflate(R.layout.layout_privacy_policy, null);
                AutoResizeWebview webview = (AutoResizeWebview) view.findViewById(R.id.privacy_policy);


                webview.getSettings().setJavaScriptEnabled(true);
                webview.setWebViewClient(new WebViewClient() {

                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        super.onPageStarted(view, url, favicon);
                        mProgressBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        return true;
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        mProgressBar.setVisibility(View.GONE);
                    }


                });
                webview.loadUrl("https://www.thehindubusinessline.com/privacypolicy/");
                mContainer.addView(view);
                mProgressBar.setVisibility(View.GONE);
            }
            getSupportActionBar().setTitle(mParam1);
        }
    }

    private void showDescription(String description, AutoResizeWebview webView) {
        if (description != null) {
            description = description.trim();
        }

        final boolean nightMode = SharedPreferenceHelper.
                getBoolean(StaticActivity.this, Constants.DAY_MODE, false);


        if (!nightMode) {
            description = "<html><head>"
                    + "<style type=\"text/css\">body{color: #000; background-color: #ffffff;}" +
                    "@font-face {\n" +
                    "   font-family: 'tundra';\n" +
                    "   src: url('file:///android_asset/fonts/TundraOffc.ttf');" +
                    "} " +
                    "body {font-family: 'tundra';}"
                    + "</style></head>"
                    + "<body>"
                    + description
                    + "</body></html>";
        } else {
            description = "<html><head>"
                    + "<style type=\"text/css\">body{color: #fff; background-color: #181818;}" +
                    "@font-face {\n" +
                    "   font-family: 'tundra';\n" +
                    "   src: url('file:///android_asset/fonts/TundraOffc.ttf');" +
                    "} " +
                    "body {font-family: 'tundra';}"
                    + "</style></head>"
                    + "<body>"
                    + description
                    + "</body></html>";
        }
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                return true;
            }
        });
        webView.loadDataWithBaseURL("https:/", description, "text/html", "UTF-8", null);
        webView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                GoogleAnalyticsTracker.setGoogleAnalyticsEvent(
                        StaticActivity.this,
                        getString(R.string.ga_action),
                        "Static Page: Back button clicked",
                        getString(R.string.setting_menu));
                FlurryAgent.logEvent("Static Page: Back button clicked");
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mToolbar.setTitle(mParam1);
        GoogleAnalyticsTracker.setGoogleAnalyticScreenName(this, "" + mParam1 + " Screen");

        FlurryAgent.logEvent(mParam1 + " Screen");
        FlurryAgent.onPageView();
    }
}
