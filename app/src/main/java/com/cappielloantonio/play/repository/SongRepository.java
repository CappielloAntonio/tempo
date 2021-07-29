package com.cappielloantonio.play.repository;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.interfaces.MediaCallback;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.subsonic.models.ResponseStatus;
import com.cappielloantonio.play.subsonic.models.SubsonicResponse;
import com.cappielloantonio.play.util.MappingUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SongRepository {
    private static final String TAG = "SongRepository";

    private Application application;

    private MutableLiveData<List<Song>> starredSongs = new MutableLiveData<>();

    public SongRepository(Application application) {
        this.application = application;
    }

    public MutableLiveData<List<Song>> getStarredSongs() {
        App.getSubsonicClientInstance(application, false)
                .getAlbumSongListClient()
                .getStarred2()
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(Call<SubsonicResponse> call, Response<SubsonicResponse> response) {
                        if (response.body().getStatus().getValue().equals(ResponseStatus.OK)) {
                            List<Song> songs = new ArrayList<>(MappingUtil.mapSong(response.body().getStarred2().getSongs()));
                            starredSongs.setValue(songs);
                        }
                    }

                    @Override
                    public void onFailure(Call<SubsonicResponse> call, Throwable t) {

                    }
                });

        return starredSongs;
    }

    public void getInstantMix(Song song, int count, MediaCallback callback) {
        App.getSubsonicClientInstance(application, false)
                .getBrowsingClient()
                .getSimilarSongs2(song.getId(), count)
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(Call<SubsonicResponse> call, Response<SubsonicResponse> response) {
                        if (response.body().getStatus().getValue().equals(ResponseStatus.OK)) {
                            List<Song> songs = new ArrayList<>(MappingUtil.mapSong(response.body().getSimilarSongs2().getSongs()));
                            if (songs.size() > 1) {
                                callback.onLoadMedia(songs);
                            } else {
                                songs.add(song);
                                callback.onLoadMedia(songs);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SubsonicResponse> call, Throwable t) {
                        List<Song> songs = new ArrayList<>();
                        songs.add(song);
                        callback.onLoadMedia(songs);
                    }
                });
    }

    public void getRandomSample(int number, MediaCallback callback) {
        App.getSubsonicClientInstance(application, false)
                .getAlbumSongListClient()
                .getRandomSongs(number)
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(Call<SubsonicResponse> call, Response<SubsonicResponse> response) {
                        List<Song> songs = new ArrayList<>();

                        if (response.body().getStatus().getValue().equals(ResponseStatus.OK)) {
                            songs = new ArrayList<>(MappingUtil.mapSong(response.body().getRandomSongs().getSongs()));
                        }

                        callback.onLoadMedia(songs);
                    }

                    @Override
                    public void onFailure(Call<SubsonicResponse> call, Throwable t) {
                        callback.onError(new Exception(t.getMessage()));
                    }
                });
    }

    public void scrobble(String id) {
        App.getSubsonicClientInstance(application, false)
                .getMediaAnnotationClient()
                .scrobble(id)
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(Call<SubsonicResponse> call, Response<SubsonicResponse> response) {

                    }

                    @Override
                    public void onFailure(Call<SubsonicResponse> call, Throwable t) {

                    }
                });
    }
}
