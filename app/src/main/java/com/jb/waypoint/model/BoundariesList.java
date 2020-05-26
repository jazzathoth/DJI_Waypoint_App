package com.jb.waypoint.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BoundariesList {
    @SerializedName("values")
    private ArrayList<Boundaries> boundariesArrayList;

    public ArrayList<Boundaries> getBoundariesArrayList() {
        return boundariesArrayList;
    }
    public void setBoundariesArrayList (ArrayList<Boundaries> boundariesArrayList) {
        this.boundariesArrayList = boundariesArrayList;
    }
}
