package com.cappielloantonio.tempo.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.media3.common.util.UnstableApi;

import com.cappielloantonio.tempo.interfaces.StarCallback;
import com.cappielloantonio.tempo.model.Download;
import com.cappielloantonio.tempo.model.Queue;
import com.cappielloantonio.tempo.repository.ArtistRepository;
import com.cappielloantonio.tempo.repository.FavoriteRepository;
import com.cappielloantonio.tempo.repository.OpenRepository;
import com.cappielloantonio.tempo.repository.QueueRepository;
import com.cappielloantonio.tempo.repository.SongRepository;
import com.cappielloantonio.tempo.subsonic.models.ArtistID3;
import com.cappielloantonio.tempo.subsonic.models.Child;
import com.cappielloantonio.tempo.subsonic.models.LyricsList;
import com.cappielloantonio.tempo.subsonic.models.PlayQueue;
import com.cappielloantonio.tempo.util.Constants;
import com.cappielloantonio.tempo.util.DownloadUtil;
import com.cappielloantonio.tempo.util.MappingUtil;
import com.cappielloantonio.tempo.util.NetworkUtil;
import com.cappielloantonio.tempo.util.OpenSubsonicExtensionsUtil;
import com.cappielloantonio.tempo.util.Preferences;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@OptIn(markerClass = UnstableApi.class)
public class PlayerBottomSheetViewModel extends AndroidViewModel {
    private static final String TAG = "PlayerBottomSheetViewModel";

    private final SongRepository songRepository;
    private final ArtistRepository artistRepository;
    private final QueueRepository queueRepository;
    private final FavoriteRepository favoriteRepository;
    private final OpenRepository openRepository;
    private final MutableLiveData<String> lyricsLiveData = new MutableLiveData<>(null);
    private final MutableLiveData<LyricsList> lyricsListLiveData = new MutableLiveData<>(null);
    private final MutableLiveData<String> descriptionLiveData = new MutableLiveData<>(null);
    private final MutableLiveData<Child> liveMedia = new MutableLiveData<>(null);
    private final MutableLiveData<ArtistID3> liveArtist = new MutableLiveData<>(null);
    private final MutableLiveData<List<Child>> instantMix = new MutableLiveData<>(null);


    public PlayerBottomSheetViewModel(@NonNull Application application) {
        super(application);

        songRepository = new SongRepository();
        artistRepository = new ArtistRepository();
        queueRepository = new QueueRepository();
        favoriteRepository = new FavoriteRepository();
        openRepository = new OpenRepository();
    }

    public LiveData<List<Queue>> getQueueSong() {
        return queueRepository.getLiveQueue();
    }

    public void setFavorite(Context context, Child media) {
        if (media != null) {
            if (media.getStarred() != null) {
                if (NetworkUtil.isOffline()) {
                    removeFavoriteOffline(media);
                } else {
                    removeFavoriteOnline(media);
                }
            } else {
                if (NetworkUtil.isOffline()) {
                    setFavoriteOffline(media);
                } else {
                    setFavoriteOnline(context, media);
                }
            }
        }
    }

    private void removeFavoriteOffline(Child media) {
        favoriteRepository.starLater(media.getId(), null, null, false);
        media.setStarred(null);
    }

    private void removeFavoriteOnline(Child media) {
        favoriteRepository.unstar(media.getId(), null, null, new StarCallback() {
            @Override
            public void onError() {
                // media.setStarred(new Date());
                favoriteRepository.starLater(media.getId(), null, null, false);
            }
        });

        media.setStarred(null);
    }

    private void setFavoriteOffline(Child media) {
        favoriteRepository.starLater(media.getId(), null, null, true);
        media.setStarred(new Date());
    }

    private void setFavoriteOnline(Context context, Child media) {
        favoriteRepository.star(media.getId(), null, null, new StarCallback() {
            @Override
            public void onError() {
                // media.setStarred(null);
                favoriteRepository.starLater(media.getId(), null, null, true);
            }
        });

        media.setStarred(new Date());

        if (Preferences.isStarredSyncEnabled()) {
            DownloadUtil.getDownloadTracker(context).download(
                    MappingUtil.mapDownload(media),
                    new Download(media)
            );
        }
    }

    public LiveData<String> getLiveLyrics() {
        return lyricsLiveData;
    }

    public LiveData<LyricsList> getLiveLyricsList() {
        return lyricsListLiveData;
    }

    public void refreshMediaInfo(LifecycleOwner owner, Child media) {
        if (OpenSubsonicExtensionsUtil.isSongLyricsExtensionAvailable()) {
            openRepository.getLyricsBySongId(media.getId()).observe(owner, lyricsListLiveData::postValue);
            lyricsLiveData.postValue(null);
        } else {
            songRepository.getSongLyrics(media).observe(owner, lyricsLiveData::postValue);
            lyricsListLiveData.postValue(null);
        }
    }

    public LiveData<Child> getLiveMedia() {
        return liveMedia;
    }

    public void setLiveMedia(LifecycleOwner owner, String mediaType, String mediaId) {
        if (mediaType != null) {
            switch (mediaType) {
                case Constants.MEDIA_TYPE_MUSIC:
                    songRepository.getSong(mediaId).observe(owner, liveMedia::postValue);
                    descriptionLiveData.postValue(null);
                    break;
                case Constants.MEDIA_TYPE_PODCAST:
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
                case Constants.MEDIA_TYPE_MUSIC:
                    artistRepository.getArtist(ArtistId).observe(owner, liveArtist::postValue);
                    break;
                case Constants.MEDIA_TYPE_PODCAST:
                    liveArtist.postValue(null);
                    break;
            }
        }
    }

    public void setLiveDescription(String description) {
        descriptionLiveData.postValue(description);
    }

    public LiveData<String> getLiveDescription() {
        return descriptionLiveData;
    }

    public LiveData<List<Child>> getMediaInstantMix(LifecycleOwner owner, Child media) {
        instantMix.setValue(Collections.emptyList());

        songRepository.getInstantMix(media, 20).observe(owner, instantMix::postValue);

        return instantMix;
    }

    public LiveData<PlayQueue> getPlayQueue() {
        return queueRepository.getPlayQueue();
    }

    public boolean savePlayQueue() {
        Child media = getLiveMedia().getValue();
        List<Child> queue = queueRepository.getMedia();
        List<String> ids = queue.stream().map(Child::getId).collect(Collectors.toList());

        if (media != null) {
            queueRepository.savePlayQueue(ids, media.getId(), 0);
            return true;
        }

        return false;
    }
}
