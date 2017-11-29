package com.app.nirogstreet.uttil;

import android.app.Application;

import com.app.nirogstreet.model.FeedModel;
import com.app.nirogstreet.model.UserDetailModel;

/**
 * Created by Preeti on 12-09-2017.
 */

public class ApplicationSingleton extends Application {
    public static boolean isGroupUpdated() {
        return isGroupUpdated;
    }

    public static boolean isClinicUpdated() {
        return isClinicUpdated;
    }

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
