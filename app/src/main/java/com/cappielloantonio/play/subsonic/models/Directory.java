package com.cappielloantonio.play.subsonic.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Directory {
    protected List<Child> children;
    protected String id;
    protected String parentId;
    protected String name;
    protected LocalDateTime starred;
    protected Integer userRating;
    protected Double averageRating;
    protected Long playCount;

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
     * Gets the value of the parentId property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getParentId() {
        return parentId;
    }

    /**
     * Sets the value of the parentId property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setParentId(String value) {
        this.parentId = value;
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

    /**
     * Gets the value of the starred property.
     *
     * @return possible object is
     * {@link String }
     */
    public LocalDateTime getStarred() {
        return starred;
    }

    /**
     * Sets the value of the starred property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setStarred(LocalDateTime value) {
        this.starred = value;
    }

    /**
     * Gets the value of the userRating property.
     *
     * @return possible object is
     * {@link Integer }
     */
    public Integer getUserRating() {
        return userRating;
    }

    /**
     * Sets the value of the userRating property.
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setUserRating(Integer value) {
        this.userRating = value;
    }

    /**
     * Gets the value of the averageRating property.
     *
     * @return possible object is
     * {@link Double }
     */
    public Double getAverageRating() {
        return averageRating;
    }

    /**
     * Sets the value of the averageRating property.
     *
     * @param value allowed object is
     *              {@link Double }
     */
    public void setAverageRating(Double value) {
        this.averageRating = value;
    }

    /**
     * Gets the value of the playCount property.
     *
     * @return possible object is
     * {@link Long }
     */
    public Long getPlayCount() {
        return playCount;
    }

    /**
     * Sets the value of the playCount property.
     *
     * @param value allowed object is
     *              {@link Long }
     */
    public void setPlayCount(Long value) {
        this.playCount = value;
    }
}
