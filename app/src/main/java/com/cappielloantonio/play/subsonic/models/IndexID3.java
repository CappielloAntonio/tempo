package com.cappielloantonio.play.subsonic.models;

import java.util.ArrayList;
import java.util.List;

public class IndexID3 {
    protected List<ArtistID3> artists;
    protected String name;

    /**
     * Gets the value of the artists property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the artists property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getArtists().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ArtistID3 }
     */
    public List<ArtistID3> getArtists() {
        if (artists == null) {
            artists = new ArrayList<ArtistID3>();
        }
        return this.artists;
    }

    /**
     * Gets the value of the name property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setName(String value) {
        this.name = value;
    }
}
