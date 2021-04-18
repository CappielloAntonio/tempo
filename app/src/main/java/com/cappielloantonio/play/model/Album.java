package com.cappielloantonio.play.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.jellyfin.apiclient.model.dto.BaseItemDto;
import org.jellyfin.apiclient.model.entities.ImageType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(tableName = "album")
public class Album implements Parcelable {
    @Ignore
    public List<Song> songs;

    /*
    * TODO: Da capire chi tra albumArtist e artistItems sono i compositori e suonatori dell'album, oppure le comparse
    * In teoria AlbumArtist sono i creatori, mentre ArtistItems le comparse
    */
    @Ignore
    public List<Artist> albumArtists;

    @Ignore
    public List<Artist> artistItems;

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "id")
    public String id;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "year")
    public int year;

    @ColumnInfo(name = "artistId")
    public String artistId;

    @ColumnInfo(name = "artistName")
    public String artistName;

    @ColumnInfo(name = "primary")
    public String primary;

    @ColumnInfo(name = "blurHash")
    public String blurHash;

    public Album(@NonNull String id, String title, int year, String artistId, String artistName, String primary, String blurHash) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.artistId = artistId;
        this.artistName = artistName;
        this.primary = primary;
        this.blurHash = blurHash;
    }

    @Ignore
    public Album(BaseItemDto itemDto) {
        this.id = itemDto.getId();
        this.title = itemDto.getName();
        this.year = itemDto.getProductionYear() != null ? itemDto.getProductionYear() : 0;

        albumArtists = new ArrayList<>();
        artistItems = new ArrayList<>();

        if (itemDto.getAlbumArtists().size() != 0) {
            this.artistId = itemDto.getAlbumArtists().get(0).getId();
            this.artistName = itemDto.getAlbumArtists().get(0).getName();

            itemDto.getAlbumArtists().forEach(artist -> {
                albumArtists.add(new Artist(artist.getId(), artist.getName()));
            });
        }
        else if (itemDto.getArtistItems().size() != 0) {
            this.artistId = itemDto.getArtistItems().get(0).getId();
            this.artistName = itemDto.getArtistItems().get(0).getName();

            itemDto.getArtistItems().forEach(artist -> {
                artistItems.add(new Artist(artist.getId(), artist.getName()));
            });
        }

        this.primary = itemDto.getImageTags().containsKey(ImageType.Primary) ? id : null;
        if (itemDto.getImageBlurHashes() != null && itemDto.getImageBlurHashes().get(ImageType.Primary) != null) {
            this.blurHash = (String) itemDto.getImageBlurHashes().get(ImageType.Primary).values().toArray()[0];
        }

        this.songs = new ArrayList<>();
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
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
        this.songs = new ArrayList<>();

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
