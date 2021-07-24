package com.cappielloantonio.play.subsonic.models;

import java.util.ArrayList;
import java.util.List;

public class MusicFolders {
    protected List<MusicFolder> musicFolders;

    /**
     * Gets the value of the musicFolders property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the musicFolders property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMusicFolders().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MusicFolder }
     */
    public List<MusicFolder> getMusicFolders() {
        if (musicFolders == null) {
            musicFolders = new ArrayList<MusicFolder>();
        }
        return this.musicFolders;
    }

}
