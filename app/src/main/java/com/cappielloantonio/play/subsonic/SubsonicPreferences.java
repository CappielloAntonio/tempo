package com.cappielloantonio.play.subsonic;

import com.cappielloantonio.play.subsonic.utils.StringUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class SubsonicPreferences {
    private String serverUrl;
    private String username;
    private String clientName = "SubsonicJavaClient";
    private int streamBitRate = 192;
    private String streamFormat = "mp3";

    private final SubsonicAuthentication authentication;

    public SubsonicPreferences(String serverUrl, String username, String password) {
        this.serverUrl = serverUrl;
        this.username = username;
        this.authentication = new SubsonicAuthentication(password);
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getClientName() {
        return clientName;
    }

    public int getStreamBitRate() {
        return streamBitRate;
    }

    public String getStreamFormat() {
        return streamFormat;
    }

    public SubsonicAuthentication getAuthentication() {
        return authentication;
    }

    public void setPassword(String password) {
        authentication.update(password);
    }

    public static class SubsonicAuthentication {
        private String salt;
        private String token;

        public SubsonicAuthentication(String password) {
            update(password);
        }

        public String getSalt() {
            return salt;
        }

        public String getToken() {
            return token;
        }

        void update(String password) {
            this.salt = UUID.randomUUID().toString();
            this.token = StringUtil.tokenize(password + salt);
        }
    }
}
