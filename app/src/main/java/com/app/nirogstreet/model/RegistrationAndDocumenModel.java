package com.app.nirogstreet.model;

/**
 * Created by Preeti on 28-08-2017.
 */

public class RegistrationAndDocumenModel {
    public String getCouncil_registration_number() {
        return council_registration_number;
    }

    public void setCouncil_registration_number(String council_registration_number) {
        this.council_registration_number = council_registration_number;
    }

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

    String council_registration_number;
    String council_name;
    String council_year;
    String id;

    public RegistrationAndDocumenModel(String council_registration_number, String council_name, String council_year, String id) {
        this.council_registration_number = council_registration_number;
        this.council_name = council_name;
        this.council_year = council_year;
        this.id = id;
    }


}
