package com.cappielloantonio.tempo.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.tempo.model.DownloadStack;
import com.cappielloantonio.tempo.repository.DownloadRepository;
import com.cappielloantonio.tempo.subsonic.models.Child;
import com.cappielloantonio.tempo.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DownloadViewModel extends AndroidViewModel {
    private static final String TAG = "DownloadViewModel";

    private final DownloadRepository downloadRepository;

    private final MutableLiveData<List<Child>> downloadedTrackSample = new MutableLiveData<>(null);
    private final MutableLiveData<ArrayList<DownloadStack>> viewStack = new MutableLiveData<>(null);

    public DownloadViewModel(@NonNull Application application) {
        super(application);

        downloadRepository = new DownloadRepository();

        initViewStack(new DownloadStack(Constants.DOWNLOAD_TYPE_TRACK, null));
    }

    public LiveData<List<Child>> getDownloadedTracks(LifecycleOwner owner) {
        downloadRepository.getLiveDownload().observe(owner, downloads -> downloadedTrackSample.postValue(downloads.stream().map(download -> (Child) download).collect(Collectors.toList())));
        return downloadedTrackSample;
    }

    public LiveData<ArrayList<DownloadStack>> getViewStack() {
        return viewStack;
    }

    public void initViewStack(DownloadStack level) {
        ArrayList<DownloadStack> stack = new ArrayList<>();
        stack.add(level);
        viewStack.setValue(stack);
    }

    public void pushViewStack(DownloadStack level) {
        ArrayList<DownloadStack> stack = viewStack.getValue();
        stack.add(level);
        viewStack.setValue(stack);
    }

    public void popViewStack() {
        ArrayList<DownloadStack> stack = viewStack.getValue();
        stack.remove(stack.size() - 1);
        viewStack.setValue(stack);
    }
}
