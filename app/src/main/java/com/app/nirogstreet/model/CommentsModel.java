package com.app.nirogstreet.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Preeti on 27-10-2017.
 */
public class CommentsModel implements Serializable

    {
        public String getProfile_pic_url() {
        return profile_pic_url;
    }

    public void setProfile_pic_url(String profile_pic_url) {
        this.profile_pic_url = profile_pic_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getComment() {
        return comment;
    }

    public int getTotalLikes() {
        return totalLikes;
    }

    public void setTotalLikes(int totalLikes) {
        this.totalLikes = totalLikes;
    }

    public boolean isUserLiked() {
        return isUserLiked;
    }

    public void setUserLiked(boolean userLiked) {
        isUserLiked = userLiked;
    }

    public boolean isUserLiked = false;
    public int totalLikes;

    public ArrayList<CommentsModel> getCommentsModels() {
        return subComments;
    }

    public void setCommentsModels(ArrayList<CommentsModel> commentsModels) {
        this.subComments = commentsModels;
    }

    ArrayList<CommentsModel> subComments=new ArrayList<>();
    public void setComment(String comment) {
        this.comment = comment;
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
    public CommentsModel(String fname, String lname, String slug, String userId, String commentId, String profile_pic_url, String name, String timeStamp, String comment, int totalLikes, boolean liked,ArrayList<CommentsModel> subComments,String user_type,String title) {
        this.fname = fname;
        this.lname = lname;
        this.totalLikes = totalLikes;
        this.isUserLiked = liked;
        this.user_type=user_type;
        this.title=title;
        this.slug = slug;
        this.subComments=subComments;
        this.userId = userId;
        this.commentId = commentId;
        this.profile_pic_url = profile_pic_url;
        this.name = name;
        this.timeStamp = timeStamp;
        this.comment = comment;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    boolean liked;
    String fname;
    String lname;


    String slug;
    String userId;

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    String commentId;
    String profile_pic_url;
    String name;
    String timeStamp;
    String comment;


}

