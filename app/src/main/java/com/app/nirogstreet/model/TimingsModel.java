package com.app.nirogstreet.model;

/**
 * Created by Preeti on 27-09-2017.
 */

public class TimingsModel {
    public TimingsModel(String day, SesstionModel sesstionModel1, SesstionModel sesstionModel2) {
        this.day = day;
        this.sesstionModel1 = sesstionModel1;
        this.sesstionModel2 = sesstionModel2;
    }

    String day;
    SesstionModel sesstionModel1,sesstionModel2;


    public SesstionModel getSesstionModel1() {
        return sesstionModel1;
    }

    public void setSesstionModel1(SesstionModel sesstionModel1) {
        this.sesstionModel1 = sesstionModel1;
    }

    public SesstionModel getSesstionModel2() {
        return sesstionModel2;
    }

    public void setSesstionModel2(SesstionModel sesstionModel2) {
        this.sesstionModel2 = sesstionModel2;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

}

