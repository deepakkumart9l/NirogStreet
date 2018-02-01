package com.app.nirogstreet.model;

/**
 * Created by Preeti on 19-01-2018.
 */

public class ShareWithModel {
    public String getId() {
        return id;
    }

    public String getShareType() {
        return shareType;
    }

    public void setShareType(String shareType) {
        this.shareType = shareType;
    }

    public String shareType;
    public void setId(String id) {
        this.id = id;
    }

    public String getFeed_id() {
        return feed_id;
    }

    public void setFeed_id(String feed_id) {
        this.feed_id = feed_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getShareMessage() {
        return shareMessage;
    }

    public void setShareMessage(String shareMessage) {
        this.shareMessage = shareMessage;
    }

    String id;
    String feed_id;
    String user_id;
    String user_name;
    String shareMessage;


    public ShareWithModel(String id, String feed_id, String user_id, String user_name, String shareMessage,String shareType) {
        this.id = id;
        this.feed_id = feed_id;
        this.user_id = user_id;
        this.user_name = user_name;
        this.shareType=shareType;
        this.shareMessage = shareMessage;
    }

}
