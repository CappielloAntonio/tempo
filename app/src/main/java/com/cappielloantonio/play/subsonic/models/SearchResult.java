package com.cappielloantonio.play.subsonic.models;

import java.util.ArrayList;
import java.util.List;

public class SearchResult {
    protected List<Child> matches;
    protected int offset;
    protected int totalHits;

    /**
     * Gets the value of the matches property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the matches property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMatches().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Child }
     */
    public List<Child> getMatches() {
        if (matches == null) {
            matches = new ArrayList<>();
        }
        return this.matches;
    }

    /**
     * Gets the value of the offset property.
     */
    public int getOffset() {
        return offset;
    }

    /**
     * Sets the value of the offset property.
     */
    public void setOffset(int value) {
        this.offset = value;
    }

    /**
     * Gets the value of the totalHits property.
     */
    public int getTotalHits() {
        return totalHits;
    }

    /**
     * Sets the value of the totalHits property.
     */
    public void setTotalHits(int value) {
        this.totalHits = value;
    }
}
