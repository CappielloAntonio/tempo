package com.cappielloantonio.play.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.model.Queue;
import com.cappielloantonio.play.model.Media;
import com.cappielloantonio.play.repository.ArtistRepository;
import com.cappielloantonio.play.repository.QueueRepository;
import com.cappielloantonio.play.repository.SongRepository;
import com.cappielloantonio.play.util.DownloadUtil;
import com.cappielloantonio.play.util.MappingUtil;
import com.cappielloantonio.play.util.PreferenceUtil;

import java.util.List;

public class PlayerBottomSheetViewModel extends AndroidViewModel {
    private static final String TAG = "PlayerBottomSheetViewModel";

    private final SongRepository songRepository;
    private final ArtistRepository artistRepository;
    private final QueueRepository queueRepository;

    private final MutableLiveData<String> lyricsLiveData = new MutableLiveData<>(null);

    private final MutableLiveData<Media> liveMedia = new MutableLiveData<>(null);
    private final MutableLiveData<Artist> liveArtist = new MutableLiveData<>(null);

    public PlayerBottomSheetViewModel(@NonNull Application application) {
        super(application);

        songRepository = new SongRepository(application);
        artistRepository = new ArtistRepository(application);
        queueRepository = new QueueRepository(application);
    }

    public LiveData<List<Queue>> getQueueSong() {
        return queueRepository.getLiveQueue();
    }

    public void setFavorite(Context context, Media media) {
        if (media != null) {
            if (media.isStarred()) {
                songRepository.unstar(media.getId());
                media.setStarred(false);
            } else {
                songRepository.star(media.getId());
                media.setStarred(true);

                if (PreferenceUtil.getInstance(context).isStarredSyncEnabled()) {
                    DownloadUtil.getDownloadTracker(context).download(
                            MappingUtil.mapMediaItem(context, media, false),
                            MappingUtil.mapDownload(media, null, null)
                    );
                }
            }
        }
    }

    public LiveData<String> getLiveLyrics() {
        return lyricsLiveData;
    }

    public void refreshMediaInfo(LifecycleOwner owner, Media media) {
        songRepository.getSongLyrics(media).observe(owner, lyricsLiveData::postValue);
    }

    public LiveData<Media> getLiveMedia() {
        return liveMedia;
    }

    public void setLiveMedia(LifecycleOwner owner, String mediaType, String mediaId) {
        if(mediaType != null) {
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

    public LiveData<Artist> getLiveArtist() {
        return liveArtist;
    }

    public void setLiveArtist(LifecycleOwner owner, String mediaType, String ArtistId) {
        if(mediaType != null) {
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
}
