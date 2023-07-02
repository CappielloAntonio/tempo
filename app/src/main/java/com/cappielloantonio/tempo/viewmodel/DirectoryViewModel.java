package com.cappielloantonio.tempo.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.cappielloantonio.tempo.repository.DirectoryRepository;
import com.cappielloantonio.tempo.subsonic.models.Directory;

public class DirectoryViewModel extends AndroidViewModel {
    private final DirectoryRepository directoryRepository;

    public DirectoryViewModel(@NonNull Application application) {
        super(application);

        directoryRepository = new DirectoryRepository();
    }

    public LiveData<Directory> loadMusicDirectory(String id) {
        return directoryRepository.getMusicDirectory(id);
    }
}
