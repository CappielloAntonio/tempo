package com.cappielloantonio.play.subsonic.api.system;

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

public class SystemClient {
    private static final String TAG = "SystemClient";

    private final Subsonic subsonic;
    private final SystemService systemService;

    public SystemClient(Subsonic subsonic) {
        this.subsonic = subsonic;
        this.systemService = new RetrofitClient(subsonic).getRetrofit().create(SystemService.class);
    }

    public Call<ApiResponse> ping() {
        Log.d(TAG, "ping()");
        return systemService.ping(subsonic.getParams());
    }

    public Call<ApiResponse> getLicense() {
        Log.d(TAG, "getLicense()");
        return systemService.getLicense(subsonic.getParams());
    }
}
