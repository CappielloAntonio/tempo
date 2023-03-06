package com.cappielloantonio.play.subsonic.models;

import com.tickaroo.tikxml.annotation.Attribute;
import com.tickaroo.tikxml.annotation.Xml;

@Xml
public class ResponseStatus {
    public static String OK = "ok";
    public static String FAILED = "failed";

    private final String value;

    public ResponseStatus(@Attribute String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
