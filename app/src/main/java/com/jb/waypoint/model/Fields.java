package com.jb.waypoint.model;

import com.google.gson.annotations.SerializedName;

public class Fields {
    @SerializedName("name")
    private String name;
    @SerializedName("id")
    private String id;

    public Fields(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
