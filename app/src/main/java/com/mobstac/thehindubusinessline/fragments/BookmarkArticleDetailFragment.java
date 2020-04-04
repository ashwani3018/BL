package com.mobstac.thehindubusinessline.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.ads.mediation.facebook.FacebookAdapter;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.mobstac.thehindubusinessline.Businessline;
import com.mobstac.thehindubusinessline.R;
import com.mobstac.thehindubusinessline.activity.AdsBaseActivity;
import com.mobstac.thehindubusinessline.activity.ImageGallaryActivity;
import com.mobstac.thehindubusinessline.activity.JWPlayerActivity;
import com.mobstac.thehindubusinessline.activity.LoginActivity;
import com.mobstac.thehindubusinessline.activity.OverlayActivity;
import com.mobstac.thehindubusinessline.activity.WebActivity;
import com.mobstac.thehindubusinessline.activity.YouTubeFullScreenActivity;
import com.mobstac.thehindubusinessline.listener.CustomScrollListenerForScrollView;
import com.mobstac.thehindubusinessline.model.BookmarkBean;
import com.mobstac.thehindubusinessline.model.ImageBean;
import com.mobstac.thehindubusinessline.model.ImageGallaryUrl;
import com.mobstac.thehindubusinessline.utils.AppUtils;
import com.mobstac.thehindubusinessline.utils.ArticleUtil;
import com.mobstac.thehindubusinessline.utils.Constants;
import com.mobstac.thehindubusinessline.utils.DFPConsent;
import com.mobstac.thehindubusinessline.utils.GoogleAnalyticsTracker;
import com.mobstac.thehindubusinessline.utils.NetworkUtils;
import com.mobstac.thehindubusinessline.utils.PicassoUtils;
import com.mobstac.thehindubusinessline.utils.SharedPreferenceHelper;
import com.mobstac.thehindubusinessline.utils.SharingArticleUtil;
import com.mobstac.thehindubusinessline.utils.VukkleCommentCountTask;
import com.mobstac.thehindubusinessline.view.AutoResizeWebview;
import com.mobstac.thehindubusinessline.view.CustomArticleScrollView;
import com.mobstac.thehindubusinessline.view.ExpandedHeightGridView;
import com.ns.tts.TTSPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;

/**
 * Created by arvind on 30/8/16.
 */
public class BookmarkArticleDetailFragment extends BaseFragment implements View.OnClickListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener, CustomScrollListenerForScrollView , VukkleCommentCountTask.VukkleCommentCountListeners {

    public static final String ARTICLE_ID = "article_id";
    public static final String SECTION_ID = "section_id";
    private final String TAG = "ArticleDetailFragment";
    private AdsBaseActivity mMainActivity;
    private int mArticleID;
    private String mSectionId;
    private BookmarkBean mArticleDetail;
    private TextView mTitleTextView;
    private TextView mUpdatedTextView;
    private TextView mLocationAndCreateDateTextView;
    private TextView mAuthorTextView;
    private ImageView mHeaderImageView;
    private AutoResizeWebview mDescriptionWebView;
    private AutoResizeWebview mSecondDescriptionWebView;
    private ExpandedHeightGridView mRelatedArticleListView;
    private Button mPostCommentButton;
    private TextView mCommentCountTextView;
    private String mCount;
    private Menu mMenu;
    private CardView mRelatedArticleParentView;
    private CustomArticleScrollView mArticleScrollView;
    private ProgressBar mProgressBar;
    private boolean isFragmentVisibleToUser;

    private FrameLayout mDFPPlaceHolderTop;
    private FrameLayout mDFPPlaceHolderBottom;
    private PublisherAdView mInsideDescriptionAdview;
    private PublisherAdView mArticleFooterAdView;

    private TextView txtViewCount;
    private ImageButton mMultiMediaButton;
    private String multiMediaUrl;
    private TextView mCaptionTextView;
    private MediaPlayer mMediaPlayer;
    private View mCaptionDevider;
    private String[] toPlayFull;
    private int positionPlayed = 0;
    private boolean isTextToSpeechNeedToPlay;
    private TextToSpeech mTextToSpeech;
    private String title, mDescriptionString;
    private int textToSpeechStatus = 1;
    private String imageUrl;
    private boolean isUserLoggedIn;
    private RelativeLayout mTransparentLayout;
    private CustomScrollListenerForScrollView customScrollListenerForScrollView;

    private Bundle nonPersonalizedAdsReqBundle;
    private String articleUrl;

    public static BookmarkArticleDetailFragment newInstance(int articleId, String sectionId) {
        BookmarkArticleDetailFragment articleDetailFragment = new BookmarkArticleDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARTICLE_ID, articleId);
        bundle.putString(SECTION_ID, sectionId);
        articleDetailFragment.setArguments(bundle);
        return articleDetailFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach: ");
        if(mMainActivity instanceof AdsBaseActivity)
        mMainActivity = (AdsBaseActivity) context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(TAG, "onAttach: ");
        if(mMainActivity instanceof AdsBaseActivity)
        mMainActivity = (AdsBaseActivity) activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        if (getArguments() != null) {
            mArticleID = getArguments().getInt(ARTICLE_ID);
            mSectionId = getArguments().getString(SECTION_ID);
        }

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        customScrollListenerForScrollView = this;
        mArticleDetail = Businessline.getRealmInstance().where(BookmarkBean.class).equalTo("aid", mArticleID).findFirst();
        View view = inflater.inflate(R.layout.fragment_article_detail, container, false);

        if (mArticleDetail != null) {
            articleUrl = mArticleDetail.getAl();
        }

        initView(view);


        if (!SharedPreferenceHelper.getBoolean(getActivity(), Constants.FIRST_TAP, false)) {
            SharedPreferenceHelper.putBoolean(getActivity(), Constants.FIRST_TAP, true);
            if(mMainActivity != null) {
                mMainActivity.loadFullScreenAds(false);
            }
        }

        // GDPR Consent Status
        nonPersonalizedAdsReqBundle = DFPConsent.GDPRStatusBundle(getActivity());

