package com.mobstac.thehindubusinessline.holder;

import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mobstac.thehindubusinessline.R;
import com.mobstac.thehindubusinessline.view.AutoResizeWebview;


public class StaticItemWebViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "StaticItemWebViewHolder";
    
    public AutoResizeWebview webView;
    public ProgressBar progressBar;
    public View mDummyView;


    public StaticItemWebViewHolder(View itemView) {
        super(itemView);
        mDummyView = itemView.findViewById(R.id.dummyView);
        webView = itemView.findViewById(R.id.staticWebView);
        progressBar = itemView.findViewById(R.id.progressBar);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                    webView.loadUrl("about:blank");
                    webView.loadUrl("file:///android_asset/web/web_error.html");
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                    webView.loadUrl("about:blank");
                    webView.loadUrl("file:///android_asset/web/web_error.html");
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
            }
        });

        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        //javascript support
        webView.getSettings().setJavaScriptEnabled(true);
        //html5 support
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setLoadWithOverviewMode(true);
    }

}
