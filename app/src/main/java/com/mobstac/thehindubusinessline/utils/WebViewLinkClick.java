package com.mobstac.thehindubusinessline.utils;

import android.content.Context;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by ashwanisingh on 11/10/18.
 */

public class WebViewLinkClick {


    public WebViewLinkClick() {

    }

    public void linkClick(WebView webView, final Context context) {
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        //javascript support
        webView.getSettings().setJavaScriptEnabled(true);
        //html5 support
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setLoadWithOverviewMode(true);


        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //Clicked url
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(context, Uri.parse(url));
                return true;
            }
        });
    }






}
