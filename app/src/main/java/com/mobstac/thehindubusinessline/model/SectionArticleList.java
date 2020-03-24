package com.mobstac.thehindubusinessline.model;

import com.mobstac.thehindubusinessline.model.databasemodel.ArticleDetail;

/**
 * Created by 9920 on 10/13/2017.
 */

public class SectionArticleList {

    private int viewType;
    private ArticleDetail articleDetail;
    private int position;


    public SectionArticleList(int viewType, ArticleDetail articleDetail, int position) {
        this.viewType = viewType;
        this.articleDetail = articleDetail;
        this.position = position;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public ArticleDetail getArticleDetail() {
        return articleDetail;
    }

    public void setArticleDetail(ArticleDetail articleDetail) {
        this.articleDetail = articleDetail;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
