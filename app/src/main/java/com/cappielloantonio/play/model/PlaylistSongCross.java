package com.cappielloantonio.play.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "playlist_song_cross", primaryKeys = {"playlist_id","song_id"})
public class PlaylistSongCross {
    @NonNull
    @ColumnInfo(name = "playlist_id")
    private String playlistId;

    @NonNull
    @ColumnInfo(name = "song_id")
    private String songId;

    public PlaylistSongCross(String playlistId, String songId) {
        this.playlistId = playlistId;
        this.songId = songId;
    }

    public String getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
    }

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }
}
