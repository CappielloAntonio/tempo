package com.cappielloantonio.tempo.util;

import androidx.annotation.OptIn;
import androidx.media3.common.Metadata;
import androidx.media3.common.Tracks;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.ExoPlayer;

import com.cappielloantonio.tempo.model.ReplayGain;

import java.util.ArrayList;
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
            for (int i = 0; i < tracks.getGroups().size(); i++) {
                Tracks.Group group = tracks.getGroups().get(i);

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
            for (int i = 0; i < metadata.size(); i++) {
                Metadata singleMetadata = metadata.get(i);

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

        return gains;
    }

    private static boolean checkReplayGain(Metadata.Entry entry) {
        for (String tag : tags) {
            if (entry.toString().contains(tag)) {
                return true;
            }
        }

        return false;
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
        if (Objects.equals(Preferences.getReplayGainMode(), "disabled") || gains.size() == 0) {
            setReplayGain(player, 0f);
        } else if (Objects.equals(Preferences.getReplayGainMode(), "track")) {
            setReplayGain(player, gains.get(0).getTrackGain() != 0f ? gains.get(0).getTrackGain() : gains.get(0).getAlbumGain());
        } else if (Objects.equals(Preferences.getReplayGainMode(), "album")) {
            setReplayGain(player, gains.get(0).getAlbumGain() != 0f ? gains.get(0).getAlbumGain() : gains.get(0).getTrackGain());
        }
    }

    private static void setReplayGain(ExoPlayer player, float gain) {
        player.setVolume((float) Math.pow(10f, gain / 20f));
    }
}
