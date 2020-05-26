package com.jb.waypoint.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ClientsList {
    @SerializedName("values")
    private ArrayList<Clients> clientsArrayList;

    public ArrayList<Clients> getClientsArrayList() {
        return clientsArrayList;
    }

    public void setClientsArrayList(ArrayList<Clients> clientsArrayList) {
        this.clientsArrayList = clientsArrayList;
    }
}
