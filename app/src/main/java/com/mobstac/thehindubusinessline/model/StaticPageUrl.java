package com.mobstac.thehindubusinessline.model;

public class StaticPageUrl {
    private String url;
    public boolean isEnabled;
    public int position;
    public String sectionId="";

    public String getUrlAndSectionId() {
        return url+"00#00"+sectionId;
    }
}
