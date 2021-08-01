package com.cappielloantonio.play.util;

import android.text.Html;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.R;
import com.cappielloantonio.play.glide.CustomGlideRequest;
import com.cappielloantonio.play.model.Song;
import com.google.android.exoplayer2.MediaItem;

import java.util.Locale;
import java.util.Map;

public class MusicUtil {
    private static final String TAG = "MusicUtil";

    public static String getSongFileUri(Song song) {
        String url = App.getSubsonicClientInstance(App.getInstance(), false).getUrl();

        Map<String, String> params = App.getSubsonicClientInstance(App.getInstance(), false).getParams();

        return url + "stream" +
                "?u=" + params.get("u") +
                "&s=" + params.get("s") +
                "&t=" + params.get("t") +
                "&v=" + params.get("v") +
                "&c=" + params.get("c") +
                "&id=" + song.getId();
    }

    public static String getReadableDurationString(long duration, boolean millis) {
        long minutes = 0;
        long seconds = 0;

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

    public static String getReadableInfo(String info) {
        if (info != null) {
            return Html.fromHtml(info, Html.FROM_HTML_MODE_COMPACT).toString();
        }

        return "";
    }

    public static int getDefaultPicPerCategory(String category) {
        if (category.equals(CustomGlideRequest.SONG_PIC)) {
            return R.drawable.default_album_art;
        } else if (category.equals(CustomGlideRequest.ALBUM_PIC)) {
            return R.drawable.default_album_art;
        } else if (category.equals(CustomGlideRequest.ARTIST_PIC)) {
            return R.drawable.default_album_art;
        } else if (category.equals(CustomGlideRequest.PLAYLIST_PIC)) {
            return R.drawable.default_album_art;
        } else {
            return R.drawable.default_album_art;
        }
    }

    public static MediaItem getMediaItemFromSong(Song song) {
        String uri = MusicUtil.getSongFileUri(song);
        MediaItem mediaItem = MediaItem.fromUri(uri);
        return mediaItem;
    }
}
