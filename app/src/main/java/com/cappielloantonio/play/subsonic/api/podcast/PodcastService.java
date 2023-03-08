package com.cappielloantonio.play.subsonic.api.podcast;

import com.cappielloantonio.play.subsonic.base.ApiResponse;
import com.cappielloantonio.play.subsonic.models.SubsonicResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface PodcastService {
    @GET("getPodcasts")
    Call<ApiResponse> getPodcasts(@QueryMap Map<String, String> params, @Query("includeEpisodes") boolean includeEpisodes, @Query("id") String id);

    @GET("getNewestPodcasts")
    Call<ApiResponse> getNewestPodcasts(@QueryMap Map<String, String> params, @Query("count") int count);

    @GET("refreshPodcasts")
    Call<ApiResponse> refreshPodcasts(@QueryMap Map<String, String> params);
}
