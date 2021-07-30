package com.cappielloantonio.play.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.play.model.Album;
import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.model.Genre;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.repository.AlbumRepository;
import com.cappielloantonio.play.repository.DownloadRepository;
import com.cappielloantonio.play.repository.SongRepository;
import com.cappielloantonio.play.util.MappingUtil;

import java.util.ArrayList;
import java.util.List;

public class AlbumListPageViewModel extends AndroidViewModel {
    private AlbumRepository albumRepository;

    public String title;

    private MutableLiveData<List<Album>> albumList;

    public AlbumListPageViewModel(@NonNull Application application) {
        super(application);

        albumRepository = new AlbumRepository(application);
    }

    public LiveData<List<Album>> getAlbumList(FragmentActivity activity) {
        albumList = new MutableLiveData<>(new ArrayList<>());

        switch (title) {
            case Song.RECENTLY_PLAYED:
                albumRepository.getAlbums("recent", 500).observe(activity, albums -> {
                    albumList.setValue(albums);
                });
                break;
            case Song.MOST_PLAYED:
                albumRepository.getAlbums("frequent", 500).observe(activity, albums -> {
                    albumList.setValue(albums);
                });
                break;
            case Song.RECENTLY_ADDED:
                albumRepository.getAlbums("newest", 500).observe(activity, albums -> {
                    albumList.setValue(albums);
                });
                break;
            case Song.STARRED:
                albumList = albumRepository.getStarredAlbums();
                break;
        }

        return albumList;
    }
}
