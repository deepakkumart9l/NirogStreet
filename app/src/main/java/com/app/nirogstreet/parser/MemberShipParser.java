package com.app.nirogstreet.parser;

import com.app.nirogstreet.model.MemberShipModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Preeti on 22-09-2017.
 */

public class MemberShipParser {
    public static ArrayList<MemberShipModel> memberShipParser(JSONObject jsonObject)
    {
        ArrayList<MemberShipModel> memberShipModels=new ArrayList<>();
        try{
            if (jsonObject.has("membership") && !jsonObject.isNull("membership")) {
                JSONArray jsonArray = jsonObject.getJSONArray("membership");
                for (int i = 0; i < jsonArray.length(); i++) {
                    String id=null,doctor_id=null,membership=null;
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    if (jsonObject1.has("id") && !jsonObject1.isNull("id")) {
                        id = jsonObject1.getString("id");
                    }
                    if (jsonObject1.has("doctor_id") && !jsonObject1.isNull("doctor_id")) {
                        doctor_id = jsonObject1.getString("doctor_id");
                    }
                    if (jsonObject1.has("membership") && !jsonObject1.isNull("membership")) {
                        membership = jsonObject1.getString("membership");
                    }

                    memberShipModels.add(new MemberShipModel(id,membership,doctor_id));
                }  }      }catch (Exception e)
        {
            e.printStackTrace();
        }
        return memberShipModels;
    }
}
