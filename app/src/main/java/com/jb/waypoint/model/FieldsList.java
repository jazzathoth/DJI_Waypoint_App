package com.jb.waypoint.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FieldsList {
    @SerializedName("values")
    private ArrayList<Fields> fieldsArrayList;

    public ArrayList<Fields> getFieldsArrayList() {
        return fieldsArrayList;
    }

    public void setFieldsArrayList(ArrayList<Fields> fieldsArrayList) {
        this.fieldsArrayList = fieldsArrayList;
    }
}
