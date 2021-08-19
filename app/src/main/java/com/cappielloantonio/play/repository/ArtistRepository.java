package com.cappielloantonio.play.repository;

import android.app.Application;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.interfaces.MediaCallback;
import com.cappielloantonio.play.model.Album;
import com.cappielloantonio.play.model.Artist;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.subsonic.models.IndexID3;
import com.cappielloantonio.play.subsonic.models.ResponseStatus;
import com.cappielloantonio.play.subsonic.models.SubsonicResponse;
import com.cappielloantonio.play.util.MappingUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArtistRepository {
    private Application application;

    public ArtistRepository(Application application) {
        this.application = application;
    }

    public MutableLiveData<List<Artist>> getStarredArtists() {
        MutableLiveData<List<Artist>> starredArtists = new MutableLiveData<>();

        App.getSubsonicClientInstance(application, false)
                .getAlbumSongListClient()
                .getStarred2()
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(Call<SubsonicResponse> call, Response<SubsonicResponse> response) {
                        if (response.body().getStatus().getValue().equals(ResponseStatus.OK)) {
                            List<Artist> artists = new ArrayList<>(MappingUtil.mapArtist(response.body().getStarred2().getArtists()));
                            getArtistInfo(artists, starredArtists);
                        }
                    }

                    @Override
                    public void onFailure(Call<SubsonicResponse> call, Throwable t) {
                    }
                });

        return starredArtists;
    }

    public MutableLiveData<List<Artist>> getArtists(boolean random, int size) {
        MutableLiveData<List<Artist>> listLiveArtists = new MutableLiveData<>();

        App.getSubsonicClientInstance(application, false)
                .getBrowsingClient()
                .getArtists()
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(Call<SubsonicResponse> call, Response<SubsonicResponse> response) {
                        if (response.body().getStatus().getValue().equals(ResponseStatus.OK)) {
                            List<Artist> artists = new ArrayList<>();

                            for (IndexID3 index : response.body().getArtists().getIndices()) {
                                artists.addAll(MappingUtil.mapArtist(index.getArtists()));
                            }

                            if(random) {
                                Collections.shuffle(artists);
                                getArtistInfo(artists.subList(0, artists.size() / size > 0 ? size : artists.size()), listLiveArtists);
                            }
                            else {
                                listLiveArtists.setValue(artists);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SubsonicResponse> call, Throwable t) {
                    }
                });

        return listLiveArtists;
    }

    /*
     * Metodo che mi restituisce le informazioni essenzionali dell'artista (cover, numero di album...)
     */
    public void getArtistInfo(List<Artist> artists, MutableLiveData<List<Artist>> list) {
        for (Artist artist : artists) {
            App.getSubsonicClientInstance(application, false)
                    .getBrowsingClient()
                    .getArtist(artist.getId())
                    .enqueue(new Callback<SubsonicResponse>() {
                        @Override
                        public void onResponse(Call<SubsonicResponse> call, Response<SubsonicResponse> response) {
                            addToMutableLiveData(list, MappingUtil.mapArtistWithAlbum(response.body().getArtist()));
                        }

                        @Override
                        public void onFailure(Call<SubsonicResponse> call, Throwable t) {

                        }
                    });
        }
    }

    public MutableLiveData<Artist> getArtistInfo(String id) {
        MutableLiveData<Artist> artist = new MutableLiveData<>();

        App.getSubsonicClientInstance(application, false)
                .getBrowsingClient()
                .getArtist(id)
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(Call<SubsonicResponse> call, Response<SubsonicResponse> response) {
                        artist.setValue(MappingUtil.mapArtistWithAlbum(response.body().getArtist()));
                    }

                    @Override
                    public void onFailure(Call<SubsonicResponse> call, Throwable t) {

                    }
                });

        return artist;
    }

    /*
     * Metodo che mi restituisce le informazioni complete dell'artista (bio, immagini prese da last.fm, artisti simili...)
     */
    public MutableLiveData<Artist> getArtistFullInfo(String id) {
        MutableLiveData<Artist> artistFullInfo = new MutableLiveData<>();

        App.getSubsonicClientInstance(application, false)
                .getBrowsingClient()
                .getArtistInfo2(id)
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(Call<SubsonicResponse> call, Response<SubsonicResponse> response) {
                        artistFullInfo.setValue(MappingUtil.mapArtist(response.body().getArtistInfo2()));
                    }

                    @Override
                    public void onFailure(Call<SubsonicResponse> call, Throwable t) {

                    }
                });

        return artistFullInfo;
    }

    public void star(String id) {
        App.getSubsonicClientInstance(application, false)
                .getMediaAnnotationClient()
                .star(null, null, id)
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(Call<SubsonicResponse> call, Response<SubsonicResponse> response) {

                    }

                    @Override
                    public void onFailure(Call<SubsonicResponse> call, Throwable t) {

                    }
                });
    }

    public void unstar(String id) {
        App.getSubsonicClientInstance(application, false)
                .getMediaAnnotationClient()
                .unstar(null, null, id)
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(Call<SubsonicResponse> call, Response<SubsonicResponse> response) {

                    }

                    @Override
                    public void onFailure(Call<SubsonicResponse> call, Throwable t) {

                    }
                });
    }

    public void setRating(String id, int rating) {
        App.getSubsonicClientInstance(application, false)
                .getMediaAnnotationClient()
                .setRating(id, rating)
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(Call<SubsonicResponse> call, Response<SubsonicResponse> response) {

                    }

                    @Override
                    public void onFailure(Call<SubsonicResponse> call, Throwable t) {

                    }
                });
    }

    public MutableLiveData<Artist> getArtist(String id) {
        MutableLiveData<Artist> artist = new MutableLiveData<>();

        App.getSubsonicClientInstance(application, false)
                .getBrowsingClient()
                .getArtist(id)
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(Call<SubsonicResponse> call, Response<SubsonicResponse> response) {
                        if (response.body().getStatus().getValue().equals(ResponseStatus.OK)) {
                            artist.setValue(MappingUtil.mapArtist(response.body().getArtist()));
                        }
                    }

                    @Override
                    public void onFailure(Call<SubsonicResponse> call, Throwable t) {

                    }
                });

        return artist;
    }

    public void getInstantMix(Artist artist, int count, MediaCallback callback) {
        App.getSubsonicClientInstance(application, false)
                .getBrowsingClient()
                .getSimilarSongs2(artist.getId(), count)
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(Call<SubsonicResponse> call, Response<SubsonicResponse> response) {
                        if (response.body().getStatus().getValue().equals(ResponseStatus.OK)) {
                            List<Song> songs = new ArrayList<>(MappingUtil.mapSong(response.body().getSimilarSongs2().getSongs()));
                            callback.onLoadMedia(songs);
                        }
                    }

                    @Override
                    public void onFailure(Call<SubsonicResponse> call, Throwable t) {
                        callback.onLoadMedia(new ArrayList<>());
                    }
                });
    }

    public MutableLiveData<ArrayList<Song>> getArtistRandomSong(FragmentActivity fragmentActivity, Artist artist, int count) {
        MutableLiveData<ArrayList<Song>> randomSongs = new MutableLiveData<>();

        App.getSubsonicClientInstance(application, false)
                .getBrowsingClient()
                .getArtist(artist.getId())
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(Call<SubsonicResponse> call, Response<SubsonicResponse> response) {
                        if (response.body().getStatus().getValue().equals(ResponseStatus.OK)) {
                            List<Album> albums = new ArrayList<>(MappingUtil.mapAlbum(response.body().getArtist().getAlbums()));

                            if (albums.size() > 0) {
                                AlbumRepository albumRepository = new AlbumRepository(App.getInstance());

                                for (int index = 0; index < albums.size(); index++) {
                                    albumRepository.getAlbumTracks(albums.get(index).getId()).observe(fragmentActivity, songs -> {
                                        ArrayList<Song> liveSongs = randomSongs.getValue();
                                        if(liveSongs == null) liveSongs = new ArrayList<>();
                                        Collections.shuffle(liveSongs);
                                        liveSongs.addAll(songs);
                                        randomSongs.setValue(liveSongs);
                                    });
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SubsonicResponse> call, Throwable t) {

                    }
                });

        return randomSongs;
    }

    public MutableLiveData<List<Song>> getTopSongs(String artistName, int count) {
        MutableLiveData<List<Song>> topSongs = new MutableLiveData<>();

        App.getSubsonicClientInstance(application, false)
                .getBrowsingClient()
                .getTopSongs(artistName, count)
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(Call<SubsonicResponse> call, Response<SubsonicResponse> response) {
                        if (response.body().getTopSongs() != null) {
                            topSongs.setValue(MappingUtil.mapSong(response.body().getTopSongs().getSongs()));
                        }
                    }

                    @Override
                    public void onFailure(Call<SubsonicResponse> call, Throwable t) {

                    }
                });

        return topSongs;
    }

    private void addToMutableLiveData(MutableLiveData<List<Artist>> liveData, Artist artist) {
        List<Artist> liveArtists = liveData.getValue();
        if(liveArtists == null) liveArtists = new ArrayList<>();
        liveArtists.add(artist);
        liveData.setValue(liveArtists);
    }
}
