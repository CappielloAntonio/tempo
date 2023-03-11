package com.cappielloantonio.play.subsonic.api.searching;

import android.util.Log;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.subsonic.RetrofitClient;
import com.cappielloantonio.play.subsonic.Subsonic;
import com.cappielloantonio.play.subsonic.base.ApiResponse;
import com.cappielloantonio.play.subsonic.utils.CacheUtil;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
