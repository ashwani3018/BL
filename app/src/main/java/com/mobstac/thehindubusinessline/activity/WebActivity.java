package com.mobstac.thehindubusinessline.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.mobstac.thehindubusinessline.R;
import com.mobstac.thehindubusinessline.utils.Constants;
import com.mobstac.thehindubusinessline.view.AutoResizeWebview;

public class WebActivity extends BaseActivity {

    private AutoResizeWebview mAroundTheWebView;
    private String mWebUrl = "";
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_web_backbutton);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");

        mAroundTheWebView = (AutoResizeWebview) findViewById(R.id.webview_around_web);
        mAroundTheWebView.getSettings().setJavaScriptEnabled(true);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mAroundTheWebView.setWebViewClient(new WebViewClient() {

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

        mWebUrl = getIntent().getStringExtra(Constants.WEB_ARTICLE_URL);
        mAroundTheWebView.loadUrl(mWebUrl);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_web, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_share_web:
                Intent mIntent = getSharingIntent(mWebUrl);
                startActivity(mIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private Intent getSharingIntent(String url) {
        Intent sharingIntent = new Intent(
                android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent
                .putExtra(android.content.Intent.EXTRA_TEXT, url);
        return Intent.createChooser(sharingIntent, "Share Via");
    }

    @Override
    public void onBackPressed() {
        if (mAroundTheWebView.canGoBack()) {
            mAroundTheWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
