package com.mobstac.thehindubusinessline.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by root on 24/8/16.
 */
public class WidgetsTable extends RealmObject {
    @PrimaryKey
    private int secId;
    private int parentSecId;
    private int homePriority;
    private int overridePriority;
    private String secName;
    private String type;

    public WidgetsTable() {
    }

    public WidgetsTable(int secId, int parentSecId, int homePriority, int overridePriority, String secName, String type) {
        this.secId = secId;
        this.homePriority = homePriority;
        this.overridePriority = overridePriority;
        this.secName = secName;
        this.type = type;
        this.parentSecId = parentSecId;
    }


    public int getSecId() {
        return secId;
    }

    public void setSecId(int secId) {
        this.secId = secId;
    }

    public int getHomePriority() {
        return homePriority;
    }

    public void setHomePriority(int homePriority) {
        this.homePriority = homePriority;
    }

    public int getOverridePriority() {
        return overridePriority;
    }

    public void setOverridePriority(int overridePriority) {
        this.overridePriority = overridePriority;
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

    public int getParentSecId() {
        return parentSecId;
    }

    public void setParentSecId(int parentSecId) {
        this.parentSecId = parentSecId;
    }
}