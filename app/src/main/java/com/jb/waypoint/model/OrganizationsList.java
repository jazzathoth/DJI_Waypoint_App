package com.jb.waypoint.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class OrganizationsList {
    @SerializedName("values")
    private ArrayList<Organizations> organizationsArrayList;

    public ArrayList<Organizations> getOrganizationsArrayList() {
        return organizationsArrayList;
    }

    public void setOrganizationsArrayList(ArrayList<Organizations> organizationsArrayList) {
        this.organizationsArrayList = organizationsArrayList;
    }
}
