package com.cappielloantonio.play.repository;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.model.Playlist;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.subsonic.models.ResponseStatus;
import com.cappielloantonio.play.subsonic.models.SubsonicResponse;
import com.cappielloantonio.play.util.MappingUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaylistRepository {
    private Application application;

    private MutableLiveData<List<Playlist>> listLivePlaylists = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<List<Playlist>> listLiveRandomPlaylist = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<List<Song>> listLivePlaylistSongs = new MutableLiveData<>(new ArrayList<>());

    public PlaylistRepository(Application application) {
        this.application = application;
    }

    public MutableLiveData<List<Playlist>> getPlaylists(boolean random, int size) {
        App.getSubsonicClientInstance(application, false)
                .getPlaylistClient()
                .getPlaylists()
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(Call<SubsonicResponse> call, Response<SubsonicResponse> response) {
                        if (response.body().getStatus().getValue().equals(ResponseStatus.OK)) {
                            List<Playlist> playlists = new ArrayList<>(MappingUtil.mapPlaylist(response.body().getPlaylists().getPlaylists()));
                            listLivePlaylists.setValue(playlists);
                            Collections.shuffle(playlists);
                            listLiveRandomPlaylist.setValue(playlists);
                        }
                    }

                    @Override
                    public void onFailure(Call<SubsonicResponse> call, Throwable t) {
                    }
                });

        return listLivePlaylists;
    }

    public MutableLiveData<List<Song>> getPlaylistSongs(String id) {
        App.getSubsonicClientInstance(application, false)
                .getPlaylistClient()
                .getPlaylist(id)
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(Call<SubsonicResponse> call, Response<SubsonicResponse> response) {
                        if (response.body().getStatus().getValue().equals(ResponseStatus.OK)) {
                            List<Song> songs = new ArrayList<>(MappingUtil.mapSong(response.body().getPlaylist().getEntries()));
                            listLivePlaylistSongs.setValue(songs);
                        }
                    }

                    @Override
                    public void onFailure(Call<SubsonicResponse> call, Throwable t) {
                    }
                });

        return listLivePlaylistSongs;
    }
}
