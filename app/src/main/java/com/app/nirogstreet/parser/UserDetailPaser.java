package com.app.nirogstreet.parser;

import com.app.nirogstreet.model.AwardsModel;
import com.app.nirogstreet.model.ClinicDetailModel;
import com.app.nirogstreet.model.ExperinceModel;
import com.app.nirogstreet.model.MemberShipModel;
import com.app.nirogstreet.model.QualificationModel;
import com.app.nirogstreet.model.RegistrationAndDocumenModel;
import com.app.nirogstreet.model.ServicesModel;
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
                int profile_complete=1;
                String user_type=null;
                String category = "", gender = "", experience = "",referral_code="", profile_pic = "", createdOn = "", id = "", email = "", mobile = "", dob = "", about = "", city = "", title = "", website = "", name = "";
                if (message.has("userDetail") && !message.isNull("userDetail")) {
                    JSONObject userJsonObject = message.getJSONObject("userDetail");
                    if (userJsonObject.has("name") && !userJsonObject.isNull("name")) {
                        name = userJsonObject.getString("name");
                    }
                    if(userJsonObject.has("user_type")&&!userJsonObject.isNull("user_type"))
                    {
                        user_type=userJsonObject.getString("user_type");
                    }
                    if(userJsonObject.has("referral_code")&&!userJsonObject.isNull("referral_code"))
                    {
                        referral_code=userJsonObject.getString("referral_code");
                    }
                    if(userJsonObject.has("profile_complete")&&!userJsonObject.isNull("profile_complete"))
                    {
                        profile_complete=userJsonObject.getInt("profile_complete");
                    }
                    if (userJsonObject.has("id") && !userJsonObject.isNull("id")) {
                        id = userJsonObject.getString("id");
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
                ArrayList<SpecializationModel> serviceModels = new ArrayList<>();
                ArrayList<ExperinceModel> experinceModels = new ArrayList<>();
                ArrayList<SpecializationModel> specializationModels = new ArrayList<>();
                ArrayList<QualificationModel> qualificationModels = new ArrayList<>();
                ArrayList<RegistrationAndDocumenModel> registrationAndDocumenModels = new ArrayList<>();
                ArrayList<ClinicDetailModel> clinicDetailModels = new ArrayList<>();

                clinicDetailModels = Clinic_Detail_Parser.clinic_detail_parser(message);
                serviceModels = ServicesParser.serviceParser(message);
                qualificationModels = QualificationParser.qualificationParser(message);
                registrationAndDocumenModels = RegistrationParser.registrationParser(message);


                specializationModels = SpecialitiesParser.specilities(message);

                experinceModels = ExpericenceParser.experienceParser(message);
                ArrayList<AwardsModel> awardsModels = new ArrayList<>();
                awardsModels = AwardsParser.awardsParser(message);
                ArrayList<MemberShipModel> memberShipModels = new ArrayList<>();
                memberShipModels = MemberShipParser.memberShipParser(message);
                userDetailModel = new UserDetailModel(id,name,profile_complete, email, mobile, gender, experience, profile_pic, category, dob, website, about, title, city, specializationModels, registrationAndDocumenModels, qualificationModels, experinceModels, clinicDetailModels, awardsModels, memberShipModels, serviceModels,null,referral_code,user_type);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userDetailModel;
    }
}
