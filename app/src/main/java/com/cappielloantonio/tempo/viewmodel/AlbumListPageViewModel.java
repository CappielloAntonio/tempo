package com.cappielloantonio.tempo.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.tempo.repository.AlbumRepository;
import com.cappielloantonio.tempo.repository.DownloadRepository;
import com.cappielloantonio.tempo.subsonic.models.AlbumID3;
import com.cappielloantonio.tempo.subsonic.models.ArtistID3;
import com.cappielloantonio.tempo.util.Constants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

public class AlbumListPageViewModel extends AndroidViewModel {
    private final AlbumRepository albumRepository;
    private final DownloadRepository downloadRepository;

    public String title;
    public ArtistID3 artist;

    private MutableLiveData<List<AlbumID3>> albumList;

    public int maxNumber = 500;

    public AlbumListPageViewModel(@NonNull Application application) {
        super(application);

        albumRepository = new AlbumRepository();
        downloadRepository = new DownloadRepository();
    }

    public LiveData<List<AlbumID3>> getAlbumList(LifecycleOwner owner) {
        albumList = new MutableLiveData<>(new ArrayList<>());

        switch (title) {
            case Constants.ALBUM_RECENTLY_PLAYED:
                albumRepository.getAlbums("recent", maxNumber, null, null).observe(owner, albums -> albumList.setValue(albums));
                break;
            case Constants.ALBUM_MOST_PLAYED:
                albumRepository.getAlbums("frequent", maxNumber, null, null).observe(owner, albums -> albumList.setValue(albums));
                break;
            case Constants.ALBUM_RECENTLY_ADDED:
                albumRepository.getAlbums("newest", maxNumber, null, null).observe(owner, albums -> albumList.setValue(albums));
                break;
            case Constants.ALBUM_STARRED:
                albumList = albumRepository.getStarredAlbums(false, -1);
                break;
            case Constants.ALBUM_NEW_RELEASES:
                int currentYear = Calendar.getInstance().get(Calendar.YEAR);
                albumRepository.getAlbums("byYear", maxNumber, currentYear, currentYear).observe(owner, albums -> {
                    albums.sort(Comparator.comparing(AlbumID3::getCreated).reversed());
                    albumList.postValue(albums.subList(0, Math.min(20, albums.size())));
                });
                break;
        }

        return albumList;
    }
}
