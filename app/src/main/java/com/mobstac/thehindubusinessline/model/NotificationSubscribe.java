package com.mobstac.thehindubusinessline.model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Vicky on 5/10/16.
 */

public class NotificationSubscribe extends RealmObject {

    @PrimaryKey
    private int secID;
    private String secName;
    private int count;
    private Date LastUpdatedDate;

    public int getSecID() {
        return secID;
    }

    public void setSecID(int secID) {
        this.secID = secID;
    }

    public String getSecName() {
        return secName;
    }

    public void setSecName(String secName) {
        this.secName = secName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Date getLastUpdatedDate() {
        return LastUpdatedDate;
    }

    public void setLastUpdatedDate(Date lastUpdatedDate) {
        LastUpdatedDate = lastUpdatedDate;
    }

    public NotificationSubscribe(int secID, String secName, int count, Date lastUpdatedDate) {
        this.secID = secID;
        this.secName = secName;
        this.count = count;
        LastUpdatedDate = lastUpdatedDate;
    }

    public NotificationSubscribe() {

    }
}
