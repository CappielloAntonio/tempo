package com.cappielloantonio.play.subsonic.models;

import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.Xml;

import java.util.ArrayList;
import java.util.List;

@Xml
public class PlaylistWithSongs extends Playlist {
    @Element(name = "entry")
    protected List<Child> entries;

    public List<Child> getEntries() {
        if (entries == null) {
            entries = new ArrayList<>();
        }
        return this.entries;
    }

    public void setEntries(List<Child> entries) {
        this.entries = entries;
    }
}
