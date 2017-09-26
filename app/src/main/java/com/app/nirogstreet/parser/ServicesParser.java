package com.app.nirogstreet.parser;

import com.app.nirogstreet.model.SpecializationModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Preeti on 22-09-2017.
 */

public class ServicesParser {
    public static ArrayList<SpecializationModel> serviceParser(JSONObject message)
    {
        ArrayList<SpecializationModel>serviceModels=new ArrayList<>();
        try{
            if (message.has("services") && !message.isNull("services")) {
                JSONArray specilizationJsonArray = message.getJSONArray("services");
                for (int i = 0; i < specilizationJsonArray.length(); i++) {
                    String specilizationId = null, specilizationName = null;
                    JSONObject speilizationJsonObject = specilizationJsonArray.getJSONObject(i);
                    if (speilizationJsonObject.has("id") && !speilizationJsonObject.isNull("id")) {
                        specilizationId = speilizationJsonObject.getString("id");
                    }
                    if (speilizationJsonObject.has("name") && !speilizationJsonObject.isNull("name")) {
                        specilizationName = speilizationJsonObject.getString("name");
                    }
                    serviceModels.add(new SpecializationModel(specilizationName, specilizationId,true));

                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return serviceModels;
    }
}