        mTextToSpeech = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                textToSpeechStatus = status;
                TTSPreference ttsPreference = TTSPreference.getInstance(getContext());
                float speed = ttsPreference.getSpeed();
                float pitch = ttsPreference.getPitch();
                if (mTextToSpeech != null) {
                    mTextToSpeech.setPitch(pitch);
                    mTextToSpeech.setSpeechRate(speed);

                    String country = ttsPreference.getCountry();
                    String language = ttsPreference.getLanguage();

                    Locale locale = new Locale(language, country);

                    int result = mTextToSpeech.setLanguage(locale);
                    Log.d("TAG", result + " set language " + country + " " + language);
                }
            }
        });

        mTextToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {

            }

            @Override
            public void onDone(String utteranceId) {
                Log.i(TAG, "onDone: " + positionPlayed);
                if (isTextToSpeechNeedToPlay) {
                    if (toPlayFull != null && positionPlayed < toPlayFull.length) {
                        playTextToSpeech(positionPlayed);
                    } else {
                        if (mMenu != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mTextToSpeech.stop();
                                    mMenu.findItem(R.id.action_speak).setIcon(R.drawable.audio);
                                }
                            });
                        }
                    }
                } else {
                    if (mMenu != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mTextToSpeech.stop();
                                mMenu.findItem(R.id.action_speak).setIcon(R.drawable.audio);
                            }
                        });
                    }
                }
            }

            @Override
            public void onError(String utteranceId) {

            }
        });
        return view;
    }

    private void initView(View view) {
        mArticleScrollView = (CustomArticleScrollView) view.findViewById(R.id.article_scrollview);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mTitleTextView = (TextView) view.findViewById(R.id.title);
        mUpdatedTextView = (TextView) view.findViewById(R.id.updated);
        mLocationAndCreateDateTextView = (TextView) view.findViewById(R.id.location_createsdate);
        mAuthorTextView = (TextView) view.findViewById(R.id.author);
        mHeaderImageView = (ImageView) view.findViewById(R.id.detail_image);
        mDescriptionWebView = (AutoResizeWebview) view.findViewById(R.id.main_web_view);
        mSecondDescriptionWebView = (AutoResizeWebview) view.findViewById(R.id.secondWebView);
        mRelatedArticleListView = (ExpandedHeightGridView) view.findViewById(R.id.related_article);
        mPostCommentButton = (Button) view.findViewById(R.id.post_comment_detail);
        mCommentCountTextView = (TextView) view.findViewById(R.id.comment_count);
        mPostCommentButton.setOnClickListener(this);
        mCommentCountTextView.setOnClickListener(this);

        mDFPPlaceHolderTop = (FrameLayout) view.findViewById(R.id.dfp_placeholder_top);
        mDFPPlaceHolderBottom = (FrameLayout) view.findViewById(R.id.dfp_placeholder_bottom);
        mInsideDescriptionAdview = (PublisherAdView) view.findViewById(R.id.inline_adview);
        mArticleFooterAdView = (PublisherAdView) view.findViewById(R.id.articleFooterAdView);

        mRelatedArticleParentView = (CardView) view.findViewById(R.id.related_article_parentView);
//        mTopBannerAdview = (VmaxAdView) view.findViewById(R.id.top_Ad_holder);
        mMultiMediaButton = (ImageButton) view.findViewById(R.id.multimedia_button);
        mCaptionTextView = (TextView) view.findViewById(R.id.caption);
        mCaptionDevider = view.findViewById(R.id.captiondevider);
        mTransparentLayout = (RelativeLayout) view.findViewById(R.id.transparent_parent_layout);
        mTransparentLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        view.findViewById(R.id.login_button).setOnClickListener(this);
        view.findViewById(R.id.register_button).setOnClickListener(this);


    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: ");
        if (mArticleDetail != null) {
            fillArticleData(mArticleDetail);
        }
    }

    private void fillArticleData(final BookmarkBean mArticleDetail) {
        String articleType = mArticleDetail.getArticleType();
        mProgressBar.setVisibility(View.GONE);
        mArticleScrollView.setVisibility(View.VISIBLE);
        final OrderedRealmCollection<ImageBean> mImageList = mArticleDetail.getMe();

        mTitleTextView.setText(mArticleDetail.getTi());
        String author = mArticleDetail.getAu();
        if (author != null && !TextUtils.isEmpty(author)) {
            mAuthorTextView.setVisibility(View.VISIBLE);
            mAuthorTextView.setText(author.replace(",\n"," | ").replace(","," | "));
//            mAuthorTextView.setText(author);
        } else {
            mAuthorTextView.setVisibility(View.GONE);
        }
        mUpdatedTextView.setText(AppUtils.getTopNewsFormattedDate(AppUtils.changeStringToMillis(mArticleDetail.getPd()), true));
        mLocationAndCreateDateTextView.setText(mArticleDetail.getLocation().toString()+""+AppUtils.getTopNewsFormattedDate(AppUtils.changeStringToMillis(mArticleDetail.getOd()), false));
        if (mImageList != null && mImageList.size() > 0) {
            mHeaderImageView.setVisibility(View.VISIBLE);
            imageUrl = mImageList.get(0).getIm_v2();
            if (imageUrl == null) {
                imageUrl = mImageList.get(0).getIm();
            }
            String caption = mImageList.get(0).getCa();
            if (imageUrl != null && !TextUtils.isEmpty(imageUrl)) {
                PicassoUtils.downloadImage(getActivity(), imageUrl, mHeaderImageView, R.mipmap.ph_topnews_th);
            } else {
                if (articleType.equalsIgnoreCase(Constants.TYPE_PHOTO) || articleType.equalsIgnoreCase(Constants.TYPE_AUDIO) || articleType.equalsIgnoreCase(Constants.TYPE_VIDEO)) {
                    mHeaderImageView.setVisibility(View.VISIBLE);
                } else {
                    mHeaderImageView.setVisibility(View.GONE);
                }
            }
            mCaptionTextView.setVisibility(View.VISIBLE);
            if (caption != null && !TextUtils.isEmpty(caption)) {
                mCaptionTextView.setText(Html.fromHtml(caption));
            } else {
                mCaptionTextView.setVisibility(View.GONE);
            }
        } else {
            if (articleType.equalsIgnoreCase(Constants.TYPE_PHOTO) || articleType.equalsIgnoreCase(Constants.TYPE_AUDIO) || articleType.equalsIgnoreCase(Constants.TYPE_VIDEO)) {
                mHeaderImageView.setVisibility(View.VISIBLE);
            } else {
                mHeaderImageView.setVisibility(View.GONE);
            }
            mCaptionTextView.setVisibility(View.GONE);
        }
        mCaptionDevider.setVisibility(View.VISIBLE);
        switch (articleType) {
            case Constants.TYPE_ARTICLE:
                mMultiMediaButton.setVisibility(View.GONE);
                break;
            case Constants.TYPE_AUDIO:
                mMultiMediaButton.setVisibility(View.VISIBLE);
                mMultiMediaButton.setBackgroundResource(R.mipmap.audio);
                multiMediaUrl = mArticleDetail.getMultimediaPath();
                break;
            case Constants.TYPE_PHOTO:
                mMultiMediaButton.setVisibility(View.VISIBLE);
                mMultiMediaButton.setBackgroundResource(R.mipmap.slide);
                break;
            case Constants.TYPE_VIDEO:
                mMultiMediaButton.setVisibility(View.VISIBLE);
                mMultiMediaButton.setBackgroundResource(R.mipmap.play_updated);
                multiMediaUrl = mArticleDetail.getMultimediaPath();
                break;
        }

//        }

        mHeaderImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mArticleDetail.getArticleType()) {
                    case Constants.TYPE_ARTICLE:
                        callImageGalleryActivity(mImageList);
                        break;
                    case Constants.TYPE_AUDIO:
                        initiateMediaPlayer(mArticleDetail.getMultimediaPath());
                        break;
                    case Constants.TYPE_PHOTO:
                        callImageGalleryActivity(mImageList);
                        break;
                    case Constants.TYPE_VIDEO:
                        if (mArticleDetail.getVid() != null && !TextUtils.isEmpty(mArticleDetail.getVid())) {
                            callJWPlayerActivity(mArticleDetail.getVid());
                        } else {
                            callYoutubeActivity();
                        }
                        break;
                }
            }
        });

        mMultiMediaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mArticleDetail.getArticleType()) {
                    case Constants.TYPE_ARTICLE:
                        callImageGalleryActivity(mImageList);
                        break;
                    case Constants.TYPE_AUDIO:
                        initiateMediaPlayer(mArticleDetail.getMultimediaPath());
                        break;
                    case Constants.TYPE_PHOTO:
                        callImageGalleryActivity(mImageList);
                        break;
                    case Constants.TYPE_VIDEO:
                        if (mArticleDetail.getVid() != null && !TextUtils.isEmpty(mArticleDetail.getVid())) {
                            callJWPlayerActivity(mArticleDetail.getVid());
                        } else {
                            callYoutubeActivity();
                        }
                        break;

                }
            }
        });


        final String description = mArticleDetail.getDe();
        if (description != null && !TextUtils.isEmpty(description) && mArticleDetail.getAdd_pos() > 0) {
            showDescription(mArticleDetail.getLe(), description.substring(0, mArticleDetail.getAdd_pos() - 1), mDescriptionWebView);
            showDescription("", description.substring(mArticleDetail.getAdd_pos() - 1), mSecondDescriptionWebView);
        } else {
            showDescription(mArticleDetail.getLe(), description, mDescriptionWebView);
        }
        mDescriptionString = String.valueOf(Html.fromHtml(Html.fromHtml(description).toString()));
        mRelatedArticleListView.setVisibility(View.GONE);
        String articleUrl;
        if (mArticleDetail != null) {
            articleUrl = mArticleDetail.getAl();
        } else {
            articleUrl = Constants.THE_HINDU_URL;
        }

        if(mMainActivity != null) {
            mMainActivity.createBannerAdRequest(true, true, articleUrl);
        }
        if (NetworkUtils.isNetworkAvailable(getActivity())) {
            // new GetCommentCountTask(String.valueOf(mArticleDetail.getAid())).execute();

            VukkleCommentCountTask mVukkleCountTask = (VukkleCommentCountTask) new VukkleCommentCountTask(String.valueOf(mArticleDetail.getAid())).execute();
            mVukkleCountTask.setVukkleCommentResult(this);

            fillInlineAd();
            fillInlineFooterAd();
        }


        /** text to speech for Title */
        title = mTitleTextView.getText().toString();
        /*Overlay screen impementation*/
        boolean articleOverlayNeedTobeLoaded = SharedPreferenceHelper.getBoolean(getActivity(), Constants.ARTICLE_OVERLAY_SCREEN_LOADED, false);
        if (articleOverlayNeedTobeLoaded) {
            Intent intent = new Intent(getActivity(), OverlayActivity.class);
            intent.putExtra(Constants.OVERLAY_TYPE, OverlayActivity.TYPE_ARTICLE);
            startActivity(intent);
        }
        /*Overlay screen impementation ends*/

        /*Login check */
        isUserLoggedIn = SharedPreferenceHelper.getBoolean(getActivity(), Constants.PORTFOLIO_IS_USER_LOGIN, Constants.IS_USER_LOGGED_IN);

        ArticleUtil.checkLoginStatus(mArticleScrollView, isUserLoggedIn, mArticleDetail.getParentId(),
                mArticleDetail.getSid(), this);

        /* login end*/
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated: ");

    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        if (mInsideDescriptionAdview != null) {
            mInsideDescriptionAdview.resume();
        }
        if (mArticleFooterAdView != null) {
            mArticleFooterAdView.resume();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB && mDescriptionWebView != null) {
            mDescriptionWebView.onResume();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB && mSecondDescriptionWebView != null) {
            mSecondDescriptionWebView.onResume();
        }
        /*Login Check*/
        if (mArticleDetail != null) {
            isUserLoggedIn = SharedPreferenceHelper.getBoolean(getActivity(), Constants.PORTFOLIO_IS_USER_LOGIN, Constants.IS_USER_LOGGED_IN);
            ArticleUtil.checkLoginStatus(mArticleScrollView, isUserLoggedIn, mArticleDetail.getParentId(),
                    mArticleDetail.getSid(), this);
        }
        /*Login Check end*/


        if (mArticleDetail != null) {
            GoogleAnalyticsTracker.setGoogleAnalyticScreenName(getActivity(), GoogleAnalyticsTracker.getGoogleArticleName(mArticleDetail.getTi()));
            FlurryAgent.logEvent("Article: " + mArticleDetail.getTi());
            FlurryAgent.onPageView();
        }
    }

    @Override
    public void onPause() {
        if (mInsideDescriptionAdview != null) {
            mInsideDescriptionAdview.pause();
        }
        if (mArticleFooterAdView != null) {
            mArticleFooterAdView.pause();
        }
        super.onPause();
        Log.d(TAG, "onPause: ");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB && mDescriptionWebView != null) {
            mDescriptionWebView.onPause();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB && mSecondDescriptionWebView != null) {
            mSecondDescriptionWebView.onPause();
        }
        stopMediaPlayer();
        if (mTextToSpeech != null && mTextToSpeech.isSpeaking()) {
            mTextToSpeech.stop();
            isTextToSpeechNeedToPlay = false;
            positionPlayed = 0;
            if (mMenu != null) {
                mMenu.findItem(R.id.action_speak).setIcon(R.drawable.audio);
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mTextToSpeech != null && mTextToSpeech.isSpeaking()) {
            mTextToSpeech.stop();
            isTextToSpeechNeedToPlay = false;
            positionPlayed = 0;
            if (mMenu != null) {
                mMenu.findItem(R.id.action_speak).setIcon(R.drawable.audio);
            }
        }
    }

    @Override
    public void onDestroyView() {
        if (mInsideDescriptionAdview != null) {
            mInsideDescriptionAdview.destroy();
        }
        if (mArticleFooterAdView != null) {
            mArticleFooterAdView.destroy();
        }
        super.onDestroyView();
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        if (mTextToSpeech != null && mTextToSpeech.isSpeaking()) {
            mTextToSpeech.stop();
            mTextToSpeech.shutdown();
        }
    }

    @Override
    public void onDestroy() {
        if(mMainActivity != null) {
            mMainActivity.deleteUnBookmarkedArticleFromDatabase();
        }
//        if (mInsideDescriptionAdview != null) {
//            mInsideDescriptionAdview.onDestroy();
//        }
        if (mTextToSpeech != null) {
            mTextToSpeech.stop();
            mTextToSpeech.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onDetach() {
//        if (mInsideDescriptionAdview != null) {
//            mInsideDescriptionAdview.finish();
//        }
        if (mTextToSpeech != null) {
            mTextToSpeech.stop();
            mTextToSpeech.shutdown();
        }
        super.onDetach();
    }

    private void showDescription(String leadText, String description, AutoResizeWebview webView) {
        if (description != null) {
            description = description.trim();
        }
        final boolean nightMode = SharedPreferenceHelper.
                getBoolean(getActivity(), Constants.DAY_MODE, false);
        if (leadText != null && TextUtils.isEmpty(leadText)) {
            if (!nightMode) {
                description = "<html><head>"
                        + "<style type=\"text/css\">body{color: #000; background-color: #ffffff;}" +
                        "@font-face {\n" +
                        "   font-family: 'tundra';\n" +
                        "   src: url('file:///android_asset/fonts/SerifOffc.ttf');" +
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
                        "   src: url('file:///android_asset/fonts/SerifOffc.ttf');" +
                        "} " +
                        "body {font-family: 'tundra';}"
                        + "</style></head>"
                        + "<body>"
                        + description
                        + "</body></html>";
            }
        } else {
            if (!nightMode) {
                description = "<html><head>"
                        + "<style type=\"text/css\">body{color: #000; background-color: #ffffff;}" +
                        "@font-face {\n" +
                        "   font-family: 'tundra';\n" +
                        "   src: url('file:///android_asset/fonts/SerifOffc.ttf');" +
                        "} " +
                        "body {font-family: 'tundra';}"
                        + "</style></head>"
                        + "<body>"
                        + "<i> " + "<font color=\"#666666\">" + leadText + "</font></i>"
                        + description
                        + "</body></html>";
            } else {
                description = "<html><head>"
                        + "<style type=\"text/css\">body{color: #fff; background-color: #181818;}" +
                        "@font-face {\n" +
                        "   font-family: 'tundra';\n" +
                        "   src: url('file:///android_asset/fonts/SerifOffc.ttf');" +
                        "} " +
                        "body {font-family: 'tundra';}"
                        + "</style></head>"
                        + "<body>"
                        + "<i> " + "<font color=\"#E8EAEC\">" + leadText + "</font></i>"
                        + description
                        + "</body></html>";
            }
        }
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // HERE YOU GET url

                URI uri = null;
                try {
                    uri = new URI(url);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                String domain = uri.getHost();
                String path = uri.getPath();
                Log.d(TAG, "shouldOverrideUrlLoading: " + path);

                if (domain.toLowerCase(Locale.getDefault()).contains("thehindu".toLowerCase(Locale.getDefault()))) {
//                    ArticleDetailFragment linkifyFragment = ArticleDetailFragment.newInstance(getArticleIdFromArticleUrl(url), null, url, false, null);
//                    mMainActivity.addFragmentToBackStack(linkifyFragment);
                    ArticleUtil.launchDetailActivity(mMainActivity, getArticleIdFromArticleUrl(url), null, url, false, null);
                    return true;
                } else {
//                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    Intent intent = new Intent(getActivity(), WebActivity.class);
                    intent.putExtra(Constants.WEB_ARTICLE_URL, url);
                    startActivity(intent);
                    return true;
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (getActivity() != null && getView() != null) {
                    sendRequestId(CREATE_FILL_ADs);
                    loadTaboolaWidgets(mArticleDetail.getAl());
                }
            }
        });
        webView.loadDataWithBaseURL("https:/", description, "text/html", "UTF-8", null);
        webView.setVisibility(View.VISIBLE);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.post_comment_detail:
                FlurryAgent.logEvent(getString(R.string.ga_article_postcomment_button_clicked));
                GoogleAnalyticsTracker.setGoogleAnalyticsEvent(getActivity(), getString(R.string.ga_action_button_click), getString(R.string.ga_article_postcomment_button_clicked), getString(R.string.ga_article_detail_lebel));
                if (mArticleDetail != null) {
                    if (NetworkUtils.isNetworkAvailable(mMainActivity)) {
                       /* PostCommentFragment postCommentFragment = PostCommentFragment.newInstance(String.valueOf(mArticleDetail.getAid()), mArticleDetail.getTi(),
                                mArticleDetail.getAl(), mArticleDetail.getPd());
                        mMainActivity.addFragmentToBackStack(postCommentFragment);*/
                        ArticleUtil.launchCommentActivity(mMainActivity, String.valueOf(mArticleDetail.getAid()), mArticleDetail.getAl(), mArticleDetail.getTi(),
                                mArticleDetail.getPd(), mCount, false, imageUrl);
                    } else {
                        mMainActivity.showSnackBar(mArticleScrollView);
                    }
                }
                break;
            case R.id.comment_count:
                Log.d(TAG, "onClick: comment count");
                GoogleAnalyticsTracker.setGoogleAnalyticsEvent(getActivity(), getString(R.string.ga_action_button_click), getString(R.string.ga_article_comment_button_clicked), getString(R.string.ga_article_detail_lebel));
                FlurryAgent.logEvent(getString(R.string.ga_article_comment_button_clicked));
               /* CommentsListFragment fragment = CommentsListFragment.getInstance(String.valueOf(mArticleDetail.getAid()), mCount, mArticleDetail.getTi(),
                        mArticleDetail.getAl(), mArticleDetail.getPd());*/
//                mMainActivity.addFragmentToBackStack(fragment);
                ArticleUtil.launchCommentActivity(mMainActivity, String.valueOf(mArticleDetail.getAid()), mArticleDetail.getAl(), mArticleDetail.getTi(),
                        mArticleDetail.getPd(), mCount, false, imageUrl);
                break;
            case R.id.login_button:
                startLoginActivity();
                showTransprentView(false);
                break;
            case R.id.register_button:
                startUserRegisterActivity();
                showTransprentView(false);
                break;
        }

    }

    private void startLoginActivity() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.putExtra(LoginActivity.SCREEN_TYPE, LoginActivity.SIGN_IN);
        startActivity(intent);
    }

    private void startUserRegisterActivity() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.putExtra(LoginActivity.SCREEN_TYPE, LoginActivity.SIGN_UP);
        startActivity(intent);
    }

    @Override
    public void getDataFromDB() {
        Log.d(TAG, "getDataFromDB: ");
    }

    @Override
    public void showLoadingFragment() {
        Log.d(TAG, "showLoadingFragment: ");
    }

    @Override
    public void hideLoadingFragment() {
        Log.d(TAG, "hideLoadingFragment: ");
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (mMainActivity != null) {
            mMainActivity.hideToolbarLogo();
            mMainActivity.setToolbarBackButton(R.mipmap.arrow_back);
            mMainActivity.setToolbarTitle(null);
        }
        if (menu != null) {
            menu.findItem(R.id.action_speak).setVisible(true);
            menu.findItem(R.id.action_comment).setVisible(true);
            menu.findItem(R.id.action_bookmarks).setVisible(true);
            menu.findItem(R.id.action_textsize).setVisible(true);
            menu.findItem(R.id.action_share_article).setVisible(true);
            menu.findItem(R.id.action_overflow).setVisible(false);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Realm mRealm = Businessline.getRealmInstance();
        BookmarkBean bookmarkBean = mRealm.where(BookmarkBean.class).equalTo("aid", mArticleID).findFirst();
        if (menu != null) {
            mMenu = menu;
            if (bookmarkBean != null && !mMainActivity.checkUnBookmarkList(bookmarkBean)) {
                menu.findItem(R.id.action_bookmarks).setIcon(R.mipmap.bookmarks_white_filled);
                mRealm.beginTransaction();
                bookmarkBean.setRead(true);
                mRealm.commitTransaction();
            } else {
                menu.findItem(R.id.action_bookmarks).setIcon(R.mipmap.bookmarks_icon_day);
            }
            MenuItem actionComment = menu.findItem(R.id.action_comment);
            final View notificaitons = (View) MenuItemCompat.getActionView(actionComment);
            txtViewCount = (TextView) notificaitons.findViewById(R.id.textview_comment_count);
            if (mCount != null && !TextUtils.isEmpty(mCount)) {
                int count = Integer.parseInt(mCount);
                if (count > 0 && count < 100) {
                    txtViewCount.setText("" + count);
                    txtViewCount.setVisibility(View.VISIBLE);
                } else if (count > 99) {
                    txtViewCount.setText("99+");
                    txtViewCount.setVisibility(View.VISIBLE);
                }
            }

            notificaitons.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mArticleDetail != null && ArticleUtil.checkLoginStatusForOptionMenu(isUserLoggedIn,
                            mArticleDetail.getParentId(), mArticleDetail.getSid(), customScrollListenerForScrollView)) {

                        // Krishan commit
                       /* if (mCount != null && !TextUtils.isEmpty(mCount)) {
                            int count = Integer.parseInt(mCount);
                            if (count > 0) {

                                ArticleUtil.launchCommentActivity(mMainActivity, String.valueOf(mArticleDetail.getAid()), mArticleDetail.getAl(), mArticleDetail.getTi(),
                                        mArticleDetail.getPd(), mCount, false);
                                GoogleAnalyticsTracker.setGoogleAnalyticsEvent(getActivity(), getString(R.string.ga_action_button_click), "Comment count", getString(R.string.ga_article_detail_lebel));
                                FlurryAgent.logEvent(getString(R.string.ga_article_comment_button_clicked));

                            } else {
                                Toast.makeText(getActivity(), "No Comments", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "No Comments", Toast.LENGTH_SHORT).show();
                        }*/

                        ArticleUtil.launchCommentActivity(mMainActivity, String.valueOf(mArticleDetail.getAid()), mArticleDetail.getAl(), mArticleDetail.getTi(),
                                mArticleDetail.getPd(), mCount, false, imageUrl);
                        GoogleAnalyticsTracker.setGoogleAnalyticsEvent(getActivity(), getString(R.string.ga_action_button_click), "Comment count", getString(R.string.ga_article_detail_lebel));
                        FlurryAgent.logEvent(getString(R.string.ga_article_comment_button_clicked));
                    }
                }
            });
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                FlurryAgent.logEvent(getString(R.string.ga_article_back_button_clicked));
                GoogleAnalyticsTracker.setGoogleAnalyticsEvent(getActivity(), getString(R.string.ga_action), getString(R.string.ga_article_back_button_clicked), getString(R.string.ga_article_detail_lebel));
                mMainActivity.popTopFragmentFromBackStack();
                return true;
            case R.id.action_bookmarks:
                if (mArticleDetail != null && ArticleUtil.checkLoginStatusForOptionMenu(isUserLoggedIn,
                        mArticleDetail.getParentId(), mArticleDetail.getSid(), this)) {
                    Realm mRealm = Businessline.getRealmInstance();
                    BookmarkBean bookmarkBean = mRealm.where(BookmarkBean.class).equalTo("aid", mArticleID).findFirst();
                    if (!mMainActivity.checkUnBookmarkList(bookmarkBean)) {
                        mMainActivity.unBookmarkedList(bookmarkBean);
                        mMenu.findItem(R.id.action_bookmarks).setIcon(R.mipmap.bookmarks_icon_day);
                        AppUtils.showToast(mMainActivity, mMainActivity.getResources().getString(R.string.removed_from_bookmark));
                    } else {
                        mMainActivity.unBookmarkedList(bookmarkBean);
                        mMenu.findItem(R.id.action_bookmarks).setIcon(R.mipmap.bookmarks_white_filled);
                        AppUtils.showToast(mMainActivity, mMainActivity.getResources().getString(R.string.added_to_bookmark));
                    }
                    FlurryAgent.logEvent(getString(R.string.ga_bookmark_article_button_clicked));
                    GoogleAnalyticsTracker.setGoogleAnalyticsEvent(getActivity(), getString(R.string.ga_action), getString(R.string.ga_bookmark_article_button_clicked), getString(R.string.ga_article_detail_lebel));

                }
                return true;
            case R.id.action_share_article:
                if (mArticleDetail != null && ArticleUtil.checkLoginStatusForOptionMenu(isUserLoggedIn,
                        mArticleDetail.getParentId(), mArticleDetail.getSid(), this)) {
                    SharingArticleUtil.shareArticle(mMainActivity, mArticleDetail.getTi(), mArticleDetail.getAl());
                }
                return true;
            case R.id.action_textsize:
                if (mArticleDetail != null && ArticleUtil.checkLoginStatusForOptionMenu(isUserLoggedIn,
                        mArticleDetail.getParentId(), mArticleDetail.getSid(), this)) {
                    int current = SharedPreferenceHelper.getInt(mMainActivity, Constants.SELECTED_FONT_SIZE, 1);

                    if (current < 4) {
                        current = current + 1;
                    } else {
                        current = 1;
                    }
                    switch (current) {
                        case 1:
                            Toast.makeText(getActivity(), "Text font is Small", Toast.LENGTH_SHORT).show();
                            break;
                        case 2:
                            Toast.makeText(getActivity(), "Text font is Medium", Toast.LENGTH_SHORT).show();
                            break;
                        case 3:
                            Toast.makeText(getActivity(), "Text font is Large", Toast.LENGTH_SHORT).show();
                            break;
                        case 4:
                            Toast.makeText(getActivity(), "Text font is Extra Large", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    SharedPreferenceHelper.putInt(mMainActivity, Constants.SELECTED_FONT_SIZE, current);
                    FlurryAgent.logEvent(getString(R.string.ga_article_text_button_clicked));
                    GoogleAnalyticsTracker.setGoogleAnalyticsEvent(getActivity(), getString(R.string.ga_action), getString(R.string.ga_article_text_button_clicked), getString(R.string.ga_article_detail_lebel));
                }
                return true;


            case R.id.action_speak:
                if (mArticleDetail != null && ArticleUtil.checkLoginStatusForOptionMenu(isUserLoggedIn,
                        mArticleDetail.getParentId(), mArticleDetail.getSid(), this)) {
                    if (mTextToSpeech != null && mTextToSpeech.isSpeaking()) {
                        isTextToSpeechNeedToPlay = false;
                        mTextToSpeech.stop();
                        item.setIcon(R.drawable.audio);
                    } else {
                        if (mArticleDetail != null && textToSpeechStatus == TextToSpeech.SUCCESS) {
                            positionPlayed = 0;
                            String leadText;
                            if (mArticleDetail.getLe() != null) {
                                leadText = String.valueOf(Html.fromHtml(Html.fromHtml(mArticleDetail.getLe()).toString()));
                            } else {
                                leadText = "";
                            }
                            String toPlay = title + "..." + leadText + "..." + mDescriptionString;
                            if (toPlayFull == null) {
                                toPlayFull = splitByNumber(toPlay, Constants.TEXT_TO_SPEECH_TEXT_SIZE);
                            }
                            playTextToSpeech(positionPlayed);
                            item.setIcon(R.drawable.stop);
                        }
                    }
                    FlurryAgent.logEvent(getString(R.string.ga_article_audio_button_clicked));
                    GoogleAnalyticsTracker.setGoogleAnalyticsEvent(getActivity(), getString(R.string.ga_action), getString(R.string.ga_article_audio_button_clicked), getString(R.string.ga_article_detail_lebel));
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.i(TAG, "setUserVisibleHint: ");
        isFragmentVisibleToUser = isVisibleToUser;
        if (isVisibleToUser && mArticleDetail != null) {
            GoogleAnalyticsTracker.setGoogleAnalyticScreenName(getActivity(), GoogleAnalyticsTracker.getGoogleArticleName(mArticleDetail.getTi()));
            FlurryAgent.logEvent("Article : " + mArticleDetail.getTi());
            FlurryAgent.onPageView();
        }
    }


    private void cancelRequestId(int requestId) {
        handler.removeMessages(requestId);
    }

    private void sendRequestId(int requestId) {
        cancelRequestId(requestId);
        handler.sendEmptyMessageDelayed(requestId, 1000);
    }

    private final int CREATE_FILL_ADs = 1;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case CREATE_FILL_ADs:
                    // If user is in Europe, and selected "Pay for the ad-free version" then ads should not display
                    if (!SharedPreferenceHelper.isUserPreferAdsFree(mMainActivity)) {
                        fillInlineAd();
                        fillInlineFooterAd();
                    }
                    break;
            }

        }
    };

    private void fillInlineAd() {
        Log.i(TAG, "fillInlineAd: Called");

        mInsideDescriptionAdview.setAdListener(new com.google.android.gms.ads.AdListener() {
            @Override
            public void onAdLoaded() {
                Log.i("Ads", "onAdLoaded");
                mDFPPlaceHolderTop.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Log.i("Ads", "onAdFailedToLoad" + errorCode);
                mDFPPlaceHolderTop.setVisibility(View.GONE);
            }

            @Override
            public void onAdOpened() {
                Log.i("Ads", "onAdOpened");
            }

            @Override
            public void onAdLeftApplication() {
                Log.i("Ads", "onAdLeftApplication");
            }

            @Override
            public void onAdClosed() {
                Log.i("Ads", "onAdClosed");
            }
        });

        PublisherAdRequest request;
        if (nonPersonalizedAdsReqBundle != null) {
            Bundle extras = new FacebookAdapter.FacebookExtrasBundleBuilder()
                    .setNativeAdChoicesIconExpandable(false)
                    .build();
            request = new PublisherAdRequest.Builder()
                    .addNetworkExtrasBundle(AdMobAdapter.class, nonPersonalizedAdsReqBundle)
                    .addNetworkExtrasBundle(FacebookAdapter.class, extras)
                    .build();
        } else {
            request = new PublisherAdRequest.Builder()
                    .build();
        }

        mInsideDescriptionAdview.loadAd(request);
    }


    private void fillInlineFooterAd() {
        Log.i(TAG, "fillInlineAd: Called");

        mArticleFooterAdView.setAdListener(new com.google.android.gms.ads.AdListener() {
            @Override
            public void onAdLoaded() {
                Log.i("Ads", "onAdLoaded");
                mDFPPlaceHolderBottom.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Log.i("Ads", "onAdFailedToLoad" + errorCode);
                mDFPPlaceHolderBottom.setVisibility(View.GONE);
            }

            @Override
            public void onAdOpened() {
                Log.i("Ads", "onAdOpened");
            }

            @Override
            public void onAdLeftApplication() {
                Log.i("Ads", "onAdLeftApplication");
            }

            @Override
            public void onAdClosed() {
                Log.i("Ads", "onAdClosed");
            }
        });

        PublisherAdRequest request;
        if (nonPersonalizedAdsReqBundle != null) {
            Bundle extras = new FacebookAdapter.FacebookExtrasBundleBuilder()
                    .setNativeAdChoicesIconExpandable(false)
                    .build();
            request = new PublisherAdRequest.Builder()
                    .addNetworkExtrasBundle(AdMobAdapter.class, nonPersonalizedAdsReqBundle)
                    .addNetworkExtrasBundle(FacebookAdapter.class, extras)
                    .build();
        } else {
            request = new PublisherAdRequest.Builder()
                    .build();
        }
        mArticleFooterAdView.loadAd(request);
    }

