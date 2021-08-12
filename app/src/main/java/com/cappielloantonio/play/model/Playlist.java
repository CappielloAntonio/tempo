package com.cappielloantonio.play.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Playlist implements Parcelable {
    private String id;
    private String name;
    private String primary;
    private String blurHash;
    private int songCount;
    private long duration;

    public Playlist(com.cappielloantonio.play.subsonic.models.Playlist playlist) {
        this.id = playlist.getId();
        this.name = playlist.getName();
        this.primary = playlist.getCoverArtId();
        this.blurHash = playlist.getCoverArtId();
        this.songCount = playlist.getSongCount();
        this.duration = playlist.getDuration();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPrimary() {
        return primary;
    }

    public String getBlurHash() {
        return blurHash;
    }

    public int getSongCount() {
        return songCount;
    }

    public long getDuration() {
        return duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Playlist playlist = (Playlist) o;
        return id.equals(playlist.id);
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
        dest.writeString(this.name);
    }

    protected Playlist(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
    }

    public static final Creator<Playlist> CREATOR = new Creator<Playlist>() {
        public Playlist createFromParcel(Parcel source) {
            return new Playlist(source);
        }

        public Playlist[] newArray(int size) {
            return new Playlist[size];
        }
    };
}