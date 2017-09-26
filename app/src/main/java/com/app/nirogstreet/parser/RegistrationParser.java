package com.app.nirogstreet.parser;

import com.app.nirogstreet.model.RegistrationAndDocumenModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Preeti on 21-09-2017.
 */

public class RegistrationParser {
    public static ArrayList<RegistrationAndDocumenModel> registrationParser(JSONObject message)
    {
        ArrayList<RegistrationAndDocumenModel> registrationAndDocumenModels=new ArrayList<>();
        try {
            if (message.has("registrations") && !message.isNull("registrations")) {
                JSONArray registraionJsonArray = message.getJSONArray("registrations");
                for (int i = 0; i < registraionJsonArray.length(); i++) {
                    String reg_board = null;
                    String council_registration_number = null;
                    String council_name = null;
                    String reg_file=null;
                    String council_year = null;
                    String registrationId = null;
                    String council_Type=null;
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
                    if(registraionJsonObject.has("council_type")&&!registraionJsonObject.isNull("council_type"))
                    {
                        council_Type=registraionJsonObject.getString("council_type");
                    }
                    if(registraionJsonObject.has("reg_file")&&!registraionJsonObject.isNull("reg_file"))
                    {
                       reg_file=registraionJsonObject.getString("reg_file");
                    }
                    registrationAndDocumenModels.add(new RegistrationAndDocumenModel(council_registration_number, council_name, council_year, registrationId, reg_board,reg_file,council_Type));
                }
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }
     return    registrationAndDocumenModels;
    }
}
