package com.cappielloantonio.tempo.subsonic.api.system;

import com.cappielloantonio.tempo.subsonic.base.ApiResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface SystemService {
    @GET("ping")
    Call<ApiResponse> ping(@QueryMap Map<String, String> params);

    @GET("getLicense")
    Call<ApiResponse> getLicense(@QueryMap Map<String, String> params);

    @GET("getOpenSubsonicExtensions")
    Call<ApiResponse> getOpenSubsonicExtensions(@QueryMap Map<String, String> params);
}
