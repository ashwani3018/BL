package com.mobstac.thehindubusinessline.fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.mobstac.thehindubusinessline.R;
import com.mobstac.thehindubusinessline.activity.CommentActivity;
import com.mobstac.thehindubusinessline.utils.AppUtils;
import com.mobstac.thehindubusinessline.utils.Constants;
import com.mobstac.thehindubusinessline.utils.GoogleAnalyticsTracker;
import com.mobstac.thehindubusinessline.utils.NetworkUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostCommentFragment extends BaseFragment implements View.OnClickListener {

    private Button mCommentSubmit;
    private EditText mName, mEmail, mCommentBody;
    private String mArticleID;
    private String mArticleTitle;
    private String mArticleUrl;
    private String mArticleTime;
    private CommentActivity mMainActivity;
    private ProgressDialog mProgressDialog;

    public PostCommentFragment() {
        // Required empty public constructor
    }

    public static PostCommentFragment newInstance(String articleId, String articleTitle, String articleLink, String publishDate) {
        PostCommentFragment fragment = new PostCommentFragment();
        Bundle bundle = new Bundle();
        bundle.putString("articleID", articleId);
        bundle.putString("title", articleTitle);
        bundle.putString("articleUrl", articleLink);
        bundle.putString("articleTime", publishDate);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMainActivity = (CommentActivity) context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mMainActivity = (CommentActivity) activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mArticleID = getArguments().getString("articleID", "");
            mArticleTitle = getArguments().getString("title", "");
            mArticleUrl = getArguments().getString("articleUrl", "");
            mArticleTime = getArguments().getString("articleTime", "");
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post_comment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCommentSubmit = (Button) view.findViewById(R.id.post_comment);
        mName = (EditText) view.findViewById(R.id.input_name);
        mEmail = (EditText) view.findViewById(R.id.input_email);
        mCommentBody = (EditText) view.findViewById(R.id.input_message);
        mCommentSubmit.setOnClickListener(this);
        mMainActivity.createBannerAdRequest(true, false, mArticleUrl);
    }

    @Override
    public void onResume() {
        super.onResume();
        FlurryAgent.logEvent(getString(R.string.ga_post_comment));
        FlurryAgent.onPageView();
        GoogleAnalyticsTracker.setGoogleAnalyticScreenName(getActivity(), getString(R.string.ga_post_comment));
    }

    @Override
    public void onPause() {
        AppUtils.hideKeyBoard(getActivity(), mCommentSubmit);
        super.onPause();
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.post_comment:
                postComment();
                GoogleAnalyticsTracker.setGoogleAnalyticsEvent(getActivity(), getString(R.string.ga_action_button_click),
                        getString(R.string.ga_article_postcomment_button_clicked), "Post comment screen");
                FlurryAgent.logEvent(getString(R.string.ga_article_postcomment_button_clicked));
                break;
        }
    }

    private void postComment() {
        String comment = "";
        String name = "";
        String email = "";
        if (mCommentBody.length() > 999) {
            Toast.makeText(getActivity(),
                    "The comment cannot exceeded 1000 characters.",
                    Toast.LENGTH_SHORT).show();
        } else if (mName.getText().toString().equals("")
                || mEmail.getText().toString().equals("")
                || mCommentBody.getText().toString().equals("")) {
            Toast.makeText(
                    getActivity(),
                    "Please provide all the details.",
                    Toast.LENGTH_LONG).show();
        } else if (!isValidEmail(mEmail.getText().toString())) {
            Toast.makeText(getActivity(),
                    "Invalid email address", Toast.LENGTH_SHORT).show();
        } else {
            comment = mCommentBody.getText().toString();
            name = mName.getText().toString();
            email = mEmail.getText().toString();
            if (NetworkUtils.isNetworkAvailable(getActivity())) {
                PostComment postComment = new PostComment(mArticleID, comment, email, name, mArticleTitle, mArticleUrl, mArticleTime);
                postComment.execute();
            } else {
                showToast(getActivity(), "Network not available");
            }
            AppUtils.hideKeyBoard(getActivity(), mCommentSubmit);
        }
        GoogleAnalyticsTracker.setGoogleAnalyticsEvent(getActivity(), getString(R.string.ga_action_button_click), "Post comment", "Post comment screen");
    }

    private final boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
                    .matches();
        }
    }

    public void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (mMainActivity != null) {
            mMainActivity.setToolbarTitle("Post Your Comment");
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
    }

    private class PostComment extends AsyncTask<Void, Void, String> {
        private String articleID;
        private String comment;
        private String email;
        private String name;
        private String title;
        private String articleUrl;
        private String articleTime;
        private String tag = "tag";

        public PostComment(String articleID, String comment, String email, String name, String title, String articleUrl, String articleTime) {
            this.articleID = articleID;
            this.comment = comment;
            this.email = email;
            this.name = name;
            this.title = title;
            this.articleUrl = articleUrl;
            this.articleTime = articleTime;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (getActivity() != null) {
                mProgressDialog = ProgressDialog.show(getActivity(), "", "Please wait.");
            }
        }

        @Override
        protected String doInBackground(Void... params) {
            String article_id = articleID;
            URL url;
            HttpURLConnection connection = null;

            try {
                comment = URLEncoder.encode(comment, "utf-8");
                email = URLEncoder.encode(email, "utf-8");
                name = URLEncoder.encode(name, "utf-8");
                title = URLEncoder.encode(title, "utf-8");
                if (!articleUrl.contains("thehindu.com")) {
                    articleUrl = "http://www.thehindu.com" + articleUrl;
                }
                articleUrl = URLEncoder.encode(articleUrl, "utf-8");
                articleTime = URLEncoder.encode(articleTime, "utf-8");
                tag = URLEncoder.encode(tag, "utf-8");

                url = new URL("http://vuukle.com/api.asmx/insert_non_social_comment?article_id=" + article_id + "&comment=" + comment
                        + "&email=" + email + "&name=" + name + "&h=" + title + "&url=" + articleUrl + "&ts=" + articleTime + "&t=" + tag +
                        "&biz_id=" + Constants.VUUKLE_API_KEY + "&secret_key=" + Constants.VUUKLE_SECRET_KEY);
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
                return response.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
            if (isAdded() && getActivity() != null && aVoid != null && !TextUtils.isEmpty(aVoid)) {
                showToast(getActivity(), "Your comment has been received and will be moderated.");
                getActivity().getSupportFragmentManager().popBackStack();

                //Flurry Analytics
                /*try {
                    Map<String, String> postCommentFlurryData = new HashMap<String, String>();
                    postCommentFlurryData.put("Category", mArticleTitle);
                    FlurryAgent.logEvent("Post Comment",
                            postCommentFlurryData);
                    FlurryAgent.logEvent(getString(R.string.flurry_page_view));
                    FlurryAgent.onPageView();
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast(getActivity(),"Failed to post comment (" + aVoid + ")");
                }*/
            } else {
                showToast(getActivity(), "Failed to post comment");
            }
        }
    }

  /*  @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mMainActivity.popTopFragmentFromBackStack();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/
}
