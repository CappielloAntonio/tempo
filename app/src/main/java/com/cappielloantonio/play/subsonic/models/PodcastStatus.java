package com.cappielloantonio.play.subsonic.models;

import com.tickaroo.tikxml.annotation.Attribute;
import com.tickaroo.tikxml.annotation.Xml;

@Xml
public class PodcastStatus {
    public static String NEW = "new";
    public static String DOWNLOADING = "downloading";
    public static String COMPLETED = "completed";
    public static String ERROR = "error";
    public static String DELETED = "deleted";
    public static String SKIPPED = "skipped";

    @Attribute
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
