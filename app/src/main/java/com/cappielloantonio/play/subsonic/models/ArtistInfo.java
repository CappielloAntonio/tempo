package com.cappielloantonio.play.subsonic.models;

import java.util.ArrayList;
import java.util.List;

public class ArtistInfo extends ArtistInfoBase {
    protected List<Artist> similarArtists;

    /**
     * Gets the value of the similarArtists property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the similarArtists property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSimilarArtists().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Artist }
     */
    public List<Artist> getSimilarArtists() {
        if (similarArtists == null) {
            similarArtists = new ArrayList<>();
        }
        return this.similarArtists;
    }

}
