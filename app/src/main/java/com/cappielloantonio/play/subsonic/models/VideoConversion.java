package com.cappielloantonio.play.subsonic.models;

public class VideoConversion {
    protected String id;
    protected Integer bitRate;
    protected Integer audioTrackId;

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
     * Gets the value of the bitRate property.
     *
     * @return possible object is
     * {@link Integer }
     */
    public Integer getBitRate() {
        return bitRate;
    }

    /**
     * Sets the value of the bitRate property.
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setBitRate(Integer value) {
        this.bitRate = value;
    }

    /**
     * Gets the value of the audioTrackId property.
     *
     * @return possible object is
     * {@link Integer }
     */
    public Integer getAudioTrackId() {
        return audioTrackId;
    }

    /**
     * Sets the value of the audioTrackId property.
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setAudioTrackId(Integer value) {
        this.audioTrackId = value;
    }
}
