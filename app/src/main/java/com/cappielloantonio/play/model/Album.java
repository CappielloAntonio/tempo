package com.cappielloantonio.play.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.cappielloantonio.play.subsonic.models.AlbumID3;

public class Album implements Parcelable {
    private static final String TAG = "Album";

    public String id;
    public String title;
    public int year;
    public String artistId;
    public String artistName;
    public String primary;
    public String blurHash;
    public boolean favorite;

    public Album(AlbumID3 albumID3) {
        this.id = albumID3.getId();
        this.title = albumID3.getName();
        this.year = albumID3.getYear();
        this.artistId = albumID3.getArtistId();
        this.artistName = albumID3.getArtist();
        this.primary = albumID3.getCoverArtId();
        this.favorite = albumID3.getStarred() != null;
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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
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

    public String getPrimary() {
        return primary;
    }

    public void setPrimary(String primary) {
        this.primary = primary;
    }

    public String getBlurHash() {
        return blurHash;
    }

    public void setBlurHash(String blurHash) {
        this.blurHash = blurHash;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Album album = (Album) o;
        return id.equals(album.id);
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
        dest.writeString(title);
        dest.writeInt(year);
        dest.writeString(artistId);
        dest.writeString(artistName);
        dest.writeString(primary);
        dest.writeString(blurHash);
    }

    protected Album(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.year = in.readInt();
        this.artistId = in.readString();
        this.artistName = in.readString();
        this.primary = in.readString();
        this.blurHash = in.readString();
    }

    public static final Creator<Album> CREATOR = new Creator<Album>() {
        public Album createFromParcel(Parcel source) {
            return new Album(source);
        }

        public Album[] newArray(int size) {
            return new Album[size];
        }
    };
}
