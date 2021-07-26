package com.cappielloantonio.play.viewmodel;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.cappielloantonio.play.model.Playlist;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.repository.AlbumRepository;
import com.cappielloantonio.play.repository.ArtistRepository;
import com.cappielloantonio.play.repository.GenreRepository;
import com.cappielloantonio.play.repository.PlaylistRepository;
import com.cappielloantonio.play.repository.PlaylistSongRepository;
import com.cappielloantonio.play.repository.SongRepository;
import com.cappielloantonio.play.subsonic.models.AlbumID3;
import com.cappielloantonio.play.subsonic.models.ArtistID3;
import com.cappielloantonio.play.subsonic.models.Child;
import com.cappielloantonio.play.subsonic.models.Genre;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SyncViewModel extends AndroidViewModel {
    private static final String TAG = "SyncViewModel";
    
    private boolean syncAlbum = false;
    private boolean syncArtist = false;
    private boolean syncGenres = false;
    private boolean syncPlaylist = false;
    private boolean syncSong = false;

    private ArrayList<AlbumID3> albumList = new ArrayList<>();
    private ArrayList<ArtistID3> artistList = new ArrayList<>();
    private ArrayList<Genre> genreList = new ArrayList<>();
    private ArrayList<Playlist> playlistList = new ArrayList<>();
    private ArrayList<Song> songList = new ArrayList<>();
    private ArrayList<Child> childList = new ArrayList<>();

    private SongRepository songRepository;
    private AlbumRepository albumRepository;
    private ArtistRepository artistRepository;
    private PlaylistRepository playlistRepository;
    private GenreRepository genreRepository;
    private PlaylistSongRepository playlistSongRepository;

    public SyncViewModel(@NonNull Application application) {
        super(application);

        songRepository = new SongRepository(application);
        albumRepository = new AlbumRepository(application);
        artistRepository = new ArtistRepository(application);
        playlistRepository = new PlaylistRepository(application);
        genreRepository = new GenreRepository(application);
        playlistSongRepository = new PlaylistSongRepository(application);
    }

    public void setArguemnts(Bundle bundle) {
        syncAlbum = bundle.getBoolean("sync_album", false);
        syncArtist = bundle.getBoolean("sync_artist", false);
        syncGenres = bundle.getBoolean("sync_genres", false);
        syncPlaylist = bundle.getBoolean("sync_playlist", false);
        syncSong = bundle.getBoolean("sync_song", false);
    }

    public boolean isSyncAlbum() {
        return syncAlbum;
    }

    public boolean isSyncArtist() {
        return syncArtist;
    }

    public boolean isSyncGenres() {
        return syncGenres;
    }

    public boolean isSyncPlaylist() {
        return syncPlaylist;
    }

    public boolean isSyncSong() {
        return syncSong;
    }

    public ArrayList<AlbumID3> getAlbumList() {
        return albumList;
    }

    public void addToAlbumList(List<AlbumID3> albumList) {
        this.albumList.addAll(albumList);
    }

    public ArrayList<ArtistID3> getArtistList() {
        return artistList;
    }

    public void setArtistList(ArrayList<ArtistID3> artistList) {
        this.artistList = artistList;
    }

    public ArrayList<Genre> getGenreList() {
        return genreList;
    }

    public void setGenreList(ArrayList<Genre> genreList) {
        this.genreList = genreList;
    }

    public ArrayList<Playlist> getPlaylistList() {
        return playlistList;
    }

    public void setPlaylistList(ArrayList<Playlist> playlistList) {
        this.playlistList = playlistList;
    }

    public ArrayList<Song> getSongList() {
        return songList;
    }

    public void setSongList(ArrayList<Song> songList) {
        this.songList = songList;
    }

    public ArrayList<Child> getChildList() {
        return childList;
    }

    public void addToChildList(ArrayList<Child> childList) {
        this.childList.addAll(childList);
    }

    public Map<Integer, Song> getCatalogue() {
        Map<Integer, Song> map = new HashMap<>();

        for (Song song : songRepository.getCatalogue()) {
            map.put(song.hashCode(), song);
        }

        return map;
    }
}
