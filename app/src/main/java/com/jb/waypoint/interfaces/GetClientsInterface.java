package com.jb.waypoint.interfaces;

import com.jb.waypoint.model.ClientsBase;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface GetClientsInterface {
    @Headers("Accept: application/vnd.deere.axiom.v3+json")
    @GET("platform/organizations/{orgid}/clients")
    Call<ClientsBase> getOrganizationData(@Path("orgid") String orgId,
                                          @Header("Authorization") String auth);
}
