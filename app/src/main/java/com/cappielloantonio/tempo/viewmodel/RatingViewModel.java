package com.cappielloantonio.tempo.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.cappielloantonio.tempo.repository.AlbumRepository;
import com.cappielloantonio.tempo.repository.ArtistRepository;
import com.cappielloantonio.tempo.repository.SongRepository;
import com.cappielloantonio.tempo.subsonic.models.AlbumID3;
import com.cappielloantonio.tempo.subsonic.models.ArtistID3;
import com.cappielloantonio.tempo.subsonic.models.Child;

public class RatingViewModel extends AndroidViewModel {
    private final SongRepository songRepository;
    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;

    private Child song;
    private AlbumID3 album;
    private ArtistID3 artist;

    public RatingViewModel(@NonNull Application application) {
        super(application);

        songRepository = new SongRepository();
        albumRepository = new AlbumRepository();
        artistRepository = new ArtistRepository();
    }

    public Child getSong() {
        return song;
    }

    public LiveData<Child> getLiveSong() {
        return songRepository.getSong(song.getId());
    }

    public void setSong(Child song) {
        this.song = song;
        this.album = null;
        this.artist = null;
    }

    public AlbumID3 getAlbum() {
        return album;
    }

    public LiveData<AlbumID3> getLiveAlbum() {
        return albumRepository.getAlbum(album.getId());
    }

    public void setAlbum(AlbumID3 album) {
        this.song = null;
        this.album = album;
        this.artist = null;
    }

    public ArtistID3 getArtist() {
        return artist;
    }

    public LiveData<ArtistID3> getLiveArtist() {
        return artistRepository.getArtist(artist.getId());
    }

    public void setArtist(ArtistID3 artist) {
        this.song = null;
        this.album = null;
        this.artist = artist;
    }

    public void rate(int star) {
        if (song != null) {
            songRepository.setRating(song.getId(), star);
        } else if (album != null) {
            albumRepository.setRating(album.getId(), star);
        } else if (artist != null) {
            artistRepository.setRating(artist.getId(), star);
        }
    }
}
