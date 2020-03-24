/*
 * Copyright (c) 2014 Mobstac TM
 * All Rights Reserved.
 * @since Jun 12, 2014
 * @author rajeshcp
 */
package com.mobstac.thehindubusinessline.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.google.gson.Gson;
import com.mobstac.thehindubusinessline.R;
import com.mobstac.thehindubusinessline.activity.CommentActivity;
import com.mobstac.thehindubusinessline.activity.WebActivity;
import com.mobstac.thehindubusinessline.adapter.CommentsListAdapter;
import com.mobstac.thehindubusinessline.model.CommentListModel;
import com.mobstac.thehindubusinessline.utils.ArticleUtil;
import com.mobstac.thehindubusinessline.utils.Constants;
import com.mobstac.thehindubusinessline.utils.GoogleAnalyticsTracker;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author rajeshcp
 */
public class CommentsListFragment extends BaseFragment implements OnClickListener {



    private ListView mListView;
    private Button mPostCommentButton;
    private TextView mCommentcount;
    private CommentsListAdapter mAdapter;
    private String mArticleId;
    private String mCount;
    private String mArticleTitle;
    private String mArticleUrl;
    private String mArticleTime;
    private String mImgUrl;
    private CommentActivity mMainActivity;


    private WebView mWebViewComments;
    private ProgressBar mProgressBar;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mMainActivity = (CommentActivity) activity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (CommentActivity) context;
    }

    public static CommentsListFragment getInstance(String articleId, String count, String title, String articleUrl, String articleTime, String imgUrl) {
        CommentsListFragment mainFragment = new CommentsListFragment();
        Bundle args = new Bundle();
        args.putString("commentArticleId", articleId);
        args.putString("commentCount", count);
        args.putString("commentArticleTitle", title);
        args.putString("commentArticleUrl", articleUrl);
        args.putString("commentArticleTime", articleTime);
        args.putString("imgUrl", imgUrl);
        mainFragment.setArguments(args);
        return mainFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mArticleId = args.getString("commentArticleId");
            mCount = args.getString("commentCount");
            mArticleTitle = args.getString("commentArticleTitle");
            mArticleUrl = args.getString("commentArticleUrl");
            mArticleTime = args.getString("commentArticleTime");
            mImgUrl = args.getString("imgUrl");
        }
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.comments_list_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMainActivity.createBannerAdRequest(true, false, mArticleUrl);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMainActivity.setToolbarTitle("Comments");
        GoogleAnalyticsTracker.setGoogleAnalyticScreenName(getActivity(), "Comment Detail Screen");
        FlurryAgent.logEvent("Comment Detail Screen");
        FlurryAgent.onPageView();

        String url = "https://cdn.vuukle.com/amp.html?" +
                "apiKey=" + Constants.VUUKLE_API_KEY + "&" +
                "host=thehindubusinessline.com" + "&" +
                "socialAuth=false" + "&" +
                "id=" + mArticleId + "&" +
                "img=" + mImgUrl + "&" +
                "title=" + URLEncoder.encode(mArticleTitle) + "&" +
                "url=" + mArticleUrl;


        configWebView();
