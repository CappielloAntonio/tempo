package com.cappielloantonio.play.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.play.model.Album;
import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.model.Download;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.repository.ArtistRepository;
import com.cappielloantonio.play.repository.DownloadRepository;
import com.cappielloantonio.play.util.MappingUtil;

import java.util.ArrayList;
import java.util.List;

public class ArtistListPageViewModel extends AndroidViewModel {
    private ArtistRepository artistRepository;
    private DownloadRepository downloadRepository;

    public String title;

    private MutableLiveData<List<Artist>> artistList;

    public ArtistListPageViewModel(@NonNull Application application) {
        super(application);

        artistRepository = new ArtistRepository(application);
        downloadRepository = new DownloadRepository(application);
    }

    public LiveData<List<Artist>> getArtistList(FragmentActivity activity) {
        artistList = new MutableLiveData<>(new ArrayList<>());

        switch (title) {
            case Artist.STARRED:
                artistList = artistRepository.getStarredArtists();
                break;
            case Artist.DOWNLOADED:
                downloadRepository.getLiveDownload().observe(activity, downloads -> {
                    artistList.setValue(MappingUtil.mapDownloadToArtist(downloads));
                });
                break;
        }

        return artistList;
    }
}
