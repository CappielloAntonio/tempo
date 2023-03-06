package com.cappielloantonio.play.subsonic.models;

import java.time.LocalDateTime;

public class Artist {
    protected String id;
    protected String name;
    protected LocalDateTime starred;
    protected Integer userRating;
    protected Double averageRating;

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

}
