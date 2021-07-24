package com.cappielloantonio.play.subsonic.models;

public enum PodcastStatus {
    NEW("new"),
    DOWNLOADING("downloading"),
    COMPLETED("completed"),
    ERROR("error"),
    DELETED("deleted"),
    SKIPPED("skipped");

    private final String value;

    PodcastStatus(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static PodcastStatus fromValue(String v) {
        for (PodcastStatus c : PodcastStatus.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
}
