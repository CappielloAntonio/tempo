package com.cappielloantonio.play.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.util.MusicUtil;
import com.cappielloantonio.play.util.PreferenceUtil;

import java.util.UUID;

@Entity(tableName = "download")
public class Download {
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "id")
    private String id;

    @ColumnInfo(name = "mediaId")
    private String mediaID;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "albumId")
    private String albumId;

    @ColumnInfo(name = "albumName")
    private String albumName;

    @ColumnInfo(name = "artistId")
    private String artistId;

    @ColumnInfo(name = "artistName")
    private String artistName;

    @ColumnInfo(name = "trackNumber")
    private int trackNumber;

    @ColumnInfo(name = "primary")
    private String primary;

    @ColumnInfo(name = "duration")
    private long duration;

    @ColumnInfo(name = "server")
    private String server;

    @ColumnInfo(name = "playlistId")
    private String playlistId;

    @ColumnInfo(name = "playlistName")
    private String playlistName;

    @ColumnInfo(name = "type")
    private String type;

    public Download(@NonNull String id, String mediaID, String title, String albumId, String albumName, String artistId, String artistName, int trackNumber, String primary, long duration, String server, String playlistId, String playlistName, String type) {
        this.id = id;
        this.mediaID = mediaID;
        this.title = title;
        this.albumId = albumId;
        this.albumName = albumName;
        this.artistId = artistId;
        this.artistName = artistName;
        this.trackNumber = trackNumber;
        this.primary = primary;
        this.duration = duration;
        this.server = server;
        this.playlistId = playlistId;
        this.playlistName = playlistName;
        this.type = type;
    }

    public Download(Media media, String playlistId, String playlistName) {
        this.id = UUID.randomUUID().toString();
        this.mediaID = media.getId();
        this.title = media.getTitle();
        this.albumId = media.getAlbumId();
        this.albumName = media.getAlbumName();
        this.artistId = media.getArtistId();
        this.artistName = MusicUtil.normalizedArtistName(media.getArtistName());
        this.trackNumber = media.getTrackNumber();
        this.primary = media.getCoverArtId();
        this.duration = media.getDuration();
        this.server = PreferenceUtil.getInstance(App.getInstance()).getServerId();
        this.playlistId = playlistId;
        this.playlistName = playlistName;
        this.type = media.getType();
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getMediaID() {
        return mediaID;
    }

    public void setMediaID(String mediaID) {
        this.mediaID = mediaID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public int getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(int trackNumber) {
        this.trackNumber = trackNumber;
    }

    public String getPrimary() {
        return primary;
    }

    public void setPrimary(String primary) {
        this.primary = primary;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
