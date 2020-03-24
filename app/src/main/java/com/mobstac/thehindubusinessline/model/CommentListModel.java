package com.mobstac.thehindubusinessline.model;

import java.util.List;

/**
 * Created by ashwani on 26/12/15.
 */
public class CommentListModel {


    private int resource_id;
    private String article_id;
    private int comments;
    private String host;

    private List<CommentFeedEntity> comment_feed;

    public void setResource_id(int resource_id) {
        this.resource_id = resource_id;
    }

    public void setArticle_id(String article_id) {
        this.article_id = article_id;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setComment_feed(List<CommentFeedEntity> comment_feed) {
        this.comment_feed = comment_feed;
    }

    public int getResource_id() {
        return resource_id;
    }

    public String getArticle_id() {
        return article_id;
    }

    public int getComments() {
        return comments;
    }

    public String getHost() {
        return host;
    }

    public List<CommentFeedEntity> getComment_feed() {
        return comment_feed;
    }

    public static class CommentFeedEntity {
        private String comment;
        private String name;
        private String ts;
        private int up_votes;
        private int down_votes;
        private String comment_id;
        private String user_id;
        private int replies;
        private int user_points;
        private String avatar_url;
        private String parent_id;
        private Object email;

        public void setComment(String comment) {
            this.comment = comment;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setTs(String ts) {
            this.ts = ts;
        }

        public void setUp_votes(int up_votes) {
            this.up_votes = up_votes;
        }

        public void setDown_votes(int down_votes) {
            this.down_votes = down_votes;
        }

        public void setComment_id(String comment_id) {
            this.comment_id = comment_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public void setReplies(int replies) {
            this.replies = replies;
        }

        public void setUser_points(int user_points) {
            this.user_points = user_points;
        }

        public void setAvatar_url(String avatar_url) {
            this.avatar_url = avatar_url;
        }

        public void setParent_id(String parent_id) {
            this.parent_id = parent_id;
        }

        public void setEmail(Object email) {
            this.email = email;
        }

        public String getComment() {
            comment = comment.replaceAll("%2c", ",");
            comment = comment.replaceAll("%20", " ");
            return comment;
        }

        public String getName() {
            return name;
        }

        public String getTs() {
            return ts;
        }

        public int getUp_votes() {
            return up_votes;
        }

        public int getDown_votes() {
            return down_votes;
        }

        public String getComment_id() {
            return comment_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public int getReplies() {
            return replies;
        }

        public int getUser_points() {
            return user_points;
        }

        public String getAvatar_url() {
            return avatar_url;
        }

        public String getParent_id() {
            return parent_id;
        }

        public Object getEmail() {
            return email;
        }
    }
}
