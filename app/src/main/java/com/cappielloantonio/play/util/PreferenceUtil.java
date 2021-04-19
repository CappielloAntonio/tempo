package com.cappielloantonio.play.util;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.cappielloantonio.play.helper.ThemeHelper;
import com.cappielloantonio.play.model.DirectPlayCodec;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PreferenceUtil {
    public static final String SERVER = "server";
    public static final String USER = "user";
    public static final String TOKEN = "token";
    public static final String MUSIC_LIBRARY_ID = "music_library_id";

    public static final String POSITION = "position";
    public static final String PROGRESS = "progress";

    public static final String SYNC = "sync";
    public static final String SONG_GENRE_SYNC = "song_genre_sync";

    public static final String IMAGE_CACHE_SIZE = "image_cache_size";
    public static final String MEDIA_CACHE_SIZE = "media_cache_size";
    public static final String INSTANT_MIX_SONG_NUMBER = "instant_mix_song_number";

    public static final String TRANSCODE_CODEC = "transcode_codec";
    public static final String DIRECT_PLAY_CODECS = "direct_play_codecs";
    public static final String MAXIMUM_BITRATE = "maximum_bitrate";
    public static final String AUDIO_DUCKING = "audio_ducking";

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

    public final int getImageCacheSize() {
        return Integer.parseInt(mPreferences.getString(IMAGE_CACHE_SIZE, "400000000"));
    }

    public final int getInstantMixSongNumber() {
        return Integer.parseInt(mPreferences.getString(INSTANT_MIX_SONG_NUMBER, "10"));
    }

    public final String getTranscodeCodec() {
        return mPreferences.getString(TRANSCODE_CODEC, "aac");
    }

    public final String getMaximumBitrate() {
        return mPreferences.getString(MAXIMUM_BITRATE, "10000000");
    }

    public List<DirectPlayCodec> getDirectPlayCodecs() {
        DirectPlayCodec.Codec[] codecs = DirectPlayCodec.Codec.values();

        Set<String> selectedCodecNames = new HashSet<>();
        for (DirectPlayCodec.Codec codec : codecs) {
            // this will be the default value
            selectedCodecNames.add(codec.name());
        }

        selectedCodecNames = mPreferences.getStringSet(DIRECT_PLAY_CODECS, selectedCodecNames);

        ArrayList<DirectPlayCodec> directPlayCodecs = new ArrayList<>();
        for (DirectPlayCodec.Codec codec : codecs) {
            String name = codec.name();
            boolean selected = selectedCodecNames.contains(name);
            directPlayCodecs.add(new DirectPlayCodec(codec, selected));
        }

        return directPlayCodecs;
    }

    public final int getMediaCacheSize() {
        return Integer.parseInt(mPreferences.getString(MEDIA_CACHE_SIZE, "400000000"));
    }

    public final boolean getAudioDucking() {
        return mPreferences.getBoolean(AUDIO_DUCKING, true);
    }
}