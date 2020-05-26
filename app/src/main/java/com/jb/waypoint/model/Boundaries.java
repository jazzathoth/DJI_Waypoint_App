package com.jb.waypoint.model;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

public class Boundaries {
    @SerializedName("name")
    private String name;
    @SerializedName("created")
    private String created;
    @SerializedName("multipolygons")
    private JSONObject boundary;

    public Boundaries(String name, String created, JSONObject boundary) {
        this.name = name;
        this.created = created;
        this.boundary = boundary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public JSONObject getBoundary() {
        return boundary;
    }

    public void setBoundary(JSONObject boundary) {
        this.boundary = boundary;
    }
}
