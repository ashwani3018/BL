package com.mobstac.thehindubusinessline.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Required;

/**
 * Created by root on 24/8/16.
 * Updated by root on 12/2/19.
 */
public class ArticleBean extends RealmObject {
    private int aid;
    private String sid;
    private String sname;
    private String pd;
    private String od;
    private String pid;
    private int opid;
    private String ti;
    private String au;
    private String al;
    private String bk;
    private String gmt;
    private String de;
    private String le;
    private String vid;
    private String youtube_video_id;
    private boolean is_rn;
    private int hi;
    private String parentId;
    private String parentName;
    private String comm_count;
    private String audioLink;
    private boolean isHome;
    private boolean isBanner;
    private long insertionTime;
    private int add_pos;
    private boolean is_photo;
    private boolean is_audio;
    private boolean is_video;
    private String short_de;
    private String im_thumbnail;
    private String im_thumbnail_v2;
    private String articleType;
    private int page;
    private String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    private RealmList<ImageBean> me;

    private RealmList<ArticleBean> rn;

    private RealmList<SectionsContainingArticleBean> sections;

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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public long getInsertionTime() {
        return insertionTime;
    }

    public void setInsertionTime(long insertionTime) {
        this.insertionTime = insertionTime;
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

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getYoutube_video_id() {
        return youtube_video_id;
    }

    public void setYoutube_video_id(String youtube_video_id) {
        this.youtube_video_id = youtube_video_id;
    }

    public int getHi() {
        return hi;
    }

    public void setHi(int hi) {
        this.hi = hi;
    }

    public String getComm_count() {
        return comm_count;
    }

    public void setComm_count(String comm_count) {
        this.comm_count = comm_count;
    }

    public String getAudioLink() {
        return audioLink;
    }

    public RealmList<ImageBean> getMe() {
        return me;
    }

    public void setMe(RealmList<ImageBean> me) {
        this.me = me;
    }

    public RealmList<ArticleBean> getRn() {
        return rn;
    }

    public void setRn(RealmList<ArticleBean> rn) {
        this.rn = rn;
    }

    public RealmList<SectionsContainingArticleBean> getSections() {
        return sections;
    }

    public void setSections(RealmList<SectionsContainingArticleBean> sections) {
        this.sections = sections;
    }

    public boolean isHome() {
        return isHome;
    }

    public void setHome(boolean home) {
        isHome = home;
    }


    public boolean isBanner() {
        return isBanner;
    }

    public void setBanner(boolean banner) {
        isBanner = banner;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public boolean getIs_rn() {
        return is_rn;
    }

    public void setIs_rn(boolean is_rn) {
        this.is_rn = is_rn;
    }

    public int getAdd_pos() {
        return add_pos;
    }

    public void setAdd_pos(int add_pos) {
        this.add_pos = add_pos;
    }


    public boolean is_photo() {
        return is_photo;
    }

    public boolean is_audio() {
        return is_audio;
    }

    public boolean is_video() {
        return is_video;
    }

    public String getShort_de() {
        return short_de;
    }

    public String getIm_thumbnail() {
        return im_thumbnail;
    }

    public String getArticleType() {
        return articleType;
    }

    public int getOpid() {
        return opid;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getIm_thumbnail_v2() {
        return im_thumbnail_v2;
    }

    public void setIm_thumbnail_v2(String im_thumbnail_v2) {
        this.im_thumbnail_v2 = im_thumbnail_v2;
    }
}
