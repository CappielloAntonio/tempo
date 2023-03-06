package com.cappielloantonio.play.subsonic.models;

import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.Xml;

import java.util.ArrayList;
import java.util.List;

@Xml
public class NewestPodcasts {
    @Element(name = "episode")
    protected List<PodcastEpisode> episodes;

    public List<PodcastEpisode> getEpisodes() {
        if (episodes == null) {
            episodes = new ArrayList<>();
        }
        return this.episodes;
    }

    public void setEpisodes(List<PodcastEpisode> episodes) {
        this.episodes = episodes;
    }
}
