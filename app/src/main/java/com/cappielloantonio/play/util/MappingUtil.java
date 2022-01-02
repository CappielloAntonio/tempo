package com.cappielloantonio.play.util;

import android.content.Context;
import android.os.Bundle;

import androidx.media3.common.MediaItem;
import androidx.media3.common.MediaMetadata;

import com.cappielloantonio.play.model.Album;
import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.model.Download;
import com.cappielloantonio.play.model.Playlist;
import com.cappielloantonio.play.model.Queue;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.subsonic.models.AlbumID3;
import com.cappielloantonio.play.subsonic.models.AlbumInfo;
import com.cappielloantonio.play.subsonic.models.AlbumWithSongsID3;
import com.cappielloantonio.play.subsonic.models.ArtistID3;
import com.cappielloantonio.play.subsonic.models.ArtistInfo2;
import com.cappielloantonio.play.subsonic.models.ArtistWithAlbumsID3;
import com.cappielloantonio.play.subsonic.models.Child;
import com.cappielloantonio.play.subsonic.models.Genre;
import com.cappielloantonio.play.subsonic.models.SimilarArtistID3;

import java.util.ArrayList;
import java.util.List;

public class MappingUtil {
    public static ArrayList<Song> mapSong(List<Child> children) {
        ArrayList<Song> songs = new ArrayList();

        for (Child child : children) {
            songs.add(new Song(child));
        }

        return songs;
    }

    public static Song mapSong(Child child) {
        return new Song(child);
    }

    public static ArrayList<Album> mapAlbum(List<AlbumID3> albumID3List) {
        ArrayList<Album> albums = new ArrayList();

        for (AlbumID3 albumID3 : albumID3List) {
            albums.add(new Album(albumID3));
        }

        return albums;
    }

    public static Album mapAlbum(AlbumWithSongsID3 albumWithSongsID3) {
        return new Album(albumWithSongsID3);
    }

    public static Album mapAlbum(AlbumInfo albumInfo) {
        return new Album(albumInfo);
    }

    public static ArrayList<Artist> mapArtist(List<ArtistID3> artistID3List) {
        ArrayList<Artist> artists = new ArrayList();

        for (ArtistID3 artistID3 : artistID3List) {
            artists.add(new Artist(artistID3));
        }

        return artists;
    }

    public static Artist mapArtist(ArtistInfo2 artistInfo2) {
        return new Artist(artistInfo2);
    }

    public static Artist mapArtist(ArtistWithAlbumsID3 artistWithAlbumsID3) {
        return new Artist(artistWithAlbumsID3);
    }

    public static Artist mapArtistWithAlbum(ArtistWithAlbumsID3 artistWithAlbumsID3) {
        return new Artist(artistWithAlbumsID3);
    }

    public static ArrayList<Artist> mapSimilarArtist(List<SimilarArtistID3> similarArtistID3s) {
        ArrayList<Artist> artists = new ArrayList();

        for (SimilarArtistID3 similarArtistID3 : similarArtistID3s) {
            artists.add(new Artist(similarArtistID3));
        }

        return artists;
    }

    public static ArrayList<Song> mapQueue(List<Queue> queueList) {
        ArrayList<Song> songs = new ArrayList();

        for (Queue item : queueList) {
            songs.add(new Song(item));
        }

        return songs;
    }

    public static Queue mapSongToQueue(Song song, int trackOrder) {
        return new Queue(trackOrder, song.getId(), song.getTitle(), song.getAlbumId(), song.getAlbumName(), song.getArtistId(), song.getArtistName(), song.getPrimary(), song.getDuration(), 0);
    }

    public static List<Queue> mapSongsToQueue(List<Song> songs) {
        List<Queue> queue = new ArrayList<>();

        for (int counter = 0; counter < songs.size(); counter++) {
            queue.add(mapSongToQueue(songs.get(counter), counter));
        }

        return queue;
    }

    public static ArrayList<Playlist> mapPlaylist(List<com.cappielloantonio.play.subsonic.models.Playlist> playlists) {
        ArrayList<Playlist> playlist = new ArrayList();

        for (com.cappielloantonio.play.subsonic.models.Playlist item : playlists) {
            playlist.add(new Playlist(item));
        }

        return playlist;
    }

