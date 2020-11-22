package com.cappielloantonio.play.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.repository.SongRepository;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {
    private static final String TAG = "HomeViewModel";
    private SongRepository songRepository;

    private List<Song> dicoverSongSample;
    private LiveData<List<Song>> recentlyPlayedSongSample;
    private LiveData<List<Song>> recentlyAddedSongSample;
    private LiveData<List<Song>> mostPlayedSongSample;

    public HomeViewModel(@NonNull Application application) {
        super(application);

        songRepository = new SongRepository(application);

        dicoverSongSample = songRepository.getRandomSample(5);
        recentlyPlayedSongSample = songRepository.getListLiveRecentlyPlayedSampleSong();
        recentlyAddedSongSample = songRepository.getListLiveRecentlyAddedSampleSong();
        mostPlayedSongSample = songRepository.getListLiveMostPlayedSampleSong();
    }


    public List<Song> getDiscoverSongList() {
        return dicoverSongSample;
    }

    public LiveData<List<Song>> getRecentlyAddedSongList() {
        return recentlyAddedSongSample;
    }

    public LiveData<List<Song>> getRecentlyPlayedSongList() {
        return recentlyPlayedSongSample;
    }

    public LiveData<List<Song>> getMostPlayedSongList() {
        return mostPlayedSongSample;
    }
}
