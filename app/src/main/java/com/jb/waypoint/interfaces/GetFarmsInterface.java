package com.jb.waypoint.interfaces;

import com.jb.waypoint.model.FarmsList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface GetFarmsInterface {
    @Headers("Accept: application/vnd.deere.axiom.v3+json")
    @GET("platform/organizations/{orgid}/clients/{clid}")
    Call<FarmsList> getOrgAndClientData(@Query("orgid") String orgId,
                                        @Query("clid") String clId,
                                        @Header("Authorization") String token);
}
