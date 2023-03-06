package com.cappielloantonio.play.subsonic.models;

import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.Xml;

import java.util.ArrayList;
import java.util.List;

@Xml
public class MusicFolders {
    @Element
    protected List<MusicFolder> musicFolders;

    public List<MusicFolder> getMusicFolders() {
        if (musicFolders == null) {
            musicFolders = new ArrayList<>();
        }
        return this.musicFolders;
    }

    public void setMusicFolders(List<MusicFolder> musicFolders) {
        this.musicFolders = musicFolders;
    }
}
