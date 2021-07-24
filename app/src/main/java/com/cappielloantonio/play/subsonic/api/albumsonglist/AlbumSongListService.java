package com.cappielloantonio.play.subsonic.api.albumsonglist;

import com.cappielloantonio.play.subsonic.models.SubsonicResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface AlbumSongListService {
    @GET("getAlbumList?type=random")
    Call<SubsonicResponse> getAlbumList(@QueryMap Map<String, String> params);

    @GET("getAlbumList2?type=random")
    Call<SubsonicResponse> getAlbumList2(@QueryMap Map<String, String> params);

    @GET("getRandomSongs?size={size}")
    Call<SubsonicResponse> getRandomSongs(@QueryMap Map<String, String> params, int size);

    @GET("getSongsByGenre?genre={genre}?count={count}")
    Call<SubsonicResponse> getSongsByGenre(@QueryMap Map<String, String> params, String genre, int count);

    @GET("getNowPlaying")
    Call<SubsonicResponse> getNowPlaying(@QueryMap Map<String, String> params);

    @GET("getStarred")
    Call<SubsonicResponse> getStarred(@QueryMap Map<String, String> params);

    @GET("getStarred2")
    Call<SubsonicResponse> getStarred2(@QueryMap Map<String, String> params);
}
