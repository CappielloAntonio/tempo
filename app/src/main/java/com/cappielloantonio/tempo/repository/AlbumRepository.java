package com.cappielloantonio.tempo.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.tempo.App;
import com.cappielloantonio.tempo.interfaces.DecadesCallback;
import com.cappielloantonio.tempo.interfaces.MediaCallback;
import com.cappielloantonio.tempo.subsonic.base.ApiResponse;
import com.cappielloantonio.tempo.subsonic.models.AlbumID3;
import com.cappielloantonio.tempo.subsonic.models.Child;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlbumRepository {
    public MutableLiveData<List<AlbumID3>> getAlbums(String type, int size, Integer fromYear, Integer toYear) {
        MutableLiveData<List<AlbumID3>> listLiveAlbums = new MutableLiveData<>(new ArrayList<>());

        App.getSubsonicClientInstance(false)
                .getAlbumSongListClient()
                .getAlbumList2(type, size, 0, fromYear, toYear)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getSubsonicResponse().getAlbumList2() != null && response.body().getSubsonicResponse().getAlbumList2().getAlbums() != null) {
                            listLiveAlbums.setValue(response.body().getSubsonicResponse().getAlbumList2().getAlbums());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {

                    }
                });

        return listLiveAlbums;
    }

    public MutableLiveData<List<AlbumID3>> getStarredAlbums(boolean random, int size) {
        MutableLiveData<List<AlbumID3>> starredAlbums = new MutableLiveData<>(new ArrayList<>());

        App.getSubsonicClientInstance(false)
                .getAlbumSongListClient()
                .getStarred2()
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getSubsonicResponse().getStarred2() != null) {
                            List<AlbumID3> albums = response.body().getSubsonicResponse().getStarred2().getAlbums();

                            if (albums != null) {
                                if (random) {
                                    Collections.shuffle(albums);
                                    starredAlbums.setValue(albums.subList(0, Math.min(size, albums.size())));
                                } else {
                                    starredAlbums.setValue(albums);
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {

                    }
                });

        return starredAlbums;
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

    public MutableLiveData<List<Child>> getAlbumTracks(String id) {
        MutableLiveData<List<Child>> albumTracks = new MutableLiveData<>();

        App.getSubsonicClientInstance(false)
                .getBrowsingClient()
                .getAlbum(id)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                        List<Child> tracks = new ArrayList<>();

                        if (response.isSuccessful() && response.body() != null && response.body().getSubsonicResponse().getAlbum() != null) {
                            tracks.addAll(response.body().getSubsonicResponse().getAlbum().getSongs());
                        }

                        albumTracks.setValue(tracks);
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {

                    }
                });

        return albumTracks;
    }

    public MutableLiveData<List<AlbumID3>> getArtistAlbums(String id) {
        MutableLiveData<List<AlbumID3>> artistsAlbum = new MutableLiveData<>(new ArrayList<>());

        App.getSubsonicClientInstance(false)
                .getBrowsingClient()
                .getArtist(id)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getSubsonicResponse().getArtist() != null) {
                            List<AlbumID3> albums = response.body().getSubsonicResponse().getArtist().getAlbums();
                            albums.sort(Comparator.comparing(AlbumID3::getYear));
                            artistsAlbum.setValue(albums);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {

                    }
                });

        return artistsAlbum;
    }

    public MutableLiveData<AlbumID3> getAlbum(String id) {
        MutableLiveData<AlbumID3> album = new MutableLiveData<>();

        App.getSubsonicClientInstance(false)
                .getBrowsingClient()
                .getAlbum(id)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getSubsonicResponse().getAlbum() != null) {
                            album.setValue(response.body().getSubsonicResponse().getAlbum());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {

                    }
                });

        return album;
    }

    public void getInstantMix(AlbumID3 album, int count, MediaCallback callback) {
        App.getSubsonicClientInstance(false)
                .getBrowsingClient()
                .getSimilarSongs2(album.getId(), count)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                        List<Child> songs = new ArrayList<>();

                        if (response.isSuccessful() && response.body() != null && response.body().getSubsonicResponse().getSimilarSongs2() != null) {
                            songs.addAll(response.body().getSubsonicResponse().getSimilarSongs2().getSongs());
                        }

                        callback.onLoadMedia(songs);
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                        callback.onLoadMedia(new ArrayList<>());
                    }
                });
    }

    public MutableLiveData<List<Integer>> getDecades() {
        MutableLiveData<List<Integer>> decades = new MutableLiveData<>();

        getFirstAlbum(new DecadesCallback() {
            @Override
            public void onLoadYear(int first) {
                getLastAlbum(new DecadesCallback() {
                    @Override
                    public void onLoadYear(int last) {
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
                    }
                });
            }
        });

        return decades;
    }

    private void getFirstAlbum(DecadesCallback callback) {
        App.getSubsonicClientInstance(false)
                .getAlbumSongListClient()
                .getAlbumList2("byYear", 1, 0, 1900, Calendar.getInstance().get(Calendar.YEAR))
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getSubsonicResponse().getAlbumList2() != null && response.body().getSubsonicResponse().getAlbumList2().getAlbums() != null && !response.body().getSubsonicResponse().getAlbumList2().getAlbums().isEmpty()) {
                            callback.onLoadYear(response.body().getSubsonicResponse().getAlbumList2().getAlbums().get(0).getYear());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                        callback.onLoadYear(-1);
                    }
                });
    }

    private void getLastAlbum(DecadesCallback callback) {
        App.getSubsonicClientInstance(false)
                .getAlbumSongListClient()
                .getAlbumList2("byYear", 1, 0, Calendar.getInstance().get(Calendar.YEAR), 1900)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getSubsonicResponse().getAlbumList2() != null) {
                            if (response.body().getSubsonicResponse().getAlbumList2().getAlbums().size() > 0 && !response.body().getSubsonicResponse().getAlbumList2().getAlbums().isEmpty()) {
                                callback.onLoadYear(response.body().getSubsonicResponse().getAlbumList2().getAlbums().get(0).getYear());
                            } else {
                                callback.onLoadYear(-1);
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                        callback.onLoadYear(-1);
                    }
                });
    }
}
