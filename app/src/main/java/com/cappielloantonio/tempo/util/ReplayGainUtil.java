package com.cappielloantonio.tempo.util;

import androidx.annotation.OptIn;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Metadata;
import androidx.media3.common.Tracks;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.ExoPlayer;

import com.cappielloantonio.tempo.model.ReplayGain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@OptIn(markerClass = UnstableApi.class)
public class ReplayGainUtil {
    private static final String[] tags = {"REPLAYGAIN_TRACK_GAIN", "REPLAYGAIN_ALBUM_GAIN", "R128_TRACK_GAIN", "R128_ALBUM_GAIN"};

    public static void setReplayGain(ExoPlayer player, Tracks tracks) {
        List<Metadata> metadata = getMetadata(tracks);
        List<ReplayGain> gains = getReplayGains(metadata);

        applyReplayGain(player, gains);
    }

    private static List<Metadata> getMetadata(Tracks tracks) {
        List<Metadata> metadata = new ArrayList<>();

        if (tracks != null && !tracks.getGroups().isEmpty()) {
            for (Tracks.Group group : tracks.getGroups()) {
                if (group != null && group.getMediaTrackGroup() != null) {
                    for (int j = 0; j < group.getMediaTrackGroup().length; j++) {
                        metadata.add(group.getTrackFormat(j).metadata);
                    }
                }
            }
        }

        return metadata;
    }

    private static List<ReplayGain> getReplayGains(List<Metadata> metadata) {
        List<ReplayGain> gains = new ArrayList<>();

        if (metadata != null) {
            for (Metadata singleMetadata : metadata) {

                if (singleMetadata != null) {
                    for (int j = 0; j < singleMetadata.length(); j++) {
                        Metadata.Entry entry = singleMetadata.get(j);

                        if (checkReplayGain(entry)) {
                            ReplayGain replayGain = setReplayGains(entry);
                            gains.add(replayGain);
                        }
                    }
                }
            }
        }

        if (gains.size() == 0) gains.add(0, new ReplayGain());
        if (gains.size() == 1) gains.add(1, new ReplayGain());

        return gains;
    }

    private static boolean checkReplayGain(Metadata.Entry entry) {
        return Arrays.stream(tags).anyMatch(entry.toString()::contains);
    }

    private static ReplayGain setReplayGains(Metadata.Entry entry) {
        ReplayGain replayGain = new ReplayGain();

        if (entry.toString().contains(tags[0])) {
            replayGain.setTrackGain(parseReplayGainTag(entry));
        }

        if (entry.toString().contains(tags[1])) {
            replayGain.setAlbumGain(parseReplayGainTag(entry));
        }

        if (entry.toString().contains(tags[2])) {
            replayGain.setTrackGain(parseReplayGainTag(entry) / 256f);
        }

        if (entry.toString().contains(tags[3])) {
            replayGain.setAlbumGain(parseReplayGainTag(entry) / 256f);
        }

        return replayGain;
    }

    private static Float parseReplayGainTag(Metadata.Entry entry) {
        try {
            return Float.parseFloat(entry.toString().replaceAll("[^\\d.]", ""));
        } catch (NumberFormatException exception) {
            return 0f;
        }
    }

    private static void applyReplayGain(ExoPlayer player, List<ReplayGain> gains) {
        if (Objects.equals(Preferences.getReplayGainMode(), "disabled") || gains == null || gains.isEmpty()) {
            setNoReplayGain(player);
            return;
        }

        if (Objects.equals(Preferences.getReplayGainMode(), "auto")) {
            if (areTracksConsecutive(player)) {
                setAutoReplayGain(player, gains);
            } else {
                setTrackReplayGain(player, gains);
            }

            return;
        }

        if (Objects.equals(Preferences.getReplayGainMode(), "track")) {
            setTrackReplayGain(player, gains);
            return;
        }

        if (Objects.equals(Preferences.getReplayGainMode(), "album")) {
            setAlbumReplayGain(player, gains);
            return;
        }

        setNoReplayGain(player);
    }

    private static void setNoReplayGain(ExoPlayer player) {
        setReplayGain(player, 0f);
    }

    private static void setTrackReplayGain(ExoPlayer player, List<ReplayGain> gains) {
        float trackGain = gains.get(0).getTrackGain() != 0f ? gains.get(0).getTrackGain() : gains.get(1).getTrackGain();

        setReplayGain(player, trackGain);
    }

    private static void setAlbumReplayGain(ExoPlayer player, List<ReplayGain> gains) {
        float albumGain = gains.get(0).getAlbumGain() != 0f ? gains.get(0).getAlbumGain() : gains.get(1).getAlbumGain();

        setReplayGain(player, albumGain);
    }

    private static void setAutoReplayGain(ExoPlayer player, List<ReplayGain> gains) {
        float albumGain = gains.get(0).getAlbumGain() != 0f ? gains.get(0).getAlbumGain() : gains.get(1).getAlbumGain();
        float trackGain = gains.get(0).getTrackGain() != 0f ? gains.get(0).getTrackGain() : gains.get(1).getTrackGain();

        setReplayGain(player, albumGain != 0f ? albumGain : trackGain);
    }

    private static boolean areTracksConsecutive(ExoPlayer player) {
        MediaItem currentMediaItem = player.getCurrentMediaItem();
        int currentMediaItemIndex = player.getCurrentMediaItemIndex();
        MediaItem pastMediaItem = currentMediaItemIndex > 0 ? player.getMediaItemAt(currentMediaItemIndex - 1) : null;

        return currentMediaItem != null &&
                pastMediaItem != null &&
                pastMediaItem.mediaMetadata.albumTitle != null &&
                currentMediaItem.mediaMetadata.albumTitle != null &&
                pastMediaItem.mediaMetadata.albumTitle.toString().equals(currentMediaItem.mediaMetadata.albumTitle.toString());
    }

    private static void setReplayGain(ExoPlayer player, float gain) {
        player.setVolume((float) Math.pow(10f, gain / 20f));
    }
}
