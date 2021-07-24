package com.cappielloantonio.play.subsonic.api.mediaretrieval;

import com.cappielloantonio.play.subsonic.models.SubsonicResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface MediaRetrievalService {
    @GET("stream?id={id}?maxBitRate={maxBitRate}?format={format}")
    Call<SubsonicResponse> stream(@QueryMap Map<String, String> params, String id, int maxBitRate, String format);

    @GET("download?id={id}")
    Call<SubsonicResponse> download(@QueryMap Map<String, String> params, String id);
}
