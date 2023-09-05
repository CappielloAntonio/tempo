package com.cappielloantonio.tempo.interfaces;


import android.os.Bundle;

import androidx.annotation.Keep;

@Keep
public interface ClickCallback {
    default void onMediaClick(Bundle bundle) {}
    default void onMediaLongClick(Bundle bundle) {}
    default void onAlbumClick(Bundle bundle) {}
    default void onAlbumLongClick(Bundle bundle) {}
    default void onArtistClick(Bundle bundle) {}
    default void onArtistLongClick(Bundle bundle) {}
    default void onGenreClick(Bundle bundle) {}
    default void onPlaylistClick(Bundle bundle) {}
    default void onPlaylistLongClick(Bundle bundle) {}
    default void onYearClick(Bundle bundle) {}
    default void onServerClick(Bundle bundle) {}
    default void onServerLongClick(Bundle bundle) {}
    default void onPodcastEpisodeClick(Bundle bundle) {}
    default void onPodcastEpisodeAltClick(Bundle bundle) {}
    default void onPodcastEpisodeLongClick(Bundle bundle) {}
    default void onPodcastChannelClick(Bundle bundle) {}
    default void onPodcastChannelLongClick(Bundle bundle) {}
    default void onInternetRadioStationClick(Bundle bundle) {}
    default void onInternetRadioStationLongClick(Bundle bundle) {}
    default void onMusicFolderClick(Bundle bundle) {}
    default void onMusicDirectoryClick(Bundle bundle) {}
    default void onMusicIndexClick(Bundle bundle) {}
    default void onDownloadGroupLongClick(Bundle bundle) {}
}
