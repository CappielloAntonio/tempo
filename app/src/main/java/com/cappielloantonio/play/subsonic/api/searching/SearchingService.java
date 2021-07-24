package com.cappielloantonio.play.subsonic.api.searching;

import com.cappielloantonio.play.subsonic.models.SubsonicResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface SearchingService {
    @GET("search2?query={query}")
    Call<SubsonicResponse> search2(@QueryMap Map<String, String> params, String query);

    @GET("search3?query={query}")
    Call<SubsonicResponse> search3(@QueryMap Map<String, String> params, String query);
}
