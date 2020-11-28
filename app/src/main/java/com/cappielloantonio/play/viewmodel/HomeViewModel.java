package com.cappielloantonio.play.viewmodel;

import android.app.Application;
import android.util.Log;

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
    private List<Integer> years;

    public HomeViewModel(@NonNull Application application) {
        super(application);

        songRepository = new SongRepository(application);

        dicoverSongSample = songRepository.getRandomSample(5);
        recentlyPlayedSongSample = songRepository.getListLiveRecentlyPlayedSampleSong(20);
        recentlyAddedSongSample = songRepository.getListLiveRecentlyAddedSampleSong(20);
        mostPlayedSongSample = songRepository.getListLiveMostPlayedSampleSong(20);
        years = songRepository.getYearList();
    }


    public List<Song> getDiscoverSongList() {
        if(dicoverSongSample.isEmpty()) {
            dicoverSongSample = songRepository.getRandomSample(5);
        }

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

    public List<Integer> getYearList() {
        Log.d(TAG, "getYearList: " + years.toString());
        return years;
    }
}
