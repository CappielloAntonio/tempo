package com.cappielloantonio.play.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.cappielloantonio.play.repository.PodcastRepository;
import com.cappielloantonio.play.subsonic.models.PodcastEpisode;

public class PodcastBottomSheetViewModel extends AndroidViewModel {
    private final PodcastRepository podcastRepository;

    private PodcastEpisode podcast;

    public PodcastBottomSheetViewModel(@NonNull Application application) {
        super(application);

        podcastRepository = new PodcastRepository();
    }

    public PodcastEpisode getPodcast() {
        return podcast;
    }

    public void setPodcast(PodcastEpisode podcast) {
        this.podcast = podcast;
    }
}
