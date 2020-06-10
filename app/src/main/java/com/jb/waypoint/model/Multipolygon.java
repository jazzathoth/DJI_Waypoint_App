package com.jb.waypoint.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Multipolygon {

    @SerializedName("rings")
    @Expose
    private ArrayList<Ring> rings;

    public Multipolygon(ArrayList<Ring> rings) {
        this.rings = rings;
    }

    public ArrayList<Ring> getRings() {
        return rings;
    }

    public void setRings(ArrayList<Ring> rings) {
        this.rings = rings;
    }
}
