package com.netoperation.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.netoperation.model.RecoBean;

@Entity(tableName = "BookmarkTable")
public class BookmarkTable {


    @PrimaryKey
    @NonNull
    private String aid ;

    private RecoBean bean;


    public BookmarkTable(String aid, RecoBean bean) {
        this.aid = aid;
        this.bean = bean;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public RecoBean getBean() {
        return bean;
    }

    public void setBean(RecoBean bean) {
        this.bean = bean;
    }
}
