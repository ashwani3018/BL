package com.mobstac.thehindubusinessline.model;


import java.util.List;

/**
 * Created by arvind on 16/1/17.
 */

public class NewsFeedPersonalizedData {
    private String sec_id;
    private List<ArticleBean> data;

    public String getSec_id() {
        return sec_id;
    }

    public List<ArticleBean> getData() {
        return data;
    }
}
