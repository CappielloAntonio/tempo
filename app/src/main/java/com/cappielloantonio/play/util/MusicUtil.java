package com.cappielloantonio.play.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.Html;
import android.util.Log;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.R;
import com.cappielloantonio.play.glide.CustomGlideRequest;
import com.cappielloantonio.play.model.Song;
import com.google.android.exoplayer2.MediaItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

public class MusicUtil {
    private static final String TAG = "MusicUtil";

    public static String getSongStreamUri(Context context, Song song) {
        Map<String, String> params = App.getSubsonicClientInstance(App.getInstance(), false).getParams();

        return App.getSubsonicClientInstance(App.getInstance(), false).getUrl() +
                "stream" +
                "?u=" + params.get("u") +
                "&s=" + params.get("s") +
                "&t=" + params.get("t") +
                "&v=" + params.get("v") +
                "&c=" + params.get("c") +
                "&id=" + song.getId() +
                "&maxBitRate=" + getBitratePreference(context) +
                "&format=" + getFormatPreference(context);
    }

    public static MediaItem getSongDownloadItem(Song song) {
        Map<String, String> params = App.getSubsonicClientInstance(App.getInstance(), false).getParams();

        String uri = App.getSubsonicClientInstance(App.getInstance(), false).getUrl() +
                "stream" +
                "?u=" + params.get("u") +
                "&s=" + params.get("s") +
                "&t=" + params.get("t") +
                "&v=" + params.get("v") +
                "&c=" + params.get("c") +
                "&id=" + song.getId();

        return MediaItem.fromUri(uri);
    }

    public static String getReadableDurationString(long duration, boolean millis) {
        long minutes;
        long seconds;

        if (millis) {
            minutes = (duration / 1000) / 60;
            seconds = (duration / 1000) % 60;
        } else {
            minutes = duration / 60;
            seconds = duration % 60;
        }

        if (minutes < 60) {
            return String.format(Locale.getDefault(), "%01d:%02d", minutes, seconds);
        } else {
            long hours = minutes / 60;
            minutes = minutes % 60;
            return String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, seconds);
        }
    }

    public static String getReadableString(String string) {
        if (string != null) {
            return Html.fromHtml(string, Html.FROM_HTML_MODE_COMPACT).toString();
        }

        return "";
    }

    public static String forceReadableString(String string) {
        if (string != null) {
            return getReadableString(string)
                    .replaceAll("&#34;", "\"")
                    .replaceAll("&#39;", "'")
                    .replaceAll("&amp;", "'")
                    .replaceAll("<a[\\s]+([^>]+)>((?:.(?!</a>))*.)</a>", "");
        }

        return "";
    }

    public static String normalizedArtistName(String string) {
        if (string != null) {
            if (string.toLowerCase().contains(" feat.")) return Pattern.compile(" feat.", Pattern.CASE_INSENSITIVE).split(string)[0].trim();
            else if (string.toLowerCase().contains(" featuring")) return Pattern.compile(" featuring", Pattern.CASE_INSENSITIVE).split(string)[0].trim();
            else return string;
        }

        return "";
    }

    public static List<String> getReadableStrings(List<String> strings) {
        List<String> readableStrings = new ArrayList<>();

        if (strings.size() > 0) {
            for (String string : strings) {
                if (string != null) {
                    readableStrings.add(Html.fromHtml(string, Html.FROM_HTML_MODE_COMPACT).toString());
                }
            }
        }

        return readableStrings;
    }

    public static int getDefaultPicPerCategory(String category) {
        switch (category) {
            case CustomGlideRequest.SONG_PIC:
                return R.drawable.default_album_art;
            case CustomGlideRequest.ALBUM_PIC:
                return R.drawable.default_album_art;
            case CustomGlideRequest.ARTIST_PIC:
                return R.drawable.default_album_art;
            case CustomGlideRequest.PLAYLIST_PIC:
                return R.drawable.default_album_art;
            default:
                return R.drawable.default_album_art;
        }
    }

    private static String getBitratePreference(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        switch (connectivityManager.getActiveNetworkInfo().getType()) {
            case ConnectivityManager.TYPE_WIFI:
                return PreferenceUtil.getInstance(context).getMaxBitrateWifi();
            case ConnectivityManager.TYPE_MOBILE:
                return PreferenceUtil.getInstance(context).getMaxBitrateMobile();
            default:
                return PreferenceUtil.getInstance(context).getMaxBitrateWifi();
        }
    }

    private static String getFormatPreference(Context context) {
        return PreferenceUtil.getInstance(context).getAudioTranscodeFormat();
    }
}
