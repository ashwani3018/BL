package com.mobstac.thehindubusinessline.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.google.android.gms.ads.formats.NativeContentAdView;
import com.mobstac.thehindubusinessline.Businessline;
import com.mobstac.thehindubusinessline.R;
import com.mobstac.thehindubusinessline.fragments.SlidingArticleFragment;
import com.mobstac.thehindubusinessline.fragments.SlidingSectionFragment;
import com.mobstac.thehindubusinessline.holder.StaticItemWebViewHolder;
import com.mobstac.thehindubusinessline.model.BookmarkBean;
import com.mobstac.thehindubusinessline.model.SectionAdapterItem;
import com.mobstac.thehindubusinessline.model.SectionTable;
import com.mobstac.thehindubusinessline.model.databasemodel.ArticleDetail;
import com.mobstac.thehindubusinessline.utils.AppUtils;
import com.mobstac.thehindubusinessline.utils.ArticleUtil;
import com.mobstac.thehindubusinessline.utils.Constants;
import com.mobstac.thehindubusinessline.utils.DFPConsent;
import com.mobstac.thehindubusinessline.utils.FeatureConstant;
import com.mobstac.thehindubusinessline.utils.GoogleAnalyticsTracker;
import com.mobstac.thehindubusinessline.utils.NetworkUtils;
import com.mobstac.thehindubusinessline.utils.PicassoUtils;
import com.mobstac.thehindubusinessline.utils.SharedPreferenceHelper;
import com.mobstac.thehindubusinessline.utils.SharingArticleUtil;
import com.mobstac.thehindubusinessline.utils.StaticItemDummyViewClick;
import com.mobstac.thehindubusinessline.utils.UniversalTouchListener;
import com.mobstac.thehindubusinessline.utils.WebViewLinkClick;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;

import static android.support.v7.widget.LinearLayoutManager.HORIZONTAL;

/**
 * Created by root on 26/8/16.
 */
public class SectionLandingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "SectionLandingAdapter";
    private Context mContext;
    private ArrayList<SectionAdapterItem> mSectionArticleList;
    private OrderedRealmCollection<SectionTable> mSubSection;
    private String mParentSectionName;
