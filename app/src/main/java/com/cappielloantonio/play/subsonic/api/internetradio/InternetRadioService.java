package com.cappielloantonio.play.subsonic.api.internetradio;

import com.cappielloantonio.play.subsonic.base.ApiResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface InternetRadioService {
    @GET("getInternetRadioStations")
    Call<ApiResponse> getInternetRadioStations(@QueryMap Map<String, String> params);

    @GET("createInternetRadioStation")
    Call<ApiResponse> createInternetRadioStation(@QueryMap Map<String, String> params, @Query("streamUrl") String streamUrl, @Query("name") String name, @Query("homepageUrl") String homepageUrl);

    @GET("updateInternetRadioStation")
    Call<ApiResponse> updateInternetRadioStation(@QueryMap Map<String, String> params, @Query("id") String id, @Query("streamUrl") String streamUrl, @Query("name") String name, @Query("homepageUrl") String homepageUrl);

    @GET("deleteInternetRadioStation")
    Call<ApiResponse> deleteInternetRadioStation(@QueryMap Map<String, String> params, @Query("id") String id);
}
