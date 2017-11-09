package com.app.nirogstreet.model;

import java.io.Serializable;

/**
 * Created by Preeti on 08-11-2017.
 */
public class MultipleSelectedItemModel implements Serializable {
    String userId;
    String userName;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    boolean selected =false;
    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    String slug;

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    String profile_pic;
    public MultipleSelectedItemModel(String userId, String userName, String slug,String profile_pic) {
        this.userId = userId;
        this.userName = userName;
        this.profile_pic=profile_pic;
        this.slug = slug;
    }
}

