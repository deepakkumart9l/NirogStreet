package com.app.nirogstreet.model;

/**
 * Created by Preeti on 30-12-2017.
 */

public class GroupNotificationModel {


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getComunityName() {
        return comunityName;
    }

    public void setComunityName(String comunityName) {
        this.comunityName = comunityName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserProfile() {
        return UserProfile;
    }

    public void setUserProfile(String userProfile) {
        UserProfile = userProfile;
    }
    String communityId;

    public GroupNotificationModel(String communityId, String comunityName, String userId, String userName, String userProfile) {
        this.communityId = communityId;
        this.comunityName = comunityName;
        this.userId = userId;
        this.userName = userName;
        UserProfile = userProfile;
    }

    String comunityName;
    String userId;
    String userName;
    String UserProfile;
}
