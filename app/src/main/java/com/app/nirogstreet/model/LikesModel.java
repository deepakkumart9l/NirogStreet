package com.app.nirogstreet.model;

/**
 * Created by Preeti on 27-10-2017.
 */
public class LikesModel {
    public LikesModel(String commentId, String createdOn, String message, String userProfile_pic, String slug, String lname, String fname, String userId,String user_type,String title) {
        this.commentId = commentId;
        this.createdOn = createdOn;
        this.message = message;
        this.userProfile_pic = userProfile_pic;
        this.slug = slug;
        this.lname = lname;
        this.fname = fname;
        this.userId = userId;
        this.user_type=user_type;
        this.title=title;
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

    public void setTitle(String title) {
        this.title = title;
    }

    String user_type;
    String title;

    String commentId = null;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getUserProfile_pic() {
        return userProfile_pic;
    }

    public void setUserProfile_pic(String userProfile_pic) {
        this.userProfile_pic = userProfile_pic;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    String userId = null;
    String fname = null;
    String lname = null;
    String slug = null;
    String userProfile_pic = null;
    String message = null;
    String createdOn = null;


}

