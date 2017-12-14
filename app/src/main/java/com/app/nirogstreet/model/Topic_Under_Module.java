package com.app.nirogstreet.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Preeti on 12-12-2017.
 */

public class Topic_Under_Module implements Serializable{
int user_completed;

    public int getUser_completed() {
        return user_completed;
    }

    public void setUser_completed(int user_completed) {
        this.user_completed = user_completed;
    }

    public ArrayList<File_Under_Topic> getFile_under_topics() {
        return file_under_topics;
    }

    public void setFile_under_topics(ArrayList<File_Under_Topic> file_under_topics) {
        this.file_under_topics = file_under_topics;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }



    public String getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(String updated_by) {
        this.updated_by = updated_by;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDoc_type() {
        return doc_type;
    }

    public void setDoc_type(String doc_type) {
        this.doc_type = doc_type;
    }

    public Author_detail_Module getAuthor_detail_module() {
        return author_detail_module;
    }

    public void setAuthor_detail_module(Author_detail_Module author_detail_module) {
        this.author_detail_module = author_detail_module;
    }

    String id;
    String parent_id;
    String name;
    String description;
    String banner;

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    String created_by;
    String created_at;
    String updated_by;
    String updated_at;
    String status;
    String type;
    String doc_type;

    public Topic_Under_Module(String id,int user_completed, String parent_id, String name, String description, String banner, String created_by,String created_at, String updated_by, String updated_at, String status, String type, String doc_type, ArrayList<File_Under_Topic> file_under_topics, Author_detail_Module author_detail_module) {
        this.id = id;
        this.parent_id = parent_id;
        this.name = name;
        this.user_completed=user_completed;
        this.description = description;
        this.banner = banner;
        this.created_by=created_by;
      this.  created_at = created_at;
        this.updated_by = updated_by;
        this.updated_at = updated_at;
        this.status = status;
        this.type = type;
        this.doc_type = doc_type;
        this.file_under_topics = file_under_topics;
        this.author_detail_module = author_detail_module;
    }

    ArrayList<File_Under_Topic> file_under_topics;
    Author_detail_Module author_detail_module;

}
