package com.cappielloantonio.tempo.subsonic.api.mediaretrieval;

import com.cappielloantonio.tempo.subsonic.base.ApiResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface MediaRetrievalService {
    @GET("stream")
    Call<ApiResponse> stream(@QueryMap Map<String, String> params, @Query("id") String id, @Query("maxBitRate") Integer maxBitRate, @Query("format") String format);

    @GET("download")
    Call<ApiResponse> download(@QueryMap Map<String, String> params, @Query("id") String id);

    @GET("getLyrics")
    Call<ApiResponse> getLyrics(@QueryMap Map<String, String> params, @Query("artist") String artist, @Query("title") String title);
}
