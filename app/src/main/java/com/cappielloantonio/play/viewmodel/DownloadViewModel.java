package com.cappielloantonio.play.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.play.model.Playlist;
import com.cappielloantonio.play.repository.DownloadRepository;
import com.cappielloantonio.play.subsonic.models.AlbumID3;
import com.cappielloantonio.play.subsonic.models.ArtistID3;
import com.cappielloantonio.play.subsonic.models.Child;
import com.cappielloantonio.play.util.MappingUtil;

import java.util.List;
import java.util.stream.Collectors;

public class DownloadViewModel extends AndroidViewModel {
    private static final String TAG = "HomeViewModel";

    private final DownloadRepository downloadRepository;

    private final MutableLiveData<List<ArtistID3>> downloadedArtistSample = new MutableLiveData<>(null);
    private final MutableLiveData<List<AlbumID3>> downloadedAlbumSample = new MutableLiveData<>(null);
    private final MutableLiveData<List<Child>> downloadedTrackSample = new MutableLiveData<>(null);
    private final MutableLiveData<List<Playlist>> downloadedPlaylistSample = new MutableLiveData<>(null);

    public DownloadViewModel(@NonNull Application application) {
        super(application);

        downloadRepository = new DownloadRepository(application);
    }

    public LiveData<List<ArtistID3>> getDownloadedArtists(LifecycleOwner owner, int size) {
        downloadRepository.getLiveDownloadSample(size, true, false, false, false)
                .observe(owner, downloads -> downloadedArtistSample.postValue(downloads.stream().map(download -> {
                    ArtistID3 artist = new ArtistID3();
                    artist.setId(download.getArtistId());
                    artist.setName(download.getArtist());
                    artist.setCoverArtId(download.getCoverArtId());
                    // artist.setAlbumCount(0);
                    // artist.setStarred(null);
                    return artist;
                }).collect(Collectors.toList())));

        return downloadedArtistSample;
    }

    public LiveData<List<AlbumID3>> getDownloadedAlbums(LifecycleOwner owner, int size) {
        downloadRepository.getLiveDownloadSample(size, false, true, false, false)
                .observe(owner, downloads -> downloadedAlbumSample.postValue(downloads.stream().map(download -> {
                    AlbumID3 album = new AlbumID3();
                    album.setId(download.getAlbumId());
                    album.setName(download.getAlbum());
                    album.setArtist(album.getArtist());
                    album.setArtistId(album.getArtistId());
                    album.setCoverArtId(album.getCoverArtId());
                    // album.setSongCount(0);
                    // album.setDuration(0);
                    // album.setPlayCount(null);
                    // album.setCreated(null);
                    // album.setStarred(null);
                    album.setYear(album.getYear());
                    // album.setGenre(null);
                    return album;
                }).collect(Collectors.toList())));

        return downloadedAlbumSample;
    }

    public LiveData<List<Child>> getDownloadedTracks(LifecycleOwner owner, int size) {
        downloadRepository.getLiveDownloadSample(size, false, false, true, false).observe(owner, downloads -> downloadedTrackSample.postValue(downloads.stream().map(download -> (Child) download).collect(Collectors.toList())));
        return downloadedTrackSample;
    }

    public LiveData<List<Playlist>> getDownloadedPlaylists(LifecycleOwner owner, int size) {
        downloadRepository.getLiveDownloadSample(size, false, false, false, true).observe(owner, downloads -> downloadedPlaylistSample.postValue(MappingUtil.mapDownloadToPlaylist(downloads)));
        return downloadedPlaylistSample;
    }
}
