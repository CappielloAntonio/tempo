package com.cappielloantonio.play.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.play.model.Playlist;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.repository.PlaylistRepository;
import com.cappielloantonio.play.repository.SongRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StarredSyncViewModel extends AndroidViewModel {
    private final SongRepository songRepository;

    private final MutableLiveData<List<Song>> starredTracks = new MutableLiveData<>(null);

    public StarredSyncViewModel(@NonNull Application application) {
        super(application);

        songRepository = new SongRepository(application);
    }

    public LiveData<List<Song>> getStarredTracks(LifecycleOwner owner) {
        songRepository.getStarredSongs(false, -1).observe(owner, starredTracks::postValue);
        return starredTracks;
    }
}
