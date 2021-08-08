package com.cappielloantonio.play.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "server")
public class Server {
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "id")
    private String serverId;

    @ColumnInfo(name = "server_name")
    private String serverName;

    @ColumnInfo(name = "username")
    private String username;

    @ColumnInfo(name = "address")
    private String address;

    @ColumnInfo(name = "token")
    private String token;

    @ColumnInfo(name = "salt")
    private String salt;

    @ColumnInfo(name = "timestamp")
    private long timestamp;

    public Server(@NonNull String serverId, String serverName, String username, String address, String token, String salt, long timestamp) {
        this.serverId = serverId;
        this.serverName = serverName;
        this.username = username;
        this.address = address;
        this.token = token;
        this.salt = salt;
        this.timestamp = timestamp;
    }

    @NonNull
    public String getServerId() {
        return serverId;
    }

    public void setServerId(@NonNull String serverId) {
        this.serverId = serverId;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
