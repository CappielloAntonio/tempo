package com.cappielloantonio.play.subsonic.models;

import java.util.ArrayList;
import java.util.List;

public class Playlists {
    protected List<Playlist> playlists;

    /**
     * Gets the value of the playlists property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the playlists property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPlaylists().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Playlist }
     */
    public List<Playlist> getPlaylists() {
        if (playlists == null) {
            playlists = new ArrayList<Playlist>();
        }
        return this.playlists;
    }
}
