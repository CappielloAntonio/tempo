package com.cappielloantonio.play.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.interfaces.MediaCallback;
import com.cappielloantonio.play.repository.PodcastRepository;
import com.cappielloantonio.play.subsonic.base.ApiResponse;
import com.cappielloantonio.play.subsonic.models.AlbumID3;
import com.cappielloantonio.play.subsonic.models.PodcastChannel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class PodcastChannelCatalogueViewModel extends AndroidViewModel {
    private final PodcastRepository podcastRepository;

    private final MutableLiveData<List<PodcastChannel>> podcastChannels = new MutableLiveData<>(null);


    public PodcastChannelCatalogueViewModel(@NonNull Application application) {
        super(application);

        podcastRepository = new PodcastRepository();
    }

    public LiveData<List<PodcastChannel>> getPodcastChannels(LifecycleOwner owner) {
        if (podcastChannels.getValue() == null) {
            podcastRepository.getPodcastChannels(false, null).observe(owner, podcastChannels::postValue);
        }

        return podcastChannels;
    }
}
