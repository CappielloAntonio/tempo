package com.cappielloantonio.play.subsonic.models;

import com.tickaroo.tikxml.annotation.Attribute;
import com.tickaroo.tikxml.annotation.Xml;

@Xml
public class AlbumInfo {
    @Attribute
    protected String notes;
    @Attribute
    protected String musicBrainzId;
    @Attribute
    protected String lastFmUrl;
    @Attribute
    protected String smallImageUrl;
    @Attribute
    protected String mediumImageUrl;
    @Attribute
    protected String largeImageUrl;
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String value) {
        this.notes = value;
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
