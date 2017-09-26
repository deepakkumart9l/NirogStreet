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
                ArrayList<SpecializationModel> serviceModels = new ArrayList<>();
                ArrayList<ExperinceModel> experinceModels = new ArrayList<>();
                ArrayList<SpecializationModel> specializationModels = new ArrayList<>();
                ArrayList<QualificationModel> qualificationModels = new ArrayList<>();
                ArrayList<RegistrationAndDocumenModel> registrationAndDocumenModels = new ArrayList<>();
                ArrayList<ClinicDetailModel> clinicDetailModels = new ArrayList<>();

                if (message.has("clinicsDetails") && !message.isNull("clinicsDetails")) {
                    JSONArray jsonArrayclinicsDetails = message.getJSONArray("clinicsDetails");
                    for (int i = 0; i < jsonArrayclinicsDetails.length(); i++) {

                        String clinicId = null;
                        String clinicName = null;
                        String clinicMobile = null;
                        String address = null;
                        String state = null;
                        String clinicCity = null;
                        String pincode = null;
                        String at_lat = null;
                        String at_long = null;
                        String consultation_fee = null;
                        JSONObject jsonObject = jsonArrayclinicsDetails.getJSONObject(i);
                        if (jsonObject.has("consultation_fee") && !jsonObject.isNull("consultation_fee")) {
                            consultation_fee = jsonObject.getString("consultation_fee");
                        }
                        if (jsonObject.has("clinicDetail") && !jsonObject.isNull("clinicDetail")) {
                            JSONObject jsonObject1 = jsonObject.getJSONObject("clinicDetail");

                            if (jsonObject1.has("id") && !jsonObject1.isNull("id")) {
                                clinicId = jsonObject1.getString("id");
                            }
                            if (jsonObject1.has("name") && !jsonObject1.isNull("name")) {
                                clinicName = jsonObject1.getString("name");
                            }
                            if (jsonObject1.has("mobile") && !jsonObject1.isNull("mobile")) {
                                clinicMobile = jsonObject1.getString("mobile");
                            }
                            if (jsonObject1.has("address") && !jsonObject1.isNull("address")) {
                                address = jsonObject1.getString("address");
                            }
                            if (jsonObject1.has("state") && !jsonObject1.isNull("state")) {
                                state = jsonObject1.getString("state");
                            }
                            if (jsonObject1.has("city") && !jsonObject1.isNull("city")) {
                                clinicCity = jsonObject1.getString("city");
                            }
                            if (jsonObject1.has("pincode") && !jsonObject1.isNull("pincode")) {
                                pincode = jsonObject1.getString("pincode");
                            }
                            if (jsonObject1.has("at_lat") && !jsonObject1.isNull("at_lat")) {
                                at_lat = jsonObject1.getString("at_lat");
                            }
                            if (jsonObject1.has("at_long") && !jsonObject1.isNull("at_long")) {
                                at_long = jsonObject1.getString("at_long");
                            }
                        }
                        ArrayList<ServicesModel> servicesModels = new ArrayList<>();

                        clinicDetailModels.add(new ClinicDetailModel(clinicId, clinicName, clinicMobile, address, state, clinicCity, pincode, at_lat, at_long, consultation_fee, servicesModels));

                    }
                }

                serviceModels = ServicesParser.serviceParser(message);
                qualificationModels = QualificationParser.qualificationParser(message);
                registrationAndDocumenModels = RegistrationParser.registrationParser(message);


                specializationModels = SpecialitiesParser.specilities(message);

                experinceModels = ExpericenceParser.experienceParser(message);
                ArrayList<AwardsModel> awardsModels = new ArrayList<>();
                awardsModels = AwardsParser.awardsParser(message);
                ArrayList<MemberShipModel> memberShipModels = new ArrayList<>();
                memberShipModels = MemberShipParser.memberShipParser(message);
                userDetailModel = new UserDetailModel(name, email, mobile, gender, experience, profile_pic, category, dob, website, about, title, city, specializationModels, registrationAndDocumenModels, qualificationModels, experinceModels, clinicDetailModels, awardsModels, memberShipModels, serviceModels);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userDetailModel;
    }
}
