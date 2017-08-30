package com.app.nirogstreet.model;

import java.io.Serializable;

/**
 * Created by Preeti on 30-08-2017.
 */

public class ServicesModel implements Serializable {
    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String service;

    public ServicesModel(String service, String id) {
        this.service = service;
        this.id = id;
    }

    String id;
}
