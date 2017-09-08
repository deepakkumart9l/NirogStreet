package com.app.nirogstreet.model;

/**
 * Created by Preeti on 08-09-2017.
 */

public class LocSearchModel {
    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    String pincode;
    public String getPlace_id() {
        return Place_id;
    }

    public void setPlace_id(String place_id) {
        Place_id = place_id;
    }

    private String Place_id;

    private String formattedtext;

    public String getAdminArea() {
        return adminArea;
    }

    public void setAdminArea(String adminArea) {
        this.adminArea = adminArea;
    }

    private String adminArea;
    public String getmLocality() {
        return mLocality;
    }

    public void setmLocality(String mLocality) {
        this.mLocality = mLocality;
    }

    private String mLocality;
    private String lat;
    private String lon;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    private String city;

    /**
     * @return the formattedtext
     */
    public String getFormattedtext() {
        return formattedtext;
    }

    /**
     * @param formattedtext
     *            the formattedtext to set
     */
    public void setFormattedtext(String formattedtext) {
        this.formattedtext = formattedtext;
    }

    /**
     * @return the lat
     */
    public String getLat() {
        return lat;
    }

    /**
     * @param lat
     *            the lat to set
     */
    public void setLat(String lat) {
        this.lat = lat;
    }

    /**
     * @return the lon
     */
    public String getLon() {
        return lon;
    }

    /**
     * @param lon
     *            the lon to set
     */
    public void setLon(String lon) {
        this.lon = lon;
    }

}
