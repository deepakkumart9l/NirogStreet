package com.app.nirogstreet.model;

import java.io.Serializable;

/**
 * Created by Preeti on 07-09-2017.
 */

public class MemberShipModel implements Serializable{
    String id;

    public String getMembership() {
        return membership;
    }

    public void setMembership(String membership) {
        this.membership = membership;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String membership;

    public MemberShipModel(String id, String membership) {
        this.id = id;
        this.membership = membership;
    }
}
