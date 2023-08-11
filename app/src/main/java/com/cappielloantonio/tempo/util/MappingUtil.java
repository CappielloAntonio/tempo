package com.cappielloantonio.tempo.util;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.OptIn;
import androidx.media3.common.MediaItem;
import androidx.media3.common.MediaMetadata;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.UnstableApi;

import com.cappielloantonio.tempo.App;
import com.cappielloantonio.tempo.subsonic.models.Child;
import com.cappielloantonio.tempo.subsonic.models.InternetRadioStation;
import com.cappielloantonio.tempo.subsonic.models.PodcastEpisode;

import java.util.ArrayList;
import java.util.List;

@OptIn(markerClass = UnstableApi.class)
public class MappingUtil {
    public static List<MediaItem> mapMediaItems(List<Child> items) {
        ArrayList<MediaItem> mediaItems = new ArrayList<>();

        for (int i = 0; i < items.size(); i++) {
            mediaItems.add(mapMediaItem(items.get(i)));
        }

        return mediaItems;
    }

    public static MediaItem mapMediaItem(Child media) {
        Uri uri = getUri(media);

        Bundle bundle = new Bundle();
        bundle.putString("id", media.getId());
        bundle.putString("parentId", media.getParentId());
        bundle.putBoolean("isDir", media.isDir());
        bundle.putString("title", media.getTitle());
        bundle.putString("album", media.getAlbum());
        bundle.putString("artist", media.getArtist());
        bundle.putInt("track", media.getTrack() != null ? media.getTrack() : 0);
        bundle.putInt("year", media.getYear() != null ? media.getYear() : 0);
        bundle.putString("genre", media.getGenre());
        bundle.putString("coverArtId", media.getCoverArtId());
        bundle.putLong("size", media.getSize() != null ? media.getSize() : 0);
        bundle.putString("contentType", media.getContentType());
        bundle.putString("suffix", media.getSuffix());
        bundle.putString("transcodedContentType", media.getTranscodedContentType());
        bundle.putString("transcodedSuffix", media.getTranscodedSuffix());
        bundle.putInt("duration", media.getDuration() != null ? media.getDuration() : 0);
        bundle.putInt("bitrate", media.getBitrate() != null ? media.getBitrate() : 0);
        bundle.putString("path", media.getPath());
        bundle.putBoolean("isVideo", media.isVideo());
        bundle.putInt("userRating", media.getUserRating() != null ? media.getUserRating() : 0);
        bundle.putDouble("averageRating", media.getAverageRating() != null ? media.getAverageRating() : 0);
        bundle.putLong("playCount", media.getPlayCount() != null ? media.getPlayCount() : 0);
        bundle.putInt("discNumber", media.getDiscNumber() != null ? media.getDiscNumber() : 0);
        bundle.putLong("created", media.getCreated() != null ? media.getCreated().getTime() : 0);
        bundle.putLong("starred", media.getStarred() != null ? media.getStarred().getTime() : 0);
        bundle.putString("albumId", media.getAlbumId());
        bundle.putString("artistId", media.getArtistId());
        bundle.putString("type", media.getType());
        bundle.putLong("bookmarkPosition", media.getBookmarkPosition() != null ? media.getBookmarkPosition() : 0);
        bundle.putInt("originalWidth", media.getOriginalWidth() != null ? media.getOriginalWidth() : 0);
        bundle.putInt("originalHeight", media.getOriginalHeight() != null ? media.getOriginalHeight() : 0);
        bundle.putString("uri", uri.toString());

        return new MediaItem.Builder()
                .setMediaId(media.getId())
                .setMediaMetadata(
                        new MediaMetadata.Builder()
                                .setTitle(MusicUtil.getReadableString(media.getTitle()))
                                .setTrackNumber(media.getTrack() != null ? media.getTrack() : 0)
                                .setDiscNumber(media.getDiscNumber() != null ? media.getDiscNumber() : 0)
                                .setReleaseYear(media.getYear() != null ? media.getYear() : 0)
                                .setAlbumTitle(MusicUtil.getReadableString(media.getAlbum()))
                                .setArtist(MusicUtil.getReadableString(media.getArtist()))
                                .setExtras(bundle)
                                .build()
                )
                .setRequestMetadata(
                        new MediaItem.RequestMetadata.Builder()
                                .setMediaUri(uri)
                                .setExtras(bundle)
                                .build()
                )
                .setMimeType(MimeTypes.BASE_TYPE_AUDIO)
                .setUri(uri)
                .build();
    }

