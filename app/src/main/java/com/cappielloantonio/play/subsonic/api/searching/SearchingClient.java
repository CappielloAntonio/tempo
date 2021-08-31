package com.cappielloantonio.play.subsonic.api.searching;

import android.content.Context;
import android.util.Log;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.subsonic.Subsonic;
import com.cappielloantonio.play.subsonic.models.SubsonicResponse;
import com.cappielloantonio.play.subsonic.utils.CacheUtil;
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;

public class SearchingClient {
    private static final String TAG = "BrowsingClient";

    private final Context context;
    private final Subsonic subsonic;
    private Retrofit retrofit;
    private final SearchingService searchingService;

    public SearchingClient(Context context, Subsonic subsonic) {
        this.context = context;
        this.subsonic = subsonic;

        this.retrofit = new Retrofit.Builder()
                .baseUrl(subsonic.getUrl())
                .addConverterFactory(TikXmlConverterFactory.create())
                .client(getOkHttpClient())
                .build();

        this.searchingService = retrofit.create(SearchingService.class);
    }

    public Call<SubsonicResponse> search2(String query, int songCount, int albumCount, int artistCount) {
        Log.d(TAG, "search2()");
        return searchingService.search2(subsonic.getParams(), query, songCount, albumCount, artistCount);
    }

    public Call<SubsonicResponse> search3(String query, int songCount, int albumCount, int artistCount) {
        Log.d(TAG, "search3()");
        return searchingService.search3(subsonic.getParams(), query, songCount, albumCount, artistCount);
    }

    private OkHttpClient getOkHttpClient() {
        CacheUtil cacheUtil = new CacheUtil(context);

        return new OkHttpClient.Builder()
                .addInterceptor(getHttpLoggingInterceptor())
                .addInterceptor(cacheUtil.offlineInterceptor)
                .addNetworkInterceptor(cacheUtil.onlineInterceptor)
                .cache(getCache())
                .build();
    }

    private HttpLoggingInterceptor getHttpLoggingInterceptor() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return loggingInterceptor;
    }

    private Cache getCache() {
        int cacheSize = 10 * 1024 * 1024;
        return context != null ? new Cache(context.getCacheDir(), cacheSize) : null;    }
}
