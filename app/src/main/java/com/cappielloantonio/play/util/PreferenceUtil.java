package com.cappielloantonio.play.util;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.cappielloantonio.play.helper.ThemeHelper;

public class PreferenceUtil {
    private static final String TAG = "PreferenceUtil";

    public static final String SERVER = "server";
    public static final String USER = "user";
    public static final String PASSWORD = "password";
    public static final String TOKEN = "token";
    public static final String SALT = "salt";
    public static final String SERVER_ID = "server_id";
    public static final String POSITION = "position";
    public static final String PROGRESS = "progress";
    public static final String IMAGE_CACHE_SIZE = "image_cache_size";
    public static final String MEDIA_CACHE_SIZE = "media_cache_size";

    private static PreferenceUtil sInstance;
    private final SharedPreferences mPreferences;

    private PreferenceUtil(final Context context) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static PreferenceUtil getInstance(final Context context) {
        if (sInstance == null) {
            sInstance = new PreferenceUtil(context);
        }

        return sInstance;
    }

    public String getTheme() {
        return mPreferences.getString("themePref", ThemeHelper.DEFAULT_MODE);
    }

    public String getServer() {
        return mPreferences.getString(SERVER, "https://jellyfin.org");
    }

    public void setServer(String server) {
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(SERVER, server);
        editor.apply();
    }

    public String getUser() {
        return mPreferences.getString(USER, null);
    }

    public void setUser(String user) {
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(USER, user);
        editor.apply();
    }

    public String getPassword() {
        return mPreferences.getString(PASSWORD, null);
    }

    public void setPassword(String password) {
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(PASSWORD, password);
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

    public String getSalt() {
        return mPreferences.getString(SALT, null);
    }

    public void setSalt(String salt) {
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(SALT, salt);
        editor.apply();
    }

    public String getServerId() {
        return mPreferences.getString(SERVER_ID, null);
    }

    public void setServerId(String serverId) {
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(SERVER_ID, serverId);
        editor.apply();
    }

    public int getPosition() {
        return mPreferences.getInt(POSITION, -1);
    }

    public void setPosition(int position) {
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(POSITION, position);
        editor.apply();
    }

    public int getProgress() {
        return mPreferences.getInt(PROGRESS, -1);
    }

    public void setProgress(int progress) {
        final SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(PROGRESS, progress);
        editor.apply();
    }

    public final int getImageCacheSize() {
        return Integer.parseInt(mPreferences.getString(IMAGE_CACHE_SIZE, "400000000"));
    }

    public final int getMediaCacheSize() {
        return Integer.parseInt(mPreferences.getString(MEDIA_CACHE_SIZE, "400000000"));
    }
}