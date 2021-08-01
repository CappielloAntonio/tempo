package com.cappielloantonio.play.subsonic.models;

import com.tickaroo.tikxml.annotation.PropertyElement;
import com.tickaroo.tikxml.annotation.Xml;

@Xml
public class ArtistInfoBase {
    @PropertyElement
    protected String biography;
    @PropertyElement
    protected String musicBrainzId;
    @PropertyElement
    protected String lastFmUrl;
    @PropertyElement
    protected String smallImageUrl;
    @PropertyElement
    protected String mediumImageUrl;
    @PropertyElement
    protected String largeImageUrl;

    public String getBiography() {
        return biography;
    }

    public void setBiography(String value) {
        this.biography = value;
    }

    public String getMusicBrainzId() {
        return musicBrainzId;
    }

    public void setMusicBrainzId(String value) {
        this.musicBrainzId = value;
    }

    public String getLastFmUrl() {
        return lastFmUrl;
    }

    public void setLastFmUrl(String value) {
        this.lastFmUrl = value;
    }

    public String getSmallImageUrl() {
        return smallImageUrl;
    }

    public void setSmallImageUrl(String value) {
        this.smallImageUrl = value;
    }

    public String getMediumImageUrl() {
        return mediumImageUrl;
    }

    public void setMediumImageUrl(String value) {
        this.mediumImageUrl = value;
    }

    public String getLargeImageUrl() {
        return largeImageUrl;
    }

    public void setLargeImageUrl(String value) {
        this.largeImageUrl = value;
    }
}
