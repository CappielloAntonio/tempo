package com.cappielloantonio.play.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.play.repository.PodcastRepository;
import com.cappielloantonio.play.subsonic.models.PodcastChannel;
import com.cappielloantonio.play.subsonic.models.PodcastEpisode;

import java.util.List;

public class PodcastViewModel extends AndroidViewModel {
    private final PodcastRepository podcastRepository;

    private final MutableLiveData<List<PodcastEpisode>> newestPodcastEpisodes = new MutableLiveData<>(null);
    private final MutableLiveData<List<PodcastChannel>> podcastChannels = new MutableLiveData<>(null);

    public PodcastViewModel(@NonNull Application application) {
        super(application);

        podcastRepository = new PodcastRepository();
    }

    public LiveData<List<PodcastEpisode>> getNewestPodcastEpisodes(LifecycleOwner owner) {
        if (newestPodcastEpisodes.getValue() == null) {
            podcastRepository.getNewestPodcastEpisodes(20).observe(owner, newestPodcastEpisodes::postValue);
        }

        return newestPodcastEpisodes;
    }

    public LiveData<List<PodcastChannel>> getPodcastChannels(LifecycleOwner owner) {
        if (podcastChannels.getValue() == null) {
            podcastRepository.getPodcastChannels(false, null).observe(owner, podcastChannels::postValue);
        }

        return podcastChannels;
    }
}
