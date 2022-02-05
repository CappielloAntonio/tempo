package com.cappielloantonio.play.subsonic.models;

import com.tickaroo.tikxml.annotation.Attribute;
import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.Xml;
import com.tickaroo.tikxml.converters.date.rfc3339.DateRfc3339TypeConverter;

import java.util.Date;

@Xml
public class Child {
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

    public String getId() {
        return id;
    }

    public void setId(String value) {
        this.id = value;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String value) {
        this.parentId = value;
    }

    public boolean isDir() {
        return dir;
    }

    public void setDir(boolean value) {
        this.dir = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String value) {
        this.title = value;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String value) {
        this.album = value;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String value) {
        this.artist = value;
    }

    public Integer getTrack() {
        return track;
    }

    public void setTrack(Integer value) {
        this.track = value;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer value) {
        this.year = value;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String value) {
        this.genre = value;
    }

    public String getCoverArtId() {
        return coverArtId;
    }

    public void setCoverArtId(String value) {
        this.coverArtId = value;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long value) {
        this.size = value;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String value) {
        this.contentType = value;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String value) {
        this.suffix = value;
    }

    public String getTranscodedContentType() {
        return transcodedContentType;
    }

    public void setTranscodedContentType(String value) {
        this.transcodedContentType = value;
    }

    public String getTranscodedSuffix() {
        return transcodedSuffix;
    }

    public void setTranscodedSuffix(String value) {
        this.transcodedSuffix = value;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer value) {
        this.duration = value;
    }

    public Integer getBitRate() {
        return bitRate;
    }

    public void setBitRate(Integer value) {
        this.bitRate = value;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String value) {
        this.path = value;
    }

    public Boolean isVideo() {
        return video;
    }

    public void setVideo(Boolean value) {
        this.video = value;
    }

    public Integer getUserRating() {
        return userRating;
    }

    public void setUserRating(Integer value) {
        this.userRating = value;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double value) {
        this.averageRating = value;
    }

    public Long getPlayCount() {
        return playCount;
    }

    public void setPlayCount(Long value) {
        this.playCount = value;
    }

    public Integer getDiscNumber() {
        return discNumber;
    }

    public void setDiscNumber(Integer value) {
        this.discNumber = value;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date value) {
        this.created = value;
    }

    public Date getStarred() {
        return starred;
    }

    public void setStarred(Date value) {
        this.starred = value;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String value) {
        this.albumId = value;
    }

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String value) {
        this.artistId = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String value) {
        this.type = value;
    }

    public Long getBookmarkPosition() {
        return bookmarkPosition;
    }

    public void setBookmarkPosition(Long value) {
        this.bookmarkPosition = value;
    }

    public Integer getOriginalWidth() {
        return originalWidth;
    }

    public void setOriginalWidth(Integer value) {
        this.originalWidth = value;
    }

    public Integer getOriginalHeight() {
        return originalHeight;
    }

    public void setOriginalHeight(Integer value) {
        this.originalHeight = value;
    }
}
