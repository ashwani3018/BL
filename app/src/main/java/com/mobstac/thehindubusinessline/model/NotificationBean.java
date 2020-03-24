package com.mobstac.thehindubusinessline.model;

import io.realm.RealmObject;
import io.realm.annotations.Required;

/**
 * Created by arvind on 8/10/16.
 */

public class NotificationBean extends RealmObject {

    private int articleId;
    private String articleUrl;
    private String title;
    private String description;
    private String imageUrl;
    private String sectionName;
    private String publishDate;
    private String notificationType;
    private boolean hasBody;
    private long insertionTime;
    private String parentId;
    private String sectionId;
    private boolean isRead;
    @Required
    private String articleType;

    public NotificationBean() {
    }

    public NotificationBean(int articleId, String articleUrl, String title, String description, String imageUrl, String sectionName, String publishDate, String notificationType, boolean hasBody, long insertionTime, String parentId, String sectionId
            , boolean isRead, String articleType) {
        this.articleId = articleId;
        this.articleUrl = articleUrl;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.sectionName = sectionName;
        this.publishDate = publishDate;
        this.notificationType = notificationType;
        this.hasBody = hasBody;
        this.insertionTime = insertionTime;
        this.parentId = parentId;
        this.sectionId = sectionId;
        this.isRead = isRead;
        this.articleType = articleType;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public String getArticleUrl() {
        return articleUrl;
    }

    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public boolean isHasBody() {
        return hasBody;
    }

    public void setHasBody(boolean hasBody) {
        this.hasBody = hasBody;
    }

    public long getInsertionTime() {
        return insertionTime;
    }

    public void setInsertionTime(long insertionTime) {
        this.insertionTime = insertionTime;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getArticleType() {
        return articleType;
    }

    public void setArticleType(String articleType) {
        this.articleType = articleType;
    }
}
