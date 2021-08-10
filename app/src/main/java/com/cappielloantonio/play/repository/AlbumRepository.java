package com.cappielloantonio.play.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.interfaces.DecadesCallback;
import com.cappielloantonio.play.interfaces.MediaCallback;
import com.cappielloantonio.play.model.Album;
import com.cappielloantonio.play.model.Song;
import com.cappielloantonio.play.subsonic.models.ResponseStatus;
import com.cappielloantonio.play.subsonic.models.SubsonicResponse;
import com.cappielloantonio.play.util.MappingUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlbumRepository {
    private static final String TAG = "AlbumRepository";

    private Application application;

    public AlbumRepository(Application application) {
        this.application = application;
    }

    public MutableLiveData<List<Album>> getAlbums(String type, int size) {
        MutableLiveData<List<Album>> listLiveAlbums = new MutableLiveData<>();

        App.getSubsonicClientInstance(application, false)
                .getAlbumSongListClient()
                .getAlbumList2(type, size, 0, null, null)
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(Call<SubsonicResponse> call, Response<SubsonicResponse> response) {
                        List<Album> albums = new ArrayList<>(MappingUtil.mapAlbum(response.body().getAlbumList2().getAlbums()));
                        listLiveAlbums.setValue(albums);
                    }

                    @Override
                    public void onFailure(Call<SubsonicResponse> call, Throwable t) {

                    }
                });

        return listLiveAlbums;
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

    public MutableLiveData<List<Integer>> getDecades() {
        MutableLiveData<List<Integer>> decades = new MutableLiveData<>();

        getFirstAlbum(first -> {
            getLastAlbum(last -> {
                if(first != -1 && last != -1) {
                    List<Integer> decadeList = new ArrayList();

                    int startDecade = first - (first % 10);
                    int lastDecade = last - (last % 10);

                    while (startDecade <= lastDecade) {
                        decadeList.add(startDecade);
                        startDecade = startDecade + 10;
                    }

                    decades.setValue(decadeList);
                }
            });
        });

        return decades;
    }

    private void getFirstAlbum(DecadesCallback callback) {
        App.getSubsonicClientInstance(application, false)
                .getAlbumSongListClient()
                .getAlbumList2("byYear", 1, 0, 1900, Calendar.getInstance().get(Calendar.YEAR))
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(Call<SubsonicResponse> call, Response<SubsonicResponse> response) {
                        if (response.body().getStatus().getValue().equals(ResponseStatus.OK)) {
                            if(response.body().getAlbumList2().getAlbums().get(0) != null){
                                callback.onLoadYear(response.body().getAlbumList2().getAlbums().get(0).getYear());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SubsonicResponse> call, Throwable t) {
                        callback.onLoadYear(-1);
                    }
                });
    }

    private void getLastAlbum(DecadesCallback callback) {
        App.getSubsonicClientInstance(application, false)
                .getAlbumSongListClient()
                .getAlbumList2("byYear", 1, 0, Calendar.getInstance().get(Calendar.YEAR), 1900)
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(Call<SubsonicResponse> call, Response<SubsonicResponse> response) {
                        if (response.body().getStatus().getValue().equals(ResponseStatus.OK)) {
                            if(response.body().getAlbumList2().getAlbums().size() > 0 && response.body().getAlbumList2().getAlbums().get(0) != null){
                                callback.onLoadYear(response.body().getAlbumList2().getAlbums().get(0).getYear());
                            }
                            else {
                                callback.onLoadYear(-1);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SubsonicResponse> call, Throwable t) {
                        callback.onLoadYear(-1);
                    }
                });
    }
}
