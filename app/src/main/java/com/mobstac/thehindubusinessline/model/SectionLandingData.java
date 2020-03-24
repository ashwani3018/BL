package com.mobstac.thehindubusinessline.model;

import com.mobstac.thehindubusinessline.model.databasemodel.ArticleDetail;

/**
 * Created by 9920 on 7/18/2017.
 */

public class SectionLandingData {
    private int viewType;
    private int articleActualPosition;
    private ArticleDetail articleDetail;

    public SectionLandingData(int viewType, int articleActualPosition, ArticleDetail articleDetail) {
        this.viewType = viewType;
        this.articleActualPosition = articleActualPosition;
        this.articleDetail = articleDetail;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public int getArticleActualPosition() {
        return articleActualPosition;
    }

    public void setArticleActualPosition(int articleActualPosition) {
        this.articleActualPosition = articleActualPosition;
    }

    public ArticleDetail getArticleDetail() {
        return articleDetail;
    }

    public void setArticleDetail(ArticleDetail articleDetail) {
        this.articleDetail = articleDetail;
    }
}
