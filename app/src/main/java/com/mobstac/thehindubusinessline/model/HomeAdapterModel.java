package com.mobstac.thehindubusinessline.model;

import com.google.android.gms.ads.formats.NativeContentAd;
import com.mobstac.thehindubusinessline.adapter.WidgetAdapter;
import com.mobstac.thehindubusinessline.model.databasemodel.ArticleDetail;

/**
 * Created by arvind on 10/1/17.
 */

public class HomeAdapterModel {
    public String mAdvertismentId;
    private int viewType;
    private int articleActualPositionInList;
    private ArticleDetail mArticle;
    private String widgetName;
    private String widgetFooterName;
    private int widgetSectionId;
    private int widgetParentSecId;
    private String widgetSectionType;
    private WidgetAdapter mWidgetAdapter;
    private SensexData sensexData;
    private NativeContentAd mNativeContentAd;

    private String adId;
    private String adType;

    private String mStaticPageUrl;

    public HomeAdapterModel(int viewType, int articleActualPositionInList, ArticleDetail mArticle
            , String widgetName, String widgetFooterName, int widgetSectionId, int widgetParentSecId,
                            String widgetSectionType, WidgetAdapter mWidgetAdapter) {
        this.viewType = viewType;
        this.articleActualPositionInList = articleActualPositionInList;
        this.mArticle = mArticle;
        this.widgetName = widgetName;
        this.widgetFooterName = widgetFooterName;
        this.widgetSectionId = widgetSectionId;
        this.widgetParentSecId = widgetParentSecId;
        this.widgetSectionType = widgetSectionType;
        this.mWidgetAdapter = mWidgetAdapter;
    }

    public HomeAdapterModel(int viewType) {
        this.viewType = viewType;
    }

    public String getStaticPageUrl() {
        return mStaticPageUrl;
    }

    public void setStaticPageUrl(String mStaticPageUrl) {
        this.mStaticPageUrl = mStaticPageUrl;
    }

    public WidgetAdapter getmWidgetAdapter() {
        return mWidgetAdapter;
    }

    public int getViewType() {
        return viewType;
    }

    public int getArticleActualPositionInList() {
        return articleActualPositionInList;
    }

    public ArticleDetail getmArticle() {
        return mArticle;
    }

    public String getWidgetName() {
        return widgetName;
    }

    public String getWidgetFooterName() {
        return widgetFooterName;
    }

    public int getWidgetSectionId() {
        return widgetSectionId;
    }

    public int getWidgetParentSecId() {
        return widgetParentSecId;
    }

    public String getWidgetSectionType() {
        return widgetSectionType;
    }

    public SensexData getSensexData() {
        return sensexData;
    }

    public void setSensexData(SensexData sensexData) {
        this.sensexData = sensexData;
    }

    public NativeContentAd getmNativeContentAd() {
        return mNativeContentAd;
    }

    public void setmNativeContentAd(NativeContentAd mNativeContentAd) {
        this.mNativeContentAd = mNativeContentAd;
    }


    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getAdType() {
        return adType;
    }

    public void setAdType(String adType) {
        this.adType = adType;
    }
}
