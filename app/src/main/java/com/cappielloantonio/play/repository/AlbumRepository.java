package com.cappielloantonio.play.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.interfaces.MediaCallback;
import com.cappielloantonio.play.model.Album;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.subsonic.models.ResponseStatus;
import com.cappielloantonio.play.subsonic.models.SubsonicResponse;
import com.cappielloantonio.play.util.MappingUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlbumRepository {
    private static final String TAG = "AlbumRepository";

    private Application application;

    private MutableLiveData<List<Album>> listLiveRecentlyAddedAlbums = new MutableLiveData<>();
    private MutableLiveData<List<Album>> listLiveMostPlayedAlbums = new MutableLiveData<>();
    private MutableLiveData<List<Album>> listLiveRecentlyPlayedAlbums = new MutableLiveData<>();
    private MutableLiveData<List<Album>> listLiveRandomAlbums = new MutableLiveData<>();

    public AlbumRepository(Application application) {
        this.application = application;
    }

    public LiveData<List<Album>> getAlbums(String type, int size) {
        App.getSubsonicClientInstance(application, false)
                .getAlbumSongListClient()
                .getAlbumList2(type, size, 0)
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(Call<SubsonicResponse> call, Response<SubsonicResponse> response) {
                        List<Album> albums = new ArrayList<>(MappingUtil.mapAlbum(response.body().getAlbumList2().getAlbums()));

                        switch (type) {
                            case "newest":
                                listLiveRecentlyAddedAlbums.setValue(albums);
                                break;
                            case "frequent":
                                listLiveMostPlayedAlbums.setValue(albums);
                                break;
                            case "recent":
                                listLiveRecentlyPlayedAlbums.setValue(albums);
                                break;
                            case "random":
                                listLiveRandomAlbums.setValue(albums);
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Call<SubsonicResponse> call, Throwable t) {

                    }
                });

        switch (type) {
            case "newest":
                return listLiveRecentlyAddedAlbums;
            case "frequent":
                return listLiveMostPlayedAlbums;
            case "recent":
                return listLiveRecentlyPlayedAlbums;
            case "random":
                return listLiveRandomAlbums;
            default:
                return new MutableLiveData<>();
        }
    }

    public MutableLiveData<List<Album>> getStarredAlbums() {
        MutableLiveData<List<Album>> starredAlbums = new MutableLiveData<>();

        App.getSubsonicClientInstance(application, false)
                .getAlbumSongListClient()
                .getStarred2()
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(Call<SubsonicResponse> call, Response<SubsonicResponse> response) {
                        if (response.body().getStatus().getValue().equals(ResponseStatus.OK)) {
                            List<Album> albums = new ArrayList<>(MappingUtil.mapAlbum(response.body().getStarred2().getAlbums()));
                            starredAlbums.setValue(albums);
                        }
                    }

                    @Override
                    public void onFailure(Call<SubsonicResponse> call, Throwable t) {

                    }
                });

        return starredAlbums;
    }

    public void star(String id) {
        App.getSubsonicClientInstance(application, false)
                .getMediaAnnotationClient()
                .star(null, id, null)
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
                .unstar(null, id, null)
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

    public MutableLiveData<List<Song>> getAlbumTracks(String id) {
        MutableLiveData<List<Song>> albumTracks = new MutableLiveData<>();

        App.getSubsonicClientInstance(application, false)
                .getBrowsingClient()
                .getAlbum(id)
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(Call<SubsonicResponse> call, Response<SubsonicResponse> response) {
                        List<Song> tracks = new ArrayList<>(MappingUtil.mapSong(response.body().getAlbum().getSongs()));
                        albumTracks.setValue(tracks);
                    }

                    @Override
                    public void onFailure(Call<SubsonicResponse> call, Throwable t) {

                    }
                });

        return albumTracks;
    }

    public MutableLiveData<List<Album>> getArtistAlbums(String id) {
        MutableLiveData<List<Album>> artistsAlbum = new MutableLiveData<>();

        App.getSubsonicClientInstance(application, false)
                .getBrowsingClient()
                .getArtist(id)
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(Call<SubsonicResponse> call, Response<SubsonicResponse> response) {
                        if (response.body().getStatus().getValue().equals(ResponseStatus.OK)) {
                            List<Album> albums = new ArrayList<>(MappingUtil.mapAlbum(response.body().getArtist().getAlbums()));
                            artistsAlbum.setValue(albums);
                        }
                    }

                    @Override
                    public void onFailure(Call<SubsonicResponse> call, Throwable t) {

                    }
                });

        return artistsAlbum;
    }

    public MutableLiveData<Album> getAlbum(String id) {
        MutableLiveData<Album> album = new MutableLiveData<>();

        App.getSubsonicClientInstance(application, false)
                .getBrowsingClient()
                .getAlbum(id)
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(Call<SubsonicResponse> call, Response<SubsonicResponse> response) {
                        if (response.body().getStatus().getValue().equals(ResponseStatus.OK)) {
                            album.setValue(MappingUtil.mapAlbum(response.body().getAlbum()));
                        }
                    }

                    @Override
                    public void onFailure(Call<SubsonicResponse> call, Throwable t) {

                    }
                });

        return album;
    }

    public MutableLiveData<Album> getAlbumInfo(String id) {
        MutableLiveData<Album> album = new MutableLiveData<>();

        App.getSubsonicClientInstance(application, false)
                .getBrowsingClient()
                .getAlbumInfo2(id)
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(Call<SubsonicResponse> call, Response<SubsonicResponse> response) {
                        if (response.body().getStatus().getValue().equals(ResponseStatus.OK)) {
                            album.setValue(MappingUtil.mapAlbum(response.body().getAlbumInfo()));
                        }
                    }

                    @Override
                    public void onFailure(Call<SubsonicResponse> call, Throwable t) {

                    }
                });

        return album;
    }

    public void getInstantMix(Album album, int count, MediaCallback callback) {
        App.getSubsonicClientInstance(application, false)
                .getBrowsingClient()
                .getSimilarSongs2(album.getId(), count)
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
}
