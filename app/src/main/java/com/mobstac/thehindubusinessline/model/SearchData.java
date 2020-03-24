package com.mobstac.thehindubusinessline.model;

import java.util.List;

/**
 * Created by arvind on 26/9/16.
 */

public class SearchData {

    private int status;

    private List<DataBean> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public class DataBean {
        private int aid;
        private String sname;
        private String pd;
        private String ti;
        private String al;

        public int getAid() {
            return aid;
        }

        public void setAid(int aid) {
            this.aid = aid;
        }

        public String getSname() {
            return sname;
        }

        public void setSname(String sname) {
            this.sname = sname;
        }

        public String getPd() {
            return pd;
        }

        public void setPd(String pd) {
            this.pd = pd;
        }

        public String getTi() {
            return ti;
        }

        public void setTi(String ti) {
            this.ti = ti;
        }

        public String getAl() {
            return al;
        }

        public void setAl(String al) {
            this.al = al;
        }
    }
}
