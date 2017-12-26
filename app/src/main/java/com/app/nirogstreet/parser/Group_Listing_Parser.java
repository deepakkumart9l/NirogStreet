package com.app.nirogstreet.parser;

import com.app.nirogstreet.model.GroupModel;
import com.app.nirogstreet.model.UserDetailModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Preeti on 08-11-2017.
 */
public class Group_Listing_Parser {
    public static ArrayList<GroupModel> groupListingParser(JSONObject jsonObject1) {
        ArrayList<GroupModel> groupModels = new ArrayList<>();
        try {

            if (jsonObject1 != null) {
                if (jsonObject1.has("response") && !jsonObject1.isNull("response")) {
                    JSONObject jsonObjectResponse = jsonObject1.getJSONObject("response");
                    if (jsonObjectResponse.has("groups") && !jsonObjectResponse.isNull("groups")) {
                        JSONArray jsonArray = jsonObjectResponse.getJSONArray("groups");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            UserDetailModel userDetails = null;
                            String user_id = null, lname = null, fname = null, profile_pic = null, slug = null;
                            String id = null, name = null, totalMembers = null, banner = null, description = null, privacy = null, created = null, updated = null, updated_by = null, status = null;
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            if (jsonObject.has("id") && !jsonObject.isNull("id")) {
                                id = jsonObject.getString("id");
                            }
                            if (jsonObject.has("totalMembers") && !jsonObject.isNull("totalMembers")) {
                                totalMembers = jsonObject.getString("totalMembers");
                            }
                            if (jsonObject.has("name") && !jsonObject.isNull("name")) {
                                name = jsonObject.getString("name");
                            }
                            if (jsonObject.has("description") && !jsonObject.isNull("description")) {
                                description = jsonObject.getString("description");
                            }
                            if (jsonObject.has("banner") && !jsonObject.isNull("banner")) {
                                banner = jsonObject.getString("banner");
                            }
                            if (jsonObject.has("privacy") && !jsonObject.isNull("privacy")) {
                                privacy = jsonObject.getString("privacy");
                            }
                            if (jsonObject.has("created") && !jsonObject.isNull("created")) {
                                created = jsonObject.getString("created");
                            }
                            if (jsonObject.has("updated_by") && !jsonObject.isNull("updated_by")) {
                                updated_by = jsonObject.getString("updated_by");
                            }
                            if (jsonObject.has("updated") && !jsonObject.isNull("updated")) {
                                updated = jsonObject.getString("updated");
                            }
                            if (jsonObject.has("status") && !jsonObject.isNull("status")) {
                                status = jsonObject.getString("status");
                            }
                            if (jsonObject.has("created_by") && !jsonObject.isNull("created_by")) {
                                JSONObject userDetail = jsonObject.getJSONObject("created_by");

                                if (userDetail.has("id") && !userDetail.isNull("id")) {
                                    user_id = userDetail.getString("id");
                                }
                                if (userDetail.has("name") && !userDetail.isNull("name")) {
                                    fname = userDetail.getString("name");
                                }
                                if (userDetail.has("lname") && !userDetail.isNull("lname")) {
                                    lname = userDetail.getString("lname");
                                }
                                if (userDetail.has("profile_pic") && !userDetail.isNull("profile_pic")) {
                                    profile_pic = userDetail.getString("profile_pic");
                                }
                                if (userDetail.has("profile_pic") && !userDetail.isNull("profile_pic")) {
                                    profile_pic = userDetail.getString("profile_pic");
                                }
                                if (userDetail.has("slug") && !userDetail.isNull("slug")) {
                                    slug = userDetail.getString("slug");
                                }
                                userDetails = new UserDetailModel(user_id, name,1, "", "", "", "", profile_pic, "", "", "", "", "", "", null, null, null, null, null, null, null, null);
                            }
                            groupModels.add(new GroupModel(id, created, name, description, totalMembers, privacy, banner, userDetails, updated, status, updated_by));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return groupModels;
    }

}
