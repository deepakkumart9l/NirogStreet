package com.app.nirogstreet.model;

/**
 * Created by Preeti on 25-08-2017.
 */

public class UserDetailModel {
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    String name;
    String email;
    String mobile;
    String gender;
    String experience;
    String profile_pic;
    String category;
    String dob;
    String webSite;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    String city;

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    String about;
    String title;

    public UserDetailModel(String name, String email, String mobile, String gender, String experience, String profile_pic, String category, String dob, String webSite,String about,String title,String city) {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.gender = gender;
        this.city=city;
        this.about=about;
        this.title=title;
        this.experience = experience;
        this.profile_pic = profile_pic;
        this.category = category;
        this.dob = dob;
        this.webSite = webSite;
    }


}