    public static List<MediaItem> mapDownloads(List<Child> items) {
        ArrayList<MediaItem> downloads = new ArrayList<>();

        for (int i = 0; i < items.size(); i++) {
            downloads.add(mapDownload(items.get(i)));
        }

        return downloads;
    }

    public static MediaItem mapDownload(Child media) {
        return new MediaItem.Builder()
                .setMediaId(media.getId())
                .setMediaMetadata(
                        new MediaMetadata.Builder()
                                .setTitle(MusicUtil.getReadableString(media.getTitle()))
                                .setTrackNumber(media.getTrack() != null ? media.getTrack() : 0)
                                .setDiscNumber(media.getDiscNumber() != null ? media.getDiscNumber() : 0)
                                .setReleaseYear(media.getYear() != null ? media.getYear() : 0)
                                .setAlbumTitle(MusicUtil.getReadableString(media.getAlbum()))
                                .setArtist(MusicUtil.getReadableString(media.getArtist()))
                                .build()
                )
                .setRequestMetadata(
                        new MediaItem.RequestMetadata.Builder()
                                .setMediaUri(MusicUtil.getDownloadUri(media.getId()))
                                .build()
                )
                .setMimeType(MimeTypes.BASE_TYPE_AUDIO)
                .setUri(MusicUtil.getDownloadUri(media.getId()))
                .build();
    }

    public static MediaItem mapInternetRadioStation(InternetRadioStation internetRadioStation) {
        Uri uri = Uri.parse(internetRadioStation.getStreamUrl());

        Bundle bundle = new Bundle();
        bundle.putString("id", internetRadioStation.getId());
        bundle.putString("title", internetRadioStation.getName());
        bundle.putString("artist", uri.toString());
        bundle.putString("uri", uri.toString());
        bundle.putString("type", Constants.MEDIA_TYPE_RADIO);

        return new MediaItem.Builder()
                .setMediaId(internetRadioStation.getId())
                .setMediaMetadata(
                        new MediaMetadata.Builder()
                                .setTitle(internetRadioStation.getName())
                                .setArtist(internetRadioStation.getStreamUrl())
                                .setExtras(bundle)
                                .build()
                )
                .setRequestMetadata(
                        new MediaItem.RequestMetadata.Builder()
                                .setMediaUri(uri)
                                .setExtras(bundle)
                                .build()
                )
                .setMimeType(MimeTypes.BASE_TYPE_AUDIO)
                .setUri(uri)
                .build();
    }

