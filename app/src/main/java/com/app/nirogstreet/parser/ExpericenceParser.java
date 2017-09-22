package com.app.nirogstreet.parser;

import com.app.nirogstreet.model.ExperinceModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Preeti on 21-09-2017.
 */

public class ExpericenceParser {
    public static ArrayList<ExperinceModel> experienceParser(JSONObject message)
    {
        ArrayList<ExperinceModel> experinceModels=new ArrayList<>();
        try{
              if (message.has("experiences") && !message.isNull("experiences")) {

                    JSONArray experiencesJsonObject = message.getJSONArray("experiences");
                    for (int i = 0; i < experiencesJsonObject.length(); i++) {
                        String start_time = null;
                        String end_time = null;
                        String address = null;
                        String experiencesid = null;
                        String organizationName = null;
                        JSONObject jsonObject = experiencesJsonObject.getJSONObject(i);
                        if (jsonObject.has("id") && !jsonObject.isNull("id")) {
                            experiencesid = jsonObject.getString("id");
                        }
                        if (jsonObject.has("organizationName") && !jsonObject.isNull("organizationName")) {
                            organizationName = jsonObject.getString("organizationName");
                        }
                        if (jsonObject.has("start_time") && !jsonObject.isNull("start_time")) {
                            start_time = jsonObject.getString("start_time");
                        }
                        if (jsonObject.has("end_time") && !jsonObject.isNull("end_time")) {
                            end_time = jsonObject.getString("end_time");
                        }
                        if (jsonObject.has("address") && !jsonObject.isNull("address")) {
                            address = jsonObject.getString("address");
                        }
                        experinceModels.add(new ExperinceModel(experiencesid, address, end_time, start_time, organizationName));
                    }
                }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return experinceModels;
    }
}
