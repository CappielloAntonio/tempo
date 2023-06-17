package com.cappielloantonio.tempo.subsonic.api.searching;

import android.util.Log;

import com.cappielloantonio.tempo.subsonic.RetrofitClient;
import com.cappielloantonio.tempo.subsonic.Subsonic;
import com.cappielloantonio.tempo.subsonic.base.ApiResponse;

import retrofit2.Call;

public class SearchingClient {
    private static final String TAG = "BrowsingClient";

    private final Subsonic subsonic;
    private final SearchingService searchingService;

    public SearchingClient(Subsonic subsonic) {
        this.subsonic = subsonic;
        this.searchingService = new RetrofitClient(subsonic).getRetrofit().create(SearchingService.class);
    }

    public Call<ApiResponse> search2(String query, int songCount, int albumCount, int artistCount) {
        Log.d(TAG, "search2()");
        return searchingService.search2(subsonic.getParams(), query, songCount, albumCount, artistCount);
    }

    public Call<ApiResponse> search3(String query, int songCount, int albumCount, int artistCount) {
        Log.d(TAG, "search3()");
        return searchingService.search3(subsonic.getParams(), query, songCount, albumCount, artistCount);
    }
}
