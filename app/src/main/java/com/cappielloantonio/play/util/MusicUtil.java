package com.cappielloantonio.play.util;

import android.content.Context;
import android.util.Log;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.R;
import com.cappielloantonio.play.glide.CustomGlideRequest;
import com.cappielloantonio.play.model.DirectPlayCodec;
import com.cappielloantonio.play.model.Song;
import com.google.android.exoplayer2.MediaItem;

import org.jellyfin.apiclient.interaction.ApiClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MusicUtil {
    public static String getSongFileUri(Song song) {
        ApiClient apiClient = App.getApiClientInstance(App.getInstance());
        PreferenceUtil preferenceUtil = PreferenceUtil.getInstance(App.getInstance());

        StringBuilder builder = new StringBuilder(256);
        builder.append(apiClient.getApiUrl());
        builder.append("/Audio/");
        builder.append(song.getId());
        builder.append("/universal");
        builder.append("?UserId=").append(apiClient.getCurrentUserId());
        builder.append("&DeviceId=").append(apiClient.getDeviceId());

        // web client maximum is 12444445 and 320kbps is 320000
        builder.append("&MaxStreamingBitrate=").append(preferenceUtil.getMaximumBitrate());

        boolean containerAdded = false;
        for (DirectPlayCodec directPlayCodec : preferenceUtil.getDirectPlayCodecs()) {
            if (directPlayCodec.selected) {
                if (!containerAdded) {
                    builder.append("&Container=");
                    containerAdded = true;
                }

                builder.append(directPlayCodec.codec.value).append(',');
            }
        }

        if (containerAdded) {
            // remove last comma
            builder.deleteCharAt(builder.length() - 1);
        }

        builder.append("&TranscodingContainer=ts");
        builder.append("&TranscodingProtocol=hls");

        // preferred codec when transcoding
        builder.append("&AudioCodec=").append(preferenceUtil.getTranscodeCodec());
        builder.append("&api_key=").append(apiClient.getAccessToken());

        Log.i(MusicUtil.class.getName(), "playing audio: " + builder);
        return builder.toString();
    }

    public static String getReadableDurationString(long songDurationMillis) {
        long minutes = (songDurationMillis / 1000) / 60;
        long seconds = (songDurationMillis / 1000) % 60;

        if (minutes < 60) {
            return String.format(Locale.getDefault(), "%01d:%02d", minutes, seconds);
        } else {
            long hours = minutes / 60;
            minutes = minutes % 60;
            return String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, seconds);
        }
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

    public static List<MediaItem> getMediaItemsFromSongs(List<Song> songs) {
        List<MediaItem> mediaItems = new ArrayList<>();

        for (Song song : songs) {
            mediaItems.add(getMediaItemFromSong(song));
        }

        return mediaItems;
    }

    public static MediaItem getMediaItemFromSong(Song song) {
        String uri = MusicUtil.getSongFileUri(song);
        MediaItem mediaItem = MediaItem.fromUri(uri);
        return mediaItem;
    }

    public static List<Integer> getRandomSongNumber(Context context, int numberOfNumbers, int refreshAfterXHours) {
        List<Integer> list = new ArrayList<>();

        for (int i = 0; i < numberOfNumbers; i++) {
            list.add(getRandomNumber(0, PreferenceUtil.getInstance(context).getSongNumber(), getMidnightTimestamp(System.currentTimeMillis() / 1000, refreshAfterXHours) + i));
        }

        return list;
    }

    private static long getMidnightTimestamp(long timestamp, int hours) {
        return timestamp - timestamp % (hours * 60 * 60); // 24 * 60 * 60 sec in one day
    }

    private static int getRandomNumber(int min, int max, long seed) {
        return new Random(seed).nextInt((max - min) + 1) + min;
    }
}
