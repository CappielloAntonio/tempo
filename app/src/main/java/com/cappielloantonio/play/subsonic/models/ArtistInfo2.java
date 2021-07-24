package com.cappielloantonio.play.subsonic.models;

import java.util.ArrayList;
import java.util.List;

public class ArtistInfo2 extends ArtistInfoBase {

    protected List<ArtistID3> similarArtists;

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
     * {@link ArtistID3 }
     */
    public List<ArtistID3> getSimilarArtists() {
        if (similarArtists == null) {
            similarArtists = new ArrayList<ArtistID3>();
        }
        return this.similarArtists;
    }
}
