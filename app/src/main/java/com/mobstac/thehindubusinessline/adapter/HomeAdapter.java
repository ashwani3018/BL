package com.mobstac.thehindubusinessline.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.ads.mediation.facebook.FacebookAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.google.android.gms.ads.formats.NativeContentAdView;
import com.google.gson.Gson;
import com.mobstac.thehindubusinessline.Businessline;
import com.mobstac.thehindubusinessline.R;
import com.mobstac.thehindubusinessline.activity.CustomizeHomeScreenActivity;
import com.mobstac.thehindubusinessline.activity.MainActivity;
import com.mobstac.thehindubusinessline.database.DatabaseJanitor;
import com.mobstac.thehindubusinessline.fragments.SlidingArticleFragment;
import com.mobstac.thehindubusinessline.fragments.SlidingSectionFragment;
import com.mobstac.thehindubusinessline.holder.StaticItemWebViewHolder;
import com.mobstac.thehindubusinessline.listener.OnSeeMoreFromSectionClickListener;
import com.mobstac.thehindubusinessline.model.BSEData;
import com.mobstac.thehindubusinessline.model.BookmarkBean;
import com.mobstac.thehindubusinessline.model.GoldDollarDataModel;
import com.mobstac.thehindubusinessline.model.HomeAdapterModel;
import com.mobstac.thehindubusinessline.model.NSEData;
import com.mobstac.thehindubusinessline.model.SensexStatus;
import com.mobstac.thehindubusinessline.model.databasemodel.ArticleDetail;
import com.mobstac.thehindubusinessline.utils.AppUtils;
import com.mobstac.thehindubusinessline.utils.ArticleUtil;
import com.mobstac.thehindubusinessline.utils.Constants;
import com.mobstac.thehindubusinessline.utils.DFPConsent;
import com.mobstac.thehindubusinessline.utils.GoogleAnalyticsTracker;
import com.mobstac.thehindubusinessline.utils.PicassoUtils;
import com.mobstac.thehindubusinessline.utils.SharingArticleUtil;
import com.mobstac.thehindubusinessline.utils.StaticItemDummyViewClick;
import com.mobstac.thehindubusinessline.utils.UniversalTouchListener;
import com.mobstac.thehindubusinessline.utils.WebViewLinkClick;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;

import static android.widget.LinearLayout.HORIZONTAL;

