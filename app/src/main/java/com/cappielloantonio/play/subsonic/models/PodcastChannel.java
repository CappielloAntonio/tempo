package com.cappielloantonio.play.subsonic.models;

import java.util.ArrayList;
import java.util.List;

public class PodcastChannel {
    protected List<PodcastEpisode> episodes;
    protected String id;
    protected String url;
    protected String title;
    protected String description;
    protected String coverArtId;
    protected String originalImageUrl;
    protected PodcastStatus status;
    protected String errorMessage;

    /**
     * Gets the value of the episodes property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the episodes property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEpisodes().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PodcastEpisode }
     */
    public List<PodcastEpisode> getEpisodes() {
        if (episodes == null) {
            episodes = new ArrayList<PodcastEpisode>();
        }
        return this.episodes;
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
     * Gets the value of the title property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the value of the title property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTitle(String value) {
        this.title = value;
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
     * Gets the value of the originalImageUrl property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getOriginalImageUrl() {
        return originalImageUrl;
    }

    /**
     * Sets the value of the originalImageUrl property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setOriginalImageUrl(String value) {
        this.originalImageUrl = value;
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
     * Gets the value of the errorMessage property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Sets the value of the errorMessage property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setErrorMessage(String value) {
        this.errorMessage = value;
    }
}
