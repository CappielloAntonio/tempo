package com.cappielloantonio.play.subsonic.base;

public class SubsonicIncompatibilityException extends RuntimeException{
    private final Version serverApiVersion;
    private final Version minClientApiVersion;

    public SubsonicIncompatibilityException(Version serverApiVersion, Version minClientApiVersion) {
        super(String.format("Server API version %s is lower than minimal supported API version %s.", serverApiVersion, minClientApiVersion));

        this.serverApiVersion = serverApiVersion;
        this.minClientApiVersion = minClientApiVersion;
    }
}