package com.cappielloantonio.play.subsonic.api.mediaannotation;

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

public class MediaAnnotationClient {
    private static final String TAG = "BrowsingClient";

    private final Context context;
    private final Subsonic subsonic;
    private Retrofit retrofit;
    private final MediaAnnotationService mediaAnnotationService;

    public MediaAnnotationClient(Context context, Subsonic subsonic) {
        this.context = context;
        this.subsonic = subsonic;

        this.retrofit = new Retrofit.Builder()
                .baseUrl(subsonic.getUrl())
                .addConverterFactory(TikXmlConverterFactory.create())
                .client(getOkHttpClient())
                .build();

        this.mediaAnnotationService = retrofit.create(MediaAnnotationService.class);
    }

    public Call<SubsonicResponse> star(String id, String albumId, String artistId) {
        Log.d(TAG, "star()");
        return mediaAnnotationService.star(subsonic.getParams(), id, albumId, artistId);
    }

    public Call<SubsonicResponse> unstar(String id, String albumId, String artistId) {
        Log.d(TAG, "unstar()");
        return mediaAnnotationService.unstar(subsonic.getParams(), id, albumId, artistId);
    }

    public Call<SubsonicResponse> setRating(String id, int rating) {
        Log.d(TAG, "setRating()");
        return mediaAnnotationService.setRating(subsonic.getParams(), id, rating);
    }

    public Call<SubsonicResponse> scrobble(String id) {
        Log.d(TAG, "scrobble()");
        return mediaAnnotationService.scrobble(subsonic.getParams(), id);
    }

    private OkHttpClient getOkHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(getHttpLoggingInterceptor())
                .addInterceptor(CacheUtil.offlineInterceptor)
                .addNetworkInterceptor(CacheUtil.onlineInterceptor)
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
