package com.mobstac.thehindubusinessline.model;


import io.realm.RealmObject;

/**
 * Created by arvind on 21/12/16.
 */
public class SectionsContainingArticleBean extends RealmObject {
    private int section_id;
    private String section_name;

    public int getSection_id() {
        return section_id;
    }

    public void setSection_id(int section_id) {
        this.section_id = section_id;
    }

    public String getSection_name() {
        return section_name;
    }

    public void setSection_name(String section_name) {
        this.section_name = section_name;
    }
}
