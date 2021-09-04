package com.cappielloantonio.play.subsonic.models;

import java.util.ArrayList;
import java.util.List;

public class Podcasts {
    protected List<PodcastChannel> channels;

    /**
     * Gets the value of the channels property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the channels property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getChannels().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PodcastChannel }
     */
    public List<PodcastChannel> getChannels() {
        if (channels == null) {
            channels = new ArrayList<>();
        }
        return this.channels;
    }
}
