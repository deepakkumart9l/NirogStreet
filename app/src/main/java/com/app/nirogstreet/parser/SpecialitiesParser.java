package com.app.nirogstreet.parser;

import com.app.nirogstreet.model.SpecializationModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Preeti on 21-09-2017.
 */

public class SpecialitiesParser {
    public static ArrayList<SpecializationModel>specilities(JSONObject message)
    {

        ArrayList<SpecializationModel> specializationModels=new ArrayList<>();
        try{
            if (message.has("specialities") && !message.isNull("specialities")) {
                JSONArray specilizationJsonArray = message.getJSONArray("specialities");
                for (int i = 0; i < specilizationJsonArray.length(); i++) {
                    String specilizationId = null, specilizationName = null;
                    JSONObject speilizationJsonObject = specilizationJsonArray.getJSONObject(i);
                    if (speilizationJsonObject.has("id") && !speilizationJsonObject.isNull("id")) {
                        specilizationId = speilizationJsonObject.getString("id");
                    }
                    if (speilizationJsonObject.has("name") && !speilizationJsonObject.isNull("name")) {
                        specilizationName = speilizationJsonObject.getString("name");
                    }
                    specializationModels.add(new SpecializationModel(specilizationName, specilizationId,true));

                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return specializationModels;
    }
}
