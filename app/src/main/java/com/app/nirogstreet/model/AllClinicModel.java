package com.app.nirogstreet.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Preeti on 23-11-2017.
 */
public class AllClinicModel implements Serializable {
    public String getSelected() {
        return Selected;
    }

    public void setSelected(String selected) {
        Selected = selected;
    }

    String Selected;



    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getFees() {
        return fees;
    }

    public void setFees(String fees) {
        this.fees = fees;
    }

    String fees;

    String address;
    String city;
    String locality;
    String pincode;

    public ArrayList<SpecializationModel> getSpecializationModels() {
        return specializationModels;
    }

    public void setSpecializationModels(ArrayList<SpecializationModel> specializationModels) {
        this.specializationModels = specializationModels;
    }

    ArrayList<SpecializationModel> specializationModels;
    public AllClinicModel()
    {

    }
    public String getClinic_name() {
        return clinic_name;
    }

    public void setClinic_name(String clinic_name) {
        this.clinic_name = clinic_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    String state;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    String created_by;

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public AllClinicModel(String clinic_name, String id, String address, String state, String locality, String city, String pincode, ArrayList<SpecializationModel> specializationModels, String isSelected, String fees,String created_by) {
        this.clinic_name = clinic_name;
        this.id = id;
        this.address=address;
        this.created_by=created_by;
        this.city=city;
        this.state=state;
        this.locality=locality;
        this.fees=fees;
        this.pincode=pincode;
        this.specializationModels=specializationModels;
        this.Selected=isSelected;
    }

    String clinic_name;
    String id;
}

