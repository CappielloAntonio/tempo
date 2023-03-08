package com.cappielloantonio.play.subsonic.api.albumsonglist;

import com.cappielloantonio.play.subsonic.base.ApiResponse;
import com.cappielloantonio.play.subsonic.models.SubsonicResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface AlbumSongListService {
    @GET("getAlbumList")
    Call<ApiResponse> getAlbumList(@QueryMap Map<String, String> params, @Query("type") String type, @Query("size") int size, @Query("offset") int offset);

    @GET("getAlbumList2")
    Call<ApiResponse> getAlbumList2(@QueryMap Map<String, String> params, @Query("type") String type, @Query("size") int size, @Query("offset") int offset, @Query("fromYear") Integer fromYear, @Query("toYear") Integer toYear);

    @GET("getRandomSongs")
    Call<ApiResponse> getRandomSongs(@QueryMap Map<String, String> params, @Query("size") int size, @Query("fromYear") Integer fromYear, @Query("toYear") Integer toYear);

    @GET("getSongsByGenre")
    Call<ApiResponse> getSongsByGenre(@QueryMap Map<String, String> params, @Query("genre") String genre, @Query("count") int count, @Query("offset") int offset);

    @GET("getNowPlaying")
    Call<ApiResponse> getNowPlaying(@QueryMap Map<String, String> params);

    @GET("getStarred")
    Call<ApiResponse> getStarred(@QueryMap Map<String, String> params);

    @GET("getStarred2")
    Call<ApiResponse> getStarred2(@QueryMap Map<String, String> params);
}
