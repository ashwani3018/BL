package com.mobstac.thehindubusinessline.model;


import com.mobstac.thehindubusinessline.model.databasemodel.ArticleDetail;

/**
 * Created by ashwanisingh on 22/12/17.
 */

public class SectionAdapterItem {

    public static final int CARTOON = 1;
    public static final int ARTICLE = 2;
    public static final int EXPLORE = 3;
    public static final int NATIVE_AD = 4;
    public static final int VIEW_INLINE_AD = 5;
    public static final int LOADING = 6;
    public static final int WEB_CONTAINT = 7;

    private ArticleDetail mArticleDetail;
    private int mViewType;
    private int articleActualPos = -1;
    private String adId;

    private boolean isInlineAdLoading = false;

    private String mStaticPageUrl;

    public SectionAdapterItem(ArticleDetail articleDetail, int viewType) {
        this.mArticleDetail = articleDetail;
        this.mViewType = viewType;
    }

    public SectionAdapterItem(String adId, int viewType, boolean isAdEnable) {
        this.adId = adId;
        this.mViewType = viewType;
    }

    public ArticleDetail getArticleDetail() {
        return mArticleDetail;
    }

    public int getViewType() {
        return mViewType;
    }

    public int getArticleActualPos() {
        return articleActualPos;
    }

    public void setArticleActualPos(int articleActualPos) {
        this.articleActualPos = articleActualPos;
    }

    public String getAdId() {
        return adId;
    }

    public boolean isInlineAdLoading() {
        return isInlineAdLoading;
    }

    public void setInlineAdLoading(boolean inlineAdLoading) {
        isInlineAdLoading = inlineAdLoading;
    }

    public String getStaticPageUrl() {
        return mStaticPageUrl;
    }

    public void setStaticPageUrl(String mStaticPageUrl) {
        this.mStaticPageUrl = mStaticPageUrl;
    }

    @Override
    public boolean equals(Object obj) {
        super.equals(obj);
        SectionAdapterItem item = (SectionAdapterItem) obj;
        return item.getViewType() == getViewType();
    }

    @Override
    public int hashCode() {
        super.hashCode();
        return (""+getViewType()).hashCode();
    }
}
