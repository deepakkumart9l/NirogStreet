package com.app.nirogstreet.parser;

import com.app.nirogstreet.model.LikesModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Preeti on 07-11-2017.
 */
public class LikeParser {
    public static ArrayList<LikesModel> likesParser(JSONObject jsonObject) {
        ArrayList<LikesModel> likesModels = new ArrayList<>();
        try {
            if (jsonObject != null) {
                if (jsonObject.has("likes") && !jsonObject.isNull("likes")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("likes");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String commentId = null, userId = null, fname = null, lname = null, slug = null, userProfile_pic = null, message = null, createdOn = null;
                        JSONObject jsonObjectComment = jsonArray.getJSONObject(i);
                        if (jsonObjectComment.has("id") && !jsonObjectComment.isNull("id")) {
                            commentId = jsonObjectComment.getString("id");

                        }
                        if (jsonObjectComment.has("message") && !jsonObjectComment.isNull("message")) {
                            message = jsonObjectComment.getString("message");
                        }
                        if (jsonObjectComment.has("created") && !jsonObjectComment.isNull("created")) {
                            createdOn = jsonObjectComment.getString("created");
                        }
                        if (jsonObjectComment.has("userdetail") && !jsonObjectComment.isNull("userdetail")) {
                            JSONObject userDetail = jsonObjectComment.getJSONObject("userdetail");
                            {
                                if (userDetail.has("id") && !userDetail.isNull("id")) {
                                    userId = userDetail.getString("id");
                                }
                                if (userDetail.has("name") && !userDetail.isNull("name")) {
                                    fname = userDetail.getString("name");
                                }
                                if (userDetail.has("lname") && !userDetail.isNull("lname")) {
                                    lname = userDetail.getString("lname");
                                }
                                if (userDetail.has("profile_pic") && !userDetail.isNull("profile_pic")) {
                                    userProfile_pic = userDetail.getString("profile_pic");
                                }
                                if (userDetail.has("slug") && !userDetail.isNull("slug")) {
                                    slug = userDetail.getString("slug");
                                }
                            }
                        }

                        likesModels.add(new LikesModel(commentId, createdOn, message, userProfile_pic, slug, lname, fname, userId));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return likesModels;
    }

}
