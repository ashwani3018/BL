package com.mobstac.thehindubusinessline.model;

/**
 * Created by Vicky on 24/5/17.
 */

public class AddressData {

    private double latitude;
    private double longitude;

    public AddressData(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
