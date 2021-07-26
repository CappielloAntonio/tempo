package com.cappielloantonio.play.subsonic.models;

import com.tickaroo.tikxml.annotation.Attribute;
import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.Xml;
import com.tickaroo.tikxml.converters.date.rfc3339.DateRfc3339TypeConverter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Xml
public class Playlist {
    protected List<String> allowedUsers;
    @Attribute
    protected String id;
    @Attribute
    protected String name;
    @Attribute
    protected String comment;
    @Attribute
    protected String owner;
    @Attribute(name = "public")
    protected Boolean universal;
    @Attribute
    protected int songCount;
    @Attribute
    protected int duration;
    @Attribute(converter = DateRfc3339TypeConverter.class)
    protected Date created;
    @Attribute(converter = DateRfc3339TypeConverter.class)
    protected Date changed;
    @Attribute
    protected String coverArtId;
    
    public List<String> getAllowedUsers() {
        if (allowedUsers == null) {
            allowedUsers = new ArrayList<>();
        }
        return this.allowedUsers;
    }

    public void setAllowedUsers(List<String> allowedUsers) {
        this.allowedUsers = allowedUsers;
    }

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

    public String getComment() {
        return comment;
    }

    public void setComment(String value) {
        this.comment = value;
    }

    public String getOwner() {
        return owner;
    }
    
    public void setOwner(String value) {
        this.owner = value;
    }

    public Boolean isUniversal() {
        return universal;
    }

    public void setUniversal(Boolean value) {
        this.universal = value;
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
    
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date value) {
        this.created = value;
    }
    
    public Date getChanged() {
        return changed;
    }

    public void setChanged(Date value) {
        this.changed = value;
    }

    public String getCoverArtId() {
        return coverArtId;
    }

    public void setCoverArtId(String value) {
        this.coverArtId = value;
    }
}
