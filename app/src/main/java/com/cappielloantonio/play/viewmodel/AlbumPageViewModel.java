package com.cappielloantonio.play.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.play.model.Album;
import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.repository.AlbumRepository;
import com.cappielloantonio.play.repository.ArtistRepository;
import com.cappielloantonio.play.repository.DownloadRepository;
import com.cappielloantonio.play.repository.SongRepository;
import com.cappielloantonio.play.util.MappingUtil;
import com.cappielloantonio.play.util.MusicUtil;

import java.util.List;

public class AlbumPageViewModel extends AndroidViewModel {
    private AlbumRepository albumRepository;
    private ArtistRepository artistRepository;
    private DownloadRepository downloadRepository;

    private MutableLiveData<List<Song>> songLiveList = new MutableLiveData<>();

    private Album album;
    private boolean isOffline;

    public AlbumPageViewModel(@NonNull Application application) {
        super(application);

        albumRepository = new AlbumRepository(application);
        artistRepository = new ArtistRepository(application);
        downloadRepository = new DownloadRepository(application);
    }

    public LiveData<List<Song>> getAlbumSongLiveList(FragmentActivity activity) {
        if(isOffline) {
            downloadRepository.getLiveDownloadFromAlbum(album.getId()).observe(activity, downloads -> songLiveList.postValue(MappingUtil.mapDownloadToSong(downloads)));
        } else {
            songLiveList = albumRepository.getAlbumTracks(album.getId());
        }

        return songLiveList;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public void setOffline(boolean offline) {
        isOffline = offline;
    }

    public LiveData<Artist> getArtist() {
        return artistRepository.getArtistInfo(album.getArtistId());
    }
}
