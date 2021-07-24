package com.cappielloantonio.play.subsonic.models;

public enum MediaType {
    MUSIC("music"),
    PODCAST("podcast"),
    AUDIOBOOK("audiobook"),
    VIDEO("video");

    private final String value;

    MediaType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static MediaType fromValue(String v) {
        for (MediaType c : MediaType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
}
