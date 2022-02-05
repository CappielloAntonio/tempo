package com.cappielloantonio.play.model;

import com.cappielloantonio.play.util.MappingUtil;

import java.util.List;

public class PodcastChannel {
    protected String id;
    protected String url;
    protected String title;
    protected String description;
    protected String coverArtId;
    protected String originalImageUrl;
    protected String status;
    protected String errorMessage;
    protected List<PodcastEpisode> episodes;

    public PodcastChannel(com.cappielloantonio.play.subsonic.models.PodcastChannel podcastChannel) {
        this.id = podcastChannel.getId();
        this.url = podcastChannel.getUrl();
        this.title = podcastChannel.getTitle();
        this.description = podcastChannel.getDescription();
        this.coverArtId = podcastChannel.getCoverArtId();
        this.originalImageUrl = podcastChannel.getOriginalImageUrl();
        this.status = podcastChannel.getStatus();
        this.errorMessage = podcastChannel.getErrorMessage();
        this.episodes = MappingUtil.mapPodcastEpisode(podcastChannel.getEpisodes());
    }

    public PodcastChannel(String id, String url, String title, String description, String coverArtId, String originalImageUrl, String status, String errorMessage, List<PodcastEpisode> episodes) {
        this.id = id;
        this.url = url;
        this.title = title;
        this.description = description;
        this.coverArtId = coverArtId;
        this.originalImageUrl = originalImageUrl;
        this.status = status;
        this.errorMessage = errorMessage;
        this.episodes = episodes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCoverArtId() {
        return coverArtId;
    }

    public void setCoverArtId(String coverArtId) {
        this.coverArtId = coverArtId;
    }

    public String getOriginalImageUrl() {
        return originalImageUrl;
    }

    public void setOriginalImageUrl(String originalImageUrl) {
        this.originalImageUrl = originalImageUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public List<PodcastEpisode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<PodcastEpisode> episodes) {
        this.episodes = episodes;
    }
}
