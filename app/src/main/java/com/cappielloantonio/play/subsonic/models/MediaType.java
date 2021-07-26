package com.cappielloantonio.play.subsonic.models;

import com.tickaroo.tikxml.annotation.Attribute;
import com.tickaroo.tikxml.annotation.Xml;

@Xml
public class MediaType {
    public static String MUSIC = "music";
    public static String PODCAST = "podcast";
    public static String AUDIOBOOK = "audiobook";
    public static String VIDEO = "video";

    @Attribute
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
