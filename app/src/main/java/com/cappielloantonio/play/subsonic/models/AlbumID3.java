package com.cappielloantonio.play.subsonic.models;

import java.time.LocalDateTime;

public class AlbumID3 {
    protected String id;
    protected String name;
    protected String artist;
    protected String artistId;
    protected String coverArtId;
    protected int songCount;
    protected int duration;
    protected Long playCount;
    protected LocalDateTime created;
    protected LocalDateTime starred;
    protected Integer year;
    protected String genre;

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
     * Gets the value of the artist property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getArtist() {
        return artist;
    }

    /**
     * Sets the value of the artist property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setArtist(String value) {
        this.artist = value;
    }

    /**
     * Gets the value of the artistId property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getArtistId() {
        return artistId;
    }

    /**
     * Sets the value of the artistId property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setArtistId(String value) {
        this.artistId = value;
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
     * Gets the value of the songCount property.
     */
    public int getSongCount() {
        return songCount;
    }

    /**
     * Sets the value of the songCount property.
     */
    public void setSongCount(int value) {
        this.songCount = value;
    }

    /**
     * Gets the value of the duration property.
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Sets the value of the duration property.
     */
    public void setDuration(int value) {
        this.duration = value;
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
     * Gets the value of the year property.
     *
     * @return possible object is
     * {@link Integer }
     */
    public Integer getYear() {
        return year;
    }

    /**
     * Sets the value of the year property.
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setYear(Integer value) {
        this.year = value;
    }

    /**
     * Gets the value of the genre property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getGenre() {
        return genre;
    }

    /**
     * Sets the value of the genre property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setGenre(String value) {
        this.genre = value;
    }

}
