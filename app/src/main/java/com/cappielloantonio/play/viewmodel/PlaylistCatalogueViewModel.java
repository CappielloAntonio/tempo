package com.cappielloantonio.play.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.model.Playlist;
import com.cappielloantonio.play.repository.PlaylistRepository;
import com.cappielloantonio.play.subsonic.models.ResponseStatus;
import com.cappielloantonio.play.subsonic.models.SubsonicResponse;
import com.cappielloantonio.play.util.MappingUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class PlaylistCatalogueViewModel extends AndroidViewModel {
    private MutableLiveData<List<Playlist>> playlists;

    private PlaylistRepository playlistRepository;
    private String query = "";

    public PlaylistCatalogueViewModel(@NonNull Application application) {
        super(application);

        playlistRepository = new PlaylistRepository(application);

        playlists = playlistRepository.getPlaylists(false, -1);
    }

    public LiveData<List<Playlist>> getPlaylistList() {
        return playlists;
    }

}
