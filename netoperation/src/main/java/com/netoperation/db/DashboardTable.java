package com.netoperation.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.netoperation.model.RecoBean;

import java.util.List;

@Entity(tableName ="DashboardTable")
public class DashboardTable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ColumnInfo(name ="aid")
    private String aid ;

    @ColumnInfo(name ="bean")
    private RecoBean bean;

    @ColumnInfo(name ="recoFrom")
    private String recoFrom;


    public DashboardTable(String aid, String recoFrom, RecoBean bean) {
        this.aid = aid;
        this.bean = bean;
        this.recoFrom = recoFrom;
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

    public String getRecoFrom() {
        return recoFrom;
    }

    public void setRecoFrom(String recoFrom) {
        this.recoFrom = recoFrom;
    }


}
