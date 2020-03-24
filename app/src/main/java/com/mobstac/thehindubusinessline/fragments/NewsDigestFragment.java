package com.mobstac.thehindubusinessline.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.flurry.android.FlurryAgent;
import com.mobstac.thehindubusinessline.R;
import com.mobstac.thehindubusinessline.activity.MainActivity;
import com.mobstac.thehindubusinessline.utils.AppUtils;
import com.mobstac.thehindubusinessline.utils.ArticleUtil;
import com.mobstac.thehindubusinessline.utils.GoogleAnalyticsTracker;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;

public class NewsDigestFragment extends BaseFragment {

    public static final String URL = "url";
    private WebView newsDigestView;
    private ProgressBar webViewProgress;
    private MainActivity mMainActivity;

    public NewsDigestFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mMainActivity = (MainActivity) activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View mView = inflater.inflate(R.layout.news_digest_fragment, container, false);
        if(mMainActivity != null){
            mMainActivity.showBottomNavigationBar(false);
        }

        webViewProgress = (ProgressBar) mView.findViewById(R.id.webViewProgress);
        newsDigestView = (WebView) mView.findViewById(R.id.newsDigestWebView);
        startWebView();
//        mMainActivity.hideHomeBottomAdBanner();
//        mMainActivity.showRosBottomBanner();
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMainActivity.createBannerAdRequest(true, false, "");
    }

    @Override
    public void onResume() {
        super.onResume();
        GoogleAnalyticsTracker.setGoogleAnalyticScreenName(getActivity(), "News Digest Screen");
        FlurryAgent.logEvent("News digest Screen");
        FlurryAgent.onPageView();
    }

    @Override
    public void getDataFromDB() {

    }

    @Override
    public void showLoadingFragment() {

    }

    @Override
    public void hideLoadingFragment() {

    }

    private void startWebView() {
        final String url = getArguments().getString(URL);
        newsDigestView.getSettings().setJavaScriptEnabled(true);
        newsDigestView.setWebViewClient(new WebViewClient() {
            private int running = 0;

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                running = Math.max(running, 1);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (--running == 0) {
                    String chopScript = "javascript:(function() {" +
                            "document.getElementById(\'header\').style.display = \'none\';" +
                            "document.getElementById(\'footer\').style.display = \'none\';" +
                            "var more_articles = document.getElementsByClassName(\'more-articles-row\').length;" +
                            "var i = 0;" +
                            "for(i=0; i < more_articles; i++){" +
                            "document.getElementsByClassName(\'more-articles-row\')[i].style.display = \'none\';" +
                            "}" +
                            "})()";
                    view.loadUrl(chopScript);
                    webViewProgress.setVisibility(View.GONE);
                    newsDigestView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                running++;
                URI uri = null;
                try {
                    uri = new URI(url);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                if (uri != null) {
                    String domain = uri.getHost();
                    String path = uri.getPath();
                    String title = path.substring(path.indexOf("/"), path.lastIndexOf("/"));

                    if (domain.toLowerCase(Locale.getDefault()).contains("thehindu".toLowerCase(Locale.getDefault()))) {
                        if (!path.equals("/newsletter/")) {
                            String onlyTitle = null;
                            try {
                                int firstOccurance = title.indexOf("/");
                                int secondOccurance = title.indexOf("/", firstOccurance + 1);
                                int thirdOccurance = title.indexOf("/", secondOccurance + 1);
                                if (thirdOccurance != -1) {
                                    onlyTitle = title.substring(thirdOccurance + 1);
                                } else {
                                    onlyTitle = title.substring(secondOccurance + 1);
                                }
                                onlyTitle = onlyTitle.replace("-", " ");
                                onlyTitle = onlyTitle.substring(0, 1).toUpperCase() + onlyTitle.substring(1);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

//                            ArticleDetailFragment linkifyFragment = ArticleDetailFragment.newInstance(0, null, url, false, null);
//                            mMainActivity.pushFragmentToBackStack(linkifyFragment);

                            int articleId = AppUtils.getArticleIdFromArticleUrls(path);
                            ArticleUtil.launchDetailActivity(mMainActivity, articleId, null, url, false, null);
                            return true;

                        } else {
                            view.loadUrl(url);
                            return true;
                        }
                    } else {
                        view.loadUrl(url);
                        return true;
                    }
                } else {
                    view.loadUrl(url);
                    return true;
                }
            }
        });

        newsDigestView.loadUrl(url);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (mMainActivity != null) {
            mMainActivity.hideToolbarLogo();
            mMainActivity.setToolbarTitle("News Digest");
            mMainActivity.setToolbarBackButton(R.mipmap.arrow_back);
        }
        if (menu != null) {
            menu.findItem(R.id.action_overflow).setVisible(false);
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            FlurryAgent.logEvent("News digest: Back button clicked");
            GoogleAnalyticsTracker.setGoogleAnalyticsEvent(getActivity(), getString(R.string.ga_action), "News digest: Back button clicked", "News digest Fragment");
            mMainActivity.popTopFragmentFromBackStack();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
