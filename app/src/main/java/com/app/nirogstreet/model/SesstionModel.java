package com.app.nirogstreet.model;

/**
 * Created by Preeti on 27-09-2017.
 */
public class SesstionModel {
    public SesstionModel(String startTime, String endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    String startTime="";
    String endTime="";
}
