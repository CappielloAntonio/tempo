package com.cappielloantonio.play.subsonic.models;

import java.time.LocalDateTime;

public class PodcastEpisode extends Child {
    protected String streamId;
    protected String channelId;
    protected String description;
    protected PodcastStatus status;
    protected LocalDateTime publishDate;

    /**
     * Gets the value of the streamId property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getStreamId() {
        return streamId;
    }

    /**
     * Sets the value of the streamId property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setStreamId(String value) {
        this.streamId = value;
    }

    /**
     * Gets the value of the channelId property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getChannelId() {
        return channelId;
    }

    /**
     * Sets the value of the channelId property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setChannelId(String value) {
        this.channelId = value;
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
     * Gets the value of the status property.
     *
     * @return possible object is
     * {@link PodcastStatus }
     */
    public PodcastStatus getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     *
     * @param value allowed object is
     *              {@link PodcastStatus }
     */
    public void setStatus(PodcastStatus value) {
        this.status = value;
    }

    /**
     * Gets the value of the publishDate property.
     *
     * @return possible object is
     * {@link String }
     */
    public LocalDateTime getPublishDate() {
        return publishDate;
    }

    /**
     * Sets the value of the publishDate property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPublishDate(LocalDateTime value) {
        this.publishDate = value;
    }
}
