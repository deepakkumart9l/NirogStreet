package com.app.nirogstreet.model;

import java.io.Serializable;

/**
 * Created by Preeti on 27-09-2017.
 */
public class SesstionModel implements Serializable{
    String sesstion;

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    boolean isAvailable;

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getSesstion() {
        return sesstion;
    }

    public void setSesstion(String sesstion) {
        this.sesstion = sesstion;
    }

    String days;
    public SesstionModel(String startTime, String endTime,String days,String sesstion,boolean isAvailable) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.sesstion=sesstion;
        this.isAvailable=isAvailable;
        this.days=days;
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
