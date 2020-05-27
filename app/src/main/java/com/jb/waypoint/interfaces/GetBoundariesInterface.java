package com.jb.waypoint.interfaces;

import com.jb.waypoint.model.BoundariesList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface GetBoundariesInterface {
    @Headers("Accept: application/vnd.deere.axiom.v3+json")
    @GET("platform/organizations/{orgid}/fields/{fieldid}/boundaries")
    Call<BoundariesList> getOrgAndFieldData(@Query("orgid") String orgId,
                                            @Query("fieldid") String fieldId,
                                            @Header("Authorization") String token);
}
