package com.cappielloantonio.play.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.media3.common.util.UnstableApi;

import com.cappielloantonio.play.model.Download;
import com.cappielloantonio.play.repository.AlbumRepository;
import com.cappielloantonio.play.repository.ArtistRepository;
import com.cappielloantonio.play.repository.SongRepository;
import com.cappielloantonio.play.subsonic.models.AlbumID3;
import com.cappielloantonio.play.subsonic.models.ArtistID3;
import com.cappielloantonio.play.subsonic.models.Child;
import com.cappielloantonio.play.util.DownloadUtil;
import com.cappielloantonio.play.util.MappingUtil;
import com.cappielloantonio.play.util.Preferences;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@UnstableApi
public class SongBottomSheetViewModel extends AndroidViewModel {
    private final SongRepository songRepository;
    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;

    private Child song;

    private final MutableLiveData<List<Child>> instantMix = new MutableLiveData<>(null);

    public SongBottomSheetViewModel(@NonNull Application application) {
        super(application);

        songRepository = new SongRepository();
        albumRepository = new AlbumRepository();
        artistRepository = new ArtistRepository();
    }

    public Child getSong() {
        return song;
    }

    public void setSong(Child song) {
        this.song = song;
    }

    public void setFavorite(Context context) {
        if (song.getStarred() != null) {
            songRepository.unstar(song.getId());
            song.setStarred(null);
        } else {
            songRepository.star(song.getId());
            song.setStarred(new Date());

            if (Preferences.isStarredSyncEnabled()) {
                DownloadUtil.getDownloadTracker(context).download(
                        MappingUtil.mapDownload(song),
                        new Download(song)
                );
            }
        }
    }

    public LiveData<AlbumID3> getAlbum() {
        return albumRepository.getAlbum(song.getAlbumId());
    }

    public LiveData<ArtistID3> getArtist() {
        return artistRepository.getArtist(song.getArtistId());
    }

    public LiveData<List<Child>> getInstantMix(LifecycleOwner owner, Child media) {
        instantMix.setValue(Collections.emptyList());

        songRepository.getInstantMix(media, 20).observe(owner, instantMix::postValue);

        return instantMix;
    }
}
