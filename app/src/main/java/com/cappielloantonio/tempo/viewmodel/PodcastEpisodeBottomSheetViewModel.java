package com.cappielloantonio.tempo.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.cappielloantonio.tempo.repository.PodcastRepository;
import com.cappielloantonio.tempo.subsonic.models.PodcastEpisode;

public class PodcastEpisodeBottomSheetViewModel extends AndroidViewModel {
    private final PodcastRepository podcastRepository;

    private PodcastEpisode podcastEpisode;

    public PodcastEpisodeBottomSheetViewModel(@NonNull Application application) {
        super(application);

        podcastRepository = new PodcastRepository();
    }

    public PodcastEpisode getPodcastEpisode() {
        return podcastEpisode;
    }

    public void setPodcastEpisode(PodcastEpisode podcast) {
        this.podcastEpisode = podcast;
    }

    public void deletePodcastEpisode() {
        if (podcastEpisode != null) podcastRepository.deletePodcastEpisode(podcastEpisode.getId());
    }
}
