package com.app.nirogstreet.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Preeti on 30-08-2017.
 */

public class ClinicDetailModel implements Serializable {
    public String getId() {
        return id;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String locality;
    public String getClinic_docID() {
        return clinic_docID;
    }

    public void setClinic_docID(String clinic_docID) {
        this.clinic_docID = clinic_docID;
    }

    public String clinic_docID;
    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    private String created_by;
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getAt_lat() {
        return at_lat;
    }

    public void setAt_lat(String at_lat) {
        this.at_lat = at_lat;
    }

    public String getAt_long() {
        return at_long;
    }

    public void setAt_long(String at_long) {
        this.at_long = at_long;
    }

    public String getConsultation_fee() {
        return consultation_fee;
    }

    public void setConsultation_fee(String consultation_fee) {
        this.consultation_fee = consultation_fee;
    }

    String id;
    String name;
    String mobile;
    String address;
    String state;
    String city;
    String pincode;
    String at_lat;
    String at_long;
    String consultation_fee;

    public ArrayList<SpecializationModel> getServicesModels() {
        return servicesModels;
    }

    public void setServicesModels(ArrayList<SpecializationModel> servicesModels) {
        this.servicesModels = servicesModels;
    }

    ArrayList<SpecializationModel> servicesModels = new ArrayList<>();

    public ArrayList<TimingsModel> getTimingsModels() {
        return timingsModels;
    }

    public void setTimingsModels(ArrayList<TimingsModel> timingsModels) {
        this.timingsModels = timingsModels;
    }

    ArrayList<TimingsModel> timingsModels=new ArrayList<>();
    public ClinicDetailModel(String id, String name,String locality,  String mobile, String address, String state, String city, String pincode, String at_lat, String at_long, String consultation_fee, ArrayList<SpecializationModel> servicesModels,ArrayList<TimingsModel>timingsModels,String created_by,String clinic_docID) {
        this.id = id;
        this.name = name;
        this.locality=locality;
        this.mobile = mobile;
        this.address = address;
        this.timingsModels=timingsModels;
        this.state = state;
        this.city = city;
        this.servicesModels = servicesModels;
        this.pincode = pincode;
        this.at_lat = at_lat;
        this.at_long = at_long;
        this.clinic_docID=clinic_docID;
        this.consultation_fee = consultation_fee;
        this.created_by=created_by;
    }


}
