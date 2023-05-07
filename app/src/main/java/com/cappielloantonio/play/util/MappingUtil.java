package com.cappielloantonio.play.util;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.OptIn;
import androidx.media3.common.MediaItem;
import androidx.media3.common.MediaMetadata;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.UnstableApi;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.subsonic.models.Child;
import com.cappielloantonio.play.subsonic.models.InternetRadioStation;

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
        bundle.putLong("playCount", media.getPlayCount() != null ? media.getTrack() : 0);
        bundle.putInt("discNumber", media.getDiscNumber() != null ? media.getTrack() : 0);
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
                                .setTrackNumber(media.getTrack())
                                .setDiscNumber(media.getDiscNumber())
                                .setReleaseYear(media.getYear())
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
                                .setTrackNumber(media.getTrack())
                                .setDiscNumber(media.getDiscNumber())
                                .setReleaseYear(media.getYear())
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

    private static Uri getUri(Child media) {
        return DownloadUtil.getDownloadTracker(App.getContext()).isDownloaded(media.getId())
                ? MusicUtil.getDownloadUri(media.getId())
                : MusicUtil.getStreamUri(media.getId());
    }
}
