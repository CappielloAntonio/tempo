package com.cappielloantonio.play.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.cappielloantonio.play.subsonic.models.Child;

import java.util.Date;
import java.util.UUID;

public class Media implements Parcelable {
    private static final String TAG = "Media";

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
    private int trackNumber;
    private int discNumber;
    private int year;
    private long duration;
    private String albumId;
    private String albumName;
    private String artistId;
    private String artistName;
    private String primary;
    private String blurHash;
    private boolean favorite;
    private String path;
    private long size;
    private String container;
    private int bitRate;
    private long added;
    private String type;
    private int playCount;
    private long lastPlay;
    private int rating;

    public Media() {
        this.id = UUID.randomUUID().toString();
    }

    public Media(Child child) {
        this.id = child.getId();
        this.title = child.getTitle();
        this.trackNumber = child.getTrack() != null ? child.getTrack() : 0;
        this.discNumber = child.getDiscNumber() != null ? child.getDiscNumber() : 0;
        this.year = child.getYear() != null ? child.getYear() : 0;
        this.duration = child.getDuration();
        this.albumId = child.getAlbumId();
        this.albumName = child.getAlbum();
        this.artistId = child.getArtistId();
        this.artistName = child.getArtist();
        this.primary = child.getCoverArtId();
        this.favorite = child.getStarred() != null;
        this.path = child.getPath();
        this.size = child.getSize();
        this.container = child.getContentType();
        this.bitRate = child.getBitRate();
        this.added = child.getCreated().getTime();
        this.playCount = 0;
        this.lastPlay = 0;
        this.rating = child.getUserRating() != null ? child.getUserRating() : 0;
    }

    public Media(Queue queue) {
        this.id = queue.getSongID();
        this.title = queue.getTitle();
        this.albumId = queue.getAlbumId();
        this.albumName = queue.getAlbumName();
        this.artistId = queue.getArtistId();
        this.artistName = queue.getArtistName();
        this.primary = queue.getPrimary();
        this.duration = queue.getDuration();
    }

    public Media(Download download) {
        this.id = download.getSongID();
        this.title = download.getTitle();
        this.albumId = download.getAlbumId();
        this.albumName = download.getAlbumName();
        this.artistId = download.getArtistId();
        this.artistName = download.getArtistName();
        this.trackNumber = download.getTrackNumber();
        this.primary = download.getPrimary();
        this.duration = download.getDuration();
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getTrackNumber() {
        return trackNumber;
    }

    public int getDiscNumber() {
        return discNumber;
    }

    public int getYear() {
        return year;
    }

    public long getDuration() {
        return duration;
    }

    public String getAlbumId() {
        return albumId;
    }

    public String getAlbumName() {
        return albumName;
    }

    public String getArtistId() {
        return artistId;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getPrimary() {
        return primary;
    }

    public String getBlurHash() {
        return blurHash;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public String getPath() {
        return path;
    }

    public long getSize() {
        return size;
    }

    public String getContainer() {
        return container;
    }

    public int getBitRate() {
        return bitRate;
    }

    public long getAdded() {
        return added;
    }

    public int getPlayCount() {
        return playCount;
    }

    public long getLastPlay() {
        return lastPlay;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTrackNumber(int trackNumber) {
        this.trackNumber = trackNumber;
    }

    public void setDiscNumber(int discNumber) {
        this.discNumber = discNumber;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public void setPrimary(String primary) {
        this.primary = primary;
    }

    public void setBlurHash(String blurHash) {
        this.blurHash = blurHash;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    public void setBitRate(int bitRate) {
        this.bitRate = bitRate;
    }

    public void setAdded(long added) {
        this.added = added;
    }

    public void setLastPlay(long lastPlay) {
        this.lastPlay = lastPlay;
    }

    public void setPlayCount(int playCount) {
        this.playCount = playCount;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
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
        dest.writeInt(this.trackNumber);
        dest.writeInt(this.discNumber);
        dest.writeInt(this.year);
        dest.writeLong(this.duration);
        dest.writeString(this.albumId);
        dest.writeString(this.albumName);
        dest.writeString(this.artistId);
        dest.writeString(this.artistName);
        dest.writeString(this.primary);
        dest.writeString(Boolean.toString(favorite));
        dest.writeString(this.blurHash);
        dest.writeString(this.path);
        dest.writeLong(this.size);
        dest.writeString(this.container);
        dest.writeInt(this.bitRate);
        dest.writeLong(this.added);
        dest.writeInt(this.playCount);
        dest.writeLong(this.lastPlay);
    }

    protected Media(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.trackNumber = in.readInt();
        this.discNumber = in.readInt();
        this.year = in.readInt();
        this.duration = in.readLong();
        this.albumId = in.readString();
        this.albumName = in.readString();
        this.artistId = in.readString();
        this.artistName = in.readString();
        this.primary = in.readString();
        this.favorite = Boolean.parseBoolean(in.readString());
        this.blurHash = in.readString();
        this.path = in.readString();
        this.size = in.readLong();
        this.container = in.readString();
        this.bitRate = in.readInt();
        this.added = in.readLong();
        this.playCount = in.readInt();
        this.lastPlay = in.readLong();
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
