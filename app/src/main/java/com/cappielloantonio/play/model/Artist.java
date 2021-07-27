package com.cappielloantonio.play.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.cappielloantonio.play.subsonic.models.ArtistID3;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "artist")
public class Artist implements Parcelable {
    private static final String TAG = "Artist";
    @Ignore
    public List<Genre> genres;
    @Ignore
    public List<Album> albums;
    @Ignore
    public List<Song> songs;

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "id")
    public String id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "primary")
    public String primary;

    @ColumnInfo(name = "primary_blurHash")
    public String primaryBlurHash;

    @ColumnInfo(name = "backdrop")
    public String backdrop;

    @ColumnInfo(name = "backdrop_blurHash")
    public String backdropBlurHash;

    public Artist(@NonNull String id, String name, String primary, String primaryBlurHash, String backdrop, String backdropBlurHash) {
        this.id = id;
        this.name = name;
        this.primary = primary;
        this.primaryBlurHash = primaryBlurHash;
        this.backdrop = backdrop;
        this.backdropBlurHash = backdropBlurHash;
    }

    public Artist(ArtistID3 artistID3) {
        this.id = artistID3.getId();
        this.name = artistID3.getName();
        this.primary = artistID3.getCoverArtId() != null ? artistID3.getCoverArtId() : artistID3.getId();
        this.backdrop = artistID3.getCoverArtId();
    }

    @Ignore
    public Artist(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrimary() {
        return primary;
    }

    public void setPrimary(String primary) {
        this.primary = primary;
    }

    public String getPrimaryBlurHash() {
        return primaryBlurHash;
    }

    public void setPrimaryBlurHash(String primaryBlurHash) {
        this.primaryBlurHash = primaryBlurHash;
    }

    public String getBackdrop() {
        return backdrop;
    }

    public void setBackdrop(String backdrop) {
        this.backdrop = backdrop;
    }

    public String getBackdropBlurHash() {
        return backdropBlurHash;
    }

    public void setBackdropBlurHash(String backdropBlurHash) {
        this.backdropBlurHash = backdropBlurHash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Artist artist = (Artist) o;
        return id.equals(artist.id);
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
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(primary);
        dest.writeString(primaryBlurHash);
        dest.writeString(backdrop);
        dest.writeString(backdropBlurHash);
    }

    protected Artist(Parcel in) {
        this.genres = new ArrayList<>();
        this.albums = new ArrayList<>();
        this.songs = new ArrayList<>();
        this.id = in.readString();
        this.name = in.readString();
        this.primary = in.readString();
        this.primaryBlurHash = in.readString();
        this.backdrop = in.readString();
        this.backdropBlurHash = in.readString();
    }

    public static final Parcelable.Creator<Artist> CREATOR = new Parcelable.Creator<Artist>() {
        @Override
        public Artist createFromParcel(Parcel source) {
            return new Artist(source);
        }

        @Override
        public Artist[] newArray(int size) {
            return new Artist[size];
        }
    };
}