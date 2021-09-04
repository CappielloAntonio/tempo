package com.cappielloantonio.play.subsonic.base;

import androidx.annotation.NonNull;

public class Version implements Comparable<Version> {

    private static final String VERSION_PATTERN = "[0-9]+(\\.[0-9]+)*";
    private final String versionString;

    public static Version of(String versionString) {
        return new Version(versionString);
    }

    private Version(String versionString) {
        if (versionString == null || !versionString.matches(VERSION_PATTERN)) {
            throw new IllegalArgumentException("Invalid version format");
        }
        this.versionString = versionString;
    }

    public String getVersionString() {
        return versionString;
    }

    public boolean isLowerThan(Version version) {
        return compareTo(version) < 0;
    }

    @Override
    public int compareTo(Version that) {
        if (that == null) {
            return 1;
        }

        String[] thisParts = this.getVersionString().split("\\.");
        String[] thatParts = that.getVersionString().split("\\.");

        int length = Math.max(thisParts.length, thatParts.length);

        for (int i = 0; i < length; i++) {
            int thisPart = i < thisParts.length ? Integer.parseInt(thisParts[i]) : 0;
            int thatPart = i < thatParts.length ? Integer.parseInt(thatParts[i]) : 0;

            if (thisPart < thatPart) {
                return -1;
            }
            if (thisPart > thatPart) {
                return 1;
            }
        }
        return 0;
    }

    @NonNull
    @Override
    public String toString() {
        return versionString;
    }
}