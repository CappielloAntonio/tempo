package com.cappielloantonio.play.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "server")
public class Server implements Parcelable {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Server server = (Server) o;
        return serverId.equals(server.getServerId());
    }

    @Override
    public int hashCode() {
        return serverId.hashCode();
    }

    @NonNull
    @Override
    public String toString() {
        return serverId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(serverId);
        dest.writeString(serverName);
        dest.writeString(username);
        dest.writeString(address);
        dest.writeString(token);
        dest.writeString(salt);
        dest.writeLong(timestamp);
    }

    protected Server(Parcel in) {
        this.serverId = in.readString();
        this.serverName = in.readString();
        this.username = in.readString();
        this.address = in.readString();
        this.token = in.readString();
        this.salt = in.readString();
        this.timestamp = in.readLong();
    }

    public static final Creator<Server> CREATOR = new Creator<Server>() {
        public Server createFromParcel(Parcel in) { return new Server(in); }

        public Server[] newArray(int size) { return new Server[size]; }
    };
}
