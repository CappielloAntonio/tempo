package com.cappielloantonio.play.subsonic.models;

import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.Xml;

import java.util.ArrayList;
import java.util.List;

@Xml
public class IndexID3 {
    @Element(name = "artist")
    protected List<ArtistID3> artists;
    protected String name;

    public List<ArtistID3> getArtists() {
        if (artists == null) {
            artists = new ArrayList<>();
        }
        return this.artists;
    }

    public void setArtists(List<ArtistID3> artists) {
        this.artists = artists;
    }

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }
}
