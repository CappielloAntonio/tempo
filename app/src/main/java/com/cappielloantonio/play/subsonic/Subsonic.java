package com.cappielloantonio.play.subsonic;

import com.cappielloantonio.play.subsonic.base.Version;

import java.util.List;
import java.util.Map;

public class Subsonic {
    private final SubsonicPreferences preferences;

    private static final Version API_MIN_VERSION = Version.of("1.13.0");
    private static final Version API_MAX_VERSION = Version.of("1.15.0");

    private Version apiVersion = API_MAX_VERSION;

    public Subsonic(SubsonicPreferences preferences) {
        this.preferences = preferences;
    }

    public SubsonicPreferences getPreferences() {
        return preferences;
    }

    public static Version getApiMinVersion() {
        return API_MIN_VERSION;
    }

    public static Version getApiMaxVersion() {
        return API_MAX_VERSION;
    }

    public Version getApiVersion() {
        return apiVersion;
    }

    public String createUrl(String path, Map<String, List<String>> params) {
        final StringBuilder sb = new StringBuilder(preferences.getServerUrl());

        sb.append("/rest/").append(path);
        sb.append("?u=").append(preferences.getUsername());
        sb.append("&s=").append(preferences.getAuthentication().getSalt());
        sb.append("&t=").append(preferences.getAuthentication().getToken());
        sb.append("&v=").append(getApiVersion().getVersionString());
        sb.append("&c=").append(preferences.getClientName());
        sb.append("&f=").append("xml");

        params.keySet().forEach(k -> params.get(k).forEach(v -> sb.append("&").append(k).append("=").append(v)));

        return sb.toString().replace("//rest", "/rest");
    }
}
