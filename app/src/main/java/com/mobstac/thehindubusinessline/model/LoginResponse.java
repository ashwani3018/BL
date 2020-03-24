package com.mobstac.thehindubusinessline.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 9920 on 10/9/2017.
 */

public class LoginResponse implements Parcelable {
    private String access_token;
    private String token_type;
    private String refresh_token;
    private long expires_in;
    private String scope;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public long getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(long expires_in) {
        this.expires_in = expires_in;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.access_token);
        dest.writeString(this.token_type);
        dest.writeString(this.refresh_token);
        dest.writeLong(this.expires_in);
        dest.writeString(this.scope);
    }

    public LoginResponse() {
    }

    protected LoginResponse(Parcel in) {
        this.access_token = in.readString();
        this.token_type = in.readString();
        this.refresh_token = in.readString();
        this.expires_in = in.readLong();
        this.scope = in.readString();
    }

    public static final Parcelable.Creator<LoginResponse> CREATOR = new Parcelable.Creator<LoginResponse>() {
        @Override
        public LoginResponse createFromParcel(Parcel source) {
            return new LoginResponse(source);
        }

        @Override
        public LoginResponse[] newArray(int size) {
            return new LoginResponse[size];
        }
    };
}
