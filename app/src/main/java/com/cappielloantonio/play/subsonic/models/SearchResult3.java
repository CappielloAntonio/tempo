package com.cappielloantonio.play.subsonic.models;

import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.Xml;

import java.util.ArrayList;
import java.util.List;

@Xml
public class SearchResult3 {
    @Element(name = "artist")
    protected List<ArtistID3> artists;
    @Element(name = "album")
    protected List<AlbumID3> albums;
    @Element(name = "song")
    protected List<Child> songs;

    public List<ArtistID3> getArtists() {
        if (artists == null) {
            artists = new ArrayList<>();
        }
        return this.artists;
    }

    public List<AlbumID3> getAlbums() {
        if (albums == null) {
            albums = new ArrayList<>();
        }
        return this.albums;
    }

    public List<Child> getSongs() {
        if (songs == null) {
            songs = new ArrayList<>();
        }
        return this.songs;
    }

    public void setArtists(List<ArtistID3> artists) {
        this.artists = artists;
    }

    public void setAlbums(List<AlbumID3> albums) {
        this.albums = albums;
    }

    public void setSongs(List<Child> songs) {
        this.songs = songs;
    }
}
