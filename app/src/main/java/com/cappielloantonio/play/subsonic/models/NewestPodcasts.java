package com.cappielloantonio.play.subsonic.models;

import java.util.ArrayList;
import java.util.List;

public class NewestPodcasts {
    protected List<PodcastEpisode> episodes;

    /**
     * Gets the value of the episodes property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the episodes property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEpisodes().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PodcastEpisode }
     */
    public List<PodcastEpisode> getEpisodes() {
        if (episodes == null) {
            episodes = new ArrayList<>();
        }
        return this.episodes;
    }
}
