package com.app.nirogstreet.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Preeti on 12-12-2017.
 */

public class ModulesModel implements Serializable{
    public int getUser_completed() {
        return user_completed;
    }

    public void setUser_completed(int user_completed) {
        this.user_completed = user_completed;
    }

    public  int user_completed;
    public ArrayList<Topic_Under_Module> getTopic_under_modules() {
        return topic_under_modules;
    }

    public void setTopic_under_modules(ArrayList<Topic_Under_Module> topic_under_modules) {
        this.topic_under_modules = topic_under_modules;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public ModulesModel(String id,int user_completed, String name, String description, ArrayList<Topic_Under_Module> topic_under_modules) {
        this.id = id;
        this.name = name;
        this.user_completed=user_completed;
        this.description = description;
        this.topic_under_modules = topic_under_modules;
    }

    String id,name,description;

    ArrayList<Topic_Under_Module> topic_under_modules;
}
