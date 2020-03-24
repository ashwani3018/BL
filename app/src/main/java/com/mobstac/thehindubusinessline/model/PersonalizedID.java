package com.mobstac.thehindubusinessline.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by root on 2/9/16.
 */
public class PersonalizedID extends RealmObject {

    @PrimaryKey
    private int personalizeID;

    public int getPersonalizeID() {
        return personalizeID;
    }

    public void setPersonalizeID(int personalizeID) {
        this.personalizeID = personalizeID;
    }
}
