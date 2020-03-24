package com.mobstac.thehindubusinessline.model;

/**
 * Created by Vicky on 24/5/17.
 */

public class SectionBasicData {

    private int secId;
    private String sectionName;

    public SectionBasicData(int secId, String sectionName) {
        this.secId = secId;
        this.sectionName = sectionName;
    }

    public int getSecId() {
        return secId;
    }

    public String getSectionName() {
        return sectionName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof SectionBasicData)) return false;
        SectionBasicData otherStudent = (SectionBasicData) obj;
        return otherStudent.secId == (this.secId);
    }
}