/*    private void fillInlineAd(String contentUrl) {
        // If user is in Europe, and selected "Pay for the ad-free version" then ads should not display
        if(SharedPreferenceHelper.isUserPreferAdsFree(mMainActivity)) {
            return;
        }
        mDFPPlaceHolderTop.setVisibility(View.VISIBLE);
        mInsideDescriptionAdview.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Log.i("Ads", "onAdLoaded");
               // mInsideDescriptionAdview.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Log.i("Ads", "onAdFailedToLoad" + errorCode);
            }

            @Override
            public void onAdOpened() {
                Log.i("Ads", "onAdOpened");
            }

            @Override
            public void onAdLeftApplication() {
                Log.i("Ads", "onAdLeftApplication");
            }

            @Override
            public void onAdClosed() {
                Log.i("Ads", "onAdClosed");
            }
        });

        mInsideDescriptionAdview.loadAd(DFPConsent.getCustomUrlAdsRequest(getActivity(), contentUrl));

    }*/

    private void callImageGalleryActivity(OrderedRealmCollection<ImageBean> mImageList) {
        ArrayList<ImageGallaryUrl> mImageUrlList = null;
        if (mImageList != null && mImageList.size() > 0) {
            mImageUrlList = new ArrayList<ImageGallaryUrl>();
            for (ImageBean imageBean : mImageList) {
                mImageUrlList.add(new ImageGallaryUrl(imageBean.getIm_v2(), imageBean.getCa(), mArticleDetail.getTi()));
            }
        }
        if (mImageUrlList != null) {
            Intent intent = new Intent(mMainActivity, ImageGallaryActivity.class);
            intent.putParcelableArrayListExtra("ImageUrl", mImageUrlList);
            startActivity(intent);
        }
        FlurryAgent.logEvent(getString(R.string.ga_article_main_image_clicked));
        GoogleAnalyticsTracker.setGoogleAnalyticsEvent(getActivity(), getString(R.string.ga_action_image_click), getString(R.string.ga_article_main_image_clicked), getString(R.string.ga_article_detail_lebel));

    }

    private void callYoutubeActivity() {
        if (multiMediaUrl != null && !TextUtils.isEmpty(multiMediaUrl)) {
            Intent intent = new Intent(mMainActivity, YouTubeFullScreenActivity.class);
            intent.putExtra("videoId", multiMediaUrl);
            mMainActivity.startActivity(intent);
        }
    }

    private void callJWPlayerActivity(String url) {
        if (url != null && !TextUtils.isEmpty(url)) {
            Intent intent = new Intent(mMainActivity, JWPlayerActivity.class);
            intent.putExtra("videoId", url);
            mMainActivity.startActivity(intent);
        }
    }

    private void initiateMediaPlayer(String mediaUrl) {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnBufferingUpdateListener(this);
            mMediaPlayer.setOnCompletionListener(this);
            try {
                mMediaPlayer.setDataSource(mMainActivity, Uri.parse(mediaUrl));
            } catch (IOException e) {
                e.printStackTrace();
            }

            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    Log.i(TAG, "onPrepared: ");
                    mMediaPlayer.start();
                }
            });
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.prepareAsync();

        } else {
            startMediaPlayer();
        }
    }

    private void stopMediaPlayer() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private void startMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private void playTextToSpeech(int textToPlay) {
        if (textToPlay < toPlayFull.length) {
            isTextToSpeechNeedToPlay = true;
            String text = toPlayFull[textToPlay];
            HashMap<String, String> params = new HashMap<String, String>();
            params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "" + positionPlayed);
            mTextToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, params);
            positionPlayed++;
            if (mMenu != null) {
                mMainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mMenu.findItem(R.id.action_speak).setIcon(R.drawable.stop);
                    }
                });
            }
        }
    }

    private String[] splitByNumber(String s, int chunkSize) {
        int chunkCount = (s.length() / chunkSize) + (s.length() % chunkSize == 0 ? 0 : 1);
        String[] returnVal = new String[chunkCount];
        for (int i = 0; i < chunkCount; i++) {
            returnVal[i] = s.substring(i * chunkSize, Math.min((i + 1) * chunkSize - 1, s.length()));
        }
        return returnVal;
    }


    @Override
    public void showTransprentView(boolean show) {
        if (show) {
            mTransparentLayout.setVisibility(View.VISIBLE);
        } else {
            mTransparentLayout.setVisibility(View.GONE);

        }
    }

    @Override
    public void onCountTaskCompletes(String aid, String result) {
        if (isAdded() && getActivity() != null && result != null && !TextUtils.isEmpty(result)) {
            try {

                JSONObject object = new JSONObject(result);
                String res = object.getString("data");
                JSONObject obj = new JSONObject(res);
                String articleCount = obj.getString(aid);


                if (!articleCount.equalsIgnoreCase("0")) {
                    Log.d(TAG, "onPostExecute: " + "Comment Count" + articleCount);
                    int count = Integer.parseInt(articleCount);
                    mCount = articleCount;
                    if (count == 1) {
                        mCommentCountTextView.setVisibility(View.VISIBLE);
                        mCommentCountTextView.setText("Read " + articleCount + " comment");
                    } else if (count > 1) {
                        mCommentCountTextView.setVisibility(View.VISIBLE);
                        mCommentCountTextView.setText("Read all " + articleCount + " comments");
                    } else if (count == 0) {
                        mCommentCountTextView.setVisibility(View.GONE);
                    }
                    if (txtViewCount != null) {
                        if (count > 0 && count < 100) {
                            txtViewCount.setText(articleCount);
                            txtViewCount.setVisibility(View.VISIBLE);
                        } else if (count > 99) {
                            txtViewCount.setText("99+");
                            txtViewCount.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    mCommentCountTextView.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                mCommentCountTextView.setVisibility(View.GONE);
            }
        } else {
            mCommentCountTextView.setVisibility(View.GONE);
        }
    }
}

   /* private class GetCommentCountTask extends AsyncTask<Void, Void, String> {

        private String articleID;

        public GetCommentCountTask(String articleID) {
            this.articleID = articleID;
        }

        @Override
        protected String doInBackground(Void... params) {
            String article_id = articleID;
            URL url;
            HttpURLConnection connection = null;

            try {
                url = new URL(Constants.VUKKLE_COMMUNT_COUNT + article_id);
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
            if (isAdded() && getActivity() != null && aVoid != null && !TextUtils.isEmpty(aVoid)) {
                try {

                    JSONObject object = new JSONObject(aVoid);
                    String res = object.getString("data");
                    JSONObject  obj = new JSONObject(res);
                    String articleCount = obj.getString(articleID);


                    if (!articleCount.equalsIgnoreCase("0")) {
                        Log.d(TAG, "onPostExecute: " + "Comment Count" + articleCount);
                        int count = Integer.parseInt(articleCount);
                        mCount = articleCount;
                        if (count == 1) {
                            mCommentCountTextView.setVisibility(View.VISIBLE);
                            mCommentCountTextView.setText("Read " + articleCount + " comment");
                        } else if (count > 1) {
                            mCommentCountTextView.setVisibility(View.VISIBLE);
                            mCommentCountTextView.setText("Read all " + articleCount + " comments");
                        } else if (count == 0) {
                            mCommentCountTextView.setVisibility(View.GONE);
                        }
                        if (txtViewCount != null) {
                            if (count > 0 && count < 100) {
                                txtViewCount.setText(articleCount);
                                txtViewCount.setVisibility(View.VISIBLE);
                            } else if (count > 99) {
                                txtViewCount.setText("99+");
                                txtViewCount.setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                        mCommentCountTextView.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    mCommentCountTextView.setVisibility(View.GONE);
                }
            } else {
                mCommentCountTextView.setVisibility(View.GONE);
            }

        }
    }
}
*/