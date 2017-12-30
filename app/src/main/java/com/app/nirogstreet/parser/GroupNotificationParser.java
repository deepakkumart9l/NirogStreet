package com.app.nirogstreet.parser;

import com.app.nirogstreet.model.GroupNotificationModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Preeti on 30-12-2017.
 */

public class GroupNotificationParser {
    public static ArrayList<GroupNotificationModel> groupNotificationModels(JSONObject jsonObject) {
        ArrayList<GroupNotificationModel> groupNotificationModels = new ArrayList<>();
        try {
            if (jsonObject.has("response") && !jsonObject.isNull("response")) {
                JSONObject response = jsonObject.getJSONObject("response");
                if (response.has("groups") && !response.isNull("groups")) {
                    JSONArray jsonArray = response.getJSONArray("groups");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String communityId = null, communityName = null, userId = null, userName = null, userProfile = null;
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        if (jsonObject1.has("community") && !jsonObject1.isNull("community")) {
                            JSONObject community = jsonObject1.getJSONObject("community");
                            if (community.has("community_id") && !community.isNull("community_id")) {
                                communityId = community.getString("community_id");
                            }
                            if (community.has("name") && !community.isNull("name")) {
                                communityName = community.getString("name");
                            }
                        }
                        if (jsonObject1.has("user") && !jsonObject1.isNull("user")) {
                            JSONObject userJsonObject = jsonObject1.getJSONObject("user");
                            if (userJsonObject.has("id") && !userJsonObject.isNull("id")) {
                                userId = userJsonObject.getString("id");
                            }
                            if (userJsonObject.has("name") && !userJsonObject.isNull("name")) {
                                userName = userJsonObject.getString("name");
                            }
                            if (userJsonObject.has("profile_pic") && !userJsonObject.isNull("profile_pic")) {
                                userProfile = userJsonObject.getString("profile_pic");
                            }
                        }
                        groupNotificationModels.add(new GroupNotificationModel(communityId, communityName, userId, userName, userProfile));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return groupNotificationModels;
    }

}
