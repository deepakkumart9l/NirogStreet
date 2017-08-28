package com.app.nirogstreet.model;

/**
 * Created by Preeti on 25-08-2017.
 */

public class QualificationModel {
    String clgName;
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public QualificationModel(String clgName, String upladedDoc, String passingYear, String degreeName,String id) {
        this.clgName = clgName;
        this.upladedDoc = upladedDoc;
        this.passingYear = passingYear;
        this.id=id;
        this.degreeName = degreeName;
    }

    String degreeName;
    String passingYear;
    String upladedDoc;


    public String getUpladedDoc() {
        return upladedDoc;
    }

    public void setUpladedDoc(String upladedDoc) {
        this.upladedDoc = upladedDoc;
    }

    public String getPassingYear() {
        return passingYear;
    }

    public void setPassingYear(String passingYear) {
        this.passingYear = passingYear;
    }

    public String getDegreeName() {
        return degreeName;
    }

    public void setDegreeName(String degreeName) {
        this.degreeName = degreeName;
    }

    public String getClgName() {
        return clgName;
    }

    public void setClgName(String clgName) {
        this.clgName = clgName;
    }

}
