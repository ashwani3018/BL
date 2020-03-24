package com.mobstac.thehindubusinessline.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mobstac.thehindubusinessline.model.databasemodel.ArticleDetail;
import com.old.database.to.Article;

import java.util.List;

public class SearchSectionContentModel {

    /**
     * status : 1
     * s : 1
     * data : [{"aid":10397592,"sid":"","sname":"Agri Business","parentId":"","parentName":"","pd":"2019-09-13 16:02:53","od":"2019-03-12 16:02:52","gmt":"2019-09-13 10:32:53","opid":0,"ti":"Jet Airways to seek shareholders\u2019 nod to convert debt into equity 123 Chennai","au":" Sendhursamy Arumugan","al":"https://alphabl.businessline.in/economy/agri-business/delhi-declaration-lends-support-to-protect-public-sector-financial-institutions/article10397592.ece","le":"Delhi declaration lends support to protect public sector financial institutions Chennai","de":"<p>Importantly, the convention called out former NITI Aayog Vice-Chairman Arvind Panagariya\u2019s suggestion that all political parties commit themselves to the idea of privatisation. Panagariya had made a strong case for the privatisation of public sector banks in India with the exception of State Bank of India.<\/p><p> <a href=\"https://alphabl.businessline.in/news/rajya-sabha-passes-triple-talaq-bill/article11329249.ece\" id=\"_6a1f886f-c493-412c-be9e-90f20036efb7\">Rajya Sabha passes triple talaq bill<\/a> <\/p><p>Having quit NITI Aayog to resume a teaching career Columbia University, Panagariya had said that political parties serious of forming the government in 2019 should include the proposal in their manifesto.<\/p><p>But this is exactly what the Delhi declaration is seeking to target. It said emphatically that public sector FIs need to be strengthened in order to defend the financial freedom of the nation. This alone would ensure that the interest of common man is taken care of by protecting the economic sovereignty of the nation at large, the declaration said.<a href=\"https://www.thehindubusinessline.com/economy/logistics/terminal-illness-how-passengers-have-been-hit-by-flight-cancellations/article26561363.ece\">for more detailis<\/a><\/p><p>Importantly, the convention called out former NITI Aayog Vice-Chairman Arvind Panagariya\u2019s suggestion that all political parties commit themselves to the idea of privatisation. Panagariya had made a strong case for the privatisation of public sector banks in India with the exception of State Bank of India.<\/p><p>Having quit NITI Aayog to resume a teaching career Columbia University, Panagariya had said that political parties serious of forming the government in 2019 should include the proposal in their manifesto.<\/p><p>But this is exactly what the Delhi declaration is seeking to target. It said emphatically that public sector FIs need to be strengthened in order to defend the financial freedom of the nation. This alone would ensure that the interest of common man is taken care of by protecting the economic sovereignty of the nation at large, the declaration said.<\/p>","short_de":"","vid":"","hi":1,"me":[{"im":"https://alphabl.businessline.in/economy/budget/29ee0j/article10396493.ece/alternates/FREE_810/jaitley-budget-ap-2660022815025340jpg","ca":"&nbsp;"}],"im_thumbnail":"https://alphabl.businessline.in/economy/budget/29ee0j/article10396493.ece/BINARY/thumbnail/jaitley-budget-ap-2660022815025340jpg","audioLink":"","articleType":"story","add_pos":0,"youtube_video_id":"","location":"Delhi","sections":[],"rn":[]}]
     */

    private int status;
    private int s;

    @SerializedName(value = "data", alternate = "article")
    private List<ArticleDetail> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getS() {
        return s;
    }

    public void setS(int s) {
        this.s = s;
    }

    public List<ArticleDetail> getData() {
        return data;
    }

