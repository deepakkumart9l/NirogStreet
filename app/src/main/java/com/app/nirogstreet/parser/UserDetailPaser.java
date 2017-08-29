package com.app.nirogstreet.parser;

import com.app.nirogstreet.model.ExperinceModel;
import com.app.nirogstreet.model.QualificationModel;
import com.app.nirogstreet.model.RegistrationAndDocumenModel;
import com.app.nirogstreet.model.SpecializationModel;
import com.app.nirogstreet.model.UserDetailModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Preeti on 25-08-2017.
 */

public class UserDetailPaser {
    public static UserDetailModel userDetailParser(JSONObject dataJsonObject) {
        UserDetailModel userDetailModel = null;
        try {

            if (dataJsonObject.has("message") && !dataJsonObject.isNull("message")) {
                JSONObject message = dataJsonObject.getJSONObject("message");
                String category = "", gender = "", experience = "", profile_pic = "", createdOn = "", id = "", email = "", mobile = "", dob = "", about = "", city = "", title = "", website = "", name = "";

                if (message.has("userDetail") && !message.isNull("userDetail")) {
                    JSONObject userJsonObject = message.getJSONObject("userDetail");
                    if (userJsonObject.has("name") && !userJsonObject.isNull("name")) {
                        name = userJsonObject.getString("name");
                    }
                    if (userJsonObject.has("experience") && !userJsonObject.isNull("experience")) {
                        experience = userJsonObject.getString("experience");
                    }
                    if (userJsonObject.has("gender") && !userJsonObject.isNull("gender")) {
                        gender = userJsonObject.getString("gender");
                    }
                    if (userJsonObject.has("email") && !userJsonObject.isNull("email")) {
                        email = userJsonObject.getString("email");
                    }
                    if (userJsonObject.has("mobile") && !userJsonObject.isNull("mobile")) {
                        mobile = userJsonObject.getString("mobile");
                    }
                    if (userJsonObject.has("profile_pic") && !userJsonObject.isNull("profile_pic")) {
                        profile_pic = userJsonObject.getString("profile_pic");
                    }
                    if (userJsonObject.has("category") && !userJsonObject.isNull("category")) {
                        category = userJsonObject.getString("category");
                    }
                    if (userJsonObject.has("dob") && !userJsonObject.isNull("dob")) {
                        dob = userJsonObject.getString("dob");
                    }
                    if (userJsonObject.has("website") && !userJsonObject.isNull("website")) {
                        website = userJsonObject.getString("website");
                    }
                    if (userJsonObject.has("aboutus") && !userJsonObject.isNull("aboutus")) {
                        about = userJsonObject.getString("aboutus");
                    }
                    if (userJsonObject.has("Title") && !userJsonObject.isNull("Title")) {
                        title = userJsonObject.getString("Title");
                    }
                    if (userJsonObject.has("city") && !userJsonObject.isNull("city")) {
                        city = userJsonObject.getString("city");
                    }

                }
                ArrayList<ExperinceModel> experinceModels=new ArrayList<>();
                ArrayList<SpecializationModel> specializationModels = new ArrayList<>();
                ArrayList<QualificationModel> qualificationModels = new ArrayList<>();
                ArrayList<RegistrationAndDocumenModel> registrationAndDocumenModels = new ArrayList<>();


                if (message.has("specialities") && !message.isNull("specialities")) {

                }
                if (message.has("qualifications") && !message.isNull("qualifications")) {
                    JSONArray qualificationJsonArray = message.getJSONArray("qualifications");
                    for (int i = 0; i < qualificationJsonArray.length(); i++) {

                        String clgName = null;
                        String QualificationId = null;
                        String userId = null, course_name = null, type = null, university = null, created_on = null, updated_on = null, updated_by = null, status = null;


                        String degreeName = null;
                        String passingYear = null;
                        String upladedDoc = null;
                        JSONObject qualificationJsonObject = qualificationJsonArray.getJSONObject(i);
                        if (qualificationJsonObject.has("id") && !qualificationJsonObject.isNull("id")) {
                            QualificationId = qualificationJsonObject.getString("id");
                        }
                        if (qualificationJsonObject.has("user_id") && !qualificationJsonObject.isNull("user_id")) {
                            userId = qualificationJsonObject.getString("user_id");
                        }
                        if (qualificationJsonObject.has("degree") && !qualificationJsonObject.isNull("degree")) {
                            degreeName = qualificationJsonObject.getString("degree");
                        }
                        if (qualificationJsonObject.has("course_name") && !qualificationJsonObject.isNull("course_name")) {
                            course_name = qualificationJsonObject.getString("course_name");
                        }
                        if (qualificationJsonObject.has("type") && !qualificationJsonObject.isNull("type")) {
                            type = qualificationJsonObject.getString("type");
                        }
                        if (qualificationJsonObject.has("college") && !qualificationJsonObject.isNull("college")) {
                            clgName = qualificationJsonObject.getString("college");
                        }
                        if (qualificationJsonObject.has("university") && !qualificationJsonObject.isNull("university")) {
                            university = qualificationJsonObject.getString("university");
                        }
                        if (qualificationJsonObject.has("year") && !qualificationJsonObject.isNull("year")) {
                            passingYear = qualificationJsonObject.getString("year");
                        }
                        if (qualificationJsonObject.has("edu_file") && !qualificationJsonObject.isNull("edu_file")) {
                            upladedDoc = qualificationJsonObject.getString("edu_file");
                        }
                        if (qualificationJsonObject.has("createdOn") && !qualificationJsonObject.isNull("createdOn")) {
                            created_on = qualificationJsonObject.getString("createdOn");
                        }
                        if (qualificationJsonObject.has("updatedOn") && !qualificationJsonObject.isNull("updatedOn")) {
                            updated_on = qualificationJsonObject.getString("updatedOn");
                        }
                        if (qualificationJsonObject.has("updated_by") && !qualificationJsonObject.isNull("updated_by")) {
                            updated_by = qualificationJsonObject.getString("updated_by");
                        }
                        if (qualificationJsonObject.has("status") && !qualificationJsonObject.isNull("status")) {
                            status = qualificationJsonObject.getString("status");
                        }
                        qualificationModels.add(new QualificationModel(clgName, QualificationId, userId, course_name, type, university, created_on, updated_on, updated_by, status, degreeName, passingYear, upladedDoc));
                    }

                }
                if (message.has("registrations") && !message.isNull("registrations")) {
                    JSONArray registraionJsonArray = message.getJSONArray("registrations");
                    for (int i = 0; i < registraionJsonArray.length(); i++) {
                        String reg_board = null;
                        String council_registration_number = null;
                        String council_name = null;
                        String council_year = null;
                        String registrationId = null;
                        JSONObject registraionJsonObject = registraionJsonArray.getJSONObject(i);
                        if (registraionJsonObject.has("id") && !registraionJsonObject.isNull("id")) {
                            registrationId = registraionJsonObject.getString("id");
                        }
                        if (registraionJsonObject.has("council") && !registraionJsonObject.isNull("council")) {
                            council_name = registraionJsonObject.getString("council");
                        }
                        if (registraionJsonObject.has("registration_number") && !registraionJsonObject.isNull("registration_number")) {
                            council_registration_number = registraionJsonObject.getString("registration_number");
                        }
                        if (registraionJsonObject.has("year") && !registraionJsonObject.isNull("year")) {
                            council_year = registraionJsonObject.getString("year");
                        }
                        if (registraionJsonObject.has("reg_board") && !registraionJsonObject.isNull("reg_board")) {
                            reg_board = registraionJsonObject.getString("reg_board");
                        }
                        registrationAndDocumenModels.add(new RegistrationAndDocumenModel(council_registration_number, council_name, council_year, registrationId, reg_board));
                    }
                }
                if (message.has("specialities") && !message.isNull("specialities")) {
                    JSONArray specilizationJsonArray = message.getJSONArray("specialities");
                    for (int i = 0; i < specilizationJsonArray.length(); i++) {
                        String specilizationId = null, specilizationName = null;
                        JSONObject speilizationJsonObject = specilizationJsonArray.getJSONObject(i);
                        if (speilizationJsonObject.has("id") && !speilizationJsonObject.isNull("specialities")) {
                            specilizationId = speilizationJsonObject.getString("id");
                        }
                        if (speilizationJsonObject.has("name") && !speilizationJsonObject.isNull("name")) {
                            specilizationName = speilizationJsonObject.getString("name");
                        }
                        specializationModels.add(new SpecializationModel(specilizationName, specilizationId));

                    }
                }
                if(message.has("experiences")&&!message.isNull("experiences"))
                {

                   JSONArray experiencesJsonObject=message.getJSONArray("experiences") ;
                    for(int i=0;i<experiencesJsonObject.length();i++)
                    {
                        String start_time=null;
                        String end_time=null;
                        String address=null;
                        String experiencesid=null;
                        String organizationName=null;
                        JSONObject jsonObject=experiencesJsonObject.getJSONObject(i);
                        if(jsonObject.has("id")&&!jsonObject.isNull("id"))
                        {
                            experiencesid=jsonObject.getString("id");
                        }
                        if(jsonObject.has("organizationName")&&!jsonObject.isNull("organizationName"))
                        {
                            organizationName=jsonObject.getString("organizationName");
                        }
                        if(jsonObject.has("start_time")&&!jsonObject.isNull("start_time"))
                        {
                            start_time=jsonObject.getString("start_time");
                        }
                        if(jsonObject.has("end_time")&&!jsonObject.isNull("end_time"))
                        {
                            end_time=jsonObject.getString("end_time");
                        }
                        if(jsonObject.has("address")&&!jsonObject.isNull("address"))
                        {
                            address=jsonObject.getString("address");
                        }
                        experinceModels.add(new ExperinceModel(experiencesid,address,end_time,start_time,organizationName));
                    }
                }

                userDetailModel = new UserDetailModel(name, email, mobile, gender, experience, profile_pic, category, dob, website, about, title, city, specializationModels, registrationAndDocumenModels, qualificationModels,experinceModels);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userDetailModel;
    }
}
