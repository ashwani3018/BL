package com.mobstac.thehindubusinessline.retrofit;

import com.mobstac.thehindubusinessline.BuildConfig;

import java.util.List;

/**
 * Created by ashwani on 10/12/15.
 */
public class Body {

    private String id;
    private String type;
    private String device = "android";
    private String api_key = "hindu@9*M";
    private List<String> sec_id;
    private String bannerId;
    private long lut;
    private int app_version = BuildConfig.VERSION_CODE;
    private int os_version = android.os.Build.VERSION.SDK_INT;
    private int page;

    public Body(String id, String type, long lut) {
        this.id = id;
        this.type = type;
        this.lut = lut;
    }

    public Body(String id, String type, long lut, int page) {
        this.id = id;
        this.type = type;
        this.lut = lut;
        this.page = page;
    }


    public Body(List<String> SectionIDList, String bannerIdParam, long lut) {
        this.sec_id = SectionIDList;
        bannerId = bannerIdParam;
        this.lut = lut;
    }

    public Body(List<String> sectionId) {
        sec_id = sectionId;
    }

    public Body() {
    }
}