//    private int mSectionId = 0;
    //    private VmaxAdView mNativeAdView;
    private String mSectionType;
    private boolean isFragmentVisibleToUser;
    private SectionExploreAdapter mExploreAdapter;
    private boolean isNativeAdsRequested;

    private Map<Integer, Boolean> adsReqIndex = new HashMap<>();
    private Map<Integer, PublisherAdView> adsInlineMap = new HashMap<>();
    private Map<Integer, NativeContentAd> adsNativeMap = new HashMap<>();

    private boolean isNetworkAvailable;

    public SectionLandingAdapter(Context ctxParam, ArrayList<SectionAdapterItem> mArticleList, OrderedRealmCollection<SectionTable> subSections,
                                 String sectionName, String sectionType, String outBrainLink, boolean isNetworkAvailable) {
        mContext = ctxParam;
        mSectionArticleList = mArticleList;
        mSubSection = subSections;
        mParentSectionName = sectionName;
        mSectionType = sectionType;
        this.isNetworkAvailable = isNetworkAvailable;

        /*if (mSectionArticleList.size() > 0) {
            mSectionId = Integer.parseInt(mSectionArticleList.get(0).getArticleDetail().getSid());
        }*/
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder mViewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case SectionAdapterItem.ARTICLE:
                View articleView = inflater.inflate(R.layout.cardview_article_list, parent, false);
                mViewHolder = new SectionViewHolder(articleView);
                break;
            case SectionAdapterItem.EXPLORE:
                View exploreView = inflater.inflate(R.layout.cardview_home_explore, parent, false);
                mViewHolder = new ExploreViewHolder(exploreView);
                break;
            case SectionAdapterItem.NATIVE_AD:
                View nativeView = inflater.inflate(R.layout.cardview_native_ad, parent, false);
                mViewHolder = new NativeAdViewHolder(nativeView);
                break;
            case SectionAdapterItem.LOADING:
                View v2 = inflater.inflate(R.layout.pagination_item_progress, parent, false);
                mViewHolder = new LoadingVH(v2);
                break;
            case SectionAdapterItem.VIEW_INLINE_AD:
                View inlineAdViewHolder = inflater.inflate(R.layout.inline_ad_container, parent, false);
                mViewHolder = new InlineAdViewHolder(inlineAdViewHolder);
                break;
            case SectionAdapterItem.WEB_CONTAINT:
                View staticWebview = inflater.inflate(R.layout.item_webview, parent, false);
                mViewHolder = new StaticItemWebViewHolder(staticWebview);
                break;

        }
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()) {
            case SectionAdapterItem.ARTICLE:
                SectionViewHolder articleHolder = (SectionViewHolder) holder;
                fillArticleData(articleHolder, position);
                break;
            case SectionAdapterItem.EXPLORE:
                ExploreViewHolder exploreHolder = (ExploreViewHolder) holder;
                fillExploreData(exploreHolder, position);
                break;
            case SectionAdapterItem.NATIVE_AD:
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
                    fillNativeAdData(nativeAdViewHolder, mSectionArticleList.get(position).getAdId(), position);
                } else {
                    adViewN.setVisibility(View.VISIBLE);
                    nativeAdViewHolder.mParentLayout.setVisibility(View.VISIBLE);

                    ((TextView) adViewN.getHeadlineView()).setText(contentAd.getHeadline());
                    ((ImageView) adViewN.getImageView()).setImageDrawable(contentAd.getImages().get(0).getDrawable());

                    adViewN.setNativeAd(contentAd);
                }

                break;
            case SectionAdapterItem.VIEW_INLINE_AD:
                InlineAdViewHolder inlineAdViewHolder = (InlineAdViewHolder) holder;
                inlineAdViewHolder.frameLayout.removeAllViews();
                inlineAdViewHolder.frameLayout.setBackgroundResource(R.drawable.interstetial_ads_bg);
                final PublisherAdView adView = adsInlineMap.get(position);
                if(adView == null) {
                    loadInlineAd(inlineAdViewHolder.frameLayout, mSectionArticleList.get(position).getAdId(), position);
                } else {
                    inlineAdViewHolder.frameLayout.setBackground(null);
                    inlineAdViewHolder.frameLayout.addView(adView);
                }
                break;

            case SectionAdapterItem.WEB_CONTAINT:
                StaticItemWebViewHolder staticItemHolder = (StaticItemWebViewHolder) holder;
                String staticPageUrl = mSectionArticleList.get(position).getStaticPageUrl();
                final String[] url = staticPageUrl.split("00#00");
                staticItemHolder.webView.loadUrl(url[0]);
                if(url.length > 1 && !url[1].equals("0")) {
                    staticItemHolder.mDummyView.setVisibility(View.VISIBLE);
                    staticItemHolder.mDummyView.setOnClickListener(new StaticItemDummyViewClick(url, mContext));
                } else {
                    staticItemHolder.mDummyView.setVisibility(View.GONE);
                    // Enabling Weblink click on Lead Text
                    new WebViewLinkClick().linkClick(staticItemHolder.webView, staticItemHolder.itemView.getContext());
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        int listSize = mSectionArticleList.size();
        return listSize;
    }

    @Override
    public int getItemViewType(int position) {
        return mSectionArticleList.get(position).getViewType();
    }


    private void fillExploreData(ExploreViewHolder exploreHolder, int position) {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, HORIZONTAL, false);
        exploreHolder.mExploreRecyclerView.setLayoutManager(mLayoutManager);
        exploreHolder.mExploreRecyclerView.setHasFixedSize(true);
        if (mExploreAdapter == null) {
            mExploreAdapter = new SectionExploreAdapter(mContext, mSubSection, Long.valueOf(mSectionArticleList.get(position).getArticleDetail().getSid()), mParentSectionName);
        }
        exploreHolder.mExploreRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i(TAG, "onTouch: Action_touch");
                int action = event.getAction();
                Log.i(TAG, "onTouch: action " + action);
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
        exploreHolder.mExploreRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                Log.i(TAG, "onInterceptTouchEvent: RecyclerView");
                int action = e.getAction();
                Log.i(TAG, "onInterceptTouchEvent: RecyclerView " + action);
                if (SlidingSectionFragment.mViewPager != null) {
                    SlidingSectionFragment.mViewPager.setPagingEnabled(false);
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                Log.i(TAG, "onTouchEvent: RecyclerView");
                if (SlidingSectionFragment.mViewPager != null) {
                    SlidingSectionFragment.mViewPager.setPagingEnabled(false);
                }
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
                Log.i(TAG, "onRequestDisallowInterceptTouchEvent: RecyclerView");
            }
        });
        exploreHolder.mExploreRecyclerView.setAdapter(mExploreAdapter);
    }

    private void fillArticleData(SectionViewHolder articleHolder, final int position) {
        final Realm mRealm = Businessline.getRealmInstance();
        final ArticleDetail bean = mSectionArticleList.get(position).getArticleDetail();
        String multiMediaUrl = null;

        if (mSectionArticleList.size() > 0) {
            final BookmarkBean bookmarkBean = mRealm.where(BookmarkBean.class).equalTo("aid", bean.getAid()).findFirst();
            if (bookmarkBean != null && bookmarkBean.getAid() == bean.getAid()) {
                articleHolder.mBookmarkButton.setBackgroundResource(R.drawable.bookmark_feed_filled);
            } else {
                articleHolder.mBookmarkButton.setBackgroundResource(R.drawable.bookmark_feed);
            }
            String articleType = bean.getArticleType();

            String imageUrl = bean.getIm_thumbnail_v2();
            if (imageUrl == null) {
                imageUrl = bean.getIm_thumbnail();
            }
            if (imageUrl != null && !TextUtils.isEmpty(imageUrl)) {
                imageUrl = imageUrl.replace(Constants.REPLACE_IMAGE_FREE, Constants.ROW_THUMB_SIZE);
                articleHolder.mImageParentLayout.setVisibility(View.VISIBLE);
                articleHolder.mArticleImageView.setVisibility(View.VISIBLE);
                PicassoUtils.downloadImage(mContext, imageUrl, articleHolder.mArticleImageView, R.mipmap.ph_newsfeed_th);
            } else {
                if (articleType.equalsIgnoreCase(Constants.TYPE_PHOTO) || articleType.equalsIgnoreCase(Constants.TYPE_AUDIO) || articleType.equalsIgnoreCase(Constants.TYPE_VIDEO)) {
                    articleHolder.mImageParentLayout.setVisibility(View.VISIBLE);
                    articleHolder.mArticleImageView.setVisibility(View.VISIBLE);
                    PicassoUtils.downloadImage(mContext, "https//", articleHolder.mArticleImageView, R.mipmap.ph_newsfeed_th);
                } else {
                    articleHolder.mArticleImageView.setVisibility(View.GONE);
                    articleHolder.mImageParentLayout.setVisibility(View.GONE);
                }
            }

            articleHolder.mMultimediaButton.setVisibility(View.VISIBLE);
            switch (articleType) {
                case Constants.TYPE_ARTICLE:
                    articleHolder.mMultimediaButton.setVisibility(View.GONE);
                    break;
                case Constants.TYPE_AUDIO:
                    articleHolder.mMultimediaButton.setVisibility(View.VISIBLE);
                    articleHolder.mMultimediaButton.setBackgroundResource(R.mipmap.audio);
                    multiMediaUrl = bean.getAudioLink();
                    break;
                case Constants.TYPE_PHOTO:
                    articleHolder.mMultimediaButton.setVisibility(View.VISIBLE);
                    articleHolder.mMultimediaButton.setBackgroundResource(R.mipmap.slide);
                    break;
                case Constants.TYPE_VIDEO:
                    articleHolder.mMultimediaButton.setVisibility(View.VISIBLE);
                    articleHolder.mMultimediaButton.setBackgroundResource(R.mipmap.play_updated);
                    multiMediaUrl = bean.getYoutube_video_id();
                    break;
            }

            articleHolder.mArticleTextView.setText(bean.getTi());
            articleHolder.mAuthorTextView.setText(bean.getAu());
            String publishTime = bean.getGmt();
            String timeDiff = AppUtils.getDurationFormattedDate(AppUtils.changeStringToMillisGMT(publishTime));
            articleHolder.mArticleUpdateTime.setText(timeDiff);
            articleHolder.mArticleSectionName.setText(bean.getSname());
            articleHolder.mParentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GoogleAnalyticsTracker.setGoogleAnalyticsEvent(mContext, "SectionLanding", "SectionLanding: Article Clicked", "Section landing Fragment");
                    FlurryAgent.logEvent("SectionLanding: " + " Article Clicked");
                    SlidingArticleFragment fragment = SlidingArticleFragment.newInstance(
                            mSectionArticleList.get(position).getArticleActualPos(),
                            bean.getSid(),
                            false);
                    ((AppCompatActivity) mContext).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.FRAME_CONTENT, fragment).addToBackStack(null).commit();
                }
            });

            articleHolder.mMultimediaButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GoogleAnalyticsTracker.setGoogleAnalyticsEvent(mContext, "SectionLanding", "SectionLanding: Article Clicked", "Section landing Fragment");
                    FlurryAgent.logEvent("SectionLanding: " + " Article Clicked");
                    SlidingArticleFragment fragment = SlidingArticleFragment.newInstance(mSectionArticleList.get(position).getArticleActualPos(),
                            bean.getSid(), false);
                    ((AppCompatActivity) mContext).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.FRAME_CONTENT, fragment).addToBackStack(null).commit();
                }
            });
            final String multiMediaLink = multiMediaUrl;
            articleHolder.mBookmarkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GoogleAnalyticsTracker.setGoogleAnalyticsEvent(mContext, "SectionLanding", "SectionLanding: Bookmark button Clicked", "Section landing Fragment");
                    FlurryAgent.logEvent("SectionLanding: " + " Bookmark button Clicked");
                    if (bookmarkBean == null) {

                        ArticleUtil.markAsBookMark(mRealm, bean.getAid(), bean.getSid(), bean.getSname(), bean.getPd(),
                                bean.getOd(), bean.getPid(), bean.getTi(), bean.getAu(), bean.getAl(), bean.getBk(),
                                bean.getGmt(), bean.getDe(), bean.getLe(), bean.getHi(),
                                ArticleUtil.getImageBeanList(bean.getMe()), System.currentTimeMillis(),
                                false, bean.getAdd_pos(), multiMediaLink,
                                bean.getArticleType(), bean.getVid(), bean.getParentId(), bean.getLocation());

                        AppUtils.showToast(mContext, mContext.getResources().getString(R.string.added_to_bookmark));
                    } else {
                        ArticleUtil.removeFromBookMark(mRealm, bookmarkBean);
                        AppUtils.showToast(mContext, mContext.getResources().getString(R.string.removed_from_bookmark));
                    }

                    Log.i(TAG, "onClick: NotifyDataSetChange");
                    notifyDataSetChanged();
                }
            });

            articleHolder.mShareArticleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharingArticleUtil.shareArticle(mContext, bean.getTi(), bean.getAl());
                }
            });

            //Enabling sliding for view pager
            new UniversalTouchListener(articleHolder.mParentView, true);
        }
    }



    private void loadInlineAd(final FrameLayout frameLayout, String adId, final int position) {

        final Boolean isRequestSent = adsReqIndex.get(position);
        if(isRequestSent == null || !isRequestSent) {

            if (isFragmentVisible()) {
                adsReqIndex.put(position, true);
                final PublisherAdView mInlineAdView = new PublisherAdView(mContext);
                mInlineAdView.setAdUnitId(adId);
                mInlineAdView.setAdSizes(AdSize.MEDIUM_RECTANGLE);

                mInlineAdView.setAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        Log.i("Ads", "onAdLoaded");
                        frameLayout.addView(mInlineAdView);
                        frameLayout.setBackground(null);
                        mInlineAdView.setVisibility(View.VISIBLE);
                        adsReqIndex.put(position, false);
                        adsInlineMap.put(position, mInlineAdView);
                    }

                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        adsReqIndex.put(position, false);
                        Log.i("Ads", "onAdFailedToLoad section kk :- " + errorCode);

                        //frameLayout.setVisibility(View.GONE);
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

                mInlineAdView.loadAd(DFPConsent.getDefaultUrlAdsRequest(mContext));
            }
        }
        //Enabling sliding for view pager
