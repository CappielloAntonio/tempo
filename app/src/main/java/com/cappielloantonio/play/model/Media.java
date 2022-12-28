package com.cappielloantonio.play.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import com.cappielloantonio.play.subsonic.models.Child;
import com.cappielloantonio.play.subsonic.models.PodcastEpisode;

public class Media implements Parcelable {
    private static final String TAG = "Media";

    public static final String MEDIA_TYPE_MUSIC = "music";
    public static final String MEDIA_TYPE_PODCAST = "podcast";
    public static final String MEDIA_TYPE_AUDIOBOOK = "audiobook";
    public static final String MEDIA_TYPE_VIDEO = "video";

    public static final float MEDIA_PLAYBACK_SPEED_080 = 0.8f;
    public static final float MEDIA_PLAYBACK_SPEED_100 = 1.0f;
    public static final float MEDIA_PLAYBACK_SPEED_125 = 1.25f;
    public static final float MEDIA_PLAYBACK_SPEED_150 = 1.50f;
    public static final float MEDIA_PLAYBACK_SPEED_175 = 1.75f;
    public static final float MEDIA_PLAYBACK_SPEED_200 = 2.0f;

    public static final String RECENTLY_PLAYED = "RECENTLY_PLAYED";
    public static final String MOST_PLAYED = "MOST_PLAYED";
    public static final String RECENTLY_ADDED = "RECENTLY_ADDED";
    public static final String BY_GENRE = "BY_GENRE";
    public static final String BY_GENRES = "BY_GENRES";
    public static final String BY_ARTIST = "BY_ARTIST";
    public static final String BY_YEAR = "BY_YEAR";
    public static final String STARRED = "STARRED";
    public static final String DOWNLOADED = "DOWNLOADED";
    public static final String FROM_ALBUM = "FROM_ALBUM";

    private String id;
    private String title;
    private String channelId;
    private String streamId;
    private String albumId;
    private String albumName;
    private String artistId;
    private String artistName;
    private String coverArtId;
    private int trackNumber;
    private int discNumber;
    private int year;
    private long duration;
    private String description;
    private String status;
    private boolean starred;
    private String path;
    private long size;
    private String container;
    private int bitrate;
    private String extension;
    private long added;
    private String type;
    private int playCount;
    private long lastPlay;
    private int rating;
    private long publishDate;

    public Media(Child child) {
        this.id = child.getId();
        this.title = child.getTitle();
        this.trackNumber = child.getTrack() != null ? child.getTrack() : 0;
        this.discNumber = child.getDiscNumber() != null ? child.getDiscNumber() : 0;
        this.year = child.getYear() != null ? child.getYear() : 0;
        this.duration = child.getDuration() != null ? child.getDuration() : 0;
        this.albumId = child.getAlbumId();
        this.albumName = child.getAlbum();
        this.artistId = child.getArtistId();
        this.artistName = child.getArtist();
        this.coverArtId = child.getCoverArtId();
        this.starred = child.getStarred() != null;
        this.path = child.getPath();
        this.size = child.getSize() != null ? child.getSize() : 0;
        this.container = child.getContentType();
        this.bitrate = child.getBitRate() != null ? child.getBitRate() : 0;
        this.extension = child.getSuffix();
        this.added = child.getCreated().getTime();
        this.playCount = 0;
        this.lastPlay = 0;
        this.rating = child.getUserRating() != null ? child.getUserRating() : 0;
        // this.type = MEDIA_TYPE_MUSIC;
        this.type = child.getType();
    }

    public Media(PodcastEpisode podcastEpisode) {
        this.id = podcastEpisode.getId();
        this.title = podcastEpisode.getTitle();
        this.albumName = podcastEpisode.getAlbum();
        this.artistName = podcastEpisode.getArtist();
        this.trackNumber = podcastEpisode.getTrack() != null ? podcastEpisode.getTrack() : 0;
        this.year = podcastEpisode.getYear();
        this.coverArtId = podcastEpisode.getCoverArtId();
        this.duration = podcastEpisode.getDuration();
        this.starred = podcastEpisode.getStarred() != null;
        this.streamId = podcastEpisode.getStreamId();
        this.channelId = podcastEpisode.getChannelId();
        this.description = podcastEpisode.getDescription();
        this.status = podcastEpisode.getStatus();
        this.publishDate = podcastEpisode.getPublishDate().getTime();
        this.container = podcastEpisode.getContentType();
        this.bitrate = podcastEpisode.getBitRate();
        this.extension = podcastEpisode.getSuffix();
        // this.type = MEDIA_TYPE_PODCAST;
        this.type = podcastEpisode.getType();
    }

