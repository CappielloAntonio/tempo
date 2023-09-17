package com.cappielloantonio.tempo.subsonic.api.sharing;

import com.cappielloantonio.tempo.subsonic.base.ApiResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface SharingService {
    @GET("getShares")
    Call<ApiResponse> getShares(@QueryMap Map<String, String> params);

    @GET("createShare")
    Call<ApiResponse> createShare(@QueryMap Map<String, String> params, @Query("id") String id, @Query("description") String description, @Query("expires") Long expires);

    @GET("updateShare")
    Call<ApiResponse> updateShare(@QueryMap Map<String, String> params, @Query("id") String id, @Query("description") String description, @Query("expires") Long expires);

    @GET("deleteShare")
    Call<ApiResponse> deleteShare(@QueryMap Map<String, String> params, @Query("id") String id);
}
