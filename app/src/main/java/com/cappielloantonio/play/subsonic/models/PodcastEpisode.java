package com.cappielloantonio.play.subsonic.models;

import com.tickaroo.tikxml.annotation.Attribute;
import com.tickaroo.tikxml.annotation.Xml;
import com.tickaroo.tikxml.converters.date.rfc3339.DateRfc3339TypeConverter;

import java.util.Date;

@Xml
public class PodcastEpisode {
    @Attribute
    protected String id;
    @Attribute(name = "parent")
    protected String parentId;
    @Attribute(name = "isDir")
    protected boolean dir;
    @Attribute
    protected String title;
    @Attribute
    protected String album;
    @Attribute
    protected String artist;
    @Attribute
    protected Integer track;
    @Attribute
    protected Integer year;
    @Attribute(name = "genre")
    protected String genre;
    @Attribute(name = "coverArt")
    protected String coverArtId;
    @Attribute
    protected Long size;
    @Attribute
    protected String contentType;
    @Attribute
    protected String suffix;
    @Attribute
    protected String transcodedContentType;
    @Attribute
    protected String transcodedSuffix;
    @Attribute
    protected Integer duration;
    @Attribute
    protected Integer bitRate;
    @Attribute
    protected String path;
    @Attribute(name = "isVideo")
    protected Boolean video;
    @Attribute
    protected Integer userRating;
    @Attribute
    protected Double averageRating;
    @Attribute
    protected Long playCount;
    @Attribute
    protected Integer discNumber;
    @Attribute(converter = DateRfc3339TypeConverter.class)
    protected Date created;
    @Attribute(converter = DateRfc3339TypeConverter.class)
    protected Date starred;
    @Attribute
    protected String albumId;
    @Attribute
    protected String artistId;
    @Attribute
    protected String type;
    @Attribute
    protected Long bookmarkPosition;
    @Attribute
    protected Integer originalWidth;
    @Attribute
    protected Integer originalHeight;

    @Attribute
    protected String streamId;
    @Attribute
    protected String channelId;
    @Attribute
    protected String description;
    @Attribute
    protected String status;
    @Attribute(converter = DateRfc3339TypeConverter.class)
    protected Date publishDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public boolean isDir() {
        return dir;
    }

    public void setDir(boolean dir) {
        this.dir = dir;
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

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getTranscodedContentType() {
        return transcodedContentType;
    }

    public void setTranscodedContentType(String transcodedContentType) {
        this.transcodedContentType = transcodedContentType;
    }

    public String getTranscodedSuffix() {
        return transcodedSuffix;
    }

    public void setTranscodedSuffix(String transcodedSuffix) {
        this.transcodedSuffix = transcodedSuffix;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getBitRate() {
        return bitRate;
    }

    public void setBitRate(Integer bitRate) {
        this.bitRate = bitRate;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Boolean getVideo() {
        return video;
    }

    public void setVideo(Boolean video) {
        this.video = video;
    }

    public Integer getUserRating() {
        return userRating;
    }

    public void setUserRating(Integer userRating) {
        this.userRating = userRating;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public Long getPlayCount() {
        return playCount;
    }

    public void setPlayCount(Long playCount) {
        this.playCount = playCount;
    }

    public Integer getDiscNumber() {
        return discNumber;
    }

    public void setDiscNumber(Integer discNumber) {
        this.discNumber = discNumber;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
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

    public Long getBookmarkPosition() {
        return bookmarkPosition;
    }

    public void setBookmarkPosition(Long bookmarkPosition) {
        this.bookmarkPosition = bookmarkPosition;
    }

    public Integer getOriginalWidth() {
        return originalWidth;
    }

    public void setOriginalWidth(Integer originalWidth) {
        this.originalWidth = originalWidth;
    }

    public Integer getOriginalHeight() {
        return originalHeight;
    }

    public void setOriginalHeight(Integer originalHeight) {
        this.originalHeight = originalHeight;
    }

    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String value) {
        this.streamId = value;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String value) {
        this.channelId = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String value) {
        this.description = value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String value) {
        this.status = value;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date value) {
        this.publishDate = value;
    }
}
