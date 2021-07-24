package com.cappielloantonio.play.subsonic.models;

import java.util.ArrayList;
import java.util.List;

public class Indexes {
    protected List<Artist> shortcuts;
    protected List<Index> indices;
    protected List<Child> children;
    protected long lastModified;
    protected String ignoredArticles;

    /**
     * Gets the value of the shortcuts property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the shortcuts property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getShortcuts().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Artist }
     */
    public List<Artist> getShortcuts() {
        if (shortcuts == null) {
            shortcuts = new ArrayList<Artist>();
        }
        return this.shortcuts;
    }

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
     * {@link Index }
     */
    public List<Index> getIndices() {
        if (indices == null) {
            indices = new ArrayList<Index>();
        }
        return this.indices;
    }

    /**
     * Gets the value of the children property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the children property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getchildren().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Child }
     */
    public List<Child> getchildren() {
        if (children == null) {
            children = new ArrayList<Child>();
        }
        return this.children;
    }

    /**
     * Gets the value of the lastModified property.
     */
    public long getLastModified() {
        return lastModified;
    }

    /**
     * Sets the value of the lastModified property.
     */
    public void setLastModified(long value) {
        this.lastModified = value;
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
