package com.cappielloantonio.play.subsonic.api.albumsonglist;

import com.cappielloantonio.play.subsonic.models.SubsonicResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface AlbumSongListService {
    @GET("getAlbumList")
    Call<SubsonicResponse> getAlbumList(@QueryMap Map<String, String> params, @Query("type") String type, @Query("size") int size, @Query("offset") int offset);

    @GET("getAlbumList2")
    Call<SubsonicResponse> getAlbumList2(@QueryMap Map<String, String> params, @Query("type") String type, @Query("size") int size, @Query("offset") int offset);

    @GET("getRandomSongs")
    Call<SubsonicResponse> getRandomSongs(@QueryMap Map<String, String> params, @Query("size") int size);

    @GET("getSongsByGenre")
    Call<SubsonicResponse> getSongsByGenre(@QueryMap Map<String, String> params, @Query("genre") String genre, @Query("count") int count, @Query("offset") int offset);

    @GET("getNowPlaying")
    Call<SubsonicResponse> getNowPlaying(@QueryMap Map<String, String> params);

    @GET("getStarred")
    Call<SubsonicResponse> getStarred(@QueryMap Map<String, String> params);

    @GET("getStarred2")
    Call<SubsonicResponse> getStarred2(@QueryMap Map<String, String> params);
}
