package com.mobstac.thehindubusinessline.model.databasemodel;

import android.os.Parcelable;


/**
 * Created by arvind on 21/12/16.
 */
public class SectionsSubsection implements Parcelable {
    private int section_id;
    private String section_name;

    public SectionsSubsection(int section_id, String section_name) {
        this.section_id = section_id;
        this.section_name = section_name;
    }

    public int getSection_id() {
        return section_id;
    }

    public void setSection_id(int section_id) {
        this.section_id = section_id;
    }

    public String getSection_name() {
        return section_name;
    }

    public void setSection_name(String section_name) {
        this.section_name = section_name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(this.section_id);
        dest.writeString(this.section_name);
    }

    protected SectionsSubsection(android.os.Parcel in) {
        this.section_id = in.readInt();
        this.section_name = in.readString();
    }

    public static final Parcelable.Creator<SectionsSubsection> CREATOR = new Parcelable.Creator<SectionsSubsection>() {
        @Override
        public SectionsSubsection createFromParcel(android.os.Parcel source) {
            return new SectionsSubsection(source);
        }

        @Override
        public SectionsSubsection[] newArray(int size) {
            return new SectionsSubsection[size];
        }
    };
}
