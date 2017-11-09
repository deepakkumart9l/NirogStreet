package com.app.nirogstreet.model;

/**
 * Created by Preeti on 28-10-2017.
 */
public class SearchModel { String fname;
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String lname;

    public String getDeprtment() {
        return deprtment;
    }

    public void setDeprtment(String deprtment) {
        this.deprtment = deprtment;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }

    String deprtment;
    String profileimage;

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    String slug;

    public SearchModel(String fname, String slug, String lname,String deprtment,String profileimage1,String id) {
        this.fname = fname;
        this.slug = slug;
        this.deprtment=deprtment;
        this.id=id;
        this.profileimage=profileimage1;
        this.lname = lname;
    }
}