    public static ArrayList<Song> mapDownloadToSong(List<Download> downloads) {
        ArrayList<Song> songs = new ArrayList();

        for (Download download : downloads) {
            Song song = new Song(download);
            if (!songs.contains(song)) {
                songs.add(song);
            }
        }

        return songs;
    }

    public static ArrayList<Album> mapDownloadToAlbum(List<Download> downloads) {
        ArrayList<Album> albums = new ArrayList();

        for (Download download : downloads) {
            Album album = new Album(download);
            if (!albums.contains(album)) {
                albums.add(album);
            }
        }

        return albums;
    }

    public static ArrayList<Artist> mapDownloadToArtist(List<Download> downloads) {
        ArrayList<Artist> artists = new ArrayList();

        for (Download download : downloads) {
            Artist artist = new Artist(download);
            if (!artists.contains(artist)) {
                artists.add(artist);
            }
        }

        return artists;
    }

    public static ArrayList<Playlist> mapDownloadToPlaylist(List<Download> downloads) {
        ArrayList<Playlist> playlists = new ArrayList();

        for (Download download : downloads) {
            playlists.add(new Playlist(download.getPlaylistId(), download.getPlaylistName()));
        }

        return playlists;
    }

    public static ArrayList<Download> mapDownload(List<Song> songs) {
        ArrayList<Download> downloads = new ArrayList();

        for (Song song : songs) {
            downloads.add(new Download(song, null, null));
        }

        return downloads;
    }

    public static Download mapDownload(Song song, String playlistId, String playlistName) {
        return new Download(song, playlistId, playlistName);
    }

    public static ArrayList<com.cappielloantonio.play.model.Genre> mapGenre(List<Genre> genreList) {
        ArrayList<com.cappielloantonio.play.model.Genre> genres = new ArrayList();

        for (Genre genre : genreList) {
            genres.add(new com.cappielloantonio.play.model.Genre(genre));
        }

        return genres;
    }

    public static MediaItem mapMediaItem(Context context, Song song, boolean stream) {
        Bundle bundle = new Bundle();
        bundle.putString("id", song.getId());
        bundle.putString("albumId", song.getAlbumId());
        bundle.putString("artistId", song.getArtistId());

        return new MediaItem.Builder()
                .setMediaId(song.getId())
                .setMediaMetadata(
                        new MediaMetadata.Builder()
                                .setMediaUri(stream ? MusicUtil.getSongStreamUri(context, song) : MusicUtil.getSongDownloadUri(song))
                                .setTitle(MusicUtil.getReadableString(song.getTitle()))
                                .setTrackNumber(song.getTrackNumber())
                                .setDiscNumber(song.getDiscNumber())
                                .setReleaseYear(song.getYear())
                                .setAlbumTitle(MusicUtil.getReadableString(song.getAlbumName()))
                                .setArtist(MusicUtil.getReadableString(song.getArtistName()))
                                .setExtras(bundle)
                                .build()
                )
                .setUri(stream ? MusicUtil.getSongStreamUri(context, song) : MusicUtil.getSongDownloadUri(song))
                .build();
    }

    public static ArrayList<MediaItem> mapMediaItems(Context context, List<Song> songs, boolean stream) {
        ArrayList<MediaItem> mediaItems = new ArrayList();

        for (Song song : songs) {
            mediaItems.add(mapMediaItem(context, song, stream));
        }

        return mediaItems;
    }

    public static ArrayList<MediaItem> markPlaylistMediaItems(ArrayList<MediaItem> mediaItems, String playlistId, String playlistName) {
        ArrayList<MediaItem> toReturn = new ArrayList();

        for(MediaItem mediaItem: mediaItems) {
            toReturn.add(markPlaylistMediaItem(mediaItem, playlistId, playlistName));
        }

        return toReturn;
    }

    private static MediaItem markPlaylistMediaItem(MediaItem mediaItem, String playlistId, String playlistName) {
        if (mediaItem.mediaMetadata.extras != null) {
            mediaItem.mediaMetadata.extras.putString("playlistId", playlistId);
            mediaItem.mediaMetadata.extras.putString("playlistName", playlistName);
        }

        return mediaItem;
    }
}
