package com.cappielloantonio.play.subsonic.models;

import java.util.ArrayList;
import java.util.List;

public class AlbumList2 {
    protected List<AlbumID3> albums;

    /**
     * Gets the value of the albums property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the albums property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAlbums().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AlbumID3 }
     */
    public List<AlbumID3> getAlbums() {
        if (albums == null) {
            albums = new ArrayList<AlbumID3>();
        }
        return this.albums;
    }

}
