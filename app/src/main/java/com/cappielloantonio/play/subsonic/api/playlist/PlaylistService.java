package com.cappielloantonio.play.subsonic.api.playlist;

import com.cappielloantonio.play.subsonic.models.SubsonicResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface PlaylistService {
    @GET("getPlaylists")
    Call<SubsonicResponse> getPlaylists(@QueryMap Map<String, String> params);

    @GET("getPlaylist")
    Call<SubsonicResponse> getPlaylist(@QueryMap Map<String, String> params, @Query("id") String id);
}
