package com.cappielloantonio.play.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.subsonic.models.Child;
import com.cappielloantonio.play.subsonic.models.SubsonicResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SongRepository {
    private static final String TAG = "SongRepository";

    private final Application application;

    public SongRepository(Application application) {
        this.application = application;
    }

    public MutableLiveData<List<Child>> getStarredSongs(boolean random, int size) {
        MutableLiveData<List<Child>> starredSongs = new MutableLiveData<>();

        App.getSubsonicClientInstance(application, false)
                .getAlbumSongListClient()
                .getStarred2()
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<SubsonicResponse> call, @NonNull Response<SubsonicResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getStarred2() != null) {
                            List<Child> songs = response.body().getStarred2().getSongs();

                            if (!random) {
                                starredSongs.setValue(songs);
                            } else {
                                Collections.shuffle(songs);
                                starredSongs.setValue(songs.subList(0, Math.min(size, songs.size())));
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<SubsonicResponse> call, @NonNull Throwable t) {

                    }
                });

        return starredSongs;
    }

    public MutableLiveData<List<Child>> getInstantMix(Child song, int count) {
        MutableLiveData<List<Child>> instantMix = new MutableLiveData<>();

        App.getSubsonicClientInstance(application, false)
                .getBrowsingClient()
                .getSimilarSongs2(song.getId(), count)
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<SubsonicResponse> call, @NonNull Response<SubsonicResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getSimilarSongs2() != null) {
                            instantMix.setValue(response.body().getSimilarSongs2().getSongs());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<SubsonicResponse> call, @NonNull Throwable t) {
                        instantMix.setValue(null);
                    }
                });

        return instantMix;
    }

    public MutableLiveData<List<Child>> getRandomSample(int number, Integer fromYear, Integer toYear) {
        MutableLiveData<List<Child>> randomSongsSample = new MutableLiveData<>();

        App.getSubsonicClientInstance(application, false)
                .getAlbumSongListClient()
                .getRandomSongs(number, fromYear, toYear)
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<SubsonicResponse> call, @NonNull Response<SubsonicResponse> response) {
                        List<Child> songs = new ArrayList<>();

                        if (response.isSuccessful() && response.body() != null && response.body().getRandomSongs() != null) {
                            songs.addAll(response.body().getRandomSongs().getSongs());
                        }

                        randomSongsSample.setValue(songs);
                    }

                    @Override
                    public void onFailure(@NonNull Call<SubsonicResponse> call, @NonNull Throwable t) {

                    }
                });

        return randomSongsSample;
    }

    public void scrobble(String id) {
        App.getSubsonicClientInstance(application, false)
                .getMediaAnnotationClient()
                .scrobble(id)
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<SubsonicResponse> call, @NonNull Response<SubsonicResponse> response) {

                    }

                    @Override
                    public void onFailure(@NonNull Call<SubsonicResponse> call, @NonNull Throwable t) {

                    }
                });
    }

    public void star(String id) {
        App.getSubsonicClientInstance(application, false)
                .getMediaAnnotationClient()
                .star(id, null, null)
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<SubsonicResponse> call, @NonNull Response<SubsonicResponse> response) {

                    }

                    @Override
                    public void onFailure(@NonNull Call<SubsonicResponse> call, @NonNull Throwable t) {

                    }
                });
    }

    public void unstar(String id) {
        App.getSubsonicClientInstance(application, false)
                .getMediaAnnotationClient()
                .unstar(id, null, null)
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<SubsonicResponse> call, @NonNull Response<SubsonicResponse> response) {

                    }

                    @Override
                    public void onFailure(@NonNull Call<SubsonicResponse> call, @NonNull Throwable t) {

                    }
                });
    }

    public void setRating(String id, int rating) {
        App.getSubsonicClientInstance(application, false)
                .getMediaAnnotationClient()
                .setRating(id, rating)
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<SubsonicResponse> call, @NonNull Response<SubsonicResponse> response) {

                    }

                    @Override
                    public void onFailure(@NonNull Call<SubsonicResponse> call, @NonNull Throwable t) {

                    }
                });
    }

    public MutableLiveData<List<Child>> getSongsByGenre(String id) {
        MutableLiveData<List<Child>> songsByGenre = new MutableLiveData<>();

        App.getSubsonicClientInstance(application, false)
                .getAlbumSongListClient()
                .getSongsByGenre(id, 500, 0)
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<SubsonicResponse> call, @NonNull Response<SubsonicResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getSongsByGenre() != null) {
                            List<Child> newSongs = response.body().getSongsByGenre().getSongs();
                            List<Child> songs = songsByGenre.getValue();

                            if (songs == null) songs = new ArrayList<>();
                            songs.addAll(newSongs);
                            Collections.shuffle(songs);

                            LinkedHashSet<Child> hashSet = new LinkedHashSet<>(songs);
                            ArrayList<Child> songsWithoutDuplicates = new ArrayList<>(hashSet);

                            songsByGenre.setValue(songsWithoutDuplicates);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<SubsonicResponse> call, @NonNull Throwable t) {

                    }
                });

        return songsByGenre;
    }

    public MutableLiveData<List<Child>> getSongsByGenres(ArrayList<String> genresId) {
        MutableLiveData<List<Child>> songsByGenre = new MutableLiveData<>();

        for (String id : genresId)
            App.getSubsonicClientInstance(application, false)
                    .getAlbumSongListClient()
                    .getSongsByGenre(id, 500, 0)
                    .enqueue(new Callback<SubsonicResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<SubsonicResponse> call, @NonNull Response<SubsonicResponse> response) {
                            List<Child> songs = new ArrayList<>();

                            if (response.isSuccessful() && response.body() != null && response.body().getSongsByGenre() != null) {
                                songs.addAll(response.body().getSongsByGenre().getSongs());
                            }

                            songsByGenre.setValue(songs);
                        }

                        @Override
                        public void onFailure(@NonNull Call<SubsonicResponse> call, @NonNull Throwable t) {

                        }
                    });

        return songsByGenre;
    }

    public MutableLiveData<Child> getSong(String id) {
        MutableLiveData<Child> song = new MutableLiveData<>();

        App.getSubsonicClientInstance(application, false)
                .getBrowsingClient()
                .getSong(id)
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<SubsonicResponse> call, @NonNull Response<SubsonicResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            song.setValue(response.body().getSong());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<SubsonicResponse> call, @NonNull Throwable t) {

                    }
                });

        return song;
    }

    public MutableLiveData<String> getSongLyrics(Child song) {
        MutableLiveData<String> lyrics = new MutableLiveData<>(null);

        App.getSubsonicClientInstance(application, false)
                .getMediaRetrievalClient()
                .getLyrics(song.getArtist(), song.getTitle())
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<SubsonicResponse> call, @NonNull Response<SubsonicResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getLyrics() != null) {
                            lyrics.setValue(response.body().getLyrics().getContent());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<SubsonicResponse> call, @NonNull Throwable t) {

                    }
                });

        return lyrics;
    }
}
