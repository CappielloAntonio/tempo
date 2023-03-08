package com.cappielloantonio.play.util;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.OptIn;
import androidx.media3.common.MediaItem;
import androidx.media3.common.MediaMetadata;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.util.UnstableApi;

import com.cappielloantonio.play.model.Album;
import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.model.Download;
import com.cappielloantonio.play.model.Media;
import com.cappielloantonio.play.model.Playlist;
import com.cappielloantonio.play.subsonic.models.Child;

import java.util.ArrayList;
import java.util.List;

public class MappingUtil {
    public static ArrayList<Album> mapDownloadToAlbum(List<Download> downloads) {
        ArrayList<Album> albums = new ArrayList();

        // TODO
        /* for (Download download : downloads) {
            Album album = new Album(download);
            if (!albums.contains(album)) {
                albums.add(album);
            }
        } */

        return albums;
    }

    public static ArrayList<Artist> mapDownloadToArtist(List<Download> downloads) {
        ArrayList<Artist> artists = new ArrayList();

        // TODO
        /* for (Download download : downloads) {
            Artist artist = new Artist(download);
            if (!artists.contains(artist)) {
                artists.add(artist);
            }
        } */

        return artists;
    }

    public static ArrayList<Playlist> mapDownloadToPlaylist(List<Download> downloads) {
        ArrayList<Playlist> playlists = new ArrayList();

        // TODO
        /*for (Download download : downloads) {
            playlists.add(new Playlist(download.getPlaylistId(), download.getPlaylistName(), null, 0, 0, null));
        }*/

        return playlists;
    }

    public static ArrayList<Download> mapDownload(List<Child> media, String playlistId, String playlistName) {
        ArrayList<Download> downloads = new ArrayList();

        // TODO
        /* for (Child item : media) {
            Download download = (Download) item;
            download.setMediaID();
            download.setServer();
            download.setPlaylistId();
            downloads.add(download);
        } */

        return downloads;
    }

    public static Download mapDownload(Child media, String playlistId, String playlistName) {
        // TODO
        //return new Download(media, playlistId, playlistName);

        return null;
    }

    @OptIn(markerClass = UnstableApi.class)
    public static MediaItem mapMediaItem(Context context, Child media, boolean stream) {
        boolean isDownloaded = DownloadUtil.getDownloadTracker(context).isDownloaded(MusicUtil.getDownloadUri(media.getId()));

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
                                .setMediaUri(getUri(context, media, stream && !isDownloaded))
                                .setExtras(bundle)
                                .build()
                )
                .setMimeType(MimeTypes.BASE_TYPE_AUDIO)
                .setUri(getUri(context, media, stream && !isDownloaded))
                .build();
    }

    private static Uri getUri(Context context, Child media, boolean stream) {
        switch (media.getType()) {
            case Media.MEDIA_TYPE_MUSIC:
                if (stream) {
                    return MusicUtil.getStreamUri(context, media.getId());
                } else {
                    return MusicUtil.getDownloadUri(media.getId());
                }
            case Media.MEDIA_TYPE_PODCAST:
                if (stream) {
                    // TODO
                    // return MusicUtil.getStreamUri(context, media.getStreamId());
                } else {
                    // TODO
                    // return MusicUtil.getDownloadUri(media.getStreamId());
                }
            default:
                return MusicUtil.getStreamUri(context, media.getId());
        }
    }

    public static ArrayList<MediaItem> mapMediaItems(Context context, List<Child> items, boolean stream) {
        ArrayList<MediaItem> mediaItems = new ArrayList();

        for (int i = 0; i < items.size(); i++) {
            mediaItems.add(mapMediaItem(context, items.get(i), stream));
        }

        return mediaItems;
    }
}
