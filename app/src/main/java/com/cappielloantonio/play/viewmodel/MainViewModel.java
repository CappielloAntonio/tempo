package com.cappielloantonio.play.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.cappielloantonio.play.repository.QueueRepository;

public class MainViewModel extends AndroidViewModel {
    private static final String TAG = "SearchViewModel";

    private QueueRepository queueRepository;

    public MainViewModel(@NonNull Application application) {
        super(application);

        queueRepository = new QueueRepository(application);
    }

    public boolean isQueueLoaded() {
        if (queueRepository.count() == 0)
            return false;

        return true;
    }
}
