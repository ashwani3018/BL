package com.mobstac.thehindubusinessline.model;

import com.mobstac.thehindubusinessline.model.databasemodel.ArticleDetail;

import java.util.List;

/**
 * Created by arvind on 6/9/16.
 */
public class SearchArticleById {

    private int status;
    private String da;
    private String yd;
    private int s;
    private String sid;
    private String sname;


    private List<ArticleDetail> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDa() {
        return da;
    }

    public void setDa(String da) {
        this.da = da;
    }

    public String getYd() {
        return yd;
    }

    public void setYd(String yd) {
        this.yd = yd;
    }

    public int getS() {
        return s;
    }

    public void setS(int s) {
        this.s = s;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public List<ArticleDetail> getData() {
        return data;
    }

    public void setData(List<ArticleDetail> data) {
        this.data = data;
    }


}
