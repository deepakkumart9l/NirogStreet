package com.app.nirogstreet.model;

/**
 * Created by Preeti on 16-12-2017.
 */

public class AppointmentModel {
    Patient_detail patient_detail;

    public String getAppointment_id() {
        return appointment_id;
    }

    public void setAppointment_id(String appointment_id) {
        this.appointment_id = appointment_id;
    }

    public Patient_detail getPatient_detail() {
        return patient_detail;
    }

    public void setPatient_detail(Patient_detail patient_detail) {
        this.patient_detail = patient_detail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }

    public String getClinic_detail() {
        return clinic_detail;
    }

    public void setClinic_detail(String clinic_detail) {
        this.clinic_detail = clinic_detail;
    }

    public String getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(String coupon_id) {
        this.coupon_id = coupon_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getAppointment_with() {
        return appointment_with;
    }

    public void setAppointment_with(String appointment_with) {
        this.appointment_with = appointment_with;
    }

    public String getOtp_verification() {
        return otp_verification;
    }

    public void setOtp_verification(String otp_verification) {
        this.otp_verification = otp_verification;
    }

    public String getIs_verified() {
        return is_verified;
    }

    public void setIs_verified(String is_verified) {
        this.is_verified = is_verified;
    }

    public String getReschedule_appointment_id() {
        return reschedule_appointment_id;
    }

    public void setReschedule_appointment_id(String reschedule_appointment_id) {
        this.reschedule_appointment_id = reschedule_appointment_id;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    String appointment_id;
    String id;
    String doctor_id;
    String clinic_detail;
    String coupon_id;
    String date;
    String time;
    String status;
    String createdOn;
    String updatedOn;
    String appointment_with;
    String otp_verification;
    String is_verified;
    String reschedule_appointment_id;
    String discount;
    String fee;
    String total;

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    String payment_method;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    String title;
    public AppointmentModel(Patient_detail patient_detail, String appointment_id, String id, String doctor_id, String clinic_detail, String coupon_id, String date, String time, String status, String createdOn, String updatedOn, String appointment_with, String otp_verification, String is_verified, String reschedule_appointment_id, String discount, String fee, String total,String payment_method,String title) {
        this.patient_detail = patient_detail;
        this.title=title;
        this.appointment_id = appointment_id;
        this.id = id;
        this.doctor_id = doctor_id;
        this.clinic_detail = clinic_detail;
        this.coupon_id = coupon_id;
        this.date = date;
        this.time = time;
        this.status = status;
        this.createdOn = createdOn;
        this.updatedOn = updatedOn;
        this.appointment_with = appointment_with;
        this.otp_verification = otp_verification;
        this.is_verified = is_verified;
        this.reschedule_appointment_id = reschedule_appointment_id;
        this.discount = discount;
        this.fee = fee;
        this.total = total;
        this.payment_method=payment_method;
    }


}
