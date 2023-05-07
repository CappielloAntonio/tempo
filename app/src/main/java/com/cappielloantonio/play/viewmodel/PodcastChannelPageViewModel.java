package com.cappielloantonio.play.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.cappielloantonio.play.repository.AlbumRepository;
import com.cappielloantonio.play.repository.ArtistRepository;
import com.cappielloantonio.play.repository.PodcastRepository;
import com.cappielloantonio.play.subsonic.models.AlbumID3;
import com.cappielloantonio.play.subsonic.models.ArtistID3;
import com.cappielloantonio.play.subsonic.models.ArtistInfo2;
import com.cappielloantonio.play.subsonic.models.Child;
import com.cappielloantonio.play.subsonic.models.PodcastChannel;
import com.cappielloantonio.play.subsonic.models.PodcastEpisode;

import java.util.List;

public class PodcastChannelPageViewModel extends AndroidViewModel {
    private final PodcastRepository podcastRepository;

    private PodcastChannel podcastChannel;

    public PodcastChannelPageViewModel(@NonNull Application application) {
        super(application);

        podcastRepository = new PodcastRepository();
    }

    public LiveData<List<PodcastChannel>> getPodcastChannelEpisodes() {
        return podcastRepository.getPodcastChannels(true, podcastChannel.getId());
    }

    public PodcastChannel getPodcastChannel() {
        return podcastChannel;
    }

    public void setPodcastChannel(PodcastChannel podcastChannel) {
        this.podcastChannel = podcastChannel;
    }
}
