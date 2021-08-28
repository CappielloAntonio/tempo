package com.cappielloantonio.play.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.play.model.Album;
import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.model.Download;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.repository.AlbumRepository;
import com.cappielloantonio.play.repository.ArtistRepository;
import com.cappielloantonio.play.repository.DownloadRepository;
import com.cappielloantonio.play.repository.SongRepository;
import com.cappielloantonio.play.util.MappingUtil;

import java.util.List;

public class DownloadViewModel extends AndroidViewModel {
    private static final String TAG = "HomeViewModel";

    private final DownloadRepository downloadRepository;

    private final MutableLiveData<List<Artist>> downloadedArtistSample = new MutableLiveData<>(null);
    private final MutableLiveData<List<Album>> downloadedAlbumSample = new MutableLiveData<>(null);
    private final MutableLiveData<List<Song>> downloadedTrackSample = new MutableLiveData<>(null);

    public DownloadViewModel(@NonNull Application application) {
        super(application);

        downloadRepository = new DownloadRepository(application);
    }

    public LiveData<List<Artist>> getDownloadedArtists(LifecycleOwner owner, int size) {
        downloadRepository.getLiveDownloadSample(size).observe(owner, downloads -> downloadedArtistSample.postValue(MappingUtil.mapDownloadToArtist(downloads)));
        return downloadedArtistSample;
    }

    public LiveData<List<Album>> getDownloadedAlbums(LifecycleOwner owner, int size) {
        downloadRepository.getLiveDownloadSample(size).observe(owner, downloads -> downloadedAlbumSample.postValue(MappingUtil.mapDownloadToAlbum(downloads)));
        return downloadedAlbumSample;
    }

    public LiveData<List<Song>> getDownloadedTracks(LifecycleOwner owner, int size) {
        downloadRepository.getLiveDownloadSample(size).observe(owner, downloads -> downloadedTrackSample.postValue(MappingUtil.mapDownloadToSong(downloads)));
        return downloadedTrackSample;
    }
}
