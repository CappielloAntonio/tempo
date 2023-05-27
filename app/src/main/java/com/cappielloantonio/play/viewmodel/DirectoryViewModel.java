package com.cappielloantonio.play.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.play.repository.DirectoryRepository;
import com.cappielloantonio.play.subsonic.models.Directory;

public class DirectoryViewModel extends AndroidViewModel {
    private final DirectoryRepository directoryRepository;

    private MutableLiveData<String> id = new MutableLiveData<>(null);
    private MutableLiveData<String> name = new MutableLiveData<>(null);

    private MutableLiveData<Directory> directory = new MutableLiveData<>(null);

    public DirectoryViewModel(@NonNull Application application) {
        super(application);

        directoryRepository = new DirectoryRepository();
    }

    public LiveData<Directory> getDirectory() {
        return directory;
    }

    public void setMusicDirectoryId(String id) {
        this.id.setValue(id);
    }

    public void setMusicDirectoryName(String name) {
        this.name.setValue(name);
    }

    public void loadMusicDirectory(LifecycleOwner owner) {
        this.id.observe(owner, id -> directoryRepository.getMusicDirectory(id).observe(owner, directory -> this.directory.setValue(directory)));
    }

    public void goBack() {
        this.id.setValue(this.directory.getValue().getParentId());
    }
}
