package com.app.nirogstreet.uttil;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.app.nirogstreet.model.Course_Detail_model;
import com.app.nirogstreet.model.FeedModel;
import com.app.nirogstreet.model.UserDetailModel;

/**
 * Created by Preeti on 12-09-2017.
 */

public class ApplicationSingleton extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static boolean isDocOpen() {
        return isDocOpen;
    }

    public static void setIsDocOpen(boolean isDocOpen) {
        ApplicationSingleton.isDocOpen = isDocOpen;
    }

    public static boolean isDocOpen=false;
    public static boolean isGroupUpdated() {
        return isGroupUpdated;
    }

    public static Course_Detail_model course_detail_model;
    public static boolean isJoinedCommunity = false;

    public static boolean isJoinedCommunity() {
        return isJoinedCommunity;
    }

    public static void setIsJoinedCommunity(boolean isJoinedCommunity) {
        ApplicationSingleton.isJoinedCommunity = isJoinedCommunity;
    }

    public static boolean isEnrolledNow() {
        return enrolledNow;
    }

    public static void setEnrolledNow(boolean enrolledNow) {
        ApplicationSingleton.enrolledNow = enrolledNow;
    }

    public static boolean enrolledNow = false;

    public static boolean isPostDeleted() {
        return isPostDeleted;
    }

    public static void setIsPostDeleted(boolean isPostDeleted) {
        ApplicationSingleton.isPostDeleted = isPostDeleted;
    }

    public static boolean isPostDeleted = false;

    public static Course_Detail_model getCourse_detail_model() {
        return course_detail_model;
    }

    public static void setCourse_detail_model(Course_Detail_model course_detail_model) {
        ApplicationSingleton.course_detail_model = course_detail_model;
    }

    public static FeedModel getFeedModelPostEdited() {
        return feedModelPostEdited;
    }

    public static void setFeedModelPostEdited(FeedModel feedModelPostEdited) {
        ApplicationSingleton.feedModelPostEdited = feedModelPostEdited;
    }

    public static boolean isCourseSubscribe() {
        return courseSubscribe;
    }

    public static void setCourseSubscribe(boolean courseSubscribe) {
        ApplicationSingleton.courseSubscribe = courseSubscribe;
    }

    public static boolean courseSubscribe = false;

    public static FeedModel feedModelPostEdited;

    public static boolean isClinicUpdated() {
        return isClinicUpdated;
    }

    public static int getGroupRequestCount() {
        return groupRequestCount;
    }

    public static void setGroupRequestCount(int groupRequestCount) {
        ApplicationSingleton.groupRequestCount = groupRequestCount;
    }
    public static int getInvitationRequestCount() {
        return invitationRequestCount;
    }

    public static void setInvitationRequestCount(int invitationRequestCount) {
        ApplicationSingleton.invitationRequestCount = invitationRequestCount;
    }

    public static int invitationRequestCount=-1;
    public static int groupRequestCount=-1;
    public static int getPostEditPosition() {
        return postEditPosition;
    }

    public static void setPostEditPosition(int postEditPosition) {
        ApplicationSingleton.postEditPosition = postEditPosition;
    }

    public static int postEditPosition = -1;
    public static boolean isListingFinish = false;
    public static FeedModel feedModel;

    public static FeedModel getFeedModel() {
        return feedModel;
    }

    public static void setFeedModel(FeedModel feedModel) {
        ApplicationSingleton.feedModel = feedModel;
    }

    public static int getPostPosition() {
        return postPosition;
    }

    public static void setPostPosition(int postPosition) {
        ApplicationSingleton.postPosition = postPosition;
    }

    public static int getPostSelectedPostion() {
        return PostSelectedPostion;
    }

    public static int getTotalLike() {
        return totalLike;
    }

    public static void setTotalLike(int totalLike) {
        ApplicationSingleton.totalLike = totalLike;
    }

    public static boolean isCurruntUserLiked() {
        return curruntUserLiked;
    }

    public static void setCurruntUserLiked(boolean curruntUserLiked) {
        ApplicationSingleton.curruntUserLiked = curruntUserLiked;
    }

    public static boolean curruntUserLiked = false;
    public static int totalLike = -1;

    public static int getNoOfComment() {
        return noOfComment;
    }

    public static void setNoOfComment(int noOfComment) {
        ApplicationSingleton.noOfComment = noOfComment;
    }

    public static int noOfComment = -1;

    public static void setPostSelectedPostion(int postSelectedPostion) {
        PostSelectedPostion = postSelectedPostion;
    }

    public static int PostSelectedPostion = -1;
    public static int postPosition = -1;

    public static boolean isListingFinish() {
        return isListingFinish;
    }

    public static void setIsListingFinish(boolean isListingFinish) {
        ApplicationSingleton.isListingFinish = isListingFinish;
    }

    public static void setIsClinicUpdated(boolean isClinicUpdated) {
        ApplicationSingleton.isClinicUpdated = isClinicUpdated;
    }

    public static boolean isClinicUpdated;

    public static boolean isGroupCreated() {
        return isGroupCreated;
    }

    public static void setIsGroupCreated(boolean isGroupCreated) {
        ApplicationSingleton.isGroupCreated = isGroupCreated;
    }

    public static boolean isGroupCreated = false;

    public static void setIsGroupUpdated(boolean isGroupUpdated) {
        ApplicationSingleton.isGroupUpdated = isGroupUpdated;
    }

    public static boolean isGroupUpdated = false;

    public static boolean isCommented = false;

    public static boolean isEditFeedPostExecuted() {
        return editFeedPostExecuted;
    }

    public static void setEditFeedPostExecuted(boolean editFeedPostExecuted) {
        ApplicationSingleton.editFeedPostExecuted = editFeedPostExecuted;
    }

    public static boolean editFeedPostExecuted = false;

    public static boolean isProfilePostExecuted() {
        return isProfilePostExecuted;
    }

    public static void setIsProfilePostExecuted(boolean isProfilePostExecuted) {
        ApplicationSingleton.isProfilePostExecuted = isProfilePostExecuted;
    }

    public static boolean isProfilePostExecuted = false;

    public static int no_of_count = -1;

    public static int getPost_position() {
        return post_position;
    }

    public static int post_position = -1;

    public static void setPost_position(int post_position) {
        ApplicationSingleton.post_position = post_position;
    }

    public static UserDetailModel getUserDetailModel() {
        return userDetailModel;
    }

    public static boolean isCommented() {
        return isCommented;
    }

    public static int getNo_of_count() {
        return no_of_count;
    }

    public static void setNo_of_count(int no_of_count) {
        ApplicationSingleton.no_of_count = no_of_count;
    }

    public static void setIsCommented(boolean isCommented) {
        ApplicationSingleton.isCommented = isCommented;
    }

    public static void setUserDetailModel(UserDetailModel userDetailModel) {
        ApplicationSingleton.userDetailModel = userDetailModel;
    }

    public static boolean isExperinceUpdated() {
        return isExperinceUpdated;
    }

    public static void setIsExperinceUpdated(boolean isExperinceUpdated) {
        ApplicationSingleton.isExperinceUpdated = isExperinceUpdated;
    }

    public static boolean isExperinceUpdated = false;

    public static boolean isQualificationUpdated() {
        return isQualificationUpdated;
    }

    public static void setIsQualificationUpdated(boolean isQualificationUpdated) {
        ApplicationSingleton.isQualificationUpdated = isQualificationUpdated;
    }

    public static boolean isQualificationUpdated = false;

    public static boolean isServicesAndSpecializationUpdated() {
        return ServicesAndSpecializationUpdated;
    }

    public static boolean isRegistrationUpdated() {
        return isRegistrationUpdated;
    }

    public static void setRegistrationUpdated(boolean registrationUpdated) {
        isRegistrationUpdated = registrationUpdated;
    }

    public static boolean isMemberShipUpdated() {
        return isMemberShipUpdated;
    }

    public static void setIsMemberShipUpdated(boolean isMemberShipUpdated) {
        ApplicationSingleton.isMemberShipUpdated = isMemberShipUpdated;
    }

    private static boolean isMemberShipUpdated = false;

    public static boolean isAwardUpdated() {
        return isAwardUpdated;
    }

    public static void setIsAwardUpdated(boolean isAwardUpdated) {
        ApplicationSingleton.isAwardUpdated = isAwardUpdated;
    }

    private static boolean isAwardUpdated = false;

    private static boolean isRegistrationUpdated = false;

    public static void setServicesAndSpecializationUpdated(boolean servicesAndSpecializationUpdated) {
        ServicesAndSpecializationUpdated = servicesAndSpecializationUpdated;
    }

    public static boolean ServicesAndSpecializationUpdated;

    public static boolean isContactInfoUpdated() {
        return isContactInfoUpdated;
    }

    public static void setIsContactInfoUpdated(boolean isContactInfoUpdated) {
        ApplicationSingleton.isContactInfoUpdated = isContactInfoUpdated;
    }

    private static boolean isContactInfoUpdated;
    private static UserDetailModel userDetailModel;


    public static int getVideoPostion() {
        return videoPostion;
    }

    public static void setVideoPostion(int videoPostion) {
        ApplicationSingleton.videoPostion = videoPostion;
    }

    public static int videoPostion = 0;


}
