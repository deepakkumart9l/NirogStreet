package com.app.nirogstreet.uttil;

import android.app.Application;

import com.app.nirogstreet.model.UserDetailModel;

/**
 * Created by Preeti on 12-09-2017.
 */

public class ApplicationSingleton extends Application {
    public static UserDetailModel getUserDetailModel() {
        return userDetailModel;
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

    public static boolean isExperinceUpdated=false;
    public static boolean isQualificationUpdated() {
        return isQualificationUpdated;
    }

    public static void setIsQualificationUpdated(boolean isQualificationUpdated) {
        ApplicationSingleton.isQualificationUpdated = isQualificationUpdated;
    }

    public static boolean isQualificationUpdated=false;
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

    private static boolean isMemberShipUpdated=false;
    public static boolean isAwardUpdated() {
        return isAwardUpdated;
    }
    public static void setIsAwardUpdated(boolean isAwardUpdated) {
        ApplicationSingleton.isAwardUpdated = isAwardUpdated;
    }
    private static boolean isAwardUpdated=false;

    private static boolean isRegistrationUpdated=false;
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
}
