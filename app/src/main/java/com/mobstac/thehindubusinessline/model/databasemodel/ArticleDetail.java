package com.mobstac.thehindubusinessline.model.databasemodel;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;


/**
 * Created by root on 24/8/16.
 */
public class ArticleDetail implements Parcelable {
    private int aid;
    private String sid;
    private String sname;
    private String pd;
    private String ti;
    private String au;
    private String al;
    private String gmt;
    private String de;
    private String le;
    private String vid;
    private String youtube_video_id;
    private boolean is_rn;
    private int hi;
    private String parentId;
    private String parentName;
    private String audioLink;
    private long insertionTime;
    private int add_pos;
    private String im_thumbnail;
    private String im_thumbnail_v2;
    private String articleType;
    private String od;
    private String pid;
    private String bk;
    private int page;
    private String location;

    public String getLocation() {
        return (location == null ? "" : location);
    }

    public void setLocation(String location) {
        this.location = location;
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

    public boolean isIs_rn() {
        return is_rn;
    }

    public void setIs_rn(boolean is_rn) {
        this.is_rn = is_rn;
    }

    public int getHi() {
        return hi;
    }

    public void setHi(int hi) {
        this.hi = hi;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getAudioLink() {
        return audioLink;
    }

    public void setAudioLink(String audioLink) {
        this.audioLink = audioLink;
    }

    public long getInsertionTime() {
        return insertionTime;
    }

    public void setInsertionTime(long insertionTime) {
        this.insertionTime = insertionTime;
    }

    public int getAdd_pos() {
        return add_pos;
    }

    public void setAdd_pos(int add_pos) {
        this.add_pos = add_pos;
    }

    public String getIm_thumbnail() {
        return im_thumbnail;
    }

    public void setIm_thumbnail(String im_thumbnail) {
        this.im_thumbnail = im_thumbnail;
    }

    public String getIm_thumbnail_v2() {
        return im_thumbnail_v2;
    }

    public void setIm_thumbnail_v2(String im_thumbnail_v2) {
        this.im_thumbnail_v2 = im_thumbnail_v2;
    }

    public String getArticleType() {
        return articleType;
    }

    public void setArticleType(String articleType) {
        this.articleType = articleType;
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

    public String getBk() {
        return bk;
    }

    public void setBk(String bk) {
        this.bk = bk;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public ArrayList<ImageData> getMe() {
        return me;
    }

    public void setMe(ArrayList<ImageData> me) {
        this.me = me;
    }

    public ArrayList<ArticleDetail> getRn() {
        return rn;
    }

    public void setRn(ArrayList<ArticleDetail> rn) {
        this.rn = rn;
    }

    public ArrayList<SectionsSubsection> getSections() {
        return sections;
    }

    public void setSections(ArrayList<SectionsSubsection> sections) {
        this.sections = sections;
    }

    public static Creator<ArticleDetail> getCREATOR() {
        return CREATOR;
    }

    private ArrayList<ImageData> me;

    private ArrayList<ArticleDetail> rn;

    private ArrayList<SectionsSubsection> sections;

    public ArticleDetail(int aid, String sid, String sname, String pd, String ti, String au, String al, String gmt, String de, String le, String vid, String youtube_video_id, boolean is_rn, int hi, String parentId, String parentName, String audioLink, long insertionTime, int add_pos, String im_thumbnail, String articleType, ArrayList<ImageData> me, ArrayList<ArticleDetail> rn, ArrayList<SectionsSubsection> sections, String img_v2, String locations) {
        this.aid = aid;
        this.sid = sid;
        this.sname = sname;
        this.pd = pd;
        this.ti = ti;
        this.au = au;
        this.al = al;
        this.gmt = gmt;
        this.de = de;
        this.le = le;
        this.vid = vid;
        this.youtube_video_id = youtube_video_id;
        this.is_rn = is_rn;
        this.hi = hi;
        this.parentId = parentId;
        this.parentName = parentName;
        this.audioLink = audioLink;
        this.insertionTime = insertionTime;
        this.add_pos = add_pos;
        this.im_thumbnail = im_thumbnail;
        this.articleType = articleType;
        this.me = me;
        this.rn = rn;
        this.sections = sections;
        this.im_thumbnail_v2 = img_v2;
        this.location = locations;
    }

    protected ArticleDetail(Parcel in) {
        aid = in.readInt();
        sid = in.readString();
        sname = in.readString();
        pd = in.readString();
        ti = in.readString();
        au = in.readString();
        al = in.readString();
        gmt = in.readString();
        de = in.readString();
        le = in.readString();
        vid = in.readString();
        youtube_video_id = in.readString();
        is_rn = in.readByte() != 0;
        hi = in.readInt();
        parentId = in.readString();
        parentName = in.readString();
        audioLink = in.readString();
        insertionTime = in.readLong();
        add_pos = in.readInt();
        im_thumbnail = in.readString();
        im_thumbnail_v2 = in.readString();
        articleType = in.readString();
        od = in.readString();
        pid = in.readString();
        bk = in.readString();
        page = in.readInt();
        me = in.createTypedArrayList(ImageData.CREATOR);
        rn = in.createTypedArrayList(ArticleDetail.CREATOR);
        sections = in.createTypedArrayList(SectionsSubsection.CREATOR);
        location = in.readString();
    }

    public static final Creator<ArticleDetail> CREATOR = new Creator<ArticleDetail>() {
        @Override
        public ArticleDetail createFromParcel(Parcel in) {
            return new ArticleDetail(in);
        }

        @Override
        public ArticleDetail[] newArray(int size) {
            return new ArticleDetail[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(aid);
        dest.writeString(sid);
        dest.writeString(sname);
        dest.writeString(pd);
        dest.writeString(ti);
        dest.writeString(au);
        dest.writeString(al);
        dest.writeString(gmt);
        dest.writeString(de);
        dest.writeString(le);
        dest.writeString(vid);
        dest.writeString(youtube_video_id);
        dest.writeByte((byte) (is_rn ? 1 : 0));
        dest.writeInt(hi);
        dest.writeString(parentId);
        dest.writeString(parentName);
        dest.writeString(audioLink);
        dest.writeLong(insertionTime);
        dest.writeInt(add_pos);
        dest.writeString(im_thumbnail);
        dest.writeString(im_thumbnail_v2);
        dest.writeString(articleType);
        dest.writeString(od);
        dest.writeString(pid);
        dest.writeString(bk);
        dest.writeInt(page);
        dest.writeTypedList(me);
        dest.writeTypedList(rn);
        dest.writeTypedList(sections);
        dest.writeString(location);
    }
}
