package com.app.nirogstreet.parser;

import com.app.nirogstreet.model.QualificationModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Preeti on 21-09-2017.
 */

public class QualificationParser {

    public static ArrayList<QualificationModel> qualificationParser(JSONObject message) {
        ArrayList<QualificationModel> qualificationModels = new ArrayList<>();
        try {
            if (message.has("qualifications") && !message.isNull("qualifications")) {
                JSONArray qualificationJsonArray = message.getJSONArray("qualifications");
                for (int i = 0; i < qualificationJsonArray.length(); i++) {

                    String clgName = null;
                    String QualificationId = null;
                    String userId = null, course_name = null, type = null, university = null, created_on = null, updated_on = null, updated_by = null, status = null;


                    String degreeName = null;
                    String passingYear = null;
                    String upladedDoc = null;
                    JSONObject qualificationJsonObject = qualificationJsonArray.getJSONObject(i);
                    if (qualificationJsonObject.has("id") && !qualificationJsonObject.isNull("id")) {
                        QualificationId = qualificationJsonObject.getString("id");
                    }
                    if (qualificationJsonObject.has("user_id") && !qualificationJsonObject.isNull("user_id")) {
                        userId = qualificationJsonObject.getString("user_id");
                    }
                    if (qualificationJsonObject.has("degree") && !qualificationJsonObject.isNull("degree")) {
                        degreeName = qualificationJsonObject.getString("degree");
                    }
                    if (qualificationJsonObject.has("course_name") && !qualificationJsonObject.isNull("course_name")) {
                        course_name = qualificationJsonObject.getString("course_name");
                    }
                    if (qualificationJsonObject.has("type") && !qualificationJsonObject.isNull("type")) {
                        type = qualificationJsonObject.getString("type");
                    }
                    if (qualificationJsonObject.has("college") && !qualificationJsonObject.isNull("college")) {
                        clgName = qualificationJsonObject.getString("college");
                    }
                    if (qualificationJsonObject.has("university") && !qualificationJsonObject.isNull("university")) {
                        university = qualificationJsonObject.getString("university");
                    }
                    if (qualificationJsonObject.has("year") && !qualificationJsonObject.isNull("year")) {
                        passingYear = qualificationJsonObject.getString("year");
                    }
                    if (qualificationJsonObject.has("edu_file") && !qualificationJsonObject.isNull("edu_file")) {
                        upladedDoc = qualificationJsonObject.getString("edu_file");
                    }
                    if (qualificationJsonObject.has("createdOn") && !qualificationJsonObject.isNull("createdOn")) {
                        created_on = qualificationJsonObject.getString("createdOn");
                    }
                    if (qualificationJsonObject.has("updatedOn") && !qualificationJsonObject.isNull("updatedOn")) {
                        updated_on = qualificationJsonObject.getString("updatedOn");
                    }
                    if (qualificationJsonObject.has("updated_by") && !qualificationJsonObject.isNull("updated_by")) {
                        updated_by = qualificationJsonObject.getString("updated_by");
                    }
                    if (qualificationJsonObject.has("status") && !qualificationJsonObject.isNull("status")) {
                        status = qualificationJsonObject.getString("status");
                    }
                    qualificationModels.add(new QualificationModel(clgName, QualificationId, userId, course_name, type, university, created_on, updated_on, updated_by, status, degreeName, passingYear, upladedDoc));
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return qualificationModels;
    }
}
