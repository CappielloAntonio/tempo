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

@Entity(tableName = "song_genre_cross")
public class SongGenreCross implements Parcelable {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "song_id")
    private String songId;

    @ColumnInfo(name = "genre_id")
    private String genreId;

    public SongGenreCross(String songId, String genreId) {
        this.songId = songId;
        this.genreId = genreId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }

    public String getGenreId() {
        return genreId;
    }

    public void setGenreId(String genreId) {
        this.genreId = genreId;
    }

    protected SongGenreCross(Parcel in) {
        id = in.readInt();
        songId = in.readString();
        genreId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(songId);
        dest.writeString(genreId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SongGenreCross> CREATOR = new Creator<SongGenreCross>() {
        @Override
        public SongGenreCross createFromParcel(Parcel in) {
            return new SongGenreCross(in);
        }

        @Override
        public SongGenreCross[] newArray(int size) {
            return new SongGenreCross[size];
        }
    };
}
