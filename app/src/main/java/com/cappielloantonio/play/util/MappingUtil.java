package com.cappielloantonio.play.util;

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
import java.util.Collections;
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
            if(!songs.contains(song)) {
                songs.add(song);
            }
        }

        return songs;
    }

    public static ArrayList<Album> mapDownloadToAlbum(List<Download> downloads) {
        ArrayList<Album> albums = new ArrayList();

        for (Download download : downloads) {
            Album album = new Album(download);
            if(!albums.contains(album)) {
                albums.add(album);
            }
        }

        return albums;
    }

    public static ArrayList<Artist> mapDownloadToArtist(List<Download> downloads) {
        ArrayList<Artist> artists = new ArrayList();

        for (Download download : downloads) {
            Artist artist = new Artist(download);
            if(!artists.contains(artist)) {
                artists.add(artist);
            }
        }

        return artists;
    }

    public static ArrayList<Download> mapToDownload(List<Song> songs) {
        ArrayList<Download> downloads = new ArrayList();

        for (Song song : songs) {
            downloads.add(new Download(song));
        }

        return downloads;
    }

    public static Download mapToDownload(Song song) {
        return new Download(song);
    }

    public static ArrayList<com.cappielloantonio.play.model.Genre> mapGenre(List<Genre> genreList) {
        ArrayList<com.cappielloantonio.play.model.Genre> genres = new ArrayList();

        for (Genre genre : genreList) {
            genres.add(new com.cappielloantonio.play.model.Genre(genre));
        }

        return genres;
    }
}
