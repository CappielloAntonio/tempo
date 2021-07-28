package com.cappielloantonio.play.subsonic.api.albumsonglist;

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

public class AlbumSongListClient {
    private static final String TAG = "BrowsingClient";

    private Subsonic subsonic;
    private Retrofit retrofit;
    private AlbumSongListService albumSongListService;

    public AlbumSongListClient(Subsonic subsonic) {
        this.subsonic = subsonic;

        this.retrofit = new Retrofit.Builder()
                .baseUrl(subsonic.getUrl())
                .addConverterFactory(TikXmlConverterFactory.create(getParser()))
                .client(getOkHttpClient())
                .build();

        this.albumSongListService = retrofit.create(AlbumSongListService.class);
    }

    public Call<SubsonicResponse> getAlbumList(String type, int size, int offset) {
        Log.d(TAG, "getAlbumList()");
        return albumSongListService.getAlbumList(subsonic.getParams(), type, size, offset);
    }

    public Call<SubsonicResponse> getAlbumList2(String type, int size, int offset) {
        Log.d(TAG, "getAlbumList2()");
        return albumSongListService.getAlbumList2(subsonic.getParams(), type, size, offset);
    }

    public Call<SubsonicResponse> getRandomSongs(int size) {
        Log.d(TAG, "getRandomSongs()");
        return albumSongListService.getRandomSongs(subsonic.getParams(), size);
    }

    public Call<SubsonicResponse> getSongsByGenre(String genre, int count, int offset) {
        Log.d(TAG, "getSongsByGenre()");
        return albumSongListService.getSongsByGenre(subsonic.getParams(), genre, count, offset);
    }

    public Call<SubsonicResponse> getNowPlaying() {
        Log.d(TAG, "getNowPlaying()");
        return albumSongListService.getNowPlaying(subsonic.getParams());
    }

    public Call<SubsonicResponse> getStarred() {
        Log.d(TAG, "getStarred()");
        return albumSongListService.getStarred(subsonic.getParams());
    }

    public Call<SubsonicResponse> getStarred2() {
        Log.d(TAG, "getStarred2()");
        return albumSongListService.getStarred2(subsonic.getParams());
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
