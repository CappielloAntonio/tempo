package com.cappielloantonio.play.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.repository.QueueRepository;

import java.util.List;

public class PlayerBottomSheetViewModel extends AndroidViewModel {
    private static final String TAG = "HomeViewModel";
    private QueueRepository queueRepository;

    private LiveData<List<Song>> queueSong;
    private LiveData<Song> nowPlayingSong = new MutableLiveData<>();


    public PlayerBottomSheetViewModel(@NonNull Application application) {
        super(application);

        queueRepository = new QueueRepository(application);

        queueSong = queueRepository.getLiveQueue();
    }

    public LiveData<List<Song>> getQueueSong() {
        return queueSong;
    }

    public LiveData<Song> getNowPlayingSong() {
        return nowPlayingSong;
    }

    public LiveData<Song> setNowPlayingSong(int position) {
         Song song = queueRepository.getSongs().get(position);

        if(song != null) {
            nowPlayingSong = new MutableLiveData<>(song);
        }

        return nowPlayingSong;
    }
}
