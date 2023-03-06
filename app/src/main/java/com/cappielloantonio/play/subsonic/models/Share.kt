package com.cappielloantonio.play.subsonic.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Share {
    protected List<Child> entries;
    protected String id;
    protected String url;
    protected String description;
    protected String username;
    protected LocalDateTime created;
    protected LocalDateTime expires;
    protected LocalDateTime lastVisited;
    protected int visitCount;

    /**
     * Gets the value of the entries property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the entries property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEntries().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Child }
     */
    public List<Child> getEntries() {
        if (entries == null) {
            entries = new ArrayList<>();
        }
        return this.entries;
    }

    /**
     * Gets the value of the id property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the url property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the value of the url property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setUrl(String value) {
        this.url = value;
    }

    /**
     * Gets the value of the description property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the username property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the value of the username property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setUsername(String value) {
        this.username = value;
    }

    /**
     * Gets the value of the created property.
     *
     * @return possible object is
     * {@link String }
     */
    public LocalDateTime getCreated() {
        return created;
    }

    /**
     * Sets the value of the created property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCreated(LocalDateTime value) {
        this.created = value;
    }

    /**
     * Gets the value of the expires property.
     *
     * @return possible object is
     * {@link String }
     */
    public LocalDateTime getExpires() {
        return expires;
    }

    /**
     * Sets the value of the expires property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setExpires(LocalDateTime value) {
        this.expires = value;
    }

    /**
     * Gets the value of the lastVisited property.
     *
     * @return possible object is
     * {@link String }
     */
    public LocalDateTime getLastVisited() {
        return lastVisited;
    }

    /**
     * Sets the value of the lastVisited property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setLastVisited(LocalDateTime value) {
        this.lastVisited = value;
    }

    /**
     * Gets the value of the visitCount property.
     */
    public int getVisitCount() {
        return visitCount;
    }

    /**
     * Sets the value of the visitCount property.
     */
    public void setVisitCount(int value) {
        this.visitCount = value;
    }
}
