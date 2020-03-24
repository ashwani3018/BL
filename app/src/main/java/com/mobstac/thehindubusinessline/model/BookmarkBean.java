package com.mobstac.thehindubusinessline.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by root on 24/8/16.
 */
public class BookmarkBean extends RealmObject {
    @PrimaryKey
    private int aid;
    private String sid;
    private String sname;
    private String pd;
    private String od;
    private String pid;
    private String ti;
    private String au;
    private String al;
    private String bk;
    private String gmt;
    private String de;
    private String le;
    private int hi;
    private long bookmarkDate;
    private boolean isRead;
    private int add_pos;
    private String multimediaPath;
    private String articleType;
    private String vid;
    private String parentId;
    private String location;

    private RealmList<ImageBean> me;

    public BookmarkBean() {
    }

    public BookmarkBean(int aid, String sid, String sname, String pd, String od, String pid, String ti, String au, String al, String bk, String gmt, String de, String le, int hi, RealmList<ImageBean> me, long bookmarkDate, boolean isRead, int add_pos
            , String multimediaPath, String articleType, String vid, String parentId, String locations) {
        this.aid = aid;
        this.sid = sid;
        this.sname = sname;
        this.pd = pd;
        this.od = od;
        this.pid = pid;
        this.ti = ti;
        this.au = au;
        this.al = al;
        this.bk = bk;
        this.gmt = gmt;
        this.de = de;
        this.le = le;
        this.hi = hi;
        this.me = me;
        this.bookmarkDate = bookmarkDate;
        this.isRead = isRead;
        this.add_pos = add_pos;
        this.multimediaPath = multimediaPath;
        this.articleType = articleType;
        this.vid = vid;
        this.parentId = parentId;
        this.location = locations;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public long getBookmarkDate() {
        return bookmarkDate;
    }

    public void setBookmarkDate(long bookmarkDate) {
        this.bookmarkDate = bookmarkDate;
    }

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
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


    public String getPd() {
        return pd;
    }

    public void setPd(String pd) {
        this.pd = pd;
    }

    public String getOd() {
        return od;
    }

    public void setOd(String od) {
        this.od = od;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getTi() {
        return ti;
    }

    public void setTi(String ti) {
        this.ti = ti;
    }

    public String getAu() {
        return au;
    }

    public void setAu(String au) {
        this.au = au;
    }

    public String getAl() {
        return al;
    }

    public void setAl(String al) {
        this.al = al;
    }

    public String getBk() {
        return bk;
    }

    public void setBk(String bk) {
        this.bk = bk;
    }

    public String getGmt() {
        return gmt;
    }

    public void setGmt(String gmt) {
        this.gmt = gmt;
    }

    public String getDe() {
        return de;
    }

    public void setDe(String de) {
        this.de = de;
    }

    public String getLe() {
        return le;
    }

    public void setLe(String le) {
        this.le = le;
    }

    public int getHi() {
        return hi;
    }

    public void setHi(int hi) {
        this.hi = hi;
    }

    public int getAdd_pos() {
        return add_pos;
    }

    public void setAdd_pos(int add_pos) {
        this.add_pos = add_pos;
    }

    public String getMultimediaPath() {
        return multimediaPath;
    }

    public void setMultimediaPath(String multimediaPath) {
        this.multimediaPath = multimediaPath;
    }

    public String getArticleType() {
        return articleType;
    }

    public void setArticleType(String articleType) {
        this.articleType = articleType;
    }

    public RealmList<ImageBean> getMe() {
        return me;
    }

    public void setMe(RealmList<ImageBean> me) {
        this.me = me;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getLocation() {
        if(location == null) {
            return "";
        }
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
