package com.cappielloantonio.play.subsonic.models;

import com.tickaroo.tikxml.annotation.Attribute;
import com.tickaroo.tikxml.annotation.Xml;
import com.tickaroo.tikxml.converters.date.rfc3339.DateRfc3339TypeConverter;

import java.time.LocalDateTime;
import java.util.Date;

@Xml(name = "album")
public class AlbumID3 {
    @Attribute
    protected String id;
    @Attribute
    protected String name;
    @Attribute
    protected String artist;
    @Attribute
    protected String artistId;
    @Attribute(name = "coverArt")
    protected String coverArtId;
    @Attribute
    protected int songCount;
    @Attribute
    protected int duration;
    @Attribute
    protected Long playCount;
    @Attribute(converter = DateRfc3339TypeConverter.class)
    protected Date created;
    @Attribute(converter = DateRfc3339TypeConverter.class)
    protected Date starred;
    @Attribute
    protected Integer year;
    @Attribute
    protected String genre;

    public String getId() {
        return id;
    }

    public void setId(String value) {
        this.id = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String value) {
        this.artist = value;
    }

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String value) {
        this.artistId = value;
    }

    public String getCoverArtId() {
        return coverArtId;
    }

    public void setCoverArtId(String value) {
        this.coverArtId = value;
    }

    public int getSongCount() {
        return songCount;
    }

    public void setSongCount(int value) {
        this.songCount = value;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int value) {
        this.duration = value;
    }

    public Long getPlayCount() {
        return playCount;
    }

    public void setPlayCount(Long value) {
        this.playCount = value;
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
}
