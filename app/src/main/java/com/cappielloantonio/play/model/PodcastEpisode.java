package com.cappielloantonio.play.model;

import java.util.Date;

public class PodcastEpisode {
    protected String id;
    protected String title;
    protected String album;
    protected String artist;
    protected Integer track;
    protected Integer year;
    protected String genre;
    protected String coverArtId;
    protected Integer duration;
    protected String path;
    protected Date starred;
    protected String albumId;
    protected String artistId;
    protected String type;
    protected String streamId;
    protected String channelId;
    protected String description;
    protected String status;
    protected Date publishDate;

    public PodcastEpisode(com.cappielloantonio.play.subsonic.models.PodcastEpisode podcastEpisode) {
        this.id = podcastEpisode.getId();
        this.title = podcastEpisode.getTitle();
        this.album = podcastEpisode.getAlbum();
        this.artist = podcastEpisode.getArtist();
        this.track = podcastEpisode.getTrack();
        this.year = podcastEpisode.getYear();
        this.coverArtId = podcastEpisode.getCoverArtId();
        this.duration = podcastEpisode.getDuration();
        this.starred = podcastEpisode.getStarred();
        this.streamId = podcastEpisode.getStreamId();
        this.channelId = podcastEpisode.getChannelId();
        this.description = podcastEpisode.getDescription();
        this.status = podcastEpisode.getStatus();
        this.publishDate = podcastEpisode.getPublishDate();
    }

    public PodcastEpisode(String id, String title, String album, String artist, Integer track, Integer year, String genre, String coverArtId, Integer duration, String path, Date starred, String albumId, String artistId, String type, String streamId, String channelId, String description, String status, Date publishDate) {
        this.id = id;
        this.title = title;
        this.album = album;
        this.artist = artist;
        this.track = track;
        this.year = year;
        this.genre = genre;
        this.coverArtId = coverArtId;
        this.duration = duration;
        this.path = path;
        this.starred = starred;
        this.albumId = albumId;
        this.artistId = artistId;
        this.type = type;
        this.streamId = streamId;
        this.channelId = channelId;
        this.description = description;
        this.status = status;
        this.publishDate = publishDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public Integer getTrack() {
        return track;
    }

    public void setTrack(Integer track) {
        this.track = track;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getCoverArtId() {
        return coverArtId;
    }

    public void setCoverArtId(String coverArtId) {
        this.coverArtId = coverArtId;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Date getStarred() {
        return starred;
    }

    public void setStarred(Date starred) {
        this.starred = starred;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }
}
