package com.cappielloantonio.play.subsonic.models;

import com.tickaroo.tikxml.annotation.Attribute;
import com.tickaroo.tikxml.annotation.Xml;

@Xml(name = "similarArtist")
public class SimilarArtistID3 {
    @Attribute
    protected String id;
    @Attribute
    protected String name;
    @Attribute(name = "coverArt")
    protected String coverArtId;
    @Attribute
    protected int albumCount;

    public String getId() {
        return id;
    }

    public void setId(String value) {
        this.id = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getCoverArtId() {
        return coverArtId;
    }

    public void setCoverArtId(String value) {
        this.coverArtId = value;
    }

    public int getAlbumCount() {
        return albumCount;
    }

    public void setAlbumCount(int value) {
        this.albumCount = value;
    }
}
