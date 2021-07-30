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

import java.util.ArrayList;
import java.util.List;

public class ArtistListPageViewModel extends AndroidViewModel {
    private ArtistRepository artistRepository;

    public String title;

    private MutableLiveData<List<Artist>> artistList;

    public ArtistListPageViewModel(@NonNull Application application) {
        super(application);

        artistRepository = new ArtistRepository(application);
    }

    public LiveData<List<Artist>> getArtistList() {
        artistList = new MutableLiveData<>(new ArrayList<>());

        switch (title) {
            case Song.STARRED:
                artistList = artistRepository.getStarredArtists();
                break;
        }

        return artistList;
    }
}
