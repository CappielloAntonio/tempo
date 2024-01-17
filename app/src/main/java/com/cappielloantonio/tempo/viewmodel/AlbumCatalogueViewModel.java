package com.cappielloantonio.tempo.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cappielloantonio.tempo.App;
import com.cappielloantonio.tempo.interfaces.MediaCallback;
import com.cappielloantonio.tempo.subsonic.base.ApiResponse;
import com.cappielloantonio.tempo.subsonic.models.AlbumID3;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class AlbumCatalogueViewModel extends AndroidViewModel {
    private final MutableLiveData<List<AlbumID3>> albumList = new MutableLiveData<>(new ArrayList<>());

    private int page = 0;
    private Status status = Status.STOPPED;

    public AlbumCatalogueViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<AlbumID3>> getAlbumList() {
        return albumList;
    }

    public void loadAlbums() {
        page = 0;
        albumList.setValue(new ArrayList<>());
        albumList.setValue(new ArrayList<>());
        status = Status.RUNNING;
        loadAlbums(500);
    }

    public void stopLoading() {
        status = Status.STOPPED;
    }

    private void loadAlbums(int size) {
        retrieveAlbums(new MediaCallback() {
            @Override
            public void onError(Exception exception) {
            }

            @Override
            public void onLoadMedia(List<?> media) {
                if (status == Status.STOPPED) {
                    return;
                }

                List<AlbumID3> liveAlbum = albumList.getValue();

                liveAlbum.addAll((List<AlbumID3>) media);
                albumList.setValue(liveAlbum);

                if (media.size() == size) {
                    loadAlbums(size);
                }
            }
        }, size, size * page++);
    }


    private void retrieveAlbums(MediaCallback callback, int size, int offset) {
        App.getSubsonicClientInstance(false)
                .getAlbumSongListClient()
                .getAlbumList2("alphabeticalByName", size, offset, null, null)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull retrofit2.Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getSubsonicResponse().getAlbumList2() != null && response.body().getSubsonicResponse().getAlbumList2().getAlbums() != null) {
                            List<AlbumID3> albumList = new ArrayList<>(response.body().getSubsonicResponse().getAlbumList2().getAlbums());
                            callback.onLoadMedia(albumList);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                        callback.onError(new Exception(t.getMessage()));
                    }
                });
    }

    private enum Status {
        RUNNING,
        STOPPED
    }
}
