package com.app.nirogstreet.model;

/**
 * Created by Preeti on 25-08-2017.
 */

public class QualificationModel {
    public String getClgName() {
        return clgName;
    }

    public void setClgName(String clgName) {
        this.clgName = clgName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public String getUpdated_on() {
        return updated_on;
    }

    public void setUpdated_on(String updated_on) {
        this.updated_on = updated_on;
    }

    public String getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(String updated_by) {
        this.updated_by = updated_by;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDegreeName() {
        return degreeName;
    }

    public void setDegreeName(String degreeName) {
        this.degreeName = degreeName;
    }

    public String getPassingYear() {
        return passingYear;
    }

    public void setPassingYear(String passingYear) {
        this.passingYear = passingYear;
    }

    public String getUpladedDoc() {
        return upladedDoc;
    }

    public void setUpladedDoc(String upladedDoc) {
        this.upladedDoc = upladedDoc;
    }

    String clgName;
    String id;
    String userId,course_name,type,university,created_on,updated_on,updated_by,status;


    String degreeName;
    String passingYear;
    String upladedDoc;

    public QualificationModel(String clgName, String id, String userId, String course_name, String type, String university, String created_on, String updated_on, String updated_by, String status, String degreeName, String passingYear, String upladedDoc) {
        this.clgName = clgName;
        this.id = id;
        this.userId = userId;
        this.course_name = course_name;
        this.type = type;
        this.university = university;
        this.created_on = created_on;
        this.updated_on = updated_on;
        this.updated_by = updated_by;
        this.status = status;
        this.degreeName = degreeName;
        this.passingYear = passingYear;
        this.upladedDoc = upladedDoc;
    }






}
