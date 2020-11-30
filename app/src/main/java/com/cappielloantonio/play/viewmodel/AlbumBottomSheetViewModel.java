package com.cappielloantonio.play.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.repository.SongRepository;

public class AlbumBottomSheetViewModel extends AndroidViewModel {
    private SongRepository songRepository;
    private Song song;

    public AlbumBottomSheetViewModel(@NonNull Application application) {
        super(application);

        songRepository = new SongRepository(application);
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public Song getSong() {
        return song;
    }

    public void setFavorite() {
        if(song.isFavorite()) {
            song.setFavorite(false);
        }
        else {
            song.setFavorite(true);
        }

        songRepository.setFavoriteStatus(song);
    }
}
