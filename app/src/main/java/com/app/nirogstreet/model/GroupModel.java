package com.app.nirogstreet.model;

/**
 * Created by Preeti on 01-11-2017.
 */
public class GroupModel {
    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public boolean isJoinShow() {
        return joinShow;
    }

    public void setJoinShow(boolean joinShow) {
        this.joinShow = joinShow;
    }

    public boolean joinShow=false;
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

    public String getTotalMembers() {
        return totalMembers;
    }

    public void setTotalMembers(String totalMembers) {
        this.totalMembers = totalMembers;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public String getGroupBanner() {
        return groupBanner;
    }

    public void setGroupBanner(String groupBanner) {
        this.groupBanner = groupBanner;
    }

    public UserDetailModel getCreatedByUser() {
        return createdByUser;
    }

    public void setCreatedByUser(UserDetailModel createdByUser) {
        this.createdByUser = createdByUser;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    String groupId,groupName,groupDescription,totalMembers,privacy,groupBanner;
    UserDetailModel createdByUser;

    public String getStatusdata() {
        return statusdata;
    }

    public void setStatusdata(String statusdata) {
        this.statusdata = statusdata;
    }

    String statusdata;
    String created,updated,status,updatedBy;
    public GroupModel(String groupId, String created, String groupName, String groupDescription, String totalMembers, String privacy, String groupBanner, UserDetailModel createdByUser, String updated, String status, String updatedBy,boolean joinShow,String statusdata) {
        this.groupId = groupId;
        this.created = created;
        this.groupName = groupName;
        this.statusdata=statusdata;
        this.joinShow=joinShow;
        this.groupDescription = groupDescription;
        this.totalMembers = totalMembers;
        this.privacy = privacy;
        this.groupBanner = groupBanner;
        this.createdByUser = createdByUser;
        this.updated = updated;
        this.status = status;
        this.updatedBy = updatedBy;
    }


}
