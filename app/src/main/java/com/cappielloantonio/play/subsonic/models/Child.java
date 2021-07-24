package com.cappielloantonio.play.subsonic.models;

import java.time.LocalDateTime;

public class Child {
    protected String id;
    protected String parentId;
    protected boolean dir;
    protected String title;
    protected String album;
    protected String artist;
    protected Integer track;
    protected Integer year;
    protected String genre;
    protected String coverArtId;
    protected Long size;
    protected String contentType;
    protected String suffix;
    protected String transcodedContentType;
    protected String transcodedSuffix;
    protected Integer duration;
    protected Integer bitRate;
    protected String path;
    protected Boolean video;
    protected Integer userRating;
    protected Double averageRating;
    protected Long playCount;
    protected Integer discNumber;
    protected LocalDateTime created;
    protected LocalDateTime starred;
    protected String albumId;
    protected String artistId;
    protected MediaType type;
    protected Long bookmarkPosition;
    protected Integer originalWidth;
    protected Integer originalHeight;

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
     * Gets the value of the parentId property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getParentId() {
        return parentId;
    }

    /**
     * Sets the value of the parentId property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setParentId(String value) {
        this.parentId = value;
    }

    /**
     * Gets the value of the dir property.
     */
    public boolean isDir() {
        return dir;
    }

    /**
     * Sets the value of the dir property.
     */
    public void setDir(boolean value) {
        this.dir = value;
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
     * Gets the value of the album property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getAlbum() {
        return album;
    }

    /**
     * Sets the value of the album property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setAlbum(String value) {
        this.album = value;
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
     * Gets the value of the track property.
     *
     * @return possible object is
     * {@link Integer }
     */
    public Integer getTrack() {
        return track;
    }

    /**
     * Sets the value of the track property.
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setTrack(Integer value) {
        this.track = value;
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
     * Gets the value of the size property.
     *
     * @return possible object is
     * {@link Long }
     */
    public Long getSize() {
        return size;
    }

    /**
     * Sets the value of the size property.
     *
     * @param value allowed object is
     *              {@link Long }
     */
    public void setSize(Long value) {
        this.size = value;
    }

    /**
     * Gets the value of the contentType property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * Sets the value of the contentType property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setContentType(String value) {
        this.contentType = value;
    }

    /**
     * Gets the value of the suffix property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getSuffix() {
        return suffix;
    }

    /**
     * Sets the value of the suffix property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setSuffix(String value) {
        this.suffix = value;
    }

    /**
     * Gets the value of the transcodedContentType property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getTranscodedContentType() {
        return transcodedContentType;
    }

    /**
     * Sets the value of the transcodedContentType property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTranscodedContentType(String value) {
        this.transcodedContentType = value;
    }

    /**
     * Gets the value of the transcodedSuffix property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getTranscodedSuffix() {
        return transcodedSuffix;
    }

    /**
     * Sets the value of the transcodedSuffix property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTranscodedSuffix(String value) {
        this.transcodedSuffix = value;
    }

    /**
     * Gets the value of the duration property.
     *
     * @return possible object is
     * {@link Integer }
     */
    public Integer getDuration() {
        return duration;
    }

    /**
     * Sets the value of the duration property.
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setDuration(Integer value) {
        this.duration = value;
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
     * Gets the value of the path property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getPath() {
        return path;
    }

    /**
     * Sets the value of the path property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPath(String value) {
        this.path = value;
    }

    /**
     * Gets the value of the video property.
     *
     * @return possible object is
     * {@link Boolean }
     */
    public Boolean isVideo() {
        return video;
    }

    /**
     * Sets the value of the video property.
     *
     * @param value allowed object is
     *              {@link Boolean }
     */
    public void setVideo(Boolean value) {
        this.video = value;
    }

    /**
     * Gets the value of the userRating property.
     *
     * @return possible object is
     * {@link Integer }
     */
    public Integer getUserRating() {
        return userRating;
    }

    /**
     * Sets the value of the userRating property.
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setUserRating(Integer value) {
        this.userRating = value;
    }

    /**
     * Gets the value of the averageRating property.
     *
     * @return possible object is
     * {@link Double }
     */
    public Double getAverageRating() {
        return averageRating;
    }

    /**
     * Sets the value of the averageRating property.
     *
     * @param value allowed object is
     *              {@link Double }
     */
    public void setAverageRating(Double value) {
        this.averageRating = value;
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
     * Gets the value of the discNumber property.
     *
     * @return possible object is
     * {@link Integer }
     */
    public Integer getDiscNumber() {
        return discNumber;
    }

    /**
     * Sets the value of the discNumber property.
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setDiscNumber(Integer value) {
        this.discNumber = value;
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
     * Gets the value of the albumId property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getAlbumId() {
        return albumId;
    }

    /**
     * Sets the value of the albumId property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setAlbumId(String value) {
        this.albumId = value;
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
     * Gets the value of the type property.
     *
     * @return possible object is
     * {@link MediaType }
     */
    public MediaType getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     *
     * @param value allowed object is
     *              {@link MediaType }
     */
    public void setType(MediaType value) {
        this.type = value;
    }

    /**
     * Gets the value of the bookmarkPosition property.
     *
     * @return possible object is
     * {@link Long }
     */
    public Long getBookmarkPosition() {
        return bookmarkPosition;
    }

    /**
     * Sets the value of the bookmarkPosition property.
     *
     * @param value allowed object is
     *              {@link Long }
     */
    public void setBookmarkPosition(Long value) {
        this.bookmarkPosition = value;
    }

    /**
     * Gets the value of the originalWidth property.
     *
     * @return possible object is
     * {@link Integer }
     */
    public Integer getOriginalWidth() {
        return originalWidth;
    }

    /**
     * Sets the value of the originalWidth property.
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setOriginalWidth(Integer value) {
        this.originalWidth = value;
    }

    /**
     * Gets the value of the originalHeight property.
     *
     * @return possible object is
     * {@link Integer }
     */
    public Integer getOriginalHeight() {
        return originalHeight;
    }

    /**
     * Sets the value of the originalHeight property.
     *
     * @param value allowed object is
     *              {@link Integer }
     */
    public void setOriginalHeight(Integer value) {
        this.originalHeight = value;
    }
}
