package com.mobstac.thehindubusinessline.model.databasemodel;

import android.os.Parcelable;


/**
 * Created by root on 24/8/16.
 */

public class ImageData implements Parcelable {
    private String im;
    private String im_v2;

    public String getIm_v2() {
        return im_v2;
    }

    public void setIm_v2(String im_v2) {
        this.im_v2 = im_v2;
    }

    private String ca;

    public ImageData(String im, String im_v2, String ca) {
        this.im = im;
        this.im_v2 = im_v2;
        this.ca = ca;
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeString(this.im);
        dest.writeString(this.ca);
    }

    protected ImageData(android.os.Parcel in) {
        this.im = in.readString();
        this.im_v2 = in.readString();
        this.ca = in.readString();
    }

    public static final Parcelable.Creator<ImageData> CREATOR = new Parcelable.Creator<ImageData>() {
        @Override
        public ImageData createFromParcel(android.os.Parcel source) {
            return new ImageData(source);
        }

        @Override
        public ImageData[] newArray(int size) {
            return new ImageData[size];
        }
    };
}
