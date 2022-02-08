package com.cappielloantonio.play.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.cappielloantonio.play.model.Media;
import com.cappielloantonio.play.repository.PodcastRepository;
import com.cappielloantonio.play.util.DownloadUtil;
import com.cappielloantonio.play.util.MappingUtil;
import com.cappielloantonio.play.util.PreferenceUtil;

public class PodcastBottomSheetViewModel extends AndroidViewModel {
    private final PodcastRepository podcastRepository;

    private Media podcast;

    public PodcastBottomSheetViewModel(@NonNull Application application) {
        super(application);

        podcastRepository = new PodcastRepository(application);
    }

    public Media getPodcast() {
        return podcast;
    }

    public void setPodcast(Media podcast) {
        this.podcast = podcast;
    }
}
