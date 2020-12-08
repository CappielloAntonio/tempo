package com.cappielloantonio.play.util;

import android.util.Log;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.model.DirectPlayCodec;
import com.cappielloantonio.play.model.Song;

import org.jellyfin.apiclient.interaction.ApiClient;

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
}
