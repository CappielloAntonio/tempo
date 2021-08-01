package com.cappielloantonio.play.subsonic.models;

import com.tickaroo.tikxml.annotation.Attribute;
import com.tickaroo.tikxml.annotation.Xml;

@Xml
public class MusicFolder {
    @Attribute
    protected int id;
    @Attribute
    protected String name;

    public int getId() {
        return id;
    }

    public void setId(int value) {
        this.id = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }
}
