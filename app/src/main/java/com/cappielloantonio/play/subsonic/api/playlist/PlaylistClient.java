package com.cappielloantonio.play.subsonic.api.playlist;

import android.util.Log;

import com.cappielloantonio.play.subsonic.Subsonic;
import com.cappielloantonio.play.subsonic.models.SubsonicResponse;
import com.tickaroo.tikxml.TikXml;
import com.tickaroo.tikxml.converter.htmlescape.HtmlEscapeStringConverter;
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;

public class PlaylistClient {
    private static final String TAG = "BrowsingClient";

    private Subsonic subsonic;
    private Retrofit retrofit;
    private PlaylistService playlistService;

    public PlaylistClient(Subsonic subsonic) {
        this.subsonic = subsonic;

        this.retrofit = new Retrofit.Builder()
                .baseUrl(subsonic.getUrl())
                .addConverterFactory(TikXmlConverterFactory.create(getParser()))
                .client(getOkHttpClient())
                .build();

        this.playlistService = retrofit.create(PlaylistService.class);
    }

    public Call<SubsonicResponse> getPlaylists() {
        Log.d(TAG, "getPlaylists()");
        return playlistService.getPlaylists(subsonic.getParams());
    }

    public Call<SubsonicResponse> getPlaylist(String id) {
        Log.d(TAG, "getPlaylist()");
        return playlistService.getPlaylist(subsonic.getParams(), id);
    }

    private TikXml getParser() {
        return new TikXml.Builder()
                .addTypeConverter(String.class, new HtmlEscapeStringConverter()) // HtmlEscapeStringConverter encode / decode html characters. This class ships as optional dependency
                .build();
    }

    private OkHttpClient getOkHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(getHttpLoggingInterceptor())
                .build();
    }

    private HttpLoggingInterceptor getHttpLoggingInterceptor() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return loggingInterceptor;
    }
}
