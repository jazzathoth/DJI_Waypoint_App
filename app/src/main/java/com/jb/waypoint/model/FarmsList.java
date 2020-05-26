package com.jb.waypoint.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FarmsList {
    @SerializedName("values")
    private ArrayList<Farms> farmsArrayList;

    public ArrayList<Farms> getFarmsArrayList() {
        return farmsArrayList;
    }

    public void setFarmsArrayList(ArrayList<Farms> farmsArrayList) {
        this.farmsArrayList = farmsArrayList;
    }
}
