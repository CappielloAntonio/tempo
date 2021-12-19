package com.cappielloantonio.play.subsonic.api.mediaretrieval;

import com.cappielloantonio.play.subsonic.models.SubsonicResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface MediaRetrievalService {
    @GET("stream")
    Call<SubsonicResponse> stream(@QueryMap Map<String, String> params, @Query("id") String id, @Query("maxBitRate") Integer maxBitRate, @Query("format") String format);

    @GET("download")
    Call<SubsonicResponse> download(@QueryMap Map<String, String> params, @Query("id") String id);

    @GET("getLyrics")
    Call<SubsonicResponse> getLyrics(@QueryMap Map<String, String> params, @Query("artist") String artist, @Query("title") String title);
}
