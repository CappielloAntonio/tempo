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
        bundle.putParcelable("child", media);

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
