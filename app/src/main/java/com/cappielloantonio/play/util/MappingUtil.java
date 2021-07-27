package com.cappielloantonio.play.util;

import com.cappielloantonio.play.model.Album;
import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.subsonic.models.AlbumID3;
import com.cappielloantonio.play.subsonic.models.ArtistID3;
import com.cappielloantonio.play.subsonic.models.Child;

import java.util.ArrayList;
import java.util.List;

public class MappingUtil {
    public static ArrayList<Song> mapSong(List<Child> children) {
        ArrayList<Song> songs = new ArrayList();

        for(Child child : children){
            songs.add(new Song(child));
        }

        return songs;
    }

    public static ArrayList<Album> mapAlbum(List<AlbumID3> albumID3List) {
        ArrayList<Album> albums = new ArrayList();

        for(AlbumID3 albumID3 : albumID3List){
            albums.add(new Album(albumID3));
        }

        return albums;
    }

    public static ArrayList<Artist> mapArtist(List<ArtistID3> albumID3List) {
        ArrayList<Artist> artists = new ArrayList();

        for(ArtistID3 artistID3 : albumID3List){
            artists.add(new Artist(artistID3));
        }

        return artists;
    }
}
