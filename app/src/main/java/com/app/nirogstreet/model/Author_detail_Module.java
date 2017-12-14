package com.app.nirogstreet.model;

import java.io.Serializable;

/**
 * Created by Preeti on 12-12-2017.
 */

public class Author_detail_Module implements Serializable{


    public Author_detail_Module(String id, String name, String email, String mobile, String alternateMobile, String gender, String experience, String profile_pic, String category, String dob, String website) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.alternateMobile = alternateMobile;
        this.gender = gender;
        this.experience = experience;
        this.profile_pic = profile_pic;
        this.category = category;
        this.dob = dob;
        this.website = website;
    }

    String email;
    String mobile;
    String alternateMobile;
    String gender;
    String experience;
    String profile_pic;
    String category;
    String dob;
    String website;
    String id;
    String name;
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAlternateMobile() {
        return alternateMobile;
    }

    public void setAlternateMobile(String alternateMobile) {
        this.alternateMobile = alternateMobile;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }


}
