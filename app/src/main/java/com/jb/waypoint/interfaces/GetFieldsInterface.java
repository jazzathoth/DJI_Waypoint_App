package com.jb.waypoint.interfaces;

import com.jb.waypoint.model.FieldsList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface GetFieldsInterface {
    @Headers("Accept: application/vnd.deere.axiom.v3+json")
    @GET("platform/organizations/{orgid}/farms/{farmid}/fields")
    Call<FieldsList> getOrgAndFarmData(@Path("orgid") String orgId,
                                       @Path("farmid") String farmId,
                                       @Header("Authorization") String token);
}
