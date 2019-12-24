package com.superdata.im.entity;

/**
 * @version v1.0.0
 * @authon zjh
 * @date 2016-01-25
 * @desc 位置信息
 */
public class CxGeoMessage
{
    private double longitude;
    private double latitude;
    private String address;

    public CxGeoMessage(double longitude, double latitude, String address) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.address = address;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
