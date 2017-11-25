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

    String profile_pic;
    public UserList(String id, String name,String profile_pic) {
        this.id = id;
        this.name = name;
        this.profile_pic=profile_pic;
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
