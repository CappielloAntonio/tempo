package com.cappielloantonio.play.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.play.model.Media;
import com.cappielloantonio.play.repository.SongRepository;

import java.util.List;

public class StarredSyncViewModel extends AndroidViewModel {
    private final SongRepository songRepository;

    private final MutableLiveData<List<Media>> starredTracks = new MutableLiveData<>(null);

    public StarredSyncViewModel(@NonNull Application application) {
        super(application);

        songRepository = new SongRepository(application);
    }

    public LiveData<List<Media>> getStarredTracks(LifecycleOwner owner) {
        songRepository.getStarredSongs(false, -1).observe(owner, starredTracks::postValue);
        return starredTracks;
    }
}
