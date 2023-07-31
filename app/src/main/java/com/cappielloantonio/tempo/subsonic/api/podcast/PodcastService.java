package com.cappielloantonio.tempo.subsonic.api.podcast;

import com.cappielloantonio.tempo.subsonic.base.ApiResponse;

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

    @GET("createPodcastChannel")
    Call<ApiResponse> createPodcastChannel(@QueryMap Map<String, String> params, @Query("url") String url);

    @GET("deletePodcastChannel")
    Call<ApiResponse> deletePodcastChannel(@QueryMap Map<String, String> params, @Query("id") String id);

    @GET("deletePodcastEpisode")
    Call<ApiResponse> deletePodcastEpisode(@QueryMap Map<String, String> params, @Query("id") String id);

    @GET("downloadPodcastEpisode")
    Call<ApiResponse> downloadPodcastEpisode(@QueryMap Map<String, String> params, @Query("id") String id);
}
