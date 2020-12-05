package com.cappielloantonio.play.util;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.cappielloantonio.play.helper.ThemeHelper;

public class PreferenceUtil {
    public static final String SERVER = "server";
    public static final String USER = "user";
    public static final String TOKEN = "token";
    public static final String MUSIC_LIBRARY_ID = "music_library_id";

    public static final String SYNC = "sync";
    public static final String SONG_GENRE_SYNC = "song_genre_sync";

    public static final String HOST_URL = "host";
    public static final String IMAGE_CACHE_SIZE = "image_cache_size";

    private static PreferenceUtil sInstance;

    private final SharedPreferences mPreferences;

    private PreferenceUtil(final Context context) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static PreferenceUtil getInstance(final Context context) {
        if (sInstance == null) {
            sInstance = new PreferenceUtil(context.getApplicationContext());
        }

        return sInstance;
    }

    public String getTheme() { return mPreferences.getString("themePref", ThemeHelper.DEFAULT_MODE ); }

    public String getServer() {
        return mPreferences.getString(SERVER, "https://jellyfin.org");
    }

    public void setServer(String server) {
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(SERVER, server);
        editor.apply();
    }

    public String getUser() {
        return mPreferences.getString(USER, "");
    }

    public void setUser(String user) {
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(USER, user);
        editor.apply();
    }

    public String getToken() {
        return mPreferences.getString(TOKEN, null);
    }

    public void setToken(String token) {
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(TOKEN, token);
        editor.apply();
    }

    public Boolean getSync() {
        return mPreferences.getBoolean(SYNC, false);
    }

    public void setSync(Boolean sync) {
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(SYNC, sync);
        editor.apply();
    }

    public Boolean getSongGenreSync() {
        return mPreferences.getBoolean(SONG_GENRE_SYNC, false);
    }

    public void setSongGenreSync(Boolean sync) {
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(SONG_GENRE_SYNC, sync);
        editor.apply();
    }

    public String getMusicLibraryID() {
        return mPreferences.getString(MUSIC_LIBRARY_ID, "");
    }

    public void setMusicLibraryID(String musicLibraryID) {
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(MUSIC_LIBRARY_ID, musicLibraryID);
        editor.apply();
    }

    public final String getHostUrl() {
        return mPreferences.getString(HOST_URL, "undefined");
    }

    public final int getImageCacheSize() {
        return Integer.parseInt(mPreferences.getString(IMAGE_CACHE_SIZE, "400000000"));
    }
}