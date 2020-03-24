package com.mobstac.thehindubusinessline.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by arvind on 20/9/16.
 */
public class ImageGallaryUrl implements Parcelable {
    private String imageUrl;
    private String caption;
    private String title;

    public ImageGallaryUrl(String imageUrl, String caption, String title) {
        this.imageUrl = imageUrl;
        this.caption = caption;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imageUrl);
        dest.writeString(this.caption);
        dest.writeString(this.title);
    }

    protected ImageGallaryUrl(Parcel in) {
        this.imageUrl = in.readString();
        this.caption = in.readString();
        this.title = in.readString();
    }

    public static final Creator<ImageGallaryUrl> CREATOR = new Creator<ImageGallaryUrl>() {
        @Override
        public ImageGallaryUrl createFromParcel(Parcel source) {
            return new ImageGallaryUrl(source);
        }

        @Override
        public ImageGallaryUrl[] newArray(int size) {
            return new ImageGallaryUrl[size];
        }
    };
}