    public void setData(List<ArticleDetail> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * aid : 10397592
         * sid :
         * sname : Agri Business
         * parentId :
         * parentName :
         * pd : 2019-09-13 16:02:53
         * od : 2019-03-12 16:02:52
         * gmt : 2019-09-13 10:32:53
         * opid : 0
         * ti : Jet Airways to seek shareholders’ nod to convert debt into equity 123 Chennai
         * au :  Sendhursamy Arumugan
         * al : https://alphabl.businessline.in/economy/agri-business/delhi-declaration-lends-support-to-protect-public-sector-financial-institutions/article10397592.ece
         * le : Delhi declaration lends support to protect public sector financial institutions Chennai
         * de : <p>Importantly, the convention called out former NITI Aayog Vice-Chairman Arvind Panagariya’s suggestion that all political parties commit themselves to the idea of privatisation. Panagariya had made a strong case for the privatisation of public sector banks in India with the exception of State Bank of India.</p><p> <a href="https://alphabl.businessline.in/news/rajya-sabha-passes-triple-talaq-bill/article11329249.ece" id="_6a1f886f-c493-412c-be9e-90f20036efb7">Rajya Sabha passes triple talaq bill</a> </p><p>Having quit NITI Aayog to resume a teaching career Columbia University, Panagariya had said that political parties serious of forming the government in 2019 should include the proposal in their manifesto.</p><p>But this is exactly what the Delhi declaration is seeking to target. It said emphatically that public sector FIs need to be strengthened in order to defend the financial freedom of the nation. This alone would ensure that the interest of common man is taken care of by protecting the economic sovereignty of the nation at large, the declaration said.<a href="https://www.thehindubusinessline.com/economy/logistics/terminal-illness-how-passengers-have-been-hit-by-flight-cancellations/article26561363.ece">for more detailis</a></p><p>Importantly, the convention called out former NITI Aayog Vice-Chairman Arvind Panagariya’s suggestion that all political parties commit themselves to the idea of privatisation. Panagariya had made a strong case for the privatisation of public sector banks in India with the exception of State Bank of India.</p><p>Having quit NITI Aayog to resume a teaching career Columbia University, Panagariya had said that political parties serious of forming the government in 2019 should include the proposal in their manifesto.</p><p>But this is exactly what the Delhi declaration is seeking to target. It said emphatically that public sector FIs need to be strengthened in order to defend the financial freedom of the nation. This alone would ensure that the interest of common man is taken care of by protecting the economic sovereignty of the nation at large, the declaration said.</p>
         * short_de :
         * vid :
         * hi : 1
         * me : [{"im":"https://alphabl.businessline.in/economy/budget/29ee0j/article10396493.ece/alternates/FREE_810/jaitley-budget-ap-2660022815025340jpg","ca":"&nbsp;"}]
         * im_thumbnail : https://alphabl.businessline.in/economy/budget/29ee0j/article10396493.ece/BINARY/thumbnail/jaitley-budget-ap-2660022815025340jpg
         * audioLink :
         * articleType : story
         * add_pos : 0
         * youtube_video_id :
         * location : Delhi
         * sections : []
         * rn : []
         */

        private int aid;
        private String sid;
        private String sname;
        private String parentId;
        private String parentName;
        private String pd;
        private String od;
        private String gmt;
        private int opid;
        private String ti;
        private String au;
        private String al;
        private String le;
        private String de;
        private String short_de;
        private String vid;
        private int hi;
        private String im_thumbnail;
        private String audioLink;
        private String articleType;
        private int add_pos;
        private String youtube_video_id;
        private String location;
        private List<MeBean> me;
        private List<?> sections;
        private List<?> rn;

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

        public String getParentName() {
            return parentName;
        }

        public void setParentName(String parentName) {
            this.parentName = parentName;
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

        public String getGmt() {
            return gmt;
        }

        public void setGmt(String gmt) {
            this.gmt = gmt;
        }

        public int getOpid() {
            return opid;
        }

        public void setOpid(int opid) {
            this.opid = opid;
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

        public String getLe() {
            return le;
        }

        public void setLe(String le) {
            this.le = le;
        }

        public String getDe() {
            return de;
        }

        public void setDe(String de) {
            this.de = de;
        }

        public String getShort_de() {
            return short_de;
        }

        public void setShort_de(String short_de) {
            this.short_de = short_de;
        }

        public String getVid() {
            return vid;
        }

        public void setVid(String vid) {
            this.vid = vid;
        }

        public int getHi() {
            return hi;
        }

        public void setHi(int hi) {
            this.hi = hi;
        }

        public String getIm_thumbnail() {
            return im_thumbnail;
        }

        public void setIm_thumbnail(String im_thumbnail) {
            this.im_thumbnail = im_thumbnail;
        }

        public String getAudioLink() {
            return audioLink;
        }

        public void setAudioLink(String audioLink) {
            this.audioLink = audioLink;
        }

        public String getArticleType() {
            return articleType;
        }

        public void setArticleType(String articleType) {
            this.articleType = articleType;
        }

        public int getAdd_pos() {
            return add_pos;
        }

        public void setAdd_pos(int add_pos) {
            this.add_pos = add_pos;
        }

        public String getYoutube_video_id() {
            return youtube_video_id;
        }

        public void setYoutube_video_id(String youtube_video_id) {
            this.youtube_video_id = youtube_video_id;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public List<MeBean> getMe() {
            return me;
        }

        public void setMe(List<MeBean> me) {
            this.me = me;
        }

        public List<?> getSections() {
            return sections;
        }

        public void setSections(List<?> sections) {
            this.sections = sections;
        }

        public List<?> getRn() {
            return rn;
        }

        public void setRn(List<?> rn) {
            this.rn = rn;
        }

        public static class MeBean {
            /**
             * im : https://alphabl.businessline.in/economy/budget/29ee0j/article10396493.ece/alternates/FREE_810/jaitley-budget-ap-2660022815025340jpg
             * ca : &nbsp;
             */

            @SerializedName("im_v2")
            private String im;
            private String ca;

            public String getIm() {
                return im;
            }

            public void setIm(String im) {
                this.im = im;
            }

            public String getCa() {
                return ca;
            }

            public void setCa(String ca) {
                this.ca = ca;
            }
        }
    }
}
