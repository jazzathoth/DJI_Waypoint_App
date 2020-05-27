package com.jb.waypoint.interfaces;

import com.jb.waypoint.model.FarmsList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface GetFieldsInterface {
    @Headers("Accept: application/vnd.deere.axiom.v3+json")
    @GET("platform/organizations/{orgid}/farms/{farmid}")
    Call<FarmsList> getOrgAndFarmData(@Query("orgid") String orgId,
                                      @Query("farmid") String farmId,
                                      @Header("Authorization") String token);
}
