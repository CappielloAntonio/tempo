package com.cappielloantonio.play.subsonic;

import com.cappielloantonio.play.subsonic.utils.StringUtil;

import java.util.UUID;

public class SubsonicPreferences {
    private String serverUrl;
    private String username;
    private String clientName = "Play for Subsonic";

    private SubsonicAuthentication authentication;

    public SubsonicPreferences(String serverUrl, String username, String password, String token, String salt) {
        this.serverUrl = serverUrl;
        this.username = username;
        if(password != null) this.authentication = new SubsonicAuthentication(password);
        if(token != null && salt != null) this.authentication = new SubsonicAuthentication(token, salt);
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

        public SubsonicAuthentication(String token, String salt) {
            this.token = token;
            this.salt = salt;
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
