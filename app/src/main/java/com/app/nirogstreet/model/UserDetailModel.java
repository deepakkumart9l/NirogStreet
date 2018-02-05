package com.app.nirogstreet.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Preeti on 25-08-2017.
 */

public class UserDetailModel implements Serializable {
    String userId;
    String created_by;

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ArrayList<ExperinceModel> getExperinceModels() {
        return experinceModels;
    }

    public void setExperinceModels(ArrayList<ExperinceModel> experinceModels) {
        this.experinceModels = experinceModels;
    }

    ArrayList<ExperinceModel> experinceModels = new ArrayList<>();

    public ArrayList<SpecializationModel> getSpecializationModels() {
        return specializationModels;
    }

    public ArrayList<SpecializationModel> getServicesModels() {
        return servicesModels;
    }

    public void setServicesModels(ArrayList<SpecializationModel> servicesModels) {
        this.servicesModels = servicesModels;
    }

    private ArrayList<SpecializationModel> servicesModels = new ArrayList<>();

    public void setSpecializationModels(ArrayList<SpecializationModel> specializationModels) {
        this.specializationModels = specializationModels;
    }

    ArrayList<SpecializationModel> specializationModels;

    public ArrayList<QualificationModel> getQualificationModels() {
        return qualificationModels;
    }

    public void setQualificationModels(ArrayList<QualificationModel> qualificationModels) {
        this.qualificationModels = qualificationModels;
    }

    public ArrayList<RegistrationAndDocumenModel> getRegistrationAndDocumenModels() {
        return registrationAndDocumenModels;
    }

    public void setRegistrationAndDocumenModels(ArrayList<RegistrationAndDocumenModel> registrationAndDocumenModels) {
        this.registrationAndDocumenModels = registrationAndDocumenModels;
    }

    ArrayList<QualificationModel> qualificationModels;

    ArrayList<RegistrationAndDocumenModel> registrationAndDocumenModels;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    String name;
    String email;
    String mobile;
    String gender;
    String experience;
    String profile_pic;
    String category;
    String dob;
    String webSite;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    String city;

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<MemberShipModel> getMemberShipModels() {
        return memberShipModels;
    }

    public void setMemberShipModels(ArrayList<MemberShipModel> memberShipModels) {
        this.memberShipModels = memberShipModels;
    }

    ArrayList<MemberShipModel> memberShipModels = new ArrayList<>();

    public ArrayList<ClinicDetailModel> getClinicDetailModels() {
        return clinicDetailModels;
    }

    public ArrayList<AwardsModel> getAwardsModels() {
        return awardsModels;
    }

    public void setAwardsModels(ArrayList<AwardsModel> awardsModels) {
        this.awardsModels = awardsModels;
    }

    public ArrayList<AwardsModel> awardsModels = new ArrayList<>();

    public void setClinicDetailModels(ArrayList<ClinicDetailModel> clinicDetailModels) {
        this.clinicDetailModels = clinicDetailModels;
    }

    ArrayList<ClinicDetailModel> clinicDetailModels = new ArrayList<>();
    String about;
    String title;

    public int getProfile_complete() {
        return profile_complete;
    }

    public void setProfile_complete(int profile_complete) {
        this.profile_complete = profile_complete;
    }

    int profile_complete;

    public String getUser_Type() {
        return user_Type;
    }

    public void setUser_Type(String user_Type) {
        this.user_Type = user_Type;
    }

    String user_Type;

    public UserDetailModel() {

    }

    public String getReferral_code() {
        return referral_code;
    }

    public void setReferral_code(String referral_code) {
        this.referral_code = referral_code;
    }

    String referral_code;

    public UserDetailModel(String userId, String name, int profile_complete, String email, String mobile, String gender, String experience, String profile_pic, String category, String dob, String webSite, String about, String title, String city, ArrayList<SpecializationModel> specializationModels, ArrayList<RegistrationAndDocumenModel> registrationAndDocumenModels, ArrayList<QualificationModel> qualificationModels, ArrayList<ExperinceModel> experinceModels, ArrayList<ClinicDetailModel> clinicDetailModels, ArrayList<AwardsModel> awardsModels, ArrayList<MemberShipModel> memberShipModels, ArrayList<SpecializationModel> servicesModels, String created_by, String referral_code, String user_Type) {
        this.name = name;
        this.userId = userId;
        this.referral_code = referral_code;
        this.user_Type = user_Type;
        this.profile_complete = profile_complete;
        this.specializationModels = specializationModels;
        this.servicesModels = servicesModels;
        this.created_by = created_by;
        this.memberShipModels = memberShipModels;
        this.clinicDetailModels = clinicDetailModels;
        this.registrationAndDocumenModels = registrationAndDocumenModels;
        this.experinceModels = experinceModels;
        this.awardsModels = awardsModels;
        this.qualificationModels = qualificationModels;
        this.email = email;
        this.mobile = mobile;
        this.gender = gender;
        this.city = city;
        this.about = about;
        this.title = title;
        this.experience = experience;
        this.profile_pic = profile_pic;
        this.category = category;
        this.dob = dob;
        this.webSite = webSite;
    }


}
