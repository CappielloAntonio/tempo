package com.cappielloantonio.play.subsonic.models;

import com.tickaroo.tikxml.annotation.Attribute;
import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.Xml;

import java.util.ArrayList;
import java.util.List;

@Xml
public class PodcastChannel {
    @Element(name = "episode")
    protected List<PodcastEpisode> episodes;
    @Attribute
    protected String id;
    @Attribute
    protected String url;
    @Attribute
    protected String title;
    @Attribute
    protected String description;
    @Attribute
    protected String coverArtId;
    @Attribute
    protected String originalImageUrl;
    @Attribute
    protected String status;
    @Attribute
    protected String errorMessage;

    public List<PodcastEpisode> getEpisodes() {
        if (episodes == null) {
            episodes = new ArrayList<>();
        }
        return this.episodes;
    }

    public void setEpisodes(List<PodcastEpisode> episodes) {
        this.episodes = episodes;
    }

    public String getId() {
        return id;
    }

    public void setId(String value) {
        this.id = value;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String value) {
        this.url = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String value) {
        this.title = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String value) {
        this.description = value;
    }

    public String getCoverArtId() {
        return coverArtId;
    }

    public void setCoverArtId(String value) {
        this.coverArtId = value;
    }

    public String getOriginalImageUrl() {
        return originalImageUrl;
    }

    public void setOriginalImageUrl(String value) {
        this.originalImageUrl = value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String value) {
        this.status = value;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String value) {
        this.errorMessage = value;
    }
}
