package com.cappielloantonio.play.subsonic.models;

import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.Xml;

import java.util.ArrayList;
import java.util.List;

@Xml
public class Playlists {
    @Element(name = "playlist")
    protected List<Playlist> playlists;

    public List<Playlist> getPlaylists() {
        if (playlists == null) {
            playlists = new ArrayList<>();
        }
        return this.playlists;
    }

    public void setPlaylists(List<Playlist> playlists) {
        this.playlists = playlists;
    }
}
