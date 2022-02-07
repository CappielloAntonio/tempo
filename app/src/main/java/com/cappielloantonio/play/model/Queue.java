package com.cappielloantonio.play.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "queue")
public class Queue {
    @PrimaryKey
    @ColumnInfo(name = "track_order")
    private int trackOrder;

    @ColumnInfo(name = "id")
    private String id;

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

    @ColumnInfo(name = "cover_art_id")
    private String coverArtId;

    @ColumnInfo(name = "duration")
    private long duration;

    @ColumnInfo(name = "last_play", defaultValue = "0")
    private long lastPlay;

    @ColumnInfo(name = "playing_changed", defaultValue = "0")
    private long playingChanged;

    @ColumnInfo(name = "stream_id")
    private String streamId;

    @ColumnInfo(name = "channel_id")
    private String channelId;

    @ColumnInfo(name = "publishing_date", defaultValue = "0")
    private long publishingDate;

    @ColumnInfo(name = "media_type")
    private String type;

    public Queue(int trackOrder, String id, String title, String albumId, String albumName, String artistId, String artistName, String coverArtId, long duration, long lastPlay, long playingChanged, String streamId, String channelId, long publishingDate, String type) {
        this.trackOrder = trackOrder;
        this.id = id;
        this.title = title;
        this.albumId = albumId;
        this.albumName = albumName;
        this.artistId = artistId;
        this.artistName = artistName;
        this.coverArtId = coverArtId;
        this.duration = duration;
        this.lastPlay = lastPlay;
        this.playingChanged = playingChanged;
        this.streamId = streamId;
        this.channelId = channelId;
        this.publishingDate = publishingDate;
        this.type = type;
    }

    public int getTrackOrder() {
        return trackOrder;
    }

    public void setTrackOrder(int trackOrder) {
        this.trackOrder = trackOrder;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getCoverArtId() {
        return coverArtId;
    }

    public void setCoverArtId(String coverArtId) {
        this.coverArtId = coverArtId;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getLastPlay() {
        return lastPlay;
    }

    public void setLastPlay(long lastPlay) {
        this.lastPlay = lastPlay;
    }

    public long getPlayingChanged() {
        return playingChanged;
    }

    public void setPlayingChanged(long playingChanged) {
        this.playingChanged = playingChanged;
    }

    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public long getPublishingDate() {
        return publishingDate;
    }

    public void setPublishingDate(long publishingDate) {
        this.publishingDate = publishingDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
