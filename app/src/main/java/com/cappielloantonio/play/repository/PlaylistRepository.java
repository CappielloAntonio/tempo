package com.cappielloantonio.play.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.model.Playlist;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.subsonic.models.SubsonicResponse;
import com.cappielloantonio.play.util.MappingUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaylistRepository {
    private final Application application;

    public PlaylistRepository(Application application) {
        this.application = application;
    }

    public MutableLiveData<List<Playlist>> getPlaylists(boolean random, int size) {
        MutableLiveData<List<Playlist>> listLivePlaylists = new MutableLiveData<>();

        App.getSubsonicClientInstance(application, false)
                .getPlaylistClient()
                .getPlaylists()
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<SubsonicResponse> call, @NonNull Response<SubsonicResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getPlaylists() != null) {
                            List<Playlist> playlists = new ArrayList<>(MappingUtil.mapPlaylist(response.body().getPlaylists().getPlaylists()));
                            if (random) {
                                Collections.shuffle(playlists);
                                listLivePlaylists.setValue(playlists.subList(0, Math.min(playlists.size(), size)));
                            } else {
                                listLivePlaylists.setValue(playlists);
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<SubsonicResponse> call, @NonNull Throwable t) {
                    }
                });

        return listLivePlaylists;
    }

    public MutableLiveData<List<Song>> getPlaylistSongs(String id) {
        MutableLiveData<List<Song>> listLivePlaylistSongs = new MutableLiveData<>();

        App.getSubsonicClientInstance(application, false)
                .getPlaylistClient()
                .getPlaylist(id)
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<SubsonicResponse> call, @NonNull Response<SubsonicResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getPlaylist() != null) {
                            List<Song> songs = new ArrayList<>(MappingUtil.mapSong(response.body().getPlaylist().getEntries()));
                            listLivePlaylistSongs.setValue(songs);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<SubsonicResponse> call, @NonNull Throwable t) {
                    }
                });

        return listLivePlaylistSongs;
    }

    public void addSongToPlaylist(String playlistId, ArrayList<String> songsId) {
        App.getSubsonicClientInstance(application, false)
                .getPlaylistClient()
                .updatePlaylist(playlistId, null, true, songsId, null)
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<SubsonicResponse> call, @NonNull Response<SubsonicResponse> response) {

                    }

                    @Override
                    public void onFailure(@NonNull Call<SubsonicResponse> call, @NonNull Throwable t) {

                    }
                });
    }

    public void createPlaylist(String playlistId, String name, ArrayList<String> songsId) {
        App.getSubsonicClientInstance(application, false)
                .getPlaylistClient()
                .createPlaylist(playlistId, name, songsId)
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<SubsonicResponse> call, @NonNull Response<SubsonicResponse> response) {

                    }

                    @Override
                    public void onFailure(@NonNull Call<SubsonicResponse> call, @NonNull Throwable t) {

                    }
                });
    }

    public void updatePlaylist(String playlistId, String name, boolean isPublic, ArrayList<String> songIdToAdd, ArrayList<Integer> songIndexToRemove) {
        App.getSubsonicClientInstance(application, false)
                .getPlaylistClient()
                .updatePlaylist(playlistId, name, isPublic, songIdToAdd, songIndexToRemove)
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<SubsonicResponse> call, @NonNull Response<SubsonicResponse> response) {

                    }

                    @Override
                    public void onFailure(@NonNull Call<SubsonicResponse> call, @NonNull Throwable t) {

                    }
                });
    }

    public void deletePlaylist(String playlistId) {
        App.getSubsonicClientInstance(application, false)
                .getPlaylistClient()
                .deletePlaylist(playlistId)
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<SubsonicResponse> call, @NonNull Response<SubsonicResponse> response) {

                    }

                    @Override
                    public void onFailure(@NonNull Call<SubsonicResponse> call, @NonNull Throwable t) {

                    }
                });
    }
}
