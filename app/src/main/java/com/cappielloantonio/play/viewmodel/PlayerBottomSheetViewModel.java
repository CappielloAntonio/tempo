package com.cappielloantonio.play.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.media3.common.util.UnstableApi;

import com.cappielloantonio.play.model.Media;
import com.cappielloantonio.play.model.Queue;
import com.cappielloantonio.play.repository.ArtistRepository;
import com.cappielloantonio.play.repository.QueueRepository;
import com.cappielloantonio.play.repository.SongRepository;
import com.cappielloantonio.play.subsonic.models.ArtistID3;
import com.cappielloantonio.play.subsonic.models.Child;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@OptIn(markerClass = UnstableApi.class)
public class PlayerBottomSheetViewModel extends AndroidViewModel {
    private static final String TAG = "PlayerBottomSheetViewModel";

    private final SongRepository songRepository;
    private final ArtistRepository artistRepository;
    private final QueueRepository queueRepository;

    private final MutableLiveData<String> lyricsLiveData = new MutableLiveData<>(null);

    private final MutableLiveData<Child> liveMedia = new MutableLiveData<>(null);
    private final MutableLiveData<ArtistID3> liveArtist = new MutableLiveData<>(null);
    private final MutableLiveData<List<Child>> instantMix = new MutableLiveData<>(null);


    public PlayerBottomSheetViewModel(@NonNull Application application) {
        super(application);

        songRepository = new SongRepository();
        artistRepository = new ArtistRepository();
        queueRepository = new QueueRepository();
    }

    public LiveData<List<Queue>> getQueueSong() {
        return queueRepository.getLiveQueue();
    }

    public void setFavorite(Context context, Child media) {
        if (media != null) {
            if (media.getStarred() != null) {
                songRepository.unstar(media.getId());
                media.setStarred(null);
            } else {
                songRepository.star(media.getId());
                media.setStarred(new Date());

                // TODO
                /* if (Preferences.isStarredSyncEnabled()) {
                    DownloadUtil.getDownloadTracker(context).download(
                            MappingUtil.mapMediaItem(media, false),
                            MappingUtil.mapDownload(media, null, null)
                    );
                } */
            }
        }
    }

    public LiveData<String> getLiveLyrics() {
        return lyricsLiveData;
    }

    public void refreshMediaInfo(LifecycleOwner owner, Child media) {
        songRepository.getSongLyrics(media).observe(owner, lyricsLiveData::postValue);
    }

    public LiveData<Child> getLiveMedia() {
        return liveMedia;
    }

    public void setLiveMedia(LifecycleOwner owner, String mediaType, String mediaId) {
        if (mediaType != null) {
            switch (mediaType) {
                case Media.MEDIA_TYPE_MUSIC:
                    songRepository.getSong(mediaId).observe(owner, liveMedia::postValue);
                    break;
                case Media.MEDIA_TYPE_PODCAST:
                    liveMedia.postValue(null);
                    break;
            }
        }
    }

    public LiveData<ArtistID3> getLiveArtist() {
        return liveArtist;
    }

    public void setLiveArtist(LifecycleOwner owner, String mediaType, String ArtistId) {
        if (mediaType != null) {
            switch (mediaType) {
                case Media.MEDIA_TYPE_MUSIC:
                    artistRepository.getArtist(ArtistId).observe(owner, liveArtist::postValue);
                    break;
                case Media.MEDIA_TYPE_PODCAST:
                    liveArtist.postValue(null);
                    break;
            }
        }
    }

    public LiveData<List<Child>> getMediaInstantMix(LifecycleOwner owner, Child media) {
        instantMix.setValue(Collections.emptyList());

        songRepository.getInstantMix(media, 20).observe(owner, instantMix::postValue);

        return instantMix;
    }
}
