package com.cappielloantonio.play.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.play.App;
import com.cappielloantonio.play.interfaces.DecadesCallback;
import com.cappielloantonio.play.interfaces.MediaCallback;
import com.cappielloantonio.play.subsonic.models.AlbumID3;
import com.cappielloantonio.play.subsonic.models.Child;
import com.cappielloantonio.play.subsonic.models.SubsonicResponse;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlbumRepository {
    private static final String TAG = "AlbumRepository";

    private final Application application;

    public AlbumRepository(Application application) {
        this.application = application;
    }

    public MutableLiveData<List<AlbumID3>> getAlbums(String type, int size, Integer fromYear, Integer toYear) {
        MutableLiveData<List<AlbumID3>> listLiveAlbums = new MutableLiveData<>(new ArrayList<>());

        App.getSubsonicClientInstance(application, false)
                .getAlbumSongListClient()
                .getAlbumList2(type, size, 0, fromYear, toYear)
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<SubsonicResponse> call, @NonNull Response<SubsonicResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getAlbumList2() != null) {
                            listLiveAlbums.setValue(response.body().getAlbumList2().getAlbums());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<SubsonicResponse> call, @NonNull Throwable t) {

                    }
                });

        return listLiveAlbums;
    }

    public MutableLiveData<List<AlbumID3>> getStarredAlbums(boolean random, int size) {
        MutableLiveData<List<AlbumID3>> starredAlbums = new MutableLiveData<>(new ArrayList<>());

        App.getSubsonicClientInstance(application, false)
                .getAlbumSongListClient()
                .getStarred2()
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<SubsonicResponse> call, @NonNull Response<SubsonicResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getStarred2() != null) {
                            List<AlbumID3> albums = response.body().getStarred2().getAlbums();

                            if (random) {
                                Collections.shuffle(albums);
                                starredAlbums.setValue(albums.subList(0, Math.min(size, albums.size())));
                            } else {
                                starredAlbums.setValue(albums);
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<SubsonicResponse> call, @NonNull Throwable t) {

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
                .unstar(null, id, null)
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

    public MutableLiveData<List<Child>> getAlbumTracks(String id) {
        MutableLiveData<List<Child>> albumTracks = new MutableLiveData<>();

        App.getSubsonicClientInstance(application, false)
                .getBrowsingClient()
                .getAlbum(id)
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<SubsonicResponse> call, @NonNull Response<SubsonicResponse> response) {
                        List<Child> tracks = new ArrayList<>();

                        if (response.isSuccessful() && response.body() != null && response.body().getAlbum() != null) {
                            tracks.addAll(response.body().getAlbum().getSongs());
                        }

                        albumTracks.setValue(tracks);
                    }

                    @Override
                    public void onFailure(@NonNull Call<SubsonicResponse> call, @NonNull Throwable t) {

                    }
                });

        return albumTracks;
    }

    public MutableLiveData<List<AlbumID3>> getArtistAlbums(String id) {
        MutableLiveData<List<AlbumID3>> artistsAlbum = new MutableLiveData<>(new ArrayList<>());

        App.getSubsonicClientInstance(application, false)
                .getBrowsingClient()
                .getArtist(id)
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<SubsonicResponse> call, @NonNull Response<SubsonicResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getArtist() != null) {
                            List<AlbumID3> albums = response.body().getArtist().getAlbums();
                            albums.sort(Comparator.comparing(AlbumID3::getYear));
                            artistsAlbum.setValue(albums);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<SubsonicResponse> call, @NonNull Throwable t) {

                    }
                });

        return artistsAlbum;
    }

    public MutableLiveData<AlbumID3> getAlbum(String id) {
        MutableLiveData<AlbumID3> album = new MutableLiveData<>();

        App.getSubsonicClientInstance(application, false)
                .getBrowsingClient()
                .getAlbum(id)
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<SubsonicResponse> call, @NonNull Response<SubsonicResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getAlbum() != null) {
                            album.setValue(response.body().getAlbum());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<SubsonicResponse> call, @NonNull Throwable t) {

                    }
                });

        return album;
    }

    public void getInstantMix(AlbumID3 album, int count, MediaCallback callback) {
        App.getSubsonicClientInstance(application, false)
                .getBrowsingClient()
                .getSimilarSongs2(album.getId(), count)
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<SubsonicResponse> call, @NonNull Response<SubsonicResponse> response) {
                        List<Child> songs = new ArrayList<>();

                        if (response.isSuccessful() && response.body() != null && response.body().getSimilarSongs2() != null) {
                            songs.addAll(response.body().getSimilarSongs2().getSongs());
                        }

                        callback.onLoadMedia(songs);
                    }

                    @Override
                    public void onFailure(@NonNull Call<SubsonicResponse> call, @NonNull Throwable t) {
                        callback.onLoadMedia(new ArrayList<>());
                    }
                });
    }

    public MutableLiveData<List<Integer>> getDecades() {
        MutableLiveData<List<Integer>> decades = new MutableLiveData<>();

        getFirstAlbum(first -> getLastAlbum(last -> {
            if (first != -1 && last != -1) {
                List<Integer> decadeList = new ArrayList();

                int startDecade = first - (first % 10);
                int lastDecade = last - (last % 10);

                while (startDecade <= lastDecade) {
                    decadeList.add(startDecade);
                    startDecade = startDecade + 10;
                }

                decades.setValue(decadeList);
            }
        }));

        return decades;
    }

    private void getFirstAlbum(DecadesCallback callback) {
        App.getSubsonicClientInstance(application, false)
                .getAlbumSongListClient()
                .getAlbumList2("byYear", 1, 0, 1900, Calendar.getInstance().get(Calendar.YEAR))
                .enqueue(new Callback<SubsonicResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<SubsonicResponse> call, @NonNull Response<SubsonicResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getAlbumList2() != null && response.body().getAlbumList2().getAlbums() != null && !response.body().getAlbumList2().getAlbums().isEmpty()) {
                            callback.onLoadYear(response.body().getAlbumList2().getAlbums().get(0).getYear());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<SubsonicResponse> call, @NonNull Throwable t) {
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
                    public void onResponse(@NonNull Call<SubsonicResponse> call, @NonNull Response<SubsonicResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getAlbumList2() != null) {
                            if (response.body().getAlbumList2().getAlbums().size() > 0 && !response.body().getAlbumList2().getAlbums().isEmpty()) {
                                callback.onLoadYear(response.body().getAlbumList2().getAlbums().get(0).getYear());
                            } else {
                                callback.onLoadYear(-1);
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<SubsonicResponse> call, @NonNull Throwable t) {
                        callback.onLoadYear(-1);
                    }
                });
    }
}
