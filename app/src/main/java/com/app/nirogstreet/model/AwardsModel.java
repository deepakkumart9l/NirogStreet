package com.app.nirogstreet.model;

import java.io.Serializable;

/**
 * Created by Preeti on 07-09-2017.
 */

public class AwardsModel implements Serializable{
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAwardName() {
        return awardName;
    }

    public void setAwardName(String awardName) {
        this.awardName = awardName;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    String createdOn;
    String userId;
    String id;
    String awardName;
    String year;


    public AwardsModel(String id, String year, String awardName,String createdOn,String userId) {
        this.id = id;
        this.year = year;
        this.createdOn=createdOn;
        this.userId=userId;
        this.awardName = awardName;
    }

}
