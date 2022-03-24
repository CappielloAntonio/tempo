package com.cappielloantonio.play.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.cappielloantonio.play.subsonic.models.ArtistID3;
import com.cappielloantonio.play.subsonic.models.ArtistInfo2;
import com.cappielloantonio.play.subsonic.models.ArtistWithAlbumsID3;
import com.cappielloantonio.play.subsonic.models.SimilarArtistID3;
import com.cappielloantonio.play.util.MappingUtil;

import java.util.ArrayList;
import java.util.List;

public class Artist implements Parcelable {
    private static final String TAG = "Artist";

    public static final String DOWNLOADED = "DOWNLOADED";
    public static final String STARRED = "STARRED";

    public static final String ORDER_BY_NAME = "ORDER_BY_NAME";
    public static final String ORDER_BY_RANDOM = "ORDER_BY_RANDOM";

    private List<Album> albums;
    private List<Artist> similarArtists;

    private String id;
    private String name;
    private String primary;
    private String primaryBlurHash;
    private String backdrop;
    private String backdropBlurHash;
    private int albumCount;
    private boolean favorite;
    private String bio;
    private String imageUrl;
    private String lastfm;

    public Artist(ArtistID3 artistID3) {
        this.id = artistID3.getId();
        this.name = artistID3.getName();
        this.primary = artistID3.getCoverArtId();
        this.backdrop = artistID3.getCoverArtId();
        this.albumCount = artistID3.getAlbumCount();
        this.favorite = artistID3.getStarred() != null;
    }

    public Artist(ArtistWithAlbumsID3 artistWithAlbumsID3) {
        this.id = artistWithAlbumsID3.getId();
        this.name = artistWithAlbumsID3.getName();
        this.primary = artistWithAlbumsID3.getCoverArtId();
        this.backdrop = artistWithAlbumsID3.getCoverArtId();
        this.albumCount = artistWithAlbumsID3.getAlbumCount();
        this.albums = MappingUtil.mapAlbum(artistWithAlbumsID3.getAlbums());
        this.favorite = artistWithAlbumsID3.getStarred() != null;
        this.albums = MappingUtil.mapAlbum(artistWithAlbumsID3.getAlbums());
    }

    public Artist(SimilarArtistID3 similarArtistID3) {
        this.id = similarArtistID3.getId();
        this.name = similarArtistID3.getName();
        this.primary = similarArtistID3.getCoverArtId();
        this.backdrop = similarArtistID3.getCoverArtId();
        this.albumCount = similarArtistID3.getAlbumCount();
    }

    public Artist(ArtistInfo2 artistInfo2) {
        this.similarArtists = MappingUtil.mapSimilarArtist(artistInfo2.getSimilarArtists());
        this.bio = artistInfo2.getBiography();
        this.imageUrl = artistInfo2.getLargeImageUrl();
        this.lastfm = artistInfo2.getLastFmUrl();
    }

    public Artist(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Artist(Download download) {
        this.id = download.getArtistId();
        this.name = download.getArtistName();
    }

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

    public int getAlbumCount() {
        return albumCount;
    }

    public void setAlbumCount(int albumCount) {
        this.albumCount = albumCount;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public List<Artist> getSimilarArtists() {
        return similarArtists;
    }

    public void setSimilarArtists(List<Artist> similarArtists) {
        this.similarArtists = similarArtists;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLastfm() {
        return lastfm;
    }

    public void setLastfm(String lastfm) {
        this.lastfm = lastfm;
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
        this.albums = new ArrayList<>();
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