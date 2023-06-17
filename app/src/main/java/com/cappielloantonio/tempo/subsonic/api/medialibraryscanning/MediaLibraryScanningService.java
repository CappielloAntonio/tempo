package com.cappielloantonio.tempo.subsonic.api.medialibraryscanning;

import com.cappielloantonio.tempo.subsonic.base.ApiResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface MediaLibraryScanningService {
    @GET("startScan")
    Call<ApiResponse> startScan(@QueryMap Map<String, String> params);

    @GET("getScanStatus")
    Call<ApiResponse> getScanStatus(@QueryMap Map<String, String> params);
}
