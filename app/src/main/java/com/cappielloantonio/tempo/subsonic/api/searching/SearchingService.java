package com.cappielloantonio.tempo.subsonic.api.searching;

import com.cappielloantonio.tempo.subsonic.base.ApiResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface SearchingService {
    @GET("search2")
    Call<ApiResponse> search2(@QueryMap Map<String, String> params, @Query("query") String query, @Query("songCount") int songCount, @Query("albumCount") int albumCount, @Query("artistCount") int artistCount);

    @GET("search3")
    Call<ApiResponse> search3(@QueryMap Map<String, String> params, @Query("query") String query, @Query("songCount") int songCount, @Query("albumCount") int albumCount, @Query("artistCount") int artistCount);
}
