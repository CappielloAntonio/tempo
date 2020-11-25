package com.cappielloantonio.play.viewmodel;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.repository.SongRepository;

import java.util.List;

public class SyncViewModel extends AndroidViewModel {
    private static final String TAG = "SyncViewModel";

    private boolean syncAlbum = false;
    private boolean syncArtist = false;
    private boolean syncGenres = false;
    private boolean syncPlaylist = false;
    private boolean syncSong = false;
    private boolean crossSyncSongGenre = false;

    private int step = 0;
    private int progress = 0;


    public SyncViewModel(@NonNull Application application) {
        super(application);
    }

    public void setArguemnts(Bundle bundle) {
        syncAlbum = bundle.getBoolean("sync_album", false);
        syncArtist = bundle.getBoolean("sync_artist", false);
        syncGenres = bundle.getBoolean("sync_genres", false);
        syncPlaylist = bundle.getBoolean("sync_playlist", false);
        syncSong = bundle.getBoolean("sync_song", false);
        crossSyncSongGenre = bundle.getBoolean("cross_sync_song_genre", false);

        countStep();
    }

    private void countStep() {
        if(syncAlbum) step++;
        if(syncArtist) step++;
        if(syncGenres) step++;
        if(syncPlaylist) step++;
        if(syncSong) step++;
        if(crossSyncSongGenre) step++;
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

    public int getStep() {
        return step;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(Boolean step) {
        if(step) progress++;
    }

    public int getProgressBarInfo() {
        return progress * (100 / step);
    }
}
