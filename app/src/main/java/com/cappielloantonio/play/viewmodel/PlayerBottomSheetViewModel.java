package com.cappielloantonio.play.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.play.model.Album;
import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.model.Queue;
import com.cappielloantonio.play.model.Song;
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

    private final MutableLiveData<Song> liveSong = new MutableLiveData<>(null);
    private final MutableLiveData<Album> liveAlbum = new MutableLiveData<>(null);
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

    public void setFavorite(Context context, Song song) {
        if (song != null) {
            if (song.isFavorite()) {
                songRepository.unstar(song.getId());
                song.setFavorite(false);
            } else {
                songRepository.star(song.getId());
                song.setFavorite(true);

                if (PreferenceUtil.getInstance(context).isStarredSyncEnabled()) {
                    DownloadUtil.getDownloadTracker(context).download(
                            MappingUtil.mapMediaItem(context, song, false),
                            MappingUtil.mapDownload(song, null, null)
                    );
                }
            }
        }
    }

    public LiveData<String> getLiveLyrics() {
        return lyricsLiveData;
    }

    public void refreshSongInfo(LifecycleOwner owner, Song song) {
        songRepository.getSongLyrics(song).observe(owner, lyricsLiveData::postValue);
    }

    public LiveData<Song> getLiveSong() {
        return liveSong;
    }

    public void setLiveSong(LifecycleOwner owner, String songId) {
        songRepository.getSong(songId).observe(owner, liveSong::postValue);
    }

    public LiveData<Artist> getLiveArtist() {
        return liveArtist;
    }

    public void setLiveArtist(LifecycleOwner owner, String ArtistId) {
        artistRepository.getArtist(ArtistId).observe(owner, liveArtist::postValue);
    }
}
