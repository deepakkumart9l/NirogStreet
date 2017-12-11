package com.app.nirogstreet.model;

/**
 * Created by Preeti on 11-12-2017.
 */

public class CoursesModel {
    public CoursesModel(String id, String courses_name, String courses_description, String time, String imageUrl, UserDetailModel userDetailModel) {
        this.id = id;
        this.courses_name = courses_name;
        this.courses_description = courses_description;
        this.time = time;
        this.imageUrl = imageUrl;
        this.userDetailModel = userDetailModel;
    }

    String id, courses_name, courses_description, time, imageUrl;
    UserDetailModel userDetailModel;

    public UserDetailModel getUserDetailModel() {
        return userDetailModel;
    }

    public void setUserDetailModel(UserDetailModel userDetailModel) {
        this.userDetailModel = userDetailModel;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCourses_description() {
        return courses_description;
    }

    public void setCourses_description(String courses_description) {
        this.courses_description = courses_description;
    }

    public String getCourses_name() {
        return courses_name;
    }

    public void setCourses_name(String courses_name) {
        this.courses_name = courses_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
