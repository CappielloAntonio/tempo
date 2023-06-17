package com.cappielloantonio.tempo.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.cappielloantonio.tempo.repository.PodcastRepository;
import com.cappielloantonio.tempo.subsonic.models.InternetRadioStation;

public class PodcastChannelEditorViewModel extends AndroidViewModel {
    private static final String TAG = "RadioEditorViewModel";

    private final PodcastRepository podcastRepository;

    private InternetRadioStation toEdit;

    public PodcastChannelEditorViewModel(@NonNull Application application) {
        super(application);

        podcastRepository = new PodcastRepository();
    }

    public void createChannel(String url) {
        podcastRepository.createPodcastChannel(url);
    }
}
