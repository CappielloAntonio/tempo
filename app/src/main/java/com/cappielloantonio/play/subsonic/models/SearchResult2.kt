package com.cappielloantonio.play.subsonic.models;

import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.Xml;

import java.util.ArrayList;
import java.util.List;

@Xml
public class SearchResult2 {
    @Element(name = "artist")
    protected List<Artist> artists;
    @Element(name = "album")
    protected List<Child> albums;
    @Element(name = "song")
    protected List<Child> songs;

    public List<Artist> getArtists() {
        if (artists == null) {
            artists = new ArrayList<>();
        }
        return this.artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public List<Child> getAlbums() {
        if (albums == null) {
            albums = new ArrayList<>();
        }
        return this.albums;
    }

    public void setAlbums(List<Child> albums) {
        this.albums = albums;
    }

    public List<Child> getSongs() {
        if (songs == null) {
            songs = new ArrayList<>();
        }
        return this.songs;
    }

    public void setSongs(List<Child> songs) {
        this.songs = songs;
    }
}
