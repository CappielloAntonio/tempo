package com.cappielloantonio.play.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.jellyfin.apiclient.model.dto.BaseItemDto;
import org.jellyfin.apiclient.model.dto.MediaSourceInfo;
import org.jellyfin.apiclient.model.entities.ImageType;
import org.jellyfin.apiclient.model.entities.MediaStream;

import java.time.Instant;
import java.util.UUID;

@Entity(tableName = "song")
public class Song implements Parcelable {
    @Ignore
    public static final String RECENTLY_PLAYED = "RECENTLY_PLAYED";

    @Ignore
    public static final String MOST_PLAYED = "MOST_PLAYED";

    @Ignore
    public static final String RECENTLY_ADDED = "RECENTLY_ADDED";

    @Ignore
    public static final String BY_GENRE = "BY_GENRE";

    @Ignore
    public static final String BY_GENRES = "BY_GENRES";

    @Ignore
    public static final String BY_ARTIST = "BY_ARTIST";

    @Ignore
    public static final String BY_YEAR = "BY_YEAR";

    @Ignore
    public static final String IS_FAVORITE = "IS_FAVORITE";

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "id")
    private String id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "trackNumber")
    private int trackNumber;

    @ColumnInfo(name = "discNumber")
    private int discNumber;

    @ColumnInfo(name = "year")
    private int year;

    @ColumnInfo(name = "duration")
    private long duration;

    @ColumnInfo(name = "albumId")
    private String albumId;

    @ColumnInfo(name = "albumName")
    private String albumName;

    @ColumnInfo(name = "artistId")
    private String artistId;

    @ColumnInfo(name = "artistName")
    private String artistName;

    @ColumnInfo(name = "primary")
    private String primary;

    @ColumnInfo(name = "blurHash")
    private String blurHash;

    @ColumnInfo(name = "favorite")
    private boolean favorite;

    @ColumnInfo(name = "path")
    private String path;

    @ColumnInfo(name = "size")
    private long size;

    @ColumnInfo(name = "container")
    private String container;

    @ColumnInfo(name = "codec")
    private String codec;

    @ColumnInfo(name = "sampleRate")
    private int sampleRate;

    @ColumnInfo(name = "bitRate")
    private int bitRate;

    @ColumnInfo(name = "bitDepth")
    private int bitDepth;

    @ColumnInfo(name = "channels")
    private int channels;

    @ColumnInfo(name = "added")
    private long added;

    @ColumnInfo(name = "play_count")
    private int playCount;

    @ColumnInfo(name = "last_play")
    private long lastPlay;

    public Song(@NonNull String id, String title, int trackNumber, int discNumber, int year, long duration, String albumId, String albumName, String artistId, String artistName, String primary, String blurHash, boolean favorite, String path, long size, String container, String codec, int sampleRate, int bitRate, int bitDepth, int channels, long added, int playCount, long lastPlay) {
        this.id = id;
        this.title = title;
        this.trackNumber = trackNumber;
        this.discNumber = discNumber;
        this.year = year;
        this.duration = duration;
        this.albumId = albumId;
        this.albumName = albumName;
        this.artistId = artistId;
        this.artistName = artistName;
        this.primary = primary;
        this.blurHash = blurHash;
        this.favorite = favorite;
        this.path = path;
        this.size = size;
        this.container = container;
        this.codec = codec;
        this.sampleRate = sampleRate;
        this.bitRate = bitRate;
        this.bitDepth = bitDepth;
        this.channels = channels;
        this.added = added;
        this.playCount = playCount;
        this.lastPlay = lastPlay;
    }

    @Ignore
    public Song(BaseItemDto itemDto) {
        this.id = itemDto.getId();
        this.title = itemDto.getName();
        this.trackNumber = itemDto.getIndexNumber() != null ? itemDto.getIndexNumber() : 0;
        this.discNumber = itemDto.getParentIndexNumber() != null ? itemDto.getParentIndexNumber() : 0;
        this.year = itemDto.getProductionYear() != null ? itemDto.getProductionYear() : 0;
        this.duration = itemDto.getRunTimeTicks() != null ? itemDto.getRunTimeTicks() / 10000 : 0;

        this.albumId = itemDto.getAlbumId();
        this.albumName = itemDto.getAlbum();

        if (itemDto.getAlbumArtists().size() != 0) {
            this.artistId = itemDto.getAlbumArtists().get(0).getId();
            this.artistName = itemDto.getAlbumArtists().get(0).getName();
        } else if (itemDto.getArtistItems().size() != 0) {
            this.artistId = itemDto.getArtistItems().get(0).getId();
            this.artistName = itemDto.getArtistItems().get(0).getName();
        }

        this.primary = itemDto.getAlbumPrimaryImageTag() != null ? albumId : null;
        if (itemDto.getImageBlurHashes() != null && itemDto.getImageBlurHashes().get(ImageType.Primary) != null) {
            this.blurHash = (String) itemDto.getImageBlurHashes().get(ImageType.Primary).values().toArray()[0];
        }

        this.favorite = itemDto.getUserData() != null && itemDto.getUserData().getIsFavorite();

        if (itemDto.getMediaSources() != null && itemDto.getMediaSources().get(0) != null) {
            MediaSourceInfo source = itemDto.getMediaSources().get(0);

            this.path = source.getPath();
            this.size = source.getSize() != null ? source.getSize() : 0;

            this.container = source.getContainer();
            this.bitRate = source.getBitrate() != null ? source.getBitrate() : 0;

            if (source.getMediaStreams() != null && source.getMediaStreams().size() != 0) {
                MediaStream stream = source.getMediaStreams().get(0);

                this.codec = stream.getCodec();
                this.sampleRate = stream.getSampleRate() != null ? stream.getSampleRate() : 0;
                this.bitDepth = stream.getBitDepth() != null ? stream.getBitDepth() : 0;
                this.channels = stream.getChannels() != null ? stream.getChannels() : 0;
            }
        }

        this.added = Instant.now().toEpochMilli();
        this.playCount = 0;
        this.lastPlay = 0;
    }

    @Ignore
    public Song(String title, String albumName) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.albumName = albumName;
    }

    @NonNull
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

    public String getCodec() {
        return codec;
    }

    public int getSampleRate() {
        return sampleRate;
    }

    public int getBitRate() {
        return bitRate;
    }

    public int getBitDepth() {
        return bitDepth;
    }

    public int getChannels() {
        return channels;
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

    public void setId(@NonNull String id) {
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

    public void setCodec(String codec) {
        this.codec = codec;
    }

    public void setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
    }

    public void setBitRate(int bitRate) {
        this.bitRate = bitRate;
    }

    public void setBitDepth(int bitDepth) {
        this.bitDepth = bitDepth;
    }

    public void setChannels(int channels) {
        this.channels = channels;
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

    public void nowPlaying() {
        this.playCount++;
        this.lastPlay = Instant.now().toEpochMilli();
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
        dest.writeString(this.codec);
        dest.writeInt(this.sampleRate);
        dest.writeInt(this.bitRate);
        dest.writeInt(this.bitDepth);
        dest.writeInt(this.channels);
        dest.writeLong(this.added);
        dest.writeInt(this.playCount);
        dest.writeLong(this.lastPlay);
    }

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
        this.codec = in.readString();
        this.sampleRate = in.readInt();
        this.bitRate = in.readInt();
        this.bitDepth = in.readInt();
        this.channels = in.readInt();
        this.added = in.readLong();
        this.playCount = in.readInt();
        this.lastPlay = in.readLong();
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
