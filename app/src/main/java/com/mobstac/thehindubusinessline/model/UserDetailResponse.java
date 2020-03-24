package com.mobstac.thehindubusinessline.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 9920 on 10/11/2017.
 */

public class UserDetailResponse implements Parcelable{

    private int id;
    private String name;
    private String login;
    private boolean active;
    private String gender;
    private String phone;
    private String preferences;
    private String error_description;
    private String error;

    protected UserDetailResponse(Parcel in) {
        id = in.readInt();
        name = in.readString();
        login = in.readString();
        active = in.readByte() != 0;
        gender = in.readString();
        phone = in.readString();
        preferences = in.readString();
        error_description = in.readString();
        error = in.readString();
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLogin() {
        return login;
    }

    public boolean isActive() {
        return active;
    }

    public String getGender() {
        return gender;
    }

    public String getPhone() {
        return phone;
    }

    public String getPreferences() {
        return preferences;
    }

    public String getError_description() {
        return error_description;
    }

    public String getError() {
        return error;
    }

    public static final Creator<UserDetailResponse> CREATOR = new Creator<UserDetailResponse>() {
        @Override
        public UserDetailResponse createFromParcel(Parcel in) {
            return new UserDetailResponse(in);
        }

        @Override
        public UserDetailResponse[] newArray(int size) {
            return new UserDetailResponse[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(login);
        dest.writeByte((byte) (active ? 1 : 0));
        dest.writeString(gender);
        dest.writeString(phone);
        dest.writeString(preferences);
        dest.writeString(error_description);
        dest.writeString(error);
    }
}
