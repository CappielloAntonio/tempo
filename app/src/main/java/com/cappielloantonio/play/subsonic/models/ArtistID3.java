package com.cappielloantonio.play.subsonic.models;

import java.time.LocalDateTime;

public class ArtistID3 {
    protected String id;
    protected String name;
    protected String coverArtId;
    protected int albumCount;
    protected LocalDateTime starred;

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
     * Gets the value of the coverArtId property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getCoverArtId() {
        return coverArtId;
    }

    /**
     * Sets the value of the coverArtId property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCoverArtId(String value) {
        this.coverArtId = value;
    }

    /**
     * Gets the value of the albumCount property.
     */
    public int getAlbumCount() {
        return albumCount;
    }

    /**
     * Sets the value of the albumCount property.
     */
    public void setAlbumCount(int value) {
        this.albumCount = value;
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

}
