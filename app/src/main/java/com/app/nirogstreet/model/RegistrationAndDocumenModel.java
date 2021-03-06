package com.app.nirogstreet.model;

import java.io.Serializable;

/**
 * Created by Preeti on 28-08-2017.
 */

public class RegistrationAndDocumenModel implements Serializable {
    public String getCouncil_registration_number() {
        return council_registration_number;
    }

    public void setCouncil_registration_number(String council_registration_number) {
        this.council_registration_number = council_registration_number;
    }


    public String getCouncilType() {
        return councilType;
    }

    public void setCouncilType(String councilType) {
        this.councilType = councilType;
    }

    private String councilType;
    public String getUploadedDoc() {
        return uploadedDoc;
    }

    public void setUploadedDoc(String uploadedDoc) {
        this.uploadedDoc = uploadedDoc;
    }

    String uploadedDoc;
    public String getCouncil_year() {
        return council_year;
    }

    public void setCouncil_year(String council_year) {
        this.council_year = council_year;
    }

    public String getCouncil_name() {
        return council_name;
    }

    public void setCouncil_name(String council_name) {
        this.council_name = council_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReg_board() {
        return reg_board;
    }

    public void setReg_board(String reg_board) {
        this.reg_board = reg_board;
    }

    String reg_board;
    String council_registration_number;
    String council_name;
    String council_year;
    String id;

    public RegistrationAndDocumenModel(String council_registration_number, String council_name, String council_year, String id,String reg_board,String uploadedDoc,String councilType) {
        this.council_registration_number = council_registration_number;
        this.council_name = council_name;
        this.reg_board=reg_board;
        this.uploadedDoc=uploadedDoc;
        this.councilType=councilType;
        this.council_year = council_year;
        this.id = id;
    }


}
