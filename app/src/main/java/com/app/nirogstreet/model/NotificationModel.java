package com.app.nirogstreet.model;

/**
 * Created by Preeti on 28-10-2017.
 */
public class NotificationModel {public String getEventId() {
    return eventId;
}

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getForumId() {
        return forumId;
    }

    public void setForumId(String forumId) {
        this.forumId = forumId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    private String eventId, groupId, forumId, postId;

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLink_url() {
        return link_url;
    }

    public void setLink_url(String link_url) {
        this.link_url = link_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    String profile_pic;
    String message;

    String link_url;
    String name;
    String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    String slug;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

    int unread;
    String id;

    public String getAppointment_id() {
        return appointment_id;
    }

    public void setAppointment_id(String appointment_id) {
        this.appointment_id = appointment_id;
    }

    String appointment_id;

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    String courseID;

    public String getNotification_type() {
        return notification_type;
    }

    public void setNotification_type(String notification_type) {
        this.notification_type = notification_type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    String title;
    String notification_type;
    public NotificationModel(String profile_pic, String message, String link_url, String name, String slug, String time, String postId, String groupId, String eventId, String fourumId, String id, int unread,String appointment_id,String courseID,String notification_type,String title) {
        this.profile_pic = profile_pic;
        this.message = message;
        this.appointment_id=appointment_id;
        this.title=title;
        this.time = time;
        this.courseID=courseID;
        this.postId = postId;
        this.groupId = groupId;
        this.notification_type=notification_type;
        this.id = id;
        this.unread = unread;
        this.eventId = eventId;
        this.forumId = fourumId;
        this.link_url = link_url;
        this.name = name;
        this.slug = slug;
    }


}

