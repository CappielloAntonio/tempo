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
    private LiveData<List<Song>> favoritesSongSample;
    private LiveData<List<Song>> downloadedSongSample;
    private List<Integer> years;

    public HomeViewModel(@NonNull Application application) {
        super(application);

        songRepository = new SongRepository(application);

        dicoverSongSample = songRepository.getRandomSample(10);
        recentlyPlayedSongSample = songRepository.getListLiveRecentlyPlayedSampleSong(20);
        recentlyAddedSongSample = songRepository.getListLiveRecentlyAddedSampleSong(20);
        mostPlayedSongSample = songRepository.getListLiveMostPlayedSampleSong(20);
        favoritesSongSample = songRepository.getListLiveFavoritesSampleSong(20);
        downloadedSongSample = songRepository.getListLiveDownloadedSampleSong(20);
        years = songRepository.getYearList();
    }


    public List<Song> getDiscoverSongList() {
        if(dicoverSongSample.isEmpty()) {
            dicoverSongSample = songRepository.getRandomSample(10);
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
        return years;
    }

    public LiveData<List<Song>> getFavorites() {
        return favoritesSongSample;
    }

    public LiveData<List<Song>> getDownloaded() {
        return downloadedSongSample;
    }
}
