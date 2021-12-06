package com.cappielloantonio.play.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.model.Download;
import com.cappielloantonio.play.repository.ArtistRepository;
import com.cappielloantonio.play.repository.DownloadRepository;
import com.cappielloantonio.play.util.MappingUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class ArtistListPageViewModel extends AndroidViewModel {
    private final ArtistRepository artistRepository;
    private final DownloadRepository downloadRepository;

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
                artistList = artistRepository.getStarredArtists(false, -1);
                break;
            case Artist.DOWNLOADED:
                downloadRepository.getLiveDownload().observe(activity, downloads -> {
                    List<Download> unique = downloads
                            .stream()
                            .collect(Collectors.collectingAndThen(
                                    Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Download::getArtistName))), ArrayList::new)
                            );

                    artistList.setValue(MappingUtil.mapDownloadToArtist(unique));
                });
                break;
        }

        return artistList;
    }
}
