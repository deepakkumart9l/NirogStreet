package com.app.nirogstreet.parser;

import com.app.nirogstreet.model.Author_detail_Module;
import com.app.nirogstreet.model.CoursesModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Preeti on 12-12-2017.
 */

public class Courses_Parser {
    public static ArrayList<CoursesModel> groupListingParser(JSONObject jsonObject) {
        ArrayList<CoursesModel> coursesModels = new ArrayList<>();
        try {
            if (jsonObject.has("response") && !jsonObject.isNull("response")) {
                JSONObject responseJsonObject = jsonObject.getJSONObject("response");
                if (responseJsonObject.has("courses") && !responseJsonObject.isNull("courses")) {
                    JSONArray jsonArray = responseJsonObject.getJSONArray("courses");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String id = null, parent_id = null, name = null, banner = null, description = null, created_by = null, created_at = null, updated_by = null, updated_at = null, status = null, type = null, doc_type = null;
                        Author_detail_Module author_detail_module = null;
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        if (jsonObject1.has("id") && !jsonObject1.isNull("id")) {
                            id = jsonObject1.getString("id");
                        }
                        if (jsonObject1.has("parent_id") && !jsonObject1.isNull("parent_id")) {
                            parent_id = jsonObject1.getString("parent_id");
                        }
                        if (jsonObject1.has("name") && !jsonObject1.isNull("name")) {
                            name = jsonObject1.getString("name");
                        }
                        if (jsonObject1.has("description") && !jsonObject1.isNull("description")) {
                            description = jsonObject1.getString("description");
                        }
                        if (jsonObject1.has("banner") && !jsonObject1.isNull("banner")) {
                            banner = jsonObject1.getString("banner");
                        }
                        if (jsonObject1.has("created_by") && !jsonObject1.isNull("created_by")) {
                            created_by = jsonObject1.getString("created_by");
                        }
                        if (jsonObject1.has("created_at") && !jsonObject1.isNull("created_at")) {
                            created_at = jsonObject1.getString("created_at");
                        }
                        if (jsonObject1.has("updated_by") && !jsonObject1.isNull("updated_by")) {
                            updated_by = jsonObject1.getString("updated_by");
                        }
                        if (jsonObject1.has("updated_at") && !jsonObject1.isNull("updated_at")) {
                            updated_at = jsonObject1.getString("updated_at");
                        }
                        if (jsonObject1.has("status") && !jsonObject1.isNull("status")) {
                            status = jsonObject1.getString("status");
                        }
                        if (jsonObject1.has("type") && !jsonObject1.isNull("type")) {
                            type = jsonObject1.getString("type");
                        }
                        if (jsonObject1.has("doc_type") && !jsonObject1.isNull("doc_type")) {
                            doc_type = jsonObject1.getString("doc_type");
                        }
                        if (jsonObject1.has("author_ids") && !jsonObject1.isNull("author_ids")) {
                            String auth_email = null;
                            String auth_mobile = null;
                            String auth_alternateMobile = null;
                            String auth_gender = null;
                            String auth_experience = null;
                            String auth_profile_pic = null;
                            String auth_category = null;
                            String auth_dob = null;
                            String auth_website = null;
                            String auth_id = null;
                            String auth_name = null;
                            JSONObject author_idsJsonObject = jsonObject1.getJSONObject("author_ids");
                            if (author_idsJsonObject.has("id") && !author_idsJsonObject.isNull("id")) {
                                auth_id = author_idsJsonObject.getString("id");
                            }
                            if (author_idsJsonObject.has("name") && !author_idsJsonObject.isNull("name")) {
                                auth_name = author_idsJsonObject.getString("name");
                            }
                            if (author_idsJsonObject.has("email") && !author_idsJsonObject.isNull("email")) {
                                auth_email = author_idsJsonObject.getString("email");
                            }
                            if (author_idsJsonObject.has("mobile") && !author_idsJsonObject.isNull("mobile")) {
                                auth_mobile = author_idsJsonObject.getString("mobile");
                            }
                            if (author_idsJsonObject.has("gender") && !author_idsJsonObject.isNull("gender")) {
                                auth_gender = author_idsJsonObject.getString("gender");
                            }
                            if (author_idsJsonObject.has("alternateMobile") && !author_idsJsonObject.isNull("alternateMobile")) {
                                auth_alternateMobile = author_idsJsonObject.getString("alternateMobile");
                            }
                            if (author_idsJsonObject.has("experience") && !author_idsJsonObject.isNull("experience")) {
                                auth_experience = author_idsJsonObject.getString("experience");
                            }
                            if (author_idsJsonObject.has("profile_pic") && !author_idsJsonObject.isNull("profile_pic")) {
                                auth_profile_pic = author_idsJsonObject.getString("profile_pic");
                            }
                            if (author_idsJsonObject.has("category") && !author_idsJsonObject.isNull("category")) {
                                auth_category = author_idsJsonObject.getString("category");
                            }
                            if (author_idsJsonObject.has("dob") && !author_idsJsonObject.isNull("dob")) {
                                auth_dob = author_idsJsonObject.getString("dob");
                            }
                            if (author_idsJsonObject.has("website") && !author_idsJsonObject.isNull("website")) {
                                auth_website = author_idsJsonObject.getString("website");
                            }
                            author_detail_module = new Author_detail_Module(auth_id, auth_name, auth_email, auth_mobile, auth_alternateMobile, auth_gender, auth_experience, auth_profile_pic, auth_category, auth_dob, auth_website);
                        }
                        coursesModels.add(new CoursesModel(id, parent_id, name, description, created_by, created_at, updated_by, updated_at, status, type, doc_type, author_detail_module, banner));
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return coursesModels;
    }
}
