package com.jb.waypoint.interfaces;

import com.jb.waypoint.model.OrganizationsList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;

public interface GetOrganizationsInterface {
    @Headers("Accept: application/vnd.deere.axiom.v3+json")
    @GET("/platform/organizations")
    Call<OrganizationsList> getOrganizationData(@Header("Authorization") String token);
}
