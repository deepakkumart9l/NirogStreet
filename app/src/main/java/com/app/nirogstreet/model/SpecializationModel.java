package com.app.nirogstreet.model;

import java.io.Serializable;

/**
 * Created by Preeti on 26-08-2017.
 */
public class SpecializationModel implements Serializable{
    public String getSpecializationName() {
        return specializationName;
    }

    public void setSpecializationName(String specializationName) {
        this.specializationName = specializationName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String specializationName;

    public SpecializationModel(String specializationName, String id) {
        this.specializationName = specializationName;
        this.id = id;
    }

    String id;
}