/**
 * Created by arvind on 10/1/17.
 */

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int VIEW_NORMAL_ARTICLE = 1;
    public static final int VIEW_NATIVE_AD = 2;
    public static final int VIEW_INLINE_AD = 3;
    public static final int VIEW_WIDGET = 4;
    public static final int VIEW_FOOTER = 5;
    public static final int VIEW_SENSEX = 6;
    public static final int VIEW_BANNER = 7;
    public static final int HOME_WEB_CONTAINT = 8;

    private final String TAG = "HomeAdapter";
    private Context mContext;
    private List<HomeAdapterModel> mHomeDataList;
    private PublisherAdView mInlineAdView;
    private boolean isFragmentVisibleToUser;
    private OnSeeMoreFromSectionClickListener onSeeMoreFromSectionClickListener;


    private Map<Integer, PublisherAdView> adsInlineMap = new HashMap<>();
    private Map<Integer, Boolean> adsReqIndex = new HashMap<>();
    private Map<Integer, NativeContentAd> adsNativeMap = new HashMap<>();

    private MainActivity mMainActivity;

    private Bundle nonPersonalizedAdsReqBundle;

    public HomeAdapter(Context mContext, List<HomeAdapterModel> mHomeDataList, MainActivity mainActivity, OnSeeMoreFromSectionClickListener onSeeMoreFromSectionClickListener) {
        this.mContext = mContext;
        this.mHomeDataList = mHomeDataList;
        this.onSeeMoreFromSectionClickListener = onSeeMoreFromSectionClickListener;
        this.mMainActivity = mainActivity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder mViewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {

            case VIEW_WIDGET:
                View widgetsView = inflater.inflate(R.layout.cardview_home_widgets, parent, false);
                mViewHolder = new WidgetsViewHolder(widgetsView);
                break;
            case VIEW_NORMAL_ARTICLE:
                View articlesView = inflater.inflate(R.layout.cardview_article_list, parent, false);
                mViewHolder = new ArticlesViewHolder(articlesView);
                break;
            case VIEW_INLINE_AD:
                View inlineAdViewHolder = inflater.inflate(R.layout.inline_ad_container, parent, false);
                mViewHolder = new InlineAdViewHolder(inlineAdViewHolder);
                break;
            case VIEW_NATIVE_AD:
                View nativeView = inflater.inflate(R.layout.cardview_native_ad, parent, false);
                mViewHolder = new NativeAdViewHolder(nativeView);
                break;
            case VIEW_FOOTER:
                View footerView = inflater.inflate(R.layout.home_footer_view, parent, false);
                mViewHolder = new CustomiseButtonViewHolder(footerView);
                break;
            case VIEW_SENSEX:
                View mSensexView = inflater.inflate(R.layout.cardview_sensex, parent, false);
                mViewHolder = new SensexViewHolder(mSensexView);
                break;
            case VIEW_BANNER:
                View mBannerViewHolder = inflater.inflate(R.layout.cardview_banner, parent, false);
                mViewHolder = new BannerViewHolder(mBannerViewHolder);
                break;
            case HOME_WEB_CONTAINT:
                View staticWebview = inflater.inflate(R.layout.item_webview, parent, false);
                mViewHolder = new StaticItemWebViewHolder(staticWebview);
                break;
        }
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        switch (holder.getItemViewType()) {

            case VIEW_WIDGET:
                WidgetsViewHolder widgetsHolder = (WidgetsViewHolder) holder;
                fillWidgetData(widgetsHolder, position);
                break;

            case VIEW_NORMAL_ARTICLE:
                ArticlesViewHolder articlesViewHolder = (ArticlesViewHolder) holder;
                fillArticleData(articlesViewHolder, position);
                break;

            case VIEW_INLINE_AD:
                InlineAdViewHolder inlineAdViewHolder = (InlineAdViewHolder) holder;
                inlineAdViewHolder.mDFPLinearLayout.setVisibility(View.GONE);
                inlineAdViewHolder.mDfpFramLayout.removeAllViews();
                inlineAdViewHolder.mDfpFramLayout.setBackgroundResource(R.drawable.interstetial_ads_bg);
                final PublisherAdView adView = adsInlineMap.get(position);

                if(adView == null) {
                    loadInlineAd(inlineAdViewHolder, position);
                } else {
                    inlineAdViewHolder.mDfpFramLayout.setBackground(null);
                    adView.removeView(inlineAdViewHolder.mDfpFramLayout);
                    inlineAdViewHolder.mDFPLinearLayout.setVisibility(View.VISIBLE);
                    inlineAdViewHolder.mDfpFramLayout.addView(adView);
                }

                break;

            case VIEW_NATIVE_AD:
//                NativeAdViewHolder nativeAdViewHolder = (NativeAdViewHolder) holder;
//                fillNativeAdData(nativeAdViewHolder, position);

                NativeAdViewHolder nativeAdViewHolder = (NativeAdViewHolder) holder;
                final NativeContentAd contentAd = adsNativeMap.get(position);
                final NativeContentAdView adViewN = nativeAdViewHolder.mNativeContentAdView;

                adViewN.setHeadlineView(adViewN.findViewById(R.id.textview_article_list_header));
                adViewN.setImageView((ImageView) adViewN.findViewById(R.id.imageview_article_list_image));

                if(contentAd == null) {
                    ((TextView) adViewN.getHeadlineView()).setText("");
                    ((ImageView) adViewN.getImageView()).setImageDrawable(null);
                    fillNativeAdData(nativeAdViewHolder, position);
                } else {
                    adViewN.setVisibility(View.VISIBLE);
                    nativeAdViewHolder.mParentLayout.setVisibility(View.VISIBLE);

                    ((TextView) adViewN.getHeadlineView()).setText(contentAd.getHeadline());
                    ((ImageView) adViewN.getImageView()).setImageDrawable(contentAd.getImages().get(0).getDrawable());

                    adViewN.setNativeAd(contentAd);
                }

                break;
            case VIEW_FOOTER:
                CustomiseButtonViewHolder customiseButtonViewHolder = (CustomiseButtonViewHolder) holder;
                fillCustomisedData(customiseButtonViewHolder, position);
                break;
            case VIEW_SENSEX:
                SensexViewHolder sensexViewHolder = (SensexViewHolder) holder;
                fillSensexData(sensexViewHolder, position);
                break;
            case VIEW_BANNER:
                BannerViewHolder mBannerViewHolder = (BannerViewHolder) holder;
                fillBannerData(mBannerViewHolder, position);
                break;
            case HOME_WEB_CONTAINT:
                StaticItemWebViewHolder staticItemHolder = (StaticItemWebViewHolder) holder;
                String staticPageUrl = mHomeDataList.get(position).getStaticPageUrl();
                final String[] url = staticPageUrl.split("00#00");

                staticItemHolder.webView.loadUrl(url[0]);
                if(url.length > 1 && !url[1].equals("0")) {
                    staticItemHolder.mDummyView.setVisibility(View.VISIBLE);
                    // DummyView Click Listener
                    staticItemHolder.mDummyView.setOnClickListener(new StaticItemDummyViewClick(url, mContext));

                } else {
                    staticItemHolder.mDummyView.setVisibility(View.GONE);
                    // Enabling Weblink click on Lead Text
                    new WebViewLinkClick().linkClick(staticItemHolder.webView, staticItemHolder.itemView.getContext());
                }

                break;
        }
    }


    private void fillCustomisedData(CustomiseButtonViewHolder customiseButtonViewHolder, int position) {
        customiseButtonViewHolder.mParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleAnalyticsTracker.setGoogleAnalyticsEvent(
                        mContext,
                        mContext.getString(R.string.ga_action),
                        "Customise: Customise Button Clicked ",
                        mContext.getString(R.string.customize_news_feed_menu));
                FlurryAgent.logEvent("Customise: Customise Button Clicked ");
                mContext.startActivity(new Intent(mContext, CustomizeHomeScreenActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mHomeDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mHomeDataList != null && mHomeDataList.size() > 0) {
            return mHomeDataList.get(position).getViewType();
        } else {
            return -1;
        }
    }


    private void fillArticleData(final ArticlesViewHolder mArticleViewHolder, final int positionParam) {
        final ArticleDetail bean = mHomeDataList.get(positionParam).getmArticle();
        final int mArticleActualPosition = mHomeDataList.get(positionParam).getArticleActualPositionInList();
        if (bean != null) {
            String imageUrl = bean.getIm_thumbnail_v2();

            if (imageUrl == null) {
                imageUrl = bean.getIm_thumbnail();
            }

            String articleType = bean.getArticleType();
            String multiMediaUrl = null;
            if (imageUrl != null && !TextUtils.isEmpty(imageUrl)) {
                imageUrl = imageUrl.replace(Constants.REPLACE_IMAGE_FREE, Constants.ROW_THUMB_SIZE);
                mArticleViewHolder.mImageParentLayout.setVisibility(View.VISIBLE);
                mArticleViewHolder.mArticleImageView.setVisibility(View.VISIBLE);
                PicassoUtils.downloadImage(mContext, imageUrl, mArticleViewHolder.mArticleImageView, R.mipmap.ph_newsfeed_th);
            } else {
                if (articleType.equalsIgnoreCase(Constants.TYPE_PHOTO) || articleType.equalsIgnoreCase(Constants.TYPE_AUDIO) || articleType.equalsIgnoreCase(Constants.TYPE_VIDEO)) {
                    mArticleViewHolder.mImageParentLayout.setVisibility(View.VISIBLE);
                    mArticleViewHolder.mArticleImageView.setVisibility(View.VISIBLE);
                    PicassoUtils.downloadImage(mContext, "https//", mArticleViewHolder.mArticleImageView, R.mipmap.ph_newsfeed_th);
                } else {
                    mArticleViewHolder.mArticleImageView.setVisibility(View.GONE);
                    mArticleViewHolder.mImageParentLayout.setVisibility(View.GONE);
                }
            }
            mArticleViewHolder.mMultimediaButton.setVisibility(View.VISIBLE);
            switch (articleType) {
                case Constants.TYPE_ARTICLE:
                    mArticleViewHolder.mMultimediaButton.setVisibility(View.GONE);
                    break;
                case Constants.TYPE_AUDIO:
                    mArticleViewHolder.mMultimediaButton.setVisibility(View.VISIBLE);
                    mArticleViewHolder.mMultimediaButton.setBackgroundResource(R.mipmap.audio);
                    multiMediaUrl = bean.getAudioLink();
                    break;
                case Constants.TYPE_PHOTO:
                    mArticleViewHolder.mMultimediaButton.setVisibility(View.VISIBLE);
                    mArticleViewHolder.mMultimediaButton.setBackgroundResource(R.mipmap.slide);
                    break;
                case Constants.TYPE_VIDEO:
                    mArticleViewHolder.mMultimediaButton.setVisibility(View.VISIBLE);
                    mArticleViewHolder.mMultimediaButton.setBackgroundResource(R.mipmap.play_updated);
                    multiMediaUrl = bean.getYoutube_video_id();
                    break;
            }

            mArticleViewHolder.mArticleTextView.setText(bean.getTi());
            mArticleViewHolder.mAuthorTextView.setText(bean.getAu());
            String publishTime = bean.getGmt();
            String timeDiff = AppUtils.getDurationFormattedDate(AppUtils.changeStringToMillisGMT(publishTime));
            final Realm mRealm = Businessline.getRealmInstance();
            final BookmarkBean bookmarkBean = mRealm.where(BookmarkBean.class).equalTo("aid", bean.getAid()).findFirst();
            if (bookmarkBean != null && bookmarkBean.getAid() == bean.getAid()) {
                mArticleViewHolder.mBookmarkButton.setBackgroundResource(R.drawable.bookmark_feed_filled);
            } else {
                mArticleViewHolder.mBookmarkButton.setBackgroundResource(R.drawable.bookmark_feed);
            }

            mArticleViewHolder.mArticleTimeTextView.setText(timeDiff);
            mArticleViewHolder.mArticleSectionName.setText(bean.getSname());

            final String multiMediaLink = multiMediaUrl;
            mArticleViewHolder.mBookmarkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GoogleAnalyticsTracker.setGoogleAnalyticsEvent(mContext, "Home", "Home: Bookmark button Clicked", "Home Fragment");
                    FlurryAgent.logEvent("Home: " + "Bookmark button Clicked");
                    if (bookmarkBean == null) {

                        ArticleUtil.markAsBookMark(mRealm, bean.getAid(), bean.getSid(), bean.getSname(), bean.getPd(),
                                bean.getOd(), bean.getPid(), bean.getTi(), bean.getAu(), bean.getAl(), bean.getBk(),
                                bean.getGmt(), bean.getDe(), bean.getLe(), bean.getHi(),
                                ArticleUtil.getImageBeanList(bean.getMe()), System.currentTimeMillis(),
                                false, bean.getAdd_pos(), multiMediaLink,
                                bean.getArticleType(), bean.getVid(), bean.getParentId(), bean.getLocation());

                        mArticleViewHolder.mBookmarkButton.setBackgroundResource(R.drawable.bookmark_feed_filled);
                        AppUtils.showToast(mContext, mContext.getResources().getString(R.string.added_to_bookmark));
                    } else {

                        ArticleUtil.removeFromBookMark(mRealm, bookmarkBean);
                        mArticleViewHolder.mBookmarkButton.setBackgroundResource(R.drawable.bookmark_feed);
                        AppUtils.showToast(mContext, mContext.getResources().getString(R.string.removed_from_bookmark));

                    }
                    notifyDataSetChanged();

                }
            });

            mArticleViewHolder.mArticlesLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GoogleAnalyticsTracker.setGoogleAnalyticsEvent(mContext, "Home", "Home: Article Clicked", "Home Fragment");
                    FlurryAgent.logEvent("Home: " + "Article Clicked");
                    SlidingArticleFragment fragment = SlidingArticleFragment.newInstance(
                            mArticleActualPosition, bean.getSid(), true);
                    ((AppCompatActivity) mContext).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.FRAME_CONTENT, fragment).addToBackStack(null).commitAllowingStateLoss();

                    // Parallax, hide the toolbar before article click then article details screen show toolbar..
                    mMainActivity.expandToolbar(true);
                }
            });

            mArticleViewHolder.mMultimediaButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GoogleAnalyticsTracker.setGoogleAnalyticsEvent(mContext, "Home", "Home: Article Clicked", "Home Fragment");
                    FlurryAgent.logEvent("Home: " + "Article Clicked");
                    SlidingArticleFragment fragment = SlidingArticleFragment.newInstance(mArticleActualPosition, bean.getSid(),
                            true);
                    ((AppCompatActivity) mContext).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.FRAME_CONTENT, fragment).addToBackStack(null).commit();
                }
            });

            mArticleViewHolder.mShareArticleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharingArticleUtil.shareArticle(mContext, bean.getTi(), bean.getAl());
                }
            });

            //Enabling sliding for view pager
            new UniversalTouchListener(mArticleViewHolder.mArticlesLayout, true);
        }
    }

    public void reInitInlineAds() {
        adsInlineMap.clear();
        adsNativeMap.clear();
        adsReqIndex.clear();
    }


    private void loadInlineAd(final InlineAdViewHolder inlineAdViewHolder, final int position) {

        final Boolean isRequestSent = adsReqIndex.get(position);
        if(isRequestSent == null || !isRequestSent) {
            if (isFragmentVisible()) {
                adsReqIndex.put(position, true);
                final PublisherAdView publisherAdView = new PublisherAdView(mContext);
                final String unitId = mHomeDataList.get(position).getAdId();
                Log.i("Ads", "Home Adapter Unit ID: " + unitId);
                publisherAdView.setAdUnitId(unitId);
                publisherAdView.setAdSizes(AdSize.MEDIUM_RECTANGLE);

                publisherAdView.setAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        Log.i("Ads", "onAdLoaded home"+unitId);

                        inlineAdViewHolder.mDfpFramLayout.addView(publisherAdView);
                        inlineAdViewHolder.mDfpFramLayout.setBackground(null);
                        publisherAdView.setVisibility(View.VISIBLE);
                        inlineAdViewHolder.mDFPLinearLayout.setVisibility(View.VISIBLE);
//                        adsReqIndex.put(position, false);
                        adsInlineMap.put(position, publisherAdView);

                    }

                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        Log.i("Ads", "onAdFailedToLoad Home kk :-" + errorCode);
                        inlineAdViewHolder.mDFPLinearLayout.setVisibility(View.GONE);
                        adsReqIndex.put(position, false);
                    }

                    @Override
                    public void onAdOpened() {
                        Log.i("Ads", "onAdOpened");
                    }

                    @Override
                    public void onAdLeftApplication() {
                        Log.i("Ads", "onAdLeftApplication");
                        adsReqIndex.put(position, false);
                    }

                    @Override
                    public void onAdClosed() {
                        Log.i("Ads", "onAdClosed");
                        adsReqIndex.put(position, false);
                    }
                });

                // GDPR Consent Status
                nonPersonalizedAdsReqBundle = DFPConsent.GDPRStatusBundle(mMainActivity);
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


                publisherAdView.loadAd(request);

            }
            //Enabling sliding for view pager
            new UniversalTouchListener(inlineAdViewHolder.mDfpFramLayout, true);
        }
    }

    private void fillNativeAdData(final NativeAdViewHolder nativeAdViewHolder, final int position) {
        final Boolean isRequestSent = adsReqIndex.get(position);
        if(isRequestSent == null || !isRequestSent) {
            if (isFragmentVisible()) {
                adsReqIndex.put(position, true);
                String unitId = mHomeDataList.get(position).getAdId();
               final NativeContentAdView adView = nativeAdViewHolder.mNativeContentAdView;
                AdLoader.Builder builder = new AdLoader.Builder(mContext, unitId);
                builder.forContentAd(new NativeContentAd.OnContentAdLoadedListener() {
                    @Override
                    public void onContentAdLoaded(NativeContentAd ad) {
                        if (ad != null) {
                            adsReqIndex.put(position, false);
                            adView.setVisibility(View.VISIBLE);
                            nativeAdViewHolder.mParentLayout.setVisibility(View.VISIBLE);
                            adView.setHeadlineView(adView.findViewById(R.id.textview_article_list_header));
                            adView.setImageView((ImageView) adView.findViewById(R.id.imageview_article_list_image));
                            ((TextView) adView.getHeadlineView()).setText(ad.getHeadline());
                            ((ImageView) adView.getImageView()).setImageDrawable(ad.getImages().get(0).getDrawable());
                            adsNativeMap.put(position, ad);
                            adView.setNativeAd(ad);
                        }
                    }
                });


                AdLoader adLoader = builder.withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        adView.setVisibility(View.GONE);
                    }
                }).build();

                adLoader.loadAd(DFPConsent.getWithoutUrlAdsRequest(mContext));


            }
            //Enabling sliding for view pager
            new UniversalTouchListener(nativeAdViewHolder.mParentLayout, true);
        }
    }

    private void fillWidgetData(WidgetsViewHolder mWidgetsViewHolder, final int position) {

        final HomeAdapterModel dataBean = mHomeDataList.get(position);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, HORIZONTAL, false);

        mWidgetsViewHolder.mWidgetsRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_MOVE:
                        Log.i(TAG, "onInterceptTouchEvent: Action_move");
                        if (SlidingSectionFragment.mViewPager != null) {
                            SlidingSectionFragment.mViewPager.setPagingEnabled(false);
                        }
                        break;
                    case MotionEvent.ACTION_DOWN:
                        Log.i(TAG, "onInterceptTouchEvent: Action_move");
                        if (SlidingSectionFragment.mViewPager != null) {
                            SlidingSectionFragment.mViewPager.setPagingEnabled(false);
                        }
                        break;
                    case MotionEvent.ACTION_OUTSIDE:
                        Log.i(TAG, "onInterceptTouchEvent: Action_outside");
                        if (SlidingSectionFragment.mViewPager != null) {
                            SlidingSectionFragment.mViewPager.setPagingEnabled(true);
                        }
                        break;

                }
                return false;
            }
        });

        mWidgetsViewHolder.mWidgetsRecyclerView.setAdapter(dataBean.getmWidgetAdapter());
        mWidgetsViewHolder.mWidgetsRecyclerView.setLayoutManager(mLayoutManager);
        mWidgetsViewHolder.mWidgetTitleTextView.setText(dataBean.getWidgetName());

        mWidgetsViewHolder.mWidgetFooterTextView.setVisibility(View.VISIBLE);
        mWidgetsViewHolder.mWidgetFooterTextView.setText(
                mContext.getString(R.string.info_widget_viewall) + " " + dataBean.getWidgetName());

        mWidgetsViewHolder.mWidgetFooterTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int parentId = dataBean.getWidgetParentSecId();
                int sectionId = dataBean.getWidgetSectionId();
                int position;
                boolean isSubsection;
                String parentName;
                if (parentId == 0) {
                    position = DatabaseJanitor.getSectionPosition(sectionId);
                    parentName = dataBean.getWidgetName();
                    isSubsection = false;
                } else {
                    position = DatabaseJanitor.getSubsectionPostion(parentId, sectionId);
                    parentName = DatabaseJanitor.getParentSectionName(parentId);
                    isSubsection = true;
                }

                if (sectionId == 5 || sectionId == 37) {
                    if (onSeeMoreFromSectionClickListener != null) {
                        onSeeMoreFromSectionClickListener.onSectionClick(sectionId, parentId, dataBean.getWidgetName());
                    }
                } else {
                    if (position >= 0) {
                        SlidingSectionFragment fragment = SlidingSectionFragment.newInstance(SlidingSectionFragment.FROM_NORMAL,
                                position, isSubsection, parentId, parentName);
                        pushFragmentToBackStack(fragment);

                    } else if (position == -1) {
                        SlidingArticleFragment fragment = SlidingArticleFragment.newInstance(0, String.valueOf(sectionId),
                                false);
                        pushFragmentToBackStack(fragment);
                    }
                }
            }
        });

    }

    public void setIsFragmentVisibleToUser(boolean isVisible) {
        this.isFragmentVisibleToUser = isVisible;
        notifyDataSetChanged();
    }

    public boolean isFragmentVisible() {
        return isFragmentVisibleToUser;
    }

    public void setUpdatedListToHomeAdapter(List<HomeAdapterModel> mArticleList) {
        this.mHomeDataList = mArticleList;
        notifyDataSetChanged();
    }

    public void setAdViewNull() {
        mInlineAdView = null;
//        mNativeAdView = null;
    }

    private void pushFragmentToBackStack(Fragment fragment) {
        ((MainActivity) mContext).getSupportFragmentManager().beginTransaction()
                .replace(R.id.FRAME_CONTENT, fragment).addToBackStack(null).commit();
    }

    private void fillSensexData(SensexViewHolder sensexViewHolder, int position) {

        final HomeAdapterModel item = mHomeDataList.get(position);

        final BSEData bseData = item.getSensexData().getmBSEData();
        final NSEData nseData = item.getSensexData().getNSEData();
        int bseStatus = bseData.getStatus();
        int nseStatus = nseData.getStatus();

        // For BSE
        switch (bseStatus) {
            case SensexStatus.INITIALISING:
                sensexViewHolder.bseValParent.findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
                sensexViewHolder.bseValParent.findViewById(R.id.refresh).setVisibility(View.GONE);
                sensexViewHolder.bseValParent.findViewById(R.id.topVal).setVisibility(View.GONE);
                sensexViewHolder.bseValParent.findViewById(R.id.bottomVal).setVisibility(View.GONE);
                sensexViewHolder.bseValParent.findViewById(R.id.refresh).setOnClickListener(null);
                break;
            case SensexStatus.SUCCESS:
                sensexViewHolder.bseValParent.findViewById(R.id.progressBar).setVisibility(View.GONE);
                sensexViewHolder.bseValParent.findViewById(R.id.refresh).setVisibility(View.GONE);
                TextView bseTopVal = (TextView) sensexViewHolder.bseValParent.findViewById(R.id.topVal);
                TextView bseBottomVal = (TextView) sensexViewHolder.bseValParent.findViewById(R.id.bottomVal);

                bseTopVal.setVisibility(View.VISIBLE);
                bseBottomVal.setVisibility(View.VISIBLE);

                double latestPrice = bseData.getCp();
                double stockChange = bseData.getPer();
                double change = bseData.getCh();
                String latestUpdatedData = NumberFormat.getNumberInstance(Locale.US).format(latestPrice);
                bseTopVal.setText("" + latestUpdatedData);
                float changeIndicator;
                changeIndicator = (float) stockChange;
                if (changeIndicator < 0) {
                    bseBottomVal.setText("" + change + "(" + stockChange + "%" + ")");
                    bseBottomVal.setTextColor(mContext.getResources().getColor(R.color.RedDown));
                    bseBottomVal.setCompoundDrawablesWithIntrinsicBounds(R.drawable.stockchange_loss, 0, 0, 0);
                } else if (changeIndicator > 0) {
                    bseBottomVal.setText("+" + change + "(" + stockChange + "%" + ")");
                    bseBottomVal.setTextColor(mContext.getResources().getColor(R.color.GreenUp));
                    bseBottomVal.setCompoundDrawablesWithIntrinsicBounds(R.drawable.stockchange_profit, 0, 0, 0);//.setImageResource(R.drawable.stockchange_profit);

                } else {
                    bseBottomVal.setText("N.A.");
                }


                sensexViewHolder.mBseParentLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onSeeMoreFromSectionClickListener != null) {
                            onSeeMoreFromSectionClickListener.onIndicesClick(true);
                        }
                    }
                });

                break;
            case SensexStatus.ERROR:
                sensexViewHolder.bseValParent.findViewById(R.id.progressBar).setVisibility(View.GONE);
                sensexViewHolder.bseValParent.findViewById(R.id.refresh).setVisibility(View.VISIBLE);
                sensexViewHolder.bseValParent.findViewById(R.id.refresh)
                        .setOnClickListener(sensexClickListener("BSE", bseData, null, null, position));

                bseData.setStatus(SensexStatus.INITIALISING);
                break;
            case SensexStatus.NONE:
                bseData.setStatus(SensexStatus.INITIALISING);
                sensexViewHolder.bseValParent.findViewById(R.id.refresh).setOnClickListener(null);
                new GetBSEDataTask(position).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Constants.BSE_URL);
                sensexViewHolder.bseValParent.findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
                sensexViewHolder.bseValParent.findViewById(R.id.refresh).setVisibility(View.GONE);
                sensexViewHolder.bseValParent.findViewById(R.id.topVal).setVisibility(View.GONE);
                sensexViewHolder.bseValParent.findViewById(R.id.bottomVal).setVisibility(View.GONE);
                break;
        }

        // For NSE
        switch (nseStatus) {
            case SensexStatus.INITIALISING:
                sensexViewHolder.nseValParent.findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
                sensexViewHolder.nseValParent.findViewById(R.id.refresh).setVisibility(View.GONE);
                sensexViewHolder.nseValParent.findViewById(R.id.topVal).setVisibility(View.GONE);
                sensexViewHolder.nseValParent.findViewById(R.id.bottomVal).setVisibility(View.GONE);
                sensexViewHolder.nseValParent.findViewById(R.id.refresh).setOnClickListener(null);
                break;
            case SensexStatus.SUCCESS:
                sensexViewHolder.nseValParent.findViewById(R.id.refresh).setVisibility(View.GONE);
                sensexViewHolder.nseValParent.findViewById(R.id.progressBar).setVisibility(View.GONE);

                TextView nseTopVal = (TextView) sensexViewHolder.nseValParent.findViewById(R.id.topVal);
                TextView nseBottomVal = (TextView) sensexViewHolder.nseValParent.findViewById(R.id.bottomVal);

                nseTopVal.setVisibility(View.VISIBLE);
                nseBottomVal.setVisibility(View.VISIBLE);

                double latestPrice = nseData.getCp();
                double stockChange = nseData.getPer();
                double change = nseData.getCh();
                String latestUpdatedData = NumberFormat.getNumberInstance(Locale.US).format(latestPrice);
                nseTopVal.setText("" + latestUpdatedData);
                float changeIndicator;
                changeIndicator = (float) stockChange;
                if (changeIndicator < 0) {
                    nseBottomVal.setText("" + change + "(" + stockChange + "%" + ")");
                    nseBottomVal.setTextColor(mContext.getResources().getColor(R.color.RedDown));
                    nseBottomVal.setCompoundDrawablesWithIntrinsicBounds(R.drawable.stockchange_loss, 0, 0, 0);

                } else if (changeIndicator > 0) {
                    nseBottomVal.setText("+" + change + "(" + stockChange + "%" + ")");
                    nseBottomVal.setTextColor(mContext.getResources().getColor(R.color.GreenUp));
                    nseBottomVal.setCompoundDrawablesWithIntrinsicBounds(R.drawable.stockchange_profit, 0, 0, 0);
                } else {
                    nseBottomVal.setText("N.A.");
                }


                sensexViewHolder.mNseParentLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onSeeMoreFromSectionClickListener != null) {
                            onSeeMoreFromSectionClickListener.onIndicesClick(false);
                        }
                    }
                });

                break;
            case SensexStatus.ERROR:
                sensexViewHolder.nseValParent.findViewById(R.id.refresh).setVisibility(View.VISIBLE);
                sensexViewHolder.nseValParent.findViewById(R.id.progressBar).setVisibility(View.GONE);
                sensexViewHolder.nseValParent.findViewById(R.id.refresh)
                        .setOnClickListener(sensexClickListener("NSE", null, nseData, null, position));
                break;
            case SensexStatus.NONE:
                nseData.setStatus(SensexStatus.INITIALISING);
                sensexViewHolder.nseValParent.findViewById(R.id.refresh).setOnClickListener(null);
                new GetNSEDataTask(position).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Constants.NSE_URL);
                sensexViewHolder.nseValParent.findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
                sensexViewHolder.nseValParent.findViewById(R.id.refresh).setVisibility(View.GONE);
                sensexViewHolder.nseValParent.findViewById(R.id.topVal).setVisibility(View.GONE);
                sensexViewHolder.nseValParent.findViewById(R.id.bottomVal).setVisibility(View.GONE);
                break;
        }
    }

    private void notifySensexData() {
        notifyDataSetChanged();
    }

    private View.OnClickListener sensexClickListener(final String from, final BSEData bse, final NSEData nse,
                                                     final GoldDollarDataModel goldForex, final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (from) {
                    case "NSE":
                        nse.setStatus(SensexStatus.INITIALISING);
                        new GetNSEDataTask(position).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Constants.NSE_URL);
                        break;
                    case "BSE":
                        bse.setStatus(SensexStatus.INITIALISING);
                        new GetBSEDataTask(position).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Constants.BSE_URL);
                        break;

                }
                notifyDataSetChanged();

            }
        };
    }

    private void fillBannerData(final BannerViewHolder mBannerViewHolder, final int positionParam) {
        final ArticleDetail bean = mHomeDataList.get(positionParam).getmArticle();
        if (bean != null) {
            final Realm mRealm = Businessline.getRealmInstance();
            String articleType = bean.getArticleType();
            String multiMediaUrl = null;
            final BookmarkBean bookmarkBean = mRealm.where(BookmarkBean.class).equalTo("aid", bean.getAid()).findFirst();
            if (bookmarkBean != null && bookmarkBean.getAid() == bean.getAid()) {
                mBannerViewHolder.mBookmarkButton.setBackgroundResource(R.drawable.bookmark_feed_filled);
            } else {
                mBannerViewHolder.mBookmarkButton.setBackgroundResource(R.drawable.bookmark_feed);
            }

            String imageUrl = null;
            if (bean.getHi() == 1) {
                imageUrl = bean.getMe().get(0).getIm_v2();
                if (imageUrl == null) {
                    imageUrl = bean.getMe().get(0).getIm();
                }
                imageUrl = imageUrl.replace(Constants.REPLACE_IMAGE_FREE, Constants.BANNER_THUMB_SIZE);
            }
            if (imageUrl != null && !TextUtils.isEmpty(imageUrl)) {
                PicassoUtils.downloadImage(mContext, imageUrl, mBannerViewHolder.mBannerImageView, R.mipmap.ph_topnews_th);
            } else {
                mBannerViewHolder.mBannerImageView.setImageResource(R.mipmap.ph_topnews_th);
            }

            mBannerViewHolder.mMultimediaButton.setVisibility(View.VISIBLE);
            switch (articleType) {
                case Constants.TYPE_ARTICLE:
                    mBannerViewHolder.mMultimediaButton.setVisibility(View.GONE);
                    break;
                case Constants.TYPE_AUDIO:
                    mBannerViewHolder.mMultimediaButton.setVisibility(View.VISIBLE);
                    mBannerViewHolder.mMultimediaButton.setBackgroundResource(R.mipmap.audio);
                    multiMediaUrl = bean.getAudioLink();
                    break;
                case Constants.TYPE_PHOTO:
                    mBannerViewHolder.mMultimediaButton.setVisibility(View.VISIBLE);
                    mBannerViewHolder.mMultimediaButton.setBackgroundResource(R.mipmap.slide);
                    break;
                case Constants.TYPE_VIDEO:
                    mBannerViewHolder.mMultimediaButton.setVisibility(View.VISIBLE);
                    mBannerViewHolder.mMultimediaButton.setBackgroundResource(R.mipmap.play_updated);
                    multiMediaUrl = bean.getYoutube_video_id();
                    break;
            }

            mBannerViewHolder.mBannerTextView.setText(bean.getTi());

            String publishTime = bean.getGmt();
            String timeDiff = AppUtils.getDurationFormattedDate(AppUtils.changeStringToMillisGMT(publishTime));
            mBannerViewHolder.mArticleUpdateTime.setText(timeDiff);
            mBannerViewHolder.mArticleSectionName.setText(bean.getSname());

            mBannerViewHolder.mBannerLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SlidingArticleFragment fragment = SlidingArticleFragment.newInstance(mHomeDataList.get(positionParam).getArticleActualPositionInList(), bean.getSid(), true);
                    GoogleAnalyticsTracker.setGoogleAnalyticsEvent(mContext, "Banner", "Banner: Article Clicked", "Home Fragment");
                    FlurryAgent.logEvent("Banner: " + "Article Clicked");
                    ((AppCompatActivity) mContext).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.FRAME_CONTENT, fragment).addToBackStack(null).commitAllowingStateLoss();
                }
            });

            mBannerViewHolder.mMultimediaButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SlidingArticleFragment fragment = SlidingArticleFragment.newInstance(mHomeDataList.get(positionParam).getArticleActualPositionInList(), bean.getSid(), true);
                    GoogleAnalyticsTracker.setGoogleAnalyticsEvent(mContext, "Banner", "Banner: Article Clicked", "Home Fragment");
                    FlurryAgent.logEvent("Banner: " + "Article Clicked");
                    ((AppCompatActivity) mContext).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.FRAME_CONTENT, fragment).addToBackStack(null).commit();
                }
            });
            final String multiMediaLink = multiMediaUrl;
            mBannerViewHolder.mBookmarkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GoogleAnalyticsTracker.setGoogleAnalyticsEvent(mContext, "Home", "Home: Bookmark button Clicked", "Home Fragment");
                    FlurryAgent.logEvent("Home: " + "Bookmark button Clicked");
                    if (bookmarkBean == null) {

                        ArticleUtil.markAsBookMark(mRealm, bean.getAid(), bean.getSid(), bean.getSname(), bean.getPd(),
                                bean.getOd(), bean.getPid(), bean.getTi(), bean.getAu(), bean.getAl(), bean.getBk(),
                                bean.getGmt(), bean.getDe(), bean.getLe(), bean.getHi(),
                                ArticleUtil.getImageBeanList(bean.getMe()), System.currentTimeMillis(),
                                false, bean.getAdd_pos(), multiMediaLink,
                                bean.getArticleType(), bean.getVid(), bean.getParentId(), bean.getLocation());

                        mBannerViewHolder.mBookmarkButton.setBackgroundResource(R.drawable.bookmark_feed_filled);
                        AppUtils.showToast(mContext, mContext.getResources().getString(R.string.added_to_bookmark));
                    } else {

                        ArticleUtil.removeFromBookMark(mRealm, bookmarkBean);
                        mBannerViewHolder.mBookmarkButton.setBackgroundResource(R.drawable.bookmark_feed);
                        AppUtils.showToast(mContext, mContext.getResources().getString(R.string.removed_from_bookmark));
                    }
                    notifyDataSetChanged();

                }
            });

            mBannerViewHolder.mShareArticleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharingArticleUtil.shareArticle(mContext, bean.getTi(), bean.getAl());
                }
            });
        }
        //Enabling sliding for view pager
        new UniversalTouchListener(mBannerViewHolder.mBannerLayout, true);

    }

    private class BannerViewHolder extends RecyclerView.ViewHolder {
        private ImageView mBannerImageView;
        private TextView mBannerTextView;
        private RelativeLayout mBannerLayout;
        private TextView mArticleUpdateTime;
        private TextView mArticleSectionName;
        private Button mBookmarkButton;
        private Button mShareArticleButton;
        private ImageButton mMultimediaButton;

        public BannerViewHolder(View itemView) {
            super(itemView);
            mBookmarkButton = (Button) itemView.findViewById(R.id.button_bookmark);
            mBannerLayout = (RelativeLayout) itemView.findViewById(R.id.layout_articles_root);
            mBannerImageView = (ImageView) itemView.findViewById(R.id.imageview_article_list_image);
            mBannerTextView = (TextView) itemView.findViewById(R.id.textview_article_list_header);
            mArticleSectionName = (TextView) itemView.findViewById(R.id.section_name);
            mArticleSectionName.setVisibility(View.VISIBLE);
            mArticleUpdateTime = (TextView) itemView.findViewById(R.id.textview_time);
            mShareArticleButton = (Button) itemView.findViewById(R.id.button_article_share);
            mMultimediaButton = (ImageButton) itemView.findViewById(R.id.multimedia_button);
            itemView.findViewById(R.id.line_view).setVisibility(View.VISIBLE);
        }


    }

    private class CustomiseButtonViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout mParentLayout;

        public CustomiseButtonViewHolder(View itemView) {
            super(itemView);
            mParentLayout = (LinearLayout) itemView.findViewById(R.id.parentLayout);
        }
    }

    private class ArticlesViewHolder extends RecyclerView.ViewHolder {

        public ImageView mArticleImageView;
        public TextView mArticleTextView;
        public TextView mArticleTimeTextView;
        public TextView mAuthorTextView;
        public TextView mArticleSectionName;
        public RelativeLayout mArticlesLayout;
        public Button mBookmarkButton;
        public Button mShareArticleButton;
        public ImageButton mMultimediaButton;
        public FrameLayout mImageParentLayout;

        public ArticlesViewHolder(View itemView) {
            super(itemView);
            mMultimediaButton = (ImageButton) itemView.findViewById(R.id.multimedia_button);
            mImageParentLayout = (FrameLayout) itemView.findViewById(R.id.imageParentLayout);
            mArticlesLayout = (RelativeLayout) itemView.findViewById(R.id.layout_articles_root);
            mArticleImageView = (ImageView) itemView.findViewById(R.id.imageview_article_list_image);
            mArticleTextView = (TextView) itemView.findViewById(R.id.textview_article_list_header);
            mShareArticleButton = (Button) itemView.findViewById(R.id.button_article_share);
            mArticleTimeTextView = (TextView) itemView.findViewById(R.id.textview_time);
            mBookmarkButton = (Button) itemView.findViewById(R.id.button_bookmark);
            mArticleSectionName = (TextView) itemView.findViewById(R.id.section_name);
            mArticleSectionName.setVisibility(View.VISIBLE);
            mAuthorTextView = (TextView) itemView.findViewById(R.id.author_textView);
            itemView.findViewById(R.id.line_view).setVisibility(View.VISIBLE);
        }
    }

    private class WidgetsViewHolder extends RecyclerView.ViewHolder {

        public RecyclerView mWidgetsRecyclerView;
        public TextView mWidgetTitleTextView, mWidgetFooterTextView;

        public WidgetsViewHolder(View itemView) {
            super(itemView);

            mWidgetsRecyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerview_widgets);
            mWidgetTitleTextView = (TextView) itemView.findViewById(R.id.textview_widget_title);
            mWidgetFooterTextView = (TextView) itemView.findViewById(R.id.textview_widget_viewAll);

        }
    }

    private class InlineAdViewHolder extends RecyclerView.ViewHolder {

        public CardView mDfpCardView;
        public FrameLayout mDfpFramLayout;
        public LinearLayout mDFPLinearLayout;


        public InlineAdViewHolder(View itemView) {
            super(itemView);
            mDfpCardView = (CardView) itemView.findViewById(R.id.dfpCardView);
            mDfpFramLayout = (FrameLayout) itemView.findViewById(R.id.dfpFramLayout);
            mDFPLinearLayout = itemView.findViewById(R.id.dfpLinearyLayout);
        }
    }



    private class NativeAdViewHolder extends RecyclerView.ViewHolder {

        public ImageView mArticleImageView;
        public TextView mArticleTextView;
        public RelativeLayout mLinearLayout;
        public CardView mParentLayout;
        public NativeContentAdView mNativeContentAdView;


        public NativeAdViewHolder(View itemView) {
            super(itemView);
            mNativeContentAdView = (NativeContentAdView) itemView.findViewById(R.id.nativeContentAdView);
            mParentLayout = (CardView) itemView.findViewById(R.id.parentLayout);
            mArticleImageView = (ImageView) itemView.findViewById(R.id.imageview_article_list_image);
            mArticleTextView = (TextView) itemView.findViewById(R.id.textview_article_list_header);
            mLinearLayout = (RelativeLayout) itemView.findViewById(R.id.layout_articles_root);
        }
    }

    private class SensexViewHolder extends RecyclerView.ViewHolder {
        public TextView mBseTitle;

        public TextView mNseTitle;

//        public TextView mForexTitle;

//        public TextView mGoldTitle;

        public LinearLayout mBseParentLayout;
        public LinearLayout mNseParentLayout;
//        public LinearLayout mForexParentLayout;
//        public LinearLayout mGoldParentLayout;

        public RelativeLayout bseValParent;
        public RelativeLayout nseValParent;
//        public RelativeLayout forexValParent;
//        public RelativeLayout goldValParent;

        public SensexViewHolder(View itemView) {
            super(itemView);
            mBseTitle = (TextView) itemView.findViewById(R.id.bseSensexTitle);

            mNseTitle = (TextView) itemView.findViewById(R.id.nseSensexTitle);

//            mForexTitle = (TextView) itemView.findViewById(R.id.forexSensexTitle);

//            mGoldTitle = (TextView) itemView.findViewById(R.id.goldSensexTitle);

            mBseParentLayout = (LinearLayout) itemView.findViewById(R.id.bseParentLayout);
            mNseParentLayout = (LinearLayout) itemView.findViewById(R.id.nseParentLayout);
//            mForexParentLayout = (LinearLayout) itemView.findViewById(R.id.forexParentLayout);
//            mGoldParentLayout = (LinearLayout) itemView.findViewById(R.id.goldParentLayout);


            bseValParent = (RelativeLayout) itemView.findViewById(R.id.bseValLayout);
            nseValParent = (RelativeLayout) itemView.findViewById(R.id.nseValLayout);
//            forexValParent = (RelativeLayout) itemView.findViewById(R.id.forexValLayout);
//            goldValParent = (RelativeLayout) itemView.findViewById(R.id.goldValLayout);


        }
    }


    /**
     * Gold & Forex Task Request
     */

