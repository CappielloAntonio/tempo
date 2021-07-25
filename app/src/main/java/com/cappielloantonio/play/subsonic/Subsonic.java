package com.cappielloantonio.play.subsonic;

import com.cappielloantonio.play.subsonic.api.system.SystemClient;
import com.cappielloantonio.play.subsonic.base.Version;

import java.util.HashMap;
import java.util.Map;

public class Subsonic {
    private final SubsonicPreferences preferences;

    private static final Version API_MAX_VERSION = Version.of("1.15.0");

    private Version apiVersion = API_MAX_VERSION;

    private SystemClient systemClient;

    public Subsonic(SubsonicPreferences preferences) {
        this.preferences = preferences;
    }

    public SubsonicPreferences getPreferences() {
        return preferences;
    }

    public Version getApiVersion() {
        return apiVersion;
    }

    public SystemClient getSystemClient() {
        if (systemClient == null) {
            systemClient = new SystemClient(this);
        }
        return systemClient;
    }

    public String getUrl() {
        return preferences.getServerUrl() + "/rest/";
    }

    public Map<String, String> getParams() {
        Map<String, String> params = new HashMap<>();
        params.put("u", preferences.getUsername());
        params.put("s", preferences.getAuthentication().getSalt());
        params.put("t", preferences.getAuthentication().getToken());
        params.put("v", getApiVersion().getVersionString());
        params.put("c", preferences.getClientName());
        params.put("f", "xml");

        return params;
    }
}