    public static MediaItem mapMediaItem(PodcastEpisode podcastEpisode) {
        Uri uri = getUri(podcastEpisode);

        Bundle bundle = new Bundle();
        bundle.putString("id", podcastEpisode.getId());
        bundle.putString("parentId", podcastEpisode.getParentId());
        bundle.putBoolean("isDir", podcastEpisode.isDir());
        bundle.putString("title", podcastEpisode.getTitle());
        bundle.putString("album", podcastEpisode.getAlbum());
        bundle.putString("artist", podcastEpisode.getArtist());
        bundle.putInt("track", podcastEpisode.getTrack() != null ? podcastEpisode.getTrack() : 0);
        bundle.putInt("year", podcastEpisode.getYear() != null ? podcastEpisode.getYear() : 0);
        bundle.putString("genre", podcastEpisode.getGenre());
        bundle.putString("coverArtId", podcastEpisode.getCoverArtId());
        bundle.putLong("size", podcastEpisode.getSize() != null ? podcastEpisode.getSize() : 0);
        bundle.putString("contentType", podcastEpisode.getContentType());
        bundle.putString("suffix", podcastEpisode.getSuffix());
        bundle.putString("transcodedContentType", podcastEpisode.getTranscodedContentType());
        bundle.putString("transcodedSuffix", podcastEpisode.getTranscodedSuffix());
        bundle.putInt("duration", podcastEpisode.getDuration() != null ? podcastEpisode.getDuration() : 0);
        bundle.putInt("bitrate", podcastEpisode.getBitrate() != null ? podcastEpisode.getBitrate() : 0);
        bundle.putString("path", podcastEpisode.getPath());
        bundle.putBoolean("isVideo", podcastEpisode.isVideo());
        bundle.putInt("userRating", podcastEpisode.getUserRating() != null ? podcastEpisode.getUserRating() : 0);
        bundle.putDouble("averageRating", podcastEpisode.getAverageRating() != null ? podcastEpisode.getAverageRating() : 0);
        bundle.putLong("playCount", podcastEpisode.getPlayCount() != null ? podcastEpisode.getPlayCount() : 0);
        bundle.putInt("discNumber", podcastEpisode.getDiscNumber() != null ? podcastEpisode.getDiscNumber() : 0);
        bundle.putLong("created", podcastEpisode.getCreated() != null ? podcastEpisode.getCreated().getTime() : 0);
        bundle.putLong("starred", podcastEpisode.getStarred() != null ? podcastEpisode.getStarred().getTime() : 0);
        bundle.putString("albumId", podcastEpisode.getAlbumId());
        bundle.putString("artistId", podcastEpisode.getArtistId());
        bundle.putString("type", podcastEpisode.getType());
        bundle.putLong("bookmarkPosition", podcastEpisode.getBookmarkPosition() != null ? podcastEpisode.getBookmarkPosition() : 0);
        bundle.putInt("originalWidth", podcastEpisode.getOriginalWidth() != null ? podcastEpisode.getOriginalWidth() : 0);
        bundle.putInt("originalHeight", podcastEpisode.getOriginalHeight() != null ? podcastEpisode.getOriginalHeight() : 0);
        bundle.putString("uri", uri.toString());

        return new MediaItem.Builder()
                .setMediaId(podcastEpisode.getId())
                .setMediaMetadata(
                        new MediaMetadata.Builder()
                                .setTitle(MusicUtil.getReadableString(podcastEpisode.getTitle()))
                                .setTrackNumber(podcastEpisode.getTrack() != null ? podcastEpisode.getTrack() : 0)
                                .setDiscNumber(podcastEpisode.getDiscNumber() != null ? podcastEpisode.getDiscNumber() : 0)
                                .setReleaseYear(podcastEpisode.getYear() != null ? podcastEpisode.getYear() : 0)
                                .setAlbumTitle(MusicUtil.getReadableString(podcastEpisode.getAlbum()))
                                .setArtist(MusicUtil.getReadableString(podcastEpisode.getArtist()))
                                .setExtras(bundle)
                                .build()
                )
                .setRequestMetadata(
                        new MediaItem.RequestMetadata.Builder()
                                .setMediaUri(uri)
                                .setExtras(bundle)
                                .build()
                )
                .setMimeType(MimeTypes.BASE_TYPE_AUDIO)
                .setUri(uri)
                .build();
    }

    private static Uri getUri(Child media) {
        return DownloadUtil.getDownloadTracker(App.getContext()).isDownloaded(media.getId())
                ? MusicUtil.getDownloadUri(media.getId())
                : MusicUtil.getStreamUri(media.getId());
    }

    private static Uri getUri(PodcastEpisode podcastEpisode) {
        return DownloadUtil.getDownloadTracker(App.getContext()).isDownloaded(podcastEpisode.getId())
                ? MusicUtil.getDownloadUri(podcastEpisode.getId())
                : MusicUtil.getStreamUri(podcastEpisode.getId());
    }
}
