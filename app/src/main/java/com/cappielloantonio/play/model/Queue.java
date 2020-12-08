package com.cappielloantonio.play.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "queue")
public class Queue {
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "id")
    private String songID;

    @ColumnInfo(name = "last_played")
    private long lastPlayed;

    public Queue(String songID, long lastPlayed) {
        this.songID = songID;
        this.lastPlayed = lastPlayed;
    }

    public String getSongID() {
        return songID;
    }

    public void setSongID(String songID) {
        this.songID = songID;
    }

    public long getLastPlayed() {
        return lastPlayed;
    }

    public void setLastPlayed(long lastPlayed) {
        this.lastPlayed = lastPlayed;
    }
}
