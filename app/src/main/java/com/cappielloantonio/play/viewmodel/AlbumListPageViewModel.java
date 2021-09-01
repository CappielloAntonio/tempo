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
import com.cappielloantonio.play.repository.DownloadRepository;
import com.cappielloantonio.play.util.MappingUtil;

import java.util.ArrayList;
import java.util.List;

public class AlbumListPageViewModel extends AndroidViewModel {
    private AlbumRepository albumRepository;
    private DownloadRepository downloadRepository;

    public String title;
    public Artist artist;

    private MutableLiveData<List<Album>> albumList;

    public AlbumListPageViewModel(@NonNull Application application) {
        super(application);

        albumRepository = new AlbumRepository(application);
        downloadRepository = new DownloadRepository(application);
    }

    public LiveData<List<Album>> getAlbumList(FragmentActivity activity) {
        albumList = new MutableLiveData<>(new ArrayList<>());

        switch (title) {
            case Album.RECENTLY_PLAYED:
                albumRepository.getAlbums("recent", 500, null, null).observe(activity, albums -> {
                    albumList.setValue(albums);
                });
                break;
            case Album.MOST_PLAYED:
                albumRepository.getAlbums("frequent", 500, null, null).observe(activity, albums -> {
                    albumList.setValue(albums);
                });
                break;
            case Album.RECENTLY_ADDED:
                albumRepository.getAlbums("newest", 500, null, null).observe(activity, albums -> {
                    albumList.setValue(albums);
                });
                break;
            case Album.STARRED:
                albumList = albumRepository.getStarredAlbums();
                break;
            case Album.DOWNLOADED:
                downloadRepository.getLiveDownload().observe(activity, downloads -> {
                    albumList.setValue(MappingUtil.mapDownloadToAlbum(downloads));
                });
                break;
            case Album.FROM_ARTIST:
                downloadRepository.getLiveDownloadFromArtist(artist.getId()).observe(activity, downloads -> {
                    albumList.setValue(MappingUtil.mapDownloadToAlbum(downloads));
                });
                break;
        }

        return albumList;
    }
}
