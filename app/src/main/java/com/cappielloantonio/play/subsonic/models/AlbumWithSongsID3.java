package com.cappielloantonio.play.subsonic.models;

import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.Xml;

import java.util.ArrayList;
import java.util.List;

@Xml
public class AlbumWithSongsID3 extends AlbumID3 {
    @Element(name = "song")
    protected List<Child> songs;

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
