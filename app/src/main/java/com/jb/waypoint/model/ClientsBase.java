package com.jb.waypoint.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ClientsBase {
    @SerializedName("values")
    private ArrayList<ClientsValues> clientsValuesArrayList;

    public ArrayList<ClientsValues> getClientsValuesArrayList() {
        return clientsValuesArrayList;
    }

    public void setClientsValuesArrayList(ArrayList<ClientsValues> clientsValuesArrayList) {
        this.clientsValuesArrayList = clientsValuesArrayList;
    }
}
