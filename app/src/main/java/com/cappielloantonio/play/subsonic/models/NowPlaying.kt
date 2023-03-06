package com.cappielloantonio.play.subsonic.models;

import java.util.ArrayList;
import java.util.List;

public class NowPlaying {
    protected List<NowPlayingEntry> entries;

    /**
     * Gets the value of the entries property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the entries property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEntries().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link NowPlayingEntry }
     */
    public List<NowPlayingEntry> getEntries() {
        if (entries == null) {
            entries = new ArrayList<>();
        }
        return this.entries;
    }
}
