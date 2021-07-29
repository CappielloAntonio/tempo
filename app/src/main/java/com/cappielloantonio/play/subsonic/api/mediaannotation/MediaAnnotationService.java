package com.cappielloantonio.play.subsonic.api.mediaannotation;

import com.cappielloantonio.play.subsonic.models.SubsonicResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface MediaAnnotationService {
    @GET("star")
    Call<SubsonicResponse> star(@QueryMap Map<String, String> params, @Query("id") String id, @Query("albumId") String albumId, @Query("artistId") String artistId);

    @GET("unstar")
    Call<SubsonicResponse> unstar(@QueryMap Map<String, String> params, @Query("id") String id, @Query("albumId") String albumId, @Query("artistId") String artistId);

    @GET("setRating")
    Call<SubsonicResponse> setRating(@QueryMap Map<String, String> params, @Query("id") String id, @Query("star") int star);

    @GET("scrobble")
    Call<SubsonicResponse> scrobble(@QueryMap Map<String, String> params, @Query("id") String id);
}
