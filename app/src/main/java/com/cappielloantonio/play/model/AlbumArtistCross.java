package com.cappielloantonio.play.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "album_artist_cross")
public class AlbumArtistCross {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "album_id")
    private String albumId;

    @ColumnInfo(name = "artist_id")
    private String artistId;

    @ColumnInfo(name = "is_producer")
    private boolean isProducer;

    public AlbumArtistCross(String albumId, String artistId, boolean isProducer) {
        this.albumId = albumId;
        this.artistId = artistId;
        this.isProducer = isProducer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public boolean isProducer() {
        return isProducer;
    }

    public void setProducer(boolean producer) {
        isProducer = producer;
    }
}
