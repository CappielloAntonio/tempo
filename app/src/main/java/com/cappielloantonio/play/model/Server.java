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

    @ColumnInfo(name = "password")
    private String password;

    @ColumnInfo(name = "address")
    private String address;

    @ColumnInfo(name = "timestamp")
    private long timestamp;

    @ColumnInfo(name = "low_security", defaultValue = "false")
    private boolean lowSecurity;

    public Server(@NonNull String serverId, String serverName, String username, String password, String address, long timestamp, boolean lowSecurity) {
        this.serverId = serverId;
        this.serverName = serverName;
        this.username = username;
        this.password = password;
        this.address = address;
        this.timestamp = timestamp;
        this.lowSecurity = lowSecurity;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isLowSecurity() {
        return lowSecurity;
    }

    public void setLowSecurity(boolean lowSecurity) {
        this.lowSecurity = lowSecurity;
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
        dest.writeLong(timestamp);
    }

    protected Server(Parcel in) {
        this.serverId = in.readString();
        this.serverName = in.readString();
        this.username = in.readString();
        this.address = in.readString();
        this.timestamp = in.readLong();
    }

    public static final Creator<Server> CREATOR = new Creator<Server>() {
        public Server createFromParcel(Parcel in) {
            return new Server(in);
        }

        public Server[] newArray(int size) {
            return new Server[size];
        }
    };
}
