package com.app.nirogstreet.parser;

import com.app.nirogstreet.model.AwardsModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Preeti on 22-09-2017.
 */

public class AwardsParser {
    public static ArrayList<AwardsModel> awardsParser(JSONObject jsonObject) {
        ArrayList<AwardsModel> awardsModels = new ArrayList<>();
        try {
            if (jsonObject != null) {
                String id = null,
                        award_name = null,
                        user_id = null,
                        year = null,
                        createdOn = null;
                if (jsonObject.has("awards") && !jsonObject.isNull("awards")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("awards");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        if (jsonObject1.has("id") && !jsonObject1.isNull("id")) {
                            id = jsonObject1.getString("id");
                        }
                        if (jsonObject1.has("award_name") && !jsonObject1.isNull("award_name")) {
                            award_name = jsonObject1.getString("award_name");
                        }
                        if (jsonObject1.has("user_id") && !jsonObject1.isNull("user_id")) {
                            user_id = jsonObject1.getString("user_id");
                        }
                        if (jsonObject1.has("year") && !jsonObject1.isNull("year")) {
                            year = jsonObject1.getString("year");
                        }
                        if (jsonObject1.has("createdOn") && !jsonObject1.isNull("createdOn")) {
                            createdOn = jsonObject1.getString("createdOn");
                        }
                        awardsModels.add(new AwardsModel(id,year,award_name,createdOn,user_id));
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return awardsModels;
    }
}