/*
    private class GetGoldDollarTask extends AsyncTask<String, Void, GoldDollarDataModel> {
        private static final String TAG = "GetGoldDollarTask";

        @Override
        protected GoldDollarDataModel doInBackground(String... strings) {
            if (isCancelled()) {
                return null;
            }
            GoldDollarDataModel response = null;
            try {

                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream is = connection.getInputStream();
                    Reader reader = new InputStreamReader(is);
                    Gson gson = new Gson();
                    response = gson.fromJson(reader, GoldDollarDataModel.class);
                }
                if (isCancelled()) {
                    return null;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (IllegalStateException | JsonSyntaxException exception) {
                exception.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(GoldDollarDataModel goldDollarDataModel) {
            super.onPostExecute(goldDollarDataModel);
            if (HomeAdapter.this != null) {
                GoldDollarDataModel bs = mHomeDataList.get(0).getSensexData().getmGoldDollarDataModel();
                if (goldDollarDataModel == null) {
                    bs.setStatus(SensexStatus.ERROR);
                } else {
                    bs.setStatus(SensexStatus.SUCCESS);
                    bs.setData(goldDollarDataModel.getData());
                }

                notifySensexData();

            }
        }
    }
*/

    private class GetBSEDataTask extends AsyncTask<String, Void, BSEData> {
        private int position;

        private GetBSEDataTask(int positon) {
            this.position = positon;
        }

        @Override
        protected BSEData doInBackground(String... strings) {
            if (isCancelled()) {
                return null;
            }
            BSEData response = null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream is = connection.getInputStream();
                    Reader reader = new InputStreamReader(is);
                    Gson gson = new Gson();
                    response = gson.fromJson(reader, BSEData.class);
                }
                if (isCancelled()) {
                    return null;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();

            }
            return response;
        }

        @Override
        protected void onPostExecute(BSEData s) {
            super.onPostExecute(s);

            if (HomeAdapter.this != null) {
                BSEData bs = mHomeDataList.get(position).getSensexData().getmBSEData();
                if (s == null) {
                    bs.setStatus(SensexStatus.ERROR);
                } else {
                    bs.setStatus(SensexStatus.SUCCESS);
                    bs.setCh(s.getCh());
                    bs.setCp(s.getCp());
                    bs.setPer(s.getPer());
                    bs.setDa(s.getDa());
                    bs.setSnapShot(s.getSnapShot());
                }
                notifySensexData();
            }
        }
    }

    /**
     * Sends Request to get NSE data from Server
     */

    private class GetNSEDataTask extends AsyncTask<String, Void, NSEData> {

        private int position;

        private GetNSEDataTask(int positon) {
            this.position = positon;
        }

        @Override
        protected NSEData doInBackground(String... strings) {
            if (isCancelled()) {
                return null;
            }
            NSEData response = null;
            try {

                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream is = connection.getInputStream();
                    Reader reader = new InputStreamReader(is);
                    Gson gson = new Gson();
                    response = gson.fromJson(reader, NSEData.class);
                }
                if (isCancelled()) {
                    return null;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(NSEData s) {
            super.onPostExecute(s);

            if (HomeAdapter.this != null) {
                NSEData bs = mHomeDataList.get(position).getSensexData().getNSEData();
                if (s == null) {
                    bs.setStatus(SensexStatus.ERROR);
                } else {
                    bs.setStatus(SensexStatus.SUCCESS);
                    bs.setCh(s.getCh());
                    bs.setCp(s.getCp());
                    bs.setPer(s.getPer());
                    bs.setDa(s.getDa());
                    bs.setSnapShot(s.getSnapShot());
                }
                notifySensexData();
            }
        }
    }

}
