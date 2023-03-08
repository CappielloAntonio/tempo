package com.cappielloantonio.play.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.subsonic.base.ApiResponse;
import com.cappielloantonio.play.subsonic.models.AlbumID3;
import com.cappielloantonio.play.subsonic.models.ArtistID3;
import com.cappielloantonio.play.subsonic.models.ArtistInfo2;
import com.cappielloantonio.play.subsonic.models.Child;
import com.cappielloantonio.play.subsonic.models.IndexID3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArtistRepository {
    private final Application application;

    public ArtistRepository(Application application) {
        this.application = application;
    }

    public MutableLiveData<List<ArtistID3>> getStarredArtists(boolean random, int size) {
        MutableLiveData<List<ArtistID3>> starredArtists = new MutableLiveData<>();

        App.getSubsonicClientInstance(application, false)
                .getAlbumSongListClient()
                .getStarred2()
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getSubsonicResponse().getStarred2() != null) {
                            List<ArtistID3> artists = response.body().getSubsonicResponse().getStarred2().getArtists();

                            if (!random) {
                                getArtistInfo(artists, starredArtists);
                            } else {
                                Collections.shuffle(artists);
                                getArtistInfo(artists.subList(0, Math.min(size, artists.size())), starredArtists);
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

        App.getSubsonicClientInstance(application, false)
                .getBrowsingClient()
                .getArtists()
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<ArtistID3> artists = new ArrayList<>();

                            for (IndexID3 index : response.body().getSubsonicResponse().getArtists().getIndices()) {
                                artists.addAll(index.getArtists());
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
            App.getSubsonicClientInstance(application, false)
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

        App.getSubsonicClientInstance(application, false)
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

    /*
     * Metodo che mi restituisce le informazioni complete dell'artista (bio, immagini prese da last.fm, artisti simili...)
     */
    public MutableLiveData<ArtistInfo2> getArtistFullInfo(String id) {
        MutableLiveData<ArtistInfo2> artistFullInfo = new MutableLiveData<>();

        App.getSubsonicClientInstance(application, false)
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

    public void star(String id) {
        App.getSubsonicClientInstance(application, false)
                .getMediaAnnotationClient()
                .star(null, null, id)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {

                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {

                    }
                });
    }

    public void unstar(String id) {
        App.getSubsonicClientInstance(application, false)
                .getMediaAnnotationClient()
                .unstar(null, null, id)
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
        App.getSubsonicClientInstance(application, false)
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

        App.getSubsonicClientInstance(application, false)
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

        App.getSubsonicClientInstance(application, false)
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

    public MutableLiveData<ArrayList<Child>> getArtistRandomSong(LifecycleOwner owner, ArtistID3 artist, int count) {
        MutableLiveData<ArrayList<Child>> randomSongs = new MutableLiveData<>();

        App.getSubsonicClientInstance(application, false)
                .getBrowsingClient()
                .getArtist(artist.getId())
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getSubsonicResponse().getArtist() != null && response.body().getSubsonicResponse().getArtist().getAlbums() != null) {
                            List<AlbumID3> albums = response.body().getSubsonicResponse().getArtist().getAlbums();

                            if (albums.size() > 0) {
                                AlbumRepository albumRepository = new AlbumRepository(App.getInstance());

                                for (int index = 0; index < albums.size(); index++) {
                                    albumRepository.getAlbumTracks(albums.get(index).getId()).observe(owner, songs -> {
                                        ArrayList<Child> liveSongs = randomSongs.getValue();
                                        if (liveSongs == null) liveSongs = new ArrayList<>();
                                        Collections.shuffle(liveSongs);
                                        liveSongs.addAll(songs);
                                        randomSongs.setValue(liveSongs);
                                    });
                                }
                            }
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

        App.getSubsonicClientInstance(application, false)
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
