package com.cappielloantonio.play.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.cappielloantonio.play.subsonic.models.AlbumID3;
import com.cappielloantonio.play.subsonic.models.AlbumInfo;
import com.cappielloantonio.play.subsonic.models.AlbumWithSongsID3;
import com.cappielloantonio.play.util.MappingUtil;

import java.util.List;

public class Album implements Parcelable {
    private static final String TAG = "Album";

    public static final String RECENTLY_PLAYED = "RECENTLY_PLAYED";
    public static final String MOST_PLAYED = "MOST_PLAYED";
    public static final String RECENTLY_ADDED = "RECENTLY_ADDED";
    public static final String STARRED = "STARRED";

    private String id;
    private String title;
    private int year;
    private String artistId;
    private String artistName;
    private String primary;
    private String blurHash;
    private boolean favorite;
    private List<Song> songs;
    private String notes;

    public Album(AlbumID3 albumID3) {
        this.id = albumID3.getId();
        this.title = albumID3.getName();
        this.year = albumID3.getYear() != null ? albumID3.getYear() : 0;
        this.artistId = albumID3.getArtistId();
        this.artistName = albumID3.getArtist();
        this.primary = albumID3.getCoverArtId();
        this.favorite = albumID3.getStarred() != null;
    }

    public Album(AlbumWithSongsID3 albumWithSongsID3) {
        this.id = albumWithSongsID3.getId();
        this.title = albumWithSongsID3.getName();
        this.year = albumWithSongsID3.getYear() != null ? albumWithSongsID3.getYear() : 0;
        this.artistId = albumWithSongsID3.getArtistId();
        this.artistName = albumWithSongsID3.getArtist();
        this.primary = albumWithSongsID3.getCoverArtId();
        this.favorite = albumWithSongsID3.getStarred() != null;
        this.songs = MappingUtil.mapSong(albumWithSongsID3.getSongs());
    }

    public Album(Download download) {
        this.id = download.getAlbumId();
        this.title = download.getAlbumName();
        this.artistId = download.getArtistId();
        this.artistName = download.getArtistName();
        this.primary = download.getPrimary();
    }

    public Album(AlbumInfo info) {
        this.notes = info.getNotes();
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

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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
