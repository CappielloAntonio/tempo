package com.cappielloantonio.play.subsonic.models;

import java.util.ArrayList;
import java.util.List;

public class Bookmarks {
    protected List<Bookmark> bookmarks;

    /**
     * Gets the value of the bookmarks property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the bookmarks property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBookmarks().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Bookmark }
     */
    public List<Bookmark> getBookmarks() {
        if (bookmarks == null) {
            bookmarks = new ArrayList<Bookmark>();
        }
        return this.bookmarks;
    }

}
