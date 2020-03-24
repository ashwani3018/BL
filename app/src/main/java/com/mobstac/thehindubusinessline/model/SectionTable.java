package com.mobstac.thehindubusinessline.model;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by arvind on 1/8/16.
 */

public class SectionTable extends RealmObject {
    @PrimaryKey
    private int secId;
    private String secName;
    private String type;
    private int parentId;
    private int priority;
    private int overridePriority;
    private boolean show_on_burger;
    private boolean show_on_explore;
    private String image;
    private String image_v2;
    private String link;
    private int customScreen;
    private int customScreenPri;
    private String webLink;
    private String from;

    private String staticPageUrl;
    private boolean isStaticPageEnable;
    private int positionInListView;
    private int staticPageSid;

    public int getStaticPageSid() {
        return staticPageSid;
    }

    public void setStaticPageSid(int staticPageSid) {
        this.staticPageSid = staticPageSid;
    }

    private RealmList<SectionTable> subSectionList;

    public SectionTable(int secId, String secName, String type, int parentId, int priority,
                        int overridePriority, boolean show_on_burger, boolean show_on_explore,
                        String image, RealmList<SectionTable> subSectionList, String link,
                        int customScreen, int customScreenPri, String webLink, String from,
                        String staticPageUrl, boolean isStaticPageEnable, int positionInListView, String image_v2) {
        this.secId = secId;
        this.secName = secName;
        this.type = type;
        this.parentId = parentId;
        this.priority = priority;
        this.overridePriority = overridePriority;
        this.show_on_burger = show_on_burger;
        this.show_on_explore = show_on_explore;
        this.image = image;
        this.subSectionList = subSectionList;
        this.link = link;
        this.customScreen = customScreen;
        this.customScreenPri = customScreenPri;
        this.webLink = webLink;
        this.from = from;
        this.staticPageUrl = staticPageUrl;
        this.isStaticPageEnable = isStaticPageEnable;
        this.positionInListView = positionInListView;
        this.image_v2 = image_v2;
    }

    public SectionTable() {
    }

    public String getStaticPageUrl() {
        return staticPageUrl;
    }

    public void setStaticPageUrl(String staticPageUrl) {
        this.staticPageUrl = staticPageUrl;
    }

    public boolean isStaticPageEnable() {
        return isStaticPageEnable;
    }

    public void setStaticPageEnable(boolean staticPageEnable) {
        this.isStaticPageEnable = staticPageEnable;
    }

    public int getPositionInListView() {
        return positionInListView;
    }

    public void setPositionInListView(int positionInListView) {
        this.positionInListView = positionInListView;
    }

    public List<SectionTable> getSubSectionList() {
        return subSectionList;
    }

    public void setSubSectionList(RealmList<SectionTable> subSectionList) {
        this.subSectionList = subSectionList;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getSecId() {
        return secId;
    }

    public void setSecId(int secId) {
        this.secId = secId;
    }

    public String getSecName() {
        return secName;
    }

    public void setSecName(String secName) {
        this.secName = secName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }


    public int getOverridePriority() {
        return overridePriority;
    }

    public void setOverridePriority(int overridePriority) {
        this.overridePriority = overridePriority;
    }

    public boolean isShow_on_burger() {
        return show_on_burger;
    }

    public void setShow_on_burger(boolean show_on_burger) {
        this.show_on_burger = show_on_burger;
    }

    public boolean isShow_on_explore() {
        return show_on_explore;
    }

    public void setShow_on_explore(boolean show_on_explore) {
        this.show_on_explore = show_on_explore;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getCustomScreen() {
        return customScreen;
    }

    public void setCustomScreen(int customScreen) {
        this.customScreen = customScreen;
    }

    public String getWebLink() {
        return webLink;
    }

    public void setWebLink(String webLink) {
        this.webLink = webLink;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getImage_v2() {
        return image_v2;
    }

    public void setImage_v2(String image_v2) {
        this.image_v2 = image_v2;
    }
}
