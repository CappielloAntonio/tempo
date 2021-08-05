package com.cappielloantonio.play.model;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import com.cappielloantonio.play.subsonic.models.Child;

import java.time.Instant;
import java.util.UUID;

public class Song implements Parcelable {
    private static final String TAG = "SongClass";

    public static final String RECENTLY_PLAYED = "RECENTLY_PLAYED";
    public static final String MOST_PLAYED = "MOST_PLAYED";
    public static final String RECENTLY_ADDED = "RECENTLY_ADDED";
    public static final String BY_GENRE = "BY_GENRE";
    public static final String BY_GENRES = "BY_GENRES";
    public static final String BY_ARTIST = "BY_ARTIST";
    public static final String BY_YEAR = "BY_YEAR";
    public static final String STARRED = "STARRED";
    public static final String DOWNLOADED = "DOWNLOADED";

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
    private int playCount;
    private long lastPlay;
    private boolean offline;

    public Song() {
        this.id = UUID.randomUUID().toString();
    }

    public Song(Child child) {
        this.id = child.getId();
        this.title = child.getTitle();
        this.trackNumber = child.getTrack();
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
        this.offline = false;
    }

    public Song(Queue queue) {
        this.id = queue.getSongID();
        this.title = queue.getTitle();
        this.albumId = queue.getAlbumId();
        this.albumName = queue.getAlbumName();
        this.artistId = queue.getArtistId();
        this.artistName = queue.getArtistName();
        this.primary = queue.getPrimary();
        this.duration = queue.getDuration();
    }

    public Song(Download download) {
        this.id = download.getSongID();
        this.title = download.getTitle();
        this.albumId = download.getAlbumId();
        this.albumName = download.getAlbumName();
        this.artistId = download.getArtistId();
        this.artistName = download.getArtistName();
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

    public boolean isOffline() {
        return offline;
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

    public void setOffline(boolean offline) {
        this.offline = offline;
    }

    /*
        Log.i(TAG, "increasePlayCount: " + isIncreased);
     * Incremento il numero di ascolti solo se ho ascoltato la canzone da più tempo di:
     * tempo dell'ultimo ascolto - (durata_canzone / 2)
     * Ritorno un booleano
     * Se vero, allora SongRepository scriverà nd DB l'incremento dell'ascolto
     * Se falso, SongRepository non scriverà nulla nel db
     */
    public boolean nowPlaying() {
        long startPlayTime = Instant.now().toEpochMilli();

        if (startPlayTime - (getDuration() / 2) > getLastPlay()) {
            this.playCount++;
            this.lastPlay = startPlayTime;
            return true;
        }

        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Song song = (Song) o;
        return id.equals(song.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @SuppressLint("NewApi")
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
        dest.writeBoolean(this.offline);
    }

    @SuppressLint("NewApi")
    protected Song(Parcel in) {
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
        this.offline = in.readBoolean();
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        public Song createFromParcel(Parcel source) {
            return new Song(source);
        }

        public Song[] newArray(int size) {
            return new Song[size];
        }
    };
}
