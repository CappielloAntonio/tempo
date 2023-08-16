package com.cappielloantonio.tempo.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.tempo.App;
import com.cappielloantonio.tempo.subsonic.base.ApiResponse;
import com.cappielloantonio.tempo.subsonic.models.ArtistID3;
import com.cappielloantonio.tempo.subsonic.models.ArtistInfo2;
import com.cappielloantonio.tempo.subsonic.models.Child;
import com.cappielloantonio.tempo.subsonic.models.IndexID3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArtistRepository {
    public MutableLiveData<List<ArtistID3>> getStarredArtists(boolean random, int size) {
        MutableLiveData<List<ArtistID3>> starredArtists = new MutableLiveData<>(new ArrayList<>());

        App.getSubsonicClientInstance(false)
                .getAlbumSongListClient()
                .getStarred2()
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getSubsonicResponse().getStarred2() != null) {
                            List<ArtistID3> artists = response.body().getSubsonicResponse().getStarred2().getArtists();

                            if (artists != null) {
                                if (!random) {
                                    getArtistInfo(artists, starredArtists);
                                } else {
                                    Collections.shuffle(artists);
                                    getArtistInfo(artists.subList(0, Math.min(size, artists.size())), starredArtists);
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {

                    }
                });

        return starredArtists;
    }

    public MutableLiveData<List<ArtistID3>> getArtists(boolean random, int size) {
        MutableLiveData<List<ArtistID3>> listLiveArtists = new MutableLiveData<>();

        App.getSubsonicClientInstance(false)
                .getBrowsingClient()
                .getArtists()
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<ArtistID3> artists = new ArrayList<>();

                            if(response.body().getSubsonicResponse().getArtists() != null) {
                                for (IndexID3 index : response.body().getSubsonicResponse().getArtists().getIndices()) {
                                    artists.addAll(index.getArtists());
                                }
                            }

                            if (random) {
                                Collections.shuffle(artists);
                                getArtistInfo(artists.subList(0, artists.size() / size > 0 ? size : artists.size()), listLiveArtists);
                            } else {
                                listLiveArtists.setValue(artists);
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                    }
                });

        return listLiveArtists;
    }

    /*
     * Metodo che mi restituisce le informazioni essenzionali dell'artista (cover, numero di album...)
     */
    public void getArtistInfo(List<ArtistID3> artists, MutableLiveData<List<ArtistID3>> list) {
        List<ArtistID3> liveArtists = list.getValue();
        if (liveArtists == null) liveArtists = new ArrayList<>();
        list.setValue(liveArtists);

        for (ArtistID3 artist : artists) {
            App.getSubsonicClientInstance(false)
                    .getBrowsingClient()
                    .getArtist(artist.getId())
                    .enqueue(new Callback<ApiResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                            if (response.isSuccessful() && response.body() != null && response.body().getSubsonicResponse().getArtist() != null) {
                                addToMutableLiveData(list, response.body().getSubsonicResponse().getArtist());
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {

                        }
                    });
        }
    }

    public MutableLiveData<ArtistID3> getArtistInfo(String id) {
        MutableLiveData<ArtistID3> artist = new MutableLiveData<>();

        App.getSubsonicClientInstance(false)
                .getBrowsingClient()
                .getArtist(id)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getSubsonicResponse().getArtist() != null) {
                            artist.setValue(response.body().getSubsonicResponse().getArtist());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {

                    }
                });

        return artist;
    }

    public MutableLiveData<ArtistInfo2> getArtistFullInfo(String id) {
        MutableLiveData<ArtistInfo2> artistFullInfo = new MutableLiveData<>(null);

        App.getSubsonicClientInstance(false)
                .getBrowsingClient()
                .getArtistInfo2(id)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getSubsonicResponse().getArtistInfo2() != null) {
                            artistFullInfo.setValue(response.body().getSubsonicResponse().getArtistInfo2());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {

                    }
                });

        return artistFullInfo;
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

    public MutableLiveData<ArtistID3> getArtist(String id) {
        MutableLiveData<ArtistID3> artist = new MutableLiveData<>();

        App.getSubsonicClientInstance(false)
                .getBrowsingClient()
                .getArtist(id)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getSubsonicResponse().getArtist() != null) {
                            artist.setValue(response.body().getSubsonicResponse().getArtist());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {

                    }
                });

        return artist;
    }

    public MutableLiveData<List<Child>> getInstantMix(ArtistID3 artist, int count) {
        MutableLiveData<List<Child>> instantMix = new MutableLiveData<>();

        App.getSubsonicClientInstance(false)
                .getBrowsingClient()
                .getSimilarSongs2(artist.getId(), count)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getSubsonicResponse().getSimilarSongs2() != null) {
                            instantMix.setValue(response.body().getSubsonicResponse().getSimilarSongs2().getSongs());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {

                    }
                });

        return instantMix;
    }

    public MutableLiveData<List<Child>> getRandomSong(ArtistID3 artist, int count) {
        MutableLiveData<List<Child>> randomSongs = new MutableLiveData<>();

        App.getSubsonicClientInstance(false)
                .getBrowsingClient()
                .getTopSongs(artist.getName(), count)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getSubsonicResponse().getTopSongs() != null && response.body().getSubsonicResponse().getTopSongs().getSongs() != null) {
                            List<Child> songs = response.body().getSubsonicResponse().getTopSongs().getSongs();

                            if (songs != null && !songs.isEmpty()) {
                                Collections.shuffle(songs);
                            }

                            randomSongs.setValue(songs);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {

                    }
                });

        return randomSongs;
    }

    public MutableLiveData<List<Child>> getTopSongs(String artistName, int count) {
        MutableLiveData<List<Child>> topSongs = new MutableLiveData<>(new ArrayList<>());

        App.getSubsonicClientInstance(false)
                .getBrowsingClient()
                .getTopSongs(artistName, count)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getSubsonicResponse().getTopSongs() != null && response.body().getSubsonicResponse().getTopSongs().getSongs() != null) {
                            topSongs.setValue(response.body().getSubsonicResponse().getTopSongs().getSongs());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {

                    }
                });

        return topSongs;
    }

    private void addToMutableLiveData(MutableLiveData<List<ArtistID3>> liveData, ArtistID3 artist) {
        List<ArtistID3> liveArtists = liveData.getValue();
        if (liveArtists != null) liveArtists.add(artist);
        liveData.setValue(liveArtists);
    }
}
