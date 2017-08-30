package com.app.nirogstreet.model;

/**
 * Created by Preeti on 30-08-2017.
 */

public class SessionTiming {
    public SessionTiming(String id, String clinic_id, String doctor_id, String start_time, String end_time, String days, String session) {
        this.id = id;
        this.clinic_id = clinic_id;
        this.doctor_id = doctor_id;
        this.start_time = start_time;
        this.end_time = end_time;
        this.days = days;
        this.session = session;
    }

    String id;
    String clinic_id;
    String doctor_id;
    String start_time;
    String end_time;
    String days;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClinic_id() {
        return clinic_id;
    }

    public void setClinic_id(String clinic_id) {
        this.clinic_id = clinic_id;
    }

    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    String session;
}
