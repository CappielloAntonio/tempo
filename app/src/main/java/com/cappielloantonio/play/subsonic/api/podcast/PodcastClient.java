package com.cappielloantonio.play.subsonic.api.podcast;

import android.content.Context;
import android.util.Log;

import com.cappielloantonio.play.subsonic.Subsonic;
import com.cappielloantonio.play.subsonic.models.SubsonicResponse;
import com.cappielloantonio.play.subsonic.utils.CacheUtil;
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;

public class PodcastClient {
    private static final String TAG = "SystemClient";

    private final Context context;
    private final Subsonic subsonic;
    private final PodcastService podcastService;

    public PodcastClient(Context context, Subsonic subsonic) {
        this.context = context;
        this.subsonic = subsonic;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(subsonic.getUrl())
                .addConverterFactory(TikXmlConverterFactory.create())
                .client(getOkHttpClient())
                .build();

        this.podcastService = retrofit.create(PodcastService.class);
    }

    public Call<SubsonicResponse> getPodcasts(boolean includeEpisodes, String channelId) {
        Log.d(TAG, "getPodcasts()");
        return podcastService.getPodcasts(subsonic.getParams(), includeEpisodes, channelId);
    }

    public Call<SubsonicResponse> getNewestPodcasts(int count) {
        Log.d(TAG, "getNewestPodcasts()");
        return podcastService.getNewestPodcasts(subsonic.getParams(), count);
    }

    public Call<SubsonicResponse> refreshPodcasts() {
        Log.d(TAG, "refreshPodcasts()");
        return podcastService.refreshPodcasts(subsonic.getParams());
    }
    private OkHttpClient getOkHttpClient() {
        CacheUtil cacheUtil = new CacheUtil(context, 60, 60 * 60 * 24 * 30);

        return new OkHttpClient.Builder()
                .callTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
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
        return context != null ? new Cache(context.getCacheDir(), cacheSize) : null;
    }
}
