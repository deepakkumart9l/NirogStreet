package com.app.nirogstreet.model;

import java.io.Serializable;

/**
 * Created by Preeti on 23-11-2017.
 */

public class UserList implements Serializable {
    String id;

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getTitle() {
        return title;
    }
String is_admin;

    public String getIs_admin() {
        return is_admin;
    }

    public void setIs_admin(String is_admin) {
        this.is_admin = is_admin;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    String user_type;
    String title;
    String profile_pic;
    public UserList(String id, String name,String profile_pic,String user_type,String title,String is_admin) {
        this.id = id;
        this.name = name;
        this.profile_pic=profile_pic;
        this.user_type=user_type;
        this.title=title;
        this.is_admin=is_admin;
    }

    String name;

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
}
