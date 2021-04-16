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

    @ColumnInfo(name = "track_order")
    private int trackOrder;

    public Queue(String songID, int trackOrder) {
        this.songID = songID;
        this.trackOrder = trackOrder;
    }

    public String getSongID() {
        return songID;
    }

    public void setSongID(String songID) {
        this.songID = songID;
    }

    public int getTrackOrder() {
        return trackOrder;
    }

    public void setTrackOrder(int trackOrder) {
        this.trackOrder = trackOrder;
    }
}
