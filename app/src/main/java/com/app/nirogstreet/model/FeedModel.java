package com.app.nirogstreet.model;

import java.util.ArrayList;

/**
 * Created by Preeti on 26-10-2017.
 */

public class FeedModel {
    String feed_id;
public FeedModel()
{

}
    public String getDoc_name() {
        return doc_name;
    }

    public void setDoc_name(String doc_name) {
        this.doc_name = doc_name;
    }

    String doc_name;
    public String getTotal_likes() {
        return total_likes;
    }

    public void setTotal_likes(String total_likes) {
        this.total_likes = total_likes;
    }
    String total_likes;

    String feed_source;

    String total_comments;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotal_comments() {
        return total_comments;
    }

    public void setTotal_comments(String total_comments) {
        this.total_comments = total_comments;
    }

    public String getFeed_source() {
        return feed_source;
    }

    public void setFeed_source(String feed_source) {
        this.feed_source = feed_source;
    }

    String status;
    String doc_Type;

    public String getDoc_Type() {
        return doc_Type;
    }

    public void setDoc_Type(String doc_Type) {
        this.doc_Type = doc_Type;
    }

    public String getCommunity_name() {
        return community_name;
    }

    public void setCommunity_name(String community_name) {
        this.community_name = community_name;
    }

    public String community_name;
    public FeedModel(String feed_id, UserDetailModel userDetailModel_creator, String community_Id, UserDetailModel parentFeedDetail, String parent_feed, String feed_type, String post_Type, String titleQuestion, String message, String link_type, String url_title, String url_description, String url_image, ArrayList<String> feedSourceArrayList, String enable_comment, String created, String updated, int user_has_liked, UserDetailModel createdBy, String feed_source, String total_comments, String total_likes, String status, String doc_name,String doc_Type,String community_name) {
        this.feed_id = feed_id;
        this.userDetailModel_creator = userDetailModel_creator;
        this.community_Id = community_Id;
        this.parentFeedDetail = parentFeedDetail;
        this.community_name=community_name;
        this.parent_feed = parent_feed;
        this.feed_type = feed_type;
        this.doc_Type=doc_Type;
        this.feed_source = feed_source;
        this.total_comments = total_comments;
        this.total_likes = total_likes;
        this.status = status;
        this.doc_name=doc_name;
        this.post_Type = post_Type;
        this.titleQuestion = titleQuestion;
        this.message = message;
        this.link_type = link_type;
        this.url_title = url_title;
        this.url_description = url_description;
        this.url_image = url_image;
        this.feedSourceArrayList = feedSourceArrayList;
        this.enable_comment = enable_comment;
        this.created = created;
        this.updated = updated;
        this.user_has_liked = user_has_liked;
        this.createdBy = createdBy;
    }

    UserDetailModel userDetailModel_creator;
    String community_Id;
    UserDetailModel parentFeedDetail;
    String parent_feed;
    String feed_type;
    String post_Type;
    String titleQuestion;
    String message;
    String link_type, url_title, url_description, url_image;

    ArrayList<String> feedSourceArrayList;

    public String getEnable_comment() {
        return enable_comment;
    }

    public void setEnable_comment(String enable_comment) {
        this.enable_comment = enable_comment;
    }

    public String getFeed_id() {
        return feed_id;
    }

    public void setFeed_id(String feed_id) {
        this.feed_id = feed_id;
    }

    public UserDetailModel getUserDetailModel_creator() {
        return userDetailModel_creator;
    }

    public void setUserDetailModel_creator(UserDetailModel userDetailModel_creator) {
        this.userDetailModel_creator = userDetailModel_creator;
    }

    public String getCommunity_Id() {
        return community_Id;
    }

    public void setCommunity_Id(String community_Id) {
        this.community_Id = community_Id;
    }

    public UserDetailModel getParentFeedDetail() {
        return parentFeedDetail;
    }

    public void setParentFeedDetail(UserDetailModel parentFeedDetail) {
        this.parentFeedDetail = parentFeedDetail;
    }

    public String getParent_feed() {
        return parent_feed;
    }

    public void setParent_feed(String parent_feed) {
        this.parent_feed = parent_feed;
    }

    public String getFeed_type() {
        return feed_type;
    }

    public void setFeed_type(String feed_type) {
        this.feed_type = feed_type;
    }

    public String getPost_Type() {
        return post_Type;
    }

    public void setPost_Type(String post_Type) {
        this.post_Type = post_Type;
    }

    public String getTitleQuestion() {
        return titleQuestion;
    }

    public void setTitleQuestion(String titleQuestion) {
        this.titleQuestion = titleQuestion;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLink_type() {
        return link_type;
    }

    public void setLink_type(String link_type) {
        this.link_type = link_type;
    }

    public String getUrl_title() {
        return url_title;
    }

    public void setUrl_title(String url_title) {
        this.url_title = url_title;
    }

    public String getUrl_description() {
        return url_description;
    }

    public void setUrl_description(String url_description) {
        this.url_description = url_description;
    }

    public String getUrl_image() {
        return url_image;
    }

    public void setUrl_image(String url_image) {
        this.url_image = url_image;
    }

    public ArrayList<String> getFeedSourceArrayList() {
        return feedSourceArrayList;
    }

    public void setFeedSourceArrayList(ArrayList<String> feedSourceArrayList) {
        this.feedSourceArrayList = feedSourceArrayList;
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

    public int getUser_has_liked() {
        return user_has_liked;
    }

    public void setUser_has_liked(int user_has_liked) {
        this.user_has_liked = user_has_liked;
    }

    public UserDetailModel getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserDetailModel createdBy) {
        this.createdBy = createdBy;
    }

    String enable_comment;
    String created, updated;
    int user_has_liked;
    UserDetailModel createdBy;

}
