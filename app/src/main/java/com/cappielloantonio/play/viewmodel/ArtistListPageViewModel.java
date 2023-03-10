package com.cappielloantonio.play.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.model.Download;
import com.cappielloantonio.play.repository.ArtistRepository;
import com.cappielloantonio.play.repository.DownloadRepository;
import com.cappielloantonio.play.subsonic.models.ArtistID3;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class ArtistListPageViewModel extends AndroidViewModel {
    private final ArtistRepository artistRepository;
    private final DownloadRepository downloadRepository;

    public String title;

    private MutableLiveData<List<ArtistID3>> artistList;

    public ArtistListPageViewModel(@NonNull Application application) {
        super(application);

        artistRepository = new ArtistRepository();
        downloadRepository = new DownloadRepository();
    }

    public LiveData<List<ArtistID3>> getArtistList(LifecycleOwner owner) {
        artistList = new MutableLiveData<>(new ArrayList<>());

        switch (title) {
            case Artist.STARRED:
                artistList = artistRepository.getStarredArtists(false, -1);
                break;
            case Artist.DOWNLOADED:
                downloadRepository.getLiveDownload().observe(owner, downloads -> {
                    List<Download> unique = downloads
                            .stream()
                            .collect(Collectors.collectingAndThen(
                                    Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Download::getArtist))), ArrayList::new)
                            );

                    // TODO
                    // artistList.setValue(MappingUtil.mapDownloadToArtist(unique));
                });
                break;
        }

        return artistList;
    }
}
