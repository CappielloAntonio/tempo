package com.cappielloantonio.play.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.cappielloantonio.play.repository.QueueRepository;

public class MainViewModel extends AndroidViewModel {
    private static final String TAG = "SearchViewModel";

    private Application application;

    public MainViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
    }

    public boolean isQueueLoaded() {
        QueueRepository queueRepository = new QueueRepository(application);
        return queueRepository.count() != 0;
    }
}
