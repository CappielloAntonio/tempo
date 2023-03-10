package com.cappielloantonio.play.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.cappielloantonio.play.repository.QueueRepository;
import com.cappielloantonio.play.repository.SystemRepository;

public class MainViewModel extends AndroidViewModel {
    private static final String TAG = "SearchViewModel";

    private final SystemRepository systemRepository;

    private final Application application;

    public MainViewModel(@NonNull Application application) {
        super(application);
        this.application = application;

        systemRepository = new SystemRepository();
    }

    public boolean isQueueLoaded() {
        QueueRepository queueRepository = new QueueRepository();
        return queueRepository.count() != 0;
    }

    public LiveData<Boolean> ping() {
        return systemRepository.ping();
    }
}
