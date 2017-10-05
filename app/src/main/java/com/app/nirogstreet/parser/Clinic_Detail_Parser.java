package com.app.nirogstreet.parser;

import com.app.nirogstreet.model.ClinicDetailModel;
import com.app.nirogstreet.model.ServicesModel;
import com.app.nirogstreet.model.SpecializationModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Preeti on 05-10-2017.
 */

public class Clinic_Detail_Parser {
    public static ArrayList<ClinicDetailModel> clinic_detail_parser(JSONObject message)
    {
        ArrayList<ClinicDetailModel> clinicDetailModels=new ArrayList<>();
        try {
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
                    ArrayList<SpecializationModel> servicesModels = new ArrayList<>();

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
                        servicesModels=ServicesParser.serviceParser(jsonObject1);

                    }
                    clinicDetailModels.add(new ClinicDetailModel(clinicId, clinicName, clinicMobile, address, state, clinicCity, pincode, at_lat, at_long, consultation_fee, servicesModels));

                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return clinicDetailModels;
    }
}
