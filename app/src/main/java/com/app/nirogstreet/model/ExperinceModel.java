package com.app.nirogstreet.model;

/**
 * Created by Preeti on 29-08-2017.
 */

public class ExperinceModel {


    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    String start_time;
    String end_time;
    String address;
    String id;
    String organizationName;

    public ExperinceModel(String id, String address, String end_time, String start_time, String organizationName) {
        this.id = id;
        this.address = address;
        this.end_time = end_time;
        this.start_time = start_time;
        this.organizationName = organizationName;
    }


}
