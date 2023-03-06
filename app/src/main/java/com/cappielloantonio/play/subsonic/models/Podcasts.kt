package com.cappielloantonio.play.subsonic.models;

import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.Xml;

import java.util.ArrayList;
import java.util.List;

@Xml
public class Podcasts {
    @Element(name = "channel")
    protected List<PodcastChannel> channels;

    public List<PodcastChannel> getChannels() {
        if (channels == null) {
            channels = new ArrayList<>();
        }
        return this.channels;
    }

    public void setChannels(List<PodcastChannel> channels) {
        this.channels = channels;
    }
}
