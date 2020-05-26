package com.jb.waypoint.interfaces;

import com.jb.waypoint.model.OrganizationsList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetOrganizationsInterface {
    @GET("/platform/organizations")
    Call<OrganizationsList> getOrganizationData();
}
