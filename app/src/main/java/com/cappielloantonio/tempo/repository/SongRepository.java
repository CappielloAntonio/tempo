package com.cappielloantonio.tempo.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.tempo.App;
import com.cappielloantonio.tempo.database.AppDatabase;
import com.cappielloantonio.tempo.database.dao.FavoriteDao;
import com.cappielloantonio.tempo.subsonic.base.ApiResponse;
import com.cappielloantonio.tempo.subsonic.models.Child;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SongRepository {
    private static final String TAG = "SongRepository";

    public MutableLiveData<List<Child>> getStarredSongs(boolean random, int size) {
        MutableLiveData<List<Child>> starredSongs = new MutableLiveData<>(Collections.emptyList());

        App.getSubsonicClientInstance(false)
                .getAlbumSongListClient()
                .getStarred2()
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getSubsonicResponse().getStarred2() != null) {
                            List<Child> songs = response.body().getSubsonicResponse().getStarred2().getSongs();

                            if (songs != null) {
                                if (!random) {
                                    starredSongs.setValue(songs);
                                } else {
                                    Collections.shuffle(songs);
                                    starredSongs.setValue(songs.subList(0, Math.min(size, songs.size())));
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {

                    }
                });

        return starredSongs;
    }

    public MutableLiveData<List<Child>> getInstantMix(Child song, int count) {
        MutableLiveData<List<Child>> instantMix = new MutableLiveData<>();

        App.getSubsonicClientInstance(false)
                .getBrowsingClient()
                .getSimilarSongs2(song.getId(), count)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getSubsonicResponse().getSimilarSongs2() != null) {
                            instantMix.setValue(response.body().getSubsonicResponse().getSimilarSongs2().getSongs());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                        instantMix.setValue(null);
                    }
                });

        return instantMix;
    }

    public MutableLiveData<List<Child>> getRandomSample(int number, Integer fromYear, Integer toYear) {
        MutableLiveData<List<Child>> randomSongsSample = new MutableLiveData<>();

        App.getSubsonicClientInstance(false)
                .getAlbumSongListClient()
                .getRandomSongs(number, fromYear, toYear)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                        List<Child> songs = new ArrayList<>();

                        if (response.isSuccessful() && response.body() != null && response.body().getSubsonicResponse().getRandomSongs() != null && response.body().getSubsonicResponse().getRandomSongs().getSongs() != null) {
                            songs.addAll(response.body().getSubsonicResponse().getRandomSongs().getSongs());
                        }

                        randomSongsSample.setValue(songs);
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                        Log.d(TAG, "onFailure: ");
                    }
                });

        return randomSongsSample;
    }

    public void scrobble(String id) {
        App.getSubsonicClientInstance(false)
                .getMediaAnnotationClient()
                .scrobble(id)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {

                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {

                    }
                });
    }

    public void setRating(String id, int rating) {
        App.getSubsonicClientInstance(false)
                .getMediaAnnotationClient()
                .setRating(id, rating)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {

                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {

                    }
                });
    }

    public MutableLiveData<List<Child>> getSongsByGenre(String id, int page) {
        MutableLiveData<List<Child>> songsByGenre = new MutableLiveData<>();

        App.getSubsonicClientInstance(false)
                .getAlbumSongListClient()
                .getSongsByGenre(id, 100, 100 * page)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getSubsonicResponse().getSongsByGenre() != null) {
                            songsByGenre.setValue(response.body().getSubsonicResponse().getSongsByGenre().getSongs());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {

                    }
                });

        return songsByGenre;
    }

    public MutableLiveData<List<Child>> getSongsByGenres(ArrayList<String> genresId) {
        MutableLiveData<List<Child>> songsByGenre = new MutableLiveData<>();

        for (String id : genresId)
            App.getSubsonicClientInstance(false)
                    .getAlbumSongListClient()
                    .getSongsByGenre(id, 500, 0)
                    .enqueue(new Callback<ApiResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                            List<Child> songs = new ArrayList<>();

                            if (response.isSuccessful() && response.body() != null && response.body().getSubsonicResponse().getSongsByGenre() != null) {
                                songs.addAll(response.body().getSubsonicResponse().getSongsByGenre().getSongs());
                            }

                            songsByGenre.setValue(songs);
                        }

                        @Override
                        public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {

                        }
                    });

        return songsByGenre;
    }

    public MutableLiveData<Child> getSong(String id) {
        MutableLiveData<Child> song = new MutableLiveData<>();

        App.getSubsonicClientInstance(false)
                .getBrowsingClient()
                .getSong(id)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            song.setValue(response.body().getSubsonicResponse().getSong());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                        Log.d(TAG, "onFailure: ");
                    }
                });

        return song;
    }

    public MutableLiveData<String> getSongLyrics(Child song) {
        MutableLiveData<String> lyrics = new MutableLiveData<>(null);

        App.getSubsonicClientInstance(false)
                .getMediaRetrievalClient()
                .getLyrics(song.getArtist(), song.getTitle())
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getSubsonicResponse().getLyrics() != null) {
                            lyrics.setValue(response.body().getSubsonicResponse().getLyrics().getValue());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {

                    }
                });

        return lyrics;
    }
}
