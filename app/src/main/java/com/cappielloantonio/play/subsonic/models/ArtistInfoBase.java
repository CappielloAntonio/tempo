package com.cappielloantonio.play.subsonic.models;

public class ArtistInfoBase {
    protected String biography;
    protected String musicBrainzId;
    protected String lastFmUrl;
    protected String smallImageUrl;
    protected String mediumImageUrl;
    protected String largeImageUrl;

    /**
     * Gets the value of the biography property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getBiography() {
        return biography;
    }

    /**
     * Sets the value of the biography property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setBiography(String value) {
        this.biography = value;
    }

    /**
     * Gets the value of the musicBrainzId property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getMusicBrainzId() {
        return musicBrainzId;
    }

    /**
     * Sets the value of the musicBrainzId property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setMusicBrainzId(String value) {
        this.musicBrainzId = value;
    }

    /**
     * Gets the value of the lastFmUrl property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getLastFmUrl() {
        return lastFmUrl;
    }

    /**
     * Sets the value of the lastFmUrl property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setLastFmUrl(String value) {
        this.lastFmUrl = value;
    }

    /**
     * Gets the value of the smallImageUrl property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getSmallImageUrl() {
        return smallImageUrl;
    }

    /**
     * Sets the value of the smallImageUrl property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setSmallImageUrl(String value) {
        this.smallImageUrl = value;
    }

    /**
     * Gets the value of the mediumImageUrl property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getMediumImageUrl() {
        return mediumImageUrl;
    }

    /**
     * Sets the value of the mediumImageUrl property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setMediumImageUrl(String value) {
        this.mediumImageUrl = value;
    }

    /**
     * Gets the value of the largeImageUrl property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getLargeImageUrl() {
        return largeImageUrl;
    }

    /**
     * Sets the value of the largeImageUrl property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setLargeImageUrl(String value) {
        this.largeImageUrl = value;
    }
}
