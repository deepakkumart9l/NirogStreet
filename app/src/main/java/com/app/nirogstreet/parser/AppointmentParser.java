package com.app.nirogstreet.parser;

import com.app.nirogstreet.model.AppointmentModel;
import com.app.nirogstreet.model.Patient_detail;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Preeti on 16-12-2017.
 */

public class AppointmentParser {
    public static ArrayList<AppointmentModel> appointmentModels(JSONArray jsonObject,String str) {
        ArrayList<AppointmentModel> appointmentModels = new ArrayList<>();
        try {

            JSONArray jsonArray = jsonObject;
            for (int i = 0; i < jsonArray.length(); i++) {
                Patient_detail patient_detail = null;
                JSONObject arrayObject = jsonArray.getJSONObject(i);
                String appointment_id = null, id = null, doctor_id = null, clinic_detail = null, coupon_id = null, date = null, time = null, status = null, createdOn = null, updatedOn = null, appointment_with = null, otp_verification = null, is_verified = null, reschedule_appointment_id = null, discount = null, fee = null, total = null;
                String patientuser_id = null;
                String patient_type = null;
                String patient_name = null;
                String payment_method = null;
                String patient_id = null;
                String patientemail = null;
                String patientstatus = null;

                String patientphone = null;
                String patientgender = null;
                String patientage = null;
                String patientdescription = null;
                String patientcreatedOn = null;
                String patientupdatedOn = null;

                if (arrayObject.has("payment_method") && !arrayObject.isNull("payment_method")) {
                    payment_method = arrayObject.getString("payment_method");
                }
                if (arrayObject.has("appointment_id") && !arrayObject.isNull("appointment_id")) {
                    appointment_id = arrayObject.getString("appointment_id");
                }
                if (arrayObject.has("id") && !arrayObject.isNull("id")) {
                    id = arrayObject.getString("id");
                }
                if (arrayObject.has("doctor_id") && !arrayObject.isNull("doctor_id")) {
                    doctor_id = arrayObject.getString("doctor_id");
                }
                if (arrayObject.has("clinic_detail") && !arrayObject.isNull("clinic_detail")) {
                    clinic_detail = arrayObject.getString("clinic_detail");
                }
                if (arrayObject.has("coupon_id") && !arrayObject.isNull("coupon_id")) {
                    coupon_id = arrayObject.getString("coupon_id");
                }
                if (arrayObject.has("date") && !arrayObject.isNull("date")) {
                    date = arrayObject.getString("date");
                }
                if (arrayObject.has("time") && !arrayObject.isNull("time")) {
                    time = arrayObject.getString("time");
                }
                if (arrayObject.has("status") && !arrayObject.isNull("status")) {
                    status = arrayObject.getString("status");
                }
                if (arrayObject.has("createdOn") && !arrayObject.isNull("createdOn")) {
                    createdOn = arrayObject.getString("createdOn");
                }
                if (arrayObject.has("updatedOn") && !arrayObject.isNull("updatedOn")) {
                    updatedOn = arrayObject.getString("updatedOn");
                }
                if (arrayObject.has("appointment_with") && !arrayObject.isNull("appointment_with")) {
                    appointment_with = arrayObject.getString("appointment_with");
                }
                if (arrayObject.has("is_verified") && !arrayObject.isNull("is_verified")) {
                    is_verified = arrayObject.getString("is_verified");
                }
                if (arrayObject.has("otp_verification") && !arrayObject.isNull("otp_verification")) {
                    otp_verification = arrayObject.getString("otp_verification");
                }
                if (arrayObject.has("reschedule_appointment_id") && !arrayObject.isNull("reschedule_appointment_id")) {
                    reschedule_appointment_id = arrayObject.getString("reschedule_appointment_id");
                }
                if (arrayObject.has("discount") && !arrayObject.isNull("discount")) {
                    discount = arrayObject.getString("discount");
                }
                if (arrayObject.has("fee") && !arrayObject.isNull("fee")) {
                    fee = arrayObject.getString("fee");
                }
                if (arrayObject.has("total") && !arrayObject.isNull("total")) {
                    total = arrayObject.getString("total");
                }
                if (arrayObject.has("patient_detail") && !arrayObject.isNull("patient_detail")) {
                    JSONObject jsonObject1 = arrayObject.getJSONObject("patient_detail");
                    if (jsonObject1.has("id") && !jsonObject1.isNull("id")) {
                        patient_id = jsonObject1.getString("id");
                    }
                    if (jsonObject1.has("user_id") && !jsonObject1.isNull("user_id")) {
                        patientuser_id = jsonObject1.getString("user_id");
                    }
                    if (jsonObject1.has("patient_type") && !jsonObject1.isNull("patient_type")) {
                        patient_type = jsonObject1.getString("patient_type");
                    }
                    if (jsonObject1.has("name") && !jsonObject1.isNull("name")) {
                        patient_name = jsonObject1.getString("name");
                    }
                    if (jsonObject1.has("email") && !jsonObject1.isNull("email")) {
                        patientemail = jsonObject1.getString("email");
                    }
                    if (jsonObject1.has("phone") && !jsonObject1.isNull("phone")) {
                        patientphone = jsonObject1.getString("phone");
                    }
                    if (jsonObject1.has("gender") && !jsonObject1.isNull("gender")) {
                        patientgender = jsonObject1.getString("gender");
                    }
                    if (jsonObject1.has("age") && !jsonObject1.isNull("age")) {
                        patientage = jsonObject1.getString("age");
                    }
                    if (jsonObject1.has("description") && !jsonObject1.isNull("description")) {
                        patientdescription = jsonObject1.getString("description");
                    }
                    if (jsonObject1.has("createdOn") && !jsonObject1.isNull("createdOn")) {
                        patientcreatedOn = jsonObject1.getString("createdOn");
                    }
                    if (jsonObject1.has("updatedOn") && !jsonObject1.isNull("updatedOn")) {
                        patientupdatedOn = jsonObject1.getString("updatedOn");
                    }
                    if (jsonObject1.has("status") && !jsonObject1.isNull("status")) {
                        patientupdatedOn = jsonObject1.getString("status");
                    }
                    patient_detail = new Patient_detail(patient_name, patient_id, patientuser_id, patient_type, patientemail, patientstatus, patientphone, patientgender, patientage, patientdescription, patientcreatedOn, patientupdatedOn);
                }
                if(i==0)
                appointmentModels.add(new AppointmentModel(patient_detail, appointment_id, id, doctor_id, clinic_detail, coupon_id, date, time, status, createdOn, updatedOn, appointment_with, otp_verification, is_verified, reschedule_appointment_id, discount, fee, total,payment_method,str));
           else
                    appointmentModels.add(new AppointmentModel(patient_detail, appointment_id, id, doctor_id, clinic_detail, coupon_id, date, time, status, createdOn, updatedOn, appointment_with, otp_verification, is_verified, reschedule_appointment_id, discount, fee, total,payment_method,""));

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return appointmentModels;
    }
}