//        new UniversalTouchListener(inlineAdViewHolder.mNativeAdContainer, true);
    }

    private void fillNativeAdData(final NativeAdViewHolder nativeAdViewHolder, String adId, final int position) {
        // If user is in Europe, and selected "Pay for the ad-free version" then ads should not display
        if(SharedPreferenceHelper.isUserPreferAdsFree(mContext)) {
            return;
        }
        Boolean isRequestSent = adsReqIndex.get(position);
        if(isRequestSent == null || !isRequestSent) {
            if (isFragmentVisible()) {
                adsReqIndex.put(position, true);
                AdLoader.Builder builder = new AdLoader.Builder(mContext, adId);
                final NativeContentAdView adView = nativeAdViewHolder.mNativeContentAdView;
                // For testing
//            AdLoader.Builder builder = new AdLoader.Builder(mContext, "/6499/example/native");
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

    public void setIsFragmentVisibleToUser(boolean isVisible) {
        this.isFragmentVisibleToUser = isVisible;
        if (isVisible) {
            Log.i(TAG, "setIsFragmentVisibleToUser: NotifyDataSetChange");
            notifyDataSetChanged();
        }
    }


    public void setAdViewNull() {

    }

    public void reInitInlineAds() {
        adsInlineMap.clear();
        adsNativeMap.clear();
    }

    private int adsInterval;

    public void setUpdatedListToHomeAdapter(ArrayList<ArticleDetail> mArticleList) {
        adsInterval = 5;

        final SectionAdapterItem loading = new SectionAdapterItem(null, SectionAdapterItem.LOADING);
        mSectionArticleList.remove(loading);

        this.mSectionArticleList = (FeatureConstant.getSectionRevisedData(mSectionArticleList.size(), mArticleList, mSubSection.size() > 0));

        if(FeatureConstant.NEW_ADS_INDEXING_REQUIRED) {
            if (NetworkUtils.isNetworkAvailable(mContext)) {

                boolean hasAllInlineAdsLoaded = false;

                boolean isInlineAd = true;

                boolean isFirstInline = true;
                boolean isSecondInline = false;
                boolean isThirdInline = false;
                boolean isForthInline = false;

                String firstInline = FeatureConstant.firstInline;
                String secondInline = FeatureConstant.secondInline;
                String thirdInline = FeatureConstant.thirdInline;
                String forthInline = FeatureConstant.forthInline;

                boolean isFirstNative = true;
                boolean isSecondNative = false;
                boolean isThirdNative = false;

                String firstNative = FeatureConstant.firstNative;
                String secondNative = FeatureConstant.secondNative;
                String thirdNative = FeatureConstant.thirdNative;

                for (int i = adsInterval; i < mSectionArticleList.size(); i += adsInterval) {
                    SectionAdapterItem adsItem;

                    String adId = "";
                    if(isInlineAd && !hasAllInlineAdsLoaded) {
                        if(isFirstInline) {
                            adId = firstInline;
                            isFirstInline = false;
                            isForthInline = false;
                            isSecondInline = true;
                        } else if(isSecondInline) {
                            adId = secondInline;
                            isFirstInline = false;
                            isSecondInline = false;
                            isForthInline = false;
                            isThirdInline = true;
                        } else if(isThirdInline) {
                            adId = thirdInline;
                            isFirstInline = false;
                            isSecondInline = false;
                            isThirdInline = false;
                            isForthInline = true;
                        } else if(isForthInline) {
                            adId = forthInline;
                            isFirstInline = true;
                            isSecondInline = false;
                            isThirdInline = false;
                            isForthInline = false;
                            hasAllInlineAdsLoaded = true;
                        }
                        adsItem = new SectionAdapterItem(adId, SectionAdapterItem.VIEW_INLINE_AD, true);

                        if (i == adsInterval) {
                            i = adsInterval - 1;
                        }

                    } else {

                        if(isFirstNative) {
                            adId = firstNative;
                            isFirstNative = false;
                            isSecondNative = true;
                            isThirdNative = false;
                        } else if(isSecondNative) {
                            adId = secondNative;
                            isFirstNative = false;
                            isSecondNative = false;
                            isThirdNative = true;
                        } else if(isThirdNative) {
                            adId = thirdNative;
                            isFirstNative = true;
                            isSecondNative = false;
                            isThirdNative = false;
                        }

                        adsItem = new SectionAdapterItem(adId, SectionAdapterItem.NATIVE_AD, true);
                        adsInterval = 9;
                    }

                    isInlineAd = !isInlineAd;
                    mSectionArticleList.add(i, adsItem);
                }
            }
        }
        notifyDataSetChanged();
    }

    public boolean isFragmentVisible() {
        return isFragmentVisibleToUser;
    }

    public class NativeAdViewHolder extends RecyclerView.ViewHolder {

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


    private class InlineAdViewHolder extends RecyclerView.ViewHolder {

        public FrameLayout frameLayout;

        public InlineAdViewHolder(View itemView) {
            super(itemView);
            frameLayout = (FrameLayout) itemView.findViewById(R.id.dfpFramLayout);
        }
    }


    private class SectionViewHolder extends RecyclerView.ViewHolder {

        private View mParentView;
        private ImageView mArticleImageView;
        private TextView mArticleTextView;
        private TextView mArticleUpdateTime;
        private TextView mArticleSectionName;
        private Button mBookmarkButton;
        private Button mShareArticleButton;
        private RelativeLayout mLinearLayout;
        private ImageButton mMultimediaButton;
        private FrameLayout mImageParentLayout;
        private TextView mAuthorTextView;

        public SectionViewHolder(View itemView) {
            super(itemView);
            mParentView = itemView;
            mArticleImageView = (ImageView) itemView.findViewById(R.id.imageview_article_list_image);
            mArticleTextView = (TextView) itemView.findViewById(R.id.textview_article_list_header);
            mAuthorTextView = (TextView) itemView.findViewById(R.id.author_textView);
            mShareArticleButton = (Button) itemView.findViewById(R.id.button_article_share);
            mArticleUpdateTime = (TextView) itemView.findViewById(R.id.textview_time);
            mBookmarkButton = (Button) itemView.findViewById(R.id.button_bookmark);
            mLinearLayout = (RelativeLayout) itemView.findViewById(R.id.layout_articles_root);
            mMultimediaButton = (ImageButton) itemView.findViewById(R.id.multimedia_button);
            mImageParentLayout = (FrameLayout) itemView.findViewById(R.id.imageParentLayout);
            mArticleSectionName = (TextView) itemView.findViewById(R.id.section_name);
        }
    }

    private class ExploreViewHolder extends RecyclerView.ViewHolder {

        private RecyclerView mExploreRecyclerView;

        public ExploreViewHolder(View itemView) {
            super(itemView);

            mExploreRecyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerview_explore);
        }
    }



    public class LoadingVH extends RecyclerView.ViewHolder {
        public LoadingVH(View itemView) {
            super(itemView);
        }
    }


    public void setLoadingPosition(boolean check) {
        SectionAdapterItem loadmoreItem = new SectionAdapterItem(null, SectionAdapterItem.LOADING);
        if (check) {
            mSectionArticleList.add(getItemCount(), loadmoreItem);
        } else {
            int indexOf = mSectionArticleList.indexOf(loadmoreItem);
            if(indexOf != -1) {
                mSectionArticleList.remove(loadmoreItem);
            }

        }
        notifyDataSetChanged();
    }

    public void staticItemLoad(String staticPageUrl, boolean isStaticPageEnable, int positionInListView) {
        // Below commented code is for Testing Purpose
        /*positionInListView = 2;
        isStaticPageEnable = true;
        // Home
//        staticPageUrl = "https://google.com"+"00#00"+"51";
        // Portfolio
//        staticPageUrl = "https://google.com"+"00#00"+"51";
        // BLink
        staticPageUrl = "https://google.com"+"00#00"+"51";*/
        if(isStaticPageEnable && staticPageUrl != null && positionInListView != -1 && isNetworkAvailable) {
            final SectionAdapterItem staticItem = new SectionAdapterItem(null, SectionAdapterItem.WEB_CONTAINT);
            staticItem.setStaticPageUrl(staticPageUrl);
            if(!mSectionArticleList.contains(staticItem)) {
                int size = mSectionArticleList.size();
                if(size > positionInListView) {
                    mSectionArticleList.add(positionInListView, staticItem);
                } else {
                    mSectionArticleList.add(staticItem);
                }
            }
        }
    }


}
