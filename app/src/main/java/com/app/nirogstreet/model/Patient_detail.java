package com.app.nirogstreet.model;

/**
 * Created by Preeti on 16-12-2017.
 */

public class Patient_detail {
    String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String name;
    public Patient_detail(String name,String id, String user_id, String patient_type, String email, String status, String phone, String gender, String age, String description, String createdOn, String updatedOn) {
        this.id = id;
        this.name=name;
        this.user_id = user_id;
        this.patient_type = patient_type;
        this.email = email;
        this.status = status;
        this.phone = phone;
        this.gender = gender;
        this.age = age;
        this.description = description;
        this.createdOn = createdOn;
        this.updatedOn = updatedOn;
    }

    String user_id;
    String patient_type;
    String email;
    String status;

    String phone;
    String gender;
    String age;
    String description;
    String createdOn;
    String updatedOn;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPatient_type() {
        return patient_type;
    }

    public void setPatient_type(String patient_type) {
        this.patient_type = patient_type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }


}
