package com.mobstac.thehindubusinessline.model;

import java.util.List;

/**
 * Created by root on 24/8/16.
 */
public class NewsFeed {

    private NewsFeedBean newsFeed;

    public NewsFeedBean getNewsFeed() {
        return newsFeed;
    }

    public static class NewsFeedBean {

        private List<NewsFeedPersonalizedData> articles;

        private List<ArticleBean> banner;

        private List<Integer> personalizeID;

        public List<NewsFeedPersonalizedData> getArticles() {
            return articles;
        }

        public List<ArticleBean> getBanner() {
            return banner;
        }

        public List<Integer> getPersonalizeID() {
            return personalizeID;
        }

    }
}
