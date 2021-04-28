package com.cappielloantonio.play.viewmodel;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.cappielloantonio.play.model.Album;
import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.model.Genre;
import com.cappielloantonio.play.model.Playlist;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.repository.SongRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SyncViewModel extends AndroidViewModel {
    private static final String TAG = "SyncViewModel";

    private SongRepository songRepository;

    private boolean syncAlbum = false;
    private boolean syncArtist = false;
    private boolean syncGenres = false;
    private boolean syncPlaylist = false;
    private boolean syncSong = false;
    private boolean crossSyncSongGenre = false;

    private ArrayList<Album> albumList = new ArrayList<>();
    private ArrayList<Artist> artistList = new ArrayList<>();
    private ArrayList<Genre> genreList = new ArrayList<>();
    private ArrayList<Playlist> playlistList = new ArrayList<>();
    private ArrayList<Song> songList = new ArrayList<>();

    private int step = 0;
    private int progress = 0;

    public SyncViewModel(@NonNull Application application) {
        super(application);

        songRepository = new SongRepository(application);
    }

    public void setArguemnts(Bundle bundle) {
        step = 0;
        progress = 0;

        syncAlbum = bundle.getBoolean("sync_album", false);
        syncArtist = bundle.getBoolean("sync_artist", false);
        syncGenres = bundle.getBoolean("sync_genres", false);
        syncPlaylist = bundle.getBoolean("sync_playlist", false);
        syncSong = bundle.getBoolean("sync_song", false);
        crossSyncSongGenre = bundle.getBoolean("cross_sync_song_genre", false);

        countStep();
    }

    private void countStep() {
        if (syncAlbum) step++;
        if (syncArtist) step++;
        if (syncGenres) step++;
        if (syncPlaylist) step++;
        if (syncSong) step++;
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

    public boolean isCrossSyncSongGenre() {
        return crossSyncSongGenre;
    }

    public ArrayList<Album> getAlbumList() {
        return albumList;
    }

    public void setAlbumList(ArrayList<Album> albumList) {
        this.albumList = albumList;
    }

    public ArrayList<Artist> getArtistList() {
        return artistList;
    }

    public void setArtistList(ArrayList<Artist> artistList) {
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

    public void increaseProggress() {
        progress++;
    }

    public int getStep() {
        return step;
    }

    public int getProgress() {
        return progress;
    }


    public Map<Integer, Song> getCatalogue() {
        Map<Integer, Song> map = new HashMap<>();

        for (Song song : songRepository.getCatalogue()) {
            map.put(song.hashCode(), song);
        }

        return map;
    }
}
