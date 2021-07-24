package com.cappielloantonio.play.subsonic.models;

import java.util.ArrayList;
import java.util.List;

public class ArtistsID3 {
    protected List<IndexID3> indices;
    protected String ignoredArticles;

    /**
     * Gets the value of the indices property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the indices property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIndices().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IndexID3 }
     */
    public List<IndexID3> getIndices() {
        if (indices == null) {
            indices = new ArrayList<IndexID3>();
        }
        return this.indices;
    }

    /**
     * Gets the value of the ignoredArticles property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getIgnoredArticles() {
        return ignoredArticles;
    }

    /**
     * Sets the value of the ignoredArticles property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setIgnoredArticles(String value) {
        this.ignoredArticles = value;
    }
}