//        testConfigWebView();

        mWebViewComments.loadUrl(url);
    }



    @Override
    public void onPause() {
        super.onPause();
        mMainActivity.setToolbarTitle("");
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

    @Override
    public void onViewCreated(View mView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(mView, savedInstanceState);
        mPostCommentButton = (Button) mView.findViewById(R.id.post_comment_detail);
        mPostCommentButton.setOnClickListener(this);
        mListView = (ListView) mView.findViewById(R.id.comments_list);
        mAdapter = new CommentsListAdapter(getActivity(), R.layout.comments_layout);
        mListView.setAdapter(mAdapter);
        //  GetCommentListTask task = new GetCommentListTask(mArticleId);
        // task.execute();


        mWebViewComments = (WebView) mView.findViewById(R.id.activity_main_webview_comments);
        mProgressBar = (ProgressBar) mView.findViewById(R.id.progressBar);

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.post_comment_detail) {
            FlurryAgent.logEvent(getString(R.string.ga_article_postcomment_button_clicked));
            GoogleAnalyticsTracker.setGoogleAnalyticsEvent(getActivity(), getString(R.string.ga_action_button_click), getString(R.string.ga_article_postcomment_button_clicked), getString(R.string.ga_article_detail_lebel));
           /* PostCommentFragment postCommentFragment = PostCommentFragment.newInstance(mArticleId, mArticleTitle,
                    mArticleUrl, mArticleTime);
            mMainActivity.pushFragmentToBackStack(postCommentFragment);*/
            ArticleUtil.launchCommentActivity(mMainActivity, mArticleId, mArticleUrl, mArticleTitle,
                    mArticleTime, "", true, mImgUrl);
        }
    }


    private class GetCommentListTask extends AsyncTask<Void, Void, CommentListModel> {
        private String articleID;
        private ProgressDialog progressDialog;

        public GetCommentListTask(String articleID) {
            this.articleID = articleID;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(mMainActivity);
            progressDialog.setMessage("Loading comments.");
            progressDialog.show();
        }

        @Override
        protected CommentListModel doInBackground(Void... params) {
            final String article_id = articleID;
            URL url;
            HttpURLConnection connection = null;

            try {
                url = new URL("http://vuukle.com/api.asmx/getCommentFeed?api_key=" + Constants.VUUKLE_API_KEY + "&article_id=" + article_id +
                        "&secret_key=" + Constants.VUUKLE_SECRET_KEY + "&time_zone=Asia/Kolkata&host=thehindubusinessline.com&from_count=0&to_count=10000");
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                //Get SingUpDetail
                InputStream is = connection.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuffer response = new StringBuffer();
                while ((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                rd.close();
                Gson gson = new Gson();
                CommentListModel commentListModel = gson.fromJson(response.toString(), CommentListModel.class);
                return commentListModel;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(CommentListModel aVoid) {
            super.onPostExecute(aVoid);
            if (isAdded() && getActivity() != null && aVoid != null) {
                progressDialog.dismiss();
                if (aVoid.getComment_feed() != null && aVoid.getComment_feed().size() > 0) {
                    mAdapter.addAll(aVoid.getComment_feed());
                }
            }

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }


    private void configWebView() {
        //javascript support
        mWebViewComments.getSettings().setJavaScriptEnabled(true);
        //html5 support
        mWebViewComments.getSettings().setDomStorageEnabled(true);

        mWebViewComments.getSettings().setSupportMultipleWindows(true);

        /*mWebViewComments.clearHistory();
        mWebViewComments.clearFormData();
        mWebViewComments.clearCache(true);

        WebSettings webSettings = mWebViewComments.getSettings();
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);*/

        mWebViewComments.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {

                String mags = consoleMessage.message();
                Log.d("VUUKLE", "onConsoleMessage: "+mags);

                if(consoleMessage.message().contains("Comments initialized!")) {
                    //signInUser(name, email) - javascript function implemented on a page
                    mWebViewComments.loadUrl("javascript:signInUser('" + "Ninestars S" + "', '" + "nineatrs@gmail.com" + "')");
                }
                return super.onConsoleMessage(consoleMessage);
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                Log.i("", "");
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
                WebView popup = new WebView(mMainActivity);
                popup.getSettings().setJavaScriptEnabled(true);
                popup.setLayoutParams(view.getLayoutParams());
                popup.setWebChromeClient(new WebChromeClient() {
                    @Override
                    public void onCloseWindow(WebView window) {
                        mWebViewComments.removeView(window);
                    }

                });
                popup.getSettings().setUserAgentString(popup.getSettings().getUserAgentString().replace("; wv", ""));
                popup.setWebViewClient(new WebViewClient() {});
                view.addView(popup);
                WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
                transport.setWebView(popup);
                resultMsg.sendToTarget();
                return true;
            }
        });


        mWebViewComments.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, final String url) {
                //Clicked url
                //if u use super() it will load url into webview

                String HOST = "vuukle.com";
                URI uri = null;
                String hostName = null;
                try {
                    uri = new URI(url);
                    hostName = uri.getHost();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }


//                if (hostName.equals(HOST)) {

                String domain = uri.getQuery();
                String path = uri.getPath();

                if (domain != null && domain.toLowerCase(Locale.getDefault()).contains("thehindubusinessline".toLowerCase(Locale.getDefault()))) {
                    ArticleUtil.launchDetailActivity(mMainActivity, getArticleIdFromArticleUrl(url), null, url, false, null);

                } else {
                    Intent intent = new Intent(getActivity(), WebActivity.class);
                    intent.putExtra(Constants.WEB_ARTICLE_URL, url);
                    startActivity(intent);
                }

                return true;
//                }
//                return false;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mProgressBar.setVisibility(View.GONE);
            }
        });


    }

    public boolean canGoBack() {
        if (mWebViewComments.canGoBack()) {
            mWebViewComments.goBack();
            return true;
        }
        return false;
    }


    private void testConfigWebView() {

        mWebViewComments.getSettings().setJavaScriptEnabled(true);
        //html5 support
        mWebViewComments.getSettings().setDomStorageEnabled(true);
        mWebViewComments.getSettings().setSupportMultipleWindows(true);
        //mWebViewComments - your WebView
        mWebViewComments.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {

                //message
                Log.d("consolejs", consoleMessage.message());
                return super.onConsoleMessage(consoleMessage);
            }

            @Override
            public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
                WebView popup = new WebView(mMainActivity);
                popup.getSettings().setJavaScriptEnabled(true);
                popup.setLayoutParams(view.getLayoutParams());
                popup.setWebChromeClient(new WebChromeClient() {
                    @Override
                    public void onCloseWindow(WebView window) {
                        mWebViewComments.removeView(window);
                    }

                });
                popup.getSettings().setUserAgentString(popup.getSettings().getUserAgentString().replace("; wv", ""));
                popup.setWebViewClient(new WebViewClient() {});
                view.addView(popup);
                WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
                transport.setWebView(popup);
                resultMsg.sendToTarget();
                return true;
            }
        });

        mWebViewComments.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, final String url) {

                //Clicked url
                Log.d("VUUKLE", "Clicked url: " + url);
                view.loadUrl(url);
                //if u use super() it will load url
                return true;
            }
        });
    }
}