    public Media(Queue queue) {
        this.id = queue.getId();
        this.title = queue.getTitle();
        this.albumId = queue.getAlbumId();
        this.albumName = queue.getAlbumName();
        this.artistId = queue.getArtistId();
        this.artistName = queue.getArtistName();
        this.coverArtId = queue.getCoverArtId();
        this.duration = queue.getDuration();
        this.streamId = queue.getStreamId();
        this.channelId = queue.getChannelId();
        this.publishDate = queue.getPublishingDate();
        this.container = queue.getContainer();
        this.bitrate = queue.getBitrate();
        this.extension = queue.getExtension();
        this.type = queue.getType();
    }

    public Media(Download download) {
        this.id = download.getMediaID();
        this.title = download.getTitle();
        this.albumId = download.getAlbumId();
        this.albumName = download.getAlbumName();
        this.artistId = download.getArtistId();
        this.artistName = download.getArtistName();
        this.trackNumber = download.getTrackNumber();
        this.coverArtId = download.getPrimary();
        this.duration = download.getDuration();
        this.container = download.getContainer();
        this.bitrate = download.getBitrate();
        this.extension = download.getExtension();
        this.type = download.getType();
    }

    public Media(Chronology item) {
        this.id = item.getTrackId();
        this.title = item.getTitle();
        this.albumId = item.getAlbumId();
        this.albumName = item.getAlbumName();
        this.artistId = item.getArtistId();
        this.artistName = item.getArtistName();
        this.coverArtId = item.getCoverArtId();
        this.duration = item.getDuration();
        this.container = item.getContainer();
        this.bitrate = item.getBitrate();
        this.extension = item.getExtension();
        this.type = MEDIA_TYPE_MUSIC;
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

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
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

    public int getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(int trackNumber) {
        this.trackNumber = trackNumber;
    }

    public int getDiscNumber() {
        return discNumber;
    }

    public void setDiscNumber(int discNumber) {
        this.discNumber = discNumber;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isStarred() {
        return starred;
    }

    public void setStarred(boolean starred) {
        this.starred = starred;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    public int getBitrate() {
        return bitrate;
    }

    public void setBitrate(int bitRate) {
        this.bitrate = bitRate;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public long getAdded() {
        return added;
    }

    public void setAdded(long added) {
        this.added = added;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPlayCount() {
        return playCount;
    }

    public void setPlayCount(int playCount) {
        this.playCount = playCount;
    }

    public long getLastPlay() {
        return lastPlay;
    }

    public void setLastPlay(long lastPlay) {
        this.lastPlay = lastPlay;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public long getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(long publishDate) {
        this.publishDate = publishDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Media song = (Media) o;
        return id.equals(song.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @NonNull
    @Override
    public String toString() {
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.channelId);
        dest.writeString(this.streamId);
        dest.writeString(this.albumId);
        dest.writeString(this.albumName);
        dest.writeString(this.artistId);
        dest.writeString(this.artistName);
        dest.writeString(this.coverArtId);
        dest.writeInt(this.trackNumber);
        dest.writeInt(this.discNumber);
        dest.writeInt(this.year);
        dest.writeLong(this.duration);
        dest.writeString(this.description);
        dest.writeString(this.status);
        dest.writeString(Boolean.toString(starred));
        dest.writeString(this.path);
        dest.writeLong(this.size);
        dest.writeString(this.container);
        dest.writeInt(this.bitrate);
        dest.writeString(this.extension);
        dest.writeLong(this.added);
        dest.writeString(this.type);
        dest.writeInt(this.playCount);
        dest.writeLong(this.lastPlay);
        dest.writeInt(this.rating);
        dest.writeLong(this.publishDate);
    }

    protected Media(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.channelId = in.readString();
        this.streamId = in.readString();
        this.albumId = in.readString();
        this.albumName = in.readString();
        this.artistId = in.readString();
        this.artistName = in.readString();
        this.coverArtId = in.readString();
        this.trackNumber = in.readInt();
        this.discNumber = in.readInt();
        this.year = in.readInt();
        this.duration = in.readLong();
        this.description = in.readString();
        this.status = in.readString();
        this.starred = Boolean.parseBoolean(in.readString());
        this.path = in.readString();
        this.size = in.readLong();
        this.container = in.readString();
        this.bitrate = in.readInt();
        this.extension = in.readString();
        this.added = in.readLong();
        this.type = in.readString();
        this.playCount = in.readInt();
        this.lastPlay = in.readLong();
        this.rating = in.readInt();
        this.publishDate = in.readLong();
    }

    public static final Creator<Media> CREATOR = new Creator<Media>() {
        public Media createFromParcel(Parcel source) {
            return new Media(source);
        }

        public Media[] newArray(int size) {
            return new Media[size];
        }
    };
}
