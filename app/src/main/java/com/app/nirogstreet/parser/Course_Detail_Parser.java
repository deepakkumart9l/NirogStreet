package com.app.nirogstreet.parser;

import com.app.nirogstreet.model.Author_detail_Module;
import com.app.nirogstreet.model.Course_Detail_model;
import com.app.nirogstreet.model.CoursesModel;
import com.app.nirogstreet.model.File_Under_Topic;
import com.app.nirogstreet.model.ModulesModel;
import com.app.nirogstreet.model.Topic_Under_Module;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Preeti on 12-12-2017.
 */

public class Course_Detail_Parser {
    public static Course_Detail_model course_detail_Parser(JSONObject jsonObject) {
        Course_Detail_model course_detail_model = null;
        try {
            if (jsonObject != null) {
                if (jsonObject.has("response") && !jsonObject.isNull("response")) {
                    JSONObject responseJsonObject = jsonObject.getJSONObject("response");
                    if (responseJsonObject.has("courseDetail") && !responseJsonObject.isNull("courseDetail")) {
                        int user_completed = -1;
                        String id = null, parent_id = null, name = null, banner = null, description = null, created_by = null, created_at = null, updated_by = null, updated_at = null, status = null, type = null, doc_type = null;
                        Author_detail_Module author_detail_module = null;
                        ArrayList<ModulesModel> modulesModels = new ArrayList<>();
                        JSONObject courseDetailJsonObject = responseJsonObject.getJSONObject("courseDetail");
                        if (courseDetailJsonObject.has("id") && !courseDetailJsonObject.isNull("id")) {
                            id = courseDetailJsonObject.getString("id");
                        }
                        if (courseDetailJsonObject.has("user_completed") && !courseDetailJsonObject.isNull("user_completed")) {
                            user_completed = courseDetailJsonObject.getInt("user_completed");
                        }
                        if (courseDetailJsonObject.has("parent_id") && !courseDetailJsonObject.isNull("parent_id")) {
                            parent_id = courseDetailJsonObject.getString("parent_id");
                        }
                        if (courseDetailJsonObject.has("name") && !courseDetailJsonObject.isNull("name")) {
                            name = courseDetailJsonObject.getString("name");
                        }
                        if (courseDetailJsonObject.has("description") && !courseDetailJsonObject.isNull("description")) {
                            description = courseDetailJsonObject.getString("description");
                        }
                        if (courseDetailJsonObject.has("banner") && !courseDetailJsonObject.isNull("banner")) {
                            banner = courseDetailJsonObject.getString("banner");
                        }
                        if (courseDetailJsonObject.has("created_by") && !courseDetailJsonObject.isNull("created_by")) {
                            created_by = courseDetailJsonObject.getString("created_by");
                        }
                        if (courseDetailJsonObject.has("created_at") && !courseDetailJsonObject.isNull("created_at")) {
                            created_at = courseDetailJsonObject.getString("created_at");
                        }
                        if (courseDetailJsonObject.has("updated_by") && !courseDetailJsonObject.isNull("updated_by")) {
                            updated_by = courseDetailJsonObject.getString("updated_by");
                        }
                        if (courseDetailJsonObject.has("status") && !courseDetailJsonObject.isNull("status")) {
                            status = courseDetailJsonObject.getString("status");
                        }
                        if (courseDetailJsonObject.has("type") && !courseDetailJsonObject.isNull("type")) {
                            type = courseDetailJsonObject.getString("type");
                        }
                        if (courseDetailJsonObject.has("doc_type") && !courseDetailJsonObject.isNull("doc_type")) {
                            doc_type = courseDetailJsonObject.getString("doc_type");
                        }
                        if (courseDetailJsonObject.has("author_ids") && !courseDetailJsonObject.isNull("author_ids")) {
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
                            JSONObject author_idsJsonObject = courseDetailJsonObject.getJSONObject("author_ids");
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
                        if (courseDetailJsonObject.has("moduleData") && !courseDetailJsonObject.isNull("moduleData")) {
                            JSONArray jsonArray = courseDetailJsonObject.getJSONArray("moduleData");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                String moduleId = null, modulebanner = null, moduleParent_id = null, module_name = null, module_description = null;
                                ArrayList<Topic_Under_Module> topic_under_modules = new ArrayList<>();
                                int moduleuser_completed = -1;
                                JSONObject moduleJsonObject = jsonArray.getJSONObject(i);
                                if (moduleJsonObject.has("id") && !moduleJsonObject.isNull("moduleData")) {
                                    moduleId = moduleJsonObject.getString("id");
                                }
                                if (moduleJsonObject.has("user_completed") && !moduleJsonObject.isNull("user_completed")) {
                                    moduleuser_completed = moduleJsonObject.getInt("user_completed");
                                }
                                if (moduleJsonObject.has("parent_id") && !moduleJsonObject.isNull("parent_id")) {
                                    moduleParent_id = moduleJsonObject.getString("parent_id");
                                }
                                if (moduleJsonObject.has("description") && !moduleJsonObject.isNull("description")) {
                                    module_description = moduleJsonObject.getString("description");
                                }
                                if (moduleJsonObject.has("name") && !moduleJsonObject.isNull("name")) {
                                    module_name = moduleJsonObject.getString("name");
                                }
                                if (moduleJsonObject.has("banner") && !moduleJsonObject.isNull("banner")) {
                                    modulebanner = moduleJsonObject.getString("banner");
                                }
                                if (moduleJsonObject.has("moduleData") && !moduleJsonObject.isNull("moduleData")) {
                                    JSONArray topicJsonArray = moduleJsonObject.getJSONArray("moduleData");
                                    for (int k = 0; k < topicJsonArray.length(); k++) {
                                        int topicuser_completed = -1;
                                        String topicParent_id = null, topic_name = null, topic_description = null, topic_banner = null, topic_created_by = null, topic_created_at = null, topic_updated_by = null, topic_updated_at = null, topic_status = null, topic_doc_type = null, topic_type = null;
                                        String topicId = null;
                                        JSONObject topicJsonObject = topicJsonArray.getJSONObject(k);

                                        if (topicJsonObject.has("id") && !topicJsonObject.isNull("id")) {
                                            topicId = topicJsonObject.getString("id");
                                        }

                                        if (topicJsonObject.has("user_completed") && !topicJsonObject.isNull("user_completed")) {
                                            topicuser_completed = topicJsonObject.getInt("user_completed");
                                        }
                                        if (topicJsonObject.has("parent_id") && !topicJsonObject.isNull("parent_id")) {
                                            topicParent_id = topicJsonObject.getString("parent_id");
                                        }
                                        if (topicJsonObject.has("name") && !topicJsonObject.isNull("name")) {
                                            topic_name = topicJsonObject.getString("name");
                                        }
                                        if (topicJsonObject.has("description") && !topicJsonObject.isNull("description")) {
                                            topic_description = topicJsonObject.getString("description");
                                        }
                                        if (topicJsonObject.has("banner") && !topicJsonObject.isNull("banner")) {
                                            topic_banner = topicJsonObject.getString("banner");
                                        }
                                        if (topicJsonObject.has("created_by") && !topicJsonObject.isNull("created_by")) {
                                            topic_created_by = topicJsonObject.getString("created_by");
                                        }
                                        if (topicJsonObject.has("created_at") && !topicJsonObject.isNull("created_at")) {
                                            topic_created_at = topicJsonObject.getString("created_at");
                                        }
                                        if (topicJsonObject.has("updated_by") && !topicJsonObject.isNull("updated_by")) {
                                            topic_updated_by = topicJsonObject.getString("updated_by");
                                        }
                                        if (topicJsonObject.has("updated_at") && !topicJsonObject.isNull("updated_at")) {
                                            topic_updated_at = topicJsonObject.getString("updated_at");
                                        }
                                        if (topicJsonObject.has("status") && !topicJsonObject.isNull("status")) {
                                            topic_status = topicJsonObject.getString("status");
                                        }
                                        if (topicJsonObject.has("type") && !topicJsonObject.isNull("type")) {
                                            topic_status = topicJsonObject.getString("type");
                                        }
                                        if (topicJsonObject.has("doc_type") && !topicJsonObject.isNull("doc_type")) {
                                            topic_doc_type = topicJsonObject.getString("doc_type");
                                        }
                                        ArrayList<File_Under_Topic> file_under_topics = new ArrayList<>();
                                        if (topicJsonObject.has("moduleData") && !topicJsonObject.isNull("moduleData")) {
                                            JSONArray filesJsonArray = topicJsonObject.getJSONArray("moduleData");
                                            for (int l = 0; l < filesJsonArray.length(); l++) {
                                                String file_id = null, file_parent_id = null, file_name = null, file_description = null, file_banner = null, file_kc_file = null, filecreated_by = null, filecreated_at = null, fileupdated_by = null, fileupdated_at = null, filestatus = null, filetype = null, filedoc_type = null;
                                                int fileuser_completed=-1;
                                                String root_id=null;


                                                JSONObject fileJsonObject = filesJsonArray.getJSONObject(l);
                                                if (fileJsonObject.has("id") && !fileJsonObject.isNull("id")) {
                                                    file_id = fileJsonObject.getString("id");
                                                }
                                                if (fileJsonObject.has("user_completed") && !fileJsonObject.isNull("user_completed")) {
                                                    fileuser_completed = fileJsonObject.getInt("user_completed");
                                                }
                                                if (fileJsonObject.has("parent_id") && !fileJsonObject.isNull("parent_id")) {
                                                    file_parent_id = fileJsonObject.getString("parent_id");
                                                }
                                                if(fileJsonObject.has("root_id")&&!fileJsonObject.isNull("root_id"))
                                                {
                                                    root_id=fileJsonObject.getString("root_id");
                                                }
                                                if (fileJsonObject.has("name") && !fileJsonObject.isNull("name")) {
                                                    file_name = fileJsonObject.getString("name");
                                                }
                                                if (fileJsonObject.has("description") && !fileJsonObject.isNull("description")) {
                                                    file_description = fileJsonObject.getString("description");
                                                }
                                                if (fileJsonObject.has("banner") && !fileJsonObject.isNull("banner")) {
                                                    file_banner = fileJsonObject.getString("banner");
                                                }
                                                if (fileJsonObject.has("description") && !fileJsonObject.isNull("description")) {
                                                    file_description = fileJsonObject.getString("description");
                                                }
                                                if (fileJsonObject.has("kc_file") && !fileJsonObject.isNull("kc_file")) {
                                                    file_kc_file = fileJsonObject.getString("kc_file");
                                                }
                                                if (fileJsonObject.has("updated_by") && !fileJsonObject.isNull("updated_by")) {
                                                    fileupdated_by = fileJsonObject.getString("updated_by");
                                                }
                                                if (fileJsonObject.has("created_by") && !fileJsonObject.isNull("created_by")) {
                                                    filecreated_by = fileJsonObject.getString("created_by");
                                                }
                                                if (fileJsonObject.has("created_at") && !fileJsonObject.isNull("created_at")) {
                                                    filecreated_at = fileJsonObject.getString("created_at");
                                                }
                                                if (fileJsonObject.has("updated_at") && !fileJsonObject.isNull("updated_at")) {
                                                    fileupdated_by = fileJsonObject.getString("updated_at");
                                                }
                                                if (fileJsonObject.has("status") && !fileJsonObject.isNull("status")) {
                                                    filestatus = fileJsonObject.getString("status");
                                                }
                                                if (fileJsonObject.has("type") && !fileJsonObject.isNull("type")) {
                                                    filetype = fileJsonObject.getString("type");
                                                }
                                                if (fileJsonObject.has("doc_type") && !fileJsonObject.isNull("doc_type")) {
                                                    filedoc_type = fileJsonObject.getString("doc_type");
                                                }
                                                file_under_topics.add(new File_Under_Topic(file_id,fileuser_completed, file_parent_id, file_name, file_description, file_banner, file_kc_file, filecreated_by, filecreated_at, fileupdated_by, fileupdated_at, filestatus, filedoc_type, filedoc_type,root_id));
                                            }
                                        }
                                        topic_under_modules.add(new Topic_Under_Module(topicId, topicuser_completed, topicParent_id, topic_name, topic_description, topic_banner, topic_created_by, topic_created_at, topic_updated_by, topic_updated_at, topic_status, topic_type, topic_doc_type, file_under_topics, author_detail_module));
                                    }
                                    modulesModels.add(new ModulesModel(moduleId, moduleuser_completed, module_name, module_description, topic_under_modules));
                                }
                            }
                        }
                        course_detail_model = new Course_Detail_model(id, user_completed, parent_id, name, description, created_by, created_at, updated_by, updated_at, status, type, doc_type, author_detail_module, banner, modulesModels);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return course_detail_model;
    }
}
