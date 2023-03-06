package com.cappielloantonio.play.subsonic.models;

import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.Xml;

import java.util.ArrayList;
import java.util.List;

@Xml
public class ArtistWithAlbumsID3 extends ArtistID3 {
    @Element(name = "album")
    protected List<AlbumID3> albums;

    public List<AlbumID3> getAlbums() {
        if (albums == null) {
            albums = new ArrayList<>();
        }
        return this.albums;
    }

    public void setAlbums(List<AlbumID3> albums) {
        this.albums = albums;
    }
}
